package com.zhutao.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FormBoardBean {
    private Integer id;
    private String name;
    private String coding;
    private String detailedname;
    private String barcode;
}
