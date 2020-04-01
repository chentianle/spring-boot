package io.renren.modules.generator.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.renren.common.exception.RRException;
import io.renren.common.utils.*;
import io.renren.modules.app.EnumPack.*;
import io.renren.modules.app.formula.FormulaList;
import io.renren.modules.app.utils.Constant;
import io.renren.modules.generator.dao.*;
import io.renren.modules.generator.entity.*;
import io.renren.modules.generator.service.WebDoctorCommentService;
import io.renren.modules.generator.service.WebQuestionnaireDetailService;
import io.renren.modules.generator.service.WebQuestionnaireService;
import io.renren.modules.generator.service.WebUserReviewService;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;


@Service("webQuestionnaireService")
@Transactional
public class WebQuestionnaireServiceImpl extends ServiceImpl<WebQuestionnaireDao, WebQuestionnaireEntity> implements WebQuestionnaireService {

    private final static Logger log = LoggerFactory.getLogger(WebQuestionnaireServiceImpl.class);

    @Resource
    private WebUserDao webUserDao;

    @Resource
    private WebQuestionnaireDao webQuestionnaireDao;

    @Resource
    private WebBasicPatientInformationDao patientInformationDao;

    @Autowired
    private WebQuestionnaireDetailService webQuestionnaireDetailService;

    @Autowired
    private WebDoctorCommentService webDoctorCommentService;

    @Resource
    private WebUserReviewService webUserReviewService;

    @Resource
    private WebUserDayorderbyDao webUserDayorderbyDao;

