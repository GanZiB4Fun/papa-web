package com.ganzib.papa.sprider.service;

import com.ganzib.papa.doc.model.AppDocument;
import com.ganzib.papa.doc.model.AppNovel;
import com.ganzib.papa.doc.service.IAppDocumentService;
import com.ganzib.papa.doc.service.IAppJianShuSpiderAuthorService;
import com.ganzib.papa.doc.service.IAppNovelService;
import com.ganzib.papa.sprider.constant.SHA1;
import com.ganzib.papa.sprider.constant.WebConstant;
import com.ganzib.papa.sprider.thread.SpriderThreadPool;
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
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GanZiB
 * Date: 2018-07-12
 * Time: 下午1:29
 * Email: ganzib4fun@gmail.com
 */
@Service
public class XiaoShuoSpider {

    private Logger logger = Logger.getLogger(XiaoShuoSpider.class);

    @Autowired
    private IAppNovelService appNovelService;
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
                logger.info("qichezhijia has started");
                boolean isServiceStart = false;
                do {
                    try {
                        isServiceStart = appNovelService.isServiceStart();
                        startFlag = true;
                    } catch (Exception e) {
                        logger.info("qichezhijia is not found,please wait");
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


    /*@Scheduled(cron = "0 12 23 ? * *")*/
    /*@Scheduled(cron = "0/1 * * * * ? ")*/
    @Scheduled(cron = "0 20 23 * * ?")
    public void spider() {
        logger.info("jian shu spider task start");
        if (!startFlag) {
            return;
        }
        List<String> categoryList = new ArrayList<>();
        categoryList.add(URLEncoder.encode("凌辱强暴"));
        categoryList.add(URLEncoder.encode("都市激情"));
        categoryList.add(URLEncoder.encode("校园师生"));
        categoryList.add(URLEncoder.encode("群P交换"));
        categoryList.add(URLEncoder.encode("职业制服"));
        categoryList.add(URLEncoder.encode("同志小说"));
        categoryList.add(URLEncoder.encode("近亲乱伦"));
        for (String category : categoryList) {
            Page page = null;
            URLEncoder.encode("");
            String url = "http://www.4438xx10.com/xiaoshuo/archives/category/" + category + "/page/{PAGE}";
            int i = 0;
            do {
                String listUrl = url.replace("{PAGE}", String.valueOf(i));
                System.out.println(listUrl);
                try {
                    page = new HttpClientDownloader().download(new Request(listUrl), site.toTask());
                } catch (Exception e) {
                    logger.info("GET LOSE");
                }
                if (page != null) {
                    if (page.getStatusCode() == 404) break;
                    List<String> divList = page.getHtml().xpath("//main[@id='main']/article/div[@class='post-content']").all();
                    for (String divStr : divList) {
                        try {
                            ansySpider(divStr, URLDecoder.decode(category));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                i++;
            } while (i <= 500);
        }

    }

    public void ansySpider(String divStr, String tag) throws Exception {
        spriderThreadPool.addTask(new Runnable() {
            @Override
            public void run() {
                Html html = new Html(divStr);
                String url = html.xpath("//h2[@class='entry-title']/a/@href").get();
                String title = html.xpath("//h2[@class='entry-title']/a/text()").get();
                String descri = html.xpath("//div[@class='entry-content']/p/text()").get();
                String content = "";
                String docId = SHA1.encode(url);
                if (appNovelService.selectById(docId) != null) {
                    return;
                }
                AppNovel appNovel = new AppNovel();
                Page page = null;
                try {
                    page = new HttpClientDownloader().download(new Request(url), site.toTask());
                } catch (Exception e) {
                    logger.info("GET LOSE");
                }
                if (page != null) {
                    content = page.getHtml().xpath("//div[@class='entry-content']").get();
                    appNovel.setNovelId(docId);
                    appNovel.setCreateTime(new Date());
                    appNovel.setUrl(url);
                    appNovel.setTag(tag);
                    appNovel.setTitle(EmojiParser.parseToAliases(title));
                    appNovel.setDescri(EmojiParser.parseToAliases(descri));
                    appNovel.setSource("www.4438xx10.com");
                    appNovel.setContent(EmojiParser.parseToAliases(content));
                    try {
                        appNovelService.insert(appNovel);
                    } catch (DuplicateKeyException e) {
                        logger.info("Article :《" + appNovel.getTitle() + "》 has crawled");
                    } catch (Exception e) {
                        logger.error("save error", e);
                    }
                }
            }
        });
    }
}
