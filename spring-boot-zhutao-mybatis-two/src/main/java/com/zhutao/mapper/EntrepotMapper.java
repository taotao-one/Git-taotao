package com.zhutao.mapper;

import com.zhutao.bean.EntrepotBean;
import com.zhutao.bean.domain.Page;
import com.zhutao.bean.domain.PageDomain;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntrepotMapper {

    List<EntrepotBean> getAll();

    Integer total(PageDomain page);

    List<EntrepotBean> find(PageDomain page);
}

