package io.renren.modules.app.EnumPack;

/**
 * 明确接触史
 * @author 50519
 * @description
 * @Date 2020/2/21 17:58
 */
public enum ClearContactHistoryEnum {

    TravelOrReside("近期疫情严重地区旅行史或居住史"),
    ContactPatient("曾接触过疫情严重地区发热伴有呼吸道症状的患者"),
    CloseContact("曾与确诊患者近距离接触"),
    MultipleOnset("居住地或工作场所中有多人发病"),
    Crowded("曾去过人群密集场所"),
    NOthing("以上情况均没有")
    ;

    private String value;

    ClearContactHistoryEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
