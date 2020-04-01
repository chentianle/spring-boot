package io.renren.modules.generator.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.renren.common.exception.RRException;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.common.utils.R;
import io.renren.modules.app.EnumPack.IfBasicFilledEnum;
import io.renren.modules.app.EnumPack.RatingEnum;
import io.renren.modules.app.formula.RiskFormulaList;
import io.renren.modules.app.utils.Constant;
import io.renren.modules.generator.dao.WebBasicPatientInformationDao;
import io.renren.modules.generator.dao.WebUserDao;
import io.renren.modules.generator.entity.WebBasicPatientInformationEntity;
import io.renren.modules.generator.entity.WebUserEntity;
import io.renren.modules.generator.service.WebBasicPatientInformationService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service("webBasicPatientInformationService")
@Transactional
public class WebBasicPatientInformationServiceImpl
        extends ServiceImpl<WebBasicPatientInformationDao, WebBasicPatientInformationEntity>
        implements WebBasicPatientInformationService {

    private final static Logger log = LoggerFactory.getLogger(WebBasicPatientInformationServiceImpl.class);

    @Resource
    private WebBasicPatientInformationDao webBasicPatientInformationDao;

    @Resource
    private WebUserDao webUserDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WebBasicPatientInformationEntity> page = this.page(
                new Query<WebBasicPatientInformationEntity>().getPage(params),
                new QueryWrapper<WebBasicPatientInformationEntity>()
        );

        return new PageUtils(page);
    }





    @Override
    public R saveCustom(WebBasicPatientInformationEntity entity) throws ScriptException, IOException, TemplateException {
        if(null == entity) {
            return R.error("没有获取到患者流行病调查数据");
        }
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

//        R firstInputValidate = validateFirstInput(entity.getUserId());
//        if(firstInputValidate != null) return firstInputValidate;
        //计算患者的基础信息的分值
        calculatePatientInfoScore(entity);
        //调用流行病学风险评估接口，进行评估
        epidemiologyRating(entity);
        //根据用户ID查询基础信息ID
        getBasicIdByUserId(entity);
        Boolean flag = this.saveOrUpdate(entity);
        if(!flag) return R.error("保存或者更新患者基础信息失败");
        //更新用户表ifBasicFilled 为已填写
        R filledResult = updateBasicInfo(entity);
        if(filledResult != null) return filledResult;
        return R.ok();
    }

    @Override
    public WebBasicPatientInformationEntity queryGeneration(Map<String, Object> params) {
        return webBasicPatientInformationDao.queryGeneration(params);
    }

    @Override
    public WebBasicPatientInformationEntity queryTriggeringConditions(Map<String,Object> params) {
        return webBasicPatientInformationDao.queryTriggeringConditions(params);
    }


    /**
     * 根据用户ID查询基础信息ID
     * @param entity
     */
    private void getBasicIdByUserId(WebBasicPatientInformationEntity entity) {
        Long userId = entity.getUserId();
        LambdaQueryWrapper<WebBasicPatientInformationEntity> lq = Wrappers.lambdaQuery();
        lq.eq(WebBasicPatientInformationEntity::getUserId,entity.getUserId());
        List<WebBasicPatientInformationEntity> list = webBasicPatientInformationDao.selectList(lq);
        if(list !=null && list.size() > 1){
            throw new RRException("一位用户对应多条基础信息，请联系管理员处理");
        }
        if(list != null && list.size() == 1){
            entity.setId(list.get(0).getId());
        }
    }


    /**
     * 调用流行病学风险评估接口，评估患者是否处于风险状态
     * @param entity
     */
    private void epidemiologyRating(WebBasicPatientInformationEntity entity) throws ScriptException, IOException, TemplateException {
        //准备数据
        prepareData(entity);
        //计算风险等级
        calculateRisk(entity);
    }

    /**
     * //计算风险等级
     * @param entity
     */
    private void calculateRisk(WebBasicPatientInformationEntity entity) throws ScriptException, TemplateException, IOException {
        List<Map<String, Object>> formulaList = RiskFormulaList.initFormulaList();
        Boolean bool = false;
        for (Map<String, Object> tmpMap : formulaList) {
            Integer tmpStatus = Integer.valueOf(String.valueOf(tmpMap.get(Constant.DiagnosticStatus))); //判定等级
            String tmpSerialNumber = String.valueOf(tmpMap.get(Constant.SerialNumber)); //序号
            String tmpFormula = String.valueOf(tmpMap.get(Constant.Formula)); //公式
            String tmpDecisionRules = String.valueOf(tmpMap.get(Constant.DecisionRules)); //判定依据
            //运行计算
            boolean eval = getCalculateResult(entity, tmpSerialNumber, tmpFormula, tmpDecisionRules);
            if(eval){
                bool = true;
                //更新流行病触发条件
                entity.setTriggeringConditions(tmpDecisionRules);
                //
                updateWebUserRating(entity, tmpStatus);
                break;
            }
        }
        if(!bool){
            updateWebUserRating(entity, RatingEnum.normal.getValue());
        }

    }


    /**
     * 更新用户的rating
     * @param entity
     * @param tmpStatus
     */
    private void updateWebUserRating(WebBasicPatientInformationEntity entity, Integer tmpStatus) {
        //更新用户表的状态
        WebUserEntity we = new WebUserEntity();
        we.setUserId(entity.getUserId());
        we.setRating(tmpStatus);
        int i = webUserDao.updateById(we);
        if (i != 1) {
            log.error("根据流行病数据，更新用户的风险状态失败,用户ID:{}", entity.getUserId());
            throw new RRException("根据流行病数据，更新用户的风险状态失败");
        }
    }


    /**
     * 为风险评估准备基础数据
     * @param basicBean
     */
    private void prepareData(WebBasicPatientInformationEntity basicBean) {
        if(StringUtils.isBlank(basicBean.getSubContent())) {
            log.error("计算患者流行病风险评估等级时，基础数据不全，患者ID：{}，社区ID：{}，传递的jsonStr:[{}]"
                    ,basicBean.getUserId(), basicBean.getHospitalId(),basicBean.getSubContent());
        }

        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            JSONArray parse = (JSONArray) JSON.parse(basicBean.getSubContent());
            for (Object o : parse) {
                Map<String,Object> map = (Map<String,Object>)o;
                if(map.containsKey(Constant.content)
                        && Constant.ifCloseConcat.equals(String.valueOf(map.get(Constant.content)))
                        && map.containsKey(Constant.value)) {
                    String tmpValue = String.valueOf(map.get(Constant.value));
                    basicBean.setIfCloseConcatStr(tmpValue);
                }

                if(map.containsKey(Constant.content)
                        && Constant.healthStatus.equals(String.valueOf(map.get(Constant.content)))
                        && map.containsKey(Constant.value)) {
                    String tmpValue = String.valueOf(map.get(Constant.value));
                    if(StringUtils.isNotBlank(tmpValue)){
                        tmpValue = tmpValue.replace("[","").replace("]","").replace("\"","");
                    }
                    basicBean.setHealthStatusStr(tmpValue);
                }

                if(map.containsKey(Constant.content)
                        && Constant.diseaseday.equals(String.valueOf(map.get(Constant.content)))
                        && map.containsKey(Constant.value)){
                    String diseaseDateStr = String.valueOf(map.get(Constant.value));
                    LocalDate startDate = LocalDate.parse(diseaseDateStr, dtf);
                    Long until = Math.abs(LocalDate.now().until(startDate, ChronoUnit.DAYS));
                    basicBean.setDiseaseDay(until.intValue());
                }

                //组织年龄段数据
                if(map.containsKey(Constant.content)
                        && Constant.age.equals(String.valueOf(map.get(Constant.content)))
                        && map.containsKey(Constant.value)){
                    String tmpValue = String.valueOf(map.get(Constant.value));
                    basicBean.setGeneration(tmpValue);
                }
            }
        }catch( Exception e){
            log.error("计算患者流行病风险评估等级时，患者ID：{}，社区ID：{}，传递的jsonStr:[{}],错误信息:[{}]"
                    ,basicBean.getUserId(), basicBean.getHospitalId(),basicBean.getSubContent(),e.getMessage());
            e.printStackTrace();
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
    private boolean getCalculateResult(WebBasicPatientInformationEntity entity,
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
     * 更新用户表ifBasicFilled 为已填写
     * @param entity
     * @return
     */
    private R updateBasicInfo(WebBasicPatientInformationEntity entity) {
        LambdaQueryWrapper<WebUserEntity> lq = Wrappers.lambdaQuery();
        lq.eq(WebUserEntity::getUserId,entity.getUserId());
        WebUserEntity bean = new WebUserEntity();
        bean.setIfBasicFilled(IfBasicFilledEnum.filled.getValue());
        int update = webUserDao.update(bean, lq);
        if(update <= 0 ) {
            return R.error("更新患者的是否填写完成基础信息失败");
        }else{
            return null;
        }
    }


    private R validatePaientExistOrNot(WebBasicPatientInformationEntity entity) {
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
     * 校验是否是第一次填写
     * @param userId
     * @return
     */
    private R validateFirstInput(Long userId) {
        LambdaQueryWrapper<WebBasicPatientInformationEntity> lq = Wrappers.lambdaQuery();
        lq.eq(WebBasicPatientInformationEntity::getUserId,userId);
        List<WebBasicPatientInformationEntity> list = webBasicPatientInformationDao.selectList(lq);
        if(list.size() > 0) return R.error("您已填写过患者基础信息，请不要重复填写");
        return null;
    }

    /**
     * 计算患者的基础信息分值
     * @param entity
     */
    private void calculatePatientInfoScore(WebBasicPatientInformationEntity entity) {
        String subContent = entity.getSubContent();
        try {
            JSONArray parse = (JSONArray) JSON.parse(subContent);
            Integer score = 0;//分值
            for (Object o : parse) {
                Map<String,Object> map = (Map<String,Object>)o;
                if(map.containsKey(Constant.content)){
                    if("姓名".equals(String.valueOf(map.get(Constant.content))) && map.containsKey(Constant.value)){
                        String tmpName = String.valueOf(map.get(Constant.value));
                        LambdaQueryWrapper<WebUserEntity> updateLq = Wrappers.lambdaQuery();
                        updateLq.eq(WebUserEntity::getUserId,entity.getUserId());
                        WebUserEntity we = new WebUserEntity();
                        we.setUsername(tmpName);
                        int update = webUserDao.update(we, updateLq);
                        if(update != 1) log.error("用户id[{}],更新用户姓名为[{}]，失败",entity.getUserId(),tmpName);
                    }
                }
                if(map.containsKey(Constant.score)){
                    Integer tmpScore = Integer.valueOf(String.valueOf(map.get(Constant.score)));
                    score += tmpScore;
                }
            }
            if(score > Constant.basic_score_max) {
                log.error("计算患者基础情况分值时超过最大值[{}]，传递的jsonStr:[{}]", Constant.basic_score_max,entity.getSubContent());
                score = Constant.basic_score_max;
            }
            entity.setScore(score);
        }catch( Exception e){
            log.error("计算患者基础情况分值时报错，传递的jsonStr:[{}]",entity.getSubContent());
        }
    }
}
