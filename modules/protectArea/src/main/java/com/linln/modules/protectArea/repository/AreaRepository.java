package com.linln.modules.protectArea.repository;

import com.linln.modules.protectArea.domain.Area;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AreaRepository extends JpaRepository<Area, Long> {
    List<Area> findByParentid(int i);
    Optional<Area> findByAreacode(String areacode);
}
