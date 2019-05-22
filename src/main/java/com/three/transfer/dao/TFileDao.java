package com.three.transfer.dao;

import com.three.transfer.entity.TFile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TFileDao {

    /**
     * 插入文件
     * @param file
     * @return
     */
    int insertFile(TFile file);

    /**
     * 通过ID删除文件
     * @return
     */
    int deleteFileById(int fileId);

    /**
     * 通过ID获取文件
     * @return
     */
    TFile getFileById(int fileId);

    /**
     * 获取用户的所有文件
     * @param userId
     * @return
     */
    List<TFile> getFilesByUserId(int userId);

    /**
     * 通过用户id和文件名查找文件
     * @param userId
     * @param fileName
     * @return
     */
    TFile getFileByNameAndUserId(int userId, String fileName);


}
