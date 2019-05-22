package com.three.transfer.service;


import com.three.transfer.dto.TFileExecution;
import com.three.transfer.entity.TFile;
import com.three.transfer.entity.UploadInfo;
import com.three.transfer.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface TFileService {

    /**
     * 添加文件
     * @param file 上传文件
     */
    TFileExecution addFile(MultipartFile file, User user);

    /**
     * 添加分片文件
     * @param info 分片文件信息
     * @param file 分片文件
     * @param user 请求用户
     * @return     添加文件执行情况
     */
    TFileExecution addShardFile(UploadInfo info, MultipartFile file, User user);

    /**
     * 获取用户的所有文件
     * @param user
     * @return
     */
    List<TFile> getFileList(User user);

    /**
     * 删除用户文件
     * @param fileId
     * @param user
     * @return
     */
    TFileExecution deleteFile(int fileId, User user);


    /**
     * 通过文件id获取文件
     * @param filed
     * @return
     */
    File getFile(int filed);

    /**
     * 通过文件id获取文件信息
     * @param filed
     * @return
     */
    TFile getFileByFileId(int filed);






}
