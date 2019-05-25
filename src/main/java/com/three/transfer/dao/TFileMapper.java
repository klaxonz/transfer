package com.three.transfer.dao;

import com.three.transfer.entity.TFile;
import java.util.List;


public interface TFileMapper {
    int deleteByPrimaryKey(Integer fileId);

    int insert(TFile record);

    int insertSelective(TFile record);

    TFile selectByPrimaryKey(Integer fileId);

    int updateByPrimaryKeySelective(TFile record);

    int updateByPrimaryKey(TFile record);

    List<TFile> selectByUserId(Integer userId);
}