    @Resource
    private WebUserRatingDayDao webUserRatingDayDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WebQuestionnaireEntity> page = this.page(
                new Query<WebQuestionnaireEntity>().getPage(params),
                new QueryWrapper<WebQuestionnaireEntity>()
        );
        return new PageUtils(page);
    }

    /**
     * 获取患者每日提报信息
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPageByDaylist(Map<String, Object> params) {
        List<WebQuestionnaireEntity> list = new ArrayList<>();
        Integer  count = baseMapper.queryPageByDayCount(params);
        if(count!=null && count>0) {
            //分页计算
            PageUtils.pageCalculation(params);
            list = baseMapper.queryPageByDaylist(params);
        }
        return new PageUtils(list,count,Integer.parseInt(params.get("limit")+""),Integer.parseInt(params.get("page")+""));
    }



    /**
     * 获取患者历史提报信息
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPageByHistorylist(Map<String, Object> params) {
        List<WebQuestionnaireEntity> list = new ArrayList<>();
        Integer  count = baseMapper.queryPageByHistoryCount(params);
        if(count!=null && count>0) {
            //分页计算
            //分页计算
            PageUtils.pageCalculation(params);
            list = baseMapper.queryPageByHistorylist(params);
        }
        return new PageUtils(list,count,Integer.parseInt(params.get("limit")+""),Integer.parseInt(params.get("page")+""));
    }


    @Override
    public void exportExcel(@RequestBody Map<String, Object> params, HttpServletResponse response) throws IOException {
        List<WebQuestionnaireEntity> list = baseMapper.queryPageByHistorylist(params);
        MultiSheetExcelUtil newInstace = MultiSheetExcelUtil.newInstace();
        List<String> title = Lists.newArrayList();
        title.add("序号");
        title.add("社区名称");
        title.add("用户姓名");
        title.add("手机号");
        title.add("处理状态");
        title.add("提交时间");
        title.add("得分");
        title.add("用户概要情况");
        title.add("诊断建议");
        //每日临床检查
        title.add(Constant.bodyTemperature);
        title.add(Constant.mentalState);
        title.add(Constant.muscleAche);
        title.add(Constant.cough);
        title.add(Constant.difficultyBreathing);
        title.add(Constant.fatigue);
        title.add(Constant.diarrhea);
        title.add(Constant.asymptomaticCloseAtHome);
        title.add(Constant.ReviewDiagnosis);
//        title.add(Constant.NucleicAcidDetection+"(临床观察)");
        //复诊情况
        title.add(Constant.lungCTResult);
        title.add(Constant.lungCTResultDate);
        title.add(Constant.cFydb);
        title.add(Constant.pct);
        title.add(Constant.NucleicAcidDetection+"(临床诊断)");
        title.add(Constant.whiteCell);
        title.add(Constant.eatCell);
        title.add(Constant.lbCell);
        List<String> fields = Lists.newArrayList();
        fields.add("number");
        fields.add("hospitalName");
        fields.add("username");
        fields.add("mobile");
        fields.add("handleStatusStr");
        fields.add("subTime");
        fields.add("score");
        fields.add("nounValue");
        fields.add("diagnosisValue");
        //每日临床检查
        fields.add("bodyTemperatureStr");
        fields.add("mentalStateStr");
        fields.add("muscleAcheStr");
        fields.add("coughStr");
        fields.add("difficultyBreathingStr");
        fields.add("fatigueStr");
        fields.add("diarrheaStr");
        fields.add("asymptomaticCloseAtHome");
        fields.add("reviewDiagnosisStr");
//        fields.add("nucleicAcidDetectionCO");
        //复诊情况
        fields.add("lungCTResult");
        fields.add("lungCTResultDate");
        fields.add("cFydb");
        fields.add("pct");
        fields.add("nucleicAcidDetection");
        fields.add("whiteCell");
        fields.add("eatCell");
        fields.add("lbCell");
        for(int i = 0; i < list.size() ; i++){
            WebQuestionnaireEntity wqBean = list.get(i);
            wqBean.setNumber(i+1);
            if(null != wqBean.getHandleStatus()){
                if(HandleStatusEnum.unhanle.getValue() == wqBean.getHandleStatus()){
                    wqBean.setHandleStatusStr("未处理");
                }else if(HandleStatusEnum.handled.getValue() == wqBean.getHandleStatus()){
                    wqBean.setHandleStatusStr("已处理");
                }else{
                    wqBean.setHandleStatusStr("");
                }
            }
            prepareExportBaseData(wqBean);

        }
        newInstace.addSheet(list, title, fields, "患者信息查询导出");
        HSSFWorkbook wb = newInstace.getWorkBook();
        OutputStream output = response.getOutputStream();
//        response.reset();
//        response.addHeader("Access-Control-Allow-Credentials","true");
//        response.addHeader("Access-Control-Allow-Origin", "http://managetest.diagnosis.suntrayoa.com/");
        response.setHeader("Content-disposition", "attachment; filename=details.xls");
        response.setContentType("application/msexcel");
        wb.write(output);
        output.close();
    }

    private void prepareExportBaseData(WebQuestionnaireEntity entity) {
        String subContent = entity.getSubContent();//问卷结果
        if(StringUtils.isBlank(subContent)){
            return;
        }
        try {
            JSONArray parse = (JSONArray) JSON.parse(entity.getSubContent());
            for (Object o : parse) {
                Map<String,Object> map = (Map<String,Object>)o;
                if(map.containsKey(Constant.content)){
                    String tmpKey = String.valueOf(map.get(Constant.content));
                    //体温分数
                    if(Constant.bodyTemperature.equals(tmpKey)){
                        if(map.containsKey(Constant.value)){
                            String bodyTemperatureStr = String.valueOf(map.get(Constant.value));
                            entity.setBodyTemperatureStr(bodyTemperatureStr);
                        }else{
                            entity.setBodyTemperatureStr(null);
                        }
                    }
                    //咳嗽分数
                    if(Constant.cough.equals(tmpKey)){
                        if(map.containsKey(Constant.value)){
                            String coughStr = String.valueOf(map.get(Constant.value));
                            entity.setCoughStr(coughStr);
                        }else{
                            entity.setCoughStr(null);
                        }
                    }
                    //腹泻分数
                    if(Constant.diarrhea.equals(tmpKey)){
                        if(map.containsKey(Constant.value)){
                            String diarrheaStr = String.valueOf(map.get(Constant.value));
                            entity.setDiarrheaStr(diarrheaStr);
                        }else{
                            entity.setDiarrheaStr(null);
                        }
                    }

                    //呼吸困难的分数
                    if(Constant.difficultyBreathing.equals(tmpKey)){
                        if(map.containsKey(Constant.value)){
                            String tmpDifficultyBreathingStr = String.valueOf(map.get(Constant.value));
                            entity.setDifficultyBreathingStr(tmpDifficultyBreathingStr);
                        }else{
                            entity.setDifficultyBreathingStr(null);
                        }
                    }

                    //乏力分值
                    if(Constant.fatigue.equals(tmpKey)){
                        if(map.containsKey(Constant.value)){
                            String fatigueStr = String.valueOf(map.get(Constant.value));
                            entity.setFatigueStr(fatigueStr);
                        }else{
                            entity.setFatigueStr(null);
                        }
                    }
                    //精神状态分值
                    if(Constant.mentalState.equals(tmpKey)){
                        if(map.containsKey(Constant.value)){
                            String tmpMentalStateStr = String.valueOf(map.get(Constant.value));
                            entity.setMentalStateStr(tmpMentalStateStr);
                        }else{
                            entity.setMentalStateStr(null);
                        }
                    }
                    //肌肉酸痛分数
                    if(Constant.muscleAche.equals(tmpKey)){
                        if(map.containsKey(Constant.value)){
                            String muscleAcheStr = String.valueOf(map.get(Constant.value));
                            entity.setMuscleAcheStr(muscleAcheStr);
                        }else{
                            entity.setMuscleAcheStr(null);
                        }
                    }
                    //今日是否有复诊检查
                    if(Constant.ReviewDiagnosis.equals(tmpKey)){
                        if(map.containsKey(Constant.value)){
                            String tmpValue = String.valueOf(map.get(Constant.value));
                            entity.setReviewDiagnosisStr(tmpValue);
                        }else{
                            entity.setReviewDiagnosisStr(null);
                        }
                    }
                    //存储今日胸部CT结果
                    if(Constant.lungCTResult.equals(tmpKey)){
                        if(map.containsKey(Constant.value)){
                            String tmpValue = String.valueOf(map.get(Constant.value));
                            entity.setLungCTResult(tmpValue);
                        }else{
                            entity.setLungCTResult(null);
                        }
                    }
                    //今日胸部CT 填写的对应日期
                    if(Constant.lungCTResultDate.equals(tmpKey)){
                        if(map.containsKey(Constant.value)){
                            String tmpValue = String.valueOf(map.get(Constant.value));
                            entity.setLungCTResultDate(tmpValue);
                        }else{
                            entity.setLungCTResultDate(null);
                        }
                    }
                    //存储今天核酸检测的结果
                    if(Constant.NucleicAcidDetection.equals(tmpKey)){
                        // 加入当前问题是复查类型的判断
                        Boolean tmpTypeFlag = false;
                        if(map.containsKey(Constant.type)){
                            Integer tmpType = Integer.valueOf(String.valueOf(map.get(Constant.type)));
                            if(QuestionTypeEnum.reCheck.getValue().intValue() == tmpType.intValue()){
                                tmpTypeFlag = true;
                            }
                        }
                        if(tmpTypeFlag && map.containsKey(Constant.value)){
                            String tmpValue = String.valueOf(map.get(Constant.value));
                            if(StringUtils.isNotBlank(tmpValue)
                                    && !tmpValue.equals(NucleicAcidDetectionEnum.uncheck.getValue())){
                                entity.setNucleicAcidDetection(tmpValue);
                            }
                        }else{
                            entity.setNucleicAcidDetection(null);
                        }
                    }

                    //存储今天核酸检测的结果
                    /*if(Constant.NucleicAcidDetection.equals(tmpKey)){
                        // 加入当前问题是复查类型的判断
                        Boolean tmpTypeFlag = false;
                        if(map.containsKey(Constant.type)){
                            Integer tmpType = Integer.valueOf(String.valueOf(map.get(Constant.type)));
                            if(QuestionTypeEnum.dayCheck.getValue().intValue() == tmpType.intValue()){
                                tmpTypeFlag = true;
                            }
                        }
                        if(tmpTypeFlag && map.containsKey(Constant.value)){
                            String tmpValue = String.valueOf(map.get(Constant.value));
                            if(StringUtils.isNotBlank(tmpValue)
                                    && !tmpValue.equals(NucleicAcidDetectionEnum.uncheck.getValue())){
                                entity.setNucleicAcidDetectionCO(tmpValue);
                            }
                        }else{
                            entity.setNucleicAcidDetectionCO(null);
                        }
                    }*/

                    //家中密切接触者有无症状
                    if(Constant.asymptomaticCloseAtHome.equals(tmpKey)){
                        if(map.containsKey(Constant.value)){
                            String tmpValue = String.valueOf(map.get(Constant.value));
                            entity.setAsymptomaticCloseAtHome(tmpValue);
                        }else{
                            entity.setAsymptomaticCloseAtHome(null);
                        }
                    }
                    //C反应蛋白
                    if(Constant.cFydb.equals(tmpKey)){
                        if(map.containsKey(Constant.value)){
                            String tmpValue = String.valueOf(map.get(Constant.value));
                            entity.setCFydb(tmpValue);
                        }else{
                            entity.setCFydb(null);
                        }
                    }
                    //PCT
                    if(Constant.pct.equals(tmpKey)){
                        if(map.containsKey(Constant.value)){
                            String tmpValue = String.valueOf(map.get(Constant.value));
                            entity.setPct(tmpValue);
                        }else{
                            entity.setPct(null);
                        }
                    }
                    //白细胞
                    if(Constant.whiteCell.equals(tmpKey)){
                        if(map.containsKey(Constant.value)){
                            String tmpValue = String.valueOf(map.get(Constant.value));
                            entity.setWhiteCell(tmpValue);
                        }else{
                            entity.setWhiteCell(null);
                        }
                    }
                    //嗜酸性粒细胞
                    if(Constant.eatCell.equals(tmpKey)){
                        if(map.containsKey(Constant.value)){
                            String tmpValue = String.valueOf(map.get(Constant.value));
                            entity.setEatCell(tmpValue);
                        }else{
                            entity.setEatCell(null);
                        }
                    }
                    //淋巴细胞
                    if(Constant.lbCell.equals(tmpKey)){
                        if(map.containsKey(Constant.value)){
                            String tmpValue = String.valueOf(map.get(Constant.value));
                            entity.setLbCell(tmpValue);
                        }else{
                            entity.setLbCell(null);
                        }
                    }
                }
            }
        }catch( Exception e){
            log.error("准备导出数据依赖基础数据时报错，传递的jsonStr:[{}],错误信息:[{}]",entity.getSubContent(),e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public R queryInfo(Long id) {
        WebQuestionnaireEntity webQuestionnaire = this.getById(id);
        if(null != webQuestionnaire && null != webQuestionnaire.getUserId()){
            //组织问题明细表记录
            LambdaQueryWrapper<WebQuestionnaireDetailEntity> lqDetail = Wrappers.lambdaQuery();
            lqDetail.eq(WebQuestionnaireDetailEntity::getWebQuestionnaireId,webQuestionnaire.getId());
            List<WebQuestionnaireDetailEntity> list = webQuestionnaireDetailService.list(lqDetail);
            List<Map<String,Object>> listNonu = new ArrayList<>();
            if(list != null && list.size() > 0){
                for(WebQuestionnaireDetailEntity bean:list){
                    Map<String,Object> map = new HashMap<>();
                    map.put("nounValue",bean.getNounValue());
                    map.put("nounValueTime",bean.getCreateTime());
                    listNonu.add(map);
                }
                webQuestionnaire.setNounValueList(listNonu);
            }
            //组织医生回复明细记录
            LambdaQueryWrapper<WebDoctorCommentEntity> lq = Wrappers.lambdaQuery();
            lq.eq(WebDoctorCommentEntity::getWebQuestionnaireId,id);
            lq.eq(WebDoctorCommentEntity::getSysUserId,webQuestionnaire.getUserId());
            List<WebDoctorCommentEntity> doctorCommentList = webDoctorCommentService.list(lq);
            List<Map<String,Object>> doCommentList = new ArrayList<>();
            if(doctorCommentList != null && doctorCommentList.size() > 0) {
                for(WebDoctorCommentEntity bean:doctorCommentList){
                    Map<String,Object> map = new HashMap<>();
                    map.put("docComment",bean.getDoctorComment());
                    map.put("docCommentTime",bean.getCreateTime());
                    doCommentList.add(map);
                }
            }
            webQuestionnaire.setDoctorCommentList(doCommentList);
        }
        return R.ok().put("webQuestionnaire", webQuestionnaire);
    }

    /**
     * 查询i天前的数据根据用户ID和hospitalId
     * 注意：// 按照提交时间倒序排列取第一条，为准，每天的最后一次检测为计算
     * @param i
     * @param userId
     * @param hospitalId
     * @return
     */
    @Override
    public WebQuestionnaireEntity queryRecentDayBean(int i, Long userId, Long hospitalId) {
        String date = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                .withZone(ZoneId.systemDefault()).format(Instant.now().minus(i, ChronoUnit.DAYS));
        LambdaQueryWrapper<WebQuestionnaireEntity> lq = Wrappers.lambdaQuery();
        lq.eq(WebQuestionnaireEntity::getUserId,userId);
        lq.eq(WebQuestionnaireEntity::getHospitalId, hospitalId);
        lq.like(WebQuestionnaireEntity::getSubTime,date);
        lq.orderByDesc(WebQuestionnaireEntity::getSubTime);// 按照提交时间倒序排列取第一条，为准
        List<WebQuestionnaireEntity> list = webQuestionnaireDao.selectList(lq);
        if(list == null || list.size() == 0){
            return null;
        }else{
            return list.get(0);
        }
    }


    /**
     * 查询i天前的数据根据用户ID和hospitalId
     * 注意：// 按照提交时间倒序排列取第一条，为准，每天的最后一次检测为计算
     * @param i
     * @param userId
     * @param hospitalId
     * @return
     */
    @Override
    public List<WebQuestionnaireEntity> queryRecentDayList(int i, Long userId, Long hospitalId) {
        String date = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                .withZone(ZoneId.systemDefault()).format(Instant.now().minus(i, ChronoUnit.DAYS));
        LambdaQueryWrapper<WebQuestionnaireEntity> lq = Wrappers.lambdaQuery();
        lq.eq(WebQuestionnaireEntity::getUserId,userId);
        lq.eq(WebQuestionnaireEntity::getHospitalId, hospitalId);
        lq.like(WebQuestionnaireEntity::getSubTime,date);
        lq.orderByDesc(WebQuestionnaireEntity::getSubTime);// 按照提交时间倒序排列取第一条，为准
        List<WebQuestionnaireEntity> list = webQuestionnaireDao.selectList(lq);
        return list;
    }

    @Override
    public R saveCustom(WebQuestionnaireEntity entity) throws ScriptException, TemplateException, IOException {
        if(null == entity.getUserId()){
            return R.error("没有得到当前用户的信息");
        }

        if(null == entity.getHospitalId()){
            return R.error("没有获取到当前登陆人的医院信息");
        }
        if(StringUtils.isBlank(entity.getSubContent())){
            return R.error("没有获取到填写的患者基础信息");
        }
        entity.setSubTime(new Date());

        R painetExist = validatePaientExistOrNot(entity);
        if(painetExist != null) return painetExist;
        entity.setSubStatus(1);//默认填1，目前其实没有什么用处
        entity.setHandleStatus(HandleStatusEnum.unhanle.getValue());

        //计算当天的临床观察得分
        calculateCurrentScore(entity);
        //计算当天的病程天数
        calculateDiseaseDays(entity);
        int insert = webQuestionnaireDao.insert(entity);
        if(insert <= 0 ) return R.error("保存患者基础信息失败");
        //准备数据
        preparePatientInfoToFormula(entity);
        //计算并保存结果
        calculateData(entity);
        return R.ok();
    }

    /**
     * 基础信息校验
     * @param entity
     * @return
     */
    private R validatePaientExistOrNot(WebQuestionnaireEntity entity) {
        LambdaQueryWrapper<WebUserEntity> lq = Wrappers.lambdaQuery();
        lq.eq(WebUserEntity::getUserId,entity.getUserId());
        List<WebUserEntity> list = webUserDao.selectList(lq);
        if(list.size() <= 0) return R.error("患者不存在，请传入正确的患者ID");
        if(list.size() > 1) return R.error("根据患者id 查询到多条患者信息，请联系技术人员支持");
        WebUserEntity webUserEntity = list.get(0);
        if(entity.getHospitalId().longValue() != webUserEntity.getHospitalId().longValue()) return R.error("患者ID和医院ID对应不上");
        return null;
    }


    /**
     * 计算当天分数
     * @param entity
     */
    private void calculateCurrentScore(WebQuestionnaireEntity entity) {
        String subContent = entity.getSubContent();
        try {
            JSONArray parse = (JSONArray) JSON.parse(subContent);
            Integer score = 0;//分值
            for (Object o : parse) {
                Map<String,Object> map = (Map<String,Object>)o;
                if(map.containsKey(Constant.score)){
                    Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                    score += tmpScore;
                }
            }
            entity.setScore(score);
        }catch( Exception e){
            log.error("计算患者基础情况分值时报错，传递的jsonStr:[{}],错误信息：[{}]",entity.getSubContent(),e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * 计算病程天数
     * @param entity
     */
    private void calculateDiseaseDays(WebQuestionnaireEntity entity) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LambdaQueryWrapper<WebBasicPatientInformationEntity> lq = Wrappers.lambdaQuery();
        lq.eq(WebBasicPatientInformationEntity::getUserId,entity.getUserId());
        lq.eq(WebBasicPatientInformationEntity::getHospitalId, entity.getHospitalId());
        WebBasicPatientInformationEntity bean = patientInformationDao.selectOne(lq);
        if(null == bean){
            log.error("用户ID为：[{}],医院id:[{}],没有查询到患者基础信息",entity.getUserId(), entity.getHospitalId());
            entity.setDiseaseDay(null);
            return;
        }

        String subContent = bean.getSubContent();
        if(StringUtils.isBlank(subContent)) entity.setDiseaseDay(null);
        try {
            JSONArray parse = (JSONArray) JSON.parse(subContent);
            Integer diseaseDate = null;//分值
            for (Object o : parse) {
                Map<String,Object> map = (Map<String,Object>)o;
                if(map.containsKey(Constant.content)
                        && Constant.diseaseday.equals(String.valueOf(map.get(Constant.content)))
                        && map.containsKey(Constant.value)){
                    String diseaseDateStr = String.valueOf(map.get(Constant.value));
                    LocalDate startDate = LocalDate.parse(diseaseDateStr, dtf);
                    Long until = Math.abs(LocalDate.now().until(startDate, ChronoUnit.DAYS));
                    diseaseDate = until.intValue();
                    break;
                }
            }
            entity.setDiseaseDay(diseaseDate);
        }catch( Exception e){
            log.error("计算患者病程天数时报错，传递的jsonStr:[{}],错误信息：[{}]",bean.getSubContent(),e.getMessage());
            entity.setDiseaseDay(null);
            e.printStackTrace();
        }
    }


    /* *
     * 根据患者填写的临床观察表，计算患者的状态，得出分值和诊断建议
     */
    private void preparePatientInfoToFormula(WebQuestionnaireEntity entity) throws IOException, TemplateException, ScriptException {
        //判断用户是否是第一天使用改工具
        judgeIfFirstDayUse(entity);
        //准备基础分值 用于 病程第8天自觉症状无好转 总分10分，包括年龄评分、妊娠评分、基础疾病评分
        prepareBasicScore(entity);
        //获取昨日分数
        calculateYesterDayScore(entity);
        //获取最近七天的临床观察分数。
        prepareRecentSevenDayData(entity);
        //查询本人所有的CT复查结果，用每天的最后一条记录来计算
        prepareAllCTResult(entity);
        //准备核酸检测相关数据
        prepareNucleicAcidDetection(entity);
    }


    /**
     * 判断患者是否是第一天使用
     * @param entity
     */
    private void judgeIfFirstDayUse(WebQuestionnaireEntity entity) {
        List<WebQuestionnaireEntity> list = webQuestionnaireDao.queryDataListGroupByDate(entity);
        if(list == null || list.size() == 0){
            entity.setIfFirstDayUse(null);
            return;
        }
        if(list.size() == 1){
            entity.setIfFirstDayUse(true);
        }else if(list.size() > 1){
            entity.setIfFirstDayUse(false);
        }
    }


    /**
     * 准备基础分值 用于 病程第8天自觉症状无好转 总分10分，包括年龄评分、妊娠评分、基础疾病评分
     * @param entity
     */
    private void prepareBasicScore(WebQuestionnaireEntity entity) {
        LambdaQueryWrapper<WebBasicPatientInformationEntity> lq = Wrappers.lambdaQuery();
        lq.eq(WebBasicPatientInformationEntity::getUserId,entity.getUserId());
        lq.eq(WebBasicPatientInformationEntity::getHospitalId,entity.getHospitalId());
        List<WebBasicPatientInformationEntity> list = patientInformationDao.selectList(lq);
        if(list == null || list.size() == 0){
            log.error("没有根据用户ID:[{}],医院id:[{}],查询到患者基础信息",entity.getUserId(), entity.getHospitalId());
            return;
        }
        if(list.size() > 1){
            log.error("根据用户ID:[{}],医院id:[{}],查询到多条患者基础信息",entity.getUserId(), entity.getHospitalId());
            return;
        }
        WebBasicPatientInformationEntity basicBean = list.get(0);
        Integer basicScore = 0;
        if(null != basicBean && StringUtils.isNotBlank(basicBean.getSubContent())) {
            try {
                JSONArray parse = (JSONArray) JSON.parse(basicBean.getSubContent());
                for (Object o : parse) {
                    Map<String,Object> map = (Map<String,Object>)o;
                    if(map.containsKey(Constant.content)
                            && Constant.age.equals(String.valueOf(map.get(Constant.content)))
                            && map.containsKey(Constant.score)) {
                        Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                        basicScore = basicScore + tmpScore;
                    }

                    if(map.containsKey(Constant.content)
                            && Constant.ifPregnancy.equals(String.valueOf(map.get(Constant.content)))
                            && map.containsKey(Constant.score)) {
                        Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                        basicScore = basicScore + tmpScore;
                    }

                    if(map.containsKey(Constant.content)
                            && Constant.ifBasicIllness.equals(String.valueOf(map.get(Constant.content)))
                            && map.containsKey(Constant.score)) {
                        Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                        basicScore = basicScore + tmpScore;
                    }
                }
                entity.setBasicScore(basicScore);
            }catch( Exception e){
                log.error("计算患者基础得分时报错，患者ID：{}，医院ID：{}，传递的jsonStr:[{}],错误信息:[{}]",entity.getUserId(), entity.getHospitalId(),basicBean.getSubContent(),e.getMessage());
                e.printStackTrace();
            }
        }
    }


    /**
     * 计算并保存结果
     * @param entity
     * @throws IOException
     * @throws TemplateException
     * @throws ScriptException
     */
    private void calculateData(WebQuestionnaireEntity entity) throws IOException, TemplateException, ScriptException {
        List<Map<String, Object>> formulaList = FormulaList.initFormulaList();
        List<WebQuestionnaireDetailEntity> saveList = Lists.newArrayList();
        Integer maxLevel = null; //计算得到的最高级
        String maxNounValue = ""; // 最高级对应的患者概论
        String maxDiagnosisValue = ""; // 最高级对应的诊断概要建议
        //特殊处理第四状态
        handleSpecialFourthStatus(entity,formulaList);

        for (Map<String, Object> tmpMap : formulaList) {
            Integer tmpStatus = Integer.valueOf(String.valueOf(tmpMap.get(Constant.DiagnosticStatus))); //判定等级
            String tmpSerialNumber = String.valueOf(tmpMap.get(Constant.SerialNumber)); //序号
            //第四状态单独处理
            if(tmpStatus == VerbStatusEnum.fourthState.getValue()){
                tmpStatus = VerbStatusEnum.normal.getValue();
            }
            String tmpFormula = String.valueOf(tmpMap.get(Constant.Formula)); //公式
            String tmpManageInfo = String.valueOf(tmpMap.get(Constant.ManageInfo)); //管理端信息
            String tmpPatientInfo = String.valueOf(tmpMap.get(Constant.PatientInfo)); //患者端信息
            String tmpDecisionRules = String.valueOf(tmpMap.get(Constant.DecisionRules)); //判定依据
            //运行计算
            boolean eval = getCalculateResult(entity, tmpSerialNumber, tmpFormula, tmpDecisionRules);
            if(eval){
                WebQuestionnaireDetailEntity tmpBean = new WebQuestionnaireDetailEntity();
                tmpBean.setWebQuestionnaireId(entity.getId());
                tmpBean.setCreateTime(new Date());
                tmpBean.setLevel(tmpStatus);
                tmpBean.setDiagnosisValue(tmpManageInfo);
                tmpBean.setNounValue(tmpPatientInfo);
                tmpBean.setDecisionRules(tmpDecisionRules);
                saveList.add(tmpBean);
                if(maxLevel == null || maxLevel < tmpStatus){
                    maxLevel = tmpStatus;
                    maxNounValue = tmpPatientInfo;
                    maxDiagnosisValue = tmpManageInfo;
                }
            }
        }

        Integer rating = RatingEnum.risk.getValue();
        if(maxLevel != null){
            if(maxLevel == VerbStatusEnum.red.getValue().intValue()){
                rating = RatingEnum.Serve.getValue();
            }else if(StringUtils.isNotBlank(entity.getNucleicAcidDetection())
                    && NucleicAcidDetectionEnum.Positive.getValue().equals(entity.getNucleicAcidDetection())
            ){
                rating = RatingEnum.Serve.getValue();
            }else if(maxLevel == VerbStatusEnum.yellow.getValue().intValue()){
                rating = RatingEnum.Mild.getValue();
            }
        }
        entity.setRating(rating);

        //保存本次判定的结果列表
        batchSaveCurrentJudgementResultList(entity,maxLevel, saveList);
        //跟新患者本次临床检查的主表结果
        updateJudgementResult(entity, maxLevel, maxNounValue, maxDiagnosisValue);
        //更新人员每日状态表数据
        saveOrUpdateRatingDayData(entity);
        //更新用户每日的分值排序
        saveOrUpdateWebUserDayorderbyData(entity);
    }


    /**
     * 特殊处理第四状态
     * @param entity
     * @param formulaList
     */
    private void handleSpecialFourthStatus(WebQuestionnaireEntity entity, List<Map<String, Object>> formulaList)
            throws IOException, TemplateException, ScriptException {
        List<WebUserReviewEntity> list = Lists.newArrayList();
        for (Map<String, Object> tmpMap : formulaList) {
            Integer tmpStatus = Integer.valueOf(String.valueOf(tmpMap.get(Constant.DiagnosticStatus))); //判定等级
            String tmpSerialNumber = String.valueOf(tmpMap.get(Constant.SerialNumber)); //序号
            String tmpCheckMark = String.valueOf(tmpMap.get(Constant.CheckMark)); // 是否需要复查的标识
            //第四状态单独处理
            if(tmpStatus != VerbStatusEnum.fourthState.getValue()){
                continue;
            }
            log.info("序号为：[{}],公式名为：[map{}],第四状态 特殊处理",tmpSerialNumber,tmpSerialNumber);

            String tmpFormule = String.valueOf(tmpMap.get(Constant.Formula)); //公式
            String tmpDecisionRules = String.valueOf(tmpMap.get(Constant.DecisionRules)); //判定依据
            String tmpManageInfo = String.valueOf(tmpMap.get(Constant.ManageInfo)); //管理端信息
            boolean eval = getCalculateResult(entity, tmpSerialNumber, tmpFormule, tmpDecisionRules);
            if(eval) {
                WebUserReviewEntity wre = new WebUserReviewEntity();
                wre.setUserId(entity.getUserId());
                wre.setHospitalId(entity.getHospitalId());
                wre.setSubTime(new Date());
                wre.setLastCheckTimeCt(entity.getLastLungCTResultDate());//上次CT检查时间
                wre.setHandleStatus(CheckHandleStatusEnum.unhanle.getValue());
                wre.setIgnoreTime(null);
                wre.setDeleteFlag(DeleteStatusEnum.unDeleted.getValue());
                wre.setDecisionRules(tmpDecisionRules);
                wre.setReviewInfo(tmpManageInfo);
                wre.setWebQuestionnaireId(entity.getId());
                list.add(wre);
            }
        }
        if(list.size() > 0){
            boolean b = webUserReviewService.saveBatch(list);
            if(!b) throw new RRException("批量保存复查诊断信息失败");
        }
    }


    /**
     *
     * 根据传递的数据，进行计算
     * @param entity 提供数据
     * @param tmpSerialNumber 公式的序号
     * @param tmpFormula 公式
     * @param tmpDecisionRules 判定规则
     * @return
     * @throws IOException
     * @throws TemplateException
     * @throws ScriptException
     */
    private boolean getCalculateResult(WebQuestionnaireEntity entity,
                                       String tmpSerialNumber, String tmpFormula, String tmpDecisionRules)
            throws IOException, TemplateException, ScriptException {
        Template t = new Template("temp", new StringReader(tmpFormula), new Configuration());
        StringWriter out = new StringWriter();
        t.process(entity, out);
        String content = out.toString();
        log.info("序号为：[{}],公式名为：[map{}],公式格式化:[{}]", tmpSerialNumber, tmpSerialNumber, content);
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("nashorn");
        boolean eval = (boolean) scriptEngine.eval(content);
        log.info("用户id:[{}],医院ID：[{}]，临床检查id:[{}],序号为：[{}],公式名为：[map{}],执行公式：[{}],判定依据:[{}],结果：[{}]",
                entity.getUserId(), entity.getHospitalId(), entity.getId(), tmpSerialNumber, tmpSerialNumber, content, tmpDecisionRules, eval);
        return eval;
    }


    /**
     * 保存本次判定的结果列表
     * @param entity
     * @param maxLevel
     * @param saveList
     */
    private void batchSaveCurrentJudgementResultList(WebQuestionnaireEntity entity, Integer maxLevel, List<WebQuestionnaireDetailEntity> saveList) {
        if(saveList == null || saveList.size() <= 0){
            WebQuestionnaireDetailEntity tmpBean = new WebQuestionnaireDetailEntity();
            tmpBean.setWebQuestionnaireId(entity.getId());
            tmpBean.setCreateTime(new Date());
            tmpBean.setLevel(VerbStatusEnum.normal.getValue());
            if(entity.getIfFirstDayUse()){
                tmpBean.setNounValue(Constant.defaultFirstPaientInfo);
            }else{
                tmpBean.setNounValue(Constant.defaultPaientInfo);
            }
            tmpBean.setDiagnosisValue("");
            tmpBean.setDecisionRules("");
            boolean saveFlag = webQuestionnaireDetailService.save(tmpBean);
            if(!saveFlag) throw new RRException("保存失败");
        }else{
            if(maxLevel == VerbStatusEnum.red.getValue() || maxLevel == VerbStatusEnum.yellow.getValue()){
                for (int i = saveList.size() - 1; i >= 0; i--) {
                    WebQuestionnaireDetailEntity tmpBean = saveList.get(i);
                    if(tmpBean == null || null == tmpBean.getLevel()){
                        continue;
                    }
                    if(tmpBean.getLevel() == VerbStatusEnum.normal.getValue()){
                        saveList.remove(i);
                    }
                }
            }
            if(maxLevel == VerbStatusEnum.normal.getValue() && entity.getIfFirstDayUse()){
                for (WebQuestionnaireDetailEntity tmpBean : saveList) {
                    tmpBean.setNounValue(Constant.defaultFirstPaientInfo);
                }
            }
//            if(RatingEnum.Mild.getValue() == entity.getRating()){
//                for (WebQuestionnaireDetailEntity tmpBean : saveList) {
//                    tmpBean.setNounValue(Constant.defaultMildPaientInfo);
//                }
//            }

            boolean b = webQuestionnaireDetailService.saveBatch(saveList);
            if(!b) throw new RRException("批量保存失败");
        }
    }

    /**
     * 跟新患者本次临床检查的主表结果
     * @param entity
     * @param maxLevel
     * @param maxNounValue
     * @param maxDiagnosisValue
     */
    private void updateJudgementResult(WebQuestionnaireEntity entity, Integer maxLevel, String maxNounValue, String maxDiagnosisValue) {
        if(maxLevel == null) {
            WebQuestionnaireEntity tmpBean = new WebQuestionnaireEntity();
            tmpBean.setVerbStatus(VerbStatusEnum.normal.getValue());
            tmpBean.setOriginalVerbStatus(VerbStatusEnum.normal.getValue());
            if(entity.getIfFirstDayUse()){
                tmpBean.setNounValue(Constant.defaultFirstPaientInfo);
            }else{
                tmpBean.setNounValue(Constant.defaultPaientInfo);
            }
            tmpBean.setDiagnosisValue(Constant.defaultNormalManageInfo);
            tmpBean.setId(entity.getId());
            int i = webQuestionnaireDao.updateById(tmpBean);
            if(i != 1) {
                log.error("用户id:[{}]，更新VerbStatus:[{}],NounValue:[{}],DiagnosisValue:[{}]失败",entity.getUserId(),maxLevel,maxNounValue,maxDiagnosisValue);
                throw new RRException("更新患者今日级别和辅助信息失败");
            }
        }else {
            WebQuestionnaireEntity tmpBean = new WebQuestionnaireEntity();
            tmpBean.setVerbStatus(maxLevel);
            tmpBean.setOriginalVerbStatus(maxLevel);
            if(maxLevel == VerbStatusEnum.normal.getValue() && entity.getIfFirstDayUse()){
                tmpBean.setNounValue(Constant.defaultFirstPaientInfo);
            }else{
                tmpBean.setNounValue(maxNounValue);
            }
            tmpBean.setDiagnosisValue(maxDiagnosisValue);
            tmpBean.setId(entity.getId());
            int i = webQuestionnaireDao.updateById(tmpBean);
            if(i != 1) {
                log.error("用户id:[{}]，更新VerbStatus:[{}],NounValue:[{}],DiagnosisValue:[{}]失败",entity.getUserId(),maxLevel,maxNounValue,maxDiagnosisValue);
                throw new RRException("更新患者今日级别和辅助信息失败");
            }
        }
    }


    /**
     * 保存或者更新用户每日的分值排序数据
     * @param entity
     */
    private void saveOrUpdateWebUserDayorderbyData(WebQuestionnaireEntity entity) {
        LambdaQueryWrapper<WebUserDayorderbyEntity> lq = new LambdaQueryWrapper<>();
        lq.eq(WebUserDayorderbyEntity::getUserId,entity.getUserId());
        lq.like(WebUserDayorderbyEntity::getSubTime, LocalDate.now());
        List<WebUserDayorderbyEntity> list = webUserDayorderbyDao.selectList(lq);
        entity = this.getById(entity.getId());//前一个方法里更新了，数据此处刷新
        if(list == null || list.size() == 0){
            WebUserDayorderbyEntity tmpBean = new WebUserDayorderbyEntity();
            tmpBean.setScore(entity.getScore());
            tmpBean.setSubTime(entity.getSubTime());
            tmpBean.setVerbStatus(entity.getVerbStatus());
            tmpBean.setWebQuestionnaireId(entity.getId());
            tmpBean.setUserId(entity.getUserId());
            int insert = webUserDayorderbyDao.insert(tmpBean);
            if(insert != 1) throw new RRException("保存或者更新用户每日的分值排序数据 失败");
        }else if(list.size() > 1){
            log.error("根据用户ID:{}和时间:{}查询用户每日的分值排序数据得到多条",entity.getUserId(),LocalDate.now());
            throw new RRException("单日有多条分值排序数据,请联系后台管理员");
        }else{
            WebUserDayorderbyEntity bean = list.get(0);
            if(bean.getVerbStatus() < entity.getVerbStatus()){
                bean.setVerbStatus(entity.getVerbStatus());
                bean.setSubTime(entity.getSubTime());
                bean.setWebQuestionnaireId(entity.getId());
                bean.setScore(entity.getScore());
                int i = webUserDayorderbyDao.updateById(bean);
                if(i != 1) throw new RRException("保存或者更新用户每日的分值排序数据 失败");
            }else if(bean.getVerbStatus() == entity.getVerbStatus() && bean.getScore() <= entity.getScore()){
                bean.setVerbStatus(entity.getVerbStatus());
                bean.setSubTime(entity.getSubTime());
                bean.setWebQuestionnaireId(entity.getId());
                bean.setScore(entity.getScore());
                int i = webUserDayorderbyDao.updateById(bean);
                if(i != 1) throw new RRException("保存或者更新用户每日的分值排序数据 失败");
            }
        }
    }


    /**
     * 更新每日患者的状态数
     * @param entity
     */
    private void saveOrUpdateRatingDayData(WebQuestionnaireEntity entity) {
        //临床观察中加入核酸检测，临床病程相关结果+核酸检测阳性评估为轻症，剩余为疑似；重症判断依据不变；
        WebQuestionnaireEntity bean = this.getById(entity.getId());//前一个方法里更新了，数据此处刷新
        entity.setVerbStatus(bean.getVerbStatus());
        LambdaQueryWrapper<WebUserRatingDayEntity> lq = new LambdaQueryWrapper<>();
        lq.eq(WebUserRatingDayEntity::getUserId,entity.getUserId());
        lq.like(WebUserRatingDayEntity::getReportDate, LocalDate.now());
        List<WebUserRatingDayEntity> list = webUserRatingDayDao.selectList(lq);
        if(list == null || list.size() == 0){
            WebUserRatingDayEntity tmpBean = new WebUserRatingDayEntity();
            orgnizationData(entity, tmpBean);
            int insert = webUserRatingDayDao.insert(tmpBean);
            if(insert != 1) throw new RRException("保存或者更新用户每日的状态数据 失败");
        }else if(list.size() > 1){
            log.error("根据用户ID:{}和时间:{}查询用户每日状态数据得到多条",entity.getUserId(),LocalDate.now());
            throw new RRException("单日同一患者有多条数据,请联系后台管理员");
        }else{
            WebUserRatingDayEntity tmpBean = list.get(0);
            if(tmpBean.getRating() < entity.getRating()){
                orgnizationData(entity, tmpBean);
                int i = webUserRatingDayDao.updateById(tmpBean);
                if(i != 1) throw new RRException("保存或者更新用户每日的状态数据 失败");
            }
        }

        //更新用户表的用户rating状态
        updateWebUserRating(entity.getUserId(),entity.getRating());
    }

    /**
     * 更新用户表的用户rating状态
     * @param userId
     * @param rating
     */
    private void updateWebUserRating(Long userId, Integer rating) {
        WebUserEntity webUserEntity = webUserDao.selectById(userId);
        if(null == webUserEntity){
            throw new RRException("用户不存在");
        }
        if(webUserEntity.getRating() == null || rating > webUserEntity.getRating()){
            WebUserEntity we = new WebUserEntity();
            we.setUserId(userId);
            we.setRating(rating);
            int i = webUserDao.updateById(we);
            if(i != 1) throw new RRException("更新用户状态数据 失败");
        }
    }


    /**
     * 组织每日状态数据
     * @param entity
     * @param rating
     * @param tmpBean
     */
    private void orgnizationData(WebQuestionnaireEntity entity, WebUserRatingDayEntity tmpBean) {
        tmpBean.setUserId(entity.getUserId());
        tmpBean.setHospitalId(entity.getHospitalId());
        ZonedDateTime zonedDateTime = LocalDate.now().atStartOfDay(ZoneId.systemDefault());
        tmpBean.setReportDate(Date.from(zonedDateTime.toInstant()));
        if (null != entity.getCoughScore() && entity.getCoughScore() > 0) {
            tmpBean.setCough(1);
        } else {
            tmpBean.setCough(0);
        }
        if (null != entity.getDiarrheaScore() && entity.getDiarrheaScore() > 0) {
            tmpBean.setDiarrhea(1);
        } else {
            tmpBean.setDiarrhea(0);
        }
        if (null != entity.getDifficultyBreathingScore() && entity.getDifficultyBreathingScore() > 0) {
            tmpBean.setDifficultyBreathing(1);
        } else {
            tmpBean.setDifficultyBreathing(0);
        }
        if (null != entity.getFatigueScore() && entity.getFatigueScore() > 0) {
            tmpBean.setFatigue(1);
        } else {
            tmpBean.setFatigue(0);
        }
        if (null != entity.getBodyTemperatureScore() && entity.getBodyTemperatureScore() > 0) {
            tmpBean.setFever(1);
        } else {
            tmpBean.setFever(0);
        }
        if (null != entity.getMentalStateScore() && entity.getMentalStateScore() > 0) {
            tmpBean.setMentalState(1);
        } else {
            tmpBean.setMentalState(0);
        }
        if (null != entity.getMuscleAcheScore() && entity.getMuscleAcheScore() > 0) {
            tmpBean.setMuscleAche(1);
        } else {
            tmpBean.setMuscleAche(0);
        }
        tmpBean.setWebQuestionnaireId(entity.getId());
        tmpBean.setRating(entity.getRating());
    }


    /**
     * 准备昨日的分数 要拿分数最小的
     * @param entity
     */
    private void calculateYesterDayScore(WebQuestionnaireEntity entity) {
        List<WebQuestionnaireEntity> list = this.queryRecentDayList(1, entity.getUserId(), entity.getHospitalId());
        if(list == null || list.size() == 0){
            entity.setYesterdayScore(null);
        }else{
            WebQuestionnaireEntity tempBean = list.stream()
                    .filter(obj -> null != obj && null != obj.getScore())
                    .min(Comparator.comparing(WebQuestionnaireEntity::getScore))
                    .orElse(null);
            if(null == tempBean || null == tempBean.getScore()){
                entity.setYesterdayScore(null);
            }else{
                entity.setYesterdayScore(tempBean.getScore());
            }
        }
    }


    /**
     * 准备最近五天的临床观察分数
     * @param entity
     */
    private void prepareRecentSevenDayData(WebQuestionnaireEntity entity) {
        //准备今天的数据
        prepareTodayData(entity);
        //准备昨天的数据
        prepareYesterdayData(entity);
        //准备两天前的数据
        prepareTwoDaysAgoData(entity);
        //准备三天前的数据
        prepareThreeDaysAgoData(entity);
        //准备四天前的数据
        prepareFourDaysAgoData(entity);
        //准备五天前的数据
        prepareFiveDaysAgoData(entity);
        //准备六天前的数据
        prepareSixDaysAgoData(entity);
    }


    /**
     * 准备今天的临床诊断数据
     * @param entity
     */
    private void prepareTodayData(WebQuestionnaireEntity entity) {
        if(null != entity && StringUtils.isNotBlank(entity.getSubContent())) {
            try {
                JSONArray parse = (JSONArray) JSON.parse(entity.getSubContent());
                for (Object o : parse) {
                    Map<String,Object> map = (Map<String,Object>)o;
                    if(map.containsKey(Constant.content)){
                        String tmpKey = String.valueOf(map.get(Constant.content));
                        //体温分数
                        if(Constant.bodyTemperature.equals(tmpKey)){
                            if(map.containsKey(Constant.score)){
                                Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                                entity.setBodyTemperatureScore(tmpScore);
                            }else{
                                entity.setBodyTemperatureScore(null);
                            }
                        }
                        //咳嗽分数
                        if(Constant.cough.equals(tmpKey)){
                            if(map.containsKey(Constant.score)){
                                Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                                entity.setCoughScore(tmpScore);
                            }else{
                                entity.setCoughScore(null);
                            }
                        }
                        //腹泻分数
                        if(Constant.diarrhea.equals(tmpKey)){
                            if(map.containsKey(Constant.score)){
                                Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                                entity.setDiarrheaScore(tmpScore);
                            }else{
                                entity.setDiarrheaScore(null);
                            }
                        }

                        //呼吸困难的分数
                        if(Constant.difficultyBreathing.equals(tmpKey)){
                            if(map.containsKey(Constant.score)){
                                Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                                entity.setDifficultyBreathingScore(tmpScore);
                            }else{
                                entity.setDifficultyBreathingScore(null);
                            }
                        }

                        //乏力分值
                        if(Constant.fatigue.equals(tmpKey)){
                            if(map.containsKey(Constant.score)){
                                Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                                entity.setFatigueScore(tmpScore);
                            }else{
                                entity.setFatigueScore(null);
                            }
                        }
                        //精神状态分值
                        if(Constant.mentalState.equals(tmpKey)){
                            if(map.containsKey(Constant.score)){
                                Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                                entity.setMentalStateScore(tmpScore);
                            }else{
                                entity.setMentalStateScore(null);
                            }
                        }
                        //肌肉酸痛分数
                        if(Constant.muscleAche.equals(tmpKey)){
                            if(map.containsKey(Constant.score)){
                                Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                                entity.setMuscleAcheScore(tmpScore);
                            }else{
                                entity.setMuscleAcheScore(null);
                            }
                        }
                        //今日是否有复诊检查
                        if(Constant.ReviewDiagnosis.equals(tmpKey)){
                            if(map.containsKey(Constant.value)){
                                String tmpValue = String.valueOf(map.get(Constant.value));
                                if("是".equals(tmpValue)){
                                    entity.setReviewDiagnosis(true);
                                }else if("否".equals(tmpValue)){
                                    entity.setReviewDiagnosis(false);
                                }
                            }else{
                                entity.setReviewDiagnosis(null);
                            }
                        }
                        //存储今日胸部CT结果
                        if(Constant.lungCTResult.equals(tmpKey)){
                            if(map.containsKey(Constant.value)){
                                String tmpValue = String.valueOf(map.get(Constant.value));
                                entity.setLungCTResult(tmpValue);
                            }else{
                                entity.setReviewDiagnosis(null);
                            }
                        }



                        //存储今天核酸检测-复查的结果
                        if(Constant.NucleicAcidDetection.equals(tmpKey)){
                            // 加入当前问题是复查类型的判断
                            Boolean tmpTypeFlag = false;
                            if(map.containsKey(Constant.type)){
                                Integer tmpType = Integer.valueOf(String.valueOf(map.get(Constant.type)));
                                if(QuestionTypeEnum.reCheck.getValue().intValue() == tmpType.intValue()){
                                    tmpTypeFlag = true;
                                }
                            }
                            if(tmpTypeFlag && map.containsKey(Constant.value)){
                                String tmpValue = String.valueOf(map.get(Constant.value));
                                if(StringUtils.isNotBlank(tmpValue)
                                        && !tmpValue.equals(NucleicAcidDetectionEnum.uncheck.getValue())){
                                    entity.setNucleicAcidDetection(tmpValue);
                                }
                            }else{
                                entity.setNucleicAcidDetection(null);
                            }
                        }

                        //存储今天核酸检测-每日临床检查的结果
                        /*if(Constant.NucleicAcidDetection.equals(tmpKey)){
                            // 加入当前问题是复查类型的判断
                            Boolean tmpTypeFlag = false;
                            if(map.containsKey(Constant.type)){
                                Integer tmpType = Integer.valueOf(String.valueOf(map.get(Constant.type)));
                                if(QuestionTypeEnum.dayCheck.getValue().intValue() == tmpType.intValue()){
                                    tmpTypeFlag = true;
                                }
                            }
                            if(tmpTypeFlag && map.containsKey(Constant.value)){
                                String tmpValue = String.valueOf(map.get(Constant.value));
                                if(StringUtils.isNotBlank(tmpValue)
                                        && !tmpValue.equals(NucleicAcidDetectionEnum.uncheck.getValue())){
                                    entity.setNucleicAcidDetectionCO(tmpValue);
                                }
                            }else{
                                entity.setNucleicAcidDetectionCO(null);
                            }
                        }*/
                    }
                }
            }catch( Exception e){
                log.error("获取今天临床观察数据时报错，传递的jsonStr:[{}],错误信息:[{}]",entity.getSubContent(),e.getMessage());
                e.printStackTrace();
            }
        }
    }


    /**
     * 准备昨天的临床诊断数据
     * @param entity
     */
    private void prepareYesterdayData(WebQuestionnaireEntity entity) {
        WebQuestionnaireEntity yesBean = this.queryRecentDayBean(1,entity.getUserId(), entity.getHospitalId());
        if(null != yesBean && StringUtils.isNotBlank(yesBean.getSubContent())) {
            try {
                JSONArray parse = (JSONArray) JSON.parse(yesBean.getSubContent());
                for (Object o : parse) {
                    Map<String,Object> map = (Map<String,Object>)o;
                    if(map.containsKey(Constant.content)){
                        String tmpKey = String.valueOf(map.get(Constant.content));
                        //体温分数
                        if(Constant.bodyTemperature.equals(tmpKey)){
                            if(map.containsKey(Constant.score)){
                                Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                                entity.setYesterdatbodyTemperatureScore(tmpScore);
                            }else{
                                entity.setYesterdatbodyTemperatureScore(null);
                            }
                        }
                        //咳嗽分数
                        if(Constant.cough.equals(tmpKey)){
                            if(map.containsKey(Constant.score)){
                                Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                                entity.setYesterdatCoughScore(tmpScore);
                            }else{
                                entity.setYesterdatCoughScore(null);
                            }
                        }
                        //腹泻分数
                        if(Constant.diarrhea.equals(tmpKey)){
                            if(map.containsKey(Constant.score)){
                                Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                                entity.setYesterdatDiarrheaScore(tmpScore);
                            }else{
                                entity.setYesterdatDiarrheaScore(null);
                            }
                        }

                        //呼吸困难的分数
                        if(Constant.difficultyBreathing.equals(tmpKey)){
                            if(map.containsKey(Constant.score)){
                                Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                                entity.setYesterdatDifficultyBreathingScore(tmpScore);
                            }else{
                                entity.setYesterdatDifficultyBreathingScore(null);
                            }
                        }

                        //乏力分值
                        if(Constant.fatigue.equals(tmpKey)){
                            if(map.containsKey(Constant.score)){
                                Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                                entity.setYesterdatFatigueScore(tmpScore);
                            }else{
                                entity.setYesterdatFatigueScore(null);
                            }
                        }
                        //精神状态分值
                        if(Constant.mentalState.equals(tmpKey)){
                            if(map.containsKey(Constant.score)){
                                Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                                entity.setYesterdatMentalStateScore(tmpScore);
                            }else{
                                entity.setYesterdatMentalStateScore(null);
                            }
                        }
                        //肌肉酸痛分数
                        if(Constant.muscleAche.equals(tmpKey)){
                            if(map.containsKey(Constant.score)){
                                Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                                entity.setYesterdatmuscleAcheScore(tmpScore);
                            }else{
                                entity.setYesterdatmuscleAcheScore(null);
                            }
                        }

                    }
                }
            }catch( Exception e){
                log.error("获取昨天临床观察数据时报错，传递的jsonStr:[{}],错误信息:[{}]",yesBean.getSubContent(),e.getMessage());
                e.printStackTrace();
            }
        }
    }


    /**
     * 准备前天的临床诊断数据
     * @param entity
     */
    private void prepareTwoDaysAgoData(WebQuestionnaireEntity entity) {
        WebQuestionnaireEntity twoDaysAgoBean = this.queryRecentDayBean(2,entity.getUserId(), entity.getHospitalId());
        if(null != twoDaysAgoBean && StringUtils.isNotBlank(twoDaysAgoBean.getSubContent())) {
            try {
                JSONArray parse = (JSONArray) JSON.parse(twoDaysAgoBean.getSubContent());
                for (Object o : parse) {
                    Map<String,Object> map = (Map<String,Object>)o;
                    if(map.containsKey(Constant.content)){
                        String tmpKey = String.valueOf(map.get(Constant.content));
                        //体温分数
                        if(Constant.bodyTemperature.equals(tmpKey)){
                            if(map.containsKey(Constant.score)){
                                Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                                entity.setBeforeYesbodyTemperatureScore(tmpScore);
                            }else{
                                entity.setBeforeYesbodyTemperatureScore(null);
                            }
                        }
                        //咳嗽分数
                        if(Constant.cough.equals(tmpKey)){
                            if(map.containsKey(Constant.score)){
                                Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                                entity.setBeforeYesCoughScore(tmpScore);
                            }else{
                                entity.setBeforeYesCoughScore(null);
                            }
                        }
                        //腹泻分数
                        if(Constant.diarrhea.equals(tmpKey)){
                            if(map.containsKey(Constant.score)){
                                Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                                entity.setBeforeYesDiarrheaScore(tmpScore);
                            }else{
                                entity.setBeforeYesDiarrheaScore(null);
                            }
                        }

                        //呼吸困难的分数
                        if(Constant.difficultyBreathing.equals(tmpKey)){
                            if(map.containsKey(Constant.score)){
                                Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                                entity.setBeforeYesDifficultyBreathingScore(tmpScore);
                            }else{
                                entity.setBeforeYesDifficultyBreathingScore(null);
                            }
                        }

                        //乏力分值
                        if(Constant.fatigue.equals(tmpKey)){
                            if(map.containsKey(Constant.score)){
                                Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                                entity.setBeforeYesFatigueScore(tmpScore);
                            }else{
                                entity.setBeforeYesFatigueScore(null);
                            }
                        }
                        //精神状态分值
                        if(Constant.mentalState.equals(tmpKey)){
                            if(map.containsKey(Constant.score)){
                                Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                                entity.setBeforeYesMentalStateScore(tmpScore);
                            }else{
                                entity.setBeforeYesMentalStateScore(null);
                            }
                        }
                        //肌肉酸痛分数
                        if(Constant.muscleAche.equals(tmpKey)){
                            if(map.containsKey(Constant.score)){
                                Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                                entity.setBeforeYesmuscleAcheScore(tmpScore);
                            }else{
                                entity.setBeforeYesmuscleAcheScore(null);
                            }
                        }

                    }
                }
            }catch( Exception e){
                log.error("获取前天临床观察数据时报错，传递的jsonStr:[{}],错误信息:[{}]",twoDaysAgoBean.getSubContent(),e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * 准备三天前的临床诊断数据
     * @param entity
     */
    private void prepareThreeDaysAgoData(WebQuestionnaireEntity entity) {
        WebQuestionnaireEntity threeDaysAgoBean = this.queryRecentDayBean(3,entity.getUserId(), entity.getHospitalId());
        if(null != threeDaysAgoBean && StringUtils.isNotBlank(threeDaysAgoBean.getSubContent())) {
            try {
                JSONArray parse = (JSONArray) JSON.parse(threeDaysAgoBean.getSubContent());
                for (Object o : parse) {
                    Map<String,Object> map = (Map<String,Object>)o;
                    if(map.containsKey(Constant.content)){
                        String tmpKey = String.valueOf(map.get(Constant.content));
                        //体温分数
                        if(Constant.bodyTemperature.equals(tmpKey)){
                            if(map.containsKey(Constant.score)){
                                Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                                entity.setGreatDayBeforebodyTemperatureScore(tmpScore);
                            }else{
                                entity.setGreatDayBeforebodyTemperatureScore(null);
                            }
                        }
                        //咳嗽分数
                        if(Constant.cough.equals(tmpKey)){
                            if(map.containsKey(Constant.score)){
                                Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                                entity.setGreatDayBeforeCoughScore(tmpScore);
                            }else{
                                entity.setGreatDayBeforeCoughScore(null);
                            }
                        }
                        //腹泻分数
                        if(Constant.diarrhea.equals(tmpKey)){
                            if(map.containsKey(Constant.score)){
                                Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                                entity.setGreatDayBeforeDiarrheaScore(tmpScore);
                            }else{
                                entity.setGreatDayBeforeDiarrheaScore(null);
                            }
                        }

                        //呼吸困难的分数
                        if(Constant.difficultyBreathing.equals(tmpKey)){
                            if(map.containsKey(Constant.score)){
                                Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                                entity.setGreatDayBeforeDifficultyBreathingScore(tmpScore);
                            }else{
                                entity.setGreatDayBeforeDifficultyBreathingScore(null);
                            }
                        }

                        //乏力分值
                        if(Constant.fatigue.equals(tmpKey)){
                            if(map.containsKey(Constant.score)){
                                Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                                entity.setGreatDayBeforeFatigueScore(tmpScore);
                            }else{
                                entity.setGreatDayBeforeFatigueScore(null);
                            }
                        }
                        //精神状态分值
                        if(Constant.mentalState.equals(tmpKey)){
                            if(map.containsKey(Constant.score)){
                                Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                                entity.setGreatDayBeforeMentalStateScore(tmpScore);
                            }else{
                                entity.setGreatDayBeforeMentalStateScore(null);
                            }
                        }
                        //肌肉酸痛分数
                        if(Constant.muscleAche.equals(tmpKey)){
                            if(map.containsKey(Constant.score)){
                                Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                                entity.setGreatDayBeforemuscleAcheScore(tmpScore);
                            }else{
                                entity.setGreatDayBeforemuscleAcheScore(null);
                            }
                        }

                    }
                }
            }catch( Exception e){
                log.error("获取三天前临床观察数据时报错，传递的jsonStr:[{}],错误信息:[{}]",threeDaysAgoBean.getSubContent(),e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * 准备四天前的临床诊断数据
     * @param entity
     */
    private void prepareFourDaysAgoData(WebQuestionnaireEntity entity) {
        WebQuestionnaireEntity fourDaysAgoBean = this.queryRecentDayBean(4,entity.getUserId(), entity.getHospitalId());
        if(null != fourDaysAgoBean && StringUtils.isNotBlank(fourDaysAgoBean.getSubContent())) {
            try {
                JSONArray parse = (JSONArray) JSON.parse(fourDaysAgoBean.getSubContent());
                for (Object o : parse) {
                    Map<String,Object> map = (Map<String,Object>)o;
                    if(map.containsKey(Constant.content)){
                        String tmpKey = String.valueOf(map.get(Constant.content));
                        //体温分数
                        if(Constant.bodyTemperature.equals(tmpKey)){
                            if(map.containsKey(Constant.score)){
                                Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                                entity.setFourDaysAgoBodyTemperatureScore(tmpScore);
                            }else{
                                entity.setFourDaysAgoBodyTemperatureScore(null);
                            }
                        }
                        //咳嗽分数
                        if(Constant.cough.equals(tmpKey)){
                            if(map.containsKey(Constant.score)){
                                Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                                entity.setFourDaysAgoCoughScore(tmpScore);
                            }else{
                                entity.setFourDaysAgoCoughScore(null);
                            }
                        }
                        //腹泻分数
                        if(Constant.diarrhea.equals(tmpKey)){
                            if(map.containsKey(Constant.score)){
                                Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                                entity.setFourDaysAgoDiarrheaScore(tmpScore);
                            }else{
                                entity.setFourDaysAgoDiarrheaScore(null);
                            }
                        }

                        //呼吸困难的分数
                        if(Constant.difficultyBreathing.equals(tmpKey)){
                            if(map.containsKey(Constant.score)){
                                Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                                entity.setFourDaysAgoDifficultyBreathingScore(tmpScore);
                            }else{
                                entity.setFourDaysAgoDifficultyBreathingScore(null);
                            }
                        }

                        //乏力分值
                        if(Constant.fatigue.equals(tmpKey)){
                            if(map.containsKey(Constant.score)){
                                Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                                entity.setFourDaysAgoFatigueScore(tmpScore);
                            }else{
                                entity.setFourDaysAgoFatigueScore(null);
                            }
                        }
                        //精神状态分值
                        if(Constant.mentalState.equals(tmpKey)){
                            if(map.containsKey(Constant.score)){
                                Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                                entity.setFourDaysAgoMentalStateScore(tmpScore);
                            }else{
                                entity.setFourDaysAgoMentalStateScore(null);
                            }
                        }
                        //肌肉酸痛分数
                        if(Constant.muscleAche.equals(tmpKey)){
                            if(map.containsKey(Constant.score)){
                                Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                                entity.setFourDaysAgoMuscleAcheScore(tmpScore);
                            }else{
                                entity.setFourDaysAgoMuscleAcheScore(null);
                            }
                        }

                    }
                }
            }catch( Exception e){
                log.error("获取四天前临床观察数据时报错，传递的jsonStr:[{}],错误信息:[{}]",fourDaysAgoBean.getSubContent(),e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * 准备五天前的临床诊断数据
     * @param entity
     */
    private void prepareFiveDaysAgoData(WebQuestionnaireEntity entity) {
        WebQuestionnaireEntity fiveDaysAgoBean = this.queryRecentDayBean(5,entity.getUserId(), entity.getHospitalId());
        if(null != fiveDaysAgoBean && StringUtils.isNotBlank(fiveDaysAgoBean.getSubContent())) {
            try {
                JSONArray parse = (JSONArray) JSON.parse(fiveDaysAgoBean.getSubContent());
                for (Object o : parse) {
                    Map<String,Object> map = (Map<String,Object>)o;
                    if(map.containsKey(Constant.content)){
                        String tmpKey = String.valueOf(map.get(Constant.content));
                        //呼吸困难的分数
                        if(Constant.difficultyBreathing.equals(tmpKey)){
                            if(map.containsKey(Constant.score)){
                                Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                                entity.setFiveDaysAgoDifficultyBreathingScore(tmpScore);
                            }else{
                                entity.setFiveDaysAgoDifficultyBreathingScore(null);
                            }
                        }
                    }
                }
            }catch( Exception e){
                log.error("获取五天前临床观察数据时报错，传递的jsonStr:[{}],错误信息:[{}]",fiveDaysAgoBean.getSubContent(),e.getMessage());
                e.printStackTrace();
            }
        }
    }


    /**
     * 准备六天前的临床诊断数据
     * @param entity
     */
    private void prepareSixDaysAgoData(WebQuestionnaireEntity entity) {
        WebQuestionnaireEntity sixDaysAgoBean = this.queryRecentDayBean(6,entity.getUserId(), entity.getHospitalId());
        if(null != sixDaysAgoBean && StringUtils.isNotBlank(sixDaysAgoBean.getSubContent())) {
            try {
                JSONArray parse = (JSONArray) JSON.parse(sixDaysAgoBean.getSubContent());
                for (Object o : parse) {
                    Map<String,Object> map = (Map<String,Object>)o;
                    if(map.containsKey(Constant.content)){
                        String tmpKey = String.valueOf(map.get(Constant.content));
                        //呼吸困难的分数
                        if(Constant.difficultyBreathing.equals(tmpKey)){
                            if(map.containsKey(Constant.score)){
                                Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                                entity.setSixDaysAgoDifficultyBreathingScore(tmpScore);
                            }else{
                                entity.setSixDaysAgoDifficultyBreathingScore(null);
                            }
                        }
                    }
                }
            }catch( Exception e){
                log.error("获取四天前临床观察数据时报错，传递的jsonStr:[{}],错误信息:[{}]",sixDaysAgoBean.getSubContent(),e.getMessage());
                e.printStackTrace();
            }
        }
    }


    /**
     * 查询本人所有的CT复查结果，用每天的最后一条记录来计算
     * @param entity
     */
    private void prepareAllCTResult(WebQuestionnaireEntity entity) {
        //查找最近一次CT时间，以及结果
        String currentLoopContent = "";
        try {
            LambdaQueryWrapper<WebQuestionnaireEntity> lq = Wrappers.lambdaQuery();
            lq.eq(WebQuestionnaireEntity::getUserId, entity.getUserId());
            lq.eq(WebQuestionnaireEntity::getHospitalId, entity.getHospitalId());
            List<WebQuestionnaireEntity> list = webQuestionnaireDao.selectList(lq);
            if(list == null || list.size() == 0) return;
            for (WebQuestionnaireEntity bean : list) {
                String subContent = bean.getSubContent();
                currentLoopContent = subContent;
                if(StringUtils.isBlank(subContent)) continue;
                JSONArray parse = (JSONArray) JSON.parse(subContent);

                for (Object o : parse) {
                    Map<String,Object> map = (Map<String,Object>)o;
                    if(map.containsKey(Constant.content)){
                        String tmpKey = String.valueOf(map.get(Constant.content));
                        //是否复诊
                        if(Constant.ReviewDiagnosis.equals(tmpKey)
                                && map.containsKey(Constant.value)
                                && "是".equals(String.valueOf(map.get(Constant.value)))){
                            bean.setReviewDiagnosis(true);
                        }else if(null == bean.getReviewDiagnosis()){
                            bean.setReviewDiagnosis(false);
                        }
                        //CT扫描结果
                        if(Constant.lungCTResult.equals(tmpKey)
                                && map.containsKey(Constant.value)){
                            String tmpValue = String.valueOf(map.get(Constant.value));
                            bean.setLungCTResult(tmpValue);
                        }else if(StringUtils.isBlank(bean.getLungCTResult())){
                            bean.setLungCTResult(null);
                        }

                        //CT扫描结果
                        if(Constant.lungCTResultDate.equals(tmpKey)
                                && map.containsKey(Constant.value)){
                            String tmpValue = String.valueOf(map.get(Constant.value));
                            bean.setLungCTResultDate(tmpValue.substring(0,10));
                        }else if(StringUtils.isBlank(bean.getLungCTResultDate())){
                            bean.setLungCTResultDate(null);
                        }
                    }
                }
            }

            //组织最近一次的CT扫描结果
            WebQuestionnaireEntity maxBean = prepareRecentCTresult(entity, list);
            if (maxBean == null) return;
            //组织倒数第二次的CT扫描结果
            Long tmpId = maxBean.getId();
            prepareLastTimeCTresult(entity, list, tmpId);
        }catch( Exception e){
            log.error("统计患者：[{}]CT扫描情况报错，传递的jsonStr:[{}],错误信息:[{}]",entity.getUserId(),currentLoopContent,e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * 组织最近一次的CT扫描结果
     * @param entity
     * @param list
     * @return
     */
    private WebQuestionnaireEntity prepareRecentCTresult(WebQuestionnaireEntity entity, List<WebQuestionnaireEntity> list) {
        WebQuestionnaireEntity maxBean = new WebQuestionnaireEntity();
        List<WebQuestionnaireEntity> maxList = list.stream()
                .filter(obj -> obj.getReviewDiagnosis() != null && obj.getReviewDiagnosis() && StringUtils.isNotBlank(obj.getLungCTResultDate()))
                .sorted(Comparator.comparing(WebQuestionnaireEntity::getLungCTResultDate).reversed())
                .sorted(Comparator.comparing(WebQuestionnaireEntity::getSubTime).reversed()).collect(Collectors.toList());

        if(maxList != null && maxList.size() > 0){
            maxBean = maxList.get(0);
        }

        if(maxBean == null){
            return null;
        }

        //保存最近一次的CT扫描时间
        entity.setLastLungCTResultDate(maxBean.getLungCTResultDate());

        //最近一次的CT检测正常。需要找到到目前为止至少7天的分数
        if(LungCTDiagnosisReportEnum.normal.getValue().equals(maxBean.getLungCTResult())){
            entity.setFirstCTStatus(true);//最近一次CT检查正常
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String diseaseDateStr = maxBean.getLungCTResultDate();
            LocalDate startDate = LocalDate.parse(diseaseDateStr, dtf);
            Long until = Math.abs(LocalDate.now().until(startDate, ChronoUnit.DAYS));
            entity.setFirstCTDays(until.intValue());//最近一次CT检查正常到目前的天数
            if(until < 7){
                entity.setLungCTResult(maxBean.getLungCTResult());
                return null;
            }else if(until >= 7){
                prepareResentSevenDayData(maxBean.getLungCTResultDate(),entity);
            }
        }
        return maxBean;
    }


    /**
     * 组织倒数第二次的CT扫描结果
     * @param entity
     * @param list
     * @param tmpId
     */
    private void prepareLastTimeCTresult(WebQuestionnaireEntity entity, List<WebQuestionnaireEntity> list, Long tmpId) {
        List<WebQuestionnaireEntity> secondMaxList = list.stream()
                .filter(obj -> obj.getReviewDiagnosis() != null
                        && obj.getReviewDiagnosis()
                        && StringUtils.isNotBlank(obj.getLungCTResultDate())
                        && null != obj.getId() && tmpId.longValue() != obj.getId().longValue())
                .sorted(Comparator.comparing(WebQuestionnaireEntity::getLungCTResultDate).reversed())
                .sorted(Comparator.comparing(WebQuestionnaireEntity::getSubTime).reversed()).collect(Collectors.toList());
        if(secondMaxList != null && secondMaxList.size() >0){
            WebQuestionnaireEntity secondMaxBean = secondMaxList.get(0);
            if(null != secondMaxBean) entity.setLastLungCTResult(secondMaxBean.getLungCTResult());
        }else{
            entity.setLastLungCTResult(null);
        }
    }


    /**
     * 准备核酸检测数据
     * @param entity
     */
    private void prepareNucleicAcidDetection(WebQuestionnaireEntity entity) {
        String currentLoopContent = "";
        try {
            LambdaQueryWrapper<WebQuestionnaireEntity> lq = Wrappers.lambdaQuery();
            lq.eq(WebQuestionnaireEntity::getUserId, entity.getUserId());
            lq.eq(WebQuestionnaireEntity::getHospitalId, entity.getHospitalId());
            List<WebQuestionnaireEntity> list = webQuestionnaireDao.selectList(lq);
            if(list == null || list.size() == 0) return;
            for (WebQuestionnaireEntity bean : list) {
                String subContent = bean.getSubContent();
                currentLoopContent = subContent;
                if(StringUtils.isBlank(subContent)) continue;
                JSONArray parse = (JSONArray) JSON.parse(subContent);
                for (Object o : parse) {
                    Map<String,Object> map = (Map<String,Object>)o;
                    if(map.containsKey(Constant.content)){
                        String tmpKey = String.valueOf(map.get(Constant.content));
                        //是否复诊
                        if(Constant.ReviewDiagnosis.equals(tmpKey)
                                && map.containsKey(Constant.value)
                                && "是".equals(String.valueOf(map.get(Constant.value)))){
                            bean.setReviewDiagnosis(true);
                        }else if(null == bean.getReviewDiagnosis()){
                            bean.setReviewDiagnosis(false);
                        }
                        //核酸检查
                        if(Constant.NucleicAcidDetection.equals(tmpKey)){
                            // 加入当前问题是复查类型的判断
                            Boolean tmpTypeFlag = false;
                            if(map.containsKey(Constant.type)){
                                Integer tmpType = Integer.valueOf(String.valueOf(map.get(Constant.type)));
                                if(QuestionTypeEnum.reCheck.getValue().intValue() == tmpType.intValue()){
                                    tmpTypeFlag = true;
                                }
                            }
                            if(tmpTypeFlag && map.containsKey(Constant.value)){
                                String tmpValue = String.valueOf(map.get(Constant.value));
                                bean.setNucleicAcidDetection(tmpValue);
                            }else if(StringUtils.isBlank(bean.getNucleicAcidDetection())){
                                bean.setNucleicAcidDetection(null);
                            }
                        }
                    }
                }
            }
            //查找上一次核酸检测的结果，空或者阳
            WebQuestionnaireEntity lastTimeBean = list.stream()
                    .filter(obj -> obj.getReviewDiagnosis() != null
                            && obj.getReviewDiagnosis()
                            && null != obj.getSubTime()
                            && StringUtils.isNotBlank(obj.getNucleicAcidDetection())
                            && !obj.getNucleicAcidDetection().equals(NucleicAcidDetectionEnum.uncheck.getValue())
                            && obj.getId().longValue() != entity.getId().longValue())
                    .max(Comparator.comparing(WebQuestionnaireEntity::getSubTime)).orElse(null);
            if(null != lastTimeBean){
                entity.setLastTimeNucleicAcidDetection(lastTimeBean.getNucleicAcidDetection());
            }

            //如果今天的核酸检测是阴性，
            // 查找最近两天内所有的核酸检测结果有一个是阳性的则TwoDaysAgoNucleicAcidDetection=阳性；全是阴性，则为阴性；
            // 如果两天内地核酸检测数据为空，则为空
            if(StringUtils.isNotBlank(entity.getNucleicAcidDetection())
                    && entity.getNucleicAcidDetection().equals(NucleicAcidDetectionEnum.negative.getValue())){
                String date = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        .withZone(ZoneId.systemDefault()).format(Instant.now().minus(2, ChronoUnit.DAYS));
                LocalDateTime twoDayAgoDateTime = LocalDateTime.parse(date+" 00:00:01",DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                List<WebQuestionnaireEntity> withInTwoDayList = list.stream()
                        .filter(obj -> obj.getReviewDiagnosis() != null
                                && obj.getReviewDiagnosis()
                                && null != obj.getSubTime()
                                && obj.getId().longValue() != entity.getId().longValue()
                                && StringUtils.isNotBlank(obj.getNucleicAcidDetection())
                                && !obj.getNucleicAcidDetection().equals(NucleicAcidDetectionEnum.uncheck.getValue())
                                && LocalDateTime.ofInstant(obj.getSubTime().toInstant(), ZoneId.systemDefault()).isAfter(twoDayAgoDateTime))
                        .collect(Collectors.toList());
                if(withInTwoDayList == null || withInTwoDayList.size() == 0){
                    entity.setTwoDaysAgoNucleicAcidDetection(null);
                }else{
                    Boolean flag = false;
                    for (WebQuestionnaireEntity tmpBean : withInTwoDayList) {
                        if(null != tmpBean
                                && NucleicAcidDetectionEnum.Positive.getValue().equals(tmpBean.getNucleicAcidDetection())){
                            entity.setTwoDaysAgoNucleicAcidDetection(tmpBean.getNucleicAcidDetection());
                            flag = true;
                            break;
                        }
                    }
                    if(!flag){
                        for (WebQuestionnaireEntity tmpBean : withInTwoDayList) {
                            if(null != tmpBean
                                    && NucleicAcidDetectionEnum.negative.getValue().equals(tmpBean.getNucleicAcidDetection())){
                                LocalDate twoDayAgoDate = LocalDate.parse(date,DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                                Boolean ifDateOK = LocalDateTime.ofInstant(tmpBean.getSubTime().toInstant(), ZoneId.systemDefault()).toLocalDate()
                                        .isEqual(twoDayAgoDate);
                                if(ifDateOK){
                                    entity.setTwoDaysAgoNucleicAcidDetection(tmpBean.getNucleicAcidDetection());
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            //核酸如果阳性者，临床症状消失后（肌肉酸痛、咳嗽0分或1分、胸闷、呼吸困难0分或1分、乏力0分、腹泻0分）持续3天
            //最近一次核酸检测是阳性，查询到目前为止 是否有连续三天 （肌肉酸痛、咳嗽0分或1分、胸闷、呼吸困难0分或1分、乏力0分、腹泻0分）持续3天
            WebQuestionnaireEntity maxBean = list.stream()
                    .filter(obj -> obj.getReviewDiagnosis() != null
                            && obj.getReviewDiagnosis()
                            && null != obj.getSubTime()
                            && StringUtils.isNotBlank(obj.getNucleicAcidDetection())
                            && !obj.getNucleicAcidDetection().equals(NucleicAcidDetectionEnum.uncheck.getValue()))
                    .max(Comparator.comparing(WebQuestionnaireEntity::getSubTime)).orElse(null);
            if(maxBean == null){
                return;
            }
            entity.setRecentNucleicAcidDetection(maxBean.getNucleicAcidDetection());
            if(NucleicAcidDetectionEnum.Positive.getValue().equals(maxBean.getNucleicAcidDetection())){
                LocalDate thatDate = LocalDateTime.ofInstant(maxBean.getSubTime().toInstant(), ZoneId.systemDefault()).toLocalDate();
                long until = LocalDate.now().until(thatDate, ChronoUnit.DAYS);
                if(Math.abs(until) < 3){
                    entity.setIfNoSymptoms(false);
                }else{
                    boolean tmpFlag = calculateIfNoSymptoms(maxBean);
                    entity.setIfNoSymptoms(tmpFlag);
                }
            }
        }catch( Exception e){
            log.error("统计患者：[{}]核酸检测情况报错，传递的jsonStr:[{}],错误信息:[{}]",entity.getUserId(),currentLoopContent,e.getMessage());
            e.printStackTrace();
        }

    }


    /**
     * 最近一次核酸检测为阳性，判断之后时间是否有连续三天症状消失
     * @param maxBean
     */
    private Boolean calculateIfNoSymptoms(WebQuestionnaireEntity maxBean) {
        LocalDate thatDate = LocalDateTime.ofInstant(maxBean.getSubTime().toInstant(), ZoneId.systemDefault()).toLocalDate();
        String date = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault()).format(thatDate);
        Map<String, Object> queryMap = Maps.newHashMap();
        queryMap.put("subTimeSeven",date);
        queryMap.put("userId",maxBean.getUserId());
        queryMap.put("hospitalId",maxBean.getHospitalId());
        List<WebQuestionnaireEntity> list = webQuestionnaireDao.queryResentXDayData(queryMap);
        for (WebQuestionnaireEntity bean : list) {
            Boolean aBoolean = calculateFomula24(bean);
            if(aBoolean == null || !aBoolean){
                return false;
            }
        }
        return true;
    }

    /**
     * 传递一个对象，判断该对象对应的日期
     * （肌肉酸痛、咳嗽0分或1分、胸闷、呼吸困难0分或1分、乏力0分、腹泻0分） 满足 记录 1否则 记录0
     * @param bean
     * @return
     */
    private Boolean calculateFomula24(WebQuestionnaireEntity bean){
        if(null == bean || StringUtils.isBlank(bean.getSubContent()) || null == bean.getSubTime()) {
            return null;
        }
        try {
            JSONArray parse = (JSONArray) JSON.parse(bean.getSubContent());
            for (Object o : parse) {
                Map<String,Object> map = (Map<String,Object>)o;
                if(map.containsKey(Constant.content)){
                    String tmpKey = String.valueOf(map.get(Constant.content));
                    //咳嗽分数
                    if(Constant.cough.equals(tmpKey)){
                        if(map.containsKey(Constant.score)){
                            Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                            bean.setCoughScore(tmpScore);
                        }else{
                            bean.setCoughScore(null);
                        }
                    }
                    //腹泻分数
                    if(Constant.diarrhea.equals(tmpKey)){
                        if(map.containsKey(Constant.score)){
                            Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                            bean.setDiarrheaScore(tmpScore);
                        }else{
                            bean.setDiarrheaScore(null);
                        }
                    }

                    //呼吸困难的分数
                    if(Constant.difficultyBreathing.equals(tmpKey)){
                        if(map.containsKey(Constant.score)){
                            Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                            bean.setDifficultyBreathingScore(tmpScore);
                        }else{
                            bean.setDifficultyBreathingScore(null);
                        }
                    }

                    //乏力分值
                    if(Constant.fatigue.equals(tmpKey)){
                        if(map.containsKey(Constant.score)){
                            Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                            bean.setFatigueScore(tmpScore);
                        }else{
                            bean.setFatigueScore(null);
                        }
                    }
                    //肌肉酸痛分数
                    if(Constant.muscleAche.equals(tmpKey)){
                        if(map.containsKey(Constant.score)){
                            Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                            bean.setMuscleAcheScore(tmpScore);
                        }else{
                            bean.setMuscleAcheScore(null);
                        }
                    }
                }
            }
            if((bean.getMuscleAcheScore() == 1 || bean.getMuscleAcheScore() == 0)
                && (bean.getCoughScore() == 1 || bean.getCoughScore() == 0)
                && (bean.getDifficultyBreathingScore() == 1 || bean.getDifficultyBreathingScore() == 0)
                && bean.getDiarrheaScore() == 0 && bean.getFatigueScore() == 0
            ){
                return true;
            }else{
                return false;
            }
        }catch( Exception e){
            log.error("计算（肌肉酸痛、咳嗽0分或1分、胸闷、呼吸困难0分或1分、乏力0分、腹泻0分）数据时报错，" +
                    "数据id:[{}],传递的jsonStr:[{}],错误信息:[{}]",bean.getId(), bean.getSubContent(),e.getMessage());
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 当查询到患者最近一次CT结果是正常，并且距今7天了
     * @param lungCTResultDate
     * @param entity
     */
    private void prepareResentSevenDayData(String lungCTResultDate, WebQuestionnaireEntity entity) {
        Map<String, Object> queryMap = Maps.newHashMap();
        queryMap.put("subTimeSeven",lungCTResultDate);
        queryMap.put("userId",entity.getUserId());
        queryMap.put("hospitalId",entity.getHospitalId());
        List<WebQuestionnaireEntity> list = webQuestionnaireDao.queryResentXDayData(queryMap);
        if(list!=null && list.size() >= 7){
            for (WebQuestionnaireEntity tmpBean : list) {
                if(tmpBean.getScore() > 5){
                    entity.setIfCTNoSymptoms(false);
                    return;
                }
            }
            entity.setIfCTNoSymptoms(true);
        }
    }

	@Override
	public List<Map<String, Object>> constantly(Map<String, Object> params) {
		return webQuestionnaireDao.constantly(params);
	}

	@Override
	public List<Map<String, Object>> communityConstantly(Map<String, Object> params) {
		return webQuestionnaireDao.communityConstantly(params);
	}

}
