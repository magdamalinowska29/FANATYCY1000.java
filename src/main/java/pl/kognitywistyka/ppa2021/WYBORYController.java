package pl.kognitywistyka.ppa2021;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

public class WYBORYController {

	@FXML
	Button chooseButton;

	@FXML
	Button showData;
	@FXML TabPane tabPane;
	@FXML Button przeprowadzture1;

	@FXML Button przeprowadzture2;

	@FXML Button zatwierdz;





	Connection c = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", "");
	Statement s = c.createStatement();

	public WYBORYController() throws SQLException {
	}


	private void createTable(Statement s) throws SQLException {
		s.execute("DROP TABLE IF EXISTS KANDYDAT_WYBORY");
		s.execute("CREATE TABLE KANDYDAT_WYBORY (ID INT IDENTITY PRIMARY KEY, IMIE VARCHAR(255), NAZWISKO VARCHAR(255), WYNIK_1_TURA  FLOAT(24), WYNIK_2_TURA FLOAT(24), CZY_WYGRAL BOOLEAN)");
	}

	private void PopulateTable(Statement s) throws SQLException {
		s.execute("INSERT INTO KANDYDAT_WYBORY (IMIE, NAZWISKO, WYNIK_1_TURA, WYNIK_2_TURA, CZY_WYGRAL) VALUES ('ADAM', 'KONIECZKO', 0, 0, FALSE )");
		s.execute("INSERT INTO KANDYDAT_WYBORY (IMIE, NAZWISKO, WYNIK_1_TURA, WYNIK_2_TURA, CZY_WYGRAL) VALUES ('ANNA', 'CHALABUDA', 0, 0, FALSE)");
		s.execute("INSERT INTO KANDYDAT_WYBORY (IMIE, NAZWISKO, WYNIK_1_TURA, WYNIK_2_TURA, CZY_WYGRAL) VALUES ('PAWEL', 'SITEK', 0, 0, FALSE)");
		s.execute("INSERT INTO KANDYDAT_WYBORY (IMIE, NAZWISKO, WYNIK_1_TURA, WYNIK_2_TURA, CZY_WYGRAL) VALUES ('MARIA', 'LABKO', 0, 0, FALSE)");
		s.execute("INSERT INTO KANDYDAT_WYBORY (IMIE, NAZWISKO, WYNIK_1_TURA, WYNIK_2_TURA, CZY_WYGRAL) VALUES ('CELINA', 'ZYCH', 0, 0, FALSE)");
	}

	private void wynik1Table(Statement s) throws SQLException {
		s.execute("UPDATE KANDYDAT_WYBORY SET WYNIK_1_TURA=40 WHERE IMIE='ADAM'");
		s.execute("UPDATE KANDYDAT_WYBORY SET WYNIK_1_TURA=20 WHERE IMIE='ANNA'");
		s.execute("UPDATE KANDYDAT_WYBORY SET WYNIK_1_TURA=15 WHERE IMIE='PAWEL'");
		s.execute("UPDATE KANDYDAT_WYBORY SET WYNIK_1_TURA=12 WHERE IMIE='MARIA'");
		s.execute("UPDATE KANDYDAT_WYBORY SET WYNIK_1_TURA=13 WHERE IMIE='CELINA'");
	}

	private void wynik2Table(Statement s) throws SQLException {
		s.execute("UPDATE KANDYDAT_WYBORY SET WYNIK_2_TURA=55 WHERE IMIE='ANNA'");
		s.execute("UPDATE KANDYDAT_WYBORY SET WYNIK_2_TURA=45 WHERE IMIE='ADAM'");
		s.execute("UPDATE KANDYDAT_WYBORY SET WYNIK_2_TURA=0 WHERE IMIE='CELINA'");
		s.execute("UPDATE KANDYDAT_WYBORY SET WYNIK_2_TURA=0 WHERE IMIE='MARIA'");
		s.execute("UPDATE KANDYDAT_WYBORY SET WYNIK_2_TURA=0 WHERE IMIE='PAWEL'");

	}

	private void czywygralTable(Statement s) throws SQLException{
		s.execute("UPDATE KANDYDAT_WYBORY SET CZY_WYGRAL=FALSE WHERE WYNIK_2_TURA=0 ");
		s.execute("UPDATE KANDYDAT_WYBORY SET CZY_WYGRAL=FALSE WHERE WYNIK_2_TURA=45 ");
		s.execute("UPDATE KANDYDAT_WYBORY SET CZY_WYGRAL=TRUE WHERE WYNIK_2_TURA=55 ");
	}

	private void updatewyniki3(Statement s) throws SQLException{
		s.execute("UPDATE KANDYDAT_WYBORY SET WYNIK_2_TURA=45 WHERE IMIE='ANNA'");
		s.execute("UPDATE KANDYDAT_WYBORY SET WYNIK_2_TURA=55 WHERE IMIE='ADAM'");
	}





	TableView tableView = new TableView();

	public void loadData(ActionEvent actionEvent) throws SQLException {

		createTable(s);
		PopulateTable(s);
		preparePokazDane();
		chooseButton.setDisable(true);

	}

	private void preparePokazDane() {
		showData.setDisable(false);

	}

	private void preparetura1() {
		przeprowadzture1.setDisable(false);

	}

