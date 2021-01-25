package com.linln.admin.protectArea.controller;

import com.linln.modules.protectArea.domain.IAdcodeTO;
import com.linln.modules.protectArea.domain.ITopicTO;
import com.linln.modules.protectArea.service.impl.AdcodeServiceImpl;
import com.linln.modules.protectArea.service.impl.AreaServiceImpl;
import com.linln.modules.protectArea.service.impl.StatTopicsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/topic/json")
public class TopicJsonController {

    @Autowired
    private AreaServiceImpl areaService;

    @Autowired
    private StatTopicsServiceImpl statTopicsService;

    @RequestMapping("/topics")
    public List<ITopicTO> topics(){
        List<ITopicTO> result = statTopicsService.findTopics();
        return result;
    }

    @RequestMapping("/subTopics")
    public List<ITopicTO> subTopics(@RequestParam String topic){
        List<ITopicTO> result = statTopicsService.findSubTopics(topic);
        return result;
    }

}
