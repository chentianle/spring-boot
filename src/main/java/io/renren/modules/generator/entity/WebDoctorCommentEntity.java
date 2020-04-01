package io.renren.modules.generator.entity;

import cn.hutool.db.DaoTemplate;
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
@TableName("web_doctor_comment")
public class WebDoctorCommentEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 
	 */
	private Long sysUserId;
	/**
	 * 
	 */
	private Long webQuestionnaireId;


	private String doctorComment;

	private Date createTime;

	private Long webUserId;

	@TableField( exist =  false)
	private String verbStatusSubmit;


}
