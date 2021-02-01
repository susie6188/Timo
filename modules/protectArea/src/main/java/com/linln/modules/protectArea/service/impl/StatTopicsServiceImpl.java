package com.linln.modules.protectArea.service.impl;

import com.linln.common.data.PageSort;
import com.linln.common.enums.StatusEnum;
import com.linln.modules.protectArea.domain.ITopicTO;
import com.linln.modules.protectArea.domain.ProtectArea;
import com.linln.modules.protectArea.domain.StatTopics;
import com.linln.modules.protectArea.repository.StatTopicsRepository;
import com.linln.modules.protectArea.service.StatTopicsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author susie
 * @date 2021/01/18
 */
@Service
public class StatTopicsServiceImpl implements StatTopicsService {

    @Autowired
    private StatTopicsRepository statTopicsRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public StatTopics getById(Long id) {
        return statTopicsRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<StatTopics> getPageList(Example<StatTopics> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return statTopicsRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param statTopics 实体对象
     */
    @Override
    public StatTopics save(StatTopics statTopics) {
        return statTopicsRepository.save(statTopics);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return statTopicsRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }

    @Override
    public List<ITopicTO> findTopics() {
        return statTopicsRepository.findTopics();
    }

    @Override
    public List<ITopicTO> findSubTopics(String topic) {
        return statTopicsRepository.findSubTopics(topic);
    }

    private Specification<StatTopics> getSpec(String topic, String subTopic){
        Specification<StatTopics> spec = new Specification<StatTopics>(){
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<StatTopics> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder cb){
                Path<String> topicsPath = root.get("topics");
                Path<String> subTopicsPath = root.get("subTopics");

                List<Predicate> predicates = new ArrayList<Predicate>();
                if(topic != null && !topic.isEmpty()) {
                    predicates.add(cb.like(topicsPath, "%" + topic + "%"));
                }

                if(subTopic != null && !subTopic.isEmpty()) {
                    predicates.add(cb.like(subTopicsPath, "%" + subTopic + "%"));
                }

                Predicate[] array = new Predicate[predicates.size()];
                query.where(predicates.toArray(array));

                return null;
            }
        };

        return spec;
    }

    @Override
    public List<StatTopics> findAll(String topic, String subTopic) {
        Specification<StatTopics> spec = getSpec(topic, subTopic);
        List<StatTopics> result = statTopicsRepository.findAll(spec);
        return result;

    }

    @Override
    public long count(String topic, String subTopic){
        Specification<StatTopics> spec = getSpec(topic, subTopic);
        long result = statTopicsRepository.count(spec);
        return result;
    }

    @Override
    public List<StatTopics> findAll() {
        return statTopicsRepository.findAll();
    }

    @Override
    public long count() {
        return statTopicsRepository.count();
    }
}