package com.three.transfer.service;


import com.three.transfer.common.ServerResponse;
import com.three.transfer.entity.TFileLink;
import com.three.transfer.vo.ShareFileVo;

public interface TFileLinkService {

    /**
     * 创建文件分享链接
     * @param fileId    文件id
     * @return          返回结果
     */
    ServerResponse<TFileLink> createFileShareLink(Integer fileId, String linkPassword);

    ServerResponse<ShareFileVo> share(String linkAddr);

    ServerResponse<ShareFileVo> confirmLinkPassword(String linkAddr, String password);

    /**
     * 通过文件id删除文件分享链接
     * @param fileId    文件id
     * @return          返回结果
     */
    ServerResponse<String> deleteFileLink(Integer fileId);

}
