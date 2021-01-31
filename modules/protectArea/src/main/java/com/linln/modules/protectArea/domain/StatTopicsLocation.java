package com.linln.modules.protectArea.domain;

import javax.persistence.*;

@Entity
@Table(name="stat_location")
public class StatTopicsLocation {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column
    private String adcode;

    @ManyToOne
    private StatTopics statTopics;

    @Transient
    private String province;

    @Transient
    private String city;

    @Transient
    private String county;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public StatTopics getStatTopics() {
        return statTopics;
    }

    public void setStatTopics(StatTopics statTopics) {
        this.statTopics = statTopics;
    }
}
