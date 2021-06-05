package pl.kognitywistyka.ppa2021.data;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class COVIDDataReader implements Closeable {

	protected XLSXDataReader reader;
	
	public COVIDDataReader(InputStream input) {
		reader = new XLSXDataReader(input);
	}
	
	public GlobalData readDataFromStream() {
		// XLSX data columns: 3 - country, 4 - date, 5 - total cases, 8 - total deaths 
		GlobalData data = new GlobalData();
		XSSFWorkbook workbook = reader.getWorkbook();
		XSSFSheet sheet = workbook.getSheetAt(0);
		int lastRow = sheet.getLastRowNum();
		for (int i = 1; i < lastRow; i++) {
			XSSFRow row = sheet.getRow(i);
			String country = row.getCell(2).getStringCellValue();
			LocalDate date = getDateOrNull(row.getCell(3));
			Integer totalCases = getNumberOrNull(row.getCell(4));
			Integer totalDeaths = getNumberOrNull(row.getCell(7));
			DailyData daily = new DailyData();
			daily.setTotalCases(totalCases);
			daily.setTotalDeaths(totalDeaths);
			CountryData cdata = data.getDataByCountry(country);
			if (cdata == null) {
				cdata = new CountryData();
				data.setDataByCountry(country, cdata);
			}
			if (date != null) {				
				cdata.setDataForDate(date, daily);
			}
		}
		return data;
	}
	
	private LocalDate getDateOrNull(XSSFCell cell) {
		if (cell == null) {
			return null;
		}
		String str = cell.toString();
		if (str == null || str.isEmpty()) {
			return null;
		} else {
			return LocalDate.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		}
	}

	private Integer getNumberOrNull(XSSFCell cell) {
		if (cell == null) {
			return null;
		}
		String str = cell.getRawValue();
		if (str == null || str.isEmpty()) {
			return null;
		} else {
			return Integer.parseInt(str);
		}
	}

	@Override
	public void close() throws IOException {
		if (reader != null) {
			reader.close();
		}
	}
	
}
