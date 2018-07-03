package com.ganzib.papa.user.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ganzib.papa.user.mapper.AppUserMapper;
import com.ganzib.papa.user.model.AppUser;
import com.ganzib.papa.user.service.IAppUserService;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GanZiB
 * Date: 2018-06-29
 * Time: 下午5:48
 * Email: ganzib4fun@gmail.com
 */
@Service("appUserService")
public class AppUserServiceImpl extends ServiceImpl<AppUserMapper, AppUser> implements IAppUserService {
    @Override
    public AppUser getUserById(String userId) {
        return baseMapper.selectById(userId);
    }
}
