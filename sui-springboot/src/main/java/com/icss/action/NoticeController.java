package com.icss.action;

import com.icss.entity.Notice;
import com.icss.service.NoticeService;
import com.icss.sys.base.module.extend.web.BaseController;
import com.icss.sys.base.entity.ResultInfo;
import com.icss.sys.utils.admin.StringUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
*【通知信息】页面接口
*/
@RestController
@RequestMapping("/admin/notice")
public class NoticeController extends BaseController {

    @Autowired
    private NoticeService noticeService;

    /**
    * 【通知信息】根据条件分页查询
    * @param page
    * @param notice
    * @return
    */
    @RequestMapping("/getPage")
    public ResultInfo getPage(Page<Notice> page, Notice notice, HttpServletRequest request) {
        IPage<Notice> iPage = noticeService.getPage(page, notice);
        return ResultInfo.ok("获取分页成功", iPage);
    }

    /**
    * 【通知信息】根据条件查询
    * @param notice
    * @return
    */
    @RequestMapping("/getList")
    public ResultInfo getList(Notice notice) {
        List<Notice> list = noticeService.getList(notice);
        return ResultInfo.ok("获取列表成功", list);
    }

    /**
    * 【通知信息】根据对象查询
    * @param notice
    * @return
    */
    @RequestMapping("/get")
    public ResultInfo get(Notice notice) {
        Notice entity = noticeService.get(notice);
        return ResultInfo.ok("获取对象成功", entity);
    }

    /**
    * 【通知信息】提交(新增或修改)
    * @param notice
    * @return
    */
    @RequestMapping("/sub")
    public ResultInfo insert(Notice notice) {
        if (StringUtils.isEmpty(notice.getId())) { //新增
            notice.setPublishTime(new Date());
            noticeService.insert(notice);
        } else {//修改
            noticeService.update(notice);
        }
        return ResultInfo.ok("提交成功!");
    }

    /**
    * 【通知信息】删除
    * @param id
    * @return
    */
    @RequestMapping("/delete")
    public ResultInfo delete(String id) {
        noticeService.delete(id);
        return ResultInfo.ok("删除成功!");
    }

    /**
    * 【通知信息】批量删除
    * @param ids
    * @return
    */
    @RequestMapping("/delAll")
    public ResultInfo delAll(String[] ids) {
        noticeService.delAll(ids);
        return ResultInfo.ok("批量删除成功！");
    }



}



