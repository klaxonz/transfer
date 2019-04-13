package com.three.transfer.dao;


import com.three.transfer.entity.TFile;
import com.three.transfer.entity.TFileCategory;
import com.three.transfer.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TFileDaoTest {

    @Autowired
    private TFileDao fileDao;

    @Test
    public void testInsertFile() {
        TFile file = new TFile();
        TFileCategory fileCategory = new TFileCategory();
        User user = new User();
        user.setUserId(21);
        file.setUser(user);
        fileCategory.setFileCategoryId(2);
        file.setFileCategory(fileCategory);
        file.setFileName("Java多线程并发并发编程");
        file.setFilePath("/upload/user1/file1");
        file.setCreateTime(new Date());
        file.setLastEditTime(new Date());
        fileDao.insertFile(file);
    }

    @Test
    public void testGetFileById() {
        TFile file = fileDao.getFileById(1);
        System.out.println(file.getFileName());
    }

    @Test
    public void testDeleteFileById() {
        int res = fileDao.deleteFileById(1);
        System.out.println(res);
    }

    @Test
    public void testGetFileByUserId() {
        List<TFile> fileList = fileDao.getFilesByUserId(13);
        System.out.println(fileList.size());
    }



}
