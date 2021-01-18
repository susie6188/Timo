package com.linln.admin.protectArea.controller;

import com.linln.admin.protectArea.validator.ProtectAreaBeforeValid;
import com.linln.common.enums.StatusEnum;
import com.linln.common.utils.EntityBeanUtil;
import com.linln.common.utils.ResultVoUtil;
import com.linln.common.utils.StatusUtil;
import com.linln.common.vo.ResultVo;
import com.linln.modules.protectArea.domain.ProtectAreaBefore;
import com.linln.modules.protectArea.service.ProtectAreaBeforeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author susie
 * @date 2020/11/26
 */
@Controller
@RequestMapping("/protectArea/protectAreaBefore")
public class ProtectAreaBeforeController {

    @Autowired
    private ProtectAreaBeforeService protectAreaBeforeService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("protectArea:protectAreaBefore:index")
    public String index(Model model, ProtectAreaBefore protectAreaBefore) {

        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", match -> match.contains())
                .withMatcher("protectedObjects", match -> match.contains())
                .withMatcher("institutionLevel", match -> match.contains())
                .withMatcher("institutionName", match -> match.contains())
                .withMatcher("institutionAffiliation", match -> match.contains());

        // 获取数据列表
        Example<ProtectAreaBefore> example = Example.of(protectAreaBefore, matcher);
        Page<ProtectAreaBefore> list = protectAreaBeforeService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/protectArea/protectAreaBefore/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("protectArea:protectAreaBefore:add")
    public String toAdd() {
        return "/protectArea/protectAreaBefore/add";
    }

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("protectArea:protectAreaBefore:edit")
    public String toEdit(@PathVariable("id") ProtectAreaBefore protectAreaBefore, Model model) {
        model.addAttribute("protectAreaBefore", protectAreaBefore);
        return "/protectArea/protectAreaBefore/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     */
    @PostMapping("/save")
    @RequiresPermissions({"protectArea:protectAreaBefore:add", "protectArea:protectAreaBefore:edit"})
    @ResponseBody
    public ResultVo save(@Validated ProtectAreaBeforeValid valid, ProtectAreaBefore protectAreaBefore) {
        // 复制保留无需修改的数据
        if (protectAreaBefore.getId() != null) {
            ProtectAreaBefore beProtectAreaBefore = protectAreaBeforeService.getById(protectAreaBefore.getId());
            EntityBeanUtil.copyProperties(beProtectAreaBefore, protectAreaBefore);
        }

        // 保存数据
        protectAreaBeforeService.save(protectAreaBefore);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("protectArea:protectAreaBefore:detail")
    public String toDetail(@PathVariable("id") ProtectAreaBefore protectAreaBefore, Model model) {
        model.addAttribute("protectAreaBefore",protectAreaBefore);
        return "/protectArea/protectAreaBefore/detail";
    }

    /**
     * 设置一条或者多条数据的状态
     */
    @RequestMapping("/status/{param}")
    @RequiresPermissions("protectArea:protectAreaBefore:status")
    @ResponseBody
    public ResultVo status(
            @PathVariable("param") String param,
            @RequestParam(value = "ids", required = false) List<Long> ids) {
        // 更新状态
        StatusEnum statusEnum = StatusUtil.getStatusEnum(param);
        if (protectAreaBeforeService.updateStatus(statusEnum, ids)) {
            return ResultVoUtil.success(statusEnum.getMessage() + "成功");
        } else {
            return ResultVoUtil.error(statusEnum.getMessage() + "失败，请重新操作");
        }
    }
}