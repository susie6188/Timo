package com.linln.admin.protectArea.controller;

import com.linln.admin.protectArea.validator.ProtectAreaValid;
import com.linln.common.enums.StatusEnum;
import com.linln.common.utils.EntityBeanUtil;
import com.linln.common.utils.ResultVoUtil;
import com.linln.common.utils.StatusUtil;
import com.linln.common.vo.ResultVo;
import com.linln.component.excel.ExcelUtil;
import com.linln.component.excel.annotation.Excel;
import com.linln.component.fileUpload.FileUpload;
import com.linln.devtools.generate.GenerateController;
import com.linln.modules.protectArea.domain.ProtectArea;
import com.linln.modules.protectArea.service.ProtectAreaService;
import com.linln.modules.system.domain.Upload;
import jdk.nashorn.internal.ir.ReturnNode;
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
 * @date 2020/11/26
 */
@Controller
@RequestMapping("/protectArea/protectArea")
public class ProtectAreaController {

    @Autowired
    private ProtectAreaService protectAreaService;
    //private GenerateController uploadService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("protectArea:protectArea:index")
    public String index(Model model, ProtectArea protectArea) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", match -> match.contains())
                .withMatcher("protectedObjects", match -> match.contains())
                .withMatcher("institutionLevel", match -> match.contains())
                .withMatcher("institutionName", match -> match.contains())
                .withMatcher("institutionAffiliation", match -> match.contains())
                .withMatcher("nameBefore", match -> match.contains());

        // 获取数据列表
        Example<ProtectArea> example = Example.of(protectArea, matcher);
        Page<ProtectArea> list = protectAreaService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/protectArea/protectArea/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("protectArea:protectArea:add")
    public String toAdd() {
        return "/protectArea/protectArea/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("protectArea:protectArea:edit")
    public String toEdit(@PathVariable("id") ProtectArea protectArea, Model model) {
        model.addAttribute("protectArea", protectArea);
        return "/protectArea/protectArea/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping("/save")
    @RequiresPermissions({"protectArea:protectArea:add", "protectArea:protectArea:edit"})
    @ResponseBody
    public ResultVo save(@Validated ProtectAreaValid valid, ProtectArea protectArea) {
        // 复制保留无需修改的数据
        if (protectArea.getId() != null) {
            ProtectArea beProtectArea = protectAreaService.getById(protectArea.getId());
            EntityBeanUtil.copyProperties(beProtectArea, protectArea);
        }

        // 保存数据
        protectAreaService.save(protectArea);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("protectArea:protectArea:detail")
    public String toDetail(@PathVariable("id") ProtectArea protectArea, Model model) {
        model.addAttribute("protectArea",protectArea);
        return "/protectArea/protectArea/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("protectArea:protectArea:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (protectAreaService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }

    /**
     * 生成excel模版并下载
     */
    @RequestMapping("/excel/template")
    @RequiresPermissions("protectArea:protectArea:add")
    @ResponseBody
    public void genExcelTemplate(){
        try {
            ExcelUtil.genTemplate(Class.forName("com.linln.modules.protectArea.domain.ProtectArea"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    /**
     * 导出excel
     */
    @RequestMapping("/excel/export")
    @RequiresPermissions("protectArea:protectArea:add")
    @ResponseBody
    public void exportExcel(){
        ProtectArea protectArea = new ProtectArea();
        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", match -> match.contains())
                .withMatcher("protectedObjects", match -> match.contains())
                .withMatcher("institutionLevel", match -> match.contains())
                .withMatcher("institutionName", match -> match.contains())
                .withMatcher("institutionAffiliation", match -> match.contains())
                .withMatcher("nameBefore", match -> match.contains());

        // 获取数据列表
        Example<ProtectArea> example = Example.of(protectArea, matcher);
        Page<ProtectArea> list = protectAreaService.getPageList(example);
        List datalist = list.getContent();
        //List datalist = (List) protectAreaService.getById(Long.valueOf(1));
        //System.out.println(datalist);
        try {
            ExcelUtil.exportExcel(Class.forName("com.linln.modules.protectArea.domain.ProtectArea"),datalist,"整合优化后保护地数据");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //return ResultVoUtil.success("成功下载");
    }

    /**
     * 导入excel
     */
    @RequestMapping("/excel/import")
    @RequiresPermissions("protectArea:protectArea:add")
    @ResponseBody
    public void importExcel(){
        try {
            String path = FileUpload.getPathPattern();
            File cfgFile;
            cfgFile = ResourceUtils.getFile( path+"/test.xlsx");
            InputStream inputStream = new FileInputStream(cfgFile);
            ExcelUtil.importExcel(Class.forName("com.linln.modules.protectArea.domain.ProtectArea"), inputStream);
        }catch (ClassNotFoundException | FileNotFoundException e){
            e.printStackTrace();
        }
    }
    @RequestMapping("/excel/upload")
    @RequiresPermissions("protectArea:protectArea:add")
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
            if(!ExcelUtil.validateTemplate(Class.forName("com.linln.modules.protectArea.domain.ProtectArea"), inputStream)){
                return ResultVoUtil.error("该Excel文件模版不合法，必须使用系统导出的模版填写数据，请检查文件！");
            }else {
                inputStream = new FileInputStream(file);
                List<?> list = ExcelUtil.importExcel(Class.forName("com.linln.modules.protectArea.domain.ProtectArea"), inputStream);
                for(Object pa:list){
                    System.out.println((ProtectArea)pa);
                    protectAreaService.save((ProtectArea) pa);
                }
            }

        }catch (ClassNotFoundException  e){
            e.printStackTrace();
        }


        return ResultVoUtil.success(upload);
    }

    @GetMapping("/upload")
    @RequiresPermissions("protectArea:protectArea:add")
    public String upload() {
        return "/protectArea/protectArea/import";
    }

    @GetMapping("stats")
    @RequiresPermissions("protectArea:protectArea:stats")
    public String stats(){
        return "/protectArea/protectArea/stats";
    }
}