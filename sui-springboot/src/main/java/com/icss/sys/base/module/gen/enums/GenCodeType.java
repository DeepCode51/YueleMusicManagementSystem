package com.icss.sys.base.module.gen.enums;

import com.icss.sys.base.properteis.GenProperteis;
import com.icss.sys.base.module.gen.service.GenPathService;
import com.icss.sys.base.module.gen.entity.GenDomain;
import com.icss.sys.utils.admin.SpringContextHolder;

import java.util.ArrayList;
import java.util.List;

//文件配置

/**
 * 定时任务启动时执行
 */
public enum GenCodeType {
    ALL(0),
    JAVA(1),
    WEB(2),
    WX(3),
    API(4),;
    private Integer value;

    public static String part = "";
    public static String createPath = "";
    public static String fileName = "";

    //获取路径变量
    public static void getCreatePath(String fileType, String fileModule, GenDomain genDomain, GenProperteis genProperteis) {
        GenPathService genPathService = (GenPathService) SpringContextHolder.getBean("genPathService");
        if (fileType.equals("entity")) {
            part = "";
            createPath = genPathService.getBootPathDir()+"/" + fileModule + "/entity";
        } else if (fileType.equals("controller")) {
            part = "Controller";
            createPath = genPathService.getBootPathDir()+"/" + fileModule + "/web";
        } else if (fileType.equals("apiController")) {
            part = "Controller";
            createPath = genPathService.getWxApiPathDir();
        } else if (fileType.equals("service")) {
            part = "Service";
            createPath = genPathService.getBootPathDir()+"/" + fileModule + "/service";
        } else if (fileType.equals("dao") || fileType.equals("daoXml")) {
            part = "Dao";
            createPath = genPathService.getBootPathDir()+"/" + fileModule + "/dao";
        } else if (fileType.equals("jsp_add")) {
            part = "Add";
        } else if (fileType.equals("jsp_edit")) {
            part = "Form";
        } else if (fileType.equals("jsp_view")) {
            part = "View";
        } else if (fileType.equals("jsp_manage")) {
            part = "Manage";
        } else if (fileType.equals("vue_details")) {
            part = "Details";
        } else if (fileType.equals("vue_list")) {
            part = "List";
        }
    }

    public static List<String> getFileTypes(GenCodeType genCodeType) {
        List list = new ArrayList();
        if (genCodeType.equals(ALL)) {
            list.add("vue_details");
            list.add("vue_list");
            list.add("jsp_add");
            list.add("jsp_edit");
            list.add("jsp_view");
            list.add("jsp_manage");
            list.add("entity");
            list.add("controller");
            list.add("apiController");
            list.add("service");
            list.add("dao");
            list.add("daoXml");
        } else if (genCodeType.equals(JAVA)) {
            list.add("entity");
            list.add("controller");
            list.add("service");
            list.add("dao");
        } else if (genCodeType.equals(WEB)) {
            list.add("jsp_edit");
            list.add("jsp_manage");
        } else if (genCodeType.equals(WX)) {
            list.add("vue_details");
            list.add("vue_list");
        }else if (genCodeType.equals(API)) {
            list.add("apiController");
        }
        return list;
    }


    private GenCodeType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public boolean isEqual(Integer value) {
        if (value == this.value) {
            return true;
        }
        return false;
    }
}
