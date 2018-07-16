package com.ganzib.papa.app.controller;

import com.ganzib.papa.doc.model.AppAdviceMessage;
import com.ganzib.papa.doc.service.IAppAdviceMessageService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
 * Time: 下午8:52
 * Email: ganzib4fun@gmail.com
 */
@Controller
public class IndexController {

    private Logger logger = Logger.getLogger(IndexController.class);

    @Autowired
    private IAppAdviceMessageService appAdviceMessageService;

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = {"text/html;charset=UTF-8"})
    public ModelAndView toIndex() {
        return new ModelAndView("redirect:/index");
    }

    @RequestMapping(value = "/particles", method = RequestMethod.GET, produces = {"text/html;charset=UTF-8"})
    public ModelAndView toHome() {
        return new ModelAndView("particle");
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET, produces = {"text/html;charset=UTF-8"})
    public ModelAndView index(HttpServletRequest request) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        logger.debug("connect to index");
        return modelAndView;
    }

    @RequestMapping(value = "/addAdvice", method = RequestMethod.POST, produces = {"text/html;charset=UTF-8"})
    public String addAdvice(HttpServletRequest request, @RequestParam("name") String name,
                              @RequestParam("email") String email, @RequestParam("message") String message) {
        if (!StringUtils.isEmpty(name)){
            AppAdviceMessage appAdviceMessage = new AppAdviceMessage();
            appAdviceMessage.setName(name);
            appAdviceMessage.setEmail(email);
            appAdviceMessage.setMessage(message);
            appAdviceMessageService.insert(appAdviceMessage);
        }
        return "redirect:/index";
    }
}
