package com.ganzib.papa.doc.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GanZiB
 * Date: 2018-07-02
 * Time: 上午10:51
 * Email: ganzib4fun@gmail.com
 */
@TableName("app_document")
public class AppDocument implements Serializable {
    private static final long serialVersionUID = -6873550646491700543L;

    @TableId
    private String docId;

    private String title;

    private String content;

    private Date createTime;

    private String authorId;

    private String tags;

    private String descri;

    private String source;

    private String sourceUrl;

    private String coverImg;

    private Date publishTime;

    private String authorName;

    private String authorHeadImg;

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
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

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorHeadImg() {
        return authorHeadImg;
    }

    public void setAuthorHeadImg(String authorHeadImg) {
        this.authorHeadImg = authorHeadImg;
    }

    @Override
    public String toString() {
        return "AppDocument{" +
                "docId='" + docId + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", authorId='" + authorId + '\'' +
                ", tags='" + tags + '\'' +
                ", descri='" + descri + '\'' +
                ", source='" + source + '\'' +
                ", sourceUrl='" + sourceUrl + '\'' +
                ", coverImg='" + coverImg + '\'' +
                ", publishTime=" + publishTime +
                ", authorName='" + authorName + '\'' +
                ", authorHeadImg='" + authorHeadImg + '\'' +
                '}';
    }
}
