package io.renren.modules.generator.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import io.renren.modules.generator.entity.WebUserRatingDayEntity;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 *
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-02-16 16:08:06
 */
@Mapper
public interface WebUserRatingDayDao extends BaseMapper<WebUserRatingDayEntity> {

	/**
	 * 发展曲线
	 * @return
	 */
	List<Map<String,Object>> progressCurve(Map<String,Object> params);
	/**
	 * 临床症状总数占比
	 * @return
	 */
	Map<String,Object> symptom(Map<String,Object> params);
	
	/**
	 * 今日新增数据统计
	 * @return
	 */
	int collect(Map<String, Object> params);
	
}
