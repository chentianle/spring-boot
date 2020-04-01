package io.renren.modules.generator.controller;

import io.renren.common.utils.R;
import io.renren.modules.generator.entity.WebUserRatingDayEntity;
import io.renren.modules.generator.service.WebUserRatingDayService;
import io.renren.modules.sys.controller.AbstractController;
import io.renren.modules.sys.entity.SysUserEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-02-16 16:08:06
 */
@RestController
@RequestMapping("generator/webUserRatingDay")
public class WebUserRatingDayController extends AbstractController {

    @Autowired
    private WebUserRatingDayService webUserRatingDayService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        //获取当前登录人信息
        SysUserEntity sysuser = getUser();
        if(sysuser!=null && sysuser.getHospitalName()!=null && !sysuser.getHospitalName().equals("")){
            params.put("hospitalId",sysuser.getHospitalName());
        }

//        PageUtils page = webUserRatingDayService.queryPage(params);
        return R.ok().put("page", null);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{ratingDayId}")
    public R info(@PathVariable("ratingDayId") Long ratingDayId){
        if(ratingDayId == null) {
            return R.error("ratingDayId不能为空");
        }
        WebUserRatingDayEntity bean = webUserRatingDayService.getById(ratingDayId);
        return R.ok().put("bean", bean);
    }

    
    /**
     * 发展曲线
     * @return
     */
    @RequestMapping("/progressCurve")
    public R progressCurve(String hospitalId){
        //获取当前登录人信息
        Map<String,Object> params = new HashMap<String,Object>();
        paramValue(params);
    	List<Map<String,Object>> list = webUserRatingDayService.progressCurve(params);
        return R.ok().put("data", list);
    }
    
    /**
     * 临床症状总数占比
     * @return
     */
    @RequestMapping("/symptom")
    public R symptom(){
        //获取当前登录人信息
        Map<String,Object> params = new HashMap<String,Object>();
        paramValue(params);
    	List<Map<String,Object>> list = webUserRatingDayService.symptom(params);
        return R.ok().put("data", list);
    }
    
    
}
