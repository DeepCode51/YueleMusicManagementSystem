package com.icss.sys.base.module.gen.service;

import com.icss.sys.base.properteis.GenProperteis;
import com.icss.sys.base.module.gen.dao.GenMysqlDao;
import com.icss.sys.base.module.gen.entity.Gen;
import com.icss.sys.base.module.gen.entity.GenDomain;
import com.icss.sys.base.module.gen.entity.GenTableColumn;
import com.icss.sys.base.module.gen.enums.GenCodeType;
import com.icss.sys.utils.admin.SpringContextHolder;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GenCreator {

    /**
     * 代码生成主方法
     *
     * @param genDomain
     * @return
     * @throws IOException
     */
    public static boolean gen(GenCodeType genCodeType, GenDomain genDomain, GenProperteis genProperteis){
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
            String path = System.getProperty("user.dir")+ "/src/main/java/com/admin/sys/gen/freemarker";
            cfg.setDirectoryForTemplateLoading(new File(path));
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            if (genCodeType.equals(GenCodeType.ALL)) {
                createCode(cfg, GenCodeType.JAVA, genDomain,genProperteis);
                createCode(cfg, GenCodeType.WEB, genDomain,genProperteis);
//                createCode(cfg, GenCodeType.WX, genDomain,genProperteis);
                createCode(cfg, GenCodeType.API, genDomain,genProperteis);
            } else {
                createCode(cfg, genCodeType, genDomain,genProperteis);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 生产代码
     *
     * @param genDomain
     */
    private static void createCode(Configuration cfg, GenCodeType genCodeType, GenDomain genDomain,GenProperteis genProperteis) throws IOException {
        //获取对应类型的文件
        List<String> fileTypes = GenCodeType.getFileTypes(genCodeType);
        GenPathService genPathService = (GenPathService) SpringContextHolder.getBean("genPathService");
        for (String fileType : fileTypes) {
            Template temp = cfg.getTemplate(fileType + ".ftl");
            Map<String, Object> root = new GenCreator().getStringObjectMap(genDomain, fileType);
            String tail = fileType.equals("daoXml") ? ".xml" : ".java";
            String fileModule = getInstanceName(root.get("tableName").toString());
            GenCodeType.fileName = getClassName(root.get("tableName").toString());
            if (genCodeType.equals(GenCodeType.JAVA)) {
                GenCodeType.getCreatePath(fileType, fileModule, genDomain,genProperteis);
            } else if (genCodeType.equals(GenCodeType.WEB)) {//后台页面
                GenCodeType.fileName = genDomain.getModelName();
                GenCodeType.createPath = genPathService.getWebPagePathDir()+"/" + fileModule;
                tail = ".vue";
                GenCodeType.getCreatePath(fileType, fileModule, genDomain,genProperteis);
            } else if (genCodeType.equals(GenCodeType.WX)) {//微信页面
                GenCodeType.fileName = genDomain.getModelName();
                GenCodeType.createPath = genPathService.getWxPagePathDir()+"/" + fileModule;
                tail = ".vue";
                GenCodeType.getCreatePath(fileType, fileModule, genDomain,genProperteis);
            } else if (genCodeType.equals(GenCodeType.API)) {//接口
                GenCodeType.fileName = "Api"+GenCodeType.fileName;
                GenCodeType.getCreatePath(fileType, fileModule, genDomain,genProperteis);
            }
            File dir = new File(GenCodeType.createPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            try {
                OutputStream fos = new FileOutputStream(new File(dir, GenCodeType.fileName + GenCodeType.part + tail)); //java文件的生成目录
                Writer out = new OutputStreamWriter(fos, "utf-8");
                temp.process(root, out);
                fos.flush();
                fos.close();
            } catch (TemplateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("gen " + fileType + " success!");
        }


    }


    //从数据库获取数据
    private Map<String, Object> getStringObjectMap(GenDomain genDomain, String fileType) {
        Gen gen = new Gen();
        gen.setTableName(genDomain.getTableName());
        GenTableColumn genTableColumn = new GenTableColumn();
        genTableColumn.setTableName(genDomain.getTableName());
        GenMysqlDao genMysqlDao = SpringContextHolder.getBean(GenMysqlDao.class);
        List<GenTableColumn> columList = genMysqlDao.findColumnList(genTableColumn);
        Map<String, Object> root = new HashMap<>();
        root.put("packageName", "com.admin.module." + getInstanceName(gen.getTableName()));
        root.put("className", getClassName(gen.getTableName()));
        root.put("tableName", gen.getTableName());
        root.put("isPort", genDomain.getIsPort());
        root.put("isFileList", genDomain.getIsFileList());
        root.put("comments", genDomain.getComments().replace("表", "信息"));//表描述
        root.put("author", "zenglf");

        List<GenTableColumn> limitList = new ArrayList<>();
        if ("entity".equals(fileType)) {
            for (GenTableColumn tableColumn : columList) {
                if (!(tableColumn.getColumnName().equals("create_by") || tableColumn.getColumnName().equals("create_date") || tableColumn.getColumnName().equals("update_by") || tableColumn.getColumnName().equals("update_date") || tableColumn.getColumnName().equals("del_flag"))) {
                    limitList.add(tableColumn);
                }
            }
            columList = limitList;
        }
        root.put("attrs", columList);
        return root;
    }

    //获取实例名称
    public static String getInstanceName(String srcStr) {
        String columnName = getColumnFileName(srcStr);
        return columnName.substring(0, 1).toLowerCase() + columnName.substring(1);
    }

    //将数据库列名转换成属性名
    public static String getColumnFileName(String srcStr) {
        srcStr = srcStr.toLowerCase();
        String org = "_";
        String newString = "";
        int first = 0;
        while (srcStr.indexOf(org) != -1) {
            first = srcStr.indexOf(org);
            if (first != srcStr.length()) {
                newString = newString + srcStr.substring(0, first);
                srcStr = srcStr.substring(first + org.length(), srcStr.length());
                srcStr = getParamName(srcStr);
            }
        }
        newString = newString + srcStr;
        return newString;
    }

    //将表名转换类名
    public static String getParamName(String srcStr) {
        return srcStr.substring(0, 1).toUpperCase() + srcStr.substring(1).toLowerCase();
    }

    //将表名转换类名
    public static String getClassName(String srcStr) {
        String columnName = getColumnFileName(srcStr);
        return columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
    }

}
