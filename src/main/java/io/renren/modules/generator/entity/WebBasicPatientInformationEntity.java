package io.renren.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * 患者基础信息
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-02-16 16:08:06
 */
@Data
@TableName("web_basic_patient_information")
public class WebBasicPatientInformationEntity implements Serializable {
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

//	/**
//	 * 流行病学风险评估是否处于风险状态 0 不是 1是
//	 */
//	private Integer riskOrNot;


	/**
	 * 近期有无明确接触史
	 */
	@TableField(exist = false)
	private String ifCloseConcatStr;


	/**
	 * 健康状况
	 */
	@TableField(exist = false)
	private String healthStatusStr;

	/**
	 * 出现健康问题日期据现在的时间
	 */
	@TableField(exist = false)
	private Integer diseaseDay;

	private String triggeringConditions;// 流行病触发条件

	private String generation;// 年龄段

	@TableField(exist = false)
	private Long lessThan17;//小于等于17岁

	@TableField(exist = false)
	private Long mainAge;//18-65岁

	@TableField(exist = false)
	private Long littleOld;//66-79岁

	@TableField(exist = false)
	private Long middleOld;//80-99岁

	@TableField(exist = false)
	private Long bigThan100;//100岁以上

	@TableField(exist = false)
	private Long travelOrReside;//近期疫情严重地区旅行史或居住史

	@TableField(exist = false)
	private Long contactPatient;//曾接触过疫情严重地区发热伴有呼吸道症状的患者

	@TableField(exist = false)
	private Long closeContact;//曾与确诊患者近距离接触

	@TableField(exist = false)
	private Long multipleOnset;//居住地或工作场所中有多人发病

	@TableField(exist = false)
	private Long crowded;//曾去过人群密集场所并且发热或者呼吸困难
	
	@TableField(exist = false)
	private Long feverQuestion;//出现健康问题日期距离填表日期大于等于5天并且发热
}
