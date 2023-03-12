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
                XSSFCell cellFirst;
                int rows; // No of rows
                rows = sheet.getPhysicalNumberOfRows();
                sheetName = sheet.getSheetName();
                int cols = 0; // No of columns
                int tmp = 0;

                // This trick ensures that we get the data properly even if it doesn't start from first few rows
//                for (int k = 0; k < 10 || k < rows; k++) {
//                    row = sheet.getRow(k);
//                    if (row != null) {
//                        tmp = sheet.getRow(k).getPhysicalNumberOfCells();
//                        if (tmp > cols) cols = tmp;
//                    }
//                }

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
                                if (c == 0) {
                                    moduleDto.setIsActive(formatter.formatCellValue(cell));
                                } else if (c == 1) {
                                    moduleDto.setNameEn(formatter.formatCellValue(cell));
                                } else if (c == 2) {
                                    moduleDto.setNameVi(formatter.formatCellValue(cell));
                                } else if (c == 3) {
                                    moduleDto.setPort(formatter.formatCellValue(cell));
                                } else if (c == 4) {
                                    moduleDto.setPrefix(formatter.formatCellValue(cell));
                                }
                                if (c + 1 == cols) {
                                    moduleDtos.add(moduleDto);
                                }
                            } else if (sheetName.equals("Entities")) {
                                if (c == 0) {
                                    entitiesDto.setIsActive(formatter.formatCellValue(cell));
                                } else if (c == 1) {
                                    entitiesDto.setEntityKey(cell.getStringCellValue());
                                } else if (c == 2) {
                                    entitiesDto.setModule(formatter.formatCellValue(cell));
                                } else if (c == 3) {
                                    entitiesDto.setNameProperty(formatter.formatCellValue(cell));
                                } else if (c == 4) {
                                    entitiesDto.setNameColumn(formatter.formatCellValue(cell));
                                } else if (c == 5) {
                                    entitiesDto.setNameDisplay(formatter.formatCellValue(cell));
                                } else if (c == 6) {
                                    entitiesDto.setTableName(formatter.formatCellValue(cell));
                                } else if (c == 7) {
                                    entitiesDto.setGenCtrl(formatter.formatCellValue(cell));
                                } else if (c == 8) {
                                    entitiesDto.setGenEntity(formatter.formatCellValue(cell));
                                } else if (c == 9) {
                                    entitiesDto.setGenService(formatter.formatCellValue(cell));
                                } else if (c == 10) {
                                    entitiesDto.setGenRepo(formatter.formatCellValue(cell));
                                }
                                if (c + 1 == cols) {
                                    entitiesDtos.add(entitiesDto);
                                }
                            } else if (sheetName.equals("Properties")) {
                                if (c == 0) {
                                    propertiesDto.setModule(cell.getStringCellValue());
                                } else if (c == 1) {
                                    propertiesDto.setEntityKey(cell.getStringCellValue());
                                } else if (c == 2) {
                                    propertiesDto.setNameProperty(formatter.formatCellValue(cell));
                                } else if (c == 3) {
                                    propertiesDto.setNameColumn(formatter.formatCellValue(cell));
                                } else if (c == 4) {
                                    propertiesDto.setNameDisplay(formatter.formatCellValue(cell));
                                } else if (c == 5) {
                                    propertiesDto.setType(formatter.formatCellValue(cell));
                                } else if (c == 6) {
                                    propertiesDto.setRefType(formatter.formatCellValue(cell));
                                } else if (c == 7) {
                                    propertiesDto.setRequired(formatter.formatCellValue(cell));
                                } else if (c == 8) {
                                    propertiesDto.setMin(formatter.formatCellValue(cell));
                                } else if (c == 9) {
                                    propertiesDto.setMax(formatter.formatCellValue(cell));
                                } else if (c == 10) {
                                    propertiesDto.setPattern(formatter.formatCellValue(cell));
                                } else if (c == 11) {
                                    propertiesDto.setToString(formatter.formatCellValue(cell));
                                } else if (c == 12) {
                                    propertiesDto.setShowOnTable(formatter.formatCellValue(cell));
                                } else if (c == 13) {
                                    propertiesDto.setCanAdd(formatter.formatCellValue(cell));
                                } else if (c == 14) {
                                    propertiesDto.setCanUpdate(formatter.formatCellValue(cell));
                                } else if (c == 15) {
                                    propertiesDto.setCanFilter(formatter.formatCellValue(cell));
                                } else if (c == 16) {
                                    propertiesDto.setCanSort(formatter.formatCellValue(cell));
                                } else if (c == 17) {
                                    propertiesDto.setCanFilterBy(formatter.formatCellValue(cell));
                                }
                                if (c + 1 == cols) {
                                    propertiesDtos.add(propertiesDto);
                                }
                            }else if (sheetName.equals("Validations")) {
                                if (c == 0) {
                                    validationDto.setNameEn(formatter.formatCellValue(cell));
                                } else if (c == 1) {
                                    validationDto.setNameVi(formatter.formatCellValue(cell));
                                } else if (c == 2) {
                                    validationDto.setValue(formatter.formatCellValue(cell));
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
            for (EntitiesDto ett : entitiesDtos) {
                VelocityContext context = new VelocityContext();
                context.put("basePackage", basePackage);
                context.put("mdl", mdl);
                context.put("ett", ett);
                context.put("vld", validationDtos);
                context.put("textUtils", new TextUtils());

                // Xử lý template
                StringWriter writer = new StringWriter();
                Velocity.evaluate(context, writer, "genCode", readResource("Ctrl" + ".txt", Charsets.UTF_8));

                // In kết quả
                exportFile(TextUtils.wordsToNoSpace(mdl.getNameEn()), writer.toString());
            }
        }
    }

    private static void exportFile(String fileName, String contentFile) throws IOException {
        //tao package neu chưa có
        String desktopPath = FileSystemView.getFileSystemView().getHomeDirectory() + "/Desktop";
        String packagePath = desktopPath + basePackage.replace(".", "/");
        File packageDir = new File(packagePath);
        if (packageDir.exists()) {
            System.out.println("Package directory already exists: " + packagePath);
        } else {
            System.out.println("Package directory does not exist: " + packagePath);
            packageDir.mkdirs();
        }

        //export file code
        String sourcePath = packagePath + "/" + fileName + ".java";
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
