package com.icss.sys.base.module.attach.web;

import com.icss.sys.base.module.extend.web.BaseController;
import com.icss.sys.base.entity.DateUtils;
import com.icss.sys.base.entity.ResultInfo;
import com.icss.sys.base.module.attach.entity.SysAttach;
import com.icss.sys.base.module.attach.service.SysAttachService;
import com.icss.sys.base.properteis.FileUploadProperteis;
import com.icss.sys.utils.admin.IdGen;
import com.icss.sys.utils.admin.StringUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.icss.sys.utils.excel.ExportExcel;
import com.icss.sys.utils.excel.ImportExcel;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
*【附件信息】页面接口
*/
@RestController
@RequestMapping("/admin/sysAttach")
public class SysAttachController extends BaseController {

    @Autowired
    private SysAttachService sysAttachService;
    @Autowired
    private FileUploadProperteis fileUploadProperteis;

    /**
    * 【附件信息】根据条件分页查询
    * @param page
    * @param sysAttach
    * @return
    */
    @RequestMapping("/getPage")
    @RequiresPermissions("sysAttach:getPage")
    public ResultInfo getPage(Page<SysAttach> page, SysAttach sysAttach) {
        IPage<SysAttach> iPage = sysAttachService.getPage(page, sysAttach);
        return ResultInfo.ok("获取分页成功", iPage);
    }


    /**
    * 【附件信息】根据条件查询
    * @param sysAttach
    * @return
    */
    @RequestMapping("/getList")
    public ResultInfo getList(SysAttach sysAttach) {
        List<SysAttach> list = sysAttachService.getList(sysAttach);
        return ResultInfo.ok("获取列表成功", list);
    }

    /**
    * 【附件信息】根据id查询
    * @param id
    * @return
    */
    @RequestMapping("/get")
    @RequiresPermissions(value = {"sysAttach:edit","sysAttach:view"},logical = Logical.OR)
    public ResultInfo get(String id) {
        SysAttach sysAttach = sysAttachService.get(id);
        return ResultInfo.ok("获取对象成功", sysAttach);
    }

    /**
    * 【附件信息】提交(新增或修改)
    * @param sysAttach
    * @return
    */
    @RequestMapping("/sub")
    @RequiresPermissions("sysAttach:save")
    public ResultInfo insert(SysAttach sysAttach) {
        if (StringUtils.isEmpty(sysAttach.getId())) { //新增
            sysAttachService.insert(sysAttach);
        } else {//修改
            sysAttachService.update(sysAttach);
        }
        return ResultInfo.ok("提交成功!");
    }

    /**
    * 【附件信息】删除
    * @param id
    * @return
    */
    @RequestMapping("/delete")
    @RequiresPermissions("sysAttach:delete")
    public ResultInfo delete(String id) {
        sysAttachService.delete(id);
        return ResultInfo.ok("删除成功!");
    }

    /**
     * 【附件信息】批量删除
     * @param ids
     * @return
     */
    @RequestMapping("/delAll")
    public ResultInfo delAll(String[] ids) {
        sysAttachService.delAll(ids);
        return ResultInfo.ok("批量删除成功！");
    }

