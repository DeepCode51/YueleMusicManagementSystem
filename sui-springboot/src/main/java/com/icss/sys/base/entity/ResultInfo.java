package com.icss.sys.base.entity;

import java.util.List;
import java.util.Map;

/**
 * 返回结果类
 */
public class ResultInfo {
    private Integer code;//操作信息
    private String info;//操作信息
    private Boolean isOk;//操作结果
    private Map<String, Object> data; // 操作返回数据
    private Object obj; // 操作返回数据
    private List list; // 操作返回数据

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Boolean getIsOk() {
        return isOk;
    }

    public void setIsOk(Boolean isOk) {
        this.isOk = isOk;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    /**
     * 返回成功信息
     * @param info
     * @return
     */
    public static ResultInfo ok(String info) {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.code = 200;
        resultInfo.info = info;
        resultInfo.isOk=true;
        return resultInfo;
    }

    /**
     * 返回成功数据
     * @param info
     * @param map
     * @return
     */
    public static ResultInfo ok(String info, Map map) {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.data = map;
        resultInfo.code = 200;
        resultInfo.info = info;
        resultInfo.isOk=true;
        return resultInfo;
    }
    /**
     * 返回成功数据
     * @param info
     * @param list
     * @return
     */
    public static ResultInfo ok(String info, List list) {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.list = list;
        resultInfo.code = 200;
        resultInfo.info = info;
        resultInfo.isOk=true;
        return resultInfo;
    }
    /**
     * 返回成功数据
     * @param info
     * @param obj
     * @return
     */
    public static ResultInfo ok(String info, Object obj) {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.obj = obj;
        resultInfo.code = 200;
        resultInfo.info = info;
        resultInfo.isOk=true;
        return resultInfo;
    }

    /**
     * 返回失败信息
     * @param info
     * @return
     */
    public static ResultInfo error(String info) {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.info = info;
        resultInfo.code = 400;
        resultInfo.isOk = false;
        return resultInfo;
    }

    /**
     * 返回失败数据
     * @param info
     * @param map
     * @return
     */
    public static ResultInfo error(String info, Map map) {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.data = map;
        resultInfo.code = 400;
        resultInfo.isOk = false;
        return resultInfo;
    }

}
