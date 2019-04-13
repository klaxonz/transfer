package com.three.transfer.service.impl;


import com.three.transfer.dao.TFileDao;
import com.three.transfer.dao.TFileLinkDao;
import com.three.transfer.dto.TFileLinkExecution;
import com.three.transfer.entity.TFile;
import com.three.transfer.entity.TFileLink;
import com.three.transfer.enums.TFileLinkStateEnum;
import com.three.transfer.service.TFileLinkService;
import com.three.transfer.util.ShareLinkUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;

@Service
public class TFileLinkServiceImpl implements TFileLinkService {

    @Autowired
    private TFileLinkDao fileLinkDao;
    @Autowired
    private TFileDao fileDao;


    @Override
    @Transactional
    public TFileLinkExecution addFileLink(int fileId, String linkPassword) {

        if (fileId <= -1) {
            return new TFileLinkExecution(TFileLinkStateEnum.INNER_ERROR);
        }

        TFileLinkExecution fileLinkExecution = null;
        try {
            TFileLink fileLink = new TFileLink();
            TFile file = fileDao.getFileById(fileId);
            fileLink.setCreateTime(new Date());
            fileLink.setLastEditTime(new Date());
            fileLink.setFileLinkAccessTimes(0);
            fileLink.setFile(file);
            fileLink.setFileLinkAddr(ShareLinkUtil.getRandomString(23));
            if (linkPassword != null && !"".equals(linkPassword)) {
                fileLink.setFileLinkPassword(linkPassword);
            }
            int res = fileLinkDao.insertLink(fileLink);
            if (res <= 0) {
                throw new RuntimeException("创建文件分享链接失败");
            }
            fileLink = fileLinkDao.getLinkByLinkId(fileLink.getFileLinkId());

            fileLinkExecution = new TFileLinkExecution(TFileLinkStateEnum.SUCCESS, fileLink);

        } catch (Exception e) {
            return new TFileLinkExecution(TFileLinkStateEnum.INNER_ERROR);
        }
        return fileLinkExecution;
    }

    @Override
    public TFileLink getFileLinkByLinkAddr(String linkAddr) {
        return fileLinkDao.getLinkByLinkName(linkAddr);
    }
}
