package com.xiao.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页对象
 */
@Data
public class PaginationDTO<T> {
    private List<T> data; //查询到的问题 集合
    private boolean showPrevious;  //向前按钮
    private boolean showFirstPage; //直接跳到第一页的按钮
    private boolean showNext; //向后按钮
    private boolean showEndPage;  //直接跳到最后一页的按钮
    private Integer page;  //当前页
    private List<Integer> pages = new ArrayList<>();  //所有页码集合
    private Integer totalPage; //总页码数

    public void setPagination(Integer totalPage, Integer page) {

        this.totalPage = totalPage;
        this.page = page;
        pages.add(page);
        for (int i = 1; i <=3 ; i++) {
            if (page - i > 0){
                pages.add(0,page - i);
            }

            if (page + i <= totalPage){
                pages.add(page+i);
            }
        }


        //是否展示上一页
        if (page == 1){
            showPrevious = false;
        }else {
            showPrevious =true;
        }
        //是否展示下一页
        if (page == totalPage){
            showNext = false;
        }else {
            showNext = true;
        }

        //是否展示直接跳到第一页的按钮
        if (pages.contains(1)){
            showFirstPage = false;
        }else {
            showFirstPage = true;
        }
        //是否展示直接跳到最后一页的按钮
        if (pages.contains(totalPage)){
            showEndPage = false;
        }else {
            showEndPage = true;
        }
    }
}
