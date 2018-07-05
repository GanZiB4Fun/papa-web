package com.ganzib.papa.doc.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GanZiB
 * Date: 2018-07-05
 * Time: 上午10:02
 * Email: ganzib4fun@gmail.com
 */
@TableName("app_jianshu_spider_author")
public class AppJianShuSpiderAuthor implements Serializable {
    private static final long serialVersionUID = -6622499441889738000L;

    @TableId
    private String spiderAuthorId;

    private String authorName;

    private String url;

    private String img;

    private Date lastCrawlTime;

    public String getSpiderAuthorId() {
        return spiderAuthorId;
    }

    public void setSpiderAuthorId(String spiderAuthorId) {
        this.spiderAuthorId = spiderAuthorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Date getLastCrawlTime() {
        return lastCrawlTime;
    }

    public void setLastCrawlTime(Date lastCrawlTime) {
        this.lastCrawlTime = lastCrawlTime;
    }

    @Override
    public String toString() {
        return "AppJianShuSpiderAuthor{" +
                "spiderAuthorId='" + spiderAuthorId + '\'' +
                ", authorName='" + authorName + '\'' +
                ", url='" + url + '\'' +
                ", img='" + img + '\'' +
                ", lastCrawlTime=" + lastCrawlTime +
                '}';
    }


    public static final class AppJianShuSpiderAuthorBuilder {
        private String spiderAuthorId;
        private String authorName;
        private String url;
        private String img;
        private Date lastCrawlTime;

        private AppJianShuSpiderAuthorBuilder() {
        }

        public static AppJianShuSpiderAuthorBuilder anAppJianShuSpiderAuthor() {
            return new AppJianShuSpiderAuthorBuilder();
        }

        public AppJianShuSpiderAuthorBuilder buildSpiderAuthorId(String spiderAuthorId) {
            this.spiderAuthorId = spiderAuthorId;
            return this;
        }

        public AppJianShuSpiderAuthorBuilder buildAuthorName(String authorName) {
            this.authorName = authorName;
            return this;
        }

        public AppJianShuSpiderAuthorBuilder buildUrl(String url) {
            this.url = url;
            return this;
        }

        public AppJianShuSpiderAuthorBuilder buildImg(String img) {
            this.img = img;
            return this;
        }

        public AppJianShuSpiderAuthorBuilder buildLastCrawlTime(Date lastCrawlTime) {
            this.lastCrawlTime = lastCrawlTime;
            return this;
        }

        public AppJianShuSpiderAuthor build() {
            AppJianShuSpiderAuthor appJianShuSpiderAuthor = new AppJianShuSpiderAuthor();
            appJianShuSpiderAuthor.setSpiderAuthorId(spiderAuthorId);
            appJianShuSpiderAuthor.setAuthorName(authorName);
            appJianShuSpiderAuthor.setUrl(url);
            appJianShuSpiderAuthor.setImg(img);
            appJianShuSpiderAuthor.setLastCrawlTime(lastCrawlTime);
            return appJianShuSpiderAuthor;
        }
    }
}
