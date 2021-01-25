package com.linln.admin.protectArea.controller;

import com.linln.modules.protectArea.domain.Area;
import com.linln.modules.protectArea.domain.ProtectArea;
import com.linln.modules.protectArea.repository.ProtectAreaRepository;
import com.linln.modules.protectArea.service.AreaService;
import com.linln.modules.protectArea.service.ProtectAreaService;
import com.linln.modules.protectArea.service.impl.AreaServiceImpl;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("area/json")
public class AreaJsonController {

    @Autowired
    private AreaServiceImpl areaService;

    @RequestMapping("/province")
//    @RequiresPermissions()
    public List<Area> getProvince(Model model){
        List<Area> provinces = areaService.getSubAreas(0);
        return provinces;
    }

}
