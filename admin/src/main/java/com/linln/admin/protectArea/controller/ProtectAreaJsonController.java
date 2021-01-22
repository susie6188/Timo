package com.linln.admin.protectArea.controller;

import com.linln.modules.protectArea.domain.ProtectArea;
import com.linln.modules.protectArea.repository.ProtectAreaRepository;
import com.linln.modules.protectArea.service.ProtectAreaService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.RequestContextUtils;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

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

//    @RequestMapping("/levelData")
//    //@RequiresPermissions("protectArea:protectArea:detail")
//    public Map getDataByLevel(Model model){
//        Map map = new HashMap<String,Object>();
//
//
//
//        HashMap<String,List> map2 = new HashMap<String, List>();
//
//        List levelList = protectAreaService.getLevel();
//        List datalist = new ArrayList();
//        List datalist2 = protectAreaRepository.getAreaStatsByLevel();
//        List categoryList = protectAreaRepository.getCategory();
//        List areaListByCategory = new ArrayList();
//
//        Iterator<String> it = levelList.iterator();
//        while (it.hasNext()){
//            String level = it.next();
//            datalist.add(protectAreaRepository.getAreaStatsByLevel(level));
//        }
//
//        List areaList = new ArrayList();
//        it = categoryList.iterator();
//        while (it.hasNext()){
//            HashMap<String,String> mapAreaGroupByCategory = new HashMap<String,String>();
//            String category = it.next();
//            //areaListByCategory.add(protectAreaRepository.getAreaStatsByCategory(category));
//            mapAreaGroupByCategory.put("category",category);
//            mapAreaGroupByCategory.put("area",protectAreaRepository.getAreaStatsByCategory(category));
//            areaList.add(mapAreaGroupByCategory);
//        }
//
//
////        map2.put("category",levelList);
////        map2.put("data",datalist);
////        map2.put("data2",datalist2);
//        map2.put("counts",areaListByCategory);
////        map2.put("areaListByCategory",areaListByCategory);
//
//
//        map.put("code","0");
//        map.put("msg","");
//        map.put("count","");
//        map.put("data",areaList);
//        return map;
//    }


    @RequestMapping("/paCount")
    public List getPaCountData(Model model){
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
}
