package io.renren.modules.employment.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.renren.modules.employment.entity.Employment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmploymentDao extends BaseMapper<Employment> {
}
