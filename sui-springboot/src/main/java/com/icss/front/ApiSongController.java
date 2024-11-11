package com.icss.front;

import com.icss.entity.Singer;
import com.icss.service.SingerService;
import com.icss.service.SongService;
import com.icss.entity.Song;
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
*【歌曲信息】管理
*/
@RestController
@RequestMapping("/api/song")
public class ApiSongController extends BaseController {

    @Autowired
    private SongService songService;
    @Autowired
    private SingerService singerService;

    /**
     *【歌曲信息】获取分页数据
     */
    @RequestMapping("/getPage")
    public ResultInfo getPage(Page<Song> page, Song song) {
        IPage<Song> iPage = songService.getPage(page, song);
        List<Song> records = iPage.getRecords();
        for (Song entity : records) {
            Singer singer = singerService.get(entity.getSingerId());
            entity.setSingerName(singer.getName());
        }
        return ResultInfo.ok("获取分页成功",iPage);
    }

    /**
    * 【歌曲信息】获取列表数据
    */
    @RequestMapping("/getList")
    public ResultInfo getList(Song song) {
        List<Song> list = songService.getList(song);
        for (Song entity : list) {
            Singer singer = singerService.get(entity.getSingerId());
            entity.setSingerName(singer.getName());
        }
        return ResultInfo.ok("获取列表成功", list);
    }

    /**
    *【歌曲信息】根据对象数据
    */
    @RequestMapping("/get")
    public ResultInfo get(Song song) {
        Song entity = songService.get(song);
        String singerId = entity.getSingerId();
        Singer singer = singerService.get(singerId);
        entity.setSingerName(singer.getName());
        return ResultInfo.ok("获取成功",entity);
    }

    /**
     *【歌曲信息】保存或修改
     */
    @RequestMapping("/sub")
    public ResultInfo saveOrUpdate(HttpServletRequest request,Song song){
        try {
            if(StringUtils.isEmpty(song.getId())){
                songService.insert(song);//新增
                return ResultInfo.ok("保存成功！");
            }else{
                songService.update(song);//修改
                return ResultInfo.ok("修改成功！");
            }
        } catch (Exception e) {
            return ResultInfo.error("保存失败！失败信息:"+e.getMessage());
        }
    }

    /**
     *【歌曲信息】根据id删除
     */
    @RequestMapping("/delete")
    public ResultInfo delete(HttpServletRequest request, Song song) {
        songService.delete(song.getId());
        return ResultInfo.ok("删除成功！");
    }

    /**
    * 【歌曲信息】批量删除
    */
    @RequestMapping("/delAll")
    public ResultInfo delAll(String[] ids) {
        songService.delAll(ids);
        return ResultInfo.ok("批量删除成功！");
    }

}