    /**
     *【附件信息】导出
     */
    @RequestMapping(value = "/export")
    @RequiresPermissions("sysAttach:export")
    public void exportFile(SysAttach sysAttach, HttpServletResponse response) {
        try {
            String fileName = "附件信息" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            List<SysAttach> list = sysAttachService.getList(sysAttach);
            new ExportExcel("附件信息", SysAttach.class).setDataList(list).write(response, fileName).dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *【附件信息】导入
     */
    @RequestMapping(value = "/import")
    @RequiresPermissions("sysAttach:import")
    public ResultInfo importFile(MultipartFile file) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<SysAttach> list = ei.getDataList(SysAttach.class);
            for (SysAttach sysAttach : list) {
                try {
                    sysAttachService.insert(sysAttach);
                    successNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条附件信息记录。");
            }
            return ResultInfo.ok("已成功导入 " + successNum + " 条附件信息记录" + failureMsg);
        } catch (Exception e) {
            return ResultInfo.error("导入附件信息失败！失败信息：" + e.getMessage());
        }
    }


    /**
     *【附件信息】模板下载
     */
    @RequestMapping(value = "/import/template")
    @RequiresPermissions("sysAttach:import")
    public ResultInfo importFileTemplate(HttpServletResponse response) {
        try {
            String fileName = "附件信息数据导入模板.xlsx";
            List<SysAttach> list = new ArrayList<>();
            new ExportExcel("附件信息数据", SysAttach.class, 1).setDataList(list).write(response, fileName).dispose();
            return ResultInfo.ok("下载模板成功！");
        } catch (Exception e) {
            return ResultInfo.error("导入模板下载失败！失败信息：" + e.getMessage());
        }
    }

    /*************************************************新增附件方法****************************************************************/

    /**
     * 上传附件
     *
     * @param request
     * @param attachType
     * @return
     */
    @RequestMapping("/uploadFile/{uploadType}/{attachType}")
    public ResultInfo ajaxUpload(HttpServletRequest request, MultipartFile file, @PathVariable("uploadType") String uploadType, @PathVariable("attachType") String attachType) {
        try {
            //FileHelper.validFile(file, uploadType);
            String uuid = IdGen.primaryKey();
            String savePath = sysAttachService.saveFile(attachType, uuid, file);
            if (StringUtils.isNotEmpty(savePath)) {
                SysAttach attach = new SysAttach();
                attach.setId(uuid);
                String fileName = file.getOriginalFilename();
                attach.setRemarks(fileName);
                attach.setFileModule(attachType);
                attach.setFileType(uploadType);//文件类型
                DecimalFormat df = new DecimalFormat("0.00");//格式化小数
                attach.setFileSize(df.format((float) file.getSize() / 1024));
                attach.setSuffix(fileName.substring(fileName.lastIndexOf(".")));
                attach.setFileName(attach.getId() + attach.getSuffix());
                attach.setSavePath(savePath);
                sysAttachService.insert(attach);
                return ResultInfo.ok("恭喜你,上传成功!", attach);
            }
        } catch (Exception e) {
            return ResultInfo.error("上传失败! " + e.getMessage());
        }
        return ResultInfo.error("对不起,上传失败!");
    }

    /**
     * 下载文件
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/downFile")
    public void downFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        SysAttach sysAttach = sysAttachService.get(id);
        if (sysAttach != null) {
            String hcPath =  fileUploadProperteis.getDiskPath() + "/" + fileUploadProperteis.getFileBasePath() + "/" + sysAttach.getSavePath();
            String hcName = sysAttach.getFileName();
            InputStream bis = new BufferedInputStream(new FileInputStream(new File(hcPath)));
            String filename = hcName;
            filename = URLEncoder.encode(filename, "UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename=" + filename);
            response.setContentType("multipart/form-data");
            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
            byte[] buffer = new byte[1024];
            int byteSum = 0;
            int byteRead = 0;
            while ((byteRead = bis.read(buffer)) != -1) {
                byteSum += byteRead;
                System.out.println(byteSum);
                out.write(buffer, 0, byteRead);
            }
            out.close();
        }
    }

    /**
     * 删除文件
     *
     * @param request
     * @throws Exception
     */
    @RequestMapping("/deleteFile")
    @ResponseBody
    public ResultInfo deleteFile(HttpServletRequest request){
        String id = request.getParameter("id");
        sysAttachService.delete(id);
        return ResultInfo.ok("附件删除成功!");
    }

    /**
     * 查询附件历史
     *
     * @param sysAttach
     * @return
     */
    @RequestMapping(value = "/getFileHistory")
    public ResultInfo getFileHistory(SysAttach sysAttach) {
        List<SysAttach> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(sysAttach.getRefId())) {
            list = sysAttachService.getList(sysAttach);
            return ResultInfo.ok("附件获取成功!", list);
        }else{
            return ResultInfo.ok("获取到0个附件!",list);
        }
    }

}



