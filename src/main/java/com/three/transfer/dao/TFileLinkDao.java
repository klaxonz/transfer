package com.three.transfer.dao;

import com.three.transfer.entity.TFileLink;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TFileLinkDao {


    /**
     * 插入文件分享链接
     * @param link
     * @return
     */
    int insertLink(TFileLink link);

    /**
     * 通过文件分享链接获取链接
     *
     * @param linkId
     * @return
     */
    TFileLink getLinkByLinkId(int linkId);

    /**
     * 通过文件id获取到链接
     * @param fileId
     * @return
     */
    List<TFileLink> getLinkByFileId(int fileId);

    /**
     * 通过分享链接删除链接
     *
     * @param linkId
     * @return
     */
    int deleteLinkByLinkId(int linkId);

    /**
     * 通过文件id删除链接
     * @param fileId
     * @return
     */
    int deleteLinkByFileId(int fileId);

    /**
     * 通过链接名获取文件链接
     * @return
     */
    TFileLink getLinkByLinkName(String linkAddr);



}
