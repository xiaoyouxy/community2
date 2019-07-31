package com.xiao.community.dto;

import com.xiao.community.domain.User;
import lombok.Data;

@Data
public class CommentDTO {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;  //评论发布的用户id
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private String content; //评论内容
    private Integer commentCount; //评论数量
    private User user; //评论发布的用户信息
}
