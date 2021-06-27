package pl.kognitywistyka.ppa2021;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.apache.poi.hpsf.Array;


public class FANATYCYController {


	@FXML Button chooseButton;

	@FXML TabPane tabPane;


	@FXML Button fanatyk;

	@FXML Button ryba;

	ArrayList<String> imiona = new ArrayList<String>();
	ArrayList<String> data_licencji =new ArrayList<String>();
	ArrayList<Boolean> zwiazek=  new ArrayList<Boolean>();

	ArrayList<String> gatunek= new ArrayList<String>();
	ArrayList<Boolean> zweryfikowana=  new ArrayList<Boolean>();
	ArrayList<String> data_zlowienia=  new ArrayList<String>();

	ArrayList<String> uzyte_imiona = new ArrayList<String>();





	Connection c = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", "");
	Statement s = c.createStatement();

	public FANATYCYController() throws SQLException {
	}

	private void createDataFanatycy(){
		imiona.add("Adam Bronowski");
		imiona.add("Alicja Wiśniewska");
		imiona.add("Barbara Laka");
		imiona.add("Bartosz Kowalski");
		imiona.add("Celina Misztela");
		imiona.add("Cezary Bąk");

		data_licencji.add("2017-03-15");
		data_licencji.add("2013-06-07");
		data_licencji.add("2017-04-20");
		data_licencji.add("2020-11-22");
		data_licencji.add("2019-01-10");
		data_licencji.add("2020-03-25");
		data_licencji.add("2017-05-24");

		zwiazek.add(true);
		zwiazek.add(false);

	}
	private void createTable(Statement s) throws SQLException {
		s.execute("DROP TABLE IF EXISTS FANATYCY_WEDKARSTWA");
		s.execute("CREATE TABLE FANATYCY_WEDKARSTWA (ID INT IDENTITY PRIMARY KEY, IMIE_NAZWISKO VARCHAR(255), DATA_UZYSKANIA_LICENCJI VARCHAR(255), CZY_W_ZWIAZKU BOOLEAN)");
	}
	private void createTable2(Statement s) throws SQLException {
		s.execute("DROP TABLE IF EXISTS ZLOWIONE_RYBY");
		s.execute("CREATE TABLE ZLOWIONE_RYBY (ID INT IDENTITY PRIMARY KEY, GATUNEK VARCHAR(255), ROZMIAR_CM INT, CZY_ZWERYFIKOWANA BOOLEAN, DATA_ZLOWIENIA DATE, FANATYK_WEDKARSTWA INT, FOREIGN KEY (FANATYK_WEDKARSTWA) REFERENCES FANATYCY_WEDKARSTWA(ID))");
	}


	TableView tableView = new TableView();
	TableView tableView2 = new TableView();


	public void loadData(ActionEvent actionEvent) throws SQLException {

		createTable(s);
		createTable2(s);

		fanatyk.setDisable(false);

		createDataFanatycy();
		createDataRyby();

		String tableQuery = "SELECT * FROM FANATYCY_WEDKARSTWA";


		try (PreparedStatement tableQueryPS = c.prepareStatement(tableQuery)) {
			ObservableList data = FXCollections.observableArrayList();
			Tab tab1=new Tab("FANATYCY_WEDKARSTWA");
			tabPane.getTabs().add(tab1);
			tab1.setContent(tableView);

			String dataQuery = "SELECT * FROM FANATYCY_WEDKARSTWA" ;
			ResultSet tableValues = s.executeQuery(dataQuery);

			for (int i = 0; i < tableValues.getMetaData().getColumnCount(); i++) {
				final int j = i;
				TableColumn tableColumn = new TableColumn(tableValues.getMetaData().getColumnName(i + 1));
				tableColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param ->
						new SimpleStringProperty(param.getValue().get(j).toString()));
				tableView.getColumns().addAll(tableColumn);

			}


			int rowCounter = 0;
			while (tableValues.next()) {
				ObservableList<String> row = FXCollections.observableArrayList();
				for (int i = 1; i <= tableValues.getMetaData().getColumnCount(); i++) {
					row.add(tableValues.getString(i));
				}

				data.add(row);
				rowCounter++;
			}
			tableView.getItems().addAll(data);
			data = null;

			String columnQuery = "SELECT * FROM FANATYCY_WEDKARSTWA";
			try (PreparedStatement columnQueryPS = c.prepareStatement(columnQuery)) {
				ResultSet columnNames = columnQueryPS.executeQuery();



			} catch (SQLException columnQueryException) {
				System.err.println(columnQueryException.toString());
			}
		}catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		tableQuery = "SELECT * FROM ZLOWIONE_RYBY";

		try (PreparedStatement tableQueryPS = c.prepareStatement(tableQuery)) {
			ObservableList data = FXCollections.observableArrayList();
			Tab tab2=new Tab("ZLOWIONE_RYBY");
			tabPane.getTabs().add(tab2);
			tab2.setContent(tableView2);

			String dataQuery = "SELECT * FROM ZLOWIONE_RYBY" ;
			ResultSet tableValues = s.executeQuery(dataQuery);

			for (int i = 0; i < tableValues.getMetaData().getColumnCount(); i++) {
				final int j = i;
				TableColumn tableColumn = new TableColumn(tableValues.getMetaData().getColumnName(i + 1));
				tableColumn.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param ->
						new SimpleStringProperty(param.getValue().get(j).toString()));
				tableView2.getColumns().addAll(tableColumn);

			}


