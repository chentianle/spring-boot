package io.renren.modules.generator.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.renren.modules.generator.dao.WebUserRatingDayDao;
import io.renren.modules.generator.entity.WebUserRatingDayEntity;
import io.renren.modules.generator.service.WebUserRatingDayService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("webUserRatingDayService")
@Transactional
public class WebUserRatingDayServiceImpl
        extends ServiceImpl<WebUserRatingDayDao, WebUserRatingDayEntity>
        implements WebUserRatingDayService {

	@Autowired
	private WebUserRatingDayDao webUserRatingDayDao;

	@Override
	public List<Map<String, Object>> progressCurve(Map<String,Object> params) {
		return webUserRatingDayDao.progressCurve(params);
	}

	@Override
	public List<Map<String, Object>> symptom( Map<String,Object> params) {
		List<Map<String, Object>> listmap = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = webUserRatingDayDao.symptom(params);

		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("name", "发烧");
		map1.put("value", map.get("mentalState"));

		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("name", "精神状态差");
		map2.put("value", map.get("fatigue"));

		Map<String, Object> map3 = new HashMap<String, Object>();
		map3.put("name", "肌肉酸痛");
		map3.put("value", map.get("muscleAche"));

		Map<String, Object> map4 = new HashMap<String, Object>();
		map4.put("name", "咳嗽");
		map4.put("value", map.get("fever"));

		Map<String, Object> map5 = new HashMap<String, Object>();
		map5.put("name", "呼吸困难");
		map5.put("value", map.get("cough"));

		Map<String, Object> map6 = new HashMap<String, Object>();
		map6.put("name", "乏力");
		map6.put("value", map.get("diarrhea"));

		Map<String, Object> map7 = new HashMap<String, Object>();
		map7.put("name", "腹泻");
		map7.put("value", map.get("difficultyBreathing"));

		listmap.add(map1);
		listmap.add(map2);
		listmap.add(map3);
		listmap.add(map4);
		listmap.add(map5);
		listmap.add(map6);
		listmap.add(map7);

		return listmap;
	}

	@Override
	public int collect( Map<String,Object> params) {
		return webUserRatingDayDao.collect(params);
	}

}
