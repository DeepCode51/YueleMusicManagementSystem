package com.icss.sys.base.module.gen.web;

import com.github.pagehelper.PageHelper;
import com.icss.sys.base.entity.ResultInfo;
import com.icss.sys.base.page.Page;
import com.icss.sys.base.properteis.GenProperteis;
import com.icss.sys.base.properteis.JdbcProperteis;
import com.icss.sys.base.module.config.entity.SysConfig;
import com.icss.sys.base.module.config.service.SysConfigService;
import com.icss.sys.base.module.gen.dao.GenMysqlDao;
import com.icss.sys.base.module.gen.dao.GenOracleDao;
import com.icss.sys.base.module.gen.entity.Gen;
import com.icss.sys.base.module.gen.entity.GenDomain;
import com.icss.sys.base.module.gen.entity.GenTableColumn;
import com.icss.sys.base.module.gen.entity.GenTableConfig;
import com.icss.sys.base.module.gen.enums.GenCodeType;
import com.icss.sys.base.module.gen.service.GenCreator;
import com.icss.sys.base.module.gen.service.GenService;
import com.icss.sys.base.module.gen.service.GenPathService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import static com.icss.sys.base.module.gen.service.GenCreator.getInstanceName;


/**
 * Created by Administrator on 2018/7/18.
 */
@Controller
@RequestMapping("/admin/sys/gen")

public class GenController {
    public String getDBName() {
        String dbUrl = jdbcProperteis.getUrl();
        String str = dbUrl.substring(dbUrl.indexOf("/") + 1, dbUrl.lastIndexOf("?"));
        String db_name = str.split("/")[2];
        return db_name;
    }

    @Autowired
    private GenProperteis genProperteis;
    @Autowired
    private JdbcProperteis jdbcProperteis;
    @Autowired
    private JdbcTemplate jdbcTemplate;//注入的方式
    @Autowired
    private SysConfigService sysConfigService;//注入的方式
    @Autowired
    private GenOracleDao genOracleDao;
    @Autowired
    private GenService genService;
    @Autowired
    private GenPathService genPathService;
    @Autowired
    private GenMysqlDao genMysqlDao;

    @RequestMapping("/manage")
    public String getClassByTable(ModelAndView modelAndView, HttpServletRequest request) {
        return "module/gen/genManage";
    }

