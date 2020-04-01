package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;

import io.renren.common.utils.PageUtils;
import io.renren.modules.generator.entity.WebHospitalEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-02-16 16:08:06
 */
public interface WebHospitalService extends IService<WebHospitalEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<WebHospitalEntity> selectByDistrict(Map<String, Object> params);

    List<WebHospitalEntity> selectByArea(Map<String, Object> params);
    
    List<Map<String,Object>> mapData(String area);
}

