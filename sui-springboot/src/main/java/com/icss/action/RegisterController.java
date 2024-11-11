package com.icss.action;

import com.icss.entity.Register;
import com.icss.service.RegisterService;
import com.icss.sys.base.module.extend.web.BaseController;
import com.icss.sys.base.entity.DateUtils;
import com.icss.sys.base.entity.ResultInfo;
import com.icss.sys.utils.admin.StringUtils;
import com.icss.sys.utils.excel.ExportExcel;
import com.icss.sys.utils.excel.ImportExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
*【前台用户】页面接口
*/
@RestController
@RequestMapping("/admin/register")
public class RegisterController extends BaseController {

    @Autowired
    private RegisterService registerService;

    /**
     * 【前台用户】根据条件分页查询
     * @param page
     * @param register
     * @return
     */
    @RequestMapping("/getPage")
    @RequiresPermissions("register:getPage")
    public ResultInfo getPage(Page<Register> page, Register register) {
        IPage<Register> iPage = registerService.getPage(page, register);
        return ResultInfo.ok("获取分页成功", iPage);
    }

    /**
     * 【前台用户】根据条件查询
     * @param register
     * @return
     */
    @RequestMapping("/getList")
    public ResultInfo getList(Register register) {
        List<Register> list = registerService.getList(register);
        return ResultInfo.ok("获取列表成功", list);
    }

    /**
     * 【前台用户】根据id查询
     * @param id
     * @return
     */
    @RequestMapping("/get")
    @RequiresPermissions(value = {"register:edit","register:view"},logical = Logical.OR)
    public ResultInfo get(String id) {
        Register register = registerService.get(id);
        return ResultInfo.ok("获取对象成功", register);
    }

    /**
     * 【前台用户】提交(新增或修改)
     * @param register
     * @return
     */
    @RequestMapping("/sub")
    @RequiresPermissions("register:save")
    public ResultInfo insert(Register register) {
        if (StringUtils.isEmpty(register.getId())) { //新增
            registerService.insert(register);
        } else {//修改
            registerService.update(register);
        }
        return ResultInfo.ok("提交成功!");
    }

    /**
     * 【前台用户】删除
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions("register:delete")
    public ResultInfo delete(String id) {
        registerService.delete(id);
        return ResultInfo.ok("删除成功!");
    }

    /**
     * 【前台用户】批量删除
     * @param ids
     * @return
     */
    @RequestMapping("/delAll")
    public ResultInfo delAll(String[] ids) {
        registerService.delAll(ids);
        return ResultInfo.ok("批量删除成功！");
    }


    /**
     *【前台用户】导出
     */
    @RequestMapping(value = "/export")
    @RequiresPermissions("register:export")
    public void exportFile(Register register, HttpServletResponse response) {
        try {
            String fileName = "注册信息" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            List<Register> list = registerService.getList(register);
            new ExportExcel("注册信息", Register.class).setDataList(list).write(response, fileName).dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *【前台用户】导入
     */
    @RequestMapping(value = "/import")
    @RequiresPermissions("register:import")
    public ResultInfo importFile(MultipartFile file) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<Register> list = ei.getDataList(Register.class);
            for (Register register : list) {
                try {
                    registerService.insert(register);
                    successNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条注册信息记录。");
            }
            return ResultInfo.ok("已成功导入 " + successNum + " 条注册信息记录" + failureMsg);
        } catch (Exception e) {
            return ResultInfo.error("导入注册信息失败！失败信息：" + e.getMessage());
        }
    }

    /**
     *【前台用户】模板下载
     */
    @RequestMapping(value = "/import/template")
    @RequiresPermissions("register:import")
    public ResultInfo importFileTemplate(HttpServletResponse response) {
        try {
            String fileName = "注册信息数据导入模板.xlsx";
            List<Register> list = new ArrayList<>();
            new ExportExcel("注册信息数据", Register.class, 1).setDataList(list).write(response, fileName).dispose();
            return ResultInfo.ok("下载模板成功！");
        } catch (Exception e) {
            return ResultInfo.error("导入模板下载失败！失败信息：" + e.getMessage());
        }
    }

}






