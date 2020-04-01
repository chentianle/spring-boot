package io.renren.modules.generator.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.generator.entity.WebCommunityFeedbackEntity;
import io.renren.modules.generator.entity.WebCommunityHelpEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-02-16 16:08:06
 */
@Mapper
public interface WebCommunityFeedbackDao extends BaseMapper<WebCommunityFeedbackEntity> {


    @Select("select wcf.*,su.username as userName from web_community_feedback wcf , sys_user su where wcf.user_id = su.user_id and community_help_id = #{id}")
    List<WebCommunityFeedbackEntity> getByHelpId(Long id);
}
