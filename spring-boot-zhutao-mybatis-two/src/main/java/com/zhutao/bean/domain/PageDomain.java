package com.zhutao.bean.domain;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageDomain {
    private Integer page;
    private Integer rows;
    private String name;
    private Integer start;
    private Integer flag;

    private Integer now;
    private Integer old;


}
