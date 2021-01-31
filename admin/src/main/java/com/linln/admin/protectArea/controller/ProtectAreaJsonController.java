package com.linln.admin.protectArea.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linln.modules.protectArea.domain.*;
import com.linln.modules.protectArea.repository.ProtectAreaRepository;
import com.linln.modules.protectArea.service.AdcodeService;
import com.linln.modules.protectArea.service.ProtectAreaService;
import com.linln.modules.protectArea.service.impl.AdcodeServiceImpl;
import com.linln.modules.protectArea.service.impl.StatTopicsServiceImpl;
import com.mysql.cj.xdevapi.JsonArray;
import net.sf.ehcache.util.LargeSet;
import netscape.javascript.JSException;
import netscape.javascript.JSObject;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("protectArea/json")
public class ProtectAreaJsonController {

    @Autowired
    private AdcodeServiceImpl adcodeService;

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

    private List<String> queryDistrictAdcode(String provinceCode, String cityCode, String countyCode){
        List<String> adcodeList = new ArrayList<>();
        Set<String> adcodeSet = new HashSet<>();
        if(countyCode.isEmpty()){
            if(cityCode.isEmpty()){
                List<IAdcodeTO> cityAdcodes = adcodeService.findCites(provinceCode);
                for(int i=0; i<cityAdcodes.size(); i++){
                    List<IAdcodeTO> countyAdcodes = adcodeService.findCounties(cityAdcodes.get(i).getCode());
                    for(int j=0;j<countyAdcodes.size();j++){
                        adcodeSet.add(countyAdcodes.get(j).getCode());
                    }
                }
            }
            else{
                List<IAdcodeTO> countyAdcodes = adcodeService.findCounties(cityCode);
                for(int i=0; i<countyAdcodes.size(); i++){
                    adcodeSet.add(countyAdcodes.get(i).getCode());
                }
            }
        }
        else{
            adcodeSet.add(countyCode);
        }

        adcodeList.addAll(adcodeSet);
        return adcodeList;
    }

    private List<String> queryStatTopicsAdcode(String topic, String subTopic){
        List<String> adcodeList = new ArrayList<>();
        Set<String> adcodeSet = new HashSet<String>();

        List<StatTopics> statTopics = statTopicsService.findAll(topic, subTopic);
        for(int i=0; i<statTopics.size(); i++){
            StatTopics item = statTopics.get(i);
            List<StatTopicsLocation> locations = item.getLocations();
            for(int j=0;j<locations.size();j++){
                adcodeSet.add(locations.get(j).getAdcode());
            }
        }

        adcodeList.addAll(adcodeSet);
        return adcodeList;
    }

    private List<ProtectArea> queryProtectArea(List<String> adcodes){
        return protectAreaService.findAllByAdcode(adcodes);
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
        List<String> countyCodes = null;

        // 区域
        if("district".equals(regionType)){
            countyCodes = queryDistrictAdcode(province, city, county);
        }
        // 专题
        else if("topic".equals(regionType)){
            countyCodes = queryStatTopicsAdcode(topic, subTopic);
        }

        data = queryProtectArea(countyCodes);

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
        List<String> countyCodes = null;

        try {
            // 区域
            if("district".equals(regionType)){
                countyCodes = queryDistrictAdcode(province, city, county);
            }
            // 专题
            else if("topic".equals(regionType)){
                countyCodes = queryStatTopicsAdcode(topic, subTopic);
            }
            data = queryProtectArea(countyCodes);
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
