/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.modules.app.controller;


import io.renren.common.utils.R;
import io.renren.common.utils.TCaptchaVerify;
import io.renren.modules.app.form.RegisterForm;
import io.renren.modules.generator.entity.WebUserEntity;
import io.renren.modules.generator.service.WebUserService;
import io.renren.modules.sys.service.SysCaptchaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 注册
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/app")
@Api("APP注册接口")
public class AppRegisterController {

    @Autowired
    private WebUserService webUserService;

    @Autowired
    private SysCaptchaService sysCaptchaService;

    @PostMapping("/preRegister")
    @ApiOperation("预注册")
    public R preRegister(HttpServletRequest httpServletRequest, @RequestBody RegisterForm form){
        if(form == null || StringUtils.isBlank(form.getTicket())|| StringUtils.isBlank(form.getRandstr())){
            return R.error("参数不全");
        }

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


        int code = TCaptchaVerify.verifyTicket(form.getTicket(),form.getRandstr(),httpServletRequest.getRemoteAddr());
        if (code == -1 || code >99) {
            return R.error("用户危险等级过高禁止登录：" + code + "！");
        }
        return webUserService.validateUserPhone(form);
    }


    /**
     * 验证码
     */
    @ApiOperation(value = "获取验证码", notes = "获取验证码")
    @ApiImplicitParam(name = "uuid", value = "uuid", required = true, paramType = "query", dataType = "String")
    @GetMapping("/captcha.jpg")
    public void captcha(HttpServletResponse response, String uuid) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        //获取图片验证码
        BufferedImage image = sysCaptchaService.getCaptcha(uuid);

        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
        out.flush();
    }

    @PostMapping("/register")
    @ApiOperation("注册")
    public R register(@RequestBody WebUserEntity form){
        return webUserService.register(form);
    }
}
