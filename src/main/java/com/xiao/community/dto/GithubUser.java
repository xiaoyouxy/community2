package com.xiao.community.dto;

import lombok.Data;

/**
 * GitHub用户信息
 */
@Data
public class GithubUser {

    private String name; //GitHub用户名
    private Long id;    //id
    private String bio;  //个性标签
    private String avatar_url; //头像地址

}
