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
@TableName("web_community_feedback")
public class WebCommunityFeedbackEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Long id;
	private Long userId;
	private Long communityHelpId;
	private String feedbackValue;//反馈时间
	private Date feedbackTime;//反馈时间

	@TableField(exist = false)
	private String userName;

}
