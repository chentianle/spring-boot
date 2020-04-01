package io.renren.modules.app.EnumPack;

/**
 * 问卷类型 1 是基础流行病学数据 2是 每日临床观察 3是复查数据
 * @author 50519
 * @description
 * @Date 2020/2/21 16:56
 */
public enum QuestionTypeEnum {

    basic(1), // 基础
    reCheck(2), //复查
    dayCheck(3); //每日

    private Integer value;

    QuestionTypeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
