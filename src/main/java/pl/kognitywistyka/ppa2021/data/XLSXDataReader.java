package pl.kognitywistyka.ppa2021.data;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;

public class XLSXDataReader implements Closeable {

	protected InputStream inputStream;
	protected XSSFWorkbook workbook;
	
	public XSSFWorkbook getWorkbook() {
		return workbook;
	}
	
	public XLSXDataReader(InputStream input) {
		this.inputStream = input;
		try {
			workbook = XSSFWorkbookFactory.createWorkbook(inputStream);
		} catch (IOException e) {
			throw new RuntimeException("Failed opening workbook: ", e);
		}
	}
	
	@Override
	public void close() throws IOException {
		if (workbook != null) {
			workbook.close();
		}
		if (inputStream != null) {
			inputStream.close();
		}
	}

}
