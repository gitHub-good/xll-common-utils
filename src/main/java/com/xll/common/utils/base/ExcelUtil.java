package com.xll.common.utils.base;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.beans.PropertyDescriptor;
import java.io.*;
import java.util.*;

public class ExcelUtil {
    private String dataFormat = "m/d/yy h:mm";
    private Map<HSSFWorkbook, HSSFCellStyle> dateStyleMaps = new HashMap();
    private String[] heanders;
    private String[] beannames;

    public ExcelUtil() {
    }

    public ExcelUtil(String[] heanders) {
        this.heanders = heanders;
    }

    public void createXLSHeader(HSSFSheet sheet) {
        for(int i = 0; i < this.heanders.length; ++i) {
            this.setStringValue(sheet, 0, (short)i, this.heanders[i]);
        }

    }

    public void createXLS(HSSFWorkbook wb, HSSFSheet sheet, List<Object[]> dateList) {
        for(int i = 1; i <= dateList.size(); ++i) {
            Object[] object = (Object[])dateList.get(i - 1);

            for(int j = 0; j < object.length; ++j) {
                this.doSetCell(wb, sheet, (short)i, (short)j, object[j]);
            }
        }

    }

    public void doSetCell(HSSFWorkbook wb, HSSFSheet sheet, int rowNum, int colNum, Object value) {
        if (value != null) {
            if (value instanceof Number) {
                this.setDoubleValue(sheet, rowNum, colNum, Double.valueOf(value.toString()));
            } else if (value instanceof String) {
                this.setStringValue(sheet, rowNum, colNum, value.toString());
            } else if (value instanceof Date) {
                HSSFCellStyle dateStyle = null;
                if (this.dateStyleMaps.containsKey(wb)) {
                    dateStyle = (HSSFCellStyle)this.dateStyleMaps.get(wb);
                } else {
                    dateStyle = wb.createCellStyle();
                    this.dateStyleMaps.put(wb, dateStyle);
                }

                this.setDateValue(sheet, dateStyle, rowNum, colNum, (Date)value);
            }
        }

    }

    public void setDoubleValue(HSSFSheet sheet, int rowNum, int colNum, Double value) {
        HSSFCell cell = this.getMyCell(sheet, rowNum, colNum);
        cell.setCellType(0);
        cell.setCellValue(value);
    }

