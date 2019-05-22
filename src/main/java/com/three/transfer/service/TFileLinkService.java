package com.three.transfer.service;


import com.three.transfer.dto.TFileLinkExecution;
import com.three.transfer.entity.TFileLink;

public interface TFileLinkService {

    /**
     * 创建文件分享链接
     * @param fileId
     * @return
     */
    TFileLinkExecution addFileLink(int fileId, String linkPassword);

    /**
     * 通过链接分享地址获取文件分享链接对象
     * @param linkAddr
     * @return
     */
    TFileLink getFileLinkByLinkAddr(String linkAddr);


    /**
     * 通过文件Id删除该文件的分享链接
    * @param fileId
     * @return
     */
    TFileLinkExecution deleteFileLink(int fileId);

}
