package com.icss.action;

import com.icss.entity.ListSong;
import com.icss.service.ListSongService;
import com.icss.service.SongSheetService;
import com.icss.entity.SongSheet;
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
*【歌单信息】页面接口
*/
@RestController
@RequestMapping("/admin/songSheet")
public class SongSheetController extends BaseController {

    @Autowired
    private SongSheetService songSheetService;
    @Autowired
    private ListSongService listSongService;

    /**
    * 【歌单信息】根据条件分页查询
    * @param page
    * @param songSheet
    * @return
    */
    @RequestMapping("/getPage")
    public ResultInfo getPage(Page<SongSheet> page, SongSheet songSheet) {
        IPage<SongSheet> iPage = songSheetService.getPage(page, songSheet);
        return ResultInfo.ok("获取分页成功", iPage);
    }

    /**
    * 【歌单信息】根据条件查询
    * @param songSheet
    * @return
    */
    @RequestMapping("/getList")
    public ResultInfo getList(SongSheet songSheet) {
        List<SongSheet> list = songSheetService.getList(songSheet);
        return ResultInfo.ok("获取列表成功", list);
    }

    /**
    * 【歌单信息】根据对象查询
    * @param songSheet
    * @return
    */
    @RequestMapping("/get")
    public ResultInfo get(SongSheet songSheet) {
        SongSheet entity = songSheetService.get(songSheet);
        return ResultInfo.ok("获取对象成功", entity);
    }

    /**
    * 【歌单信息】提交(新增或修改)
    * @param songSheet
    * @return
    */
    @RequestMapping("/sub")
    public ResultInfo insert(SongSheet songSheet) {
        if (StringUtils.isEmpty(songSheet.getId())) { //新增
            songSheetService.insert(songSheet);
        } else {//修改
            songSheetService.update(songSheet);
        }
        return ResultInfo.ok("提交成功!");
    }

    /**
    * 【歌单信息】删除
    * @param id
    * @return
    */
    @RequestMapping("/delete")
    public ResultInfo delete(String id) {
        ListSong params = new ListSong();
        params.setSongSheetId(id);
        List<ListSong> list = listSongService.getList(params);
        if(list.size()>0){
            return ResultInfo.error("该歌单存在歌曲，移除歌单歌曲后才能删除");
        }
        songSheetService.delete(id);
        return ResultInfo.ok("删除成功!");
    }

    /**
    * 【歌单信息】批量删除
    * @param ids
    * @return
    */
    @RequestMapping("/delAll")
    public ResultInfo delAll(String[] ids) {
        for (String id : ids) {
            ListSong params = new ListSong();
            params.setSongSheetId(id);
            List<ListSong> list = listSongService.getList(params);
            if(list.size()>0){
                return ResultInfo.error("该歌单存在歌曲，移除歌单歌曲后才能删除");
            }
            songSheetService.delete(id);
        }
        return ResultInfo.ok("批量删除成功！");
    }



}



