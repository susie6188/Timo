package com.linln.modules.protectArea.domain;

import lombok.AllArgsConstructor;
import lombok.Data;


public class AdcodeTO implements  IAdcodeTO{

    private String code;
    private String name;
    private int depth;

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getCode() {
        return null;
    }
}
