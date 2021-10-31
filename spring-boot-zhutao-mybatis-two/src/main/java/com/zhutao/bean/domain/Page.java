package com.zhutao.bean.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class Page implements Serializable {
    private Integer max;
    private List<?> list;
}
