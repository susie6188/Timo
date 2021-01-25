package com.linln.modules.protectArea.repository;

import com.linln.modules.protectArea.domain.ProtectArea;
import com.linln.modules.system.repository.BaseRepository;
import net.sf.ehcache.search.aggregator.Count;
import org.hibernate.query.NativeQuery;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author susie
 * @date 2020/11/26
 */
public interface ProtectAreaRepository extends BaseRepository<ProtectArea, Long>,
        JpaSpecificationExecutor<ProtectArea> {

    @Query(nativeQuery = true,value = "select distinct pa.level from pa_protect_area pa")
    List getLevel();

    @Query(nativeQuery = true,value = "" +
            "select ROUND(sum(pa.current_area),2) as currentAreaSum from pa_protect_area pa" +
            " where pa.level = :level"
            )
    List getAreaStatsByLevel(@Param("level")String level);

    @Query(nativeQuery = true,value = "" +
            "select ROUND(sum(pa.current_area),2) as currentAreaSum, pa.level from pa_protect_area pa" +
            " group by pa.level"
    )
    List getAreaStatsByLevel();


    @Query(nativeQuery = true,value = "" +
            "select ROUND(sum(pa.current_area),2) as currentAreaSum from pa_protect_area pa"
    )
    String getAreaStats();

    @Query(nativeQuery = true,value = "" +
            "select count(*) as count from pa_protect_area pa"
    )
    String getCount();

    @Query(nativeQuery = true,value = "" +
            "select ROUND(sum(pa.current_area),2) as currentAreaSum from pa_protect_area pa" +
            " where pa.category = :category"
    )
    String getAreaStatsByCategory(@Param("category")String category);

    @Query(nativeQuery = true,value = "" +
            "select count(*) as count from pa_protect_area pa" +
            " where pa.category = :category"
    )
    String getCountStatsByCategory(@Param("category")String category);


    @Query(nativeQuery = true,value = "select distinct pa.category from pa_protect_area pa")
    List getCategory();


}