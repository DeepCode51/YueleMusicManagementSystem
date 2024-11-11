package com.icss.front;

import com.icss.service.CategoryService;
import com.icss.entity.Category;
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
*【类别信息】管理
*/
@RestController
@RequestMapping("/api/category")
public class ApiCategoryController extends BaseController {

    @Autowired
    private CategoryService categoryService;

    /**
     *【类别信息】获取分页数据
     */
    @RequestMapping("/getPage")
    public ResultInfo getPage(Page<Category> page, Category category) {
        IPage<Category> iPage = categoryService.getPage(page, category);
        return ResultInfo.ok("获取分页成功",iPage);
    }

    /**
    * 【类别信息】获取列表数据
    */
    @RequestMapping("/getList")
    public ResultInfo getList(Category category) {
        List<Category> list = categoryService.getList(category);
        return ResultInfo.ok("获取列表成功", list);
    }

    /**
    *【类别信息】根据id数据
    */
    @RequestMapping("/get")
    public ResultInfo get(String id) {
        Category entity = categoryService.get(id);
        return ResultInfo.ok("获取成功",entity);
    }

    /**
     *【类别信息】保存或修改
     */
    @RequestMapping("/sub")
    public ResultInfo saveOrUpdate(HttpServletRequest request,Category category){
        try {
            if(StringUtils.isEmpty(category.getId())){
                categoryService.insert(category);//新增
                return ResultInfo.ok("保存成功！");
            }else{
                categoryService.update(category);//修改
                return ResultInfo.ok("修改成功！");
            }
        } catch (Exception e) {
            return ResultInfo.error("保存失败！失败信息:"+e.getMessage());
        }
    }

    /**
     *【类别信息】根据id删除
     */
    @RequestMapping("/delete")
    public ResultInfo delete(HttpServletRequest request, Category category) {
        categoryService.delete(category.getId());
        return ResultInfo.ok("删除成功！");
    }

}



