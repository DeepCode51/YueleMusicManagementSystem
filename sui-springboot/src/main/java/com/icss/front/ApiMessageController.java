package com.icss.front;

import com.icss.entity.SysUser;
import com.icss.service.SysUserService;
import com.icss.sys.base.entity.ResultInfo;
import com.icss.sys.base.module.extend.web.BaseController;
import com.icss.sys.base.module.message.entity.SysMessage;
import com.icss.sys.base.module.message.service.SysMessageService;
import com.icss.sys.utils.admin.ObjectUtils;
import com.icss.sys.utils.admin.StringUtils;
import com.icss.sys.utils.jwt.JwtUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 【消息信息】页面接口
 */
@RestController
@RequestMapping("/api/message")
public class ApiMessageController extends BaseController {

    @Autowired
    private SysMessageService sysMessageService;
    @Autowired
    private SysUserService sysUserService;

    //留言分页数据
    @RequestMapping("/getTreePage")
    public ResultInfo getPage(Page<SysMessage> page, SysMessage sysMessage) {
        IPage<SysMessage> iPage = sysMessageService.getTreePage(page, sysMessage);
        return ResultInfo.ok("获取分页成功",iPage);
    }

    @RequestMapping("/getList")
    public ResultInfo getList(SysMessage sysMessage) {
        sysMessage.setUserId(JwtUtils.getCurrentUserId());
        List<SysMessage> list = sysMessageService.getList(sysMessage);
        return ResultInfo.ok("获取列表成功", list);
    }

    //留言|讨论|评价
    @RequestMapping("/getTreeList")
    public ResultInfo getTreeList(SysMessage sysMessage) {
        List<SysMessage> list = sysMessageService.getTreeList(sysMessage);
        return ResultInfo.ok("获取列表成功", list);
    }

    //提交【留言|讨论】数据
    @RequestMapping("/sub")
    public ResultInfo subEvaluate(SysMessage sysMessage) {
        String currentUserId = JwtUtils.getCurrentUserId();
        if (StringUtils.isEmpty(currentUserId)) {
            return ResultInfo.error("后台检测到您还没有登录，请重新登录后操作。");
        }
        if (ObjectUtils.isNull(sysMessage.getType())) {
            return ResultInfo.error("消息类型不能为空！");
        }
        if(StringUtils.isEmpty(sysMessage.getPid())){
            sysMessage.setPid("0");
        }
        sysMessage.setUserId(currentUserId);
        sysMessageService.insert(sysMessage);
        return ResultInfo.ok("操作成功");
    }

    //提交【点赞|收藏】数据
    @RequestMapping("/subCollect")
    public ResultInfo subCollect(SysMessage sysMessage) {
        String currentUserId = JwtUtils.getCurrentUserId();
        if (StringUtils.isEmpty(currentUserId)) {
            return ResultInfo.error("后台检测到您还没有登录，请重新登录后操作。");
        }
        if (ObjectUtils.isNull(sysMessage.getType())) {
            return ResultInfo.error("消息类型不能为空！");
        }
        SysMessage params = new SysMessage();
        params.setUserId(currentUserId);
        params.setRefId(sysMessage.getRefId());
        params.setType(sysMessage.getType());
        SysMessage entity = sysMessageService.get(params);
        if (ObjectUtils.isNotNull(entity)) {
            sysMessageService.delete(entity.getId());
            return ResultInfo.ok(sysMessage.getType() + "已取消");
        } else {
            sysMessage.setUserId(currentUserId);
            sysMessage.setStars(1);
            sysMessageService.insert(sysMessage);
            return ResultInfo.ok(sysMessage.getType() + "成功");
        }
    }


    //提交【浏览】数据
    @RequestMapping("/subViewRecord")
    public ResultInfo subViewRecord(SysMessage sysMessage) {
        SysMessage params = new SysMessage();
        params.setType("浏览");
        params.setRefId(sysMessage.getRefId());
        SysMessage entity = sysMessageService.get(params);
        if (ObjectUtils.isNotNull(entity)) {
            entity.setStars(entity.getStars() + 1);
            sysMessageService.update(entity);
        } else {
            sysMessage.setType("浏览");
            sysMessage.setStars(1);
            sysMessageService.insert(sysMessage);
        }
        return ResultInfo.ok("记录成功");
    }

    /**
     * 【消息信息】根据id查询
     * @param sysMessage
     * @return
     */
    @RequestMapping("/get")
    public ResultInfo get(SysMessage sysMessage) {
        List<SysMessage> list = sysMessageService.getList(sysMessage);
        String currentUserId = JwtUtils.getCurrentUserId();
        boolean flag = false;
        if(StringUtils.isNotEmpty(currentUserId)){
            sysMessage.setUserId(currentUserId);
            SysMessage entity = sysMessageService.get(sysMessage);
            if (ObjectUtils.isNotNull(entity)) {
                flag = true;
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("flag", flag);
        map.put("total", list.size());
        return ResultInfo.ok("获取对象成功", map);
    }

    /**
     * 删除讨论|留言
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public ResultInfo delete(String id) {
        sysMessageService.delete(id);
        SysMessage params = new SysMessage();
        params.setPid(id);
        List<SysMessage> list = sysMessageService.getList(params);
        for (SysMessage sysMessage : list) {
            sysMessageService.delete(sysMessage.getId());
        }
        return ResultInfo.ok("删除成功!");
    }

    /**
     * 取消收藏|点赞
     *
     * @param id
     * @return
     */
    @RequestMapping("/cancel")
    public ResultInfo cancel(String id) {
        sysMessageService.delete(id);
        return ResultInfo.ok("取消成功!");
    }


    //获取服务端聊天用户
    @RequestMapping("/getServiceUserList")
    public ResultInfo getServiceUserList() {
        SysUser sysUser = new SysUser();
        sysUser.setRoleCode("adminRole");
        List<SysUser> list = sysUserService.getUserList(sysUser);
        List<SysUser> serviceList= list.stream().filter(item -> !"dev".equals(item.getLoginName())).collect(Collectors.toList());
        return ResultInfo.ok("获取列表成功", serviceList);
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



