package com.linln.admin.protectArea.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linln.modules.protectArea.domain.*;
import com.linln.modules.protectArea.repository.AdcodeRepository;
import com.linln.modules.protectArea.repository.ProtectAreaBeforeRepository;
import com.linln.modules.protectArea.repository.ProtectAreaRepository;
import com.linln.modules.protectArea.service.ProtectAreaBeforeService;
import com.linln.modules.protectArea.service.ProtectAreaService;
import com.linln.modules.protectArea.service.impl.AdcodeServiceImpl;
import com.linln.modules.protectArea.service.impl.StatTopicsServiceImpl;
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
@RequestMapping("protectAreaBefore/json")
public class PretectAreaBeforeJsonController {
    @Autowired
    private AdcodeServiceImpl adcodeService;

    @Autowired
    private ProtectAreaBeforeService protectAreaBeforeService;

    @Autowired
    private ProtectAreaBeforeRepository protectAreaBeforeRepository;

    @Autowired
    private StatTopicsServiceImpl statTopicsService;

    @Autowired
    private AdcodeRepository adcodeRepository;

    /**
     * 数据列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("protectArea:protectAreaBefore:list")
    public List getDataBefore(Model model){
        ProtectAreaBefore protectAreaBefore = new ProtectAreaBefore();
        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", match -> match.contains())
                .withMatcher("protectedObjects", match -> match.contains())
                .withMatcher("institutionLevel", match -> match.contains())
                .withMatcher("institutionName", match -> match.contains())
                .withMatcher("institutionAffiliation", match -> match.contains())
                .withMatcher("nameBefore", match -> match.contains());

        // 获取数据列表
        Example<ProtectAreaBefore> example = Example.of(protectAreaBefore, matcher);
        Page<ProtectAreaBefore> list = protectAreaBeforeService.getPageList(example);
        List datalist = list.getContent();
        return datalist;
    }



    @RequestMapping("/paCount")
    public List getPaCountDataBefore(Model model){
        ProtectAreaBefore protectAreaBefore = new ProtectAreaBefore();
        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", match -> match.contains())
                .withMatcher("protectedObjects", match -> match.contains())
                .withMatcher("institutionLevel", match -> match.contains())
                .withMatcher("institutionName", match -> match.contains())
                .withMatcher("institutionAffiliation", match -> match.contains())
                .withMatcher("nameBefore", match -> match.contains());

        // 获取数据列表
        Example<ProtectAreaBefore> example = Example.of(protectAreaBefore, matcher);
        Page<ProtectAreaBefore> list = protectAreaBeforeService.getPageList(example);
        List datalist = list.getContent();
        return datalist;
    }

    @ResponseBody
    @RequestMapping(value = "/query4Chart", produces = "application/json;charset=UTF-8")
    public JSONObject query4ChartBefore(
            @RequestParam String regionType,
            @RequestParam(defaultValue = "") String province,
            @RequestParam(defaultValue = "") String city,
            @RequestParam(defaultValue = "") String county,
            @RequestParam(defaultValue = "") String topic,
            @RequestParam(defaultValue = "") String subTopic,
            @RequestParam(defaultValue = "") String protectedObjects,
            @RequestParam(defaultValue = "-1") int startYear,
            @RequestParam(defaultValue = "-1") int endYear,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "0") int limit
    ){
        List<ProtectAreaBefore> data = null;
        long count = 0;
        List<String> countyCodes = null;
        System.out.println("--------------");
        System.out.println("regionType="+regionType);
        System.out.println("province="+province);
        System.out.println("city="+city);
        System.out.println("county="+county);
        System.out.println("topic="+topic);
        System.out.println("subTopic="+subTopic);
        System.out.println("protectedObjects="+protectedObjects);
        // 区域
        if("district".equals(regionType)){
            countyCodes = queryDistrictAdcode(province, city, county);
        }
        // 专题
        else if("topic".equals(regionType)){
            countyCodes = queryStatTopicsAdcode(topic, subTopic);
        }
        System.out.println(countyCodes.size());

        protectedObjects = "%" + protectedObjects + "%";
        Date startDate = setStartDate(startYear);
        Date endDate = setEndDate(endYear);
        data = queryProtectArea(countyCodes, protectedObjects, startDate, endDate, page, limit);

        System.out.println(data.size());

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
                double beforeArea = provinceAreaList.get(index);
                double area = currentArea + beforeArea;
                provinceAreaList.set(index, area);
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

        //title
        String title = getTitle(province,city,county);

        JSONObject result = new JSONObject();
        result.put("provinceData", provinceData);
        result.put("protectedObjectsData", protectedObjectsData);
        result.put("title", title);
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/query4Table", produces = "application/json;charset=UTF-8")
    public LayuiTableDataVO query4TableBefore(
            @RequestParam(defaultValue = "") String regionType,
            @RequestParam(defaultValue = "") String province,
            @RequestParam(defaultValue = "") String city,
            @RequestParam(defaultValue = "") String county,
            @RequestParam(defaultValue = "") String topic,
            @RequestParam(defaultValue = "") String subTopic,
            @RequestParam(defaultValue = "") String protectedObjects,
            @RequestParam(defaultValue = "-1") int startYear,
            @RequestParam(defaultValue = "-1") int endYear,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "0") int limit
    ){
        List<ProtectAreaBefore> data = null;
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

            protectedObjects = "%" + protectedObjects + "%";
            Date startDate = setStartDate(startYear);
            Date endDate = setEndDate(endYear);
            data = queryProtectArea(countyCodes, protectedObjects, startDate, endDate, page, limit);
            count = data.size();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        //title
        String title = getTitle(province,city,county);

        LayuiTableDataVO result = new LayuiTableDataVO();
        result.setCode(0);
        result.setMsg("");
        result.setCount(count);
        result.setData(data);
        return result;
    }

    private List<String> queryDistrictAdcode(String provinceCode, String cityCode, String countyCode){
        List<String> adcodeList = new ArrayList<>();
        Set<String> adcodeSet = new HashSet<>();
        if(!countyCode.isEmpty()){
            adcodeSet.add(countyCode);
        }
        else{
            if(!cityCode.isEmpty()){
                List<IAdcodeTO> countyAdcodes = adcodeService.findCounties(cityCode);
                for(int i=0; i<countyAdcodes.size(); i++){
                    adcodeSet.add(countyAdcodes.get(i).getCode());
                }
            }
            else{
                if(!provinceCode.isEmpty()){
                    List<IAdcodeTO> cityAdcodes = adcodeService.findCites(provinceCode);
                    for(int i=0; i<cityAdcodes.size(); i++){
                        List<IAdcodeTO> countyAdcodes = adcodeService.findCounties(cityAdcodes.get(i).getCode());
                        for(int j=0;j<countyAdcodes.size();j++){
                            adcodeSet.add(countyAdcodes.get(j).getCode());
                        }
                    }
                }

            }
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

    private List<ProtectAreaBefore> queryProtectArea(List<String> adcodes,
                                               String protectedObjects,
                                               Date startDate,
                                               Date endDate,
                                               int page,
                                               int limit){
        int offset = (page - 1) * limit;
        if(limit == 0) limit = Integer.MAX_VALUE;

        System.out.println("protectedObjects="+protectedObjects);

        if(adcodes.size() > 0){
            return protectAreaBeforeService.findAll(adcodes, protectedObjects, startDate, endDate, offset, limit);
        }
        else{
            return protectAreaBeforeService.findAll(protectedObjects, startDate, endDate, offset, limit);
        }
    }

    private Date setStartDate(int startYear){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, 1);
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        if(startYear != -1){
            calendar.set(Calendar.YEAR, startYear);
        }
        else{
            calendar.set(Calendar.YEAR, 1900);
        }
        return calendar.getTime();
    }

    private Date setEndDate(int endYear){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, 12);
        calendar.set(Calendar.DATE, 31);
        calendar.set(Calendar.HOUR, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        if(endYear != -1){
            calendar.set(Calendar.YEAR, endYear);
        }
        else{
            calendar.set(Calendar.YEAR, 2999);
        }
        return calendar.getTime();
    }

    private String getTitle(String province, String city, String county){
        String title = "全国";
        if(!province.isEmpty()){
            title = adcodeRepository.findByProvinceCode(province).get(0).getProvince();
        }
        if(!city.isEmpty()){
            title = adcodeRepository.findByCityCode(city).get(0).getProvince();
            title += adcodeRepository.findByCityCode(city).get(0).getCity();
        }
        if(!county.isEmpty()){
            title = adcodeRepository.findByCountyCode(county).get(0).getProvince();
            title += adcodeRepository.findByCountyCode(county).get(0).getCity();
            title += adcodeRepository.findByCountyCode(county).get(0).getCounty();
        }
        return title;
    }
}
