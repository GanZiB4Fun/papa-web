package com.ganzib.papa.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GanZiB
 * Date: 2018-07-02
 * Time: 下午8:52
 * Email: ganzib4fun@gmail.com
 */
@Controller
public class IndexController {

    @RequestMapping(value = "/index", method = RequestMethod.GET, produces = {"text/html;charset=UTF-8"})
    public ModelAndView index(HttpServletRequest request) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }
}
