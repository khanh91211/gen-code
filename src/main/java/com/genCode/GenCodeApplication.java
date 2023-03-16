package com.genCode;

import com.genCode.dto.EntitiesDto;
import com.genCode.dto.ModuleDto;
import com.genCode.dto.PropertiesDto;
import com.genCode.dto.ValidationDto;
import com.genCode.util.TextUtils;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class GenCodeApplication {

    public final static String basePackage = "vn.gtel";

    public static void main(String[] args) throws IOException {
        SpringApplication.run(GenCodeApplication.class, args);
        readExcel();
    }

    private static void readExcel() {
        try {
            OPCPackage fs = OPCPackage.open(new FileInputStream(new File("C:\\Users\\khanh\\Desktop\\Model-gen-code.xlsx")));
            XSSFWorkbook wb = new XSSFWorkbook(fs);
            List<ModuleDto> moduleDtos = new ArrayList<>();
            List<EntitiesDto> entitiesDtos = new ArrayList<>();
            List<PropertiesDto> propertiesDtos = new ArrayList<>();
            List<ValidationDto> validationDtos = new ArrayList<>();
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
                        ModuleDto moduleDto = new ModuleDto();
                        PropertiesDto propertiesDto = new PropertiesDto();
                        ValidationDto validationDto = new ValidationDto();
                        //xu ly tung cot
                        for (int c = 0; c < cols; c++) {
                            cell = row.getCell((short) c);
                            if (sheetName.equals("Modules")) {
                                switch (c) {
                                    case 0:
                                        moduleDto.setIsActive(formatter.formatCellValue(cell));
                                        break;
                                    case 1:
                                        moduleDto.setNameEn(formatter.formatCellValue(cell));
                                        break;
                                    case 2:
                                        moduleDto.setNameVi(formatter.formatCellValue(cell));
                                        break;
                                    case 3:
                                        moduleDto.setPort(formatter.formatCellValue(cell));
                                        break;
                                    case 4:
                                        moduleDto.setPrefix(formatter.formatCellValue(cell));
                                        break;
                                    default: {
                                    }
                                }
                                if (c + 1 == cols) {
                                    moduleDtos.add(moduleDto);
                                }
                            } else if (sheetName.equals("Entities")) {
                                switch (c) {
                                    case 0:
                                        entitiesDto.setIsActive(formatter.formatCellValue(cell));
                                        break;
                                    case 1:
                                        entitiesDto.setEntityKey(cell.getStringCellValue());
                                        break;
                                    case 2:
                                        entitiesDto.setModule(formatter.formatCellValue(cell));
                                        break;
                                    case 3:
                                        entitiesDto.setNameProperty(formatter.formatCellValue(cell));
                                        break;
                                    case 4:
                                        entitiesDto.setNameColumn(formatter.formatCellValue(cell));
                                        break;
                                    case 5:
                                        entitiesDto.setNameDisplay(formatter.formatCellValue(cell));
                                        break;
                                    case 6:
                                        entitiesDto.setTableName(formatter.formatCellValue(cell));
                                        break;
                                    case 7:
                                        entitiesDto.setGenCtrl(formatter.formatCellValue(cell));
                                        break;
                                    case 8:
                                        entitiesDto.setGenEntity(formatter.formatCellValue(cell));
                                        break;
                                    case 9:
                                        entitiesDto.setGenService(formatter.formatCellValue(cell));
                                        break;
                                    case 10:
                                        entitiesDto.setGenRepo(formatter.formatCellValue(cell));
                                        break;
                                    default: {
                                    }
                                }
                                if (c + 1 == cols) {
                                    entitiesDtos.add(entitiesDto);
                                }
                            } else if (sheetName.equals("Properties")) {
                                switch (c) {
                                    case 0:
                                        propertiesDto.setModule(cell.getStringCellValue());
                                        break;
                                    case 1:
                                        propertiesDto.setEntityKey(cell.getStringCellValue());
                                        break;
                                    case 2:
                                        propertiesDto.setNameProperty(formatter.formatCellValue(cell));
                                        break;
                                    case 3:
                                        propertiesDto.setNameColumn(formatter.formatCellValue(cell));
                                        break;
                                    case 4:
                                        propertiesDto.setNameDisplay(formatter.formatCellValue(cell));
                                        break;
                                    case 5:
                                        propertiesDto.setType(formatter.formatCellValue(cell));
                                        break;
                                    case 6:
                                        propertiesDto.setRefType(formatter.formatCellValue(cell));
                                        break;
                                    case 7:
                                        propertiesDto.setRequired(formatter.formatCellValue(cell));
                                        break;
                                    case 8:
                                        propertiesDto.setMin(formatter.formatCellValue(cell));
                                        break;
                                    case 9:
                                        propertiesDto.setMax(formatter.formatCellValue(cell));
                                        break;
                                    case 10:
                                        propertiesDto.setPattern(formatter.formatCellValue(cell));
                                        break;
                                    case 11:
                                        propertiesDto.setToString(formatter.formatCellValue(cell));
                                        break;
                                    case 12:
                                        propertiesDto.setShowOnTable(formatter.formatCellValue(cell));
                                        break;
                                    case 13:
                                        propertiesDto.setCanAdd(formatter.formatCellValue(cell));
                                        break;
                                    case 14:
                                        propertiesDto.setCanUpdate(formatter.formatCellValue(cell));
                                        break;
                                    case 15:
                                        propertiesDto.setCanFilter(formatter.formatCellValue(cell));
                                        break;
                                    case 16:
                                        propertiesDto.setCanSort(formatter.formatCellValue(cell));
                                        break;
                                    case 17:
                                        propertiesDto.setCanFilterBy(formatter.formatCellValue(cell));
                                        break;
                                    default: {
                                    }
                                }
                                if (c + 1 == cols) {
                                    propertiesDtos.add(propertiesDto);
                                }
                            } else if (sheetName.equals("Validations")) {
                                switch (c) {
                                    case 0:
                                        validationDto.setNameEn(formatter.formatCellValue(cell));
                                        break;
                                    case 1:
                                        validationDto.setNameVi(formatter.formatCellValue(cell));
                                        break;
                                    case 2:
                                        validationDto.setValue(formatter.formatCellValue(cell));
                                    default: {
                                    }
                                }
                                if (c + 1 == cols) {
                                    validationDtos.add(validationDto);
                                }
                            }
                        }
                    }
                }
            }
            updateContent(entitiesDtos, moduleDtos, propertiesDtos, validationDtos);
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
    }

    private static void updateContent(List<EntitiesDto> entitiesDtos, List<ModuleDto> moduleDtos, List<PropertiesDto> propertiesDtos, List<ValidationDto> validationDtos) throws IOException, NoSuchMethodException {
        for (ModuleDto mdl : moduleDtos) {
            if ("TRUE".equals(mdl.getIsActive())) {
                List<EntitiesDto> listEntityModule = entitiesDtos.stream().filter(c -> c.getModule().equals(mdl.getNameEn())).collect(Collectors.toList());
                for (EntitiesDto ett : listEntityModule) {
                    String ettNameLower = TextUtils.wordsToLowerCase(ett.getNameProperty());
                    String mdlNameLower = TextUtils.wordsToLowerCase(mdl.getNameEn());
                    VelocityContext context = new VelocityContext();
                    context.put("basePackage", basePackage);
                    context.put("mdlNameLower", mdlNameLower);
                    context.put("ettNameLower", ettNameLower);
                    context.put("ettNamePpt", TextUtils.wordsToCamelFirstUpper(ett.getNameProperty()));
                    context.put("ettRequestMapping", TextUtils.wordsToKebabLower(ett.getNameProperty()));
                    context.put("ettNameCamel", TextUtils.wordsToCamel(ett.getNameProperty()));

                    context.put("mdl", mdl);
                    context.put("ett", ett);
                    List<PropertiesDto> listProEntityModule = propertiesDtos.stream().filter(c -> c.getModule().equals(mdl.getNameEn()) && c.getEntityKey().equals(ett.getEntityKey())).collect(Collectors.toList());
                    context.put("ppts", listProEntityModule);
                    context.put("pptAll",propertiesDtos);
                    context.put("vld", validationDtos);
                    context.put("textUtils", new TextUtils());

                    // Xử lý template
                    String typeClass = "";
                    if ("TRUE".equals(ett.getGenCtrl())) {
                        typeClass = "Ctrl";
                        StringWriter writer = new StringWriter();
                        Velocity.evaluate(context, writer, "gen"+typeClass, readResource(typeClass + ".txt", Charsets.UTF_8));
                        // In kết quả
                        exportFile(mdlNameLower,ettNameLower, typeClass.toLowerCase(), TextUtils.wordsToNoSpace(ett.getNameProperty()) + typeClass, writer.toString());
                    }
                    if ("TRUE".equals(ett.getGenEntity())) {
                        typeClass = "Entity";
                        StringWriter writer = new StringWriter();
                        Velocity.evaluate(context, writer, "gen"+typeClass, readResource(typeClass + ".txt", Charsets.UTF_8));
                        // In kết quả
                        exportFile(mdlNameLower,ettNameLower, typeClass.toLowerCase(), TextUtils.wordsToNoSpace(ett.getNameProperty()), writer.toString());
                        typeClass = "Dto";
                        StringWriter writerDto = new StringWriter();
                        Velocity.evaluate(context, writerDto, "gen"+typeClass, readResource(typeClass + ".txt", Charsets.UTF_8));
                        // In kết quả
                        exportFile(mdlNameLower,ettNameLower, typeClass.toLowerCase(), TextUtils.wordsToNoSpace(ett.getNameProperty()) + typeClass, writerDto.toString());
                        typeClass = "Request";
                        StringWriter writerRq = new StringWriter();
                        Velocity.evaluate(context, writerRq, "gen"+typeClass, readResource(typeClass + ".txt", Charsets.UTF_8));
                        // In kết quả
                        exportFile(mdlNameLower,ettNameLower, "dto".toLowerCase(), TextUtils.wordsToNoSpace(ett.getNameProperty()) + typeClass, writerRq.toString());
                    }
                    if ("TRUE".equals(ett.getGenService())) {
                        typeClass = "Service";
                        StringWriter writer = new StringWriter();
                        Velocity.evaluate(context, writer, "gen"+typeClass, readResource(typeClass + ".txt", Charsets.UTF_8));
                        // In kết quả
                        exportFile(mdlNameLower,ettNameLower, typeClass.toLowerCase(), TextUtils.wordsToNoSpace(ett.getNameProperty()) + typeClass, writer.toString());
                        typeClass = "ServiceImpl";
                        StringWriter writerImpl = new StringWriter();
                        Velocity.evaluate(context, writerImpl, "gen"+typeClass, readResource(typeClass + ".txt", Charsets.UTF_8));
                        // In kết quả
                        exportFile(mdlNameLower,ettNameLower, "Service".toLowerCase(), TextUtils.wordsToNoSpace(ett.getNameProperty()) + typeClass, writerImpl.toString());
                    }
                    if ("TRUE".equals(ett.getGenRepo())) {
                        typeClass = "Repo";
                        StringWriter writer = new StringWriter();
                        Velocity.evaluate(context, writer, "gen"+typeClass, readResource(typeClass + ".txt", Charsets.UTF_8));
                        // In kết quả
                        exportFile(mdlNameLower,ettNameLower, typeClass.toLowerCase(), TextUtils.wordsToNoSpace(ett.getNameProperty()) + typeClass, writer.toString());
                    }
                }
            }
        }
    }

    private static void exportFile(String moduleName,String packageName, String packageClassName, String fileName, String contentFile) throws IOException {
        //tao package neu chưa có
        String desktopPath = FileSystemView.getFileSystemView().getHomeDirectory() + "/Desktop/";
        String packagePath = desktopPath + moduleName +"/"+ basePackage.replace(".", "/") + "/" + moduleName + "/" + packageName+"/"+packageClassName;
        File packageDir = new File(packagePath);
        if (packageDir.exists()) {
            System.out.println("Package directory already exists: " + packagePath);
        } else {
            System.out.println("Package directory does not exist: " + packagePath);
            packageDir.mkdirs();
        }

        //export file code
        String sourcePath = packagePath
                + "/" + fileName + ".java";
        try (FileOutputStream fos = new FileOutputStream(sourcePath)) {
            OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            osw.write(contentFile);
            osw.close();
            System.out.println("File exported successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to export file.");
        }
    }

    private static String readResource(String fileName, Charset charset) throws IOException {
        return Resources.toString(Resources.getResource(fileName), charset);
    }
}
