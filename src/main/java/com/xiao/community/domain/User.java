package com.xiao.community.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户实体类
 */
@Data
public class User implements Serializable {
    private Integer id;
    private String name;
    private String accountId;
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
    private String avatarUrl;
}
