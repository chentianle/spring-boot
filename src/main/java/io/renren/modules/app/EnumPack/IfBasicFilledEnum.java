package io.renren.modules.app.EnumPack;

/**
 * @author 50519
 * @description
 * @Date 2020/2/22 10:10
 */
public enum IfBasicFilledEnum {
    filled(1),
    unfilled(2);

    private Integer value;

    public Integer getValue() {
        return value;
    }

    IfBasicFilledEnum(Integer value) {
        this.value = value;
    }
}
