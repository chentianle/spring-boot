package io.renren.modules.generator.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import io.renren.common.utils.DateUtils;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.generator.entity.WebCommunityFeedbackEntity;
import io.renren.modules.generator.entity.WebCommunityHelpEntity;
import io.renren.modules.generator.entity.WebHospitalEntity;
import io.renren.modules.generator.entity.WebUserEntity;
import io.renren.modules.generator.service.WebCommunityFeedbackService;
import io.renren.modules.generator.service.WebCommunityHelpService;
import io.renren.modules.generator.service.WebUserService;
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
@RequestMapping("generator/webCommunityHelp")
public class WebCommunityHelpController extends AbstractController {

    @Autowired
    private WebCommunityHelpService webCommunityHelpService;

    @Autowired
    private WebUserService webUserService;

    @Autowired
    private WebCommunityFeedbackService webCommunityFeedbackService;

    /**
     * 社区求助列表
     */
    @RequestMapping("/list")
    public R daylist(@RequestParam Map<String, Object> params){
        paramValue(params);
        PageUtils page = webCommunityHelpService.queryPageBylist(params);
        return R.ok().put("page", page);
    }



    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") String id){
        WebCommunityHelpEntity bean = webCommunityHelpService.queryById(id);
        List<WebCommunityFeedbackEntity> feedabackList = webCommunityFeedbackService.getByHelpId(bean.getId());
        bean.setList(feedabackList);
        return R.ok().put("bean", bean);
    }



    /**
     * 列表
     */
    @RequestMapping("/appList")
    public R list(@RequestParam Map<String, Object> params){
        QueryWrapper<WebCommunityHelpEntity> queryWrapper = new QueryWrapper<WebCommunityHelpEntity>();
        if(params.get("type")!=null && !params.get("type").equals("") ){
            queryWrapper.eq("type",params.get("type"));
        }
        if(params.get("webuserId")!=null && !params.get("webuserId").equals("") ){
            queryWrapper.eq("webuser_id",params.get("webuserId"));
        }
        List<WebCommunityHelpEntity> list = webCommunityHelpService.list(queryWrapper);
        if(list!=null && list.size()>0){
            for(WebCommunityHelpEntity bean:list) {
                List<WebCommunityFeedbackEntity> feedabackList = webCommunityFeedbackService.getByHelpId(bean.getId());
                bean.setList(feedabackList);
            }
        }
        return R.ok().put("data", list);
    }

    /**
     * 保存
     */
    @RequestMapping("/saveFeedback")
    public R saveFeedback(@RequestBody WebCommunityFeedbackEntity bean){
        //验证
        if(bean == null) {
            return R.error("内容不能为空");
        }
        //获取当前登录人信息
        SysUserEntity sysuser = getUser();
        bean.setUserId(sysuser.getUserId());
        bean.setFeedbackTime(new Date());
        webCommunityFeedbackService.save(bean);
        WebCommunityHelpEntity webCommunityHelpEntity = new WebCommunityHelpEntity();
        webCommunityHelpEntity.setId(bean.getCommunityHelpId());
        webCommunityHelpEntity.setFeedbackStatus(2);
        webCommunityHelpService.updateById(webCommunityHelpEntity);
        return R.ok();
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody WebCommunityHelpEntity bean){
        //验证
        if(bean == null){
            return R.error("内容不能为空");
        }

        if(bean.getWebuserId() == null){
            return R.error("用户ID不能为空");
        }
        WebUserEntity webUserEntity =webUserService.getById(bean.getWebuserId());
        if(webUserEntity==null){
            return R.error("用户:"+bean.getWebuserId()+",无此人");
        }else{
            if(webUserEntity.getHospitalId()==null){
                return R.error("用户:"+bean.getWebuserId()+",无社区ID");
            }else{
                bean.setHospitalId(webUserEntity.getHospitalId());
            }
        }
        if(bean.getType()!=null) {
            if(bean.getType() == 1){
                if(bean.getDemandTime()==null){
                    return R.error("需求时间不能为空");
                }
                if(bean.getHomeAddress()==null || bean.getHomeAddress().equals("")){
                    return R.error("家庭住址不能为空");
                }
                if(bean.getOptionValue()==null || bean.getOptionValue().equals("")){
                    return R.error("选项不能为空");
                }

            }else{
                if(bean.getDescribeValue()==null || bean.getDescribeValue().equals("")){
                    return R.error("描述不能为空");
                }
            }
        }else{
            return R.error("求助类型不能为空");
        }
        bean.setCreateTime(new Date());
        webCommunityHelpService.save(bean);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody WebCommunityHelpEntity bean){
        webCommunityHelpService.updateById(bean);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody String[] ids) {
        webCommunityHelpService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

    /**
     * 社区端时时数据
     */
    @PostMapping("/communityConstantly")
    public R communityConstantly(@RequestBody Map<String, Object> params) {
        paramValue(params);
		return R.ok().put("data", webCommunityHelpService.communityConstantly(params));
    }

    /**
     * 管理端时时数据
     */
    @PostMapping("/constantly")
    public R constantly(@RequestParam Map<String, Object> params) {
        paramValue(params);
		return R.ok().put("data", webCommunityHelpService.constantly(params));
    }
}
