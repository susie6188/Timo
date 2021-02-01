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

    // 根据单一行政区查询
    private Specification<ProtectArea> getSpecByDistrict(String[] provinces, String[] cities, String[] counties, String protectedObjects, int startYear, int endYear){
        Specification<ProtectArea> spec = new Specification<ProtectArea>(){
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<ProtectArea> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder cb){
                Path<String> provincePath = root.get("province");
                Path<String> cityPath = root.get("city");
                Path<String> countyPath = root.get("county");
                Path<String> protectedObjectsPath = root.get("protectedObjects");
                Path<Date> replyTimePath = root.get("replyTime");

                List<Predicate> predicates = new ArrayList<Predicate>();

                Predicate predicate = null;
                if(provinces.length > 0 && cities.length >0 && counties.length > 0) {
                    for(int i=0; i<provinces.length; i++){
                        Predicate predict = cb.like(provincePath, "%" + provinces[i] + "%");
                        predict = cb.and(predict, cb.like(cityPath, "%" + cities[i] + "%"));
                        predict = cb.and(predict, cb.like(countyPath, "%" + counties[i] + "%"));
                        if(predicate == null){
                            predicate = predict;
                        }
                        else{
                            predicate = cb.or(predicate, predict);
                        }
                    }
                }

                Predicate protectedObjectsPredicate = null;
                if(protectedObjects != null && protectedObjects.length() > 0) {
                    protectedObjectsPredicate = cb.like(protectedObjectsPath, "%" + protectedObjects + "%");
                }

                Predicate startYearPredicate = null;
                if(startYear > 0) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, startYear);
                    calendar.set(Calendar.MONTH, 1);
                    calendar.set(Calendar.DATE, 1);
                    calendar.set(Calendar.HOUR, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);

                    startYearPredicate = cb.greaterThan(replyTimePath, calendar.getTime());
                }

                Predicate endYearPredicate = null;
                if(endYear > 0) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, endYear);
                    calendar.set(Calendar.MONTH, 12);
                    calendar.set(Calendar.DATE, 31);
                    calendar.set(Calendar.HOUR, 23);
                    calendar.set(Calendar.MINUTE, 59);
                    calendar.set(Calendar.SECOND, 59);

                    endYearPredicate = cb.lessThan(replyTimePath, calendar.getTime());
                }
                if(protectedObjectsPredicate != null){
                    predicate = cb.and(predicate, protectedObjectsPredicate);
                }
                if(startYearPredicate != null){
                    predicate = cb.and(predicate, startYearPredicate);
                }
                if(endYearPredicate != null){
                    predicate = cb.and(predicate, endYearPredicate);
                }

                query.where(predicate);
                return null;
            }
        };

        return spec;
    }

    @Override
    public List<ProtectArea> findAllByDistrict(String[] provinces, String[] cities, String[] counties, String protectedObjects, int startYear, int endYear) {
        Specification<ProtectArea> spec = getSpecByDistrict(provinces, cities, counties, protectedObjects, startYear, endYear);
        return protectAreaRepository.findAll(spec);
    }

    @Override
    public long countByDistrict(String provinces[], String[] cities, String[] counties, String protectedObjects, int startYear, int endYear){
        Specification<ProtectArea> spec = getSpecByDistrict(provinces, cities, counties, protectedObjects, startYear, endYear);
        return protectAreaRepository.count(spec);
    }

    @Override
    public List getCategory() {
        return protectAreaRepository.getCategory();
    }

    @Override
    public List<ProtectArea> findAllByAdcode(List<String> adcodes) {
        String adcodesStr = String.join(",", adcodes);
        List<IProtectAreaTO> objects = protectAreaRepository.findAllByAdcode(adcodes);

        List<ProtectArea> result = new ArrayList<ProtectArea>();
        for(int i=0;i<objects.size();i++){
            ProtectArea protectArea = new ProtectArea();
            BeanUtils.copyProperties(objects.get(i), protectArea);
            result.add(protectArea);
        }
        return result;
    }

}