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
@TableName("web_user")
public class WebUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Long userId;
	/**
	 *
	 */
	private String username;
	/**
	 *
	 */
	private String mobile;


	private String areaCode;

	/**
	 *
	 */
	private String password;
	/**
	 *
	 */
	private Date createTime;

	private String nickname;

	private Long hospitalId;

	@TableField(exist = false)
	private String hospitalName;

	/**
	 * 基础信息是否已填写  1是已填写 2是未填写
	 */
	private Integer ifBasicFilled;

	/**
	 * 0:继续隔离观察，1:痊愈离开隔离点，2:病情重症倾向转定点医院，3:死亡
	 */
	private Integer userStatus;

	//定位信息-经度
	private String locationInfomationLongitude;
	//定位信息-维度
	private String locationInfomationDimension;
	//详细地址
	private String formattedAddress;
	//流行病调查评级（0：正常  1：疑似  2：轻症  3：重症）
	private Integer rating;

	/**
	 * 医师的意见
	 */
	@TableField(exist = false)
	private String doctorComment;

	/**
	 * 医师的意见最新时间
	 */
	@TableField(exist = false)
	private Date doctorCommentTime;

	/**
	 * 患者概要情况
	 */
	@TableField(exist = false)
	private String	nounValue; // '患者概要情况'
	/**
	 * 患者概要情况 最新时间
	 */
	@TableField(exist = false)
	private Date nounValueTime;

	@TableField(exist = false)
	private List<Map<String,Object>> nounValueList;


}
