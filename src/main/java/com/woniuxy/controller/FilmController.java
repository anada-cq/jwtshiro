package com.woniuxy.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.woniuxy.dto.Result;
import com.woniuxy.dto.StatusCode;
import com.woniuxy.mapper.FilmMapper;
import com.woniuxy.model.Film;
import com.woniuxy.vo.FilmVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ananda
 * @since 2021-02-02
 */
@RestController
@RequestMapping("/film")
public class FilmController {

    @Resource
    private FilmMapper filmMapper;

    //查询电影
    @RequestMapping("all")
    public Result getFilm(@RequestBody FilmVO filmVO){
        System.out.println(filmVO);
//        List<Film> films = filmMapper.selectList(null);
        //生成分页对象
        Page<Film> page = new Page<>(filmVO.getCurrent(), filmVO.getSize());
        //查询对应页面的数据
        IPage<Film> iPage = filmMapper.selectPage(page, null);
        page.getCurrent();
        page.getSize();
        page.getTotal();
        page.getRecords();
        return  new Result(true, StatusCode.OK,"查询成功",iPage);
    }

}

