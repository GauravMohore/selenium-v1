package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class DataProviders {

    /* Read data from workbook */
    public static Object[][] readFromExcel(String filePath, String sheetName) {
        Object[][] data = null;
        FileInputStream inputStream = null;
        Workbook workbook = null;

        try {
            inputStream = new FileInputStream(new File(filePath));
            workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getLastRowNum();
            int colCount = sheet.getRow(0).getLastCellNum();

            data = new Object[rowCount][colCount];

            for (int i = 1; i <= rowCount; i++) {
                Row row = sheet.getRow(i);
                for (int j = 0; j < colCount; j++) {
                    Cell cell = row.getCell(j);

                    // Handle different cell types
                    switch (cell.getCellType()) {
                        case STRING:
                            data[i - 1][j] = cell.getStringCellValue();
                            break;
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                data[i - 1][j] = dateFormat.format(cell.getDateCellValue());
                            } else {
                                data[i - 1][j] = cell.getNumericCellValue();
                            }
                            break;
                        case BOOLEAN:
                            data[i - 1][j] = cell.getBooleanCellValue();
                            break;
                        default:
                            data[i - 1][j] = null;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (workbook != null) {
                    workbook.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return data;
    }

    public static Map<String, Object[][]> readFromExcelAsMap(String filePath) {
        Map<String, Object[][]> sheetsData = new HashMap<>();
        FileInputStream inputStream = null;
        Workbook workbook = null;

        try {
            inputStream = new FileInputStream(new File(filePath));
            workbook = WorkbookFactory.create(inputStream);

            int numOfSheets = workbook.getNumberOfSheets();

            for (int i = 0; i < numOfSheets; i++) {
                Sheet sheet = workbook.getSheetAt(i);
                String sheetName = sheet.getSheetName();

                int rowCount = sheet.getLastRowNum() + 1;
                int colCount = sheet.getRow(0).getLastCellNum();

                Object[][] data = new Object[rowCount][colCount];

                for (int j = 0; j < rowCount; j++) {
                    Row row = sheet.getRow(j);
                    for (int k = 0; k < colCount; k++) {
                        Cell cell = row.getCell(k, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        if (cell != null) {
                            switch (cell.getCellType()) {
                                case STRING:
                                    data[j][k] = cell.getStringCellValue();
                                    break;
                                case NUMERIC:
                                    if (DateUtil.isCellDateFormatted(cell)) {
                                        data[j][k] = cell.getDateCellValue();
                                    } else {
                                        data[j][k] = cell.getNumericCellValue();
                                    }
                                    break;
                                case BOOLEAN:
                                    data[j][k] = cell.getBooleanCellValue();
                                    break;
                                default:
                                    data[j][k] = null;
                            }
                        } else {
                            data[j][k] = null;
                        }
                    }
                }
                sheetsData.put(sheetName, data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (workbook != null) {
                    workbook.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sheetsData;
    }

    /* Print data from workbook */
    public static void printData(String fileName, String sheetName) {
        String filePath = System.getProperty("user.dir") + "\\src\\main\\resources\\testdata\\" + fileName;
        System.out.println(filePath);
        Object[][] data = readFromExcel(filePath, "validate_page_info");
        for (Object[] row : data) {
            for (Object cell : row) {
                System.out.print(cell + "\t|\t");
            }
            System.out.println(); // Move to the next line for the next row
        }
    }

    /* Write data to workbook */
    public static void writeToExcel(String workbookName, String sheetName, LinkedHashMap<String, Object> rowData) {
        String testDataPath = System.getProperty("user.dir") + "//src//main//resources//testdata//";
        File file = new File(testDataPath + workbookName);
        Workbook workbook;

        // Check if the workbook exists; if not, create a new one
        if (!file.exists()) {
            workbook = new XSSFWorkbook();
        } else {
            try (FileInputStream inputStream = new FileInputStream(file)) {
                workbook = WorkbookFactory.create(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        // Check if the sheet exists; if not, create a new sheet
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            sheet = workbook.createSheet(sheetName);

            // Write header row with keys from the LinkedHashMap
            Row headerRow = sheet.createRow(0);
            int colNum = 0;
            for (String key : rowData.keySet()) {
                Cell headerCell = headerRow.createCell(colNum++);
                headerCell.setCellValue(key);
            }
        }

        // If sheet already exists, find the next available row index
        int rowIndex = findEmptyRowIndex(sheet);

        // Write data from the LinkedHashMap to the sheet
        Row dataRow = sheet.createRow(rowIndex);
        int colNum = 0;
        for (Object value : rowData.values()) {
            Cell dataCell = dataRow.createCell(colNum++);
            if (value instanceof String) {
                dataCell.setCellValue((String) value);
            } else if (value instanceof Integer) {
                dataCell.setCellValue((Integer) value);
            }
            // Add more data type handling as needed
        }

        // Write the changes back to the workbook
        try (FileOutputStream outputStream = new FileOutputStream(testDataPath + workbookName)) {
            workbook.write(outputStream);
//            System.out.println("Data appended successfully to " + sheetName + " in " + workbookName);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* Helper method to find the lowest empty row index (except the first row) */
    private static int findEmptyRowIndex(Sheet sheet) {
        int rowIndex = 0;
        while (sheet.getRow(rowIndex) != null) {
            rowIndex++;
        }
        return rowIndex;
    }


    private static Map<String, Object[]> bestExcelReader(String filePath) {
        Map<String, Object[]> sheetsData = new HashMap();
        FileInputStream inputStream = null;
        Workbook workbook = null;

        try {
            inputStream = new FileInputStream(new File(filePath));
            workbook = WorkbookFactory.create(inputStream);

            int numOfSheets = workbook.getNumberOfSheets();

            for (int i = 0; i < numOfSheets; i++) {
                Sheet sheet = workbook.getSheetAt(i);
                String sheetName = sheet.getSheetName();

                int rowCount = sheet.getLastRowNum() + 1;
                int colCount = sheet.getRow(0).getLastCellNum();

                Object[][] data = new Object[rowCount][colCount];

                for (int j = 0; j < rowCount; j++) {
                    Row row = sheet.getRow(j);
                    for (int k = 0; k < colCount; k++) {
                        Cell cell = row.getCell(k, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        if (cell != null) {
                            switch (cell.getCellType()) {
                                case STRING:
                                    data[j][k] = cell.getStringCellValue();
                                    break;
                                case NUMERIC:
                                    if (DateUtil.isCellDateFormatted(cell)) {
                                        data[j][k] = cell.getDateCellValue();
                                    } else {
                                        data[j][k] = cell.getNumericCellValue();
                                    }
                                    break;
                                case BOOLEAN:
                                    data[j][k] = cell.getBooleanCellValue();
                                    break;
                                default:
                                    data[j][k] = null;
                            }
                        } else {
                            data[j][k] = null;
                        }
                    }
                }
                sheetsData.put(sheetName, data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (workbook != null) {
                    workbook.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sheetsData;
    }

    /* ------>TEST USAGE */
    public static void main(String[] args) {
        String workbookName = "example.xlsx";
        String sheetName = "state-list";
        LinkedHashMap<String, Object> rowData = new LinkedHashMap<>();
        rowData.put("Index", 4);
        rowData.put("Page Title", "Maharashtra");
        rowData.put("Page URL", "https://shuru.co.in/news/maharashtra");
        // Add more key-value pairs as needed

        writeToExcel(workbookName, sheetName, rowData);
    }

}
