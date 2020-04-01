package io.renren.modules.app.formula;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.renren.modules.app.EnumPack.*;
import io.renren.modules.app.utils.Constant;

import java.util.List;
import java.util.Map;

/**
 * @author 50519
 * @description
 * @Date 2020/2/21 19:19
 */
public class RiskFormulaList {

    /**
     * 初始化公式集合
     * @return
     */
    public static List<Map<String, Object>> initFormulaList(){
        List<Map<String, Object>> formulaList = Lists.newArrayList();

       /* "近期有无明确接触史：
        近期疫情严重地区旅行史或居住史
        或者 曾接触过疫情严重地区发热伴有呼吸道症状的患者
        或者 曾与确诊患者近距离接触
        或者 居住地或工作场所中有多人发病"	风险
        "近期有无明确接触史：曾去过人群密集场所
        并且 健康状况选择 发热或者呼吸困难"	风险
        出现健康问题日期距离填表日期大于等于5天 并且 健康状况选择发热	风险
*/

        //近期有无明确接触史==近期疫情严重地区旅行史或居住史 风险
        Map<String, Object> map1 = initFormulaMap(
                RiskStatusEnum.hasRisk.getValue(),
                1,
                "近期疫情严重地区旅行史或居住史",
                " \"${(ifCloseConcatStr)!''}\".equals(\""+ClearContactHistoryEnum.TravelOrReside.getValue()+"\") "
        );
        formulaList.add(map1);

        //近期有无明确接触史==曾接触过疫情严重地区发热伴有呼吸道症状的患者 风险
        Map<String, Object> map2 = initFormulaMap(
                RiskStatusEnum.hasRisk.getValue(),
                2,
                "曾接触过疫情严重地区发热伴有呼吸道症状的患者",
                " \"${(ifCloseConcatStr)!''}\".equals(\""+ClearContactHistoryEnum.ContactPatient.getValue()+"\") "
        );
        formulaList.add(map2);

        //近期有无明确接触史==曾与确诊患者近距离接触 风险
        Map<String, Object> map3 = initFormulaMap(
                RiskStatusEnum.hasRisk.getValue(),
                3,
                "曾与确诊患者近距离接触",
                " \"${(ifCloseConcatStr)!''}\".equals(\""+ClearContactHistoryEnum.CloseContact.getValue()+"\") "
        );
        formulaList.add(map3);

        //近期有无明确接触史==居住地或工作场所中有多人发病 风险
        Map<String, Object> map4 = initFormulaMap(
                RiskStatusEnum.hasRisk.getValue(),
                4,
                "居住地或工作场所中有多人发病",
                " \"${(ifCloseConcatStr)!''}\".equals(\""+ClearContactHistoryEnum.MultipleOnset.getValue()+"\") "
        );
        formulaList.add(map4);


        //近期有无明确接触史：曾去过人群密集场所 并且 健康状况选择 发热或者呼吸困难 风险
        Map<String, Object> map5 = initFormulaMap(
                RiskStatusEnum.hasRisk.getValue(),
                5,
                "曾去过人群密集场所并且发热或者呼吸困难",
                " \"${(ifCloseConcatStr)!''}\".equals(\""+ClearContactHistoryEnum.Crowded.getValue()+"\") " +
                        " && ((\"${(healthStatusStr)!''}\".indexOf(\""+HealthStatusEnum.Fever.getValue()+"\") >= 0) " +
                        " || (\"${(healthStatusStr)!''}\".indexOf(\""+HealthStatusEnum.DifficultyBreathing.getValue()+"\") >= 0)) "
        );
        formulaList.add(map5);

        //出现健康问题日期距离填表日期大于等于5天 并且 健康状况选择发热 风险
        Map<String, Object> map6 = initFormulaMap(
                RiskStatusEnum.hasRisk.getValue(),
                6,
                "出现健康问题日期距离填表日期大于等于5天并且发热",
                " ${(diseaseDay)!-100}>= 5 " +
                        " && \"${(healthStatusStr)!''}\".equals(\""+HealthStatusEnum.Fever.getValue()+"\") "
        );
        formulaList.add(map6);
        return formulaList;
    }

    /**
     *
     * @param verbStatus 诊断分级
     //* @param manageInfo 管理端展示信息
     * @param serialNumber 序号
     //* @param checkMarkStatus 是否需要复查标识
     * @param decisionRules 判定规则
     //* @param patientInfo 患者端展示信息
     * @param formula 实际公式
     * @return
     */
    private static Map<String, Object> initFormulaMap(Integer verbStatus, Integer serialNumber,
                                                String decisionRules, String formula){
        Map<String, Object> templateMap = Maps.newHashMap();
        templateMap.put(Constant.DiagnosticStatus, verbStatus);//诊断分级
        //templateMap.put(Constant.ManageInfo,manageInfo); //管理端展示信息
        templateMap.put(Constant.SerialNumber,serialNumber); //序号
        //templateMap.put(Constant.CheckMark, checkMarkStatus); //检查标识
        templateMap.put(Constant.DecisionRules,decisionRules); //判定规则
        //templateMap.put(Constant.PatientInfo,patientInfo); //患者端展示信息
        templateMap.put(Constant.Formula, formula);
        return templateMap;
    }
}
