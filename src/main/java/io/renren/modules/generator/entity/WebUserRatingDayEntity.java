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
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-02-16 16:08:06
 */
@Data
@TableName("web_user_rating_day")
public class WebUserRatingDayEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Long id;//主键

	/**
	 *
	 */
	private Long userId;//患者ID

	/**
	 *
	 */
	private Date reportDate;//日期

	private Long hospitalId;//社区ID

	private Integer rating;//评级结果

	private Integer fever; //是否发烧 0不发烧 1发烧

	private Integer mentalState; //精神状态 0好 1不好

	private Integer muscleAche; // 肌肉酸痛 0 不痛 1痛

	private Integer cough;// 咳嗽 0 不咳嗽 1 咳嗽

	private Integer difficultyBreathing; // 呼吸困难 0 不困难 1困难

	private Integer fatigue;// 乏力现象 0 没有 1有

	private Integer diarrhea; //腹泻现象 0 没有 1有

	private Long webQuestionnaireId;// 每日临床观察数据ID
}
