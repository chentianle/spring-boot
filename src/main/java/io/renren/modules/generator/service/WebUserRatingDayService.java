package io.renren.modules.generator.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

import io.renren.modules.generator.entity.WebUserRatingDayEntity;

/**
 *
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-02-16 16:08:06
 */
public interface WebUserRatingDayService extends IService<WebUserRatingDayEntity> {
	/**
	 * 发展曲线
	 * @return
	 */
	List<Map<String,Object>> progressCurve(Map<String,Object> params);
	/**
	 * 临床症状总数占比
	 * @return
	 */
	List<Map<String,Object>> symptom( Map<String,Object> params);
	
	/**
	 * 今日新增数据统计
	 * @return
	 */
	int collect(Map<String,Object> params);
}

