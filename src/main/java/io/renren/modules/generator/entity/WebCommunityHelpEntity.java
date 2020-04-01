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
@TableName("web_community_help")
public class WebCommunityHelpEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Long id;
	private Long webuserId;
	private Long hospitalId;
	private String demandTime;//需求时间
	private String describeValue;//描述
	private String homeAddress;//家庭住址
	private String optionValue;//选项
	private Integer type;//1:社区类，2:非社区类
	private Integer feedbackStatus;//1暂无反馈，2已反馈
	private Date createTime;//创建时间

	@TableField(exist = false)
	private String userName;

	@TableField(exist = false)
	private String mobile;

	@TableField(exist = false)
	private String hospitalName;

	@TableField(exist = false)
	private List<WebCommunityFeedbackEntity> list;
}
