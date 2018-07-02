package com.ganzib.papa.app.controller;

import com.ganzib.papa.user.model.AppUser;
import com.ganzib.papa.user.service.PapaAppUserService;
import org.apache.log4j.Logger;
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
@RequestMapping("/user")
public class UserController {

    private Logger logger = Logger.getLogger(UserController.class);

    @Autowired
    private PapaAppUserService papaAppUserService;


    @RequestMapping(value = "/index", method = RequestMethod.GET, produces = {"text/html;charset=UTF-8"})
    public ModelAndView index(HttpServletRequest request, @PathVariable("version") String version,
                              @RequestParam(value = "userId", required = false) String userId) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        if (userId != null) {
            AppUser appUser = null;
            try {
                appUser = papaAppUserService.getUserById(userId);
            } catch (Exception e) {
                logger.error("Error ", e);
            }
            if (appUser != null) {
                modelAndView.addObject("appUser", appUser);
            }
        }
        return modelAndView;
    }
}
