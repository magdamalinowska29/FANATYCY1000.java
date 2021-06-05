package pl.kognitywistyka.ppa2021.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DbTester {

	@Test
	public void testConnection() {
		try (Connection c = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", "")) {

		} catch (SQLException e) {
			Assertions.fail(e);
		}
	}
	
	private void createAndPopulateTable(Statement s) throws SQLException {
		s.execute("DROP TABLE IF EXISTS TEST");
		s.execute("CREATE TABLE TEST (ID INT IDENTITY PRIMARY KEY, NUM INT, TXT VARCHAR(255))");
		s.execute("INSERT INTO TEST (NUM, TXT) VALUES (14, 'blabla')");
		s.execute("INSERT INTO TEST (NUM, TXT) VALUES (22, 'haha')");
	}
	
	@Test
	public void testCreateAndPopulateDatabaseStatement() {
		try (Connection c = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", "")) {
			try (Statement s = c.createStatement()) {
				createAndPopulateTable(s);
			}
		} catch (SQLException e) {
			Assertions.fail(e);
		}
	}
	
	@Test
	public void testPopulateAndQuery() {
		try (Connection c = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", "")) {
			try (Statement s = c.createStatement()) {
				createAndPopulateTable(s);
				List<Object[]> results = new ArrayList<>();
				try (ResultSet rs = s.executeQuery("SELECT * FROM TEST")) {
					while (rs.next()) {
						Object[] row = new Object[2];
						row[0] = rs.getInt("NUM");
						row[1] = rs.getString("TXT");
						results.add(row);
					}
				}
				Assertions.assertArrayEquals(new Object[] { 14, "blabla" }, results.get(0));
				Assertions.assertArrayEquals(new Object[] { 22, "haha" }, results.get(1));
			}
		} catch (SQLException e) {
			Assertions.fail(e);
		}
	}
	
	@Test
	public void testQueryWithWhere() {
		try (Connection c = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", "")) {
			try (Statement s = c.createStatement()) {
				createAndPopulateTable(s);
				List<Object[]> results = new ArrayList<>();
				try (ResultSet rs = s.executeQuery("SELECT * FROM TEST WHERE TXT='blabla'")) {
					while (rs.next()) {
						Object[] row = new Object[2];
						row[0] = rs.getInt("NUM");
						row[1] = rs.getString("TXT");
						results.add(row);
					}
				}
				Assertions.assertArrayEquals(new Object[] { 14, "blabla" }, results.get(0));
			}
		} catch (SQLException e) {
			Assertions.fail(e);
		}
	}
	
	@Test
	public void testQueryWithWherePS() {
		try (Connection c = DriverManager.getConnection("jdbc:hsqldb:mem:mymemdb", "SA", "")) {
			try (Statement s = c.createStatement()) {			
				createAndPopulateTable(s);
			}
			try (PreparedStatement s = c.prepareStatement("INSERT INTO TEST (NUM, TXT) VALUES (?, ?)")) {
				s.setInt(1, 20);
				s.setString(2, "bee");
				s.execute();
				
				s.setInt(1, 100);
				s.setString(2, "pomidor");
				s.execute();
			}
			try (PreparedStatement s = c.prepareStatement("SELECT * FROM TEST WHERE TXT = ?")) {
				s.setString(1, "blabla");
				List<Object[]> results = new ArrayList<>();
				try (ResultSet rs = s.executeQuery()) {
					while (rs.next()) {
						Object[] row = new Object[2];
						row[0] = rs.getInt("NUM");
						row[1] = rs.getString("TXT");
						results.add(row);
					}
				}
				Assertions.assertArrayEquals(new Object[] { 14, "blabla" }, results.get(0));
			}
		} catch (SQLException e) {
			Assertions.fail(e);
		}
	}

}
