package com.three.transfer.service.impl;


import com.three.transfer.common.Const;
import com.three.transfer.common.ResponseCode;
import com.three.transfer.common.ServerResponse;
import com.three.transfer.dao.TFileLinkMapper;
import com.three.transfer.dao.TFileMapper;
import com.three.transfer.entity.TFile;
import com.three.transfer.entity.TFileLink;
import com.three.transfer.service.TFileLinkService;
import com.three.transfer.util.DateTimeUtil;
import com.three.transfer.util.PropertiesUtil;
import com.three.transfer.util.ShareLinkUtil;
import com.three.transfer.vo.ShareFileVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TFileLinkServiceImpl implements TFileLinkService {

    @Autowired
    private TFileMapper fileMapper;
    @Autowired
    private TFileLinkMapper fileLinkMapper;



    @Override
    @Transactional
    public ServerResponse<TFileLink> createFileShareLink(Integer fileId, String linkPassword) {
        if (fileId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEAGEL_ARGUMENT.getCode(), "请求参数非法");
        }
        TFile file = fileMapper.selectByPrimaryKey(fileId);
        if (file == null) {
            return ServerResponse.createByErrorMessage("文件不存在，创建分享链接失败");
        }
        //分享链接没有链接密码
        if (linkPassword == null) {
            //查看数据库是否已经为该链接创建未加密分享链接
            TFileLink fileLink = fileLinkMapper.selectNoPasswordLinkByFileId(fileId);
            if (fileLink != null) {
                return ServerResponse.createBySuccess("获取链接成功", fileLink);
            }
            //创建无密码分享链接
            fileLink = assembleFileShareLink(fileId, linkPassword);
            int rowCount = fileLinkMapper.insert(fileLink);
            if (rowCount == 0) {
                return ServerResponse.createByErrorMessage("创建分享链接失败");
            }
            return ServerResponse.createBySuccess("创建分享链接成功", fileLink);
        } else {
            //分享链接有密码
            //查看数据库是否已经为该链接创建已加密分享链接
            TFileLink fileLink = fileLinkMapper.selectHasPasswordLinkByFileId(fileId);
            if (fileLink != null) {
                //重置分享链接密码
                fileLink.setFileLinkPassword(linkPassword);
                int rowCount = fileLinkMapper.updateByPrimaryKeySelective(fileLink);
                if (rowCount == 0) {
                    return ServerResponse.createByErrorMessage("创建分享链接失败");
                }
                return ServerResponse.createBySuccess("创建分享链接成功", fileLink);
            }
            //数据库没有，新建分享链接
            fileLink = assembleFileShareLink(fileId, linkPassword);
            int rowCount = fileLinkMapper.insert(fileLink);
            if (rowCount == 0) {
                return ServerResponse.createByErrorMessage("创建分享链接失败");
            }
            fileLink = fileLinkMapper.selectHasPasswordLinkByFileId(fileId);
            return ServerResponse.createBySuccess("创建分享链接成功", fileLink);
        }
    }

    private TFileLink assembleFileShareLink(Integer fileId, String password) {
        TFileLink fileLink = new TFileLink();
        fileLink.setFileId(fileId);
        fileLink.setFileLinkAccessTimes(0);
        fileLink.setFileLinkPassword(password);
        fileLink.setFileLinkAddr(ShareLinkUtil.generateShareLink());
        fileLink.setFileLinkValidTime(System.currentTimeMillis() + Const.Link.LINK_VALID_TIME);
        return fileLink;
    }

    @Override
    public ServerResponse<ShareFileVo> share(String linkAddr) {
        linkAddr = PropertiesUtil.getProperty("share.link.prefix") + linkAddr.trim();
        TFileLink fileLink = fileLinkMapper.selectByLinkAddr(linkAddr);
        if (fileLink == null) {
            return ServerResponse.createByErrorMessage("链接不存在");
        }
        Long fileLinkValidTime = fileLink.getFileLinkValidTime();
        if (fileLinkValidTime < System.currentTimeMillis()) {
            return ServerResponse.createByErrorMessage("链接已过期");
        }
        TFile file = fileMapper.selectByPrimaryKey(fileLink.getFileId());
        ShareFileVo shareFileVo = assembleShareFileVo(file, fileLink);
        return ServerResponse.createBySuccess(shareFileVo);
    }

    private ShareFileVo assembleShareFileVo(TFile file, TFileLink fileLink) {
        ShareFileVo shareFileVo = new ShareFileVo();
        shareFileVo.setFileId(file.getFileId());
        shareFileVo.setFileName(file.getFileName());
        shareFileVo.setFileSize(file.getFileSize());
        shareFileVo.setHasPassword(fileLink.getFileLinkPassword() != null);
        shareFileVo.setFileValidTime(DateTimeUtil.dateToStr(file.getFileValidTime()));
        return shareFileVo;
    }

    @Override
    public ServerResponse<ShareFileVo> confirmLinkPassword(String linkAddr, String password) {
        TFileLink fileLink = fileLinkMapper.selectByLinkAddr(linkAddr);
        if (fileLink == null) {
            return ServerResponse.createByErrorMessage("链接已过期或删除");
        }
        fileLink = fileLinkMapper.selectByLinkAddrAndPassword(linkAddr, password);
        if (fileLink == null) {
            return ServerResponse.createByErrorMessage("分享码错误");
        }
        TFile file = fileMapper.selectByPrimaryKey(fileLink.getFileId());
        ShareFileVo shareFileVo = assembleShareFileVo(file, fileLink);
        return ServerResponse.createBySuccess(shareFileVo);
    }


    @Override
    public ServerResponse<String> deleteFileLink(Integer fileId){
        if (fileId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEAGEL_ARGUMENT.getCode(), "文件id不能为空");
        }
        int result = fileLinkMapper.deleteByFileId(fileId);
        if (result == 0) {
            return ServerResponse.createByErrorMessage("文件分享连接删除失败");
        }
        return ServerResponse.createBySuccessMessage("文件分享链接删除成功");
    }

}
