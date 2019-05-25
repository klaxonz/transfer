package com.three.transfer.dao;

import com.three.transfer.entity.TFileLink;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TFileLinkMapper {
    int deleteByPrimaryKey(Integer fileLinkId);

    int insert(TFileLink record);

    int insertSelective(TFileLink record);

    TFileLink selectByPrimaryKey(Integer fileLinkId);

    int updateByPrimaryKeySelective(TFileLink record);

    int updateByPrimaryKey(TFileLink record);

    int deleteByFileId(Integer fileId);

    List<TFileLink> selectByFileId(Integer fileId);

    TFileLink selectNoPasswordLinkByFileId(Integer fileId);

    TFileLink selectHasPasswordLinkByFileId(Integer fileId);

    TFileLink selectByLinkAddr(String linkAddr);

    TFileLink selectByLinkAddrAndPassword(@Param("linkAddr") String linkAddr, @Param("password")String password);

}