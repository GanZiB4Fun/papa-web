package com.ganzib.papa.doc;

import com.ganzib.papa.user.model.AppUser;
import com.ganzib.papa.user.service.IAppUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GanZiB
 * Date: 2018-07-02
 * Time: 下午2:47
 * Email: ganzib4fun@gmail.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class AppDocumentServiceImplTest {

    @Autowired
    private IAppUserService appUserService;

    @Test
    public void test() {
        AppUser appUser = appUserService.getUserById("sdada");
        System.out.println(appUser);
    }

}
