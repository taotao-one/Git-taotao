package com.zhutao.mapper;

import com.zhutao.bean.FormBoardBean;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormBoardMapper {

    List<FormBoardBean> selectAll();
}
