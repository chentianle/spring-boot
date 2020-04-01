package io.renren.modules.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.generator.dao.WebHospitalDao;
import io.renren.modules.generator.entity.WebHospitalEntity;
import io.renren.modules.generator.service.WebHospitalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service("webHospitalService")
@Transactional
public class WebHospitalServiceImpl extends ServiceImpl<WebHospitalDao, WebHospitalEntity> implements WebHospitalService {

	@Autowired
	private WebHospitalDao webHospitalDao;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        QueryWrapper<WebHospitalEntity> queryWrapper = new QueryWrapper<>();
        if(params!=null && null != params.get("hospitalName") && !params.get("hospitalName").equals("")) {
            queryWrapper.like("hospital_name", params.get("hospitalName"));
        }
        if(params!=null && null != params.get("area") && !params.get("area").equals("")) {
            queryWrapper.eq("area", params.get("area"));
        }
        if(params!=null && null != params.get("district") && !params.get("district").equals("")) {
            queryWrapper.eq("district", params.get("district"));
        }
        IPage<WebHospitalEntity> page = this.page(
                new Query<WebHospitalEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public List<WebHospitalEntity> selectByDistrict( Map<String, Object> params) {
        return baseMapper.selectByDistrict(params);
    }

    @Override
    public List<WebHospitalEntity> selectByArea(Map<String, Object> params) {
        return baseMapper.selectByArea(params);
    }

	@Override
	public List<Map<String, Object>> mapData(String area) {
		return webHospitalDao.mapData(area);
	}

}
