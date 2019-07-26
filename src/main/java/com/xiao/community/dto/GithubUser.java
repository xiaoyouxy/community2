package com.xiao.community.dto;

import lombok.Data;

@Data
public class GithubUser {

    private String name;
    private Long id;
    private String bio;  //个性标签
    private String avatar_url;

}
