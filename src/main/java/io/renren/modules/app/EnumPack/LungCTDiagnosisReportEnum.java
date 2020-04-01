package io.renren.modules.app.EnumPack;

/**
 * 肺部CT诊断报告
 * @author 50519
 * @description
 * @Date 2020/2/21 17:58
 */
public enum LungCTDiagnosisReportEnum {

    normal("正常"),
    singleShotGroundGlass("单发磨玻璃影"),
    multipleShotsOfGroundGlass("多发磨玻璃影"),
    pgwspc("单肺斑片影"),//patchyGGOWithSegmentalPulmonaryConsolidation
    dggsobwbis("双肺斑片影"),//diffuseGroundGlassShadowsOfBothLungsWithBronchialInflationSigns
    lcotlwlit("双肺实变"),//largeAreaConsolidationOfTheLungsWithLobularInterstitialThickening
    others("其他");

    private String value;

    LungCTDiagnosisReportEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
