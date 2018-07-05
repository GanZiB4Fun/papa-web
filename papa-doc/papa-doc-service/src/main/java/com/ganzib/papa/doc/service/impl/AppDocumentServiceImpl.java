package com.ganzib.papa.doc.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ganzib.papa.doc.mapper.AppDocumentMapper;
import com.ganzib.papa.doc.model.AppDocument;
import com.ganzib.papa.doc.service.IAppDocumentService;
import com.ganzib.papa.support.util.Pager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GanZiB
 * Date: 2018-07-02
 * Time: 上午11:12
 * Email: ganzib4fun@gmail.com
 */
@Service("appDocumentService")
public class AppDocumentServiceImpl extends ServiceImpl<AppDocumentMapper, AppDocument> implements IAppDocumentService {


    @Override
    public Boolean isServiceStart() {
        return true;
    }

    @Override
    public Page<AppDocument> pageFindByParams(Map<String, Object> paramsMap, Pager pager) {
        Page<AppDocument> page = new Page<>(pager.getPageIndex(), pager.getPageSize());
        List<AppDocument> docs = baseMapper.getArticleList(paramsMap, page);
        page.setRecords(docs);
        return page;
    }

    @Override
    public List<AppDocument> getAppDocumentList(Pager pager) {
        Page<AppDocument> page = new Page<>(pager.getPageIndex(), pager.getPageSize());
        List<AppDocument> docs = baseMapper.getAllArticleList(page);
        return docs;
    }
}
