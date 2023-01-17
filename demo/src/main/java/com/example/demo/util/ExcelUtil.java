package com.example.demo.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;

/***
 * Excel解析
 */
public class ExcelUtil {

    /***
     * 读取EXCEL
     * @param cell
     * @return
     */
    public static String getCellValue(Cell cell) {
        String value = null;

        //格式化number和String字符串
        DecimalFormat df = new DecimalFormat("0");
        //日期格式化
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        switch (cell.getCellTypeEnum()) {
            case STRING:
                value = cell.getRichStringCellValue().getString();
                break;
            case NUMERIC:
                if("General".equals(cell.getCellStyle().getDataFormatString())){
                    if(StringUtils.isNotEmpty(cell.toString())){
                        if(cell.toString().indexOf(".")==-1){
                            value = cell.toString();
                        }else{
                            String[] list = cell.toString().split("/.");
                            try{
                                if(list[1].length()>1){
                                    value = cell.toString();
                                }else{
                                    value = df.format(cell.getNumericCellValue());
                                }
                            }catch (Exception ex){
                                value = df.format(cell.getNumericCellValue());
                            }
                        }
                    }else{
                        value = "";
                    }
                }else if("m/d/yy".equals(cell.getCellStyle().getDataFormatString())){
                    value = sdf.format(cell.getDateCellValue());
                }else{
                    value = df.format(cell.getNumericCellValue());
                }
                break;
            case BLANK:
                value = "";
                break;
            default:
                value = cell.toString();
                break;
        }
        return StringUtils.defaultString(value, StringUtils.EMPTY);
    }
}