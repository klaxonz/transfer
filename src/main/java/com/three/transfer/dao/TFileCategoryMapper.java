package com.three.transfer.dao;

import com.three.transfer.entity.TFileCategory;

public interface TFileCategoryMapper {
    int deleteByPrimaryKey(Integer fileCategoryId);

    int insert(TFileCategory record);

    int insertSelective(TFileCategory record);

    TFileCategory selectByPrimaryKey(Integer fileCategoryId);

    int updateByPrimaryKeySelective(TFileCategory record);

    int updateByPrimaryKey(TFileCategory record);
}