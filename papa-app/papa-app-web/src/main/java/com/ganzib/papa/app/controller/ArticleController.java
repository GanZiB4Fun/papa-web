package com.ganzib.papa.app.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.ganzib.papa.doc.model.AppDocument;
import com.ganzib.papa.doc.service.IAppDocumentService;
import com.ganzib.papa.support.date.DateUtils;
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
public class ArticleController {

    @Autowired
    private IAppDocumentService appDocumentService;

    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = {"text/html;charset=UTF-8"})
    public ModelAndView docListPage(HttpServletRequest request,
                                    @RequestParam(value = "page", required = false) Integer pageIndex,
                                    @RequestParam(value = "rows", required = false) Integer rows,
                                    @RequestParam(value = "author", required = false) String author,
                                    @RequestParam(value = "title", required = false) String title,
                                    @RequestParam(value = "tag", required = false) String tag) {
        ModelAndView modelAndView = new ModelAndView("article/article_list");
        Pager pager = new Pager();
        if (pageIndex == null) {
            pageIndex = 1;
        }
        if (rows == null || rows > 100) {
            rows = 10;
        }
        pager.setPageIndex(pageIndex);
        pager.setPageSize(rows);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("tag", tag);
        paramMap.put("author", author);
        paramMap.put("title", title);

        Page<AppDocument> appDocumentPage = appDocumentService.pageFindByParams(paramMap, pager);
        if (appDocumentPage != null) {
            if (pageIndex > 1) {
                modelAndView.addObject("previousPage", pageIndex - 1);

            }
            modelAndView.addObject("currentPage", appDocumentPage.getCurrent());
            modelAndView.addObject("nextPage", pageIndex + 1);
            modelAndView.addObject("articleList", appDocumentPage.getRecords());
        }
        modelAndView.addObject("tag", tag);
        modelAndView.addObject("title", title);
        modelAndView.addObject("author", author);
        return modelAndView;
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET, produces = {"text/html;charset=UTF-8"})
    public ModelAndView articleInfo(HttpServletRequest request,
                                    @RequestParam(value = "docId", required = true) String docId) {
        ModelAndView modelAndView = new ModelAndView();

        AppDocument appDocument = appDocumentService.selectById(docId);
        if (appDocument != null) {
            appDocument.setContent(appDocument.getContent().replace("简书", " ·违法犯罪GanZiB· "));
            appDocumentService.updateById(appDocument);
            modelAndView.addObject("appDocument", appDocument);
            String publishTime = DateUtils.DatePattern.PATTERN_DATE_YMD_POINT_HM.getDateStr(appDocument.getPublishTime());
            modelAndView.addObject("publishTime", publishTime);
            modelAndView.setViewName("/article/article_info");
        } else {
            modelAndView.setViewName("index");
        }

        return modelAndView;
    }

}
