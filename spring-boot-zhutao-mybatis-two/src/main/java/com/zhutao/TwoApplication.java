package com.zhutao;

import com.sun.java.swing.plaf.windows.WindowsTabbedPaneUI;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@MapperScan("com.zhutao.mapper")
public class TwoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TwoApplication.class, args);

    }

}
