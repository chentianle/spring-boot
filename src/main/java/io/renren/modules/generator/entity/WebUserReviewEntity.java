package io.renren.modules.generator.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * 患者记录复查表
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-02-16 16:08:06
 */
@Data
@TableName("web_user_review")
public class WebUserReviewEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private Long id;

	private Long userId;

	private Long hospitalId;

	private Date subTime;

	private String lastCheckTimeCt;

	private Integer handleStatus;//处理状态 处理状态 0 未处理 1已处理 2 忽略

	private Date ignoreTime;//忽略时间

	private Integer deleteFlag; //删除状态 0是未删除 1是已删除

	private String decisionRules;//诊断依据

	private String reviewInfo; //复查信息

	private Long webQuestionnaireId; //关联ID


	@TableField(exist = false)
	private String username;

	@TableField(exist = false)
	private String mobile;


	@TableField(exist = false)
	private String areaCode;
}
