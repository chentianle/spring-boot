package io.renren.modules.generator.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.generator.entity.WebQuestionnaireEntity;
import io.renren.modules.generator.entity.WebSubjectEntity;
import io.renren.modules.generator.service.WebSubjectService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-02-16 16:08:06
 */
@RestController
@RequestMapping("generator/websubject")
public class WebSubjectController {
    @Autowired
    private WebSubjectService webSubjectService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = webSubjectService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * APP获取列表
     */
    @RequestMapping("/getWebSubjectList")
    public R appList(@RequestParam Map<String, Object> params){
        List<WebSubjectEntity> list = webSubjectService.listByMap(params);
        return R.ok().put("data", list);
    }
    
    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		WebSubjectEntity webSubject = webSubjectService.getById(id);

        return R.ok().put("webSubject", webSubject);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody WebSubjectEntity webSubject){
		webSubjectService.save(webSubject);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody WebSubjectEntity webSubject){
		webSubjectService.updateById(webSubject);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		webSubjectService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
