package io.renren.modules.generator.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.utils.DateUtils;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.generator.entity.*;
import io.renren.modules.generator.service.*;
import io.renren.modules.sys.controller.AbstractController;
import io.renren.modules.sys.entity.SysUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-02-16 16:08:06
 */
@RestController
@RequestMapping("generator/webUserReview")
public class WebUserReviewController extends AbstractController {

    @Autowired
    private WebUserReviewService webUserReviewService;

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
        params.put("ignoreTime",DateUtils.format(new Date(),DateUtils.DATE_PATTERN));
        PageUtils page = webUserReviewService.queryPagelist(params);
        return R.ok().put("page", page);
    }

    /**
     */
    @RequestMapping("/allInfoByUserId")
    public R allInfo(@RequestBody Map<String,String> param){
        Long id = Long.parseLong(param.get("id"));
        Map<String,Object> resultMap = webUserReviewService.allInfoByUserid(id);
        return R.ok().put("resultData", resultMap);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{reviewId}")
    public R info(@PathVariable("reviewId") Long reviewId){
        return R.ok();
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody WebUserReviewEntity bean){
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody WebUserReviewEntity bean){
        if(bean.getHandleStatus()!=null && !bean.getHandleStatus().equals(0)){
            if(bean.getHandleStatus() == 1){
                bean.setDeleteFlag(1);
            }else if(bean.getHandleStatus() == 2){
                bean.setIgnoreTime(new Date());
            }
        }
        webUserReviewService.updateById(bean);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] reviewIds){
        return R.ok();
    }

}
