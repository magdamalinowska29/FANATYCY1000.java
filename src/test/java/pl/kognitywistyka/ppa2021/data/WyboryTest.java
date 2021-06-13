package pl.kognitywistyka.ppa2021.data;


import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WyboryTest {

    @Test
    public void testConnection() {
        try (Connection c = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", "")) {

        } catch (SQLException e) {
            Assertions.fail(e);
        }
    }


    private void createTable(Statement s) throws SQLException {
        s.execute("DROP TABLE IF EXISTS KANDYDAT_WYBORY");
        s.execute("CREATE TABLE KANDYDAT_WYBORY (ID INT IDENTITY PRIMARY KEY, IMIE VARCHAR(255), NAZWISKO VARCHAR(255), WYNIK_1_TURA  FLOAT(24), WYNIK_2_TURA FLOAT(24), CZY_WYGRAL BOOLEAN)");
    }

    private void PopulateTable(Statement s) throws SQLException {
        s.execute("INSERT INTO KANDYDAT_WYBORY (IMIE, NAZWISKO) VALUES ('ADAM', 'KONIECZKO')");
        s.execute("INSERT INTO KANDYDAT_WYBORY (IMIE, NAZWISKO) VALUES ('ANNA', 'CHALABUDA')");
        s.execute("INSERT INTO KANDYDAT_WYBORY (IMIE, NAZWISKO) VALUES ('PAWEL', 'SITEK')");
        s.execute("INSERT INTO KANDYDAT_WYBORY (IMIE, NAZWISKO) VALUES ('MARIA', 'LABKO')");
        s.execute("INSERT INTO KANDYDAT_WYBORY (IMIE, NAZWISKO) VALUES ('CELINA', 'ZYCH')");
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


    @Test
    public void testCreateDatabaseStatement() {
        try (Connection c = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", "")) {
            try (Statement s = c.createStatement()) {
                createTable(s);
            }
        } catch (SQLException e) {
            Assertions.fail(e);
        }
    }

    @Test
    public void testPOpulateDatabaseStatement() {
        try (Connection c = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", "")) {
            try (Statement s = c.createStatement()) {
                createTable(s);
                PopulateTable(s);
            }
        } catch (SQLException e) {
            Assertions.fail(e);
        }
    }

    @Test
    public void testWYNIK1DatabaseStatement() {
        try (Connection c = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", "")) {
            try (Statement s = c.createStatement()) {
                createTable(s);
                PopulateTable(s);
                wynik1Table(s);
            }
        } catch (SQLException e) {
            Assertions.fail(e);
        }
    }

    @Test

    public void testselectTable() {
        try (Connection c = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", "")) {

            try (Statement s = c.createStatement()) {
                createTable(s);
                PopulateTable(s);
                wynik1Table(s);
                List<Object[]> results = new ArrayList<>();


                try (ResultSet rs = s.executeQuery("SELECT * FROM KANDYDAT_WYBORY ORDER BY WYNIK_1_TURA DESC LIMIT 2")) {
                    while (rs.next()) {
                        Object[] row = new Object[2];
                        row[0] = rs.getString("IMIE");
                        row[1] = rs.getString("NAZWISKO");

                        results.add(row);
                    }
                }
                Assertions.assertArrayEquals(new Object[]{"ADAM", "KONIECZKO"}, results.get(0));
                Assertions.assertArrayEquals(new Object[]{"ANNA", "CHALABUDA"}, results.get(1));

            }
        } catch (SQLException e) {
            Assertions.fail(e);
        }
    }





    @Test

    public void testKandydaci2Query() {
        try (Connection c = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", "")) {

            try (Statement s = c.createStatement()) {
                createTable(s);
                PopulateTable(s);
                wynik1Table(s);
                wynik2Table(s);
                List<Object[]> results = new ArrayList<>();

                c.setAutoCommit(false);
                s.execute("UPDATE KANDYDAT_WYBORY SET WYNIK_2_TURA=45 WHERE IMIE='ANNA'");
                s.execute("UPDATE KANDYDAT_WYBORY SET WYNIK_2_TURA=55 WHERE IMIE='ADAM'");


                c.rollback();


                try (ResultSet rs = s.executeQuery("SELECT * FROM KANDYDAT_WYBORY ORDER BY WYNIK_2_TURA DESC LIMIT 1")) {
                    while (rs.next()) {
                        Object[] row = new Object[3];
                        row[0] = rs.getString("IMIE");
                        row[1] = rs.getString("NAZWISKO");
                        row[2] = rs.getFloat("WYNIK_2_TURA");
                        results.add(row);
                    }
                }
                Assertions.assertArrayEquals(new Object[]{"ANNA", "CHALABUDA", (float) 55}, results.get(0));
            }
            catch (SQLException e) {
                Assertions.fail(e);
            }

            try (Statement s = c.createStatement()) {
                createTable(s);
                PopulateTable(s);
                wynik1Table(s);
                wynik2Table(s);
                List<Object[]> results = new ArrayList<>();



                s.execute("UPDATE KANDYDAT_WYBORY SET WYNIK_2_TURA=45 WHERE IMIE='ANNA'");
                s.execute("UPDATE KANDYDAT_WYBORY SET WYNIK_2_TURA=55 WHERE IMIE='ADAM'");


                c.commit();

                try (ResultSet rs = s.executeQuery("SELECT * FROM KANDYDAT_WYBORY ORDER BY WYNIK_2_TURA DESC LIMIT 1")) {
                    while (rs.next()) {
                        Object[] row = new Object[3];
                        row[0] = rs.getString("IMIE");
                        row[1] = rs.getString("NAZWISKO");
                        row[2] =rs.getFloat("WYNIK_2_TURA");
                        results.add(row);
                    }
                }
                Assertions.assertArrayEquals(new Object[] { "ADAM", "KONIECZKO", (float) 55}, results.get(0));


            }
        } catch (SQLException e) {
            Assertions.fail(e);
        }
    }

    @Test
    public void testWYNIKIDatabaseStatement() {
        try (Connection c = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", "")) {
            try (Statement s = c.createStatement()) {
                createTable(s);
                PopulateTable(s);
                wynik1Table(s);
                wynik2Table(s);
                czywygralTable(s);
            }
        } catch (SQLException e) {
            Assertions.fail(e);
        }
    }







    private ObservableList<ObservableList> data;
    private TableView tableview;




    @Test

    public void testCZYWYGRALDatabaseStatement() {
        try (Connection c = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", "")) {
            try (Statement s = c.createStatement()) {
                createTable(s);
                PopulateTable(s);
                wynik1Table(s);
                wynik2Table(s);
                czywygralTable(s);
                List<Object[]> results = new ArrayList<>();

                try (ResultSet rs = s.executeQuery("SELECT * FROM KANDYDAT_WYBORY ORDER BY WYNIK_2_TURA DESC LIMIT 1")) {
                    while (rs.next()) {
                        Object[] row = new Object[4];
                        row[0] = rs.getString("IMIE");
                        row[1] = rs.getString("NAZWISKO");
                        row[2] = rs.getFloat("WYNIK_2_TURA");
                        row[3]=rs.getBoolean("CZY_WYGRAL");

                        results.add(row);
                    }
                }
                Assertions.assertArrayEquals(new Object[]{"ANNA", "CHALABUDA", (float) 55, true}, results.get(0));
            }
        } catch (SQLException e) {
            Assertions.fail(e);
        }
    }










}