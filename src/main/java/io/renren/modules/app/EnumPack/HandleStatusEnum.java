package io.renren.modules.app.EnumPack;

/**
 * 处理状态
 * @author 50519
 * @description
 * @Date 2020/2/21 16:56
 */
public enum HandleStatusEnum {

    handled(1),
    unhanle(0);

    private Integer value;

    HandleStatusEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
