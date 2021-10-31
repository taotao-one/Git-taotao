package com.zhutao.mapper;

import com.zhutao.bean.UserBean;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserMapper {
    List<UserBean> getAll();

    void inserOne(UserBean bean);

    UserBean getOne(String name);
}
