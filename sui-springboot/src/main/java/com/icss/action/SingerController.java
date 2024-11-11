package com.icss.action;

import com.icss.service.SingerService;
import com.icss.entity.Singer;
import com.icss.entity.Song;
import com.icss.service.SongService;
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
*【歌手信息】页面接口
*/
@RestController
@RequestMapping("/admin/singer")
public class SingerController extends BaseController {

    @Autowired
    private SingerService singerService;
    @Autowired
    private SongService songService;

    /**
    * 【歌手信息】根据条件分页查询
    * @param page
    * @param singer
    * @return
    */
    @RequestMapping("/getPage")
    public ResultInfo getPage(Page<Singer> page, Singer singer) {
        IPage<Singer> iPage = singerService.getPage(page, singer);
        return ResultInfo.ok("获取分页成功", iPage);
    }

    /**
    * 【歌手信息】根据条件查询
    * @param singer
    * @return
    */
    @RequestMapping("/getList")
    public ResultInfo getList(Singer singer) {
        List<Singer> list = singerService.getList(singer);
        return ResultInfo.ok("获取列表成功", list);
    }

    /**
    * 【歌手信息】根据对象查询
    * @param singer
    * @return
    */
    @RequestMapping("/get")
    public ResultInfo get(Singer singer) {
        Singer entity = singerService.get(singer);
        return ResultInfo.ok("获取对象成功", entity);
    }

    /**
    * 【歌手信息】提交(新增或修改)
    * @param singer
    * @return
    */
    @RequestMapping("/sub")
    public ResultInfo insert(Singer singer) {
        if (StringUtils.isEmpty(singer.getId())) { //新增
            singerService.insert(singer);
        } else {//修改
            singerService.update(singer);
        }
        return ResultInfo.ok("提交成功!");
    }

    /**
    * 【歌手信息】删除
    * @param id
    * @return
    */
    @RequestMapping("/delete")
    public ResultInfo delete(String id) {
        Song params = new Song();
        params.setSingerId(id);
        List<Song> list = songService.getList(params);
        if(list.size()>0){
            return ResultInfo.error("该歌手存在相关歌曲，不能删除！");
        }
        singerService.delete(id);
        return ResultInfo.ok("删除成功!");
    }

    /**
    * 【歌手信息】批量删除
    * @param ids
    * @return
    */
    @RequestMapping("/delAll")
    public ResultInfo delAll(String[] ids) {
        for (String id : ids) {
            Song params = new Song();
            params.setSingerId(id);
            List<Song> list = songService.getList(params);
            if(list.size()>0){
                return ResultInfo.error("该歌手存在相关歌曲，不能删除！");
            }
            singerService.delete(id);
        }
        return ResultInfo.ok("批量删除成功！");
    }



}



