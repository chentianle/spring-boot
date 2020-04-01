package io.renren.modules.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;

import freemarker.template.TemplateException;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import io.renren.modules.generator.entity.WebQuestionnaireDetailEntity;
import io.renren.modules.generator.entity.WebQuestionnaireEntity;

import org.springframework.web.bind.annotation.RequestBody;

import javax.script.ScriptException;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-02-16 16:08:06
 */
public interface WebQuestionnaireService extends IService<WebQuestionnaireEntity> {
    PageUtils queryPage(Map<String, Object> params);

    R saveCustom(WebQuestionnaireEntity webQuestionnaire) throws ScriptException, TemplateException, IOException;

    /**
     * 查询i天前的数据根据用户ID和hospitalId
     * @param i
     * @param userId
     * @param hospitalId
     * @return
     */
    WebQuestionnaireEntity queryRecentDayBean(int i, Long userId, Long hospitalId);


    /**
     * 获取患者每日提报信息
     * @param params
     * @return
     */
    public PageUtils queryPageByDaylist(Map<String, Object> params);

    /**
     * 获取历史填报信息
     * @param params
     * @return
     */
    PageUtils queryPageByHistorylist(Map<String, Object> params);

    R queryInfo(Long id);

    /**
     * 查询i天前的列表数据根据用户ID和hospitalId
     * 注意：// 按照提交时间倒序排列取第一条，为准，每天的最后一次检测为计算
     * @param i
     * @param userId
     * @param hospitalId
     * @return
     */
    public List<WebQuestionnaireEntity> queryRecentDayList(int i, Long userId, Long hospitalId);


    /**
     * 导出excel数据
     * @param params
     * @param response
     * @throws IOException
     */
    public void exportExcel(@RequestBody Map<String, Object> params, HttpServletResponse response) throws IOException;
    
    /**
     * 管理端时时数据前3条
     * @return
     */
    List<Map<String, Object>> constantly(Map<String, Object> params);
    
    /**
     * 社区端时时数据前5条
     * @return
     */
    List<Map<String, Object>> communityConstantly(Map<String, Object> params);
    
}

