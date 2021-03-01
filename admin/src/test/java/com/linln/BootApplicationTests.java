package com.linln;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linln.modules.protectArea.domain.*;
import com.linln.modules.protectArea.service.impl.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BootApplicationTests {

    @Autowired
    ProtectAreaServiceImpl protectAreaService;

    @Autowired
    ProtectAreaBeforeServiceImpl protectAreaBeforeService;

    @Autowired
    StatTopicsServiceImpl statTopicsService;

    @Autowired
    private AdcodeServiceImpl adcodeService;

    @Autowired
    private AreaServiceImpl areaCodeService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void insertAreas(){
//        try {
//            String filePath = "E:\\Workspaces\\IdeaProjects\\Timo\\admin\\src\\main\\resources\\areaCode.json";
//            InputStream is = new FileInputStream(filePath);
//            int size = is.available();
//            byte[] bytes = new byte[size];
//            is.read(bytes);
//            String jsonStr = new String(bytes);
//            is.close();

//            JSONArray jsonArray = JSON.parseArray(jsonStr);
//            int count = jsonArray.size();
//            for(int i=0; i<jsonArray.size(); i++){
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//
//                Area areaCode = new Area();
//                areaCode.setId(jsonObject.getInteger("id"));
//                areaCode.setName(jsonObject.getString("name"));
//                areaCode.setParentid(jsonObject.getInteger("parentid"));
//                areaCode.setParentname(jsonObject.getString("parentname"));
//                areaCode.setAreacode(jsonObject.getString("areacode"));
//                areaCode.setZipcode(jsonObject.getString("zipcode"));
//                areaCode.setDepth(jsonObject.getInteger("depth"));
//
//                System.out.print(String.format("saving %d/%d", (i+1), count));
//                areaCodeService.save(areaCode);
//            }
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }

        try {
            int id = 1;
            List<IAdcodeTO> provinces = adcodeService.findProvinces();
            for(int i=0; i<provinces.size(); i++){
                IAdcodeTO province = provinces.get(i);
                int provinceId= id;
                System.out.println(province.getName());

                Area provinceArea = new Area();
                provinceArea.setId(id++);
                provinceArea.setName(province.getName());
                provinceArea.setParentid(0);
                provinceArea.setParentname("");
                provinceArea.setAreacode(province.getCode());
                provinceArea.setZipcode("");
                provinceArea.setDepth(1);
                areaCodeService.save(provinceArea);

                // city
                List<IAdcodeTO> cities = adcodeService.findCites(province.getCode());
                for(int j=0; j<cities.size(); j++){
                    IAdcodeTO city = cities.get(j);
                    int cityId = id;

                    Area cityArea = new Area();
                    cityArea.setId(id++);
                    cityArea.setName(city.getName());
                    cityArea.setParentid(provinceId);
                    cityArea.setParentname(province.getName());
                    cityArea.setAreacode(city.getCode());
                    cityArea.setZipcode("");
                    cityArea.setDepth(2);
                    areaCodeService.save(cityArea);

                    // county
                    List<IAdcodeTO> counties = adcodeService.findCounties(city.getCode());
                    for(int k=0; k<counties.size(); k++){
                        IAdcodeTO county = counties.get(k);

                        Area countyArea = new Area();
                        countyArea.setId(id++);
                        countyArea.setName(county.getName());
                        countyArea.setParentid(cityId);
                        countyArea.setParentname(city.getName());
                        countyArea.setAreacode(county.getCode());
                        countyArea.setZipcode("");
                        countyArea.setDepth(3);
                        areaCodeService.save(countyArea);
                    }
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void parseProtectedArea(){
        List<ProtectArea> protectAreas = protectAreaService.findAll();
        for(int i=0;i<protectAreas.size();i++){
            ProtectArea protectArea = protectAreas.get(i);

            int locationCount = 0;
            List<ProjectAreaLocation> locations = new ArrayList<ProjectAreaLocation>();
            String[] provinces = {};
            String[] cities = {};
            String[] counties = {};
            if(protectArea.getProvince() != null) provinces = protectArea.getProvince().split(",");
            if(protectArea.getCity() != null) cities = protectArea.getCity().split(",");
            if(protectArea.getCounty() != null) counties = protectArea.getCounty().split(",");
//            System.out.println(provinces.length + "-" + cities.length + "-" + counties.length);

            for(int provinceIndex = 0;provinceIndex<provinces.length;provinceIndex++){
                for(int cityIndex = 0;cityIndex<cities.length;cityIndex++){
                    for(int countyIndex = 0;countyIndex<counties.length;countyIndex++){
                        List<Adcode> adcodes = adcodeService.findAllByProvinceAndCityAndCounty(provinces[provinceIndex], cities[cityIndex], counties[countyIndex]);

                        for(int adcodeIndex =0; adcodeIndex < adcodes.size(); adcodeIndex++){
                            Adcode adcode = adcodes.get(adcodeIndex);
                            ProjectAreaLocation location = new ProjectAreaLocation();
                            location.setAdcode(adcode.getCountyCode());
                            location.setProtectArea(protectArea);
                            locations.add(location);
                            locationCount++;
                        }
                    }
                }
            }

            protectArea.setLocations(locations);
            protectArea.setLocationCount(locationCount);

            System.out.println((i + 1) + "/" + protectAreas.size() + ": " + protectArea.getProvince() + "-" + protectArea.getCity() + "-" + protectArea.getCounty() + " " + locationCount);

            protectAreaService.save(protectArea);
        }
    }

    @Test
    public void parseStatTopics() {
        List<StatTopics> statTopicsList = statTopicsService.findAll();
        for (int i = 0; i < statTopicsList.size(); i++) {
            StatTopics statTopics = statTopicsList.get(i);

            int locationCount = 0;
            List<StatTopicsLocation> locations = new ArrayList<StatTopicsLocation>();
            String[] provinces = {};
            String[] counties = {};
            if (statTopics.getProvince() != null) provinces = statTopics.getProvince().split(",");
            if (statTopics.getCounty() != null) counties = statTopics.getCounty().split(",");
//            System.out.println(provinces.length + "-" + counties.length);

            for (int provinceIndex = 0; provinceIndex < provinces.length; provinceIndex++) {
                for (int countyIndex = 0; countyIndex < counties.length; countyIndex++) {
                    List<Adcode> adcodes = adcodeService.findAllByProvinceAndCounty(provinces[provinceIndex], counties[countyIndex]);

                    for (int adcodeIndex = 0; adcodeIndex < adcodes.size(); adcodeIndex++) {
                        Adcode adcode = adcodes.get(adcodeIndex);
                        StatTopicsLocation location = new StatTopicsLocation();
                        location.setAdcode(adcode.getCountyCode());
                        location.setStatTopics(statTopics);
                        locations.add(location);
                        locationCount++;
                    }
                }
            }

            statTopics.setLocations(locations);
            statTopics.setLocationCount(locationCount);

            System.out.println((i + 1) + "/" + statTopicsList.size() + ": " + statTopics.getProvince() + "-" + statTopics.getCounty() + " " + locationCount);

            statTopicsService.save(statTopics);
        }
    }

    @Test
    public void parseProtectedAreaBefore(){
        List<ProtectAreaBefore> protectAreas = protectAreaBeforeService.findAll();
        for(int i=0;i<protectAreas.size();i++){
            ProtectAreaBefore protectAreaBefore = protectAreas.get(i);

            int locationCount = 0;
            List<ProjectAreaBeforeLocation> locations = new ArrayList<ProjectAreaBeforeLocation>();
            String[] provinces = {};
            String[] cities = {};
            String[] counties = {};
            if(protectAreaBefore.getProvince() != null) provinces = protectAreaBefore.getProvince().split(",");
            if(protectAreaBefore.getCity() != null) cities = protectAreaBefore.getCity().split(",");
            if(protectAreaBefore.getCounty() != null) counties = protectAreaBefore.getCounty().split(",");
//            System.out.println(provinces.length + "-" + cities.length + "-" + counties.length);

            for(int provinceIndex = 0;provinceIndex<provinces.length;provinceIndex++){
                for(int cityIndex = 0;cityIndex<cities.length;cityIndex++){
                    for(int countyIndex = 0;countyIndex<counties.length;countyIndex++){
                        List<Adcode> adcodes = adcodeService.findAllByProvinceAndCityAndCounty(provinces[provinceIndex], cities[cityIndex], counties[countyIndex]);

                        for(int adcodeIndex =0; adcodeIndex < adcodes.size(); adcodeIndex++){
                            Adcode adcode = adcodes.get(adcodeIndex);
                            ProjectAreaBeforeLocation location = new ProjectAreaBeforeLocation();
                            location.setAdcode(adcode.getCountyCode());
                            location.setProtectAreaBefore(protectAreaBefore);
                            locations.add(location);
                            locationCount++;
                        }
                    }
                }
            }

            protectAreaBefore.setLocations(locations);
            protectAreaBefore.setLocationCount(locationCount);

            System.out.println((i + 1) + "/" + protectAreas.size() + ": " + protectAreaBefore.getProvince() + "-" + protectAreaBefore.getCity() + "-" + protectAreaBefore.getCounty() + " " + locationCount);

            protectAreaBeforeService.save(protectAreaBefore);
        }
    }

}
