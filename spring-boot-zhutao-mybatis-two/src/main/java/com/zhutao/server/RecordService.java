package com.zhutao.server;

import com.zhutao.bean.FormBoardBean;
import com.zhutao.bean.RecordBean;
import com.zhutao.bean.domain.ImplDomain;
import com.zhutao.bean.domain.ImplSerDomain;
import com.zhutao.bean.domain.Page;
import com.zhutao.bean.domain.PageDomain;
import com.zhutao.mapper.FormBoardMapper;
import com.zhutao.mapper.RecordBreMapper;
import com.zhutao.mapper.RecordMapper;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RecordService {

    @Autowired
    private FormBoardMapper formBoardMapper;
    @Autowired
    private RecordMapper recordMapper;


    public void implSer(MultipartFile file) {

        try {
            InputStream is = file.getInputStream();
            Workbook workbook = WorkbookFactory.create(is);

            Sheet sheet = workbook.getSheetAt(0);
            List<RecordBean> list = sheets(sheet);
            for (RecordBean recordBean : list) {
                recordMapper.insertOne(recordBean);
            }



        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public Page pageZ(PageDomain page){
        if(page.getPage()<1){
            page.setPage(1);
        }
        int total = recordMapper.reTotal(page);

        int max = total%page.getRows()==0?total/page.getRows():total/page.getRows()+1;

        max = Math.max(1,max);
        if(page.getPage()>max){
            page.setPage(max);
        }

        page.setStart((page.getPage()-1)*page.getRows());
        return new Page(max,recordMapper.reFind(page));

    }

    public  HSSFWorkbook exprt(PageDomain page){

        if(page.getPage()<1){
            page.setPage(1);
        }
        int total = recordMapper.reTotal(page);

        int max = total%page.getRows()==0?total/page.getRows():total/page.getRows()+1;

        max = Math.max(1,max);
        if(page.getPage()>max){
            page.setPage(max);
        }

        page.setStart((page.getPage()-1)*page.getRows());

        List<RecordBean> list = recordMapper.reFind(page);


        String[] totals = {"货号","编码","商品名称","条形码","数量","导入时间"};
        HSSFWorkbook wb = new HSSFWorkbook();

        HSSFSheet sheet = wb.createSheet("汇总");

        HSSFRow row = sheet.createRow(0);

        HSSFCellStyle style = wb.createCellStyle();

        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        HSSFCell cell = null;


        for(int i = 0;i<totals.length;i++){
            cell = row.createCell(i);
            cell.setCellValue(totals[i]);
            cell.setCellStyle(style);
        }

        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i+1);

            row.createCell(0).setCellValue(list.get(i).getName());
            row.createCell(1).setCellValue(list.get(i).getCoding());
            row.createCell(2).setCellValue(list.get(i).getDetailedname());
            row.createCell(3).setCellValue(list.get(i).getBarcode());
            row.createCell(4).setCellValue(list.get(i).getQuantity());
            row.createCell(4).setCellValue(list.get(i).getAlterTime());
        }


        return wb;
    }


    private List<RecordBean> sheets(Sheet sheet){
        List<ImplSerDomain> list = new ArrayList<>();

        for (int a = 1; a <= sheet.getLastRowNum(); a++) {


            Row row = sheet.getRow(a);

            Cell c1 = row.getCell(19);
            Cell c2 = row.getCell(20);

            list.add(new ImplSerDomain(getTypeValue(c1).toString(),Integer.parseInt(getTypeValue(c2).toString().split("\\.")[0])));
        }
        List<ImplSerDomain> servic =service(list);


        return mapping(servic);
    }

    private Object getTypeValue(Cell cell){
        if(null==cell){
            return null;
        }
        switch (cell.getCellType()){
            case Cell.CELL_TYPE_NUMERIC:return cell.getNumericCellValue();
            case Cell.CELL_TYPE_STRING:return cell.getStringCellValue();
            default:return "null";
        }

    }



    private List<ImplSerDomain> service(List<ImplSerDomain> list) {
        List<ImplSerDomain> list1 = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();

        String pattern = "[0-9]{3,4}";

        String pattern1 = "[\\u4e00-\\u9fa5]+x?\\d[\\u4e00-\\u9fa5]?";


        Pattern r = Pattern.compile(pattern);

        Pattern r1 = Pattern.compile(pattern1);


        for (ImplSerDomain listDomain : list) {

            int zhuYuFen = 0;

            String str = listDomain.getName();

            Matcher m = r.matcher(str);

            Matcher m1 = r1.matcher(str);

            if (str.contains("足浴粉")||str.contains("足浴包")||str.contains("泡脚粉")||str.contains("泡脚包")||str.contains("泡脚")) {
                while (m1.find()) {
                    String s = m1.group();

                    zhuYuFen = jisuan(s);

                }
            }
            if (str.contains("三个")) {
                listDomain.setNum(listDomain.getNum() * 3);
            }

            while (m.find()) {
                String name = m.group();
                if (name.equals("300") || name.equals("180")) {
                    if (null != map.get(name)) {
                        map.put(name, map.get(name) + (zhuYuFen*listDomain.getNum()));
                    } else {
                        map.put(name, zhuYuFen*listDomain.getNum());
                    }
                } else {
                    if (null != map.get(name)) {
                        map.put(name, map.get(name) + listDomain.getNum());
                    } else {
                        map.put(name, listDomain.getNum());
                    }
                }

            }
        }
        Set<String> set = map.keySet();
        for (String s : set) {
            list1.add(new ImplSerDomain(s,map.get(s)));
        }
        return list1;
    }

    private Integer jisuan (String str){

        int i1 = 0;

        String pattern2 = "[0-9]";


        Pattern r2 = Pattern.compile(pattern2);


        Matcher m2 = r2.matcher(str);
        if (m2.find()) {
            String s1 = m2.group();
            i1 = Integer.parseInt(s1);
        }
        return i1;
    }

    private List<RecordBean> mapping(List<ImplSerDomain> implSerDomainList){

        List<RecordBean> recordBeans = new ArrayList<>();

        List<FormBoardBean> formList = formBoardMapper.selectAll();

        for (ImplSerDomain implSerDomain : implSerDomainList) {
            String name = implSerDomain.getName();

            for (FormBoardBean formBoardBean : formList) {
                if(name.equals(formBoardBean.getName())){
                    recordBeans.add(new RecordBean(null,
                            name,
                            formBoardBean.getCoding(),
                            formBoardBean.getDetailedname(),
                            formBoardBean.getBarcode(),
                            implSerDomain.getNum(),
                            null
                            ));
                }
            }

        }

        return recordBeans;
    }

}
