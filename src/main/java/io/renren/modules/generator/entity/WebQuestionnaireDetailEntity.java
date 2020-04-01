package io.renren.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-02-16 16:08:06
 */
@Data
@TableName("web_questionnaire_detail")
public class WebQuestionnaireDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 *  答案表ID
	 */
	private Long webQuestionnaireId;
	/**
	 * 患者情况
	 */
	private String nounValue;
	/**
	 *  诊断建议
	 */
	private String diagnosisValue;
	/**
	 *  评分
	 */
	private Integer level;

	private Date createTime;

	private String decisionRules;


}

