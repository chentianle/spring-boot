package io.renren.modules.generator.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import freemarker.template.TemplateException;
import io.renren.common.utils.*;
import io.renren.modules.generator.entity.WebDoctorCommentEntity;
import io.renren.modules.generator.entity.WebQuestionnaireDetailEntity;
import io.renren.modules.generator.service.WebDoctorCommentService;
import io.renren.modules.generator.service.WebQuestionnaireDetailService;
import io.renren.modules.sys.controller.AbstractController;
import io.renren.modules.sys.entity.SysUserEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import io.renren.modules.generator.entity.WebQuestionnaireEntity;
import io.renren.modules.generator.entity.WebSubjectEntity;
import io.renren.modules.generator.service.WebQuestionnaireService;

import javax.script.ScriptException;
import javax.servlet.http.HttpServletResponse;


/**
 *
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-02-16 16:08:06
 */
@RestController
@RequestMapping("generator/webquestionnaire")
public class WebQuestionnaireController extends AbstractController {
    @Autowired
    private WebQuestionnaireService webQuestionnaireService;

    @Autowired
    private WebQuestionnaireDetailService webQuestionnaireDetailService;

    /**
     * 患者每日提报信息
     */
    @RequestMapping("/daylist")
    public R daylist(@RequestParam Map<String, Object> params){
        params.put("subTime", DateUtils.format(new Date(),DateUtils.DATE_PATTERN));
        //获取当前登录人信息
        paramValue(params);
        PageUtils page = webQuestionnaireService.queryPageByDaylist(params);
        return R.ok().put("page", page);
    }



    /**
     * app患者历史提报信息
     */
    @RequestMapping("/historylist")
    public R historylist(@RequestParam Map<String, Object> params){
        if(params!=null && params.get("userId")!=null && !params.get("userId").equals("")){
            params.put("subTimeOrder","desc");
            PageUtils page = webQuestionnaireService.queryPageByHistorylist(params);
            return R.ok().put("page", page);
        }else{
            return R.error("userId不能为空");
        }
    }



    /**
     * admin患者历史提报信息
     */
    @RequestMapping("/adminHistorylist")
    public R adminHistorylist(@RequestParam Map<String, Object> params){
        //获取当前登录人信息
        params.put("subTimeOrder","desc");
        paramValue(params);
        PageUtils page = webQuestionnaireService.queryPageByHistorylist(params);
        return R.ok().put("page", page);
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = webQuestionnaireService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * APP获取列表
     */
    @RequestMapping("/getByUserIdList")
    public R appList(@RequestParam Map<String, Object> params){
    	String userId = (String)params.get("userId");
    	if(StringUtil.isEmpty(userId)){
    		return R.error("用户ID不可为空");
    	}
    	String subStatus = (String)params.get("subStatus");
    	String subTime = (String)params.get("subTime");

        List<WebQuestionnaireEntity> list = webQuestionnaireService.list(new  QueryWrapper<WebQuestionnaireEntity>()
				.eq(StringUtil.checkNotNull(subStatus),"sub_status", subStatus).eq(StringUtil.checkNotNull(userId),"user_id", userId)
				.like(StringUtil.checkNotNull(subTime),"sub_time", subTime));
        return R.ok().put("data", list);
    }


    /**
     * Admin,诊断详情
     * @return
     * scc 2020-02-22
     */
    @RequestMapping("/allInfo")
    public R allInfo(@RequestBody Map<String,String> param){
        Long id = Long.parseLong(param.get("id"));
        String type = param.get("type");
        Map<String,Object> resultMap = webQuestionnaireDetailService.allInfoByUserid(type,id);
        return R.ok().put("resultData", resultMap);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		return webQuestionnaireService.queryInfo(id);
    }


    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody  WebQuestionnaireEntity webQuestionnaire) throws ScriptException, TemplateException, IOException {
		return webQuestionnaireService.saveCustom(webQuestionnaire);
    }


    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody WebQuestionnaireEntity webQuestionnaire){
		webQuestionnaireService.updateById(webQuestionnaire);

        return R.ok();
    }


    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		webQuestionnaireService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 导出数据
     */
    @PostMapping("/exportDataList")
    public void exportDataList(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {

            //获取当前登录人信息
            SysUserEntity sysuser = getUser();
            if (sysuser != null && sysuser.getHospitalName() != null && !sysuser.getHospitalName().equals("")) {
                params.put("hospitalId", sysuser.getHospitalName());
            }
            webQuestionnaireService.exportExcel(params, response);
        }catch(Exception e){
            e.printStackTrace();

        }
    }


    /**
     * 管理端时时数据
     */
    @PostMapping("/constantly")
    public R constantly(@RequestParam Map<String, Object> params) {
        paramValue(params);
		return R.ok().put("data", webQuestionnaireService.constantly(params));
    }

    /**
     * 社区端时时数据
     */
    @PostMapping("/communityConstantly")
    public R communityConstantly(@RequestBody Map<String, Object> params) {
        paramValue(params);
		return R.ok().put("data", webQuestionnaireService.communityConstantly(params));
    }
}
