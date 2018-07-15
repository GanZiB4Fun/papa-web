package com.ganzib.papa.doc.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ganzib.papa.doc.mapper.AppNovelMapper;
import com.ganzib.papa.doc.model.AppNovel;
import com.ganzib.papa.doc.service.IAppNovelService;
import com.ganzib.papa.support.util.Pager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GanZiB
 * Date: 2018-07-03
 * Time: 下午12:44
 * Email: ganzib4fun@gmail.com
 */
@Service("appNovelService")
public class AppNovelServiceImpl extends ServiceImpl<AppNovelMapper, AppNovel> implements IAppNovelService {

    @Override
    public Boolean isServiceStart() {
        return true;
    }


    @Override
    public Page<AppNovel> pageFindByParams(Map<String, Object> paramsMap, Pager pager) {
        Page<AppNovel> page = new Page<>(pager.getPageIndex(), pager.getPageSize());
        List<AppNovel> docs = baseMapper.getArticleList(paramsMap, page);
        page.setRecords(docs);
        return page;
    }

    @Override
    public List<String> getTags() {
        return baseMapper.getTags();
    }
}
