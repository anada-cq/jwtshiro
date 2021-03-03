package com.woniuxy.service.impl;

import com.woniuxy.model.Film;
import com.woniuxy.mapper.FilmMapper;
import com.woniuxy.service.FilmService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ananda
 * @since 2021-02-02
 */
@Service
public class FilmServiceImpl extends ServiceImpl<FilmMapper, Film> implements FilmService {

}
