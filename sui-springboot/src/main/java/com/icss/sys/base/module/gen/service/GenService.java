package com.icss.sys.base.module.gen.service;

import com.icss.sys.base.entity.ResultInfo;
import com.icss.sys.base.properteis.JdbcProperteis;
import com.icss.sys.base.module.config.entity.SysConfig;
import com.icss.sys.base.module.config.service.SysConfigService;
import com.icss.sys.base.module.gen.dao.GenMysqlDao;
import com.icss.sys.base.module.gen.entity.Gen;
import com.icss.sys.base.module.gen.entity.GenTableColumn;
import com.icss.sys.base.module.gen.entity.GenTableConfig;
import com.icss.sys.base.module.menu.entity.SysMenu;
import com.icss.sys.base.module.menu.service.SysMenuService;
import com.icss.sys.base.module.role.entity.SysRole;
import com.icss.sys.base.module.role.service.SysRoleService;
import com.icss.sys.base.module.roleMenu.dao.SysRoleMenuDao;
import com.icss.sys.base.module.roleMenu.entity.SysRoleMenu;
import com.icss.sys.utils.admin.IdGen;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.icss.sys.base.module.gen.service.GenCreator.getInstanceName;


//机构信息接口类
@Service
public class GenService {

    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysRoleMenuDao sysRoleMenuDao;
    @Autowired
    private GenMysqlDao genMysqlDao;
    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private JdbcTemplate jdbcTemplate;//注入的方式
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private JdbcProperteis jdbcProperteis;

    public String getDBName() {
        String dbUrl = this.jdbcProperteis.getUrl();
        String str = dbUrl.substring(dbUrl.indexOf("/") + 1, dbUrl.lastIndexOf("?"));
        String db_name = str.split("/")[2];
        return db_name;
    }

