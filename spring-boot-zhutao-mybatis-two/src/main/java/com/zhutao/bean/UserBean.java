package com.zhutao.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
public class UserBean implements Serializable {
    private Long id;
    private String name;
    private String RealName;
    private String password;
    private String nowTime;
    private Integer flag;
}
