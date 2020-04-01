package io.renren.modules.generator.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.common.utils.StringUtil;
import io.renren.modules.generator.entity.WebHospitalEntity;
import io.renren.modules.generator.service.WebHospitalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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
@RequestMapping("/generator/webHospital")
public class WebHospitalController {
    @Autowired
    private WebHospitalService webHospitalService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = webHospitalService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") String id){
        WebHospitalEntity webUser = webHospitalService.getById(id);
        return R.ok().put("webUser", webUser);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody WebHospitalEntity webHospital){
        webHospitalService.save(webHospital);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody WebHospitalEntity webHospital){
        webHospitalService.updateById(webHospital);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody String[] ids) {
        webHospitalService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

    @RequestMapping("/selectList")
    public R selectList(@RequestParam Map<String, Object> params){
        QueryWrapper<WebHospitalEntity> queryWrapper = new QueryWrapper<>();
        if(params!=null && null!=params.get("hospitalName") && !params.get("hospitalName").equals("")) {
            queryWrapper.like("hospital_name", params.get("hospitalName"));
        }
        if(params!=null && null!=params.get("district") && !params.get("district").equals("")) {
            queryWrapper.like("district", params.get("district"));
        }
        if(params!=null && null!=params.get("id") && !params.get("id").equals("")) {
            queryWrapper.eq("id", params.get("id"));
        }
        List<WebHospitalEntity> list = webHospitalService.list(queryWrapper);
        return R.ok().put("list", list);
    }

    @RequestMapping("/selectByDistrict")
    public R selectByDistrict(@RequestParam Map<String, Object> params){
        List<WebHospitalEntity> list = webHospitalService.selectByDistrict(params);
        return R.ok().put("list", list);
    }


    @RequestMapping("/selectByArea")
    public R selectByArea(@RequestParam Map<String, Object> params){
        List<WebHospitalEntity> list = webHospitalService.selectByArea(params);
        return R.ok().put("list", list);
    }

    
    @RequestMapping("/mapData")
    public R mapData(@RequestParam String area){
    	if(!StringUtil.checkNotNull(area)){
    		area = "武汉市";
    	}
        List<Map<String,Object>> list = webHospitalService.mapData(area);
        return R.ok().put("data", list);
    }
    
}
