package com.icss.front;

import com.icss.service.SongSheetService;
import com.icss.entity.SongSheet;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.*;
import com.icss.sys.utils.admin.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import com.icss.sys.base.module.extend.web.BaseController;
import com.icss.sys.base.entity.ResultInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
*【歌单信息】管理
*/
@RestController
@RequestMapping("/api/songList")
public class ApiSongListController extends BaseController {

    @Autowired
    private SongSheetService songSheetService;

    /**
     *【歌单信息】获取分页数据
     */
    @RequestMapping("/getPage")
    public ResultInfo getPage(Page<SongSheet> page, SongSheet songSheet) {
        IPage<SongSheet> iPage = songSheetService.getPage(page, songSheet);
        return ResultInfo.ok("获取分页成功",iPage);
    }

    /**
    * 【歌单信息】获取列表数据
    */
    @RequestMapping("/getList")
    public ResultInfo getList(SongSheet songSheet) {
        List<SongSheet> list = songSheetService.getList(songSheet);
        return ResultInfo.ok("获取列表成功", list);
    }

    /**
    *【歌单信息】根据对象数据
    */
    @RequestMapping("/get")
    public ResultInfo get(SongSheet songSheet) {
        SongSheet entity = songSheetService.get(songSheet);
        return ResultInfo.ok("获取成功",entity);
    }

    /**
     *【歌单信息】保存或修改
     */
    @RequestMapping("/sub")
    public ResultInfo saveOrUpdate(HttpServletRequest request, SongSheet songSheet){
        try {
            if(StringUtils.isEmpty(songSheet.getId())){
                songSheetService.insert(songSheet);//新增
                return ResultInfo.ok("保存成功！");
            }else{
                songSheetService.update(songSheet);//修改
                return ResultInfo.ok("修改成功！");
            }
        } catch (Exception e) {
            return ResultInfo.error("保存失败！失败信息:"+e.getMessage());
        }
    }

    /**
     *【歌单信息】根据id删除
     */
    @RequestMapping("/delete")
    public ResultInfo delete(HttpServletRequest request, SongSheet songSheet) {
        songSheetService.delete(songSheet.getId());
        return ResultInfo.ok("删除成功！");
    }

    /**
    * 【歌单信息】批量删除
    */
    @RequestMapping("/delAll")
    public ResultInfo delAll(String[] ids) {
        songSheetService.delAll(ids);
        return ResultInfo.ok("批量删除成功！");
    }

}



