package com.example.application.excel;

import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ExcelReader {
    public static void getData(String filePath) {
        int sheetIndex = 0;
        int rowIndex = 0;
        int columnIndex = 0;

        try (InputStream inputStream = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(sheetIndex);

            if (sheet != null) {
                Row row = sheet.getRow(rowIndex);

                if (row != null) {
                    Cell cell = row.getCell(columnIndex);

                    if (cell != null) {
                        System.out.println(cell.getStringCellValue());
                    } else {
                        System.out.println("Cell is empty");
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("OK");
    }
}
