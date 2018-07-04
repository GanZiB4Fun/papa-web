package com.ganzib.papa.sprider.service;

import com.ganzib.papa.doc.model.AppDocument;
import com.ganzib.papa.doc.service.IAppDocumentService;
import com.ganzib.papa.sprider.constant.SHA1;
import com.ganzib.papa.sprider.constant.WebConstant;
import com.ganzib.papa.sprider.thread.SpriderThreadPool;
import com.ganzib.papa.support.date.DateUtils;
import com.ganzib.papa.user.service.IAppUserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.selector.Html;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GanZiB
 * Date: 2018-07-04
 * Time: 下午2:23
 * Email: ganzib4fun@gmail.com
 */
@Service("jianShuSpiderService")
public class JianShuSpiderService {

    private Logger logger = Logger.getLogger(JianShuSpiderService.class);

    @Autowired
    private IAppDocumentService appDocumentService;
    @Autowired
    private IAppUserService appUserService;
    @Autowired
    private SpriderThreadPool spriderThreadPool;

    private static Site site = Site.me().
            setUserAgent(WebConstant.USER_AGENT).
            addHeader("accept", WebConstant.HEADER_ACCEPT).
            addHeader("Accept-Language", WebConstant.ACCEPT_LANGUAGE).addHeader("Content-type", "text/html")
            .setTimeOut(30000)
            .setSleepTime(3000).setCharset("UTF-8");

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");

    private static boolean startFlag = false;

    /*@PostConstruct
    public void init() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                logger.info("ArticleIncomeRecordService has started");
                boolean isServiceStart = false;
                do {
                    try {
                        isServiceStart = appDocumentService.isServiceStart();
                        startFlag = true;
                    } catch (Exception e) {
                        logger.info("appArticleIncomeRecordService is not found,please wait");
                        try {
                            Thread.sleep(2000L);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                } while (!isServiceStart);
            }
        }).start();
    }*/

    @PostConstruct
    public void spider() {
        logger.info("jian shu spider task start");
        /*if (!startFlag) {
            return;
        }*/
        Page page = null;
        String startUrl = "https://www.jianshu.com/recommendations/collections?utm_medium=index-collections&utm_source=desktop";
        try {
            page = new HttpClientDownloader().download(new Request(startUrl),site.toTask());
        }catch (Exception e){
            logger.error("Http connect error ",e);
        }
        if (page!=null){
            Html html = page.getHtml();
            List<String> divList = html.xpath("//div[@id='list-container']/div[@class='col-xs-8']").all();
            for (String divStr : divList){
                Html divHtml = new Html(divStr);
                String url = divHtml.xpath("//div[@class='collection-wrap']/a/@href").get();
                for (int i = 0; i <= 10; i++) {
                    String realUrl = "https://www.jianshu.com/" + url + "?order_by=added_at&page=" + i;
                    Page articlePage = null;
                    try {
                        articlePage = new HttpClientDownloader().download(new Request(realUrl), site.toTask());
                    } catch (Exception e) {
                        logger.error("Http connect error ", e);
                    }
                    if (articlePage != null) {
                        Html articleListHtml = articlePage.getHtml();
                        List<String> articleDivList = articleListHtml.xpath("//ul[@class='note-list']/li/div[@class='content']").all();
                        for (String articleDiv : articleDivList) {
                            ansySpider(articleDiv);
                        }
                    }
                }
            }
        }

    }
    public void ansySpider(String divStr){
       spriderThreadPool.addTask(new Runnable() {
           @Override
           public void run() {
               AppDocument appDocument = new AppDocument();
               Html html = new Html(divStr);
               String img = html.xpath("//a[@class='wrap-img']/img/@src").get();
               if (img != null) {
                   img = "http://" + img.substring(2, img.indexOf("?"));
               }
               String infoUrl = html.xpath("//div[@class='content']/a[@class='title']/@href").get();
               if (infoUrl != null) {
                   infoUrl = "https://www.jianshu.com" + infoUrl;
                   String articleId = SHA1.encode(infoUrl);
                   Page infoPage = null;
                   try {
                       infoPage = new HttpClientDownloader().download(new Request(infoUrl), site.toTask());
                   } catch (Exception e) {
                       logger.error("Http connect error ", e);
                   }
                   if (infoPage != null) {
                       Html infoHtml = infoPage.getHtml();
                       String title = infoHtml.xpath("//meta[@property='twitter:title']/@content").get();
                       String description = infoHtml.xpath("//meta[@name='description']/@content").get();
                       String authorName = infoHtml.xpath("//div[@class='info']/span[@class='name']/allText()").get();
                       String content = infoHtml.xpath("//div[@class='show-content-free']/").get();
                       content = content.replace("data-original-src=\"//", "src=\"http://").replace("data-original-", "");
                       String authorHeadImg = infoHtml.xpath("//div[@class='info']/a[@class='avatar']/img/@src").get();
                       authorHeadImg = "https://" + authorHeadImg.substring(2, authorHeadImg.indexOf("?"));
                       String publishTimeStr = infoHtml.xpath("//span[@class='publish-time']/text()").get();
                       if (publishTimeStr != null) {
                           Date publishTime = DateUtils.DatePattern.PATTERN_DATE_YMD_POINT_HM.getDate(publishTimeStr);
                           appDocument.setPublishTime(publishTime);
                       }
                       appDocument.setDocId(articleId);
                       appDocument.setTitle(title);
                       appDocument.setDescri(description);
                       appDocument.setSource("www.jianshu.com");
                       appDocument.setSourceUrl(infoUrl);
                       appDocument.setAuthorName(authorName);
                       appDocument.setCoverImg(img);
                       appDocument.setCreateTime(new Date());
                       appDocument.setContent(content);
                       appDocument.setAuthorName(authorHeadImg);

                   }
               }
           }
       });
    }

}
