package com.zhutao.controller;

import com.google.gson.Gson;
import com.zhutao.bean.EntrepotBean;
import com.zhutao.bean.UserBean;
import com.zhutao.bean.domain.Page;
import com.zhutao.bean.domain.PageDomain;
import com.zhutao.mapper.RecordBreMapper;
import com.zhutao.server.EntrepotService;
import com.zhutao.server.RecordBreService;
import com.zhutao.server.RecordService;
import com.zhutao.server.UserService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@Controller
public class DataController {

    @Autowired
    private UserService userService;
    @Autowired
    private EntrepotService entrepotService;
    @Autowired
    private RecordService recordService;
    @Autowired
    private RecordBreService recordBreService;

    private Gson gson = new Gson();


    //点击登录跳转请求
    @RequestMapping("/")
    public String get(){
        return "index";
    }
    @RequestMapping("/main")
    public String main(){
        return "main";
    }

    @RequestMapping("/index")
    public String index(){
        return "index";
    }
    @RequestMapping("/toIndex")
    @ResponseBody
    public String toIndex(UserBean bean, HttpServletRequest request){
        UserBean dbUser = userService.getOne(bean);
        if(null!=dbUser){
            request.getSession(true).setAttribute("ADMIN",dbUser.getName());
            return "1";
        }
        return "0";
    }

    @RequestMapping("/insert")
    public String insert(UserBean user){
        return userService.insert(user);
    }

    @RequestMapping("/sapwm")
    public String sapWm(Model model){

        List<EntrepotBean> beans = entrepotService.getAll();

        model.addAttribute("beans",beans);

        return "sapwm";
    }
    @ResponseBody
    @RequestMapping("/query")
    public Page query(PageDomain page){
        return entrepotService.pageZ(page);
    }

    @RequestMapping("/recordQuery")
    @ResponseBody
    public Page recordQuery(PageDomain page){
        return recordService.pageZ(page);
    }

    @RequestMapping("/record")
    public String record(){

        return "record";
    }

    @RequestMapping("/down")
    public void down(PageDomain page,HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");

        response.setHeader("content-disposition","attachment;filename=count.xls");

        HSSFWorkbook we = recordBreService.exprt(page);
        OutputStream os = response.getOutputStream();
        we.write(os);
        os.flush();
        os.close();
    }

    @RequestMapping("/recordRedQuery")
    @ResponseBody
    public Page recordRedQuery(PageDomain page){
        return recordBreService.pageZ(page);
    }

    // 导入信息
    @RequestMapping("/impl")
    public String impl(){
        return "impl";
    }

    @RequestMapping("/impl.do")
    public void implDo(@RequestParam("excel")MultipartFile file){

        recordBreService.implSer(file);
    }

    @RequestMapping("/recordRed")
    public String recordRed(){

        return "recordRed";
    }

    @RequestMapping("/saveOrDlt")
    @ResponseBody
    public String saveOrDltC(@RequestParam("id") String id,@RequestParam("flag")String flag){
        return recordBreService.saveOrDlt(id,flag);
    }
    @RequestMapping("savesOrDlts")
    @ResponseBody
    public String savsOrDltsC(String str,String flag){

        return recordBreService.savesOrDlts(str,flag);
    }
    @RequestMapping("mainEcharts")
    public String mainEcharts(Model model){
        model.addAttribute("name",gson.fromJson(recordBreService.findAll(),List.class));
        return "mainEcharts";
    }


}