    @RequestMapping("/getList")
    @ResponseBody
    public Page<Gen> getList(Page<Gen> page, Gen gen) {
        List<Gen> list = new ArrayList<>();
        PageHelper.startPage(page.getPage(), page.getLimit());
        try {
            if (jdbcProperteis.getUrl().split(":")[1].equals("oracle")) {
                list = genOracleDao.findList(gen);
            } else {
                String dbName = getDBName();
                gen.setDbName(dbName);
                System.out.println("数据库名称: " + dbName);
                list = genMysqlDao.findList(gen);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Gen sys_user = new Gen();
        sys_user.setId("sys_user");
        sys_user.setTableName("sys_user");
        sys_user.setComments("用户表");
        list.add(sys_user);
        Gen sys_office = new Gen();
        sys_office.setId("sys_office");
        sys_office.setTableName("sys_office");
        sys_office.setComments("机构表");
        list.add(sys_office);
        Gen sys_role = new Gen();
        sys_role.setId("sys_role");
        sys_role.setTableName("sys_role");
        sys_role.setComments("角色表");
        list.add(sys_role);
        page.getPageList(list, page);
        return page;
    }

    @RequestMapping("/edit")
    public ModelAndView edit(ModelAndView modelAndView, Gen gen) {
        List<GenTableColumn> baseList = genService.setGenColumnsSorts(gen);
        SysConfig sysConfig = new SysConfig();
        sysConfig.setCode("genTableConfigs");
        SysConfig setting = sysConfigService.get(sysConfig);
        if(setting!=null&& StringUtils.isNotEmpty(setting.getVal())){
            JSONArray configsArray = JSONArray.fromObject(setting.getVal());
            for (Object o : configsArray) {
                JSONObject table = (JSONObject) o;
                JSONObject config = (JSONObject) table.get(gen.getId());
                if (config != null) {
                    boolean isPort = (boolean) config.get("isPort");
                    boolean isLock = (boolean) config.get("isLock");
                    boolean isFileList = (boolean) config.get("isFileList");
                    modelAndView.addObject("isPort", isPort);
                    modelAndView.addObject("isLock", isLock);
                    modelAndView.addObject("isFileList", isFileList);
                    break;
                }
            }
        }
        modelAndView.addObject("columnList", baseList);
        modelAndView.setViewName("module/gen/genEdit");
        return modelAndView;
    }

    @RequestMapping("/view")
    public ModelAndView view(ModelAndView modelAndView, Gen gen) {
        modelAndView.setViewName("module/gen/genView");
        return modelAndView;
    }

    //创建新表对象
    @RequestMapping("/delete")
    @ResponseBody
    public ResultInfo delete(ModelAndView modelAndView, Gen gen) throws Exception {

        if(!StringUtils.isEmpty(gen.getId())){
            jdbcTemplate.execute("delete from SYS_COLUMN where table_name = '" + gen.getId() + "'");
            jdbcTemplate.execute("DROP TABLE IF EXISTS " + gen.getId() + ";");
        }
        SysConfig sysConfig = new SysConfig();
        sysConfig.setCode("genTableConfigs");
        SysConfig setting = sysConfigService.get(sysConfig);
        if(setting!=null){
            JSONArray configsArray = JSONArray.fromObject(setting.getVal());
            for (Object o : configsArray) {
                JSONObject table = (JSONObject) o;
                Object obj = table.get(gen.getId());
                if(obj!=null){
                    table.remove(gen.getId());
                    break;
                }
            }
            setting.setVal(configsArray.toString());
        }
        sysConfigService.update(setting);
        return ResultInfo.ok("删除表成功！");
    }

    @RequestMapping("/getCloums")
    @ResponseBody
    public Page<Gen> getCloums(Page<Gen> page, Gen gen) {
        page.setLimit(100000);
        List<Gen> list = null;
        if (jdbcProperteis.getUrl().split(":")[1].equals("oracle")) {
            list = genOracleDao.getCloums(gen);
        } else {
            list = genMysqlDao.getCloums(gen);
        }
        page.setData(list);
        Page<Gen> pageDate = page.getPageList(list, page);
        return pageDate;
    }

    @RequestMapping("/config")
    public ModelAndView config(ModelAndView modelAndView, Gen entity, Boolean flag) {
        if (flag != null && flag == true) {
            SysConfig sysConfig = new SysConfig();
            sysConfig.setCode("genTableConfigs");
            SysConfig setting = sysConfigService.get(sysConfig);
            boolean isFileList = false;
            boolean isPort = false;
            if(setting!=null){
                JSONArray configsArray = JSONArray.fromObject(setting.getVal());
                for (Object o : configsArray) {
                    JSONObject table = (JSONObject) o;
                    JSONObject obj = (JSONObject) table.get(entity.getId());
                    if(obj!=null&&obj.getBoolean("isFileList")){
                        isFileList=true;
                    }
                    if(obj!=null&&obj.getBoolean("isPort")){
                        isPort=true;
                    }
                }
            }
            modelAndView.addObject("isPort", isPort);
            modelAndView.addObject("isFileList", isFileList);
            modelAndView.addObject("id", entity.getId());
            modelAndView.addObject("path", genPathService.getBasePathDir());
            modelAndView.addObject("tableName", entity.getId());
            modelAndView.addObject("comments", entity.getComments());
            modelAndView.addObject("prefixPath", "com.portal.module");
            modelAndView.addObject("modelName", getInstanceName(entity.getId()));
        }
        modelAndView.setViewName("module/gen/genConfig");
        return modelAndView;
    }

    //创建新表对象
    @RequestMapping("/createTable")
    public ModelAndView createTable(ModelAndView modelAndView, Gen gen) {
        modelAndView.addObject("tableName", gen.getId());
        modelAndView.addObject("tableComments", gen.getComments());
        modelAndView.setViewName("module/gen/genAdd");
        return modelAndView;
    }

    @RequestMapping("/addSub")
    @ResponseBody
    public ResultInfo saveOrUpdate(HttpServletRequest request, GenTableConfig tableConfig) throws Exception {
        String tableName = request.getParameter("tableName");
        String tableDesc = request.getParameter("tableDesc");
        Boolean isOk = genService.saveGenColumData(tableConfig, tableName, tableDesc);
        if (isOk) {
            return ResultInfo.ok("表对象创建成功！");
        } else {
            return ResultInfo.error("表对象创建失败！");
        }
    }

    /**
     * 锁定数据
     * @param request
     * @param tableConfig
     * @return
     * @throws Exception
     */
    @RequestMapping("/setConfig")
    @ResponseBody
    public ResultInfo addLock(HttpServletRequest request, GenTableConfig tableConfig) throws Exception {
        String tableName = request.getParameter("tableName");
        String tableDesc = request.getParameter("tableDesc");
        if(StringUtils.isEmpty(tableName)||StringUtils.isEmpty(tableDesc)){
            return ResultInfo.error("表名或表描述不能为空！");
        }
        boolean isPort = Boolean.parseBoolean(request.getParameter("isPort"));
        tableConfig.setPort(isPort);
        boolean isLock = Boolean.parseBoolean(request.getParameter("isLock"));
        tableConfig.setLock(isLock);
        boolean isFileList = Boolean.parseBoolean(request.getParameter("isFileList"));
        tableConfig.setFileList(isFileList);
        return genService.genConfigTable(tableName, tableDesc,isPort, isLock, isFileList);
    }

    //生成前台代码
    @RequestMapping("/genCodeWeb")
    @ResponseBody
    public ResultInfo genCodeWeb(GenDomain genDomain) {
        try {
            boolean bool = GenCreator.gen(GenCodeType.WEB, genDomain,genProperteis);
            if (bool) {
                return ResultInfo.ok("恭喜你!,Jsp代码生成成功! 代码存放目录:"+genPathService.getBasePathDir());
            } else {
                return ResultInfo.ok("对不起!,Jsp代码生成失败!");
            }
        } catch (Exception e) {
            return ResultInfo.error("对不起!,Jsp代码生成异常!"+e.getMessage());
        }
    }
    //生成api代码
    @RequestMapping("/genCodeApi")
    @ResponseBody
    public ResultInfo genCodeApi(GenDomain genDomain) {
        try {
            boolean bool = GenCreator.gen(GenCodeType.API, genDomain,genProperteis);
            if (bool) {
                return ResultInfo.ok("恭喜你!,API代码生成成功! 代码存放目录:"+genPathService.getBasePathDir());
            } else {
                return ResultInfo.ok("对不起!,API代码生成失败!");
            }
        } catch (Exception e) {
            return ResultInfo.error("对不起!,API代码生成异常!"+e.getMessage());
        }
    }

    //生成后台代码
    @RequestMapping("/genCodeJava")
    @ResponseBody
    public ResultInfo genCodeJava(GenDomain genDomain, HttpServletRequest request) {
        try {
            SysConfig sysConfig = new SysConfig();
            sysConfig.setCode("genTableConfigs");
            SysConfig setting = sysConfigService.get(sysConfig);
            if(setting!=null){
                JSONArray configsArray = JSONArray.fromObject(setting.getVal());
                for (Object o : configsArray) {
                    JSONObject table = (JSONObject) o;
                    JSONObject obj = (JSONObject) table.get(genDomain.getTableName());
                    if(obj!=null&&obj.getBoolean("isLock")){
                        return ResultInfo.error("该表已经被锁定,无法生成代码！");
                    }
                }
            }
            boolean bool = GenCreator.gen(GenCodeType.JAVA, genDomain,genProperteis);
            if (bool) {
                return ResultInfo.ok("恭喜你!,java代码生成成功! 代码存放目录:"+genPathService.getBasePathDir());
            } else {
                return ResultInfo.ok("对不起!,java代码生成失败!");
            }
        } catch (Exception e) {
            return ResultInfo.error("对不起!,java代码生成异常!"+e.getMessage());
        }
    }

    //生成前台代码
    @RequestMapping("/genCodeWX")
    @ResponseBody
    public ResultInfo genCodeWX(GenDomain genDomain) {
        try {
            boolean bool = GenCreator.gen(GenCodeType.WX, genDomain,genProperteis);
            if (bool) {
                return ResultInfo.ok("恭喜你!,vue代码生成成功! 代码存放目录:"+genPathService.getBasePathDir());
            } else {
                return ResultInfo.ok("对不起!,vue代码生成失败!");
            }
        } catch (Exception e) {
            return ResultInfo.error("对不起!,vue代码生成异常!"+e.getMessage());
        }
    }

    //生成前台和后台代码
    @RequestMapping("/genCodeWebAndJava")
    @ResponseBody
    public ResultInfo genCodeWebAndJava(GenDomain genDomain) {
        try {
            SysConfig sysConfig = new SysConfig();
            sysConfig.setCode("genTableConfigs");
            SysConfig setting = sysConfigService.get(sysConfig);
            if(setting!=null){
                JSONArray configsArray = JSONArray.fromObject(setting.getVal());
                for (Object o : configsArray) {
                    JSONObject table = (JSONObject) o;
                    JSONObject obj = (JSONObject) table.get(genDomain.getTableName());
                    if(obj!=null&&obj.getBoolean("isLock")){
                        return ResultInfo.error("该表已经被锁定,无法生成代码！");
                    }
                }
            }
            boolean bool = GenCreator.gen(GenCodeType.ALL, genDomain,genProperteis);
            if (bool) {
                return ResultInfo.ok("恭喜你!,前后台代码生成成功! 代码存放目录:"+genPathService.getBasePathDir());
            } else {
                return ResultInfo.ok("对不起!,前后台代码生成失败!");
            }
        } catch (Exception e) {
            return ResultInfo.error("对不起!,前后台代码生成异常!"+e.getMessage());
        }
    }

    //生成菜单数据
    @RequestMapping("/genMenu")
    @ResponseBody
    public ResultInfo genMenu(Gen gen){
        SysConfig sysConfig = new SysConfig();
        sysConfig.setCode("genTableConfigs");
        SysConfig setting = sysConfigService.get(sysConfig);
        boolean isPort = false;
        if(setting!=null){
            JSONArray configsArray = JSONArray.fromObject(setting.getVal());
            for (Object o : configsArray) {
                JSONObject table = (JSONObject) o;
                JSONObject obj = (JSONObject) table.get(gen.getId());
                if(obj!=null&&obj.getBoolean("isLock")){
                    return ResultInfo.error("该表已经被锁定,无法生成菜单！");
                }
                if(obj!=null&&obj.getBoolean("isPort")){
                    isPort = (boolean) obj.get("isPort");
                }
            }
        }
        return genService.genMenuList(isPort,gen);
    }

}
