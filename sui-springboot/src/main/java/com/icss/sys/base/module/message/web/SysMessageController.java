package com.icss.sys.base.module.message.web;

import com.icss.entity.Register;
import com.icss.service.RegisterService;
import com.icss.service.SysUserService;
import com.icss.sys.base.module.extend.web.BaseController;
import com.icss.sys.base.entity.ResultInfo;
import com.icss.sys.base.module.message.entity.SysMessage;
import com.icss.sys.base.module.message.service.SysMessageService;
import com.icss.sys.utils.admin.ShiroUtils;
import com.icss.sys.utils.admin.StringUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
*【消息信息】页面接口
*/
@RestController
@RequestMapping("/admin/sysMessage")
public class SysMessageController extends BaseController {

    @Autowired
    private SysMessageService sysMessageService;
    @Autowired
    private RegisterService registerService;
    @Autowired
    private SysUserService sysUserService;

    /**
    * 【消息信息】根据条件分页查询
    * @param page
    * @param sysMessage
    * @return
    */
    @RequestMapping("/getPage")
    @RequiresPermissions("sysMessage:getPage")
    public ResultInfo getPage(Page<SysMessage> page, SysMessage sysMessage) {
        sysMessage.setPid("0");
        List<SysMessage> treeList = sysMessageService.getTreeList(sysMessage);
        IPage<SysMessage> newPage = new Page<>();
        newPage.setRecords(treeList);
        newPage.setTotal(treeList.size());
        return ResultInfo.ok("获取分页成功", newPage);
    }

    /**
    * 【消息信息】根据条件查询
    * @param sysMessage
    * @return
    */
    @RequestMapping("/getList")
    public ResultInfo getList(SysMessage sysMessage) {
        List<SysMessage> list = sysMessageService.getList(sysMessage);
        return ResultInfo.ok("获取列表成功", list);
    }

    /**
    * 【消息信息】根据id查询
    * @param id
    * @return
    */
    @RequestMapping("/get")
    @RequiresPermissions(value = {"sysMessage:edit","sysMessage:view"},logical = Logical.OR)
    public ResultInfo get(String id) {
        SysMessage sysMessage = sysMessageService.get(id);
        return ResultInfo.ok("获取对象成功", sysMessage);
    }

    /**
    * 【消息信息】提交(新增或修改)
    * @param sysMessage
    * @return
    */
    @RequestMapping("/sub")
    @RequiresPermissions("sysMessage:save")
    public ResultInfo insert(SysMessage sysMessage) {
        if (StringUtils.isEmpty(sysMessage.getId())) { //新增
            sysMessage.setUserId(ShiroUtils.getCurrentUserId());
            sysMessageService.insert(sysMessage);
        } else {//修改
            sysMessageService.update(sysMessage);
        }
        return ResultInfo.ok("提交成功!");
    }

    /**
    * 【消息信息】删除
    * @param id
    * @return
    */
    @RequestMapping("/delete")
    @RequiresPermissions("sysMessage:delete")
    public ResultInfo delete(String id) {
        sysMessageService.delete(id);
        SysMessage params =new SysMessage();
        params.setPid(id);//删除子级信息
        List<SysMessage> list = sysMessageService.getList(params);
        for (SysMessage sysMessage : list) {
            sysMessageService.delete(sysMessage.getId());
        }
        return ResultInfo.ok("删除成功!");
    }

    /**
     * 【消息信息】批量删除
     * @param ids
     * @return
     */
    @RequestMapping("/delAll")
    public ResultInfo delAll(String[] ids) {
        sysMessageService.delAll(ids);
        for (String id : ids) {
            SysMessage params =new SysMessage();
            params.setPid(id);
            List<SysMessage> list = sysMessageService.getList(params);
            for (SysMessage sysMessage : list) {
                sysMessageService.delete(sysMessage.getId());
            }
        }
        return ResultInfo.ok("批量删除成功！");
    }

    //获取客户端聊天用户
    @RequestMapping("/getClientUserList")
    public ResultInfo getClientUserList() {
        Register register = new Register();
        List<Register> list = registerService.getList(register);
        return ResultInfo.ok("获取列表成功", list);
    }

    //获取后台用户
    @RequestMapping("/subChat")
    public ResultInfo subChat(SysMessage sysMessage){
        sysMessage.setType("聊天");
        sysMessageService.insert(sysMessage);
        return ResultInfo.ok("获取列表成功");
    }

    //获取后台用户
    @RequestMapping("/getChatList")
    public ResultInfo getChatList(SysMessage sysMessage){
        sysMessage.setType("聊天");
        List<SysMessage> list = sysMessageService.getList(sysMessage);
        return ResultInfo.ok("获取列表成功",list);
    }
}



