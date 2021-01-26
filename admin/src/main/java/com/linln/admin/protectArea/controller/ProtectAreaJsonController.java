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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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

    @Autowired
    private StatTopicsServiceImpl statTopicsService;

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

    @ResponseBody
    @RequestMapping(value = "/query4Chart", produces = "application/json;charset=UTF-8")
    public JSONObject query4Chart(
            @RequestParam String regionType,
            @RequestParam(defaultValue = "") String province,
            @RequestParam(defaultValue = "") String city,
            @RequestParam(defaultValue = "") String county,
            @RequestParam(defaultValue = "") String topic,
            @RequestParam(defaultValue = "") String subTopic,
            @RequestParam String protectedObjects,
            @RequestParam int startYear,
            @RequestParam int endYear
    ){
        List<ProtectArea> data = null;
        long count = 0;
        String[] provinces = {};
        String[] cities = {};
        String[] counties = {};

        // 区域
        if("district".equals(regionType)){
            provinces = new String[]{province};
            cities = new String[]{city};
            counties = new String[]{county};
        }
        // 专题
        else if("topic".equals(regionType)){
            List<String> provinceList = new ArrayList<>();
            List<String> cityList = new ArrayList<>();
            List<String> countyList = new ArrayList<>();

            List<StatTopics> statTopics = statTopicsService.findAll(topic, subTopic);
            for(int i=0; i<statTopics.size(); i++){
                StatTopics item = statTopics.get(i);
                if(!provinceList.contains(item.getProvince())){
                    provinceList.add(item.getProvince());
                }
                if(!countyList.contains(item.getCounty())){
                    countyList.add(item.getCounty());
                }
            }

            provinces = provinceList.toArray(new String[0]);
            cities = cityList.toArray(new String[0]);
            counties = countyList.toArray(new String[0]);
        }
        data = protectAreaService.findAllByDistrict(provinces, cities, counties, protectedObjects, startYear, endYear);
//        count = protectAreaService.countByDistrict(provinces, cities, counties, protectedObjects, startYear,endYear);

        JSONArray provinceData = new JSONArray();
        List<String> provinceList = new ArrayList<>();
        List<Integer> provinceCountList = new ArrayList<>();
        List<Double> provinceAreaList = new ArrayList<>();

        JSONArray protectedObjectsData = new JSONArray();
        List<String> protectedObjectsList = new ArrayList<>();
        List<Integer> protectedObjectsCountList = new ArrayList<>();
        List<Double> protectedObjectsAreaList = new ArrayList<>();

        for(int i=0;i<data.size();i++){
            // province
            String provinceName = data.get(i).getProvince().trim();
            String protectedObjectsString = data.get(i).getProtectedObjects().trim();
            long id = data.get(i).getId();
            double currentArea = data.get(i).getCurrentArea();

            if(!provinceList.contains(provinceName)){
                provinceList.add(provinceName);
                provinceCountList.add(1);
                provinceAreaList.add(currentArea);
            }
            else{
                int index = provinceList.indexOf(provinceName);
                provinceCountList.set(index, provinceCountList.get(index) + 1);
                provinceAreaList.set(index, provinceAreaList.get(index) + currentArea);
            }

            // protectedObjects
            if(!protectedObjectsList.contains(protectedObjectsString)){
                protectedObjectsList.add(protectedObjectsString);
                protectedObjectsCountList.add(1);
                protectedObjectsAreaList.add(currentArea);
            }
            else{
                int index = protectedObjectsList.indexOf(protectedObjectsString);
                protectedObjectsCountList.set(index, protectedObjectsCountList.get(index) + 1);
                double beforeArea = protectedObjectsAreaList.get(index);
                double area = currentArea + beforeArea;
                protectedObjectsAreaList.set(index, area);
            }
        }

        for(int i=0; i<provinceList.size();i++){
            provinceData.add(JSONObject.parse("{'province': '" + provinceList.get(i) + "', 'count': " + provinceCountList.get(i) + ", 'area': " + provinceAreaList.get(i) + "}"));
        }

        for(int i=0; i<protectedObjectsList.size();i++){
            protectedObjectsData.add(JSONObject.parse("{'protectedObjects': '" + protectedObjectsList.get(i) + "', 'count': " + protectedObjectsCountList.get(i) + ", 'area': " + String.format("%.2f", protectedObjectsAreaList.get(i)) + "}"));
        }

        JSONObject result = new JSONObject();
        result.put("provinceData", provinceData);
        result.put("protectedObjectsData", protectedObjectsData);
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/query4Table", produces = "application/json;charset=UTF-8")
    public LayuiTableDataVO query4Table(
            @RequestParam(defaultValue = "") String regionType,
            @RequestParam(defaultValue = "") String province,
            @RequestParam(defaultValue = "") String city,
            @RequestParam(defaultValue = "") String county,
            @RequestParam(defaultValue = "") String topic,
            @RequestParam(defaultValue = "") String subTopic,
            @RequestParam(defaultValue = "") String protectedObjects,
            @RequestParam(defaultValue = "-1") int startYear,
            @RequestParam(defaultValue = "-1") int endYear
    ){
        List<ProtectArea> data = null;
        long count = 0;
        String[] provinces = {};
        String[] cities = {};
        String[] counties = {};

        try {
            // 区域
            if("district".equals(regionType)){
                provinces = new String[]{province};
                cities = new String[]{city};
                counties = new String[]{county};
            }
            // 专题
            else if("topic".equals(regionType)){
                List<String> provinceList = new ArrayList<>();
                List<String> cityList = new ArrayList<>();
                List<String> countyList = new ArrayList<>();

                List<StatTopics> statTopics = statTopicsService.findAll(topic, subTopic);
                for(int i=0; i<statTopics.size(); i++){
                    StatTopics item = statTopics.get(i);
                    if(!provinceList.contains(item.getProvince())){
                        provinceList.add(item.getProvince());
                    }
                    if(!countyList.contains(item.getCounty())){
                        countyList.add(item.getCounty());
                    }
                }

                provinces = provinceList.toArray(new String[0]);
                cities = cityList.toArray(new String[0]);
                counties = countyList.toArray(new String[0]);
            }

            data = protectAreaService.findAllByDistrict(provinces, cities, counties, protectedObjects, startYear, endYear);
//            count = protectAreaService.countByDistrict(provinces, cities, counties, protectedObjects, startYear, endYear);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        LayuiTableDataVO result = new LayuiTableDataVO();
        result.setCode(0);
        result.setMsg("");
        result.setCount(count);
        result.setData(data);
        return result;
    }
}
