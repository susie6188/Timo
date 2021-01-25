package com.linln.modules.protectArea.service;

import com.linln.modules.protectArea.domain.Adcode;
import com.linln.modules.protectArea.domain.AdcodeTO;
import com.linln.modules.protectArea.domain.Area;
import com.linln.modules.protectArea.domain.IAdcodeTO;

import java.util.List;
import java.util.Optional;

public interface AdcodeService {

    public Optional<Adcode> findById(long id);

    public List<IAdcodeTO> findProvinces();

    public List<IAdcodeTO> findCites(String provinceCode);

    public List<IAdcodeTO> findCounties(String cityCode);

    public void save(Adcode entity);
}
