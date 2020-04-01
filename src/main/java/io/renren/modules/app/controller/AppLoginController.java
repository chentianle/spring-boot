/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.modules.app.controller;


import io.renren.common.utils.R;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.app.form.LoginForm;
import io.renren.modules.app.utils.JwtUtils;
import io.renren.modules.generator.service.WebBasicPatientInformationService;
import io.renren.modules.generator.service.WebUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * APP登录授权
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/app")
@Api("APP登录接口")
public class AppLoginController {
    @Autowired
    private WebUserService webUserService;
    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 登录
     */
    @PostMapping("/login")
    @ApiOperation("登录")
    public R login(@RequestBody LoginForm form){
        //表单校验
        ValidatorUtils.validateEntity(form);

        if(StringUtils.isBlank(form.getAreaCode())){
            return R.error("请选择国家或者地区");
        }

        if(StringUtils.isBlank(form.getMobile())){
            return R.error("请输入正确的手机号");
        }
//        if("+86".equals(form.getAreaCode())){
//            //国内手机号
//            Pattern pattern = Pattern.compile("^1[0-9]{10}$");
//            Matcher matcher = pattern.matcher(form.getMobile());
//            if(!matcher.matches()){
//                return R.error("请输入正确的手机号");
//            }
//        }else if("+39".equals(form.getAreaCode())){
//            //意大利手机号
//            Pattern pattern2 = Pattern.compile("^3[0-9]{9}$");
//            Matcher matcher2 = pattern2.matcher(form.getMobile());
//            if(!matcher2.matches()){
//                return R.error("请输入正确的手机号");
//            }
//        }else {
            Pattern pattern3 = Pattern.compile("^\\d{9,11}$");
            Matcher matcher3 = pattern3.matcher(form.getMobile());
            if(!matcher3.matches()){
                return R.error("请输入正确的手机号");
            }
//        }

        //用户登录
        long userId = webUserService.login(form);

        //生成token
        String token = jwtUtils.generateToken(userId);

        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("expire", jwtUtils.getExpire());
        map.put("userId", userId);
        return R.ok(map);
    }

}
