package com.linln.modules.protectArea.service.impl;

import com.linln.common.data.PageSort;
import com.linln.common.enums.StatusEnum;
import com.linln.modules.protectArea.domain.IProtectAreaTO;
import com.linln.modules.protectArea.domain.ProtectArea;
import com.linln.modules.protectArea.domain.ProtectAreaBefore;
import com.linln.modules.protectArea.repository.ProtectAreaBeforeRepository;
import com.linln.modules.protectArea.service.ProtectAreaBeforeService;
import com.linln.modules.system.service.DictService;
import jdk.nashorn.internal.ir.ReturnNode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author susie
 * @date 2020/11/26
 */
@Service
public class ProtectAreaBeforeServiceImpl implements ProtectAreaBeforeService {

    @Autowired
    private ProtectAreaBeforeRepository protectAreaBeforeRepository;


    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    @Override
    @Transactional
    public ProtectAreaBefore getById(Long id) {
        return protectAreaBeforeRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<ProtectAreaBefore> getPageList(Example<ProtectAreaBefore> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest();
        return protectAreaBeforeRepository.findAll(example, page);
    }

    /**
     * 保存数据
     * @param protectAreaBefore 实体对象
     */
    @Override
    public ProtectAreaBefore save(ProtectAreaBefore protectAreaBefore) {
        return protectAreaBeforeRepository.save(protectAreaBefore);
    }

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> idList) {
        return protectAreaBeforeRepository.updateStatus(statusEnum.getCode(), idList) > 0;
    }

    @Override
    public List getLevel() {
        return protectAreaBeforeRepository.getLevel();
    }

    @Override
    public List<ProtectAreaBefore> findAll() {
        return protectAreaBeforeRepository.findAll();
    }

    @Override
    public List<ProtectAreaBefore> findAll(List<String> adcodes, String protectedObjects, Date startDate, Date endDate, int offset, int limit) {
        String adcodesStr = String.join(",", adcodes);
        List<IProtectAreaTO> objects = protectAreaBeforeRepository.findAll(adcodes, protectedObjects, startDate, endDate, offset, limit);

        List<ProtectAreaBefore> result = new ArrayList<ProtectAreaBefore>();
        for(int i=0;i<objects.size();i++){
            ProtectAreaBefore protectAreaBefore = new ProtectAreaBefore();
            BeanUtils.copyProperties(objects.get(i), protectAreaBefore);
            result.add(protectAreaBefore);
        }
        return result;
    }

    @Override
    public List<ProtectAreaBefore> findAll(String protectedObjects, Date startDate, Date endDate, int offset, int limit) {
        List<IProtectAreaTO> objects = protectAreaBeforeRepository.findAll(protectedObjects, startDate, endDate, offset, limit);

        List<ProtectAreaBefore> result = new ArrayList<ProtectAreaBefore>();
        for(int i=0;i<objects.size();i++){
            ProtectAreaBefore protectAreaBefore = new ProtectAreaBefore();
            BeanUtils.copyProperties(objects.get(i), protectAreaBefore);
            result.add(protectAreaBefore);
        }
        return result;
    }


    @Override
    public List getCategory() {
        return protectAreaBeforeRepository.getCategory();
    }

}