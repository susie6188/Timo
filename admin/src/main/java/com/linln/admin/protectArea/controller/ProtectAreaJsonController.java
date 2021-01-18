package com.linln.admin.protectArea.controller;

import com.linln.modules.protectArea.domain.ProtectArea;
import com.linln.modules.protectArea.repository.ProtectAreaRepository;
import com.linln.modules.protectArea.service.ProtectAreaService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("protectArea/json")
public class ProtectAreaJsonController {
    @Autowired
    private ProtectAreaService protectAreaService;

    @Autowired
    private ProtectAreaRepository protectAreaRepository;

    /**
     * 数据列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("protectArea:protectArea:detail")
    public List getData(Model model){
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
        return datalist;
    }

    @RequestMapping("/levelData")
    @RequiresPermissions("protectArea:protectArea:detail")
    public HashMap<String,List> getDataByLevel(Model model){
        HashMap<String,List> map = new HashMap<String, List>();
        List levelList = protectAreaService.getLevel();
        List datalist = new ArrayList();

        Iterator<String> it = levelList.iterator();
        while (it.hasNext()){
            String level = it.next();
            datalist.add(protectAreaRepository.getAreaStatsByLevel(level));
        }
        //List datalist = protectAreaRepository.getStatsByLevel();
        map.put("category",levelList);
        map.put("data",datalist);
        return map;
    }
}
