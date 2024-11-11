package com.icss.sys.base.module.gen.web;

import com.icss.sys.base.module.extend.web.BaseController;
import com.icss.sys.base.entity.ResultInfo;
import com.icss.sys.base.page.Page;
import com.icss.sys.base.module.dict.entity.DictTree;
import com.icss.sys.base.module.dict.entity.SysDict;
import com.icss.sys.base.module.dict.service.SysDictService;
import com.icss.sys.utils.admin.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;


/**
*   字典信息管理
*/
@Controller
@RequestMapping("/admin/sys/dict")
public class WebDictController extends BaseController {

    @Autowired
    private SysDictService sysDictService;

    /**
    *   字典信息列表页面
    */
    @RequestMapping("/manage")
    public ModelAndView list(ModelAndView modelAndView, SysDict sysDict) {
        modelAndView.setViewName("module/dict/sysDictManage");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/getList")
    public Page<SysDict> getList(Page<SysDict> page, SysDict sysDict) {
        Page<SysDict> pageDate = new Page();
        List<SysDict> list = sysDictService.getList(sysDict);
        pageDate.setData(list);
        pageDate.setLimit(10000000);
        pageDate.setCode("00000");
        return pageDate;
    }

    /**
    *   字典信息新增页面
    */
    @RequestMapping("/add")
    public ModelAndView add(ModelAndView modelAndView, SysDict sysDict) {
        modelAndView.setViewName("module/dict/sysDictAdd");
        return modelAndView;
    }
    /**
    *   数据字典新增页面
    */
    @RequestMapping("/dictTypeAdd")
    public ModelAndView addDictType(ModelAndView modelAndView, SysDict sysDict) {
        modelAndView.setViewName("module/dict/dictTypeAdd");
        return modelAndView;
    }
    /**
    *   数据字典编辑页面
    */
    @RequestMapping("/dictTypeEdit")
    public ModelAndView dictTypeEdit(ModelAndView modelAndView, SysDict sysDict) {
        SysDict sysDictF = sysDictService.get(sysDict.getId());
        modelAndView.addObject("sysDict", sysDictF);
        modelAndView.setViewName("module/dict/dictTypeEdit");
        return modelAndView;
    }
    /**
    *   字典信息查看页面
    */
    @RequestMapping("/view")
    public ModelAndView view(ModelAndView modelAndView, SysDict sysDict) {
        SysDict sysDictF = sysDictService.get(sysDict.getId());
        modelAndView.addObject("sysDict", sysDictF);
        modelAndView.setViewName("module/dict/sysDictView");
        return modelAndView;
    }
    /**
    *   字典信息编辑页面
    */
    @RequestMapping("/edit")
    public ModelAndView edit(ModelAndView modelAndView, SysDict sysDict) {
        SysDict sysDictF = sysDictService.get(sysDict.getId());
        modelAndView.addObject("sysDict", sysDictF);
        modelAndView.setViewName("module/dict/sysDictEdit");
        return modelAndView;
    }
    /**
    *  字典信息保存或修改
    */
    @ResponseBody
    @RequestMapping("/save")
    public ResultInfo saveOrUpdate(HttpServletRequest request, SysDict sysDict) {
        try {
            if (StringUtils.isNotEmpty(sysDict.getId())) {//编辑表单保存
                sysDictService.update(sysDict);//保存
                return ResultInfo.ok("修改成功！");
            } else {//新增表单保存
                sysDictService.insert(sysDict);//保存
                return ResultInfo.ok("保存成功！");
            }
        } catch (Exception e) {
            return ResultInfo.error("保存失败！失败信息:" + e.getMessage());
        }
    }
    /**
    *  字典信息根据id删除
    */
    @ResponseBody
    @RequestMapping("/delete")
    public ResultInfo delete(HttpServletRequest request, SysDict sysDict) {
        sysDictService.delete(sysDict.getId());
        return ResultInfo.ok("删除成功！");
    }
    /**
    *  字典信息批量删除
    */
    @ResponseBody
    @RequestMapping("/delAll")
    public ResultInfo delAll(HttpServletRequest request, SysDict sysDict) {
        String ids = request.getParameter("ids");
        sysDictService.delete(ids);
        return ResultInfo.ok("删除成功！");
    }

    @ResponseBody
    @RequestMapping("/getDictTree")
    public ResultInfo getDictTree(SysDict sysDict) {
        List<DictTree> list = sysDictService.getDictTree(sysDict);
        return ResultInfo.ok("获取字典树成功",list);
    }


}



