package io.renren.modules.generator.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.DateUtils;
import io.renren.modules.generator.dao.*;
import io.renren.modules.generator.entity.*;
import io.renren.modules.generator.service.WebQuestionnaireDetailService;
import io.renren.modules.generator.service.WebUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service("webQuestionnaireDetail")
@Transactional
public class WebQuestionnaireDetailImpl
        extends ServiceImpl<WebQuestionnaireDetailDao, WebQuestionnaireDetailEntity>
        implements WebQuestionnaireDetailService {

    private final static Logger log = LoggerFactory.getLogger(WebQuestionnaireDetailImpl.class);



    @Autowired
    private WebUserService webUserService;

    @Autowired
    private WebQuestionnaireDao webQuestionnaireDao;

    @Autowired
    private WebQuestionnaireDetailDao webQuestionnaireDetailDao;

    @Autowired
    private WebBasicPatientInformationDao webBasicPatientInformationDao;

    @Autowired
    private WebDoctorCommentDao webDoctorCommentDao;


    /**
     * Admin 获取诊断详情
     * @param type  1：每日的情况 id为用户ID ；2：历史每条结果的详情 id为答案表的详情
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> allInfoByUserid(String type, Long id) {
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("webQuestionnaireId","");
        resultMap.put("webQuestionnaire","");

        WebQuestionnaireEntity webQuestionnaire = new WebQuestionnaireEntity();
        //判断type
        if(type.equals("1")){
            //获取用户信息
            WebUserEntity webUser = webUserService.getById(id);
            Map<String,Object> webUserMap = new HashMap<String,Object>();
            webUserMap.put("username",webUser.getUsername());
            webUserMap.put("mobile",webUser.getMobile());
            webUserMap.put("rating",webUser.getRating());
            //填报信息
            Map<String,Object> queryWebQuestionnaire = new HashMap<>();
            queryWebQuestionnaire.put("userId",id);
            queryWebQuestionnaire.put("subTime", DateUtils.format(new Date(),DateUtils.DATE_PATTERN));
            webQuestionnaire = webQuestionnaireDao.getLastQuestionnaire(queryWebQuestionnaire);
            if(webQuestionnaire!=null){
                webUserMap.put("subTime",webQuestionnaire.getSubTime());
                webUserMap.put("score",webQuestionnaire.getScore());
                webUserMap.put("handleStatus",webQuestionnaire.getHandleStatus());
                webUserMap.put("verbStatus",webQuestionnaire.getVerbStatus());
                resultMap.put("webQuestionnaireId",webQuestionnaire.getId());
            }else{
                webUserMap.put("subTime","未填报");
            }
            resultMap.put("webUser",webUserMap);
        }else{
            resultMap.put("webQuestionnaireId",id);
            Map<String,Object> webUserMap = new HashMap<String,Object>();
            //填报信息
            webQuestionnaire = webQuestionnaireDao.selectById(id);
            if(webQuestionnaire!=null){
                webUserMap.put("subTime",webQuestionnaire.getSubTime());
                webUserMap.put("score",webQuestionnaire.getScore());
                webUserMap.put("handleStatus",webQuestionnaire.getHandleStatus());
                webUserMap.put("verbStatus",webQuestionnaire.getVerbStatus());
            }
            //获取用户信息
            WebUserEntity webUser = webUserService.getById(webQuestionnaire.getUserId());
            webUserMap.put("username",webUser.getUsername());
            webUserMap.put("mobile",webUser.getMobile());
            webUserMap.put("rating",webUser.getRating());
            resultMap.put("webUser",webUserMap);
        }

        List<WebQuestionnaireDetailEntity> detailsList = new ArrayList<>();
        List<WebDoctorCommentEntity> detaiDoctorList = new ArrayList<>();

        //诊断结果  & 医生建议 & 复查结果 & 临床诊断结果
        if(webQuestionnaire!=null && webQuestionnaire.getId()!=null){
            QueryWrapper<WebQuestionnaireDetailEntity> qw = new QueryWrapper<>();
            qw.eq("web_questionnaire_id",webQuestionnaire.getId());
            qw.orderByDesc("level");
            detailsList = webQuestionnaireDetailDao.selectList(qw);

            QueryWrapper<WebDoctorCommentEntity> qwDc = new QueryWrapper<>();
            qwDc.eq("web_questionnaire_id",webQuestionnaire.getId());
            qwDc.orderByDesc("create_time");
            detaiDoctorList = webDoctorCommentDao.selectList(qwDc);
            resultMap.put("webQuestionnaire",webQuestionnaire);

        }
        resultMap.put("webDetail",detailsList);
        resultMap.put("detaiDoctorList",detaiDoctorList);
        //患者基础情况
        WebBasicPatientInformationEntity webBasic = new WebBasicPatientInformationEntity();
        if(type.equals("1")) {
            webBasic = webBasicPatientInformationDao.selectOne(new QueryWrapper<WebBasicPatientInformationEntity>().eq("user_id",id));
        }else{
            webBasic = webBasicPatientInformationDao.selectOne(new QueryWrapper<WebBasicPatientInformationEntity>().eq("user_id",webQuestionnaire.getUserId()));
        }
        resultMap.put("webBasic",webBasic);

        return resultMap;
    }

}