    public ResultInfo genMenuList(boolean isPort,Gen gen) {
        //生成菜单数据
        String modelName = getInstanceName(gen.getId());
        SysMenu sysMenuManageF = new SysMenu();
        sysMenuManageF.setName(gen.getComments().replace("表", "管理"));
        List<SysMenu> list = sysMenuService.getList(sysMenuManageF);
        List<String> idsArr = new ArrayList<>();
        if (list.size() > 0) {
            return ResultInfo.error("菜单数据已经生成过了!");
        } else {
            SysMenu sysMenuManage = new SysMenu();
            String menuId = IdGen.primaryKey();
            sysMenuManage.setId(menuId);
            sysMenuManage.setPid("1");
            sysMenuManage.setSort("2000");
            sysMenuManage.setIsShow(1);
            sysMenuManage.setHref("/" + modelName + "/manage");
            sysMenuManage.setComponent("admin/module/"+modelName+"/"+modelName + "Manage.vue");
            sysMenuManage.setIcon("fa fa-envira");//设置默认图标
            sysMenuManage.setName(gen.getComments().replace("表", "管理"));
            sysMenuService.insert(sysMenuManage);
            idsArr.add(sysMenuManage.getId());

            SysMenu sysMenuList = new SysMenu();
            sysMenuList.setId(IdGen.primaryKey());
            sysMenuList.setPid(menuId);
            sysMenuList.setSort("1");
            sysMenuList.setIsShow(0);
            sysMenuList.setPermission(modelName + ":getPage");
            sysMenuList.setName(gen.getComments().replace("表", "查询"));
            sysMenuService.insert(sysMenuList);
            idsArr.add(sysMenuList.getId());

            SysMenu sysMenuView = new SysMenu();
            sysMenuView.setId(IdGen.primaryKey());
            sysMenuView.setPid(menuId);
            sysMenuView.setSort("2");
            sysMenuView.setIsShow(0);
            sysMenuView.setPermission(modelName + ":view");
            sysMenuView.setName(gen.getComments().replace("表", "查看"));
            sysMenuService.insert(sysMenuView);
            idsArr.add(sysMenuView.getId());

            SysMenu sysMenuAdd = new SysMenu();
            sysMenuAdd.setId(IdGen.primaryKey());
            sysMenuAdd.setPid(menuId);
            sysMenuAdd.setSort("3");
            sysMenuAdd.setIsShow(0);
            sysMenuAdd.setPermission(modelName + ":add");
            sysMenuAdd.setName(gen.getComments().replace("表", "新增"));
            sysMenuService.insert(sysMenuAdd);
            idsArr.add(sysMenuAdd.getId());

            SysMenu sysMenuEdit = new SysMenu();
            sysMenuEdit.setId(IdGen.primaryKey());
            sysMenuEdit.setPid(menuId);
            sysMenuEdit.setSort("4");
            sysMenuEdit.setIsShow(0);
            sysMenuEdit.setPermission(modelName + ":edit");
            sysMenuEdit.setName(gen.getComments().replace("表", "编辑"));
            sysMenuService.insert(sysMenuEdit);
            idsArr.add(sysMenuEdit.getId());

            SysMenu sysMenuSave = new SysMenu();
            sysMenuSave.setId(IdGen.primaryKey());
            sysMenuSave.setPid(menuId);
            sysMenuSave.setSort("5");
            sysMenuSave.setIsShow(0);
            sysMenuSave.setPermission(modelName + ":save");
            sysMenuSave.setName(gen.getComments().replace("表", "保存"));
            sysMenuService.insert(sysMenuSave);
            idsArr.add(sysMenuSave.getId());

            SysMenu sysMenuDelete = new SysMenu();
            sysMenuDelete.setId(IdGen.primaryKey());
            sysMenuDelete.setPid(menuId);
            sysMenuDelete.setSort("6");
            sysMenuDelete.setIsShow(0);
            sysMenuDelete.setPermission(modelName + ":delete");
            sysMenuDelete.setName(gen.getComments().replace("表", "删除"));
            sysMenuService.insert(sysMenuDelete);
            idsArr.add(sysMenuDelete.getId());

        if(isPort){
            SysMenu sysMenuExpor = new SysMenu();
            sysMenuExpor.setId(IdGen.primaryKey());
            sysMenuExpor.setPid(menuId);
            sysMenuExpor.setSort("7");
            sysMenuExpor.setIsShow(0);
            sysMenuExpor.setPermission(modelName + ":export");
            sysMenuExpor.setName(gen.getComments().replace("表", "导出"));
            sysMenuService.insert(sysMenuExpor);
            idsArr.add(sysMenuExpor.getId());

            SysMenu sysMenuImport = new SysMenu();
            sysMenuImport.setId(IdGen.primaryKey());
            sysMenuImport.setPid(menuId);
            sysMenuImport.setSort("8");
            sysMenuImport.setIsShow(0);
            sysMenuImport.setPermission(modelName + ":import");
            sysMenuImport.setName(gen.getComments().replace("表", "导入"));
            sysMenuService.insert(sysMenuImport);
            idsArr.add(sysMenuImport.getId());
        }
            //管理员免菜单配置
            SysRole devParams = new SysRole();
            devParams.setRoleCode("devRole");
            SysRole devRole = sysRoleService.get(devParams);
            for (String id : idsArr) {
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setId(IdGen.primaryKey());
                sysRoleMenu.setMenuId(id);
                sysRoleMenu.setRoleId(devRole.getId());
                sysRoleMenuDao.insert(sysRoleMenu);
            }
            SysRole adminParams = new SysRole();
            adminParams.setRoleCode("adminRole");
            SysRole adminRole = sysRoleService.get(adminParams);
            for (String id : idsArr) {
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setId(IdGen.primaryKey());
                sysRoleMenu.setMenuId(id);
                sysRoleMenu.setRoleId(adminRole.getId());
                sysRoleMenuDao.insert(sysRoleMenu);
            }
            return ResultInfo.ok("生成菜单成功!");
        }
    }
    //表配置属性
    public ResultInfo genConfigTable(String tableName,String tableDesc,boolean isPort,boolean isLock,boolean isFileList) throws Exception {
        try {
            JSONArray configsArray = new JSONArray();
            SysConfig sysConfig = new SysConfig();
            sysConfig.setCode("genTableConfigs");
            SysConfig setting = sysConfigService.get(sysConfig);
            if(setting==null){
                SysConfig entity = new  SysConfig();
                entity.setCode("genTableConfigs");
                JSONObject table = new JSONObject();
                JSONObject config = new JSONObject();
                config.put("isPort", isPort);
                config.put("isLock", isLock);
                config.put("isFileList", isFileList);
                config.put("tableName", tableName);
                config.put("tableDesc", tableDesc);
                table.put(tableName, config);
                configsArray.add(table);
                entity.setVal(configsArray.toString());
                sysConfigService.insert(entity);
            }else{
                configsArray = JSONArray.fromObject(setting.getVal());
                for (Object o : configsArray) {
                    JSONObject table = (JSONObject) o;
                    JSONObject config = (JSONObject) table.get(tableName);
                    if(config==null){
                        config = new JSONObject();
                        config.put("isPort", isPort);
                        config.put("isLock", isLock);
                        config.put("isFileList", isFileList);
                        config.put("tableName", tableName);
                        config.put("tableDesc", tableDesc);
                    }else{
                        config.put("isPort", isPort);
                        config.put("isLock", isLock);
                        config.put("isFileList", isFileList);
                        config.put("tableName", tableName);
                        config.put("tableDesc", tableDesc);
                    }
                    table.put(tableName, config);
                }
                setting.setName("代码配置");
                setting.setVal(configsArray.toString());
                sysConfigService.update(setting);
            }
            return ResultInfo.ok("操作成功");
        } catch (Exception e) {
            return ResultInfo.error("操作失败");
        }
    }

