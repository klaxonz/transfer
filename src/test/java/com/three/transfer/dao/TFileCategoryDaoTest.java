package com.three.transfer.dao;


import com.three.transfer.entity.TFileCategory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TFileCategoryDaoTest {

    @Autowired
    private TFileCategoryDao fileCategoryDao;

    @Test
    public void testAddFileCategory() {

        TFileCategory fileCategory = new TFileCategory();
        fileCategory.setFileCategoryName("音频");
        fileCategory.setFileCategoryFormat(3);
        fileCategory.setCreateTime(new Date());

        int res = fileCategoryDao.insertFileCategory(fileCategory);
        System.out.println(res);

    }

    @Test
    public void testGetFileCategoryById() {
        TFileCategory fileCategory = fileCategoryDao.getFileCategoryById(2);
        System.out.println(fileCategory.getFileCategoryName());
    }

    @Test
    public void testDeleteFileCategoryById() {
        fileCategoryDao.deleteFileCategoryById(1);
    }


}
