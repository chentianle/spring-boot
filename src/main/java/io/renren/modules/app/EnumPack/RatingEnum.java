package io.renren.modules.app.EnumPack;

/**
 * s流行病风险状态评估
 * @author 50519
 * @description
 * @Date 2020/2/21 16:56
 */
public enum RatingEnum {
    normal(0), // 正常
    risk(1), // 风险/疑似
    Mild(2), // 轻症
    Serve(3); // 重症;

    private Integer value;

    RatingEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
