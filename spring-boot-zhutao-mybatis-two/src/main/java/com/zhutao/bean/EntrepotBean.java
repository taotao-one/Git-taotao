package com.zhutao.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Data
@AllArgsConstructor
public class EntrepotBean {
    private Integer id;
    private String name;
    private String artNo;
    private Integer quantity;
    private String runTime;
    private Integer flag;

    @Override
    public String toString() {
        return "EntrepotBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", artNo='" + artNo + '\'' +
                ", quantity=" + quantity +
                ", runTime=" + runTime +
                ", flag=" + flag +
                '}';
    }
}
