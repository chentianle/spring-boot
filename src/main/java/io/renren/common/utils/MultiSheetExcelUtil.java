package io.renren.common.utils;


import io.renren.common.exception.RRException;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class MultiSheetExcelUtil {

    private HSSFWorkbook wb;
    private HSSFCellStyle defaultStyle;

    private MultiSheetExcelUtil() {
        this.wb = new HSSFWorkbook();
        this.defaultStyle = getDefaultStyle();
    }

    private HSSFCellStyle getDefaultStyle() {
        HSSFCellStyle style = wb.createCellStyle();
        // 设置字体
        HSSFFont fontDefault = wb.getFontAt((short) 0);
        fontDefault.setCharSet(HSSFFont.DEFAULT_CHARSET);
        fontDefault.setFontHeightInPoints((short) 11);// 更改默认字体大小
        fontDefault.setFontName("宋体");//
        style.setFont(fontDefault);
        // 设置对齐
        style.setAlignment(HorizontalAlignment.CENTER);// 水平居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
        // 设置边框
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        // 自动换行
        style.setWrapText(true);
        return style;
    }

    private HSSFCellStyle getHeadDefaultStyle() {
        HSSFCellStyle style = wb.createCellStyle();
        // 设置字体
        HSSFFont fontDefault = wb.getFontAt((short) 0);
        fontDefault.setCharSet(HSSFFont.DEFAULT_CHARSET);
        fontDefault.setFontHeightInPoints((short) 11);// 更改默认字体大小
        fontDefault.setFontName("宋体");//
        style.setFont(fontDefault);
        // 设置对齐
        style.setAlignment(HorizontalAlignment.CENTER);// 水平居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
        // 设置边框
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        // 自动换行
        style.setWrapText(true);
        return style;
    }

    private void createCell(HSSFRow row, int cellnum, String value) {
        HSSFCell cell = row.createCell(cellnum);
        cell.setCellStyle(defaultStyle);
        cell.setCellValue(value);
    }

    public static MultiSheetExcelUtil newInstace() {
        return new MultiSheetExcelUtil();
    }

    public void addSheet(List<?> datas, List<String> title, List<String> fields, String sheetName) {
        HSSFSheet sheet = wb.createSheet(sheetName);
        sheet.setDefaultColumnWidth(25);
        HSSFRow titleRow = sheet.createRow(0);
        sheet.createFreezePane(0,1,0,1);
        for (int i = 0; i < title.size(); i++) {
            createCell(titleRow, i, title.get(i));
        }

        for (int i = 0; i < datas.size(); i++) {
            Object model = datas.get(i);
            HSSFRow row = sheet.createRow(i + 1);
            for (int j = 0; j < fields.size(); j++) {
                String value = fields.get(j);
                createCell(row, j, getValue(model, value));
            }
        }
    }

    private String getValue(Object model, String field) {
        if (model == null) {
            return "";
        }
        if (StringUtils.isEmpty(field)) {
            throw new RRException("导出excel的列不能为空");
        }
        try {
            Class<?> t = model.getClass();
            String getMethodName = getMethodName(field);
            Field[] fe = t.getDeclaredFields();
            for (Field f : fe) {
                if (!f.getName().equals(field)) {
                    continue;
                }
                Method m = model.getClass().getDeclaredMethod(getMethodName);
                Object o = m.invoke(model);
                if (null == o) {
                    return "-";
                }
                if (o instanceof Date) {
                    return DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
                            .withZone(ZoneId.systemDefault()).format(((Date)o).toInstant());
                    //return DateTimeUtil.parse2String((Date) o, "yyyy/MM/dd");
                }
                return String.valueOf(o);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RRException("导出excel的列不正确");
        }
        return "";
    }

    private String getMethodName(String fieldName) {
        return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    public HSSFWorkbook getWorkBook(){
        return this.wb;
    }
}