    @Transactional
    public Boolean saveGenColumData(GenTableConfig tableConfig, String tableName, String tableDesc) throws SQLException {
        Gen gen = new Gen();
        gen.setId(tableName);
        int i = genMysqlDao.deleteByTableName(tableName);
        List<GenTableColumn> tableOptions = tableConfig.getTableOptions();
        int index = 1;
        for (GenTableColumn tableOption : tableOptions) {
            GenTableColumn genTableColumn = new GenTableColumn();
            //genTableColumn.setId(getDBName().trim() + ":" + tableName.trim() + ":" + tableOption.getColumnName().trim());//id(数据库名称：表名：字段名);
            genTableColumn.setId(tableName.trim() + ":" + tableOption.getColumnName().trim());//id(表名：字段名);
            genTableColumn.setTableName(tableName.trim());
            genTableColumn.setTableDesc(tableDesc.trim());
            genTableColumn.setColumnName(tableOption.getColumnName().trim());
            genTableColumn.setColumnDesc(tableOption.getColumnDesc().trim());
            genTableColumn.setDictType(tableOption.getDictType().trim());
            String dictType = tableOption.getDictType();
            if(StringUtils.isNotEmpty(dictType)&&tableOption.getInputType().equals("associate")){
                String referTable = dictType.split(":")[0];
                String referFiled = dictType.split(":")[1];
                genTableColumn.setAssociateType((getInstanceName(referTable)+":"+getInstanceName(referFiled)).trim());
            }
            if(tableOption.getInputType().equals("office")){
                genTableColumn.setDictType("sys_office:name");
                genTableColumn.setAssociateType("sysOffice:name");
            }
            genTableColumn.setInputType(tableOption.getInputType().trim());
            genTableColumn.setQueryType(tableOption.getQueryType().trim());
            genTableColumn.setSort(index++);
            if (tableOption.getColumnName().equals("id")) {
                genTableColumn.setIsPk(1);
            }
            genTableColumn.setCreateDate(new Date());
            genTableColumn.setIsShow(tableOption.getIsShow());
            genTableColumn.setIsHidden(tableOption.getIsHidden());
            genTableColumn.setIsUnique(tableOption.getIsUnique());
            genMysqlDao.insert(genTableColumn);
            System.out.println("保存成功。");
        }

        //基础字段进行重新排序,构建表示基础字段排在后面
        List<GenTableColumn> allList = new ArrayList<>();
        List<GenTableColumn> baseList = new ArrayList<>();
        for (GenTableColumn tableOption : tableOptions) {
            if ("create_by".equals(tableOption.getColumnName()) || "create_date".equals(tableOption.getColumnName()) || "update_by".equals(tableOption.getColumnName()) || "update_date".equals(tableOption.getColumnName()) || "del_flag".equals(tableOption.getColumnName())) {
                baseList.add(tableOption);
            } else {
                allList.add(tableOption);
            }
        }
        allList.addAll(baseList);
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE `" + tableName + "`(");
        for (GenTableColumn tableOption : allList) {
            if (tableOption.getInputType().equals("date") || tableOption.getColumnName().equals("create_date") || tableOption.getColumnName().equals("update_date")) {
                sb.append("`" + tableOption.getColumnName() + "` datetime NULL DEFAULT NULL COMMENT '" + tableOption.getColumnDesc() + "',");
            } else if (tableOption.getColumnName().equals("id")) {
                sb.append("`" + tableOption.getColumnName() + "` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '" + tableOption.getColumnDesc() + "',");
            } else if (tableOption.getColumnName().equals("del_flag")) {
                sb.append("`" + tableOption.getColumnName() + "` tinyint(1) DEFAULT 0 COMMENT '" + tableOption.getColumnDesc() + "',");
            }else if(tableOption.getInputType().equals("select")){
                sb.append("`" + tableOption.getColumnName() + "` tinyint(2) NULL DEFAULT NULL COMMENT '" + tableOption.getColumnDesc() + "',");
            }else if(tableOption.getInputType().equals("picture")){
                sb.append("`" + tableOption.getColumnName() + "` varchar(200) NULL DEFAULT NULL COMMENT '" + tableOption.getColumnDesc() + "',");
            }else if(tableOption.getInputType().equals("textarea")){
                sb.append("`" + tableOption.getColumnName() + "` varchar(500) NULL DEFAULT NULL COMMENT '" + tableOption.getColumnDesc() + "',");
            }else if(tableOption.getInputType().equals("editor")){
                sb.append("`" + tableOption.getColumnName() + "` text NULL COMMENT '" + tableOption.getColumnDesc() + "',");
            }else {
                sb.append("`" + tableOption.getColumnName() + "` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '" + tableOption.getColumnDesc() + "',");
            }
        }
        sb.append("PRIMARY KEY (`id`) USING BTREE)");
        sb.append("ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '" + tableDesc + "' ROW_FORMAT = Compact;");
        String SQL = sb.toString();
        System.out.println("执行的语句：" + sb.toString());
        jdbcTemplate.execute("DROP TABLE IF EXISTS " + tableName + ";");
        jdbcTemplate.execute(SQL);
        return true;
    }

    public List<GenTableColumn> setGenColumnsSorts(Gen gen) {
        GenTableColumn genTableColumnF = new GenTableColumn();
        genTableColumnF.setTableName(gen.getId());
        List<GenTableColumn> columnList = genMysqlDao.findColumnList(genTableColumnF);
        for (GenTableColumn genTableColumn : columnList) {
            if (genTableColumn.getColumnDateType().equals("datetime") || genTableColumn.getColumnDateType().equals("date")) {
                genTableColumn.setInputType("date");
            }
        }
        //基础字段进行重新排序,构建表示基础字段排在后面
        List<GenTableColumn> otherList = new ArrayList<>();
        List<GenTableColumn> baseList = new ArrayList<>();
        GenTableColumn id = null;
        GenTableColumn create_by = null;
        GenTableColumn create_date = null;
        GenTableColumn update_date = null;
        GenTableColumn update_by = null;
        GenTableColumn del_flag = null;
        for (GenTableColumn tableOption : columnList) {
            if ("id".equals(tableOption.getColumnName()) || "create_by".equals(tableOption.getColumnName()) || "create_date".equals(tableOption.getColumnName()) || "update_by".equals(tableOption.getColumnName()) || "update_date".equals(tableOption.getColumnName()) || "del_flag".equals(tableOption.getColumnName())) {
                if ("id".equals(tableOption.getColumnName())) {
                    tableOption.setIsPk(1);
                    tableOption.setIsHidden(1);
                    tableOption.setQueryType("equal");
                    id = tableOption;
                }
                if ("create_by".equals(tableOption.getColumnName())) {
                    create_by = tableOption;
                    tableOption.setInputType("text");
                    tableOption.setQueryType("equal");
                }
                if ("create_date".equals(tableOption.getColumnName())) {
                    create_date = tableOption;
                    tableOption.setInputType("date");
                    tableOption.setQueryType("equal");
                }
                if ("update_by".equals(tableOption.getColumnName())) {
                    update_by = tableOption;
                    tableOption.setInputType("text");
                    tableOption.setQueryType("equal");
                }
                if ("update_date".equals(tableOption.getColumnName())) {
                    update_date = tableOption;
                    tableOption.setInputType("date");
                    tableOption.setQueryType("equal");
                }
                if ("del_flag".equals(tableOption.getColumnName())) {
                    del_flag = tableOption;
                    tableOption.setInputType("text");
                    tableOption.setQueryType("equal");
                }
            } else {
                if("date".equals(tableOption.getInputType())){
                    tableOption.setQueryType("between");
                }
                otherList.add(tableOption);
            }
        }
        baseList.add(id);
        baseList.add(create_by);
        baseList.add(create_date);
        baseList.add(update_by);
        baseList.add(update_date);
        baseList.add(del_flag);
        baseList.addAll(otherList);
        for (int i = 0; i < baseList.size(); i++) {
            if (baseList.get(i) != null) {
                baseList.get(i).setSort(i + 1);
            }
        }
        return baseList;
    }

}