package pl.kognitywistyka.ppa2021.data;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.Month;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class XLSXTester {
	
	private InputStream getCOVIDDataStream() {
		return getClass().getResourceAsStream("owid-covid-data.xlsx");
	}
	
	@Test
	public void testOpenSheet() {
		try (XLSXDataReader reader = new XLSXDataReader(getCOVIDDataStream())) {
			XSSFSheet sheet = reader.getWorkbook().getSheetAt(0);
			String strVal = sheet.getRow(0).getCell(0).getStringCellValue();
			Assertions.assertEquals("iso_code", strVal);
		} catch (IOException e) {
			Assertions.fail(e);
		}
	}
	
	@Test
	public void testReadData() {
		try (COVIDDataReader reader = new COVIDDataReader(getCOVIDDataStream())) {
			GlobalData gdata = reader.readDataFromStream();
			Assertions.assertEquals(1305774, gdata.getDataByCountry("Poland").getDataForDate(LocalDate.of(2021, Month.JANUARY, 1)).getTotalCases());
			Assertions.assertEquals(1010, gdata.getDataByCountry("Israel").getDataForDate(LocalDate.of(2020, Month.SEPTEMBER, 4)).getTotalDeaths());
		} catch (IOException e) {
			Assertions.fail(e);
		}
	}

}
