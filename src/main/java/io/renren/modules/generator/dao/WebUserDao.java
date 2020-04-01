package io.renren.modules.generator.dao;

import io.renren.modules.generator.entity.WebUserEntity;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-02-16 16:08:06
 */
@Mapper
public interface WebUserDao extends BaseMapper<WebUserEntity> {
	
	List<Map<String,Object>> ranking(Map<String,Object> params);
	
	List<Integer> communityState(Map<String,Object> params);
}
