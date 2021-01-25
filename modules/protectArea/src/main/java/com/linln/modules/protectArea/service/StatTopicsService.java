package com.linln.modules.protectArea.service;

import com.linln.common.enums.StatusEnum;
import com.linln.modules.protectArea.domain.IAdcodeTO;
import com.linln.modules.protectArea.domain.ITopicTO;
import com.linln.modules.protectArea.domain.StatTopics;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author susie
 * @date 2021/01/18
 */
public interface StatTopicsService {

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    Page<StatTopics> getPageList(Example<StatTopics> example);

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    StatTopics getById(Long id);

    /**
     * 保存数据
     * @param statTopics 实体对象
     */
    StatTopics save(StatTopics statTopics);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList);

    List<ITopicTO> findTopics();

    List<ITopicTO> findSubTopics(String topic);

    List<StatTopics> findAll(String topic, String subTopic);
    long count(String topic, String subTopic);
}