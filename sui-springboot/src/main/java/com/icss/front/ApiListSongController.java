package com.icss.front;

import com.icss.service.ListSongService;
import com.icss.entity.ListSong;
import com.icss.entity.Singer;
import com.icss.service.SingerService;
import com.icss.entity.Song;
import com.icss.service.SongService;
import com.icss.sys.base.module.attach.entity.SysAttach;
import com.icss.sys.base.module.attach.service.SysAttachService;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.lang.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import com.icss.sys.base.module.extend.web.BaseController;
import com.icss.sys.base.entity.ResultInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
*【歌单关联信息】管理
*/
@RestController
@RequestMapping("/api/listSong")
public class ApiListSongController extends BaseController {

    @Autowired
    private ListSongService listSongService;
    @Autowired
    private SongService songService;
    @Autowired
    private SingerService singerService;
    @Autowired
    private SysAttachService sysAttachService;

    /**
     *【歌单关联信息】获取分页数据
     */
    @RequestMapping("/getPage")
    public ResultInfo getPage(ListSong listSong) {
        List<ListSong> list = listSongService.getList(listSong);
        List<Song> listAll = new ArrayList<>();
        for (ListSong ls : list) {
            Song song = songService.get(ls.getSongId());
            SysAttach params = new SysAttach();
            params.setRefId(song.getId());
            List<SysAttach> fileList = sysAttachService.getList(params);
            if (fileList.size() > 0) {//获取歌曲文件
                song.setUrl(fileList.get(0).getSavePath());
            }
            Singer singer = singerService.get(song.getSingerId());
            song.setSingerName(singer.getName());
            listAll.add(song);
        }
        return ResultInfo.ok("获取分页成功", listAll);
    }

    /**
    * 【歌单关联信息】获取列表数据
    */
    @RequestMapping("/getList")
    public ResultInfo getList(ListSong listSong) {
        List<ListSong> list = listSongService.getList(listSong);
        return ResultInfo.ok("获取列表成功", list);
    }

    /**
    *【歌单关联信息】根据对象数据
    */
    @RequestMapping("/get")
    public ResultInfo get(ListSong listSong) {
        ListSong entity = listSongService.get(listSong);
        return ResultInfo.ok("获取成功",entity);
    }

    /**
     *【歌单关联信息】保存或修改
     */
    @RequestMapping("/sub")
    public ResultInfo saveOrUpdate(HttpServletRequest request,ListSong listSong){
        try {
            if(StringUtils.isEmpty(listSong.getId())){
                listSongService.insert(listSong);//新增
                return ResultInfo.ok("保存成功！");
            }else{
                listSongService.update(listSong);//修改
                return ResultInfo.ok("修改成功！");
            }
        } catch (Exception e) {
            return ResultInfo.error("保存失败！失败信息:"+e.getMessage());
        }
    }

    /**
     *【歌单关联信息】根据id删除
     */
    @RequestMapping("/delete")
    public ResultInfo delete(HttpServletRequest request, ListSong listSong) {
        listSongService.delete(listSong.getId());
        return ResultInfo.ok("删除成功！");
    }

    /**
    * 【歌单关联信息】批量删除
    */
    @RequestMapping("/delAll")
    public ResultInfo delAll(String[] ids) {
        listSongService.delAll(ids);
        return ResultInfo.ok("批量删除成功！");
    }

}



