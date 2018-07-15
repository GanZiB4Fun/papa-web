package com.ganzib.papa.doc.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.ganzib.papa.doc.model.AppNovel;
import com.ganzib.papa.support.util.Pager;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GanZiB
 * Date: 2018-07-03
 * Time: 下午12:43
 * Email: ganzib4fun@gmail.com
 */
public interface IAppNovelService extends IService<AppNovel> {

    /**
     * 测试Dubbo 服务是否启动成功
     *
     * @return
     */
    Boolean isServiceStart();

    Page<AppNovel> pageFindByParams(Map<String, Object> paramsMap, Pager pager);

    List<String> getTags();
}
