package com.icss.front;

import com.icss.service.PlayListService;
import com.icss.entity.PlayList;
import com.icss.entity.Singer;
import com.icss.service.SingerService;
import com.icss.entity.Song;
import com.icss.service.SongService;
import com.icss.sys.base.entity.DateUtils;
import com.icss.sys.base.module.message.entity.SysMessage;
import com.icss.sys.base.module.message.service.SysMessageService;
import com.icss.sys.utils.admin.ObjectUtils;
import com.icss.sys.utils.excel.ExportExcel;
import com.icss.sys.utils.excel.ImportExcel;
import com.icss.sys.utils.jwt.JwtUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.*;
import com.icss.sys.utils.admin.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.icss.sys.base.module.extend.web.BaseController;
import com.icss.sys.base.entity.ResultInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
*【播放清单信息】管理
*/
@RestController
@RequestMapping("/api/playList")
public class ApiPlayListController extends BaseController {

    @Autowired
    private PlayListService playListService;
    @Autowired
    private SysMessageService sysMessageService;
    @Autowired
    private SingerService singerService;
    @Autowired
    private SongService songService;

    /**
     *【播放清单信息】获取分页数据
     */
    @RequestMapping("/getPage")
    public ResultInfo getPage(Page<PlayList> page, PlayList playList) {
            playList.setUserId(JwtUtils.getCurrentUserId());
            IPage<PlayList> iPage = playListService.getPage(page, playList);
            for (PlayList entity : iPage.getRecords()) {
                Song song = songService.get(entity.getSongId());
                entity.setPicture(song.getPicture());
                entity.setSongName(song.getName());
                Singer singer = singerService.get(song.getSingerId());
                entity.setSingerName(singer.getName());
            }
            return ResultInfo.ok("获取分页成功",iPage);

    }

    /**
     * 音乐组件列表歌曲
     * @param playList
     * @return
     */
    @RequestMapping("/getList")
    public ResultInfo getList(PlayList playList) {
        String searchType = playList.getSearchType();
        if ("collect".equals(searchType)) {
            SysMessage params = new SysMessage();
            params.setUserId(JwtUtils.getCurrentUserId());
            params.setType("收藏");
            List<SysMessage> list = sysMessageService.getList(params);
            //获取收藏歌曲id集合
            List<String> collect = list.stream().map(SysMessage::getRefId).collect(Collectors.toList());
            List<PlayList> songList = new ArrayList<>();
            for (String songId : collect) {
                PlayList entity = new PlayList();
                Song song = songService.get(songId);
                entity.setSongId(songId);
                entity.setSongName(song.getName());
                entity.setPicture(song.getPicture());
                Singer singer = singerService.get(song.getSingerId());
                entity.setSingerName(singer.getName());
                songList.add(entity);
            }
            return ResultInfo.ok("获取列表成功", songList);
        } else {
            PlayList params = new PlayList();
            params.setUserId(JwtUtils.getCurrentUserId());
            List<PlayList> list = playListService.getList(params);
            List<Song> songList = new ArrayList<>();
            for (PlayList entity : list) {
                Song song = songService.get(entity.getSongId());
                Singer singer = singerService.get(song.getSingerId());
                song.setSingerName(singer.getName());
                songList.add(song);
            }
            return ResultInfo.ok("获取列表成功", songList);
        }
    }

    /**
    *【播放清单信息】根据对象数据
    */
    @RequestMapping("/get")
    public ResultInfo get(PlayList playList) {
        PlayList entity = playListService.get(playList);
        return ResultInfo.ok("获取成功",entity);
    }

    /**
     *【播放清单信息】保存或修改
     */
    @RequestMapping("/sub")
    public ResultInfo saveOrUpdate(HttpServletRequest request,PlayList playList){
        try {
            if(StringUtils.isEmpty(playList.getId())){
                playList.setUserId(JwtUtils.getCurrentUserId());
                PlayList entity = playListService.get(playList);
                if(ObjectUtils.isNotNull(entity)){//防止重复添加
                    playListService.delete(entity.getId());
                }
                playListService.insert(playList);//新增
                return ResultInfo.ok("保存成功！");
            }else{
                playListService.update(playList);//修改
                return ResultInfo.ok("修改成功！");
            }
        } catch (Exception e) {
            return ResultInfo.error("保存失败！失败信息:"+e.getMessage());
        }
    }
    /**
     *【播放清单信息】根据id删除
     */
    @RequestMapping("/delete")
    public ResultInfo delete(HttpServletRequest request, PlayList playList) {
        playListService.delete(playList.getId());
        return ResultInfo.ok("删除成功！");
    }

    /**
    * 【播放清单信息】批量删除
    */
    @RequestMapping("/delAll")
    public ResultInfo delAll(String[] ids) {
        playListService.delAll(ids);
        return ResultInfo.ok("批量删除成功！");
    }

    /**
     *【播放清单】导出
     */
    @RequestMapping(value = "/export")
    public void exportFile(PlayList playList, HttpServletResponse response) {
        try {
            String fileName = "播放清单" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            List<PlayList> list = playListService.getList(playList);
            for (PlayList entity : list) {
                Song song = songService.get(entity.getSongId());
                if(song!=null){
                    entity.setSongId(song.getName());
                }
            }
            new ExportExcel("播放清单", PlayList.class).setDataList(list).write(response, fileName).dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *【播放清单】导入
     */
    @RequestMapping(value = "/import")
    public ResultInfo importFile(MultipartFile file,HttpServletRequest request) {
        try {
            int successNum = 0;
            int failureNum = 0;
            String userId = request.getParameter("userId");
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<PlayList> list = ei.getDataList(PlayList.class);
            for (PlayList playList : list) {
                try {
                    Song params = new Song();
                    params.setName(playList.getSongId());
                    Song song = songService.get(params);
                    if(song!=null){
                        playList.setSongId(song.getId());
                    }
                    playList.setUserId(userId);
                    PlayList playListF = playListService.get(playList);
                    if(playListF==null){
                        playListService.insert(playList);
                    }
                    successNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条播放清单记录。");
            }
            return ResultInfo.ok("已成功导入 " + successNum + " 条播放清单记录" + failureMsg);
        } catch (Exception e) {
            return ResultInfo.error("导入播放清单失败！失败信息：" + e.getMessage());
        }
    }

    /**
     *【播放清单】模板下载
     */
    @RequestMapping(value = "/import/template")
    public ResultInfo importFileTemplate(HttpServletResponse response) {
        try {
            String fileName = "播放清单数据导入模板.xlsx";
            List<PlayList> list = new ArrayList<>();
            new ExportExcel("播放清单数据", PlayList.class, 1).setDataList(list).write(response, fileName).dispose();
            return ResultInfo.ok("下载模板成功！");
        } catch (Exception e) {
            return ResultInfo.error("导入模板下载失败！失败信息：" + e.getMessage());
        }
    }

}



