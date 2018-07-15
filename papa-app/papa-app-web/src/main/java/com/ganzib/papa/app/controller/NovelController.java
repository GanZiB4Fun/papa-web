package com.ganzib.papa.app.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.ganzib.papa.doc.model.AppNovel;
import com.ganzib.papa.doc.service.IAppNovelService;
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
import java.util.List;
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
@RequestMapping("/novel")
public class NovelController {

    @Autowired
    private IAppNovelService appNovelService;

    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = {"text/html;charset=UTF-8"})
    public ModelAndView docListPage(HttpServletRequest request,
                                    @RequestParam(value = "page", required = false) Integer pageIndex,
                                    @RequestParam(value = "rows", required = false) Integer rows,
                                    @RequestParam(value = "tag", required = false) String tag) {
        ModelAndView modelAndView = new ModelAndView("novel/novel_list");
        Pager pager = new Pager();
        if (pageIndex == null) {
            pageIndex = 1;
        }
        if (rows == null || rows > 100) {
            rows = 10;
        }

        List<String> tagList = appNovelService.getTags();
        pager.setPageIndex(pageIndex);
        pager.setPageSize(rows);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("tag", tag);
        modelAndView.addObject("tagList", tagList);
        Page<AppNovel> appNovelPage = appNovelService.pageFindByParams(paramMap, pager);
        if (appNovelPage != null) {
            if (pageIndex > 1) {
                modelAndView.addObject("previousPage", pageIndex - 1);

            }
            modelAndView.addObject("currentPage", appNovelPage.getCurrent());
            modelAndView.addObject("nextPage", pageIndex + 1);
            modelAndView.addObject("novelList", appNovelPage.getRecords());
        }
        return modelAndView;
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET, produces = {"text/html;charset=UTF-8"})
    public ModelAndView novelInfo(HttpServletRequest request,
                                  @RequestParam(value = "novelId", required = true) String novelId) {
        ModelAndView modelAndView = new ModelAndView();

        AppNovel appNovel = appNovelService.selectById(novelId);
        if (appNovel != null) {
            modelAndView.addObject("novel", appNovel);
            String publishTime = DateUtils.DatePattern.PATTERN_DATE_YMD_POINT_HM.getDateStr(appNovel.getCreateTime());
            modelAndView.addObject("publishTime", publishTime);
            modelAndView.setViewName("/novel/novel_info");
        } else {
            modelAndView.setViewName("index");
        }

        return modelAndView;
    }

}
