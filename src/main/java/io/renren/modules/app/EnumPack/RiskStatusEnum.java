package io.renren.modules.app.EnumPack;

/**
 * s流行病风险状态评估
 * @author 50519
 * @description
 * @Date 2020/2/21 16:56
 */
public enum RiskStatusEnum {

    hasRisk(1),
    noRisk(0);

    private Integer value;

    RiskStatusEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
