package com.zhutao.server;

import com.google.gson.Gson;
import com.zhutao.bean.UserBean;
import com.zhutao.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper dao;

    private Gson gson = new Gson();

    public List<UserBean> getList(){
        return dao.getAll();
    }

    public UserBean getOne(UserBean user){
        UserBean dbUser = dao.getOne(user.getName());
        if(dbUser.getPassword().equals(user.getPassword())&&dbUser.getFlag()==0){

            return dbUser;
        }else {

            return null;
        }
    }

    public String insert(UserBean user){

        try{
            dao.inserOne(user);
            return "成功";
        }catch (Exception e){
            return "失败";
        }
    }

}
