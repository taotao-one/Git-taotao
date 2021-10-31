package com.zhutao.server;


import com.zhutao.bean.EntrepotBean;
import com.zhutao.bean.domain.Page;
import com.zhutao.bean.domain.PageDomain;
import com.zhutao.mapper.EntrepotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class EntrepotService {
    @Autowired
    private EntrepotMapper entrepotMapper;

    public List<EntrepotBean> getAll(){
        return entrepotMapper.getAll();
    }

    public Page pageZ(PageDomain page){

        if(page.getPage()<1){
            page.setPage(1);
        }
        int total = entrepotMapper.total(page);

        int max = total%page.getRows()==0?total/page.getRows():total/page.getRows()+1;

        max = Math.max(1,max);
        if(page.getPage()>max){
            page.setPage(max);
        }

        page.setStart((page.getPage()-1)*page.getRows());
        return new Page(max,entrepotMapper.find(page));

    }

}
