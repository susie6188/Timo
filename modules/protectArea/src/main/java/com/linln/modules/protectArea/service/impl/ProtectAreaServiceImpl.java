package com.linln.modules.protectArea.service.impl;

import com.linln.common.data.PageSort;
import com.linln.common.enums.StatusEnum;
import com.linln.modules.protectArea.domain.ProtectArea;
import com.linln.modules.protectArea.repository.ProtectAreaRepository;
import com.linln.modules.protectArea.service.ProtectAreaService;
import com.linln.modules.system.domain.Dict;
import com.linln.modules.system.service.DictService;
import org.hibernate.query.criteria.internal.predicate.InPredicate;
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

                Predicate provincePredicate = null;
                if(provinces.length > 0) {
                    for(int i=0; i<provinces.length; i++){
                        // predicates.add(cb.like(provincePath, "%" + provinces[i] + "%"));
                        if(provincePredicate == null){
                            provincePredicate = cb.like(provincePath, "%" + provinces[i] + "%");
                        }
                        else{
                            provincePredicate = cb.or(provincePredicate, cb.like(provincePath, "%" + provinces[i] + "%"));
                        }
                    }
//                    predicates.add(provincePredicate);
                }

                Predicate cityPredicate = null;
                if(cities.length > 0) {
                    for(int i=0; i<cities.length; i++){
//                        predicates.add(cb.like(cityPath, "%" + cities[i] + "%"));
                        if(cityPredicate == null){
                            cityPredicate = cb.or(cb.like(cityPath, "%" + cities[i] + "%"));
                        }
                        else{
                            cityPredicate = cb.or(cityPredicate, cb.like(cityPath, "%" + cities[i] + "%"));
                        }
                    }
//                    predicates.add(cityPredicate);
                }

                Predicate countyPredict = null;
                if(counties.length > 0) {
                    for(int i=0;i<counties.length;i++){
                        if(countyPredict == null){
//                            predicates.add(cb.like(countyPath, "%" + counties[i] + "%"));
                            countyPredict = cb.like(countyPath, "%" + counties[i] + "%");
                        }
                        else{
                            countyPredict = cb.or(countyPredict, cb.like(countyPath, "%" + counties[i] + "%"));
                        }
                    }
                }

                Predicate protectedObjectsPredicate = null;
                if(protectedObjects != null && protectedObjects.length() > 0) {
//                    predicates.add(cb.like(protectedObjectsPath, "%" + protectedObjects + "%"));
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

//                    predicates.add(cb.greaterThan(replyTimePath, calendar.getTime()));
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

//                    predicates.add(cb.lessThan(replyTimePath, calendar.getTime()));
                    endYearPredicate = cb.lessThan(replyTimePath, calendar.getTime());
                }

//                Predicate[] array = new Predicate[predicates.size()];
//                query.where(predicates.toArray(array));

                Predicate predicate = cb.like(provincePath, "%");

                if(provincePredicate != null){
                    predicate = cb.and(predicate, provincePredicate);
                }
                if(cityPredicate != null){
                    predicate = cb.and(predicate, cityPredicate);
                }
                if(countyPredict != null){
                    predicate = cb.and(predicate, countyPredict);
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

}