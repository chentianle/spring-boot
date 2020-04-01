package io.renren.modules.generator.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import io.renren.modules.generator.entity.WebHospitalEntity;

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
public interface WebHospitalDao extends BaseMapper<WebHospitalEntity> {

    List<WebHospitalEntity> selectByDistrict(Map<String, Object> params);

    List<WebHospitalEntity> selectByArea(Map<String, Object> params);
    
    List<Map<String,Object>> mapData(String area);
}
