package com.linln.modules.protectArea.repository;

import com.linln.modules.protectArea.domain.Adcode;
import com.linln.modules.protectArea.domain.AdcodeTO;
import com.linln.modules.protectArea.domain.Area;
import com.linln.modules.protectArea.domain.IAdcodeTO;
import com.linln.modules.system.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AdcodeRepository extends JpaRepository<Adcode, Long> {
    @Query(nativeQuery = true, value = "select distinct province as name, province_code as code from adcode")
    List<IAdcodeTO> findProvinces();

    @Query(nativeQuery = true, value = "select distinct city as name, city_code as code from adcode where province_code = :provinceCode")
    List<IAdcodeTO> findCites(@Param("provinceCode") String provinceCode);

    @Query(nativeQuery = true, value = "select county as name, county_code as code from adcode where city_code = :cityCode")
    List<IAdcodeTO> findCounties(@Param("cityCode") String cityCode);
}
