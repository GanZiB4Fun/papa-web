package com.ganzib.papa.user.service;

import com.baomidou.mybatisplus.service.IService;
import com.ganzib.papa.user.model.AppUser;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GanZiB
 * Date: 2018-06-29
 * Time: 下午4:48
 * Email: ganzib4fun@gmail.com
 */
public interface IAppUserService extends IService<AppUser> {

    AppUser getUserById(String userId);

}
