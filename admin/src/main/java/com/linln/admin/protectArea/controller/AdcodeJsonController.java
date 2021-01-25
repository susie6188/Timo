package com.linln.admin.protectArea.controller;

import com.linln.modules.protectArea.domain.AdcodeTO;
import com.linln.modules.protectArea.domain.Area;
import com.linln.modules.protectArea.domain.IAdcodeTO;
import com.linln.modules.protectArea.service.impl.AdcodeServiceImpl;
import com.linln.modules.protectArea.service.impl.AreaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/adcode/json")
public class AdcodeJsonController {

    @Autowired
    private AreaServiceImpl areaService;

    @Autowired
    private AdcodeServiceImpl adcodeService;

    @RequestMapping("/provinces")
    public List<IAdcodeTO> provinces(){
        List<IAdcodeTO> result = adcodeService.findProvinces();
        return result;
    }

    @RequestMapping("/cities")
    public List<IAdcodeTO> cities(@RequestParam String provinceCode){
        List<IAdcodeTO> result = adcodeService.findCites(provinceCode);
        return result;
    }

    @RequestMapping("/counties")
    public List<IAdcodeTO> counties(@RequestParam String cityCode){
        List<IAdcodeTO> result = adcodeService.findCounties(cityCode);
        return result;
    }
}
