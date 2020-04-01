package io.renren.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("web_user_dayorderby")
public class WebUserDayorderbyEntity implements Serializable {
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
	 *  提交问卷内容
	 */
	private Integer verbStatus;
	/**
	 *  评分
	 */
	private Integer score;
	/**
	 * 用户ID
	 */
	private Long userId;


	private Long webQuestionnaireId;
}
