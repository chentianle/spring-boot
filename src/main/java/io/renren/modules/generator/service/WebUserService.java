package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.app.form.LoginForm;
import io.renren.modules.app.form.RegisterForm;
import io.renren.modules.generator.entity.WebUserEntity;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-02-16 16:08:06
 */
public interface WebUserService extends IService<WebUserEntity> {

    public R validateUserPhone(RegisterForm form) ;

    PageUtils queryPage(Map<String, Object> params);

    R register(WebUserEntity form);

    long login(LoginForm form);

    List<Map<String,Object>> ranking(Map<String,Object> params);
    
	List<Integer> communityState( Map<String,Object> params);

}

