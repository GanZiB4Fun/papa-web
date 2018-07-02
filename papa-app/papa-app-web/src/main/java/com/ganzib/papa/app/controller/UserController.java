package com.ganzib.papa.app.controller;

import com.ganzib.papa.user.model.AppUser;
import com.ganzib.papa.user.service.IAppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GanZiB
 * Date: 2018-07-02
 * Time: 上午11:30
 * Email: ganzib4fun@gmail.com
 */
@Controller
@RequestMapping("app/{version}")
public class UserController {
    @Autowired
    private IAppUserService userService;


    @RequestMapping(value = "/index", method = RequestMethod.GET, produces = {"text/html;charset=UTF-8"})
    public ModelAndView index(HttpServletRequest request, @PathVariable("version") String version,
                              @RequestParam(value = "userId", required = false) String userId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        if (userId != null) {
            AppUser appUser = userService.selectById(userId);
            if (appUser != null) {
                modelAndView.addObject("appUser", appUser);
            }
        }
        return modelAndView;
    }
}
