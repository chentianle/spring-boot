/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.modules.app.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 登录表单
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
@ApiModel(value = "登录表单")
public class LoginForm {
    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "国家或者地区")
    private String areaCode;//区号

    @ApiModelProperty(value = "密码")
    @NotBlank(message="密码不能为空")
    private String password;

//    @ApiModelProperty(value = "医院")
//    @NotNull(message="医院不能为空")
//    private Long hospitalId;


}
