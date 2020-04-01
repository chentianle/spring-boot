package io.renren.modules.app.EnumPack;

/**
 * 核酸检测
 * @author 50519
 * @description
 * @Date 2020/2/21 21:52
 */
public enum NucleicAcidDetectionEnum {
    Positive("阳性"),//阳性
    negative("阴性"),//阴性
    uncheck("未检测");//未检测;
    private String value;

    public String getValue() {
        return value;
    }

    NucleicAcidDetectionEnum(String value) {
        this.value = value;
    }
}
