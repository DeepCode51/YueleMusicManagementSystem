package com.icss.front;

import com.icss.service.NoticeService;
import com.icss.entity.Notice;
import com.icss.sys.utils.admin.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import com.icss.sys.base.module.extend.web.BaseController;
import com.icss.sys.base.entity.ResultInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
*【通知信息】管理
*/
@RestController
@RequestMapping("/api/notice")
public class ApiNoticeController extends BaseController {

    @Autowired
    private NoticeService noticeService;

    /**
     *【通知信息】获取分页数据
     */
    @RequestMapping("/getPage")
    public ResultInfo getPage(Page<Notice> page, Notice notice) {
        IPage<Notice> iPage = noticeService.getPage(page, notice);
        return ResultInfo.ok("获取分页成功",iPage);
    }

    /**
    * 【通知信息】获取列表数据
    */
    @RequestMapping("/getList")
    public ResultInfo getList(Notice notice) {
        List<Notice> list = noticeService.getList(notice);
        return ResultInfo.ok("获取列表成功", list);
    }

    /**
    *【通知信息】根据对象数据
    */
    @RequestMapping("/get")
    public ResultInfo get(Notice notice) {
        Notice entity = noticeService.get(notice);
        return ResultInfo.ok("获取成功",entity);
    }

    /**
     *【通知信息】保存或修改
     */
    @RequestMapping("/sub")
    public ResultInfo saveOrUpdate(HttpServletRequest request,Notice notice){
        try {
            if(StringUtils.isEmpty(notice.getId())){
                noticeService.insert(notice);//新增
                return ResultInfo.ok("保存成功！");
            }else{
                noticeService.update(notice);//修改
                return ResultInfo.ok("修改成功！");
            }
        } catch (Exception e) {
            return ResultInfo.error("保存失败！失败信息:"+e.getMessage());
        }
    }

    /**
     *【通知信息】根据id删除
     */
    @RequestMapping("/delete")
    public ResultInfo delete(HttpServletRequest request, Notice notice) {
        noticeService.delete(notice.getId());
        return ResultInfo.ok("删除成功！");
    }

    /**
    * 【通知信息】批量删除
    */
    @RequestMapping("/delAll")
    public ResultInfo delAll(String[] ids) {
        noticeService.delAll(ids);
        return ResultInfo.ok("批量删除成功！");
    }

}



