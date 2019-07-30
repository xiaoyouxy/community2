package com.xiao.community.controller;

import com.xiao.community.dto.CommentDTO;
import com.xiao.community.dto.QuestionDTO;
import com.xiao.community.enums.CommentTypeEnum;
import com.xiao.community.service.CommentService;
import com.xiao.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

/**
 * 问题详情页
 */
@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable("id") Long id,
                           Model model){

        QuestionDTO questionDTO = questionService.findById(id);
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);
        List<CommentDTO> comments = commentService.findByTargetId(id, CommentTypeEnum.Question);

        questionService.incView(id);
        model.addAttribute("question",questionDTO);
        model.addAttribute("comments",comments);
        model.addAttribute("relatedQuestions",relatedQuestions);
        return "question";
    }


}
