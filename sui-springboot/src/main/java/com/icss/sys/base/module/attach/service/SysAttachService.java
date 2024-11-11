package com.icss.sys.base.module.attach.service;

import com.icss.sys.base.module.attach.dao.SysAttachDao;
import com.icss.sys.base.module.attach.entity.SysAttach;
import com.icss.sys.base.module.extend.service.BaseService;
import com.icss.sys.base.properteis.FileUploadProperteis;
import com.icss.sys.utils.admin.FileHelper;
import com.icss.sys.utils.admin.ObjectUtils;
import com.icss.sys.utils.admin.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

//附件信息接口类
@Service
public class SysAttachService extends BaseService {

    @Autowired
    private SysAttachDao sysAttachDao;
    @Autowired
    private FileUploadProperteis fileUploadProperteis;

    //【附件信息】设置查询条件
    private LambdaQueryWrapper<SysAttach> getMenuQueryCondition(SysAttach sysAttach) {
        LambdaQueryWrapper<SysAttach> lambdaQuery = this.getBaseQueryCondition(sysAttach);
        //根据id排序
        lambdaQuery.orderByAsc(SysAttach::getId);
        if (ObjectUtils.isNotNull(sysAttach)) {
            //【文件名称】模糊查询
            lambdaQuery.like(StringUtils.isNotEmpty(sysAttach.getFileName()), SysAttach::getFileName, sysAttach.getFileName());
            //【文件大小】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(sysAttach.getFileSize()), SysAttach::getFileSize, sysAttach.getFileSize());
            //【关联id】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(sysAttach.getRefId()), SysAttach::getRefId, sysAttach.getRefId());
            //【文件后缀】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(sysAttach.getSuffix()), SysAttach::getSuffix, sysAttach.getSuffix());
            //【文件模块】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(sysAttach.getFileModule()), SysAttach::getFileModule, sysAttach.getFileModule());
            //【文件类型】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(sysAttach.getFileType()), SysAttach::getFileType, sysAttach.getFileType());
            //【保存路径】精确查询
            lambdaQuery.eq(StringUtils.isNotEmpty(sysAttach.getSavePath()), SysAttach::getSavePath, sysAttach.getSavePath());
            //【备注】模糊查询
            lambdaQuery.like(StringUtils.isNotEmpty(sysAttach.getRemarks()), SysAttach::getRemarks, sysAttach.getRemarks());
        }
        return lambdaQuery;
    }


    //【附件信息】分页查询
    public IPage<SysAttach> getPage(Page<SysAttach> page, SysAttach sysAttach) {
        LambdaQueryWrapper<SysAttach> lambdaQuery = getMenuQueryCondition(sysAttach);
        return sysAttachDao.selectPage(page, lambdaQuery);
    }

    //【附件信息】查询列表
    public List<SysAttach> getList(SysAttach sysAttach) {
        LambdaQueryWrapper<SysAttach> lambdaQuery = getMenuQueryCondition(sysAttach);
        return sysAttachDao.selectList(lambdaQuery);
    }

    //【附件信息】根据id查询
    public SysAttach get(String id) {
        return sysAttachDao.selectById(id);
    }

    //【附件信息】根据对象查询
    public SysAttach get(SysAttach sysAttach) {
        LambdaQueryWrapper<SysAttach> lambdaQuery = getMenuQueryCondition(sysAttach);
        return sysAttachDao.selectOne(lambdaQuery);
    }

    //【附件信息】根据refId查询文件路径
    public List<String> getByRefId(String refId) {
        SysAttach params = new SysAttach();
        params.setRefId(refId);
        List<SysAttach> list = this.getList(params);
        List<String> fileList = list.stream().map(SysAttach::getSavePath).collect(Collectors.toList());
        return fileList;
    }

    //【附件信息】新增
    public int insert(SysAttach sysAttach) {
        sysAttach.preInsert();
        return sysAttachDao.insert(sysAttach);
    }

    //【附件信息】修改
    public int update(SysAttach sysAttach) {
        sysAttach.preUpdate();
        return sysAttachDao.updateById(sysAttach);
    }

    //【附件信息】删除
    public int delete(String id) {
        return sysAttachDao.deleteById(id);
    }

    //【附件信息】批量删除
    public int delAll(String[] ids) {
        return sysAttachDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 保存附件
     *
     * @param attachType
     * @param file
     * @return
     */
    public String saveFile(String attachType, String uuid, MultipartFile file) throws IOException, InterruptedException {
            Calendar cad = Calendar.getInstance();
            String month = String.valueOf(cad.get(Calendar.MONTH) + 1);
            if (month.length() < 2) {
                month = 0 + month;
            }
            String day = String.valueOf(cad.get(Calendar.DATE));
            if (day.length() < 2) {
                day = 0 + day;
            }
            String datePath = cad.get(Calendar.YEAR) + "/" + month + "/" + day;
            String fileName = uuid+ FileHelper.getFileSuffix(file.getOriginalFilename());
            String savePath = attachType + "/" + datePath;
            Thread.sleep(100);//防止时间重复
            FileHelper.saveMultiPartFile(file, fileUploadProperteis.getDiskPath() + "/" + fileUploadProperteis.getFileBasePath() + "/" + savePath, fileName);
            return savePath + "/" + fileName;
    }

}