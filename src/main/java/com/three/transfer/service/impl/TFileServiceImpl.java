package com.three.transfer.service.impl;

import com.three.transfer.bo.UploadFileBo;
import com.three.transfer.common.Const;
import com.three.transfer.common.ResponseCode;
import com.three.transfer.common.ServerResponse;
import com.three.transfer.dao.*;
import com.three.transfer.entity.*;
import com.three.transfer.service.TFileLinkService;
import com.three.transfer.service.TFileService;
import com.three.transfer.util.FileUtil;
import com.three.transfer.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class TFileServiceImpl implements TFileService {


    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TFileMapper fileMapper;
    @Autowired
    private TFileLinkMapper fileLinkMapper;

    @Autowired
    private TFileLinkService fileLinkService;


    @Override
    @Transactional
    public ServerResponse<String> uploadFile(UploadFileBo uploadFileBo,MultipartFile file, User user) throws Exception {

        //判断用户存储目录是否存在，如果不存在就创建
        String tempDirName = PathUtil.getFilePath(user.getUserId());
        String tempDirFullPath = FileUtil.makeDirPath(tempDirName);
        if (tempDirFullPath == null) {
            return ServerResponse.createByErrorMessage("上传文件失败");
        }
        //保存文件
        boolean result = FileUtil.saveFile(tempDirFullPath, uploadFileBo.getName(), file);
        if (!result) {
            return ServerResponse.createByErrorMessage("上传文件失败");
        }
        //更新数据库
        user.setUsedCapacity(user.getUsedCapacity() + uploadFileBo.getSize());
        int rowCount = userMapper.updateByPrimaryKeySelective(user);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage("上传文件失败");
        }
        TFile tFile = assembleFile(uploadFileBo, user);
        rowCount = fileMapper.insert(tFile);
        if (rowCount == 0) {
            return ServerResponse.createByErrorMessage("上传文件失败");
        }
        return ServerResponse.createBySuccessCodeMessage(ResponseCode.FILE_UPLOAD_SUCCESS.getCode(),
                ResponseCode.FILE_UPLOAD_SUCCESS.getMsg());
    }


    @Override
    @Transactional
    public ServerResponse<String> uploadShardFile(UploadFileBo uploadFileBo, MultipartFile file, User user) throws Exception {
        //判断以guid命名的目录是否存在，如果不存在就创建
        String tempDirName = PathUtil.getTempShardFilePath(user.getUserId(), uploadFileBo.getGuid());
        String tempDirFullPath = FileUtil.makeDirPath(tempDirName);
        if (tempDirFullPath == null) {
            return ServerResponse.createByErrorMessage("上传文件失败");
        }

        //重命名分片文件，将其保存保存在临时目录中
        //命名规则 chunk + "_" + "file" + "." + ext  如：1_file.pdf
        String shardFileName = uploadFileBo.getChunk() + "_" + "file" + "." + uploadFileBo.getExt();
        boolean result = FileUtil.saveFile(tempDirFullPath, shardFileName, file);
        if (!result) {
            return ServerResponse.createByErrorMessage("上传文件失败");
        }
        //存储分片文件md5值
        FileUtil.storeMd5(uploadFileBo);
        //判断所有分片是否上传完成，如果所有分片文件已经上传完成，则进行合并
        if (FileUtil.isAllUploaded(uploadFileBo.getMd5(), uploadFileBo.getChunks())) {
            result = FileUtil.mergeFile(uploadFileBo.getChunks(), uploadFileBo.getName(), PathUtil.getFileBasePath() + PathUtil.getFilePath(user.getUserId()),
                    tempDirFullPath, uploadFileBo.getExt());
            if (!result) {
                return ServerResponse.createByErrorMessage("上传文件失败");
            }
            //更新数据库
            user.setUsedCapacity(user.getUsedCapacity() + uploadFileBo.getSize());
            int rowCount = userMapper.updateByPrimaryKeySelective(user);
            if (rowCount == 0) {
                return ServerResponse.createByErrorMessage("上传文件失败");
            }
            TFile tFile = assembleFile(uploadFileBo, user);
            rowCount = fileMapper.insert(tFile);
            if (rowCount == 0) {
                return ServerResponse.createByErrorMessage("上传文件失败");
            }
            return ServerResponse.createBySuccessCodeMessage(ResponseCode.FILE_UPLOAD_SUCCESS.getCode(),
                    ResponseCode.FILE_UPLOAD_SUCCESS.getMsg());
        }
        return ServerResponse.createBySuccessMessage("分片文件上传成功");
    }


    private TFile assembleFile(UploadFileBo uploadFileBo, User user) {
        TFile file = new TFile();
        file.setFileName(uploadFileBo.getName());
        file.setUserId(user.getUserId());
        file.setFileCategoryId(2);
        file.setFileSize(uploadFileBo.getSize());
        file.setFileDownloadTime(0);
        file.setFilePath(PathUtil.getFilePath(user.getUserId()));
        Date validTime = new Date(System.currentTimeMillis() + Const.File.FILE_VAILD_TIME);
        file.setFileValidTime(validTime);
        return file;
    }

    @Override
    public ServerResponse<List<TFile>> getFileList(User user) {
        List<TFile> fileList = fileMapper.selectByUserId(user.getUserId());
        return ServerResponse.createBySuccess("获取文件列表成功",fileList);
    }

    @Override
    @Transactional
    public ServerResponse<String> deleteFile(Integer fileId, User user) {
        //删除本地存储的文件
        TFile file = fileMapper.selectByPrimaryKey(fileId);
        String filePath = PathUtil.getFileBasePath() + file.getFilePath() + file.getFileName();
        boolean result = FileUtil.deleteFile(filePath);
        if (!result) {
            return ServerResponse.createByErrorMessage("文件删除失败");
        }
        //如果文件有分享链接，则删除
        List<TFileLink> fileLinkList = fileLinkMapper.selectByFileId(fileId);
        if (fileLinkList.size() > 0) {
            for (TFileLink fileLinkItem : fileLinkList) {
                int res = fileLinkMapper.deleteByPrimaryKey(fileLinkItem.getFileLinkId());
                if (res == 0) {
                    return ServerResponse.createByErrorMessage("文件删除失败");
                }
            }
        }
        //更新数据库文件信息
        int res = fileMapper.deleteByPrimaryKey(fileId);
        if (res == 0) {
            return ServerResponse.createByErrorMessage("文件删除失败");
        }
        //更新数据库用户信息
        user.setUsedCapacity(user.getUsedCapacity() - file.getFileSize());
        res = userMapper.updateByPrimaryKeySelective(user);
        if (res == 0) {
            return ServerResponse.createByErrorMessage("文件删除失败");
        }
        return ServerResponse.createBySuccessMessage("删除文件成功");
    }

    @Override
    @Transactional
    public ServerResponse<String> downloadFile(Integer fileId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (fileId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEAGEL_ARGUMENT.getCode(), "请求参数非法");
        }
        File file = getFile(fileId);
        if (file == null) {
            return ServerResponse.createByErrorMessage("请求文件不存在");
        }
        FileUtil.download(file,request,response);
        //更新数据库
        TFile tFile = fileMapper.selectByPrimaryKey(fileId);
        tFile.setFileDownloadTime(tFile.getFileDownloadTime() + 1);
        return ServerResponse.createBySuccess("下载文件成功");
    }


    @Override
    public File getFile(int filed) {
        TFile fileInfo = fileMapper.selectByPrimaryKey(filed);
        String filePath = PathUtil.getFileBasePath() + fileInfo.getFilePath() + fileInfo.getFileName();
        File file = new File(filePath);
        return file.exists() ? file : null;
    }

}