			int rowCounter = 0;
			while (tableValues.next()) {
				ObservableList<String> row = FXCollections.observableArrayList();
				for (int i = 1; i <= tableValues.getMetaData().getColumnCount(); i++) {
					row.add(tableValues.getString(i));
				}

				data.add(row);
				rowCounter++;
			}
			tableView2.getItems().addAll(data);
			data = null;

			String columnQuery = "SELECT * FROM ZLOWIONE_RYBY";
			try (PreparedStatement columnQueryPS = c.prepareStatement(columnQuery)) {
				ResultSet columnNames = columnQueryPS.executeQuery();



			} catch (SQLException columnQueryException) {
				System.err.println(columnQueryException.toString());
			}
		}catch (SQLException throwables) {
			throwables.printStackTrace();
		}

		chooseButton.setDisable(true);

	}

	private void createDataRyby() {
		gatunek.add("Szczupak");
		gatunek.add("Karp");
		gatunek.add("Okoń");
		gatunek.add("Sielawa");
		gatunek.add("Węgorz");
		gatunek.add("Sum");
		gatunek.add("Leszcz");
		gatunek.add("Karaś");
		gatunek.add("Leszcz");

		zweryfikowana.add(true);
		zweryfikowana.add(false);

		data_zlowienia.add("2020-10-15");
		data_zlowienia.add("2021-03-07");
		data_zlowienia.add("2019-02-23");
		data_zlowienia.add("2020-11-29");
		data_zlowienia.add("2020-05-02");
		data_zlowienia.add("2021-12-16");
		data_zlowienia.add("2020-08-22");
		data_zlowienia.add("2021-01-02");
		data_zlowienia.add("2020-04-15");
		data_zlowienia.add("2020-09-23");


	}







	public void showchange(String nazwa, TableView view) throws SQLException {

		String tableQuery="";
		if (nazwa=="FANATYCY_WEDKARSTWA") {

			tableQuery = "SELECT * FROM FANATYCY_WEDKARSTWA";
		}else if(nazwa=="ZLOWIONE_RYBY"){
			tableQuery = "SELECT ID, GATUNEK, ROZMIAR_CM, CZY_ZWERYFIKOWANA, DATA_ZLOWIENIA, F.IMIE_NAZWISKO FROM ZLOWIONE_RYBY R INNER JOIN FANATYCY_WEDKARSTWA F ON R.FANATYK_WEDKARSTWA=F.ID";
		}

		view.getItems().clear();

		try (PreparedStatement tableQueryPS = c.prepareStatement(tableQuery)) {
			ObservableList data = FXCollections.observableArrayList();


			String dataQuery=tableQuery;

			ResultSet tableValues = s.executeQuery(dataQuery);


			int rowCounter = 0;
			while (tableValues.next()) {
				ObservableList<String> row = FXCollections.observableArrayList();
				for (int i = 1; i <= tableValues.getMetaData().getColumnCount(); i++) {
					row.add(tableValues.getString(i));
				}

				data.add(row);
				rowCounter++;
			}
			view.getItems().addAll(data);
			data = null;

			String columnQuery = tableQuery;
			try (PreparedStatement columnQueryPS = c.prepareStatement(columnQuery)) {
				ResultSet columnNames = columnQueryPS.executeQuery();



			} catch (SQLException columnQueryException) {
				System.err.println(columnQueryException.toString());
			}
		}catch (SQLException throwables) {
			throwables.printStackTrace();
		}



	}


	public void addfanatyk(ActionEvent actionEvent) throws SQLException {


		Random random_method = new Random();



		int a = random_method.nextInt(imiona.size());
		String imie = imiona.get(a);
		imiona.remove(a);

		uzyte_imiona.add(imie);

		if (imiona.size()==0){
			fanatyk.setDisable(true);
		}

		int b = random_method.nextInt(data_licencji.size());
		String data_l = data_licencji.get(b);


		int c = random_method.nextInt(zwiazek.size());
		Boolean czy_z = zwiazek.get(c);


		String data_line= "INSERT INTO FANATYCY_WEDKARSTWA (IMIE_NAZWISKO, DATA_UZYSKANIA_LICENCJI, CZY_W_ZWIAZKU) VALUES ('"+imie+"', '"+data_l+"', "+czy_z+")";
		s.execute(data_line);
		showchange("FANATYCY_WEDKARSTWA", tableView);

		ryba.setDisable(false);
	}

	public void addryba(ActionEvent actionEvent) throws SQLException {
		Random random_method = new Random();



		int a = random_method.nextInt(gatunek.size());
		String nazwa = gatunek.get(a);




		int b = random_method.nextInt(60);

		if (b==0){
			b=b+1;
		}

		int c = random_method.nextInt(zweryfikowana.size());
		Boolean czy_z = zweryfikowana.get(c);


		int d= random_method.nextInt(data_zlowienia.size());
		String data_z= data_zlowienia.get(d);

		int e = random_method.nextInt(uzyte_imiona.size());
		String imie_fanatyka=uzyte_imiona.get(e);


		String data_line= "INSERT INTO ZLOWIONE_RYBY (GATUNEK, ROZMIAR_CM, CZY_ZWERYFIKOWANA, DATA_ZLOWIENIA, FANATYK_WEDKARSTWA) SELECT '"+nazwa+"', "+b+", "+czy_z+", '"+data_z+"', ID FROM FANATYCY_WEDKARSTWA WHERE IMIE_NAZWISKO= '"+imie_fanatyka+"'";
		s.execute(data_line);
		showchange("ZLOWIONE_RYBY", tableView2);
	}

}
