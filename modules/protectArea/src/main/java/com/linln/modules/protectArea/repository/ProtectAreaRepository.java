package com.linln.modules.protectArea.repository;

import com.linln.modules.protectArea.domain.IProtectAreaTO;
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

    @Query(nativeQuery = true, value = "SELECT " +
            "t1.id, " +
            "t1.name, " +
            "t1.level, " +
            "t1.category, " +
            "t1.longitude, " +
            "t1.latitude, " +
            "t1.four_range as fourRange, " +
            "t1.initial_area as initialArea, " +
            "t1.current_area as currentArea, " +
            "t1.land_area as landArea, " +
            "t1.sea_area as seaArea, " +
            "t1.original_time as originalTime, " +
            "t1.original_document_no as originalDocumentNo, " +
            "t1.promotion_provincial_level_time as promotionProvincialLevelTime, " +
            "t1.promotion_provincial_level_area as promotionProvincialLevelArea, " +
            "t1.promotion_provincial_level_no as promotionProvincialLevelNo, " +
            "t1.promotion_national_level_time as promotionNationalLevelTime, " +
            "t1.promotion_national_level_area as promotionNationalLevelArea, " +
            "t1.promotion_national_level_no as promotionNationalLevelNo, " +
            "t1.province, " +
            "t1.city, " +
            "t1.county, " +
            "t1.protected_objects as protectedObjects, " +
            "t1.is_independent_institution as isIndependentInstitution, " +
            "t1.institution_level as institutionLevel, " +
            "t1.institution_establishment_time as institutionEstablishmentTime, " +
            "t1.institution_nature as institutionNature, " +
            "t1.institution_name as institutionName, " +
            "t1.institution_affiliation as institutionAffiliation, " +
            "t1.is_master_plan as isMasterPlan, " +
            "t1.reply_time as replyTime, " +
            "t1.functional_partition as functionalPartition, " +
            "t1.name_before as nameBefore, " +
            "t1.remarks, " +
            "t1.create_date as createDate, " +
            "t1.update_date as updateDate, " +
            "t1.status, " +
            "t1.location_count as locationCount " +
            "FROM pa_protect_area t1, pa_location t2 where t1.id = t2.protect_area_id and t2.adcode in (:adcodes)")
    List<IProtectAreaTO> findAllByAdcode(@Param(value = "adcodes")List<String> adcodes);

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