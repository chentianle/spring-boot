package io.renren.modules.generator.entity;

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
@TableName("web_hospital")
public class WebHospitalEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 
	 */
	private String hospitalName;
	/**
	 * 
	 */
	private String hospitalAddress;

	private String address;

	private String district;

	private String area;

	//定位信息-经度
	private String locationInfomationLongitude;
	//定位信息-维度
	private String locationInfomationDimension;


}
