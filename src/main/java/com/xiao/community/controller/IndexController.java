package com.xiao.community.controller;

import com.xiao.community.dto.PaginationDTO;
import com.xiao.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired(required = false)
    private QuestionService questionService;

    @GetMapping("/")
    public String hello(HttpServletRequest request,
                        Model model,
                        @RequestParam(name="page",defaultValue = "1") Integer page,  //当前页码     size是显示个数
                        @RequestParam(name="size",defaultValue = "5") Integer size){

        PaginationDTO pagination = questionService.findAll(page,size);
        model.addAttribute("pagination",pagination);
        return "index";
    }

}
