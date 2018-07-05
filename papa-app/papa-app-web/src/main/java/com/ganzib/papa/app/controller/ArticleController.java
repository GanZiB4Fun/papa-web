package com.ganzib.papa.app.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.ganzib.papa.doc.model.AppDocument;
import com.ganzib.papa.doc.service.IAppDocumentService;
import com.ganzib.papa.support.util.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GanZiB
 * Date: 2018-07-05
 * Time: 上午11:29
 * Email: ganzib4fun@gmail.com
 */
@Controller
@RequestMapping("/article")
public class ArticleController{

    @Autowired
    private IAppDocumentService appDocumentService;

    @RequestMapping(value = "/article_list", method = RequestMethod.GET, produces = {"text/html;charset=UTF-8"})
    public ModelAndView docListPage(HttpServletRequest request,
                                    @RequestParam(value = "page", required = false) Integer pageIndex,
                                    @RequestParam(value = "rows", required = false) Integer rows,
                                    @RequestParam(value = "author", required = false) Integer author,
                                    @RequestParam(value = "tag", required = false) String tag) {
        ModelAndView modelAndView = new ModelAndView("article/article_list");
        Pager pager = new Pager();
        pager.setPageIndex(pageIndex);
        pager.setPageSize(rows);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("tag", tag);
        paramMap.put("author", author);
        Page<AppDocument> appDocumentPage = appDocumentService.pageFindByParams(paramMap, pager);
        modelAndView.addObject("articleList", appDocumentPage.getRecords());
        return modelAndView;
    }

}
