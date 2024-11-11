package com.icss.action;

import com.icss.service.PlayListService;
import com.icss.entity.PlayList;
import com.icss.sys.base.module.extend.web.BaseController;
import com.icss.sys.base.entity.ResultInfo;
import com.icss.sys.utils.admin.StringUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
*【播放清单信息】页面接口
*/
@RestController
@RequestMapping("/admin/playList")
public class PlayListController extends BaseController {

    @Autowired
    private PlayListService playListService;

    /**
    * 【播放清单信息】根据条件分页查询
    * @param page
    * @param playList
    * @return
    */
    @RequestMapping("/getPage")
    public ResultInfo getPage(Page<PlayList> page, PlayList playList) {
        IPage<PlayList> iPage = playListService.getPage(page, playList);
        return ResultInfo.ok("获取分页成功", iPage);
    }

    /**
    * 【播放清单信息】根据条件查询
    * @param playList
    * @return
    */
    @RequestMapping("/getList")
    public ResultInfo getList(PlayList playList) {
        List<PlayList> list = playListService.getList(playList);
        return ResultInfo.ok("获取列表成功", list);
    }

    /**
    * 【播放清单信息】根据对象查询
    * @param playList
    * @return
    */
    @RequestMapping("/get")
    public ResultInfo get(PlayList playList) {
        PlayList entity = playListService.get(playList);
        return ResultInfo.ok("获取对象成功", entity);
    }

    /**
    * 【播放清单信息】提交(新增或修改)
    * @param playList
    * @return
    */
    @RequestMapping("/sub")
    public ResultInfo insert(PlayList playList) {
        if (StringUtils.isEmpty(playList.getId())) { //新增
            playListService.insert(playList);
        } else {//修改
            playListService.update(playList);
        }
        return ResultInfo.ok("提交成功!");
    }

    /**
    * 【播放清单信息】删除
    * @param id
    * @return
    */
    @RequestMapping("/delete")
    public ResultInfo delete(String id) {
        playListService.delete(id);
        return ResultInfo.ok("删除成功!");
    }

    /**
    * 【播放清单信息】批量删除
    * @param ids
    * @return
    */
    @RequestMapping("/delAll")
    public ResultInfo delAll(String[] ids) {
        playListService.delAll(ids);
        return ResultInfo.ok("批量删除成功！");
    }



}



