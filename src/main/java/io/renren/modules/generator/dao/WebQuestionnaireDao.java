package io.renren.modules.generator.dao;

import io.renren.modules.generator.entity.WebQuestionnaireEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
public interface WebQuestionnaireDao extends BaseMapper<WebQuestionnaireEntity> {


    List<WebQuestionnaireEntity> queryPageByDaylist(Map<String, Object> params);

    Integer queryPageByDayCount(Map<String, Object> params);

    List<WebQuestionnaireEntity> queryCurrentPaientAllData(WebQuestionnaireEntity entity);

    List<WebQuestionnaireEntity> queryResentXDayData(Map<String, Object> queryMap);

    WebQuestionnaireEntity getLastQuestionnaire(Map<String, Object> queryWebQuestionnaire);

    Integer queryPageByHistoryCount(Map<String, Object> params);

    List<WebQuestionnaireEntity> queryPageByHistorylist(Map<String, Object> params);

    List<WebQuestionnaireEntity> queryDataListGroupByDate(WebQuestionnaireEntity entity);

    List<WebQuestionnaireEntity> getByUserList(@Param("userIds") List<Long> userIds,@Param("params") Map<String, Object> params);
    
    List<Map<String, Object>> constantly(Map<String, Object> params);
    
    List<Map<String, Object>> communityConstantly(Map<String, Object> params);
}
