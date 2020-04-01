package io.renren.modules.generator.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.generator.entity.WebBasicPatientInformationEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 *
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-02-16 16:08:06
 */
@Mapper
public interface WebBasicPatientInformationDao extends BaseMapper<WebBasicPatientInformationEntity> {

    public WebBasicPatientInformationEntity queryGeneration(Map<String, Object> params);

    public WebBasicPatientInformationEntity queryTriggeringConditions(Map<String,Object> params);

}
