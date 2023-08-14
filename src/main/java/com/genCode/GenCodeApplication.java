package com.genCode;

import com.genCode.dto.*;
import com.genCode.util.TextUtils;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.ResourceUtils;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class GenCodeApplication {

    public final static String basePackage = "com.mbv.channel";

    public final static String basePackageImport = "com.mbv";

    public static void main(String[] args) throws IOException {
        SpringApplication.run(GenCodeApplication.class, args);
        readExcel();
    }

    private static void readExcel() {
        try {
            OPCPackage fs = OPCPackage.open(new FileInputStream(new File("C:\\Users\\khanh\\Desktop\\Model-gen-code.xlsx")));
            XSSFWorkbook wb = new XSSFWorkbook(fs);
            List<PackageDto> packageDtos = new ArrayList<>();
            List<LineDto> lineDtos = new ArrayList<>();
            List<EntitiesDto> entitiesDtos = new ArrayList<>();
            List<PropertiesDto> propertiesDtos = new ArrayList<>();
            List<AnnotationDto> annotationDtos = new ArrayList<>();
            String sheetName = null;
            for (int i = 0; i < wb.getNumberOfSheets(); i++) {
                XSSFSheet sheet = wb.getSheetAt(i);
                XSSFRow row;
                XSSFCell cell;
                int rows; // No of rows
                rows = sheet.getPhysicalNumberOfRows();
                sheetName = sheet.getSheetName();
                int cols = 0; // No of columns
                int tmp = 0;

                // This trick ensures that we get the data properly even if it doesn't start from first few rows
                for (int k = 0; k < 10 || k < rows; k++) {
                    row = sheet.getRow(k);
                    if (row != null) {
                        tmp = sheet.getRow(k).getPhysicalNumberOfCells();
                        if (tmp > cols) cols = tmp;
                    }
                }

                //xu ly tung dong (bo qua dong header)
                for (int r = 1; r < rows; r++) {
                    DataFormatter formatter = new DataFormatter();
                    row = sheet.getRow(r);
                    if (row != null) {
                        EntitiesDto entitiesDto = new EntitiesDto();
                        PackageDto packageDto = new PackageDto();
                        LineDto lineDto = new LineDto();
                        PropertiesDto propertiesDto = new PropertiesDto();
                        AnnotationDto annotationDto = new AnnotationDto();
                        //xu ly tung cot
                        for (int c = 0; c < cols; c++) {
                            cell = row.getCell((short) c);
                            if (sheetName.equalsIgnoreCase("Line")) {
                                switch (c) {
                                    case 0:
                                        lineDto.setIsActive(formatter.formatCellValue(cell));
                                        break;
                                    case 1:
                                        lineDto.setNameEn(formatter.formatCellValue(cell));
                                        break;
                                    case 2:
                                        lineDto.setNameVi(formatter.formatCellValue(cell));
                                        break;
                                    default: {
                                    }
                                }
                                if (c + 1 == cols) {
                                    lineDtos.add(lineDto);
                                }
                            } else if (sheetName.equalsIgnoreCase("Package")) {
                                switch (c) {
                                    case 0:
                                        packageDto.setIsActive(formatter.formatCellValue(cell));
                                        break;
                                    case 1:
                                        packageDto.setNameEn(formatter.formatCellValue(cell));
                                        break;
                                    case 2:
                                        packageDto.setNameVi(formatter.formatCellValue(cell));
                                        break;
                                    case 3:
                                        packageDto.setType(formatter.formatCellValue(cell));
                                        break;
                                    case 4:
                                        packageDto.setLine(formatter.formatCellValue(cell));
                                        break;
                                    default: {
                                    }
                                }
                                if (c + 1 == cols) {
                                    packageDtos.add(packageDto);
                                }
                            } else if (sheetName.equalsIgnoreCase("Entities")) {
                                switch (c) {
                                    case 0:
                                        entitiesDto.setIsActive(formatter.formatCellValue(cell));
                                        break;
                                    case 1:
                                        entitiesDto.setEntityKey(cell.getStringCellValue());
                                        break;
                                    case 2:
                                        entitiesDto.setPackageKey(cell.getStringCellValue());
                                        break;
                                    case 3:
                                        entitiesDto.setTypeKey(formatter.formatCellValue(cell));
                                        break;
                                    case 4:
                                        entitiesDto.setLineKey(formatter.formatCellValue(cell));
                                        break;
                                    case 5:
                                        entitiesDto.setNameDisplay(formatter.formatCellValue(cell));
                                        break;
                                    case 6:
                                        entitiesDto.setGenCtrl(formatter.formatCellValue(cell));
                                        break;
                                    case 7:
                                        entitiesDto.setGenEntity(formatter.formatCellValue(cell));
                                        break;
                                    case 8:
                                        entitiesDto.setGenService(formatter.formatCellValue(cell));
                                        break;
                                    case 9:
                                        entitiesDto.setGenRepo(formatter.formatCellValue(cell));
                                        break;
                                    case 10:
                                        entitiesDto.setExcelName(formatter.formatCellValue(cell));
                                        break;
                                    default: {
                                    }
                                }
                                if (c + 1 == cols) {
                                    entitiesDtos.add(entitiesDto);
                                }
                            } else if (sheetName.equalsIgnoreCase("Properties")) {
                                switch (c) {
                                    case 0:
                                        propertiesDto.setLineKey(cell.getStringCellValue());
                                        break;
                                    case 1:
                                        propertiesDto.setTypeKey(cell.getStringCellValue());
                                        break;
                                    case 2:
                                        propertiesDto.setEntityKey(formatter.formatCellValue(cell));
                                        break;
                                    case 3:
                                        propertiesDto.setNameProperty(formatter.formatCellValue(cell));
                                        break;
                                    case 4:
                                        propertiesDto.setNameColumn(formatter.formatCellValue(cell));
                                        break;
                                    case 5:
                                        propertiesDto.setNameDisplay(formatter.formatCellValue(cell));
                                        break;
                                    case 6:
                                        propertiesDto.setType(formatter.formatCellValue(cell));
                                        break;
                                    case 7:
                                        propertiesDto.setRefType(formatter.formatCellValue(cell));
                                        break;
                                    case 8:
                                        propertiesDto.setRequired(formatter.formatCellValue(cell));
                                        break;
                                    case 9:
                                        propertiesDto.setMin(formatter.formatCellValue(cell));
                                        break;
                                    case 10:
                                        propertiesDto.setMax(formatter.formatCellValue(cell));
                                        break;
                                    case 11:
                                        propertiesDto.setAnnotation(formatter.formatCellValue(cell));
                                        break;
                                    case 12:
                                        propertiesDto.setCanFilter(formatter.formatCellValue(cell));
                                        break;
                                    case 13:
                                        propertiesDto.setIsCatalog(formatter.formatCellValue(cell));
                                        break;
                                    case 14:
                                        propertiesDto.setCheckEmpty(formatter.formatCellValue(cell));
                                        break;
                                    case 15:
                                        propertiesDto.setIdType(formatter.formatCellValue(cell));
                                        break;
                                    default: {
                                    }
                                }
                                if (c + 1 == cols) {
                                    propertiesDtos.add(propertiesDto);
                                }
                            } else if (sheetName.equalsIgnoreCase("Annotation")) {
                                switch (c) {
                                    case 0:
                                        annotationDto.setNameEn(formatter.formatCellValue(cell));
                                        break;
                                    case 1:
                                        annotationDto.setNameVi(formatter.formatCellValue(cell));
                                        break;
                                    case 2:
                                        annotationDto.setValue(formatter.formatCellValue(cell));
                                    default: {
                                    }
                                }
                                if (c + 1 == cols) {
                                    annotationDtos.add(annotationDto);
                                }
                            }
                        }
                    }
                }
            }
            updateContent(lineDtos, packageDtos, entitiesDtos, propertiesDtos, annotationDtos);
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
    }

    private static void updateContent(List<LineDto> lineDtos, List<PackageDto> packageDtos, List<EntitiesDto> entitiesDtos, List<PropertiesDto> propertiesDtos, List<AnnotationDto> annotationDtos) throws IOException, InvalidFormatException {
        for (PackageDto pck : packageDtos) {
            if ("TRUE".equals(pck.getIsActive())) {
                VelocityContext context = new VelocityContext();
                context.put("basePackage", basePackage);
                context.put("basePackageImport", basePackageImport);
                List<PropertiesDto> listPropertiesPck = propertiesDtos.stream().filter(c -> c.getEntityKey().equalsIgnoreCase(pck.getNameEn()) && c.getLineKey().equalsIgnoreCase(pck.getLine()) && c.getTypeKey().equalsIgnoreCase(pck.getType())).collect(Collectors.toList());
                context.put("pptPck", listPropertiesPck);
                List<EntitiesDto> listEntityModule = entitiesDtos.stream().filter(c -> c.getPackageKey().equalsIgnoreCase(pck.getNameEn())).collect(Collectors.toList());
                for (EntitiesDto ett : listEntityModule) {
                    if ("TRUE".equals(ett.getIsActive())) {
                        context.put("ettsOfPck", listEntityModule);
                        List<PropertiesDto> listProEntityModule = propertiesDtos.stream().filter(c -> c.getEntityKey().equalsIgnoreCase(ett.getEntityKey()) && c.getLineKey().equalsIgnoreCase(ett.getLineKey())).collect(Collectors.toList());
                        context.put("ppts", listProEntityModule);
                        context.put("pckNameLower", TextUtils.wordsToLowerCase(pck.getNameEn()));
                        context.put("pckRequestMapping", TextUtils.wordsToKebabLower(pck.getNameEn()));
                        context.put("pckFirstUpperCamel", TextUtils.toPascalCase(pck.getNameEn()));
                        context.put("ettNameCamel", TextUtils.wordsToCamel(ett.getEntityKey()));
                        context.put("ettFirstUpperCamel", TextUtils.toPascalCase(ett.getEntityKey()));
                        context.put("ettNameLower", TextUtils.wordsToLowerCase(ett.getEntityKey()));

                        context.put("pck", pck);
                        context.put("ett", ett);
                        context.put("pptAll", propertiesDtos);
                        context.put("annt", annotationDtos);
                        context.put("textUtils", new TextUtils());

                        getInfoExcelUpload(ett.getExcelName(), context);

                        // Xử lý template
                        String typeClass = "";
                        if (ett.getEntityKey().equalsIgnoreCase(pck.getNameEn())) {
                            typeClass = "Ctrl";
                            StringWriter writer = new StringWriter();
                            Velocity.evaluate(context, writer, "gen" + typeClass, readResource(typeClass + ".txt", Charsets.UTF_8));
                            // In kết quả
                            exportFile(TextUtils.wordsToLowerCase(pck.getNameEn()), TextUtils.wordsToLowerCase(ett.getEntityKey()), typeClass.toLowerCase(), ett.getEntityKey() + " " + typeClass, writer.toString());
                        } else if ("TRUE".equals(ett.getGenCtrl())) {
                            typeClass = "Ctrl";
                            StringWriter writer = new StringWriter();
                            Velocity.evaluate(context, writer, "gen" + typeClass, readResource(typeClass + ".txt", Charsets.UTF_8));
                            // In kết quả
                            exportFile(TextUtils.wordsToLowerCase(pck.getNameEn()), TextUtils.wordsToLowerCase(ett.getEntityKey()), typeClass.toLowerCase(), ett.getEntityKey() + " " + typeClass, writer.toString());
                        }
                        if ("TRUE".equals(ett.getGenEntity())) {
                            typeClass = "Entity";
                            StringWriter writer = new StringWriter();
                            Velocity.evaluate(context, writer, "gen" + typeClass, readResource(typeClass + ".txt", Charsets.UTF_8));
                            // In kết quả
                            exportFile(TextUtils.wordsToLowerCase(pck.getNameEn()), TextUtils.wordsToLowerCase(ett.getEntityKey()), typeClass.toLowerCase(), ett.getEntityKey(), writer.toString());
                            typeClass = "Dto";
                            StringWriter writerDto = new StringWriter();
                            Velocity.evaluate(context, writerDto, "gen" + typeClass, readResource(typeClass + ".txt", Charsets.UTF_8));
                            // In kết quả
                            exportFile(TextUtils.wordsToLowerCase(pck.getNameEn()), TextUtils.wordsToLowerCase(ett.getEntityKey()), typeClass.toLowerCase(), ett.getEntityKey() + typeClass, writerDto.toString());
                            typeClass = "Request";
                            StringWriter writerRq = new StringWriter();
                            Velocity.evaluate(context, writerRq, "gen" + typeClass, readResource(typeClass + ".txt", Charsets.UTF_8));
                            // In kết quả
                            exportFile(TextUtils.wordsToLowerCase(pck.getNameEn()), TextUtils.wordsToLowerCase(ett.getEntityKey()), typeClass.toLowerCase(), ett.getEntityKey() + " " + typeClass, writerRq.toString());

                        }
                        if ("TRUE".equals(ett.getGenService())) {
                            typeClass = "TypeEnum";
                            StringWriter writerTypeEnum = new StringWriter();
                            Velocity.evaluate(context, writerTypeEnum, "gen" + typeClass, readResource(typeClass + ".txt", Charsets.UTF_8));
                            // In kết quả
                            exportFile(TextUtils.wordsToLowerCase(pck.getNameEn()), TextUtils.wordsToLowerCase(ett.getEntityKey()), "enum".toLowerCase(), pck.getNameEn() + " Type Enum", writerTypeEnum.toString());
                            if (!ett.getEntityKey().equalsIgnoreCase(pck.getNameEn())) {
                                typeClass = "ServiceImpl";
                                StringWriter writerImpl = new StringWriter();
                                Velocity.evaluate(context, writerImpl, "gen" + typeClass, readResource(typeClass + ".txt", Charsets.UTF_8));
                                // In kết quả
                                exportFile(TextUtils.wordsToLowerCase(pck.getNameEn()), TextUtils.wordsToLowerCase(ett.getEntityKey()), "Service/impl".toLowerCase(), ett.getEntityKey() + typeClass, writerImpl.toString());
                            }
                        }
                        if ("TRUE".equals(ett.getGenRepo())) {
                            typeClass = "Repository";
                            StringWriter writer = new StringWriter();
                            Velocity.evaluate(context, writer, "gen" + typeClass, readResource(typeClass + ".txt", Charsets.UTF_8));
                            // In kết quả
                            exportFile(TextUtils.wordsToLowerCase(pck.getNameEn()), TextUtils.wordsToLowerCase(ett.getEntityKey()), typeClass.toLowerCase(), ett.getEntityKey() + typeClass, writer.toString());
                        }
                    }
                }
                boolean checkCreateSearchRq = listPropertiesPck.stream().anyMatch(c -> "TRUE".equalsIgnoreCase(c.getCanFilter()));
                if (checkCreateSearchRq) {
                    String typeClass = "SearchRequest";
                    StringWriter writerSearchRq = new StringWriter();
                    Velocity.evaluate(context, writerSearchRq, "gen" + typeClass, readResource(typeClass + ".txt", Charsets.UTF_8));
                    // In kết quả
                    exportFile(TextUtils.wordsToLowerCase(pck.getNameEn()), TextUtils.wordsToLowerCase(pck.getNameEn()), "Request".toLowerCase(), "Search " + pck.getNameEn() + " Request", writerSearchRq.toString());
                }
                String typeClass = "CommonService";
                StringWriter writerComService = new StringWriter();
                Velocity.evaluate(context, writerComService, "gen" + typeClass, readResource(typeClass + ".txt", Charsets.UTF_8));
                // In kết quả
                exportFile(TextUtils.wordsToLowerCase(pck.getNameEn()), TextUtils.wordsToLowerCase(pck.getNameEn()), "Service".toLowerCase(), pck.getNameEn() + " " + typeClass, writerComService.toString());
                typeClass = "Adapter";
                StringWriter writerAdapter = new StringWriter();
                Velocity.evaluate(context, writerAdapter, "gen" + typeClass, readResource(typeClass + ".txt", Charsets.UTF_8));
                // In kết quả
                exportFile(TextUtils.wordsToLowerCase(pck.getNameEn()), TextUtils.wordsToLowerCase(pck.getNameEn()), "Service/" + typeClass.toLowerCase(), pck.getNameEn() + " CommonAdapter", writerAdapter.toString());
                typeClass = "CommonServiceImpl";
                StringWriter writerComServiceImpl = new StringWriter();
                Velocity.evaluate(context, writerComServiceImpl, "gen" + typeClass, readResource(typeClass + ".txt", Charsets.UTF_8));
                // In kết quả
                exportFile(TextUtils.wordsToLowerCase(pck.getNameEn()), TextUtils.wordsToLowerCase(pck.getNameEn()), "Service/impl".toLowerCase(), pck.getNameEn() + " " + typeClass, writerComServiceImpl.toString());
            }
        }
    }

    private static void getInfoExcelUpload(String fileName, VelocityContext context) throws IOException, InvalidFormatException {
        if (fileName != null && !fileName.isEmpty()) {
            File file = ResourceUtils.getFile("classpath:templates/" + fileName);
            OPCPackage fs = OPCPackage.open(new FileInputStream(file));
            XSSFWorkbook wb = new XSSFWorkbook(fs);
//        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
//        }
            XSSFSheet sheet = wb.getSheetAt(0);
            XSSFRow row;
            int rows; // No of rows
            rows = sheet.getPhysicalNumberOfRows();
            int cols = 0; // No of columns
            int tmp = 0;

            // This trick ensures that we get the data properly even if it doesn't start from first few rows
            for (int k = 0; k < 10 || k < rows; k++) {
                row = sheet.getRow(k);
                if (row != null) {
                    tmp = sheet.getRow(k).getPhysicalNumberOfCells();
                    if (tmp > cols) cols = tmp;
                }
            }

            Row headerRow = sheet.getRow(0);
            List<String> lstHeader = new ArrayList<>();
            int headerSize = 0;
            if (headerRow != null) {
                for (Cell c : headerRow) {
                    DataFormatter formatter = new DataFormatter();
                    if (c != null) {
                        lstHeader.add(formatter.formatCellValue(c));
                        headerSize++;
                    }
                }
            }
            context.put("lstHeaderString", lstHeader.stream().map(item -> "\"" + item + "\"").collect(Collectors.joining(", ")));
            context.put("lstHeader", lstHeader);
            context.put("headerSize", headerSize - 1);
        } else {
            context.put("lstHeaderString", null);
            context.put("lstHeader", null);
            context.put("headerSize", null);
        }
    }

    private static void exportFile(String moduleName, String packageName, String packageClassName, String fileName, String contentFile) throws IOException {
        //tao package neu chưa có
        String desktopPath = FileSystemView.getFileSystemView().getHomeDirectory() + "/Desktop/";
        String packagePath = desktopPath + moduleName + "/" + basePackage.replace(".", "/") + "/" + moduleName + "/" + packageClassName;
        File packageDir = new File(packagePath);
        if (packageDir.exists()) {
            System.out.println("Package directory already exists: " + packagePath);
        } else {
            System.out.println("Package directory does not exist: " + packagePath);
            packageDir.mkdirs();
        }

        //export file code
        String sourcePath = packagePath
                + "/" + TextUtils.toPascalCase(fileName) + ".java";
        try (FileOutputStream fos = new FileOutputStream(sourcePath)) {
            OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            osw.write(contentFile);
            osw.close();
            System.out.println("File exported successfully! - " + TextUtils.toPascalCase(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to export file.");
        }
    }

    private static String readResource(String fileName, Charset charset) throws IOException {
        return Resources.toString(Resources.getResource(fileName), charset);
    }
}
