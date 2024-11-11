package com.icss.service;

import com.icss.dao.NoticeDao;
import com.icss.entity.Notice;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.icss.sys.utils.admin.StringUtils;
import com.icss.sys.utils.admin.ObjectUtils;
import com.icss.sys.base.module.extend.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

//通知信息接口类
@Service
public class NoticeService extends BaseService {

    @Autowired
    private NoticeDao noticeDao;

    //【通知信息】设置查询条件
    private LambdaQueryWrapper<Notice> getNoticeQueryCondition(Notice notice) {
        LambdaQueryWrapper<Notice> lambdaQuery = this.getBaseQueryCondition(notice);
        //根据创建时间排序
        lambdaQuery.orderByDesc(Notice::getCreateDate);
        if (ObjectUtils.isNotNull(notice)) {
            //【主键id】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(notice.getId()), Notice::getId, notice.getId());
            //【消息标题】模糊查询
            lambdaQuery.like(StringUtils.isNotEmpty(notice.getTitle()), Notice::getTitle, notice.getTitle());
            //【通知类型】范围查询
            lambdaQuery.in(StringUtils.isNotNull(notice.getNoticeTypeRange()), Notice::getNoticeType, notice.getNoticeTypeRange());
            //【发布时间】范围查询
            if (ObjectUtils.isNotNull(notice.getPublishTimeRange()) && notice.getPublishTimeRange().size() > 0) {
                lambdaQuery.between(Notice::getPublishTime, notice.getPublishTimeRange().get(0), notice.getPublishTimeRange().get(1));
            }
        }
        return lambdaQuery;
    }

    //【通知信息】分页查询
    public IPage<Notice> getPage(Page<Notice> page, Notice notice) {
        LambdaQueryWrapper<Notice> lambdaQuery = getNoticeQueryCondition(notice);
        return noticeDao.selectPage(page, lambdaQuery);
    }
    
    //【通知信息】查询列表
    public List<Notice> getList(Notice notice) {
        LambdaQueryWrapper<Notice> lambdaQuery = getNoticeQueryCondition(notice);
        return noticeDao.selectList(lambdaQuery);
    }
    
    //【通知信息】根据id查询
    public Notice get(String id) {
        return noticeDao.selectById(id);
    }

    //【通知信息】根据对象查询
    public Notice get(Notice notice) {
    LambdaQueryWrapper<Notice> lambdaQuery = getNoticeQueryCondition(notice);
        return noticeDao.selectOne(lambdaQuery);
    }
    
    //【通知信息】新增
    public int insert(Notice notice) {
        this.preInsert(notice);
        return noticeDao.insert(notice);
    }
    
    //【通知信息】修改
    public int update(Notice notice) {
        this.preUpdate(notice);
        return noticeDao.updateById(notice);
    }
    
    //【通知信息】删除
    public int delete(String id) {
        return noticeDao.deleteById(id);
    }

    //【通知信息】批量删除
    public int delAll(String[] ids) {
        return noticeDao.deleteBatchIds(Arrays.asList(ids));
    }

}