	public float[] Rand() {

		int count = 5;
		int sum = 100;
		java.util.Random g = new java.util.Random();

		float vals[] = new float[count];
		sum -= count;

		for (int i = 0; i < count-1; ++i) {
			vals[i] = g.nextInt(sum);
		}
		vals[count-1] = sum;

		java.util.Arrays.sort(vals);
		for (int i = count-1; i > 0; --i) {
			vals[i] -= vals[i-1];
		}
		for (int i = 0; i < count; ++i) { ++vals[i]; }


		return vals;
	}

	public float[] Rand2() {

		int count = 2;
		int sum = 100;
		java.util.Random g = new java.util.Random();

		float vals[] = new float[count];
		sum -= count;

		for (int i = 0; i < count-1; ++i) {
			vals[i] = g.nextInt(sum);
		}
		vals[count-1] = sum;

		java.util.Arrays.sort(vals);
		for (int i = count-1; i > 0; --i) {
			vals[i] -= vals[i-1];
		}
		for (int i = 0; i < count; ++i) { ++vals[i]; }


		return vals;
	}




	public void showData() throws SQLException {

		String tableQuery = "SELECT * FROM KANDYDAT_WYBORY";

		try (PreparedStatement tableQueryPS = c.prepareStatement(tableQuery)) {
			ObservableList data = FXCollections.observableArrayList();
			Tab tab=new Tab("KANDYDAT_WYBORY");
			tabPane.getTabs().add(tab);
			tab.setContent(tableView);

			String dataQuery = "SELECT * FROM KANDYDAT_WYBORY" ;
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

			String columnQuery = "SELECT * FROM KANDYDAT_WYBORY";
			try (PreparedStatement columnQueryPS = c.prepareStatement(columnQuery)) {
				ResultSet columnNames = columnQueryPS.executeQuery();



			} catch (SQLException columnQueryException) {
				System.err.println(columnQueryException.toString());
			}
		}catch (SQLException throwables) {
			throwables.printStackTrace();
			}



	}

	public void showchange() throws SQLException {

		String tableQuery = "SELECT * FROM KANDYDAT_WYBORY";

		try (PreparedStatement tableQueryPS = c.prepareStatement(tableQuery)) {
			ObservableList data = FXCollections.observableArrayList();


			String dataQuery = "SELECT * FROM KANDYDAT_WYBORY" ;
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
			tableView.getItems().addAll(data);
			data = null;

			String columnQuery = "SELECT * FROM KANDYDAT_WYBORY";
			try (PreparedStatement columnQueryPS = c.prepareStatement(columnQuery)) {
				ResultSet columnNames = columnQueryPS.executeQuery();



			} catch (SQLException columnQueryException) {
				System.err.println(columnQueryException.toString());
			}
		}catch (SQLException throwables) {
			throwables.printStackTrace();
		}



	}




	public void tura1(ActionEvent actionEvent) throws SQLException {
		float[] wyniki=Rand();

		for (int i=0; i<5;i++){
			String query="UPDATE KANDYDAT_WYBORY SET WYNIK_1_TURA="+String.valueOf(wyniki[i])+ " WHERE ID="+ String.valueOf(i);

			s.execute(query);
		}
		tableView.getItems().clear();
		showchange();

		preparetura2();


	}

	private void preparetura2() {
		przeprowadzture2.setDisable(false);
	}


	public void tura2(ActionEvent actionEvent) throws SQLException {
		przeprowadzture1.setDisable(true);

		List<Object[]> results = new ArrayList<>();


		try (ResultSet rs = s.executeQuery("SELECT * FROM KANDYDAT_WYBORY ORDER BY WYNIK_1_TURA DESC LIMIT 2")) {
			while (rs.next()) {
				Object[] row = new Object[1];
				row[0] = rs.getInt("ID");

				results.add(row);
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}

		float[] wyniki=Rand2();

		String query1="UPDATE KANDYDAT_WYBORY SET WYNIK_2_TURA="+String.valueOf(wyniki[0])+ " WHERE ID="+ String.valueOf(results.get(0)[0]);

		s.execute(query1);

		String query2="UPDATE KANDYDAT_WYBORY SET WYNIK_2_TURA="+String.valueOf(wyniki[1])+ " WHERE ID="+ String.valueOf(results.get(1)[0]);

		s.execute(query2);

		tableView.getItems().clear();
		showchange();

		zatwierdz.setDisable(false);


	}

	public void zatwierdz(ActionEvent actionEvent) throws SQLException {
		przeprowadzture2.setDisable(true);

		List<Object[]> results = new ArrayList<>();


		try (ResultSet rs = s.executeQuery("SELECT * FROM KANDYDAT_WYBORY ORDER BY WYNIK_2_TURA DESC LIMIT 1")) {
			while (rs.next()) {
				Object[] row = new Object[1];
				row[0] = rs.getInt("ID");

				results.add(row);
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}

		String query3="UPDATE KANDYDAT_WYBORY SET CZY_WYGRAL=TRUE WHERE ID="+ String.valueOf(results.get(0)[0]);

		s.execute(query3);

		tableView.getItems().clear();
		showchange();

		zatwierdz.setDisable(true);

	}

	public void showDataTable(ActionEvent actionEvent) throws SQLException {
		tableView.getItems().clear();
		showData();
		preparetura1();
		showData.setDisable(true);
	}
}
