package com.linln.modules.protectArea.service.impl;

import com.linln.modules.protectArea.domain.Area;
import com.linln.modules.protectArea.repository.AreaRepository;
import com.linln.modules.protectArea.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaRepository repository;

    @Override
    public Optional<Area> findById(long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Area> findByCode(String areacode) {
        return repository.findByAreacode(areacode);
    }

    @Override
    public List<Area> getSubAreas(long parentid) {
        return repository.findByParentid(0);
    }

    @Override
    public void save(Area entity) {
        repository.save(entity);
    }
}
