package com.zhutao.service;

import com.zhutao.domain.ListDomain;
import org.apache.poi.hssf.usermodel.*;

import java.util.List;

public class mainService {

    public static HSSFWorkbook exprt(List<ListDomain> list){
        String[] total = {"商品编码","商品名称","货号","数量"};
        HSSFWorkbook wb = new HSSFWorkbook();

        HSSFSheet sheet = wb.createSheet("汇总");

        HSSFRow row = sheet.createRow(0);

        HSSFCellStyle style = wb.createCellStyle();

        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        HSSFCell cell = null;


        for(int i = 0;i<total.length;i++){
            cell = row.createCell(i);
            cell.setCellValue(total[i]);
            cell.setCellStyle(style);
        }

        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i+1);

            row.createCell(0).setCellValue(list.get(i).getId());
            row.createCell(1).setCellValue(list.get(i).getProductName());
            row.createCell(2).setCellValue(list.get(i).getTotal());
            row.createCell(3).setCellValue(list.get(i).getNum());

        }


        return wb;
    }
}
