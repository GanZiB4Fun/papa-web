package com.ganzib.papa.app.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RespClient<T> extends RespBase {

    private Integer total = 0;

    private Integer pageCount = 0;

    private List<T> rows = new ArrayList<T>();
    
    private List<T> footer;
    
    private Map<String, Object> otherInfo;

    public Map<String, Object> getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(Map<String, Object> otherInfo) {
        this.otherInfo = otherInfo;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

	public List<T> getFooter() {
		return footer;
	}

	public void setFooter(List<T> footer) {
		this.footer = footer;
	}
    
    
}
