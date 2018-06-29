package mybatis;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * Mybatis 抽象类
 *
 * @param <T>
 */
public interface GenericMapper<T> extends BaseMapper<T> {

    List<T> pageFindByParam(@Param("paramMap") Map<String, Object> paramMap, RowBounds rowBounds);

    void update(T t);

    Integer countByParam(@Param("paramMap") Map<String, Object> paramMap);


    int maxto();

}
