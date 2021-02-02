package com.linln.modules.protectArea.service.impl;

import com.linln.common.data.PageSort;
import com.linln.common.enums.StatusEnum;
import com.linln.modules.protectArea.domain.IProtectAreaTO;
import com.linln.modules.protectArea.domain.ProtectArea;
import com.linln.modules.protectArea.repository.ProtectAreaRepository;
import com.linln.modules.protectArea.service.ProtectAreaService;
import com.linln.modules.system.domain.Dict;
import com.linln.modules.system.service.DictService;
import org.hibernate.query.criteria.internal.predicate.InPredicate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

/**
 * @author susie
 * @date 2020/11/26
 */
@Service
public class ProtectAreaServiceImpl implements ProtectAreaService {

    @Autowired
    private ProtectAreaRepository protectAreaRepository;

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public ProtectArea getById(Long id) {
        return protectAreaRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<ProtectArea> getPageList(Example<ProtectArea> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return protectAreaRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param protectArea 实体对象
     */
    @Override
    public ProtectArea save(ProtectArea protectArea) {
        return protectAreaRepository.save(protectArea);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return protectAreaRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }

    @Override
    public List getLevel() {
        return protectAreaRepository.getLevel();
    }

    @Override
    public List<ProtectArea> findAll() {
        return protectAreaRepository.findAll();
    }

    @Override
    public List<ProtectArea> findAll(List<String> adcodes, String protectedObjects, Date startDate, Date endDate, int offset, int limit) {
        String adcodesStr = String.join(",", adcodes);
        List<IProtectAreaTO> objects = protectAreaRepository.findAll(adcodes, protectedObjects, startDate, endDate, offset, limit);

        List<ProtectArea> result = new ArrayList<ProtectArea>();
        for(int i=0;i<objects.size();i++){
            ProtectArea protectArea = new ProtectArea();
            BeanUtils.copyProperties(objects.get(i), protectArea);
            result.add(protectArea);
        }
        return result;
    }

    @Override
    public List<ProtectArea> findAll(String protectedObjects, Date startDate, Date endDate, int offset, int limit) {
        List<IProtectAreaTO> objects = protectAreaRepository.findAll(protectedObjects, startDate, endDate, offset, limit);

        List<ProtectArea> result = new ArrayList<ProtectArea>();
        for(int i=0;i<objects.size();i++){
            ProtectArea protectArea = new ProtectArea();
            BeanUtils.copyProperties(objects.get(i), protectArea);
            result.add(protectArea);
        }
        return result;
    }


    @Override
    public List getCategory() {
        return protectAreaRepository.getCategory();
    }

}