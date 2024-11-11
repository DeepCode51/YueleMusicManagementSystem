package com.icss.front;

import com.icss.entity.Song;
import com.icss.service.SongService;
import com.icss.sys.base.module.extend.web.BaseController;
import com.icss.sys.base.entity.ResultInfo;
import com.icss.sys.base.module.message.service.SysMessageService;
import com.icss.sys.utils.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.util.*;

/**
*【推荐歌曲】管理
*/
@RestController
@RequestMapping("/api/recommend")
public class ApiRecommendController extends BaseController {

    @Autowired
    private SongService songService;
    @Autowired
    private SysMessageService sysMessageService;


    /**
    * 获取最新数据
    */
    @RequestMapping("/getLastList")
    public ResultInfo getNewList(Song song) {
        List<Song> list = songService.getList(song);
        list = list.size()>5?list.subList(0,5):list;
        return ResultInfo.ok("获取列表成功", list);
    }

    /**
     * 获取推荐数据
     */
    @RequestMapping("/getRecommendList")
    public ResultInfo getRecommendList(Song song, HttpServletRequest request) {
        String currentUserId = JwtUtils.getCurrentUserId();
        List<Song> recommendList = null;
        List<Song> songList = songService.getList(song);
        Collections.shuffle(songList);//打乱顺序
        recommendList = songList.subList(0, 3);
        return ResultInfo.ok("获取用户推荐数据", recommendList);
    }

}



