package io.renren.modules.app.EnumPack;

/**
 * @author 50519
 * @description
 * @Date 2020/2/25 12:43
 */
public enum CheckHandleStatusEnum {
    unhanle(0),//默认未处理
    handled(1),// 处理
    ignore(2); //忽略

    private Integer value;

    public Integer getValue() {
        return value;
    }

    CheckHandleStatusEnum(Integer value) {
        this.value = value;
    }
}
