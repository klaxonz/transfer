package com.three.transfer.service;


import com.three.transfer.bo.UploadFileBo;
import com.three.transfer.common.ServerResponse;
import com.three.transfer.entity.TFile;
import com.three.transfer.entity.User;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

public interface TFileService {

    /**
     * 上传单个文件
     * @param uploadFileBo  上传文件信息
     * @param file          上传文件对象
     * @param user          上传用户
     * @return              上传结果
     * @throws Exception    上传文件出现的异常
     */
    ServerResponse<String>  uploadFile(UploadFileBo uploadFileBo, MultipartFile file, User user) throws Exception;


    /**
     * 上传分片文件
     * @param uploadFileBo  上传分片文件信息
     * @param file          上传分片文件对象
     * @param user          上传用户
     * @return              上传结果
     * @throws Exception    上传文件出现的异常
     */
    ServerResponse<String> uploadShardFile(UploadFileBo uploadFileBo, MultipartFile file, User user) throws Exception;

    /**
     * 获取用户已上传的文件列表
     * @param user          用户对象
     * @return              返回List集合
     */
    ServerResponse<List<TFile>> getFileList(User user);

    /**
     * 删除用户文件
     * @param fileId    文件id
     * @param user      文件所属用户
     * @return          返回结果
     */
    ServerResponse<String> deleteFile(Integer fileId, User user);

    /**
     * 下载文件
     * @param fileId        文件id
     * @param request       请求对象
     * @param response      响应对象
     * @return              返回结果
     * @throws IOException  文件处理异常
     */
    ServerResponse<String> downloadFile(Integer fileId, HttpServletRequest request, HttpServletResponse response) throws IOException;


    /**
     * 通过文件id获取文件，转化成File对象
     * @param filed     文件id
     * @return          File对象
     */
    File getFile(int filed);


}
