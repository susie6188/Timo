package com.linln.modules.protectArea.service;

import com.linln.common.enums.StatusEnum;
import com.linln.modules.protectArea.domain.ProtectAreaBefore;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author susie
 * @date 2020/11/26
 */
public interface ProtectAreaBeforeService {

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    Page<ProtectAreaBefore> getPageList(Example<ProtectAreaBefore> example);

    /**
     * 根据ID查询数据
     * @param id 主键ID
     */
    ProtectAreaBefore getById(Long id);

    /**
     * 保存数据
     * @param protectAreaBefore 实体对象
     */
    ProtectAreaBefore save(ProtectAreaBefore protectAreaBefore);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList);

    List getLevel();
}