package io.renren.modules.generator.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import io.renren.modules.generator.entity.WebCommunityHelpEntity;
import io.renren.modules.generator.entity.WebQuestionnaireEntity;
import io.renren.modules.generator.entity.WebUserEntity;

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
public interface WebCommunityHelpDao extends BaseMapper<WebCommunityHelpEntity> {


    Integer queryPageByCount(Map<String, Object> params);

    List<WebQuestionnaireEntity> queryPageBylist(Map<String, Object> params);

    List<Map<String, Object>> constantly(Map<String, Object> params);

    List<Map<String, Object>> communityConstantly(Map<String, Object> params);

    WebCommunityHelpEntity queryById(String id);
}
