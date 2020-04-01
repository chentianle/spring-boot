/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.modules.sys.controller;

import io.renren.modules.sys.entity.SysUserEntity;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Controller公共组件
 *
 * @author Mark sunlightcs@gmail.com
 */
public abstract class AbstractController {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	protected SysUserEntity getUser() {
		return (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
	}

	protected Long getUserId() {
		return getUser().getUserId();
	}

	protected void paramValue(Map<String, Object> params){
		//获取当前登录人信息
		SysUserEntity sysuser = getUser();
		if(sysuser!=null && sysuser.getHospitalType()!=null && !sysuser.getHospitalType().equals("")){
			switch (sysuser.getHospitalType()){
				case 1 : params.put("area",sysuser.getHospitalName());break;
				case 2 : params.put("district",sysuser.getHospitalName());break;
				case 3 : params.put("hospitalName",sysuser.getHospitalName());break;
				default:break;
			}
		}
	}
}
