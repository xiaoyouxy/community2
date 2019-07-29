package com.xiao.community.dto;

import lombok.Data;

/**
 * 浏览器传过来的
 */
@Data
public class CommentCreateDTO {
    private Long parentId;
    private String content;
    private Integer type;

}
