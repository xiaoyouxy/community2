package com.xiao.community.mapper;

import com.xiao.community.domain.Comment;

public interface CommentExtMapper {

    int incCommentCount(Comment record);
}