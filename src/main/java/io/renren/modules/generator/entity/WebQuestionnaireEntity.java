package io.renren.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 *
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-02-16 16:08:06
 */
@Data
@TableName("web_questionnaire")
public class WebQuestionnaireEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Long id;
	/**
	 *  提交时间
	 */
	private Date subTime;
	/**
	 *
	 */
	private Integer subStatus;
	/**
	 *  提交问卷内容
	 */
	private String subContent;
	/**
	 *  评分
	 */
	private Integer score;


	/**
	 * 用户ID
	 */
	private Long userId;

	/**
	 * 医院ID
	 */

	private Long hospitalId;


	private Integer handleStatus; //'0:未处理，1：已处理'

	private Integer diseaseDay;// '病程天数'

	private Integer verbStatus;// -1:忽略，0：普通状态，1：黄色状态，2:红色状态

	private Integer originalVerbStatus;//原始状态，0：普通状态，1：黄色状态，2:红色状态

	private String	nounValue; // '患者概要情况'

	private String diagnosisValue; // 诊断概要建议

	@TableField(exist = false)
	private int number;//导出时序号

	@TableField(exist = false)
	private String handleStatusStr; //'0:未处理，1：已处理'

	@TableField(exist = false)
	private String username;

	@TableField(exist = false)
	private String hospitalName;

	@TableField(exist = false)
	private String mobile;

	/**
	 *  昨日评分
	 */
	@TableField(exist = false)
	private Integer yesterdayScore;

	/**
	 *  最近一次CT 是否无症状
	 */
	@TableField(exist = false)
	private boolean firstCTStatus;

	/**
	 *  最近一次CT无症状到今天的时间
	 */
	@TableField(exist = false)
	private Integer firstCTDays;


	/**
	 *  今天体温分数
	 */
	@TableField(exist = false)
	private Integer bodyTemperatureScore;

	/**
	 *  昨天体温分数
	 */
	@TableField(exist = false)
	private Integer yesterdatbodyTemperatureScore;

	/**
	 *  前天体温分数
	 */
	@TableField(exist = false)
	private Integer beforeYesbodyTemperatureScore;


	/**
	 *  大前天体温分数
	 */
	@TableField(exist = false)
	private Integer greatDayBeforebodyTemperatureScore;

	/**
	 * *  五天前体温分数
	 */
	@TableField(exist = false)
	private Integer fourDaysAgoBodyTemperatureScore;


	/**
	 *  今天精神状态分值
	 */
	@TableField(exist = false)
	private Integer mentalStateScore;

	/**
	 *  昨天精神状态分值
	 */
	@TableField(exist = false)
	private Integer yesterdatMentalStateScore;

	/**
	 *  前天精神状态分值
	 */
	@TableField(exist = false)
	private Integer beforeYesMentalStateScore;

	/**
	 *  大前天精神状态分值
	 */
	@TableField(exist = false)
	private Integer greatDayBeforeMentalStateScore;

	/**
	 *  五天前精神状态分值
	 */
	@TableField(exist = false)
	private Integer fourDaysAgoMentalStateScore;

	/**
	 *  今天肌肉酸痛分数
	 */
	@TableField(exist = false)
	private Integer muscleAcheScore;


	/**
	 *  昨天肌肉酸痛分数
	 */
	@TableField(exist = false)
	private Integer yesterdatmuscleAcheScore;


	/**
	 *  前天肌肉酸痛分数
	 */
	@TableField(exist = false)
	private Integer beforeYesmuscleAcheScore;

	/**
	 *  大前天肌肉酸痛分数
	 */
	@TableField(exist = false)
	private Integer greatDayBeforemuscleAcheScore;


	/**
	 *  五天前肌肉酸痛分数
	 */
	@TableField(exist = false)
	private Integer fourDaysAgoMuscleAcheScore;

	/**
	 *  今天呼吸困难分数
	 */
	@TableField(exist = false)
	private Integer difficultyBreathingScore;

	/**
	 *  昨天呼吸困难分数
	 */
	@TableField(exist = false)
	private Integer yesterdatDifficultyBreathingScore;

	/**
	 *  前天呼吸困难分数
	 */
	@TableField(exist = false)
	private Integer beforeYesDifficultyBreathingScore;

	/**
	 *  大前天呼吸困难分数
	 */
	@TableField(exist = false)
	private Integer greatDayBeforeDifficultyBreathingScore;

	/**
	 *  四天前呼吸困难分数
	 */
	@TableField(exist = false)
	private Integer fourDaysAgoDifficultyBreathingScore;


	/**
	 *  五天前呼吸困难分数
	 */
	@TableField(exist = false)
	private Integer fiveDaysAgoDifficultyBreathingScore;


	/**
	 *  六天前呼吸困难分数
	 */
	@TableField(exist = false)
	private Integer sixDaysAgoDifficultyBreathingScore;

	/**
	 *  病程第八天基础分数
	 */
	@TableField(exist = false)
	private Integer basicScore;

	/**
	 *  肺部CT诊断结果
	 */
	@TableField(exist = false)
	private String lungCTResult;

	/**
	 *  是否复诊
	 */
	@TableField(exist = false)
	private Boolean reviewDiagnosis;


	/**
	 *  查找距离最近的一次肺部CT诊断结果
	 */
	@TableField(exist = false)
	private String lastLungCTResult;


	/**
	 *  乏力分值
	 */
	@TableField(exist = false)
	private Integer fatigueScore;

	/**
	 *  昨天乏力分值
	 */
	@TableField(exist = false)
	private Integer yesterdatFatigueScore;

	/**
	 *  前天乏力分值
	 */
	@TableField(exist = false)
	private Integer beforeYesFatigueScore;

	/**
	 *  大前天乏力分值
	 */
	@TableField(exist = false)
	private Integer greatDayBeforeFatigueScore;

	/**
	 *  五天前乏力分值
	 */
	@TableField(exist = false)
	private Integer fourDaysAgoFatigueScore;

	/**
	 *  咳嗽分值
	 */
	@TableField(exist = false)
	private Integer coughScore;


	/**
	 *  昨天咳嗽分值
	 */
	@TableField(exist = false)
	private Integer yesterdatCoughScore;


	/**
	 *  前天咳嗽分值
	 */
	@TableField(exist = false)
	private Integer beforeYesCoughScore;


	/**
	 *  大前天咳嗽分值
	 */
	@TableField(exist = false)
	private Integer greatDayBeforeCoughScore;


	/**
	 *  五天前咳嗽分值
	 */
	@TableField(exist = false)
	private Integer fourDaysAgoCoughScore;


	/**
	 *  腹泻分值
	 */
	@TableField(exist = false)
	private Integer diarrheaScore;

	/**
	 *  昨天腹泻分值
	 */
	@TableField(exist = false)
	private Integer yesterdatDiarrheaScore;

	/**
	 *  前天腹泻分值
	 */
	@TableField(exist = false)
	private Integer beforeYesDiarrheaScore;

	/**
	 *  大前腹泻分值
	 */
	@TableField(exist = false)
	private Integer greatDayBeforeDiarrheaScore;

	/**
	 *  五天前腹泻分值
	 */
	@TableField(exist = false)
	private Integer fourDaysAgoDiarrheaScore;


	/**
	 * 本次核酸检测结果（复查）
	 */
	@TableField(exist = false)
	private String nucleicAcidDetection;


	/**
	 * 本次核酸检测结果（每日临床填写）
	 */
	@TableField(exist = false)
	private String nucleicAcidDetectionCO;

	/**
	 * 两天前核酸检测结果（复查）
	 */
	@TableField(exist = false)
	private String twoDaysAgoNucleicAcidDetection;

	/**
	 * 上次核酸检测结果 含盖本次范围内（复查）
	 */
	@TableField(exist = false)
	private String lastTimeNucleicAcidDetection;


	/**
	 * 最近一次核酸检测结果（复查）
	 */
	@TableField(exist = false)
	private String recentNucleicAcidDetection;

	/**
	 * 如果最近一次核酸检测为阳性，临床症状消失，是否持续至少三天（复查）
	 */
	@TableField(exist = false)
	private Boolean ifNoSymptoms;

	/**
	 * 如果最近一次CT扫描时间为正常，到目前为止至少7天并且每一天的分数都小于5分
	 */
	@TableField(exist = false)
	private Boolean ifCTNoSymptoms;


	/**
	 *  CT扫描日期字符串
	 */
	@TableField(exist = false)
	private String lungCTResultDate;

	/**
	 *  最近一次CT扫描日期字符串
	 */
	@TableField(exist = false)
	private String lastLungCTResultDate;


	/**
	 * 医师的意见
	 */
	@TableField(exist = false)
	private List<Map<String,Object>> doctorCommentList;


	@TableField(exist = false)
	private List<Map<String,Object>> nounValueList;

	/**
	 * 是否是第一天使用
	 */
	@TableField(exist = false)
	private Boolean ifFirstDayUse;

	//家中密切接触者有无症状
	@TableField(exist = false)
	private String asymptomaticCloseAtHome;

	@TableField(exist = false)
	private String cFydb;

	@TableField(exist = false)
	private String pct;

	@TableField(exist = false)
	private String whiteCell;

	@TableField(exist = false)
	private String eatCell;

	@TableField(exist = false)
	private String lbCell;

	/**
	 *  是否复诊
	 */
	@TableField(exist = false)
	private String reviewDiagnosisStr;

	@TableField(exist = false)
	private String bodyTemperatureStr;

	@TableField(exist = false)
	private String coughStr;

	@TableField(exist = false)
	private String diarrheaStr;

	@TableField(exist = false)
	private String difficultyBreathingStr;

	@TableField(exist = false)
	private String fatigueStr;

	@TableField(exist = false)
	private String mentalStateStr;

	@TableField(exist = false)
	private String muscleAcheStr;

	@TableField(exist = false)
	private Integer rating;// 流行病等级
}

