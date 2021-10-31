package com.zhutao.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.InputStream;

@Data
@AllArgsConstructor
public class ListDomain {
    private String id;
    private String total;
    private Integer num;
    private String productName;
    public ListDomain(String total, Integer num){
        this.total=total;
        this.num=num;
    }
}
