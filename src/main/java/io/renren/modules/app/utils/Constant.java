package io.renren.modules.app.utils;

import com.google.common.collect.Maps;
import io.renren.modules.app.EnumPack.VerbStatusEnum;

import java.util.Map;

/**
 * @author 50519
 * @description
 * @Date 2020/2/21 16:19
 */
public class Constant {

    //日常填写每个问题都有的规范字段
    public final static String content = "content";
    public final static String value = "value";
    public final static String type = "type";
    public final static String sort = "sort";
    public final static String id = "id";
    public final static String score = "score";
    public final static String input = "input";

    public final static Integer basic_score_max = 3;

    //以下是进行公式判别时，用到的字段
    //计算病程时，用到的发病日期 key，静态处理
//    public final static String diseaseday = "发病日期";
    public final static String diseaseday = "出现健康问题日期";
    public final static String bodyTemperature = "体温";
    public final static String mentalState = "精神状态";
    public final static String muscleAche = "肌肉酸痛";
    public final static String cough = "咳嗽现象";
    public final static String difficultyBreathing = "呼吸困难";
    public final static String fatigue = "乏力现象";
    public final static String diarrhea = "腹泻现象";
    public final static String asymptomaticCloseAtHome = "家中密切接触者有无症状";
    public final static String ReviewDiagnosis = "今日是否有复诊检查";
    // 复诊单词
    public final static String lungCTResult = "肺部CT诊断报告";
    public final static String lungCTResultDate = "肺部CT诊断报告-检查日期";
    public final static String cFydb = "C反应蛋白";
    public final static String pct = "PCT";
    public final static String NucleicAcidDetection = "咽拭子检测";
    public final static String whiteCell = "白细胞计数";
    public final static String eatCell = "嗜酸性粒细胞";
    public final static String lbCell = "淋巴细胞百分比";
    // 计算基础分值时用到的规定的key名
    public final static String age = "年龄";
    public final static String ifPregnancy = "是否妊娠期";
    public final static String ifBasicIllness = "是否有基础疾病";

    //流行病学风险评估使用
    public final static String ifCloseConcat = "近期有无明确接触史";
    public final static String healthStatus = "健康状况";
    //第一次诊断建议
    public final static String defaultFirstPaientInfo = "感谢您第一天的使用，我们根据变化的趋势给出诊断建议，所以明天测试才会有诊断建议的。\n " +
            "这个工具之所以能帮助到你，是基于数据真实连续的填报，变化的趋势是判断病情重要的标志，请您一定坚持";

    //轻症的诊断建议
    public final static String defaultMildPaientInfo = "请及时联系社区工作人员并前往医院就诊";
    //默认没有时的诊断建议
    public final static String defaultPaientInfo = "您现在的状态比较稳定，维持现有的治疗方案，请注意卧床休息";
    //默认没有时黄/红状态的管理端诊断建议
    public final static String defaultSeriousManageInfo = "当前患者需重点关注";
    //默认没有时。普通状态的建议
    public final static String defaultNormalManageInfo = "请持续保持对患者进行观察";

    //公式集指定KEY
    public final static String DiagnosticStatus = "diagnosticStatus"; // 患者级别
    public final static String ManageInfo = "manageInfo"; //管理端展示诊断建议
    public final static String SerialNumber = "serialNumber";  // 公式序号
    public final static String DecisionRules = "decisionRules"; // 判定规则
    public final static String PatientInfo = "patientInfo"; // 患者端展示信息
    public final static String Formula = "formula"; //判定公式
    public final static String CheckMark = "checkMark"; //检查标识
}
