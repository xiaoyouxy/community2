package com.xiao.community.controller;

import com.xiao.community.dto.PaginationDTO;
import com.xiao.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired(required = false)
    private QuestionService questionService;

    @GetMapping("/")
    public String hello(Model model,
                        @RequestParam(name="page",defaultValue = "1") Integer page,  //当前页码     size是显示个数
                        @RequestParam(name="size",defaultValue = "5") Integer size,
                        @RequestParam(name = "search",required = false) String serach ){

        PaginationDTO pagination = null;
        if (StringUtils.isBlank(serach)){
            pagination = questionService.findAll(page,size);
        } else {
            pagination = questionService.findAll(serach,page,size);
        }

        model.addAttribute("pagination",pagination);
        model.addAttribute("serach",serach);
        return "index";
    }

}
