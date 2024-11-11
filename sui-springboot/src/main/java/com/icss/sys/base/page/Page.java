package com.icss.sys.base.page;


import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 分页对象
 * @param <T>
 */
public class Page<T> {
    private String code="00000";
    private String msg;
    private long count;
    private List<T> data;

    private Integer page=1;//默认值
    private Integer limit=1000000000;//默认值

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Page<T> getPageList(List list, Page<T> page) {
        //将myBtis分页转换成layui分页
        PageInfo<T> pageInfo = new PageInfo<>(list);
        page.setCode("00000");
        page.setCount(pageInfo.getTotal());
        page.setData(pageInfo.getList());
        return page;
    }

}
