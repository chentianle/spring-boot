package io.renren.modules.generator.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import freemarker.template.TemplateException;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.generator.entity.WebBasicPatientInformationEntity;
import io.renren.modules.generator.service.WebBasicPatientInformationService;
import io.renren.modules.sys.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.script.ScriptException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * 患者基础信息管理
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-02-16 16:08:06
 */
@RestController
@RequestMapping("generator/patientBasicInfoManage")
public class WebBasicPatientInformationController extends AbstractController {
    @Autowired
    private WebBasicPatientInformationService patientInformationService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = patientInformationService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        WebBasicPatientInformationEntity webQuestionnaire = patientInformationService.getById(id);
        return R.ok().put("webQuestionnaire", webQuestionnaire);
    }


    /**
     * 根据用户信息获取
     */
    @RequestMapping("/infoByUserId/{userId}")
    public R infoByUserId(@PathVariable("userId") Long userId) {
        WebBasicPatientInformationEntity webQuestionnaire = patientInformationService.getOne(new QueryWrapper<WebBasicPatientInformationEntity>().eq("user_id", userId));
        return R.ok().put("webQuestionnaire", webQuestionnaire);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody WebBasicPatientInformationEntity webQuestionnaire) throws ScriptException, IOException, TemplateException {
        return patientInformationService.saveCustom(webQuestionnaire);
    }

    /* *//**
     * 修改
     *//*
    @PostMapping("/update")
    public R update(@RequestBody WebBasicPatientInformationEntity webQuestionnaire){
        patientInformationService.updateById(webQuestionnaire);

        return R.ok();
    }

    *//**
     * 删除
     *//*
    @PostMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        patientInformationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }*/

    /**
     * 查询年龄占比
     */
    @PostMapping("/queryGeneration")
    public R queryGeneration(@RequestBody Map<String, Object> params) {
        paramValue(params);
        WebBasicPatientInformationEntity webQuestionnaire = patientInformationService.queryGeneration(params);
        return R.ok().put("generation", webQuestionnaire);
    }

    /**
     * 查询触发流行病风险来源占比
     */
    @PostMapping("/queryTriggeringConditions")
    public R queryTriggeringConditions() {
        Map<String, Object> params = new HashMap<>();
        paramValue(params);
        WebBasicPatientInformationEntity webQuestionnaire = patientInformationService.queryTriggeringConditions(params);
        return R.ok().put("triggeringConditions", webQuestionnaire);
    }
}
