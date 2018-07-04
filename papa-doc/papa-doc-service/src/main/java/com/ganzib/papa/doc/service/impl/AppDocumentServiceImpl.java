package com.ganzib.papa.doc.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ganzib.papa.doc.mapper.AppDocumentMapper;
import com.ganzib.papa.doc.model.AppDocument;
import com.ganzib.papa.doc.service.IAppDocumentService;
import org.springframework.stereotype.Service;

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
}
