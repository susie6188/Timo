package com.linln;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linln.modules.protectArea.domain.Area;
import com.linln.modules.protectArea.service.impl.AreaServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.InputStream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BootApplicationTests {

    @Autowired
    private AreaServiceImpl areaCodeService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void insertAreas(){
        try {
            String filePath = "E:\\Workspaces\\IdeaProjects\\Timo\\admin\\src\\main\\resources\\areaCode.json";
            InputStream is = new FileInputStream(filePath);
            int size = is.available();
            byte[] bytes = new byte[size];
            is.read(bytes);
            String jsonStr = new String(bytes);
            is.close();

            JSONArray jsonArray = JSON.parseArray(jsonStr);
            int count = jsonArray.size();
            for(int i=0; i<jsonArray.size(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Area areaCode = new Area();
                areaCode.setId(jsonObject.getInteger("id"));
                areaCode.setName(jsonObject.getString("name"));
                areaCode.setParentid(jsonObject.getInteger("parentid"));
                areaCode.setParentname(jsonObject.getString("parentname"));
                areaCode.setAreacode(jsonObject.getString("areacode"));
                areaCode.setZipcode(jsonObject.getString("zipcode"));
                areaCode.setDepth(jsonObject.getInteger("depth"));

                System.out.print(String.format("saving %d/%d", (i+1), count));
                areaCodeService.save(areaCode);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}
