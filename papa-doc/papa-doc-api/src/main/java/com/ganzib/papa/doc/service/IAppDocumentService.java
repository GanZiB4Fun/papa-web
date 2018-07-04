package com.ganzib.papa.doc.service;


import com.baomidou.mybatisplus.service.IService;
import com.ganzib.papa.doc.model.AppDocument;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GanZiB
 * Date: 2018-07-02
 * Time: 上午10:56
 * Email: ganzib4fun@gmail.com
 */
public interface IAppDocumentService extends IService<AppDocument> {

    /**
     * 测试Dubbo 服务是否启动成功
     * @return
     */
    Boolean isServiceStart();

}
