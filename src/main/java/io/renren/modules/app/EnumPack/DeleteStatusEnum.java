package io.renren.modules.app.EnumPack;

/**
 * s删除状态
 * @author 50519
 * @description
 * @Date 2020/2/21 16:56
 */
public enum DeleteStatusEnum {

    deleted(1),
    unDeleted(0);

    private Integer value;

    DeleteStatusEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
