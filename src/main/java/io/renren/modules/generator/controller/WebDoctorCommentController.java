package io.renren.modules.generator.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.generator.entity.WebDoctorCommentEntity;
import io.renren.modules.generator.entity.WebQuestionnaireEntity;
import io.renren.modules.generator.service.WebDoctorCommentService;
import io.renren.modules.generator.service.WebQuestionnaireService;
import io.renren.modules.sys.controller.AbstractController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
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
@RequestMapping("/generator/webDoctorComment")
public class WebDoctorCommentController  extends AbstractController {
    @Autowired
    private WebDoctorCommentService webDoctorCommentService;

    @Autowired
    private WebQuestionnaireService webQuestionnaireService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = webDoctorCommentService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") String id){
        WebDoctorCommentEntity webUser = webDoctorCommentService.getById(id);

        return R.ok().put("webUser", webUser);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody WebDoctorCommentEntity bean){
        //获取填表患者的ID
        WebQuestionnaireEntity webQuestionnaire = webQuestionnaireService.getById(bean.getWebQuestionnaireId());
        if(webQuestionnaire!=null) {
            bean.setCreateTime(new Date());
            bean.setSysUserId(getUserId());
            bean.setWebUserId(webQuestionnaire.getUserId());
            webDoctorCommentService.save(bean);
            //修改处理状态
            WebQuestionnaireEntity qbean = new WebQuestionnaireEntity();
            qbean.setHandleStatus(1);
            qbean.setId(bean.getWebQuestionnaireId());
            if(bean.getVerbStatusSubmit()!=null && !bean.getVerbStatusSubmit().equals("")){
                qbean.setVerbStatus(Integer.parseInt(bean.getVerbStatusSubmit()));
            }
            webQuestionnaireService.updateById(qbean);
        }else{
            return R.error("填表信息数据库中未找到");
        }
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody WebDoctorCommentEntity bean){
        webDoctorCommentService.updateById(bean);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody String[] ids) {
        webDoctorCommentService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

    @RequestMapping("/selectList")
    public R selectList(@RequestParam Map<String, Object> params){
        List<WebDoctorCommentEntity> list = webDoctorCommentService.list();
        return R.ok().put("list", list);
    }

}
