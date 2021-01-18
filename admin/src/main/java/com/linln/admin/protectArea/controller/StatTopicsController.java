package com.linln.admin.protectArea.controller;

import com.linln.admin.protectArea.validator.StatTopicsValid;
import com.linln.common.enums.StatusEnum;
import com.linln.common.utils.EntityBeanUtil;
import com.linln.common.utils.ResultVoUtil;
import com.linln.common.utils.StatusUtil;
import com.linln.common.vo.ResultVo;
import com.linln.component.excel.ExcelUtil;
import com.linln.component.fileUpload.FileUpload;
import com.linln.modules.protectArea.domain.ProtectArea;
import com.linln.modules.protectArea.domain.StatTopics;
import com.linln.modules.protectArea.service.StatTopicsService;
import com.linln.modules.system.domain.Upload;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

/**
 * @author susie
 * @date 2021/01/18
 */
@Controller
@RequestMapping("/protectArea/statTopics")
public class StatTopicsController {

    @Autowired
    private StatTopicsService statTopicsService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("protectArea:statTopics:index")
    public String index(Model model, StatTopics statTopics) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("topics", match -> match.contains());

        // 获取数据列表
        Example<StatTopics> example = Example.of(statTopics, matcher);
        Page<StatTopics> list = statTopicsService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/protectArea/statTopics/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("protectArea:statTopics:add")
    public String toAdd() {
        return "/protectArea/statTopics/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("protectArea:statTopics:edit")
    public String toEdit(@PathVariable("id") StatTopics statTopics, Model model) {
        model.addAttribute("statTopics", statTopics);
        return "/protectArea/statTopics/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping("/save")
    @RequiresPermissions({"protectArea:statTopics:add", "protectArea:statTopics:edit"})
    @ResponseBody
    public ResultVo save(@Validated StatTopicsValid valid, StatTopics statTopics) {
        // 复制保留无需修改的数据
        if (statTopics.getId() != null) {
            StatTopics beStatTopics = statTopicsService.getById(statTopics.getId());
            EntityBeanUtil.copyProperties(beStatTopics, statTopics);
        }

        // 保存数据
        statTopicsService.save(statTopics);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("protectArea:statTopics:detail")
    public String toDetail(@PathVariable("id") StatTopics statTopics, Model model) {
        model.addAttribute("statTopics",statTopics);
        return "/protectArea/statTopics/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("protectArea:statTopics:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (statTopicsService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }

    /**
     * 生成excel模版并下载
     */
    @RequestMapping("/excel/template")
    @RequiresPermissions("protectArea:statTopics:add")
    @ResponseBody
    public void genExcelTemplate(){
        try {
            ExcelUtil.genTemplate(Class.forName("com.linln.modules.protectArea.domain.StatTopics"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * 导出excel
     */
    @RequestMapping("/excel/export")
    @RequiresPermissions("protectArea:statTopics:add")
    @ResponseBody
    public void exportExcel(){
        StatTopics statTopics = new StatTopics();
        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("topics", match -> match.contains());

        // 获取数据列表
        Example<StatTopics> example = Example.of(statTopics, matcher);
        Page<StatTopics> list = statTopicsService.getPageList(example);
        List datalist = list.getContent();
        //List datalist = (List) statTopicsService.getById(Long.valueOf(1));
        //System.out.println(datalist);
        try {
            ExcelUtil.exportExcel(Class.forName("com.linln.modules.protectArea.domain.StatTopics"),datalist,"统计专题数据");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //return ResultVoUtil.success("成功下载");
    }

    /**
     * 导入excel
     */
    @RequestMapping("/excel/import")
    @RequiresPermissions("protectArea:statTopics:add")
    @ResponseBody
    public void importExcel(){
        try {
            String path = FileUpload.getPathPattern();
            File cfgFile;
            cfgFile = ResourceUtils.getFile( path+"/test.xlsx");
            InputStream inputStream = new FileInputStream(cfgFile);
            ExcelUtil.importExcel(Class.forName("com.linln.modules.protectArea.domain.StatTopics"), inputStream);
        }catch (ClassNotFoundException | FileNotFoundException e){
            e.printStackTrace();
        }
    }

    @RequestMapping("/excel/upload")
    @RequiresPermissions("protectArea:statTopics:add")
    @ResponseBody
    public ResultVo uploadFile(@RequestParam("myfile") MultipartFile multipartFile) throws IOException {

        // 创建Upload实体对象
        Upload upload = FileUpload.getFile(multipartFile, "/upload");

        // 保存文件到指定路径
        multipartFile.transferTo(FileUpload.getDestFile(upload));

        // 保存文件上传信息
        //uploadService.save(upload);

        File file = FileUpload.getDestFile(upload);
        InputStream inputStream = new FileInputStream(file);
        try {
            if(!ExcelUtil.validateTemplate(Class.forName("com.linln.modules.protectArea.domain.StatTopics"), inputStream)){
                return ResultVoUtil.error("该Excel文件模版不合法，必须使用系统导出的模版填写数据，请检查文件！");
            }else {
                inputStream = new FileInputStream(file);
                List<?> list = ExcelUtil.importExcel(Class.forName("com.linln.modules.protectArea.domain.StatTopics"), inputStream);
                for(Object st:list){
                    System.out.println((StatTopics)st);
                    statTopicsService.save((StatTopics) st);
                }
            }

        }catch (ClassNotFoundException  e){
            e.printStackTrace();
        }


        return ResultVoUtil.success(upload);
    }

    @GetMapping("/upload")
    @RequiresPermissions("protectArea:statTopics:add")
    public String upload() {
        return "/protectArea/statTopics/import";
    }
}