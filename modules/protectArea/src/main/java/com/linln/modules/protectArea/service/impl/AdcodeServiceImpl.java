package com.linln.modules.protectArea.service.impl;

import com.linln.modules.protectArea.domain.Adcode;
import com.linln.modules.protectArea.domain.AdcodeTO;
import com.linln.modules.protectArea.domain.IAdcodeTO;
import com.linln.modules.protectArea.repository.AdcodeRepository;
import com.linln.modules.protectArea.service.AdcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdcodeServiceImpl implements AdcodeService {

    @Autowired
    private AdcodeRepository repository;

    @Override
    public Optional<Adcode> findById(long id) {
        return repository.findById(id);
    }

    @Override
    public List<IAdcodeTO> findProvinces() {
        return repository.findProvinces();
    }

    @Override
    public List<IAdcodeTO> findCites(String provinceCode) {
        return repository.findCites(provinceCode);
    }

    @Override
    public List<IAdcodeTO> findCounties(String cityCode) {
        return repository.findCounties(cityCode);
    }

    @Override
    public void save(Adcode entity) {
        repository.save(entity);
    }
}
