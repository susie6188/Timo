package com.linln.modules.protectArea.domain;

import java.util.Date;

public interface IProtectAreaTO {

    public Long getId();
    public String getName();
    public String getLevel();
    public String getCategory();
    public String getLongitude();
    public String getLatitude();
    public String getFourRange();
    public Double getInitialArea();
    public Double getCurrentArea();
    public Double getLandArea();
    public Double getSeaArea();
    public Long getOriginalTime();
    public String getOriginalDocumentNo();
    public Long getPromotionProvincialLevelTime();
    public Double getPromotionProvincialLevelArea();
    public String getPromotionProvincialLevelNo();
    public Long getPromotionNationalLevelTime();
    public Double getPromotionNationalLevelArea();
    public String getPromotionNationalLevelNo();
    public String getProvince();
    public String getCity();
    public String getCounty();
    public String getProtectedObjects();
    public String getIsIndependentInstitution();
    public String getInstitutionLevel();
    public Long getInstitutionEstablishmentTime();
    public String getInstitutionNature();
    public String getInstitutionName();
    public String getInstitutionAffiliation();
    public String getIsMasterPlan();
    public String getReplyTime();
    public String getFunctionalPartition();
    public String getNameBefore();
    public String getRemarks();
    public Date getCreateDate();
    public Date getUpdateDate();
    public Byte getStatus();
    public int getLocationCount();

}