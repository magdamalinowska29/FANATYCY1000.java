package pl.kognitywistyka.ppa2021;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import pl.kognitywistyka.ppa2021.data.COVIDDataReader;
import pl.kognitywistyka.ppa2021.data.CountryData;
import pl.kognitywistyka.ppa2021.data.DailyData;
import pl.kognitywistyka.ppa2021.data.GlobalData;
import pl.kognitywistyka.ppa2021.transfer.CountryDailyData;

public class COVIDController {

	@FXML Button chooseButton;
	@FXML ComboBox<String> chooseCountry;
	@FXML TableView<CountryDailyData> dataTable;
	
	private GlobalData globalData;
	
	public void initialize() {
		// inicjalizujemy kolumny odpowiednimi własnościami obiektu danych
		TableColumn<CountryDailyData, LocalDate> dateColumn = (TableColumn<CountryDailyData, LocalDate>) dataTable.getColumns().get(0);
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
		
		TableColumn<CountryDailyData, Integer> casesColumn = (TableColumn<CountryDailyData, Integer>) dataTable.getColumns().get(1);
		casesColumn.setCellValueFactory(new PropertyValueFactory<>("totalCases"));
		
		TableColumn<CountryDailyData, Integer> deathsColumn = (TableColumn<CountryDailyData, Integer>) dataTable.getColumns().get(2);
		deathsColumn.setCellValueFactory(new PropertyValueFactory<>("totalDeaths"));
	}

	@FXML public void triggerChooseFile(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz plik z danymi (.xlsx)");                 
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("XLSX", "*.xlsx")
        );
        File dataFile = fileChooser.showOpenDialog(dataTable.getScene().getWindow());
        if (dataFile != null) {
        	chooseButton.setText(dataFile.getName());
        	performLoadData(dataFile);
        }
	}

	private void performLoadData(File dataFile) {
		try (FileInputStream fis = new FileInputStream(dataFile)) {		
			try (COVIDDataReader reader = new COVIDDataReader(fis)) {
				this.globalData = reader.readDataFromStream();
				prepareCountryChooser();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}		
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void prepareCountryChooser() {
		chooseCountry.setDisable(false);
		chooseCountry.setValue(null);
		dataTable.getItems().clear();
		chooseCountry.getItems().clear();
		chooseCountry.getItems().addAll(globalData.getCountries());
	}

	@FXML public void triggerChooseCountry(ActionEvent event) {
		ComboBox<String> source = (ComboBox<String>) event.getSource();
		String country = source.getSelectionModel().getSelectedItem();
		CountryData forCountry = globalData.getDataByCountry(country);
		List<CountryDailyData> dailyList = convertToCountryDailyData(forCountry);
		dataTable.getItems().clear();
		dataTable.getItems().addAll(dailyList);
	}

	private List<CountryDailyData> convertToCountryDailyData(CountryData forCountry) {
		List<CountryDailyData> ret = new ArrayList<>();
		LocalDate min = forCountry.getMinDate();
		LocalDate max = forCountry.getMaxDate();
		if (min != null && max != null && !min.isAfter(max)) {
			for (LocalDate cur = min; !cur.isAfter(max); cur = cur.plusDays(1)) {
				CountryDailyData cdd = new CountryDailyData();
				DailyData dailyData = forCountry.getDataForDate(cur);
				cdd.setDate(cur);
				cdd.setTotalCases(dailyData.getTotalCases());
				cdd.setTotalDeaths(dailyData.getTotalDeaths());
				ret.add(cdd);
			}
		}
		return ret;
	}
	
}
