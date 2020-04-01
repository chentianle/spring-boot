package io.renren.modules.generator.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.generator.entity.WebQuestionnaireEntity;
import io.renren.modules.generator.entity.WebUserReviewEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-02-16 16:08:06
 */
@Mapper
public interface WebUserReviewDao extends BaseMapper<WebUserReviewEntity> {

    Integer queryPageByCount(Map<String, Object> params);

    List<WebUserReviewEntity> queryPageBylist(Map<String, Object> params);
}
