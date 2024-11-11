package com.icss.action;

import com.icss.entity.Category;
import com.icss.service.CategoryService;
import com.icss.sys.base.module.extend.web.BaseController;
import com.icss.sys.base.entity.ResultInfo;
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
 *【类别信息】页面接口
 */
@RestController
@RequestMapping("/admin/category")
public class CategoryController extends BaseController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 【类别信息】根据条件分页查询
     * @param page
     * @param category
     * @return
     */
    @RequestMapping("/getPage")
    @RequiresPermissions("category:getPage")
    public ResultInfo getPage(Page<Category> page, Category category) {
        IPage<Category> iPage = categoryService.getPage(page, category);
        return ResultInfo.ok("获取分页成功", iPage);
    }

    /**
     * 【类别信息】根据条件查询
     * @param category
     * @return
     */
    @RequestMapping("/getList")
    public ResultInfo getList(Category category) {
        List<Category> list = categoryService.getList(category);
        return ResultInfo.ok("获取列表成功", list);
    }

    /**
     * 【类别信息】根据id查询
     * @param id
     * @return
     */
    @RequestMapping("/get")
    @RequiresPermissions(value = {"category:edit","category:view"},logical = Logical.OR)
    public ResultInfo get(String id) {
        Category category = categoryService.get(id);
        return ResultInfo.ok("获取对象成功", category);
    }

    /**
     * 【类别信息】提交(新增或修改)
     * @param category
     * @return
     */
    @RequestMapping("/sub")
    @RequiresPermissions("category:save")
    public ResultInfo insert(Category category) {
        if (StringUtils.isEmpty(category.getId())) { //新增
            categoryService.insert(category);
        } else {//修改
            categoryService.update(category);
        }
        return ResultInfo.ok("提交成功!");
    }

    /**
     * 【类别信息】删除
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions("category:delete")
    public ResultInfo delete(String id) {
        categoryService.delete(id);
        return ResultInfo.ok("删除成功!");
    }

    /**
     * 【类别信息】批量删除
     * @param ids
     * @return
     */
    @RequestMapping("/delAll")
    @RequiresPermissions("category:delete")
    public ResultInfo delAll(String[] ids) {
        categoryService.delAll(ids);
        return ResultInfo.ok("批量删除成功！");
    }



}



