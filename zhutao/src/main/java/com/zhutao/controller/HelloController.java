package com.zhutao.controller;

import com.zhutao.domain.ListDomain;
import com.zhutao.domain.oldListDomain;
import com.zhutao.service.mainService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class HelloController {

    private List<ListDomain> list1 = null;
    private Util util = new Util();

    @RequestMapping("/hello")
    public String hello(@RequestParam("excel") MultipartFile file) {

        try {
            InputStream is = file.getInputStream();
                Workbook workbook = WorkbookFactory.create(is);

            Sheet sheet = workbook.getSheetAt(0);
            List<ListDomain> list = new ArrayList<>();
            for (int a = 1; a <= sheet.getLastRowNum(); a++) {


                Row row = sheet.getRow(a);

                Cell c1 = row.getCell(19);
                Cell c2 = row.getCell(20);

                try {

                    String $c1 = c1.getStringCellValue();
                    Integer $c2 = (int) c2.getNumericCellValue();

                    list.add(new ListDomain($c1, $c2));
                } catch (Exception e) {

                    Integer $c1 = (int) c1.getNumericCellValue();
                    Integer $c2 = (int) c2.getNumericCellValue();
                    String c2_g = $c1.toString();
                    list.add(new ListDomain(c2_g, $c2));
                }


            }
            list1=service(list);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "main";
    }



    @RequestMapping("/main")
    public String main(Model model){
        List<oldListDomain> listYuan = util.yuan();

        for (ListDomain listDomain : list1) {
            boolean flag =false;
                if(listDomain.getTotal().equals("3338")){
                    for (oldListDomain oldListDomain : listYuan) {
                        if(oldListDomain.getProductName().contains("3333")){
                            listDomain.setProductName(oldListDomain.getProductName());
                            listDomain.setId(oldListDomain.getNumber());
                            flag=true;
                        }
                    }
                    continue;
                }else if(listDomain.getTotal().equals("681")){

                    listDomain.setProductName("%TB嘉星杜鹃花681-B足浴桶");
                    listDomain.setId("342234");
                    flag=true;
                    continue;
                }else if(listDomain.getTotal().equals("682")){

                    listDomain.setProductName("%TB嘉星杜鹃花682新式足浴桶");
                    listDomain.setId("342192");
                    flag=true;
                    continue;
                }else if(listDomain.getTotal().equals("683")){

                    listDomain.setProductName("%TB嘉星杜鹃花683按摩足浴桶");
                    listDomain.setId("342040");

                    flag=true;
                    continue;
                }else if(listDomain.getTotal().equals("300")){

                    listDomain.setProductName("300g足浴粉");
                    listDomain.setId("300g足浴粉");
                    flag=true;
                    continue;
                }else if(listDomain.getTotal().equals("180")){

                    listDomain.setProductName("180g足浴粉");
                    listDomain.setId("180g足浴粉");
                    flag=true;
                    continue;
                } else {
                    for (oldListDomain oldListDomain : listYuan) {
                        if(oldListDomain.getProductName().contains(listDomain.getTotal())){
                            listDomain.setProductName(oldListDomain.getProductName());
                            listDomain.setId(oldListDomain.getNumber());
                            flag=true;
                        }
                    }
                }

            if(flag==false){
                listDomain.setProductName("没有匹配项");
                listDomain.setId("没有匹配项");

            }
        }


        model.addAttribute("beans",list1);
        return "main";
    }

    @RequestMapping("/down")
    public void down(HttpServletResponse response) throws IOException {
        response.setHeader("content-disposition","attachment;filename=collect.xls");
        HSSFWorkbook we = mainService.exprt(list1);
        OutputStream os = response.getOutputStream();
        we.write(os);
        os.flush();
        os.close();

    }


    public List<ListDomain> service(List<ListDomain> list) {
        List<ListDomain> list1 = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();

        String pattern = "[0-9]{3,4}";

        String pattern1 = "[\\u4e00-\\u9fa5]+x?\\d[\\u4e00-\\u9fa5]?";


        Pattern r = Pattern.compile(pattern);

        Pattern r1 = Pattern.compile(pattern1);


        for (ListDomain listDomain : list) {

            int zhuYuFen = 0;

            String str = listDomain.getTotal();

            Matcher m = r.matcher(str);

            Matcher m1 = r1.matcher(str);

            if (str.contains("足浴粉")||str.contains("足浴包")||str.contains("泡脚粉")||str.contains("泡脚包")) {
                while (m1.find()) {
                    String s = m1.group();

                    zhuYuFen = jisuan(s);

                }
            } else {
                while (m1.find()) {
                    String s = m1.group();
                    listDomain.setNum(listDomain.getNum() * jisuan(s));
                }
            }
            if (str.contains("三个")) {
                listDomain.setNum(listDomain.getNum() * 3);
            }

            while (m.find()) {
                String name = m.group();
                System.out.println(name);
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
            list1.add(new ListDomain(s,map.get(s)));
        }
        return list1;
    }


//        public static void main (String[]args){
//            Map<String, Integer> map = new HashMap<>();
//            map.put("name", 1);
//
//            map.put("name", 2);
//            System.out.println(map.get("name"));
//        String test ="cf08-6072-浅灰x4 cf08-6091-发2包   1230三个" ;
//
////        System.out.println(test.contains("三个"));
//
//        String pattern = "[\\u4e00-\\u9fa5]+x?\\d[\\u4e00-\\u9fa5]?";
//
//        String pattern1 = "[0-9]";
//
//        Pattern p = Pattern.compile(pattern1);
//
//        Pattern  r = Pattern.compile(pattern);
//        Matcher m = r.matcher(test);
//        while (m.find()){
//            String s = m.group();
//            Matcher m1 = p.matcher(s);
//            if(m1.find()){
//                String s1 = m1.group();
//                Integer i1 = Integer.parseInt(s1);
//                System.out.println(i1);
//            }
//            System.out.println(s);
//        }

//        }

    public Integer jisuan (String str){

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


}