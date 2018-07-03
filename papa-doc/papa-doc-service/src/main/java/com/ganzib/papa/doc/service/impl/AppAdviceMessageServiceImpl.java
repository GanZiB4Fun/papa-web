package com.ganzib.papa.doc.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ganzib.papa.doc.mapper.AppAdviceMessageMapper;
import com.ganzib.papa.doc.model.AppAdviceMessage;
import com.ganzib.papa.doc.service.IAppAdviceMessageService;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GanZiB
 * Date: 2018-07-03
 * Time: 下午12:44
 * Email: ganzib4fun@gmail.com
 */
@Service("appAdviceMessageService")
public class AppAdviceMessageServiceImpl extends ServiceImpl<AppAdviceMessageMapper,AppAdviceMessage> implements IAppAdviceMessageService {
}
