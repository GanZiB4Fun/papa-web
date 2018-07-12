package com.ganzib.papa.sprider.service;

import com.ganzib.papa.doc.model.AppDocument;
import com.ganzib.papa.doc.model.AppJianShuSpiderAuthor;
import com.ganzib.papa.doc.service.IAppDocumentService;
import com.ganzib.papa.doc.service.IAppJianShuSpiderAuthorService;
import com.ganzib.papa.sprider.constant.SHA1;
import com.ganzib.papa.sprider.constant.WebConstant;
import com.ganzib.papa.sprider.thread.SpriderThreadPool;
import com.ganzib.papa.support.date.DateUtils;
import com.ganzib.papa.support.util.Pager;
import com.vdurmont.emoji.EmojiParser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.selector.Html;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
    private IAppJianShuSpiderAuthorService appJianShuSpiderAuthorService;
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

    @PostConstruct
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
    }

//    @Scheduled(cron = "0 0/10 * * * ?")
@Scheduled(cron = "0 0 1 * * ?")
    public void spider() {
        logger.info("jian shu spider task start");
        if (!startFlag) {
            return;
        }
        Page page = null;
        String startUrl = "https://www.jianshu.com/recommendations/collections?utm_medium=index-collections&utm_source=desktop";
        try {
            page = new HttpClientDownloader().download(new Request(startUrl), site.toTask());
        } catch (Exception e) {
            logger.error("Http connect error ", e);
        }
        if (page != null) {
            Html html = page.getHtml();
            List<String> divList = html.xpath("//div[@id='list-container']/div[@class='col-xs-8']").all();
            for (String divStr : divList) {
                Html divHtml = new Html(divStr);
                String url = divHtml.xpath("//div[@class='collection-wrap']/a/@href").get();
                String tag = divHtml.xpath("//div[@class='collection-wrap']/a/h4[@class='name']/text()").get();
                boolean runFlag = true;
                int i = 1;
                do {
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
                        if (articleDivList==null||articleDivList.size()==0){
                            runFlag = false;
                        }
                        for (String articleDiv : articleDivList) {
                            try {
                                ansySpider(articleDiv, tag);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    i++;
                }while (runFlag && i<=1000);
            }
        }

    }

    public void ansySpider(String divStr, String tag) throws Exception{
        spriderThreadPool.addTask(new Runnable() {
            @Override
            public void run() {
                Html html = new Html(divStr);
                String img = html.xpath("//a[@class='wrap-img']/img/@src").get();
                if (img != null) {
                    img = "http://" + img.substring(2, img.indexOf("?"));
                }
                String infoUrl = html.xpath("//div[@class='content']/a[@class='title']/@href").get();
                saveArticle(infoUrl,img,tag);
            }
        });
    }

    public void saveArticle(String infoUrl,String img,String tag){
        AppDocument appDocument = new AppDocument();
        if (infoUrl != null) {
            infoUrl = "https://www.jianshu.com" + infoUrl;
            String articleId = SHA1.encode(infoUrl);
            if (appDocumentService.selectById(articleId) != null) {
                return;
            }

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
                String content = infoHtml.xpath("//div[@class='show-content-free']").get();
                content = content.replace("data-original-src=\"//", "src=\"http://").replace("data-original-", "");
                String authorHeadImg = infoHtml.xpath("//div[@class='info']/a[@class='avatar']/img/@src").get();
                if (authorHeadImg != null) {
                    authorHeadImg = "https://" + authorHeadImg.substring(2, authorHeadImg.indexOf("?"));
                }else {
                    authorHeadImg = "http://www.zhaotouxiang.cn/uploads/allimg/170303/1-1F303104930.jpg";
                }
                String authorUrl = infoHtml.xpath("//a[@class='avatar']/@href").get();
                if (authorUrl != null) {
                    authorUrl = "https://www.jianshu.com" + authorUrl;
                    String encode = SHA1.encode(authorUrl);
                    if (appJianShuSpiderAuthorService.selectById(encode) == null) {
                        AppJianShuSpiderAuthor appJianShuSpiderAuthor = AppJianShuSpiderAuthor
                                .AppJianShuSpiderAuthorBuilder.anAppJianShuSpiderAuthor().buildUrl(authorUrl)
                                .buildSpiderAuthorId(encode).buildImg(authorHeadImg).buildAuthorName(authorName).build();
                        try {
                            appJianShuSpiderAuthorService.insert(appJianShuSpiderAuthor);
                        } catch (DuplicateKeyException e) {
                            logger.info("Author : " + authorName + " has  collected.");
                        }
                    }

                }
                String publishTimeStr = infoHtml.xpath("//span[@class='publish-time']/text()").get();
                appDocument.setPublishTime(new Date());
                if (publishTimeStr != null) {
                    try {
                        publishTimeStr = publishTimeStr.replace("*","");
                        Date publishTime = DateUtils.DatePattern.PATTERN_DATE_YMD_POINT_HM.getDate(publishTimeStr);
                        appDocument.setPublishTime(publishTime);
                    } catch (Exception e) {
                        logger.error("error publish time can't parse");
                    }
                }
                if (img==null){
                    Html html = new Html(content);
                    img = html.xpath("//img/@src").get();
                }
                appDocument.setDocId(articleId);
                appDocument.setTitle(EmojiParser.parseToAliases(title));
                appDocument.setDescri(EmojiParser.parseToAliases(description));
                appDocument.setSource("www.jianshu.com");
                appDocument.setSourceUrl(infoUrl);
                appDocument.setAuthorHeadImg(authorHeadImg);
                appDocument.setCoverImg(img);
                appDocument.setCreateTime(new Date());
                appDocument.setContent(EmojiParser.parseToAliases(content));
                appDocument.setAuthorName(authorName);
                appDocument.setTags(tag);
                try {
                    appDocumentService.insert(appDocument);
                } catch (DuplicateKeyException e) {
                    logger.info("Article :《" + appDocument.getTitle() + "》 has crawled");
                }
            }
        }
    }
}
