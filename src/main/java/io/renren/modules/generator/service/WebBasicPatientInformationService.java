package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import freemarker.template.TemplateException;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.generator.entity.WebBasicPatientInformationEntity;

import javax.script.ScriptException;
import java.io.IOException;
import java.util.Map;

/**
 *
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-02-16 16:08:06
 */
public interface WebBasicPatientInformationService extends IService<WebBasicPatientInformationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    R saveCustom(WebBasicPatientInformationEntity webQuestionnaire) throws ScriptException, IOException, TemplateException;

    public WebBasicPatientInformationEntity queryGeneration(Map<String, Object> params);

    public WebBasicPatientInformationEntity queryTriggeringConditions(Map<String,Object> params);
}

