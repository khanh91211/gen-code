package com.fw.core.util;

import com.fw.model.Constants;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tuannda
 */
public class ExcelUtil {
    private ExcelUtil() {
    }

    protected static String[] xlsxSignature = {"50", "4B", "03", "04"};
    protected static String[] xlsSignature = {"DO", "CF", "11", "E0", "A1", "B1", "1A", "E1"};

    /**
     * @param fileName
     * @return
     */
    public static boolean checkXlsxExtension(String fileName) {
        boolean check = false;
        String extension = "";
        int i = fileName.lastIndexOf(".");
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }
        if (extension.equals("xlsx") || extension.equals("xls")) {
            check = true;
        }
        return check;
    }

    /**
     * @param mimeType
     * @return
     */
    public static boolean checkXlsxMimetype(String mimeType) {
        boolean check = false;
        if (mimeType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                || mimeType.equals("application/vnd.ms-excel")) {
            check = true;
        }
        return check;
    }

    /**
     * @param bytes
     * @return
     * @author tuannda
     */
    public static boolean checkSizeFile(byte[] bytes) {
        boolean check = true;
        if (bytes.length > 50000000) {
            check = false;
        }
        return check;
    }

    /**
     * Check header
     *
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static boolean checkHeader(MultipartFile file) throws IOException {
        int[] headerBytes = new int[8];
        for (int i = 0; i < 8; i++) {
            headerBytes[i] = file.getInputStream().read();
            if (!String.valueOf(headerBytes[i]).equals(xlsxSignature[i])) {
                return true;
            }
            if (!String.valueOf(headerBytes[i]).equals(xlsSignature[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param s
     * @return
     */

    public static String escapeHTML(String s) {
        if (s == null) {
            return s;
        }
        return s.replace("<", "&lt;").replace(">", "&gt;");
    }

    /**
     * @param row
     * @return
     */
    public static boolean isRowEmpty(Row row) {
        boolean isEmpty = true;
        if (row != null) {
            int i = 0;
            for (Cell cell : row) {
                if (!isCellEmpty(cell) || i > 20) {
                    isEmpty = false;
                    break;
                }
                i++;
            }
        }
        return isEmpty;

    }

    /**
     * @param cell
     * @return
     */

    public static boolean isCellEmpty(Cell cell) {
        return cell == null || cell.getCellType() == CellType.BLANK
                || (cell.getCellType() == CellType.STRING && cell.getStringCellValue().trim().isEmpty());
    }

    /**
     * @param cell
     * @return
     */
    public static Object getCellValue(Cell cell) {
        Object val = null;
        if (!isCellEmpty(cell)) {
            switch (cell.getCellType()) {
                case BLANK:
                    break;
                case ERROR:
                    break;
                case FORMULA:
                    break;
                case _NONE:
                    break;
                case BOOLEAN:
                    val = String.valueOf(cell.getBooleanCellValue()).trim();
                    val = validationSizeCell(val.toString());
                    val = escapeHTML(val.toString());
                    break;
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        val = cell.getDateCellValue();
                    } else {
                        val = String.valueOf(cell.getNumericCellValue()).trim();
                        val = validationSizeCell(val.toString());
                        val = escapeHTML(val.toString());
                    }
                    break;
                case STRING:
                    val = cell.getStringCellValue().trim();
                    val = validationSizeCell(val.toString());
                    val = escapeHTML(val.toString());
                    break;
                default:
                    val = cell.getRichStringCellValue().getString();
            }
        }
        return val;

    }

    /**
     * @param str
     * @return
     */
    public static String validationSizeCell(String str) {
        char[] c = str.toCharArray();
        if (c.length > 245) {
            return String.copyValueOf(c, 0, 245);
        }
        return str;
    }

    public static Map<Integer, Integer> getNewKey(Map<Integer, String> mapNew, Map<Integer, String> mapOld) {
        Map<Integer, Integer> mapKey = new HashMap<>();
        for (Map.Entry<Integer, String> entryNew : mapNew.entrySet()) {
            for (Map.Entry<Integer, String> entryOld : mapOld.entrySet()) {
                if (entryNew.getValue().equals(entryOld.getValue())) {
                    mapKey.put(entryNew.getKey(), entryOld.getKey());
                }
            }
        }
        return mapKey;
    }

    public static String convertIndexToColumnName(int columnIndex) {
        StringBuilder columnName = new StringBuilder();

        while (columnIndex > 0) {
            int remainder = (columnIndex - 1) % 26;
            columnName.insert(0, (char) (65 + remainder)); // Chuyển đổi thành ký tự theo mã ASCII
            columnIndex = (columnIndex - 1) / 26;
        }

        return columnName.toString();
    }

    public static HttpHeaders createHeaderExport(String fileName, String reportName) {
        HttpHeaders headers = new HttpHeaders();
        List<String> headerName = new ArrayList<>();
        headerName.add("*");
        headers.setAccessControlExposeHeaders(headerName);
        if (fileName.endsWith("xls")) {
            headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        } else {
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        }
        String name = URLEncoder.encode(reportName, StandardCharsets.UTF_8).replace("+", "%20");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=utf-8''" + name);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return headers;
    }

    public static XSSFCellStyle createXSSFCellStyle(Workbook workbook) {
        XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setWrapText(true);
        return style;
    }

    public static CellStyle createCellStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.JUSTIFY);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setWrapText(true);
        return cellStyle;
    }

    public static Font createFont(Workbook workbook, String fontName, int size, boolean isBold) {
        Font font = workbook.createFont();
        if (!CommonUtil.isNullOrEmpty(fontName)) {
            font.setFontName(fontName);
        } else {
            font.setFontName(Constants.TIMES_NEW_ROMAN_FONT);
        }
        font.setFontHeightInPoints((short) size);
        font.setBold(isBold);
        return font;
    }

    public static void setBordersToMergedCells(Sheet sheet) {
        int numMerged = sheet.getNumMergedRegions();
        for (int i = 0; i < numMerged; i++) {
            CellRangeAddress mergedRegions = sheet.getMergedRegion(i);
            RegionUtil.setBorderTop(BorderStyle.THIN, mergedRegions, sheet);
            RegionUtil.setBorderLeft(BorderStyle.THIN, mergedRegions, sheet);
            RegionUtil.setBorderRight(BorderStyle.THIN, mergedRegions, sheet);
            RegionUtil.setBorderBottom(BorderStyle.THIN, mergedRegions, sheet);
        }
    }
}
