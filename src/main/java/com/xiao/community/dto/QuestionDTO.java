package com.xiao.community.dto;

import com.xiao.community.domain.User;
import lombok.Data;

import java.io.Serializable;

/**
 * 问题和发布者
 */
@Data
public class QuestionDTO implements Serializable {
    private Integer id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private User user;
}
