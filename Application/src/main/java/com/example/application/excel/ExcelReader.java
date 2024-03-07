package com.example.application.excel;

import com.example.application.entity.FinancialIndicators;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ExcelReader {
    public static FinancialIndicators getData(String filePath) {
        FinancialIndicators financialIndicators = new FinancialIndicators();
        financialIndicators.setEnterprise(getNameEnterprise(filePath));

        Map<String, Double> indicators = getIndicators(filePath);

        financialIndicators.setNetProfit(indicators.get("чистая прибыль"));
        financialIndicators.setBorrowedFunds(indicators.get("заемные средства"));
        financialIndicators.setCurrentAssets(indicators.get("оборотные активы"));
        financialIndicators.setShortTermLiabilities(indicators.get("краткосрочные обязательства"));
        financialIndicators.setLongTermDuties(indicators.get("долгосрочные обязательства"));
        financialIndicators.setEquity(indicators.get("собственный капитал"));
        financialIndicators.setNetLoss(indicators.get("чистый убыток"));
        financialIndicators.setAccountsPayable(indicators.get("кредиторская задолженность"));
        financialIndicators.setAccountsReceivable(indicators.get("дебиторская задолженность"));
        financialIndicators.setMostLiquidAssets(indicators.get("наиболее ликвидные активы"));
        financialIndicators.setVolumeOfSales(indicators.get("объем реализации"));
        financialIndicators.setOwnSourcesFinancing(indicators.get("собственные источники финансирования"));
        financialIndicators.setBalanceCurrency(indicators.get("валюта баланса"));
        financialIndicators.setRevenue(indicators.get("выручка"));
        financialIndicators.setDate(getDate(filePath));

        return financialIndicators;
    }

    private static LocalDate getDate(String filePath) {
        try (InputStream inputStream = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(2);

            Cell cell = getCell(sheet, 3, 0);
            String year = cell.getStringCellValue().substring(3, 7);

            return LocalDate.of(Integer.parseInt(year), 12, 31);
        } catch (FileNotFoundException e) {
            return LocalDate.of(LocalDate.now().getYear(), 12, 31);
        } catch (IOException e) {
            return LocalDate.of(LocalDate.now().getYear(), 12, 31);
        }
    }

    private static String getNameEnterprise(String filePath) {
        int sheetIndex = 0;
        int rowIndex = 5;
        int columnIndex = 7;

        try (InputStream inputStream = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(sheetIndex);

            if (sheet != null) {
                Row row = sheet.getRow(rowIndex);

                if (row != null) {
                    Cell cell = row.getCell(columnIndex);

                    if (cell != null) {
                        return cell.getStringCellValue();
                    }
                }
            }

            return "Unknown enterprise";
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<String, Double> getIndicators(String filePath) {
        Map<String, Double> indicators = new HashMap<>();

        try (InputStream inputStream = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(2);

            Cell cell = getCell(sheet, 22, 15);
            putDataInMap(indicators, "чистая прибыль", cell);
            indicators.put("чистый убыток", indicators.get("чистая прибыль"));

            cell = getCell(sheet, 6, 12);
            putDataInMap(indicators, "выручка", cell);

            sheet = workbook.getSheetAt(1);

            cell = getCell(sheet, 43, 10);
            putDataInMap(indicators, "заемные средства", cell);

            cell = getCell(sheet, 25, 10);
            putDataInMap(indicators, "оборотные активы", cell);

            cell = getCell(sheet, 48, 10);
            putDataInMap(indicators, "краткосрочные обязательства", cell);

            cell = getCell(sheet, 40, 10);
            putDataInMap(indicators, "долгосрочные обязательства", cell);

            cell = getCell(sheet, 35, 10);
            putDataInMap(indicators, "собственный капитал", cell);
            indicators.put("собственные источники финансирования", indicators.get("собственный капитал"));

            cell = getCell(sheet, 44, 10);
            putDataInMap(indicators, "кредиторская задолженность", cell);

            cell = getCell(sheet, 21, 10);
            putDataInMap(indicators, "дебиторская задолженность", cell);

            cell = getCell(sheet, 22, 10);
            putDataInMap(indicators, "1240", cell);
            cell = getCell(sheet, 23, 10);
            putDataInMap(indicators, "1250", cell);
            indicators.put("наиболее ликвидные активы", indicators.get("1240") + indicators.get("1250"));

            cell = getCell(sheet, 34, 10);
            putDataInMap(indicators, "объем реализации", cell);

            cell = getCell(sheet, 26, 10);
            putDataInMap(indicators, "1600", cell);
            cell = getCell(sheet, 49, 10);
            putDataInMap(indicators, "1700", cell);
            indicators.put("валюта баланса", indicators.get("1600") + indicators.get("1700"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return indicators;
    }

    private static Cell getCell(Sheet sheet, int rowIndex, int columnIndex) {
        if (sheet != null) {
            Row row = sheet.getRow(rowIndex);

            if (row != null) {
                return row.getCell(columnIndex);
            }
        }

        return null;
    }

    private static void putDataInMap(Map<String, Double> indicators, String name, Cell cell) {
        if (cell == null || !isNumeric(cell.getStringCellValue())) {
            indicators.put(name, 0.);
            //indicators.put("чистый убыток", 0.);
        } else {
            indicators.put(name, Double.parseDouble(cell.getStringCellValue().replaceAll(" ", "")));
            //indicators.put("чистый убыток", Double.parseDouble(cell.getStringCellValue().replaceAll(" ", "")));
        }
    }

    public static boolean isNumeric(String s) {
        if (s == null) {
            return false;
        }

        s = s.replaceAll(" ", "");

        try {
            Double.parseDouble(s);
        } catch (NumberFormatException nfe) {
            return false;
        }

        return true;
    }
}
