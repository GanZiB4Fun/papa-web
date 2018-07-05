package com.ganzib.papa.doc.mapper;

import com.ganzib.papa.doc.model.AppDocument;
import mybatis.GenericMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GanZiB
 * Date: 2018-06-29
 * Time: 下午5:49
 * Email: ganzib4fun@gmail.com
 */
public interface AppDocumentMapper extends GenericMapper<AppDocument> {

    List<AppDocument> getArticleList(@Param("paramMap") Map<String, Object> paramMap, RowBounds rowBounds);


    List<AppDocument> getAllArticleList(RowBounds rowBounds);

}
