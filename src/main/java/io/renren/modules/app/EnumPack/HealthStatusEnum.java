package io.renren.modules.app.EnumPack;

/**
 * 健康状况枚举
 * @author 50519
 * @description
 * @Date 2020/2/21 17:58
 */
public enum HealthStatusEnum {

    Fever("发热"),
    Cough("咳嗽"),
    Fatigue("乏力"),
    Diarrhea("腹泻"),
    MuscleAche("肌肉酸痛"),
    Headache("头痛"),
    DifficultyBreathing("呼吸困难"),
    ;

    private String value;

    HealthStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
