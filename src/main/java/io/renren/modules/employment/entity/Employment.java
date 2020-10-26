package io.renren.modules.employment.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("t_employment")
public class Employment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private String id;
    /**
     * 姓名
     */
    private String name;
    /**
     * 年龄
     */
    private String age;
    /**
     * 性别
     */
    private String sex;
    /**
     * 住址
     */
    private String address;
    /**
     * 身份证号
     */
    private String number;
    /**
     * 应聘时间
     */
    @TableField("invite_time")
    private Date inviteTime;
    /**
     * 意向单位
     */
    private String department;
    /**
     * 社保是否转移 0--否 1--是
     */
    @TableField("social_security")
    private String socialSecurity;
    /**
     * 体检情况  0--不合格 1--合格
     */
    private String health;
    /**
     * 是否签订合同 0--未签 1--已签
     */
    private String contract;
    /**
     * 培训情况  0--不合格  1--合格
     */
    private String train;

}
