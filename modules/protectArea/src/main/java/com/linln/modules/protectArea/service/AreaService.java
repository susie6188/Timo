package com.linln.modules.protectArea.service;

import com.linln.modules.protectArea.domain.Area;

import java.util.List;
import java.util.Optional;

public interface AreaService {

    public Optional<Area> findById(long id);

    public Optional<Area> findByCode(String areaCode);

    public List<Area> getSubAreas(long parentid);

    public void save(Area entity);
}