    public void setDateValue(HSSFSheet sheet, HSSFCellStyle dateStyle, int rowNum, int colNum, Date value) {
        HSSFCell cell = this.getMyCell(sheet, rowNum, colNum);
        dateStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat(this.dataFormat));
        cell.setCellStyle(dateStyle);
        cell.setCellValue(value);
    }

    public void setStringValue(HSSFSheet sheet, int rowNum, int colNum, String value) {
        HSSFCell cell = this.getMyCell(sheet, rowNum, colNum);
        cell.setCellType(1);
        HSSFRichTextString str = new HSSFRichTextString(value);
        cell.setCellValue(str);
    }

    private HSSFCell getMyCell(HSSFSheet sheet, int rowNum, int colNum) {
        HSSFRow row = sheet.getRow(rowNum);
        if (null == row) {
            row = sheet.createRow(rowNum);
        }

        HSSFCell cell = row.getCell((short)colNum);
        if (null == cell) {
            cell = row.createCell((short)colNum);
        }

        return cell;
    }

    public String[] getBeannames() {
        return this.beannames;
    }

    public void setBeannames(String[] beannames) {
        this.beannames = beannames;
    }

    public String getDataFormat() {
        return this.dataFormat;
    }

    public void setDataFormat(String dataFormat) {
        this.dataFormat = dataFormat;
    }

    public static boolean checkTitleExl(File file, String[] protyName) {
        boolean checkFlag = false;

        try {
            Workbook wb = WorkbookFactory.create(new FileInputStream(file));
            Sheet sheet = wb.getSheetAt(0);
            Row row = sheet.getRow(0);
            if (null == row) {
                checkFlag = false;
            } else {
                for(int i = 0; i < protyName.length; ++i) {
                    Cell cell = row.getCell((short)i);
                    if (cell == null) {
                        checkFlag = false;
                        break;
                    }

                    if (!protyName[i].equals(cell.getRichStringCellValue().toString())) {
                        checkFlag = false;
                        break;
                    }

                    if (i == protyName.length - 1) {
                        checkFlag = true;
                    }
                }
            }
        } catch (FileNotFoundException var8) {
            checkFlag = false;
        } catch (IOException var9) {
            checkFlag = false;
        } catch (EncryptedDocumentException var10) {
            checkFlag = false;
        } catch (InvalidFormatException var11) {
            checkFlag = false;
        }

        return checkFlag;
    }

    public static <T> List<T> genEXLToObject(File file, Class<T> clazz, String[] protyName) throws FileNotFoundException {
        return genEXLToObject((InputStream)(new FileInputStream(file)), clazz, protyName);
    }

    public static <T> List<T> genEXLToObject(InputStream is, Class<T> clazz, String[] protyName) {
        List<T> reList = new ArrayList();
        int j = 1;

        try {
            Workbook wb = WorkbookFactory.create(is);
            Sheet sheet = wb.getSheetAt(0);
            Row row = sheet.getRow(j);

            for(HashMap protyMap = new HashMap(); row != null; row = sheet.getRow(j)) {
                T obj = clazz.newInstance();
                ++j;

                for(int i = 0; i < protyName.length; ++i) {
                    Cell cell = row.getCell((short)i);
                    if (cell != null) {
                        protyMap.put(protyName[i], conversionCell(cell));
                    }
                }

                org.apache.commons.beanutils.BeanUtils.populate(obj, protyMap);
                reList.add(obj);
            }
        } catch (Exception var12) {
            var12.printStackTrace();
        }

        return reList;
    }

    public static <T> List<T> genEXLToObject(InputStream is, Class<T> clazz, String[] protyName, String[] format) {
        List<T> reList = new ArrayList();
        int j = 1;

        try {
            Workbook wb = WorkbookFactory.create(is);
            Sheet sheet = wb.getSheetAt(0);

            for(Row row = sheet.getRow(j); row != null; row = sheet.getRow(j)) {
                T obj = clazz.newInstance();
                setPropertyValues(obj, protyName, format, row);
                reList.add(obj);
                ++j;
            }
        } catch (IOException var10) {
        } catch (InstantiationException var11) {
        } catch (IllegalAccessException var12) {
        } catch (EncryptedDocumentException var13) {
        } catch (InvalidFormatException var14) {
        }

        return reList;
    }

    private static String setPropertyValues(Object bean, String[] protyName, String[] format, Row row) {
        for(int i = 0; i < protyName.length; ++i) {
            Cell cell = row.getCell((short)i);
            if (cell != null) {
                Object value = conversionCell(cell);

                try {
                    PropertyDescriptor property = PropertyUtils.getPropertyDescriptor(bean, protyName[i]);
                    if (property.getPropertyType().isAssignableFrom(Date.class)) {
                        if (cell.getCellType() == 0) {
                            org.apache.commons.beanutils.BeanUtils.setProperty(bean, protyName[i], cell.getDateCellValue());
                        } else if (cell.getCellType() == 1 && format != null && format.length > i && StringUtils.isNotBlank(format[i])) {
                            org.apache.commons.beanutils.BeanUtils.setProperty(bean, protyName[i], TimeUtil.toCalendar((String)value, format[i]).getTime());
                        }
                    } else {
                        org.apache.commons.beanutils.BeanUtils.setProperty(bean, protyName[i], value);
                    }
                } catch (Exception var10) {
                    Exception e = var10;

                    try {
                        BeanUtils.setProperty(bean, "errorInfo", e.getMessage());
                    } catch (Exception var9) {
                    }

                    return var10.getMessage();
                }
            }
        }

        return "";
    }

    private static Object conversionCell(Cell cell) {
        return cell.getCellType() == 0 ? cell.getNumericCellValue() : cell.getRichStringCellValue().toString();
    }
}
