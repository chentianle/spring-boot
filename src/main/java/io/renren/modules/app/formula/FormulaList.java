package io.renren.modules.app.formula;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.renren.modules.app.EnumPack.CheckMarkStatusEnum;
import io.renren.modules.app.EnumPack.LungCTDiagnosisReportEnum;
import io.renren.modules.app.EnumPack.NucleicAcidDetectionEnum;
import io.renren.modules.app.EnumPack.VerbStatusEnum;
import io.renren.modules.app.utils.Constant;

import java.util.List;
import java.util.Map;

/**
 * @author 50519
 * @description
 * @Date 2020/2/21 19:19
 */
public class FormulaList {

    /**
     * 初始化公式集合
     * @return
     */
    public static List<Map<String, Object>> initFormulaList(){
        List<Map<String, Object>> formulaList = Lists.newArrayList();

        /**
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         *  * * 数字类型可能为空，给与默认值应远离 分数的最大值和最小值；* * * * * *
         * * *     当数值为空时，给与的默认值应该让公式为假          * * * * * * *
         * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
         */

        //0<=今日分数-昨日分数<=2 || 0<昨日分数-今日分数<=4
        Map<String, Object> map1 = initFormulaMap(VerbStatusEnum.normal.getValue(),
                Constant.defaultNormalManageInfo,
                1,CheckMarkStatusEnum.NoNeed.getValue(),
                " 病情稳定或好转\n a. 总分较昨日减少，减少幅度，小于等于4分\n b. 总分较昨日增加，增加幅度，小于等于2分 ",
                "您现在的状态比较稳定，维持现有的治疗方案",
                "${(score)!500}-${(yesterdayScore)!100} >= 0 && ${(score)!500}-${(yesterdayScore)!100} <= 2 " +
                        "|| ${(yesterdayScore)!100}-${(score)!500} > 0 && ${(yesterdayScore)!100}-${(score)!500} <= 4"
        );
        formulaList.add(map1);

        //昨日分数-今日分数 >=5
        Map<String, Object> map2 = initFormulaMap(VerbStatusEnum.normal.getValue(),
                Constant.defaultNormalManageInfo,
                2, CheckMarkStatusEnum.NoNeed.getValue(),
                " 患者病情趋于好转\n 总分比昨日减少，减少的幅度，大于等于5分 ",
                "您的病情正在趋于好转，请继续加油!",
                "${(yesterdayScore)!-100}-${(score)!500} >= 5"
        );
        formulaList.add(map2);

        //今日分数-昨日分数=3||4
        Map<String, Object> map3 = initFormulaMap(VerbStatusEnum.yellow.getValue(),
                Constant.defaultSeriousManageInfo,
                3, CheckMarkStatusEnum.NoNeed.getValue(),
                " 患者评分较昨日增加3分、4分 ",
                "请4小时后加一次自测;",
                "${(score)!500}-${(yesterdayScore)!100} == 3 || ${(score)!500}-${(yesterdayScore)!100} == 4 "
        );
        formulaList.add(map3);

        //病程天数>=4&& 发热
        Map<String, Object> map4 = initFormulaMap(VerbStatusEnum.yellow.getValue(),
                Constant.defaultSeriousManageInfo,
                4, CheckMarkStatusEnum.NoNeed.getValue(),
                " 病程第4天还存在发热\n 大于等于4天 ",
                "体温检测变成6小时一次;",
                "${(diseaseDay)!-100}>= 4 && ${(bodyTemperatureScore)!-100}>=1 "
        ); //根据2月25日要求修改
        formulaList.add(map4);

        //病程天数>=5 && 精神状态分值==2 根据需求讨论 精神状态分值为合计分值
        Map<String, Object> map5 = initFormulaMap(VerbStatusEnum.yellow.getValue(),
                Constant.defaultSeriousManageInfo,
                5, CheckMarkStatusEnum.NoNeed.getValue(),
                " 根据病程计算，大于等于第五天精神状态仍在2分 ",
                "请您保证充足睡眠，放松心情，继续观察。如果明天症状持续，看医生;",
                "${(diseaseDay)!-100}>= 5 && ${(mentalStateScore)!-100}==2 "
        ); //根据2月25日要求修改 精神状态改为2分，不在是合计，而是当天分数
        formulaList.add(map5);

        //最近四天肌肉酸痛>=1，病程大于等于第四天
        Map<String, Object> map6 = initFormulaMap(VerbStatusEnum.yellow.getValue(),
                Constant.defaultSeriousManageInfo,
                6, CheckMarkStatusEnum.NoNeed.getValue(),
                " 肌肉酸痛（大于等于1分）持续4天及以上 ",
                "注意休息，减少活动。继续观察体征变化;",
                "${(diseaseDay)!-100}>= 4 && ${(muscleAcheScore)!-100}>= 1 " +
                        "&& ${(yesterdatmuscleAcheScore)!-100}>=1 && ${(beforeYesmuscleAcheScore)!-100}>=1 " +
                        "&& ${(greatDayBeforemuscleAcheScore)!-100}>=1 "
        );
        formulaList.add(map6);

        //病程天数>=7 && 今天呼吸困难分数>=1
        Map<String, Object> map7 = initFormulaMap(VerbStatusEnum.yellow.getValue(),
                Constant.defaultSeriousManageInfo,
                7, CheckMarkStatusEnum.NoNeed.getValue(),
                " 根据病程计算，大于等于第7天仍有呼吸困难大于等于1分 ",
                "注意呼吸频率，心跳，如果呼吸频率超过24次/分，HR 超过100次/分，见医生;",
                "${(diseaseDay)!-100}>= 7 && ${(difficultyBreathingScore)!-100}>=1 "
        ); //根据2月25日要求修改 只看当天
        formulaList.add(map7);

        //病程天数>=8 && 自觉症状无好转 总分10分，包括年龄评分、妊娠评分、基础疾病评分  根据2月25日要求修改 大于等于10分
        Map<String, Object> map8 = initFormulaMap(VerbStatusEnum.yellow.getValue(),
                Constant.defaultSeriousManageInfo,
                8, CheckMarkStatusEnum.NoNeed.getValue(),
                " 病程第8天自觉症状无好转\n 总分10分，包括年龄评分、妊娠评分、基础疾病评分 ",
                "注意休息，减少活动。继续观察体征变化;",
                "${(diseaseDay)!-100}>= 8 && ${(score)!-100}+${(basicScore)!-200}>=10 "
        );
        formulaList.add(map8);

       /*
        //患者基础状况表-肺部CT诊断报告：单肺斑片影
        Map<String, Object> map9 = initFormulaMap(VerbStatusEnum.yellow.getValue(),
        Constant.defaultSeriousManageInfo,
        9, CheckMarkStatusEnum.NoNeed.getValue(),
        " 患者基础状况表-肺部CT诊断报告：单肺斑片影； ",
        Constant.defaultPaientInfo,
        "\"${(lungCTResult)!''}\".equals(\""+ LungCTDiagnosisReportEnum.pgwspc.getValue()+"\") "
        );
        formulaList.add(map9);
        */

        //复诊选项-CT检测：从上次检查结果的“正常”到这次检查结果的“单发磨玻璃影”；
        Map<String, Object> map10 = initFormulaMap(VerbStatusEnum.yellow.getValue(),
                "密切关注患者影像学进展，患者有重症倾向的可能",
                10, CheckMarkStatusEnum.NoNeed.getValue(),
                "复诊-肺部CT诊断报告:从上次检查结果的“正常”到这次检查结果的“单发磨玻璃影”  ",
                 "密切关注您的情况;",
                "<#if reviewDiagnosis??>${reviewDiagnosis?string('true','false')}==true<#else>false</#if> " +
                        "&&  \"${(lungCTResult)!''}\".equals(\""+LungCTDiagnosisReportEnum.singleShotGroundGlass.getValue()+"\") " +
                        "&& \"${(lastLungCTResult)!''}\".equals(\""+LungCTDiagnosisReportEnum.normal.getValue()+"\")"
        );
        formulaList.add(map10);

        //复诊选项-CT检测：从“单发磨玻璃影；” 到“多发磨玻璃影；”
        Map<String, Object> map11 = initFormulaMap(VerbStatusEnum.yellow.getValue(),
                "密切关注患者影像学进展，患者有重症倾向的可能",
                11, CheckMarkStatusEnum.NoNeed.getValue(),
                "复诊-肺部CT诊断报告:从 ” 单发磨玻璃影；” 到 “ 多发磨玻璃影；”  ",
                "密切关注您的情况;",
                "<#if reviewDiagnosis??>${reviewDiagnosis?string('true','false')}==true<#else>false</#if>" +
                        "&&  \"${(lungCTResult)!''}\".equals(\""+LungCTDiagnosisReportEnum.multipleShotsOfGroundGlass.getValue()+"\") " +
                        "&& \"${(lastLungCTResult)!''}\".equals(\""+LungCTDiagnosisReportEnum.singleShotGroundGlass.getValue()+"\")"
        );
        formulaList.add(map11);

        //复诊选项-CT检测：：单肺斑片影；
        Map<String, Object> map12 = initFormulaMap(VerbStatusEnum.yellow.getValue(),
                "密切关注患者影像学进展，患者有重症倾向的可能",
                12, CheckMarkStatusEnum.NoNeed.getValue(),
                " 复诊-肺部CT诊断报告:“ 单肺斑片影 ” ",
                 "密切关注您的情况;",
                "<#if reviewDiagnosis??>${reviewDiagnosis?string('true','false')}==true<#else>false</#if> " +
                        "&&  \"${(lungCTResult)!''}\".equals(\""+LungCTDiagnosisReportEnum.pgwspc.getValue()+"\") " +
                        "&&  \"${(lastLungCTResult)!''}\".equals(\"\")"
        );
        formulaList.add(map12);

        //今日分数-昨日分数>=5
        Map<String, Object> map13 = initFormulaMap(VerbStatusEnum.red.getValue(),
                Constant.defaultSeriousManageInfo,
                13, CheckMarkStatusEnum.NoNeed.getValue(),
                " 评分较昨日增加5分及以上 ",
                "您现在的状态不好，请立即联系您的医生",
                "${(score)!100}-${(yesterdayScore)!500} >= 5"
        );
        formulaList.add(map13);

        // 严格按照字面意思 1001
        // 再次出现发热：患者出现体温大于等于1分的情况（发热）、接下来患者出现体温等于0分的情况持续两天（不发热），患者出现体温大于等于1分的情况（再次发热）
        Map<String, Object> map14 = initFormulaMap(VerbStatusEnum.red.getValue(),
                Constant.defaultSeriousManageInfo,
                14, CheckMarkStatusEnum.NoNeed.getValue(),
                " 再次出现发热：\n 患者出现体温大于等于1分的情况（发热）、接下来患者出现体温等于0分的情况持续两天（不发热），患者出现体温大于等于1分的情况（再次发热） ",
                "您现在的状态出现了变化，请立即联系您的医生",
                "${(bodyTemperatureScore)!-100}>=1 && ${(yesterdatbodyTemperatureScore)!-100}==0 " +
                        "&& ${(beforeYesbodyTemperatureScore)!-100}==0 && ${(greatDayBeforebodyTemperatureScore)!-100}>=1 "
        );
        formulaList.add(map14);

        /*
        //患者基础状况表-肺部CT诊断报告：双肺斑片影
        Map<String, Object> map15 = initFormulaMap(VerbStatusEnum.red.getValue(),
        "患者有明显的重症倾向，建议转诊至定点医院",
        15, CheckMarkStatusEnum.NoNeed.getValue(),
        " 患者基础状况表-肺部CT诊断报告：\n “ 双肺斑片影 ” ",
        Constant.defaultPaientInfo,
        "\"${(lungCTResult)!''}\".equals(\""+LungCTDiagnosisReportEnum.dggsobwbis.getValue()+"\")"
        );
        formulaList.add(map15);

        //患者基础状况表-肺部CT诊断报告：双肺实变
        Map<String, Object> map16 = initFormulaMap(VerbStatusEnum.red.getValue(),
        "患者有明显的重症倾向，建议转诊至定点医院",
        16, CheckMarkStatusEnum.NoNeed.getValue(),
        " 患者基础状况表-肺部CT诊断报告：\n “ 双肺实变  ” ",
        Constant.defaultPaientInfo,
        "\"${(lungCTResult)!''}\".equals(\""+LungCTDiagnosisReportEnum.lcotlwlit.getValue()+"\")");
        formulaList.add(map14);
        */

        //复诊选项-CT检测：从“正常”到“多发磨玻璃影；
        Map<String, Object> map17 = initFormulaMap(VerbStatusEnum.red.getValue(),
                "患者有明显的重症倾向，建议转诊至定点医院",
                17, CheckMarkStatusEnum.NoNeed.getValue(),
                " 复诊-肺部CT诊断报告：\n 从 “正常” 到 “多发磨玻璃影；” ",
                "您现在的状态出现了变化，请立即联系您的医生",
                "<#if reviewDiagnosis??>${reviewDiagnosis?string('true','false')}==true<#else>false</#if>" +
                        "&&  \"${(lungCTResult)!''}\".equals(\""+LungCTDiagnosisReportEnum.multipleShotsOfGroundGlass.getValue()+"\") " +
                        "&& \"${(lastLungCTResult)!''}\".equals(\""+LungCTDiagnosisReportEnum.normal.getValue()+"\") "
        );
        formulaList.add(map17);

        //复诊选项-CT检测：从“正常”到“单肺斑片影；
        Map<String, Object> map18 = initFormulaMap(VerbStatusEnum.red.getValue(),
                "患者有明显的重症倾向，建议转诊至定点医院",
                18, CheckMarkStatusEnum.NoNeed.getValue(),
                " 复诊-肺部CT诊断报告：\n 从 “正常” 到 “单肺斑片影；” ",
                "您现在的状态出现了变化，请立即联系您的医生",
                "<#if reviewDiagnosis??>${reviewDiagnosis?string('true','false')}==true<#else>false</#if>" +
                        "&&  \"${(lungCTResult)!''}\".equals(\""+LungCTDiagnosisReportEnum.pgwspc.getValue()+"\") " +
                        "&& \"${(lastLungCTResult)!''}\".equals(\""+LungCTDiagnosisReportEnum.normal.getValue()+"\") "
        );
        formulaList.add(map18);

        //复诊选项-CT检测：从“单发磨玻璃影；”到“单肺斑片影； ”
        Map<String, Object> map19 = initFormulaMap(VerbStatusEnum.red.getValue(),
                "患者有明显的重症倾向，建议转诊至定点医院",
                19, CheckMarkStatusEnum.NoNeed.getValue(),
                " 复诊-肺部CT诊断报告：\n 从 “单发磨玻璃影；” 到 “单肺斑片影；” ",
                "您现在的状态出现了变化，请立即联系您的医生",
                "<#if reviewDiagnosis??>${reviewDiagnosis?string('true','false')}==true<#else>false</#if>" +
                        "&&  \"${(lungCTResult)!''}\".equals(\""+LungCTDiagnosisReportEnum.pgwspc.getValue()+"\") " +
                        "&& \"${(lastLungCTResult)!''}\".equals(\""+LungCTDiagnosisReportEnum.singleShotGroundGlass.getValue()+"\") "
        );
        formulaList.add(map19);

        //复诊选项-CT检测：双肺斑片影
        Map<String, Object> map20 = initFormulaMap(VerbStatusEnum.red.getValue(),
                "患者有明显的重症倾向，建议转诊至定点医院",
                20, CheckMarkStatusEnum.NoNeed.getValue(),
                " 复诊-肺部CT诊断报告：\n  “双肺斑片影；” ",
                 "您现在的状态出现了变化，请立即联系您的医生",
                "<#if reviewDiagnosis??>${reviewDiagnosis?string('true','false')}==true<#else>false</#if> " +
                        "&&  \"${(lungCTResult)!''}\".equals(\""+LungCTDiagnosisReportEnum.dggsobwbis.getValue()+"\")"
        );
//        "${reviewDiagnosis???c} &&  \"${(lungCTResult)!''}\".equals(\""+LungCTDiagnosisReportEnum.dggsobwbis.getValue()+"\") &&  \"${(lastLungCTResult)!''}\".equals(\"\")"
        formulaList.add(map20);

        //复诊选项-CT检测：双肺实变
        Map<String, Object> map21 = initFormulaMap(VerbStatusEnum.red.getValue(),
                "该患者的状态为红色预警状态，请您优先注意",
                21, CheckMarkStatusEnum.NoNeed.getValue(),
                " 复诊-肺部CT诊断报告：\n  “双肺实变；” ",
                 "您现在的状态不适合使用这个工具，请立即联系您的医生",
                "<#if reviewDiagnosis??>${reviewDiagnosis?string('true','false')}==true<#else>false</#if> " +
                        "&&  \"${(lungCTResult)!''}\".equals(\""+LungCTDiagnosisReportEnum.lcotlwlit.getValue()+"\")"
        );
//        "${reviewDiagnosis???c} &&  \"${(lungCTResult)!''}\".equals(\""+LungCTDiagnosisReportEnum.lcotlwlit.getValue()+"\") &&  \"${(lastLungCTResult)!''}\".equals(\"\")"
        formulaList.add(map21);

        //第一张CT片无症状时的日期到目前连续7天总分评<=5分
        Map<String, Object> map22 = initFormulaMap(VerbStatusEnum.fourthState.getValue(),
                "第7天复查，复查CT同时复查血常规",
                22, CheckMarkStatusEnum.Need.getValue(),
                " 距离第一张CT片，无症状时\n总分5分及以下，持续7天 ",
                Constant.defaultPaientInfo,
                "<#if firstCTStatus??>${firstCTStatus?string('true','false')}==true<#else>false</#if>" +
                        "&&  ${(firstCTDays)!-100} >= 7 " +
                        "&&  <#if ifCTNoSymptoms??>${ifCTNoSymptoms?string('true','false')}==true<#else>false</#if>"
        );
        formulaList.add(map22);

        //(发烧>=1 || 呼吸困难>=1 || 乏力>=1 || 体温>=1分 && (其他项>=2)持续5天
        Map<String, Object> map23 = initFormulaMap(VerbStatusEnum.fourthState.getValue(),
                "第5天，复查CT同时复查血常规",
                23, CheckMarkStatusEnum.Need.getValue(),
                " 症状持续：\n出现（发烧，呼吸困难、乏力、体温）体温1分及以上，其他某一项2分及以上，持续5天 ",
                Constant.defaultPaientInfo,
                "(${(bodyTemperatureScore)!-100}>=1 || ${(difficultyBreathingScore)!-100} >= 1 || ${(fatigueScore)!-100} >= 1 || ${(mentalStateScore)!-100} >=2 || ${(muscleAcheScore)!-100}>=2 || ${(coughScore)!-100}>=2 || ${(diarrheaScore)!-100}>=2)" +
                "&& (${(yesterdatbodyTemperatureScore)!-100}>=1 || ${(yesterdatDifficultyBreathingScore)!-100} >= 1 || ${(yesterdatFatigueScore)!-100} >= 1 || ${(yesterdatMentalStateScore)!-100} >=2 || ${(yesterdatmuscleAcheScore)!-100}>=2 || ${(yesterdatCoughScore)!-100}>=2 || ${(yesterdatDiarrheaScore)!-100}>=2) " +
                "&& (${(beforeYesbodyTemperatureScore)!-100}>=1 || ${(beforeYesDifficultyBreathingScore)!-100} >= 1 || ${(beforeYesFatigueScore)!-100} >= 1 || ${(beforeYesMentalStateScore)!-100} >=2 || ${(beforeYesmuscleAcheScore)!-100}>=2 || ${(beforeYesCoughScore)!-100}>=2 || ${(beforeYesDiarrheaScore)!-100}>=2) " +
                "&& (${(greatDayBeforebodyTemperatureScore)!-100}>=1 || ${(greatDayBeforeDifficultyBreathingScore)!-100} >= 1 || ${(greatDayBeforeFatigueScore)!-100} >= 1 || ${(greatDayBeforeMentalStateScore)!-100} >=2 || ${(greatDayBeforemuscleAcheScore)!-100}>=2 || ${(greatDayBeforeCoughScore)!-100}>=2 || ${(greatDayBeforeDiarrheaScore)!-100}>=2) " +
                "&& (${(fourDaysAgoBodyTemperatureScore)!-100}>=1 || ${(fourDaysAgoDifficultyBreathingScore)!-100} >= 1 || ${(fourDaysAgoFatigueScore)!-100} >= 1 || ${(fourDaysAgoMentalStateScore)!-100} >=2 || ${(fourDaysAgoMuscleAcheScore)!-100}>=2 || ${(fourDaysAgoCoughScore)!-100}>=2 || ${(fourDaysAgoDiarrheaScore)!-100}>=2) "
        );
        formulaList.add(map23);

        //并且核酸检测=阳性 && （肌肉酸痛=0,1 && 咳嗽=0,1 && 胸闷=0,1 &&呼吸困难=0,1&& 乏力=0 && 腹泻=0）持续3天
        Map<String, Object> map24 = initFormulaMap(VerbStatusEnum.fourthState.getValue(),
                "安排复查核酸",
                24, CheckMarkStatusEnum.Need.getValue(),
                " 核酸如果阳性者，临床症状消失后\n（肌肉酸痛、咳嗽0分或1分、胸闷、呼吸困难0分或1分、乏力0分、腹泻0分）\n 持续3天 ",
                Constant.defaultPaientInfo,
                "\"${(recentNucleicAcidDetection)!''}\".equals(\""+ NucleicAcidDetectionEnum.Positive.getValue() +"\") " +
                        "&& <#if ifNoSymptoms??>${ifNoSymptoms?string('true','false')}==true<#else>false</#if>"
        );
        formulaList.add(map24);

        //进行了复检 并且核酸检测=阴性 && 48小时后核酸检测=阴性
        Map<String, Object> map25 = initFormulaMap(VerbStatusEnum.fourthState.getValue(),
                "仍阴性，可以解除隔离",
                25, CheckMarkStatusEnum.NoNeed.getValue(),
                " 咽拭子检测阴性，48小时后再复查仍阴性 ",
                Constant.defaultPaientInfo,
                "<#if reviewDiagnosis??>${reviewDiagnosis?string('true','false')}==true<#else>false</#if>" +
                        "&& \"${(nucleicAcidDetection)!''}\".equals(\""+ NucleicAcidDetectionEnum.negative.getValue()+"\") " +
                        "&&  \"${(twoDaysAgoNucleicAcidDetection)!''}\".equals(\""+ NucleicAcidDetectionEnum.negative.getValue()+"\")"
        );
        formulaList.add(map25);

        //进行了复检 并且本次核酸检测=阴性 && 上次核酸检测为阳性或者没有 则提示 患者本次核酸检测阴性，请提醒48小时后再次进行核酸检测
        Map<String, Object> map26 = initFormulaMap(VerbStatusEnum.fourthState.getValue(),
                "患者本次核酸检测阴性，请48小时后再次进行核酸检测",
                26, CheckMarkStatusEnum.Need.getValue(),
                " 患者本次核酸检测阴性，之前没有核酸检测或者上一次核酸检测为阳性",
                Constant.defaultPaientInfo,
                "<#if reviewDiagnosis??>${reviewDiagnosis?string('true','false')}==true<#else>false</#if>" +
                        "&& \"${(nucleicAcidDetection)!''}\".equals(\""+ NucleicAcidDetectionEnum.negative.getValue()+"\") " +
                        "&&  (\"${(lastTimeNucleicAcidDetection)!''}\".equals(\""+ NucleicAcidDetectionEnum.Positive.getValue()+"\") " +
                        "|| \"${(lastTimeNucleicAcidDetection)!''}\".equals(\"\"))"
        );
        formulaList.add(map26);

        return formulaList;
    }

    /**
     *
     * @param verbStatus 诊断分级
     * @param manageInfo 管理端展示信息
     * @param serialNumber 序号
     * @param checkMarkStatus 是否需要复查标识
     * @param decisionRules 判定规则
     * @param patientInfo 患者端展示信息
     * @param formula 实际公式
     * @return
     */
    private static Map<String, Object> initFormulaMap(Integer verbStatus, String manageInfo, Integer serialNumber,
                                                Integer checkMarkStatus, String decisionRules, String patientInfo,
                                                String formula){
        Map<String, Object> templateMap = Maps.newHashMap();
        templateMap.put(Constant.DiagnosticStatus, verbStatus);//诊断分级
        templateMap.put(Constant.ManageInfo,manageInfo); //管理端展示信息
        templateMap.put(Constant.SerialNumber,serialNumber); //序号
        templateMap.put(Constant.CheckMark, checkMarkStatus); //检查标识
        templateMap.put(Constant.DecisionRules,decisionRules); //判定规则
        templateMap.put(Constant.PatientInfo,patientInfo); //患者端展示信息
        templateMap.put(Constant.Formula, formula);
        return templateMap;
    }
}
