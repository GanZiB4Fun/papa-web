package com.ganzib.papa.doc.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GanZiB
 * Date: 2018-07-12
 * Time: 下午9:47
 * Email: ganzib4fun@gmail.com
 */
@TableName("app_novel")
public class AppNovel implements Serializable {
    private static final long serialVersionUID = 1714897111117705280L;

    @TableId
    private String novelId;

    private String url;

    private String tag;

    private Date createTime;

    private String title;

    private String descri;

    private String source;

    private String content;

    public String getNovelId() {
        return novelId;
    }

    public void setNovelId(String novelId) {
        this.novelId = novelId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescri() {
        return descri;
    }

    public void setDescri(String descri) {
        this.descri = descri;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "AppNovel{" +
                "novelId='" + novelId + '\'' +
                ", url='" + url + '\'' +
                ", tag='" + tag + '\'' +
                ", createTime=" + createTime +
                ", title='" + title + '\'' +
                ", descri='" + descri + '\'' +
                ", source='" + source + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
