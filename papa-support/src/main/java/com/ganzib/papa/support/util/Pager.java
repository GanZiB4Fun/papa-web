package com.ganzib.papa.support.util;

import com.baomidou.mybatisplus.plugins.Page;

import java.io.Serializable;

public class Pager implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static final int DEFAULT_PAGE_INDEX = 1;
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int DEFAULT_REC_COUNT = 0;
    public static final int DEFAULT_PAGE_INDEX_OFFSET = 9; //

    private boolean isRecCountSet = false;
    private Page<?> page;

    private int pageIndex; //页码
    private int pageSize; // d
    private int recCount; // 总记录条数
    private int pageCount; // 记录总页数
    private int skipNo; // 跳过查询的行号

    private int pageStart; // 第一位页码
    private int pageEnd; // 最后一位页码
    private int pageIndexOffset; // 页面�?��显示的页码个�?

    public Pager() {
        this.pageIndex = DEFAULT_PAGE_INDEX;
        this.pageSize = DEFAULT_PAGE_SIZE;
        this.recCount = DEFAULT_REC_COUNT;
        this.pageCount = 1;
        this.skipNo = 0;
        this.pageStart = 1;
        this.pageEnd = 1;
        this.setPageIndexOffset(DEFAULT_PAGE_INDEX_OFFSET);
    }

    public void reset() {
        this.pageIndex = DEFAULT_PAGE_INDEX;
        this.pageSize = DEFAULT_PAGE_SIZE;
        this.recCount = DEFAULT_REC_COUNT;
        this.pageCount = 1;
        this.skipNo = 0;
        this.pageStart = 1;
        this.pageEnd = 1;
    }

    /**
     * 获取当前页的序号起始页
     *
     * @return
     */
    public int getSequenceStart() {
        return (getPageIndex() - 1) * getPageSize() + 1;
    }

    private void setPageStartAndPageEnd() {
        if (isRecCountSet) {
            setSkipNo(getPageSize() * (getPageIndex() - 1));
            int offset = (getPageIndexOffset() + 1) / 2;
            if (getPageIndex() < offset) {
                setPageStart(1);
                if (getPageCount() > getPageIndexOffset()) {
                    setPageEnd(getPageIndexOffset());
                } else {
                    setPageEnd(getPageCount());
                }
            } else if (getPageIndex() > getPageCount() - offset + 1) {
                if (getPageCount() - getPageIndexOffset() < 1) {
                    setPageStart(1);
                } else {
                    setPageStart(getPageCount() - getPageIndexOffset() + 1);
                }
                setPageEnd(getPageCount());
            } else {
                setPageStart(getPageIndex() - offset + 1);
                setPageEnd(getPageIndex() + offset - 1);
            }
        }
    }

    // Setters
    public void setPageIndex(int pageIndex) {
        if (pageIndex <= 0) {
            pageIndex = DEFAULT_PAGE_INDEX;
        }
        this.pageIndex = pageIndex;
        if (isRecCountSet) {
            if (getPageIndex() > getPageCount()) {
                this.pageIndex = getPageCount();
            }
        }
        setPageStartAndPageEnd();
    }

    public void setPageSize(int pageSize) {
        if (pageSize <= 0) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        this.pageSize = pageSize;
        setPageIndex(getPageIndex());
    }

    public void setRecCount(int recCount) {
        if (recCount <= 0) {
            recCount = DEFAULT_REC_COUNT;
        }
        this.recCount = recCount;
        if (getRecCount() != 0) {
            if (recCount % getPageSize() == 0) {
                setPageCount(getRecCount() / getPageSize());
            } else {
                setPageCount(getRecCount() / getPageSize() + 1);
            }
        } else {
            setPageCount(1);
        }
        // 以下代码是为了将�?���?��记录全部删除后页面能正确跳转到更新后的最后一页
        if (getPageIndex() > getPageCount()) {
            setPageIndex(getPageCount());
        }
        isRecCountSet = true;
        setPageStartAndPageEnd();
    }

    public void setPageIndexOffset(int pageIndexOffset) {
        if (pageIndexOffset <= 0) {
            pageIndexOffset = DEFAULT_PAGE_INDEX_OFFSET;
        }
        this.pageIndexOffset = pageIndexOffset;
        setPageStartAndPageEnd();
    }

    // Private setters
    private void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    private void setSkipNo(int skipNo) {
        this.skipNo = skipNo;
    }

    private void setPageStart(int pageStart) {
        this.pageStart = pageStart;
    }

    private void setPageEnd(int pageEnd) {
        this.pageEnd = pageEnd;
    }

    // Getters
    public int getPageIndex() {
        return pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getRecCount() {
        return recCount;
    }

    public int getPageCount() {
        return pageCount;
    }

    public int getSkipNo() {
        return skipNo;
    }

    public int getPageStart() {
        return pageStart;
    }

    public int getPageEnd() {
        return pageEnd;
    }

    public int getPageIndexOffset() {
        return pageIndexOffset;
    }

    @SuppressWarnings("rawtypes")
    public Page<?> getPage() {
        if (page == null) {
            page = new Page(this.getPageIndex(), this.getPageSize());
        }
        return page;
    }

    public void setPage(Page<?> page) {
        this.page = page;
    }

}
