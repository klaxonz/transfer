package com.three.transfer.dao;


import com.three.transfer.entity.TFile;
import com.three.transfer.entity.TFileLink;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TFileLinkDaoTest {

    @Autowired
    private TFileLinkDao fileLinkDao;

    @Test
    public void testInsertLinkTest() {
        TFile file = new TFile();
        file.setFileId(131);
        TFileLink fileLink = new TFileLink();
        fileLink.setFile(file);
        fileLink.setFileLinkAccessTimes(1);
        fileLink.setFileLinkAddr("shareLink3");
        fileLink.setFileLinkPassword("1313113");
        fileLink.setLastEditTime(new Date());
        fileLink.setCreateTime(new Date());
        int res = fileLinkDao.insertLink(fileLink);
        System.out.println(fileLink.getFileLinkId());

    }

    @Test
    public void testGetLinkByLinkId() {
        TFileLink fileLink = fileLinkDao.getLinkByLinkId(1);
        System.out.println(fileLink.getFileLinkAddr());
        System.out.println(fileLink.getFile().getFileId());

    }

    @Test
    public void testGetLinkByFileId() {
        List<TFileLink> fileLink = fileLinkDao.getLinkByFileId(131);
        System.out.println(fileLink.get(0).getFileLinkAddr());
        System.out.println(fileLink.get(0).getFile().getFileId());
    }

    @Test
    public void testGetLinkByLinkName() {
        TFileLink fileLink = fileLinkDao.getLinkByLinkName("pCDVV68G8SoWU0aSkRV3Dkt");
        System.out.println(fileLink.getFileLinkId());
    }

}
