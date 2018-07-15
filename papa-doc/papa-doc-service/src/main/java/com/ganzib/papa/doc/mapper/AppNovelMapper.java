package com.ganzib.papa.doc.mapper;

import com.ganzib.papa.doc.model.AppNovel;
import mybatis.GenericMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GanZiB
 * Date: 2018-07-03
 * Time: 下午12:45
 * Email: ganzib4fun@gmail.com
 */
public interface AppNovelMapper extends GenericMapper<AppNovel> {

    List<AppNovel> getArticleList(@Param("paramMap") Map<String, Object> paramMap, RowBounds rowBounds);

    List<String> getTags();
}
