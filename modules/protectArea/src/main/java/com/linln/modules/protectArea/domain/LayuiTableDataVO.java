package com.linln.modules.protectArea.domain;

import java.util.List;

public class LayuiTableDataVO implements ILayuiTableData{

    private int code;
    private String msg;
    private long count;
    private List<?> data;

    public LayuiTableDataVO() {
    }

    public LayuiTableDataVO(int code, String msg, long count, List<Object> data) {
        this.code = code;
        this.msg = msg;
        this.count = count;
        this.data = data;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }

    @Override
    public long getCount() {
        return this.count;
    }

    @Override
    public List<?> getData() {
        return this.data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public void setData(List<?> data) {
        this.data = data;
    }
}
