package com.three.transfer.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class User {

    private int userId;
    private String userName;
    private String userPassword;
    private String userEmail;
    private Long totalCapacity;
    private Long usedCapacity;
    private Date createTime;
    private Date lastEditTime;

}
