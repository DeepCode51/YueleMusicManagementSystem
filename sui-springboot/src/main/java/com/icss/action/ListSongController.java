package com.icss.action;

import com.icss.service.ListSongService;
import com.icss.entity.ListSong;
import com.icss.sys.base.module.extend.web.BaseController;
import com.icss.sys.base.entity.ResultInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
*【歌单关联信息】页面接口
*/
@RestController
@RequestMapping("/admin/listSong")
public class ListSongController extends BaseController {

    @Autowired
    private ListSongService listSongService;

    /**
    * 【歌单关联信息】根据条件分页查询
    * @param page
    * @param listSong
    * @return
    */
    @RequestMapping("/getPage")
    public ResultInfo getPage(Page<ListSong> page, ListSong listSong) {
        IPage<ListSong> iPage = listSongService.getPage(page, listSong);
        return ResultInfo.ok("获取分页成功", iPage);
    }

    /**
    * 【歌单关联信息】根据条件查询
    * @param listSong
    * @return
    */
    @RequestMapping("/getList")
    public ResultInfo getList(ListSong listSong) {
        List<ListSong> list = listSongService.getList(listSong);
        return ResultInfo.ok("获取列表成功", list);
    }

    /**
    * 【歌单关联信息】根据对象查询
    * @param listSong
    * @return
    */
    @RequestMapping("/get")
    public ResultInfo get(ListSong listSong) {
        ListSong entity = listSongService.get(listSong);
        return ResultInfo.ok("获取对象成功", entity);
    }

    /**
    * 【歌单关联信息】提交(新增或修改)
    * @param listSong
    * @return
    */
    @RequestMapping("/sub")
    public ResultInfo insert(ListSong listSong) {
        ListSong params = new ListSong();
        //先删除歌单歌曲
        params.setSongSheetId(listSong.getSongSheetId());
        List<ListSong> list = listSongService.getList(params);
        for (ListSong song : list) {
            listSongService.delete(song.getId());
        }
        //重新添加
        String[] songIds = listSong.getSongIds();
        if(songIds!=null && songIds.length>0){
            for (String songId : songIds) {
                ListSong entity = new ListSong();
                entity.setSongId(songId);
                entity.setSongSheetId(listSong.getSongSheetId());
                listSongService.insert(entity);
            }
        }
        return ResultInfo.ok("提交成功!");
    }

    /**
    * 【歌单关联信息】删除
    * @param id
    * @return
    */
    @RequestMapping("/delete")
    public ResultInfo delete(String id) {
        listSongService.delete(id);
        return ResultInfo.ok("删除成功!");
    }

    /**
    * 【歌单关联信息】批量删除
    * @param ids
    * @return
    */
    @RequestMapping("/delAll")
    public ResultInfo delAll(String[] ids) {
        listSongService.delAll(ids);
        return ResultInfo.ok("批量删除成功！");
    }



}



