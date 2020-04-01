package io.renren.modules.app.EnumPack;

/**
 * 每个公式结果为true时，是否需要检查标识
 * @author 50519
 * @description
 * @Date 2020/2/25 11:55
 */
public enum CheckMarkStatusEnum {
    NoNeed(0), //不需要
    Need(1); //需要

    private Integer value;

    CheckMarkStatusEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
