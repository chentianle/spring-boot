package io.renren.modules.generator.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.modules.generator.dao.WebBasicPatientInformationDao;
import io.renren.modules.generator.dao.WebQuestionnaireDao;
import io.renren.modules.generator.dao.WebUserReviewDao;
import io.renren.modules.generator.entity.WebBasicPatientInformationEntity;
import io.renren.modules.generator.entity.WebQuestionnaireEntity;
import io.renren.modules.generator.entity.WebUserReviewEntity;
import io.renren.modules.generator.service.WebUserReviewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("webUserReviewService")
@Transactional
public class WebUserReviewServiceImpl
        extends ServiceImpl<WebUserReviewDao, WebUserReviewEntity>
        implements WebUserReviewService {

    @Resource
    private WebUserReviewDao webUserReviewDao;

    @Resource
    private WebQuestionnaireDao webQuestionnaireDao;

    @Resource
    private WebBasicPatientInformationDao webBasicPatientInformationDao;

    @Override
    public PageUtils queryPagelist(Map<String, Object> params) {
        List<WebUserReviewEntity> list = new ArrayList<>();
        Integer  count = webUserReviewDao.queryPageByCount(params);
        if(count!=null && count>0) {
            //分页计算
            PageUtils.pageCalculation(params);
            list = webUserReviewDao.queryPageBylist(params);
        }
        return new PageUtils(list,count,Integer.parseInt(params.get("limit")+""),Integer.parseInt(params.get("page")+""));
    }

    @Override
    public Map<String, Object> allInfoByUserid(Long id) {
        Map<String,Object> resultMap = new HashMap<String,Object>();
        WebQuestionnaireEntity bean = webQuestionnaireDao.selectById(id);
        resultMap.put("webQuestionnaire", bean);
        WebBasicPatientInformationEntity   webBasic = webBasicPatientInformationDao.selectOne(new QueryWrapper<WebBasicPatientInformationEntity>().eq("user_id",bean.getUserId()));
        resultMap.put("webBasic",webBasic);
        return resultMap;
    }

}

