package io.renren.modules.app.EnumPack;

/**
 * 诊断状态
 * @description
 * @author 50519
 * @Date 2020/2/21 17:01
 */
public enum VerbStatusEnum {
    normal(0), //正常
    yellow(1), //黄色
    red(2),  //红色
    fourthState(3) //第四状态
    ;
    private Integer value;

    VerbStatusEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}