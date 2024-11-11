package com.icss.action;

import com.icss.entity.ListSong;
import com.icss.service.ListSongService;
import com.icss.service.SongService;
import com.icss.entity.Song;
import com.icss.sys.base.module.attach.entity.SysAttach;
import com.icss.sys.base.module.attach.service.SysAttachService;
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
*【歌曲信息】页面接口
*/
@RestController
@RequestMapping("/admin/song")
public class SongController extends BaseController {

    @Autowired
    private SongService songService;
    @Autowired
    private SysAttachService sysAttachService;
    @Autowired
    private ListSongService listSongService;

    /**
    * 【歌曲信息】根据条件分页查询
    * @param page
    * @param song
    * @return
    */
    @RequestMapping("/getPage")
    public ResultInfo getPage(Page<Song> page, Song song) {
        IPage<Song> iPage = songService.getPage(page, song);
        return ResultInfo.ok("获取分页成功", iPage);
    }

    /**
    * 【歌曲信息】根据条件查询
    * @param song
    * @return
    */
    @RequestMapping("/getList")
    public ResultInfo getList(Song song) {
        List<Song> list = songService.getList(song);
        return ResultInfo.ok("获取列表成功", list);
    }

    /**
    * 【歌曲信息】根据对象查询
    * @param song
    * @return
    */
    @RequestMapping("/get")
    public ResultInfo get(Song song) {
        Song entity = songService.get(song);
        return ResultInfo.ok("获取对象成功", entity);
    }

    /**
    * 【歌曲信息】提交(新增或修改)
    * @param song
    * @return
    */
    @RequestMapping("/sub")
    public ResultInfo insert(Song song) {
        List<String> fileList = song.getFileList();
        SysAttach sysAttach = sysAttachService.get(fileList.get(0));
        if(sysAttach!=null){
            song.setUrl(sysAttach.getSavePath());
        }
        if (StringUtils.isEmpty(song.getId())) { //新增
            songService.insert(song);
        } else {//修改
            songService.update(song);
        }
        return ResultInfo.ok("提交成功!");
    }

    /**
    * 【歌曲信息】删除
    * @param id
    * @return
    */
    @RequestMapping("/delete")
    public ResultInfo delete(String id) {
        ListSong params = new ListSong();
        params.setSongId(id);
        List<ListSong> list = listSongService.getList(params);
        if(list.size()>0){
            return ResultInfo.error("该歌曲已被歌单使用，移除歌单歌曲后才能删除");
        }
        songService.delete(id);
        return ResultInfo.ok("删除成功!");
    }

    /**
    * 【歌曲信息】批量删除
    * @param ids
    * @return
    */
    @RequestMapping("/delAll")
    public ResultInfo delAll(String[] ids) {
        for (String id : ids) {
            ListSong params = new ListSong();
            params.setSongId(id);
            List<ListSong> list = listSongService.getList(params);
            if(list.size()>0){
                return ResultInfo.error("该歌曲已被歌单使用，移除歌单歌曲后才能删除");
            }
            songService.delete(id);
        }
        return ResultInfo.ok("批量删除成功！");
    }



}



