package com.linln.modules.protectArea.service;

import com.linln.common.enums.StatusEnum;
import com.linln.modules.protectArea.domain.ProtectArea;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author susie
 * @date 2020/11/26
 */
public interface ProtectAreaService {

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    Page<ProtectArea> getPageList(Example<ProtectArea> example);

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    ProtectArea getById(Long id);

    /**
     * 保存数据
     * @param protectArea 实体对象
     */
    ProtectArea save(ProtectArea protectArea);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList);

    /**
     * 查询保护地级别
     * @return
     */
    List getLevel();

    /**
     * 查询保护地类别
     * @return
     */
    List getCategory();

    List<ProtectArea> findAll();

    List<ProtectArea> findAllByDistrict(String[] provinces, String[] cities, String[] counties, String protectedObjects, int startYear, int endYear);
    long countByDistrict(String[] provinces, String[] cities, String[] counties, String protectedObjects, int startYear, int endYear);

    List<ProtectArea> findAllByAdcode(List<String> adcodes);
}