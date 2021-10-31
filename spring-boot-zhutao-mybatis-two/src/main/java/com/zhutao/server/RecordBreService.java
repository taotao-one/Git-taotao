package com.zhutao.server;

import com.google.gson.Gson;
import com.zhutao.bean.FormBoardBean;
import com.zhutao.bean.RecordBean;
import com.zhutao.bean.domain.EchartsBreDomain;
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
public class RecordBreService {

    @Autowired
    private FormBoardMapper formBoardMapper;
    @Autowired
    private RecordBreMapper recordBreMapper;
    @Autowired
    private RecordMapper recordMapper;

    private Gson gson = new Gson();


    public String saveOrDlt(String id,String flag){
        try{

            if(flag.equals("save")){
                recordMapper.insertOne(save(id));
                return "操作成功";
            }if(flag.equals("dlt")){
                dlt(id);
                return "删除成功";
            }else {
                return "无效操作";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "操作失败";
        }
    }

    public String savesOrDlts(String ids,String flag){
        String[] id = splitId(ids);
        try{
            for(int i = 0;i<id.length;i++){
                this.saveOrDlt(id[i],flag);
            }
        }catch (Exception e){
            return "操作失败";
        }
        return "操作成功";
    }

    private String[] splitId(String id){
        return id.split(",");
    }


    public void implSer(MultipartFile file) {

        try {
            InputStream is = file.getInputStream();
            Workbook workbook = WorkbookFactory.create(is);

            Sheet sheet = workbook.getSheetAt(0);
            List<RecordBean> list = sheets(sheet);
            for (RecordBean recordBean : list) {
                recordBreMapper.insertOne(recordBean);
            }



        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public Page pageZ(PageDomain page){
        if(page.getPage()<1){
            page.setPage(1);
        }
        int total = recordBreMapper.reTotal(page);

        int max = total%page.getRows()==0?total/page.getRows():total/page.getRows()+1;

        max = Math.max(1,max);
        if(page.getPage()>max){
            page.setPage(max);
        }

        page.setStart((page.getPage()-1)*page.getRows());
        return new Page(max,recordBreMapper.reFind(page));

    }

    public  HSSFWorkbook exprt(PageDomain page){

        if(page.getPage()<1){
            page.setPage(1);
        }
        int total = recordBreMapper.reTotal(page);

        int max = total%page.getRows()==0?total/page.getRows():total/page.getRows()+1;

        max = Math.max(1,max);
        if(page.getPage()>max){
            page.setPage(max);
        }

        page.setStart((page.getPage()-1)*page.getRows());

        List<RecordBean> list = recordBreMapper.reFind(page);


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

            Cell c1 = row.createCell(0);
            c1.setCellValue(list.get(i).getName());
            c1.setCellStyle(style);

            Cell c2 = row.createCell(1);
            c2.setCellValue(list.get(i).getCoding());
            c2.setCellStyle(style);


            Cell c3 = row.createCell(2);
            c3.setCellValue(list.get(i).getDetailedname());
            c3.setCellStyle(style);

            Cell c4 = row.createCell(3);
            c4.setCellValue(list.get(i).getBarcode());
            c4.setCellStyle(style);

            Cell c5 = row.createCell(4);
            c5.setCellValue(list.get(i).getQuantity());
            c5.setCellStyle(style);

            Cell c6 = row.createCell(5);
            c6.setCellValue(list.get(i).getAlterTime());
            c6.setCellStyle(style);
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

    public RecordBean save(String id) throws Exception {
        RecordBean recordBean = recordBreMapper.selectOne(id);
        dlt(id);
        return recordBean;
    }

    public void dlt(String id)throws Exception{
        try{
        recordBreMapper.deleteOne(id);

        }catch (Exception e){

        }
    }

    public String findAll(){
        List<EchartsBreDomain> list= new ArrayList<>();
        for (RecordBean recordBean : recordBreMapper.selectAll()) {
            list.add(new EchartsBreDomain(recordBean.getName(),recordBean.getQuantity()));
        }
        return gson.toJson(list);
    }


}
