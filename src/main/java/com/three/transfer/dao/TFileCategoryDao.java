package com.three.transfer.dao;

import com.three.transfer.entity.TFileCategory;
import org.springframework.stereotype.Repository;

@Repository
public interface TFileCategoryDao {

    /**
     * 添加文件类别
     */
    int insertFileCategory(TFileCategory fileCategory);

    /**
     * 通过ID删除文件类别
     * @param fileCategoryId 文件类别id
     */
    void deleteFileCategoryById(int fileCategoryId);


    /**
     * 通过ID查找文件类别
     * @param fileCategoryId 文件类别ID
     * @return 一个文件类别对象
     */
    TFileCategory getFileCategoryById(int fileCategoryId);



}
