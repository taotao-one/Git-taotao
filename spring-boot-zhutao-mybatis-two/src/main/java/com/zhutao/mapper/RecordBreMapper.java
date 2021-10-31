package com.zhutao.mapper;

import com.zhutao.bean.RecordBean;
import com.zhutao.bean.domain.PageDomain;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordBreMapper {

    RecordBean selectOne(String id);

    void deleteOne(String id);

    void insertOne(RecordBean bean);

    List<RecordBean> selectAll();

    Integer reTotal(PageDomain page);

    List<RecordBean> reFind(PageDomain page);
}
