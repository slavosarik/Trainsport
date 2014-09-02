package dbmanager;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.mysql.jdbc.PreparedStatement;

import entities.Request;
import entities.Route;
import entities.TimeTableStop;
import entities.Train;
import entities.TrainStop;

/**
 * Thrieda pre spravu a ovladanie databazy, vykonavanie dopytov
 * 
 * @author Slavomir Sarik
 * 
 */
public class DatabaseManager {

	// konfiguracne subory pre databazu - username, password a url
	private String CONFIG_FILE = "/properties/database.properties";
	private String CONFIG_FILE1 = "/properties/database1.properties";

	private static DatabaseManager instance;
	private Connection connection;
	private Statement statement;
	private PreparedStatement pStatement;

	private ResultSet resultSet;
	private Logger logger = Logger.getLogger(DatabaseManager.class);

	private DatabaseManager() {
		logger.debug("Vytvara sa instancia DatabaseManager");
	}

	/**
	 * Singleton - vracia instanciu spravcu databazy
	 * 
	 * @return instancia DatabaseManager
	 */
	public static DatabaseManager getInstance() {

		if (instance == null)
			instance = new DatabaseManager();

		return instance;
	}

	/**
	 * Vracia database connection, ak nie je otvorene, tak vytvara nove
	 * connection
	 * 
	 * @return Databse connection
	 */
	public Connection getDBConnection() {

		try {
			if (connection == null) {
				createConnection(CONFIG_FILE);
			}
		} catch (SQLException e) {
			logger.error("Nie je mozne nadviazat spojenie s databazou", e);
			JOptionPane.showMessageDialog(null,
					"Nie je mozne nadviazat spojenie s databazou",
					"Database Error", JOptionPane.ERROR_MESSAGE);

		}

		return connection;
	}

	/**
	 * Zatvaranie statementov a resultsetov
	 */
	public void disconnect() {

		try {
			if (resultSet != null)
				resultSet.close();

		} catch (SQLException e) {
			logger.error("Chyba pri zatvarani resultsetu", e);

		}

		try {
			if (statement != null)
				statement.close();

		} catch (SQLException e) {
			logger.error("Chyba pri zatvarani statementu", e);

		}

		try {
			if (pStatement != null)
				pStatement.close();

		} catch (SQLException e) {
			logger.error("Chyba pri zatvarani prepared statementu", e);

		}

	}

	/**
	 * Pripajanie sa k databaze, vytvaranie connection
	 * 
	 * @param configFile
	 *            konfiguracny subor
	 * @throws SQLException
	 */
	public void createConnection(String configFile) throws SQLException {

		Properties properties = new Properties();
		InputStream in = null;

		// konfiguracia loggera
		in = this.getClass().getResourceAsStream(configFile);

		// citanie z konfiguracneho suboru
		try {
			properties.load(in);

		} catch (IOException e) {
			logger.error("Chyba IO pri citani konfiguracneho suboru", e);

		}

		// zatavranie konfiguracneho suboru
		try {
			in.close();

		} catch (IOException e) {
			logger.error("Chyba pri zatvarani konfiguracneho suboru", e);

		}

		// ziskavanie parametrov connection
		String url = properties.getProperty("jdbc.url");
		String username = properties.getProperty("jdbc.username");
		String password = properties.getProperty("jdbc.password");

		logger.info("Vytvaram spojenie s databazou");

		// pripajanie sa k databaze
		connection = DriverManager.getConnection(url, username, password);
		connection.setAutoCommit(false);

		logger.info("User: " + username + ", Pripojene k: " + url);

	}

	/**
	 * Zatvorenie spojenia s databazou
	 */
	public void closeConnection() {

		// odpojenie statementov a resulsetu
		disconnect();

		// uzatvaranie spojenia
		try {
			if (connection != null) {
				connection.close();
				connection = null;
			}
			logger.info("Database connection bolo odpojene");

		} catch (SQLException e) {
			logger.error("Chyba pri odpajani databazy", e);

		}
	}

	/**
	 * Inicializacia databazy
	 * 
	 * @throws SQLException
	 */
	public void initDatabaseSystem() throws SQLException {

		closeConnection();

		createDatabase();

		getDBConnection();

		createTables();

		logger.info("Databaza bola vytvorena");

	}

	/**
	 * Vytvorenie databazy, jej drop-nutie ak existovala predtym
	 * 
	 * @throws SQLException
	 */
	public void createDatabase() throws SQLException {

		createConnection(CONFIG_FILE1);

		statement = getDBConnection().createStatement();
		statement.executeUpdate("DROP DATABASE IF EXISTS trains");
		statement.execute("CREATE DATABASE trains");

		closeConnection();

	}

	/**
	 * Vytvorenie tabuliek v databaze
	 */
	public void createTables() {

		try {

			statement = getDBConnection().createStatement();

			statement
					.executeUpdate("CREATE TABLE vlaky (type VARCHAR(5), number BIGINT, name VARCHAR(20) DEFAULT NULL, PRIMARY KEY (number))");
			statement
					.executeUpdate("CREATE TABLE stanice (id BIGINT AUTO_INCREMENT, name VARCHAR(30), PRIMARY KEY (id))");
			statement
					.executeUpdate("CREATE TABLE odchody (id BIGINT AUTO_INCREMENT, train_number BIGINT, station VARCHAR(40), "
							+ "arrival TIME DEFAULT null, departure TIME, stop BOOLEAN DEFAULT FALSE, PRIMARY KEY (id),"
							+ " FOREIGN KEY (train_number) REFERENCES vlaky (number))");

		} catch (SQLException e) {
			logger.error("Chyba pri vytvarani tabuliek v databaze", e);

		} catch (NullPointerException e) {
			logger.error(
					"Chyba pri vytvarani tabuliek v databaze - ziadne spojenie s databazou",
					e);
		} finally {
			disconnect();

		}
	}

	/**
	 * Vykonaju sa opravy jednotlivych poloziek databazy
	 */
	public void bugFixes() {

		try {

			statement = getDBConnection().createStatement();

			statement
					.executeUpdate("DELETE FROM odchody WHERE station = 'Pha hl.n.L601b,L602b'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station LIKE '%Vyh.%'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station LIKE '%Odb.%'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station  LIKE '%Besa%'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station  LIKE '%Lok%'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station LIKE '%Hul%'");
			statement
					.executeUpdate("UPDATE odchody SET station = '\u0160a\u013Ea, Slovensko' WHERE station LIKE '%Sala%'");
			statement
					.executeUpdate("UPDATE odchody SET station = 'Kriv\u00E1\u0148, Detva' WHERE station LIKE '%Krivan%'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station LIKE '%dvory%'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station LIKE '%Celovce%'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station LIKE '%Krasna n/Hornadom%'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station LIKE '%Pardubice-\u010C.za B.%'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station LIKE '%\u017Dilina zr. st. Nov\u00E1 harfa%'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station LIKE '%Brand\u00FDs n.O.kol.%'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station LIKE '%Vydrnik%'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station LIKE '%Harmanec jaskyna%'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station LIKE '%Ulanka%'");
			statement
					.executeUpdate("UPDATE odchody SET station = 'Radva\u0148, Slovensko' WHERE station = 'Radva\u0148'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station LIKE '%Nalepkovo%'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station LIKE '%Mlynky%'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station  LIKE '%Dubova%'");
			statement
					.executeUpdate("UPDATE odchody SET station = '\u010Cerven\u00E1 Skala, Slovensko' WHERE station  LIKE '%Cervena skala%'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station LIKE '%Dobra pri Ciernej n/Tisou%'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station LIKE '%Modrice%'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station LIKE '%Moravany%'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station LIKE '%ser.n%'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station LIKE '%vhb.c%'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station LIKE '%Velim%'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station LIKE '%Kolin zastavka%'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station LIKE '%Lanzhot%'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station LIKE '%Lovosice jih%'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station LIKE '%Hradek z%'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station  LIKE '%Mosty u Jabl. st.hr.%'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station  LIKE '%Hranice%'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station = 'Polom'");
			statement
					.executeUpdate("UPDATE odchody SET station = 'Olomouc' WHERE station = 'Olomouc hl.n.'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station LIKE '%Krasikov %'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station LIKE '%Hostejn%'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station LIKE '%hr.VUSC%'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station LIKE '%Lukova%'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station LIKE '%Moravicany%'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station  LIKE '%Olomouc predn.%'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station  LIKE '%Mosty u %'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station  LIKE '%Ahr%'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station  LIKE '%Brnov%'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station   LIKE '%Bystricka nz%'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station   LIKE '%Leskovec z%'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station   LIKE '%Horni Lidec st.hr.%'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station  LIKE '%Strelna%'");
			statement
					.executeUpdate("DELETE FROM odchody WHERE station  LIKE '%Barca%'");

			connection.commit();

		} catch (SQLException e) {
			logger.error("Doslo k chybe pri vykonavani oprav bugov v databaze",
					e);

			// rollback
			if (connection != null) {
				try {
					logger.info("Dochadza k rollbacku");
					connection.rollback();

				} catch (SQLException e1) {
					logger.error("Doslo k chybe pri rollbacku", e1);

				}
			}

		} finally {
			disconnect();

		}
	}

	/**
	 * vlozi do tabulky zoznam vlakov s ich cislami, typmi a nazvami
	 * 
	 * @param trainList
	 *            zoznam vlakov
	 * @throws NullPointerException
	 */
	public void initializeTrainDb(List<Train> trainList)
			throws NullPointerException {

		try {
			statement = getDBConnection().createStatement();

			for (Train train : trainList) {

				// vkladanie vlakov do databazy
				if (train.getName() == null) {

					statement
							.executeUpdate("INSERT INTO vlaky(type, number) VALUES('"
									+ train.getType()
									+ "', "
									+ train.getNumber() + ")");

				} else {

					statement
							.executeUpdate("INSERT INTO vlaky(type, number, name) VALUES('"
									+ train.getType()
									+ "', "
									+ train.getNumber()
									+ ", '"
									+ train.getName() + "')");
				}
			}
			connection.commit();

		} catch (SQLException e) {

			logger.error("Doslo k chybe pri vykonavani dopytov", e);

			// rollback
			if (connection != null) {
				try {
					logger.info("Dochadza k rollbacku");
					connection.rollback();

				} catch (SQLException e1) {
					logger.error("Doslo k chybe pri rollbacku", e1);

				}
			}
		}

		finally {
			disconnect();
		}
	}

	/**
	 * obdrzi arraylist so zoznamom cisel vlakov
	 * 
	 * @return zoznam cisel vlakov
	 */
	public List<Integer> getAllTrainNumbers() {
		List<Integer> trainNumbers = new ArrayList<Integer>();

		try {
			statement = getDBConnection().createStatement();

			resultSet = statement.executeQuery("SELECT * FROM vlaky");

			while (resultSet.next())
				trainNumbers.add(resultSet.getInt("number"));

		} catch (SQLException e) {
			logger.error("Doslo k chybe pri vykonavani dopytov", e);

		} finally {
			disconnect();

		}

		return trainNumbers;
	}

	/**
	 * vrati zoznam nazvov stanic
	 * 
	 * @return zoznam nazvov stanic
	 */
	public List<String> getAllStationNames() {

		List<String> stationNameList = new ArrayList<String>();

		try {
			statement = getDBConnection().createStatement();

			ResultSet rs = statement
					.executeQuery("SELECT moj.station FROM "
							+ "(SELECT id, station FROM odchody WHERE stop = TRUE ORDER BY station, id) moj "
							+ "GROUP BY moj.station " + "HAVING COUNT(*) > 1");

			while (rs.next())
				stationNameList.add(rs.getString("station"));

		} catch (SQLException e) {
			logger.error("Doslo k chybe pri vykonavani dopytov", e);

		} catch (NullPointerException e) {
			logger.error("Doslo k chybe pri praci s databazou", e);

		} finally {
			disconnect();

		}

		return stationNameList;

	}

	/**
	 * vyberie stanice, ktore su dostupne z vybratej stanice nejakym vlakom
	 * 
	 * @param selectedStation
	 *            vychodzia stanica
	 * @return zoznam stanic, kam sa mozem dostat z vychodzej stanice
	 */
	public List<String> getSelectionStationNames(String selectedStation) {

		List<String> selectionStationNamesList = new ArrayList<String>();

		try {
			statement = getDBConnection().createStatement();

			resultSet = statement.executeQuery("SELECT station FROM odchody "
					+ " WHERE stop = true AND train_number IN "
					+ " ( SELECT train_number FROM odchody WHERE station = '"
					+ selectedStation
					+ "'  AND stop = true  GROUP BY train_number) "
					+ " GROUP BY station " + " ORDER BY station");

			while (resultSet.next())
				selectionStationNamesList.add(resultSet.getString("station"));

		} catch (SQLException e) {
			logger.error("Doslo k chybe pri vykonavani dopytov", e);

		} finally {
			disconnect();

		}

		return selectionStationNamesList;
	}

	/**
	 * inicializuje odchody a prichode v databaze
	 * 
	 * @param trainStops
	 *            zoznam prichodov a odchodov zo stanic
	 * @throws NullPointerException
	 */
	public void initStationDb(List<TrainStop> trainStops)
			throws NullPointerException {

		try {
			statement = getDBConnection().createStatement();

			// prehladavanie listu
			for (TrainStop ts : trainStops) {

				// vkladanie prichodov a odchodov do databazy
				// zistujem, ci dana stanica ma evidovany aj prichod
				if (ts.getArrivalTime() != null) {
					statement
							.executeUpdate("INSERT INTO odchody (train_number, station, arrival, departure, stop) VALUES ('"
									+ ts.getTrainNumber()
									+ "', '"
									+ ts.getStationName()
									+ "', '"
									+ ts.getArrivalTime()
									+ "','"
									+ ts.getDepartureTime()
									+ "', "
									+ ts.isStops() + ")");

				} else {

					statement
							.executeUpdate("INSERT INTO odchody (train_number, station, departure, stop) VALUES ('"
									+ ts.getTrainNumber()
									+ "', '"
									+ ts.getStationName()
									+ "', '"
									+ ts.getDepartureTime()
									+ "', "
									+ ts.isStops() + ")");
				}
			}

			connection.commit();

		} catch (SQLException e) {
			logger.error("Doslo k chybe pri vykonavani dopytov", e);

			// rollback
			if (connection != null) {
				try {
					logger.info("Dochadza k rollbacku");
					connection.rollback();

				} catch (SQLException e1) {
					logger.error("Doslo k chybe pri rollbacku", e1);

				}
			}
		} finally {
			disconnect();

		}
	}

	/**
	 * Vrati cestovny poriadok - zoznam zastavok medzi 2 stanicami s ich
	 * prichodmi a odchodmi
	 * 
	 * @param station1
	 *            vychodzia stanica
	 * @param station2
	 *            cielova stanica
	 * @param cislo
	 *            cislo vlaku
	 * @return zoznam zastavok medzi 2 stanicami
	 */
	public List<TimeTableStop> Timetable(String station1, String station2,
			int cislo) {

		List<TimeTableStop> stopList = new ArrayList<TimeTableStop>();
		String querryString = "SELECT station, arrival, departure, `stop` FROM odchody "
				+ "WHERE train_number = ? AND id >= "
				+ "(SELECT id FROM odchody WHERE train_number = ? AND station = ? AND stop = TRUE) AND id <= "
				+ "(SELECT id FROM odchody WHERE train_number = ? AND station = ? AND stop = TRUE)";

		try {

			pStatement = (PreparedStatement) getDBConnection()
					.prepareStatement(querryString);

			// inicializacia hodnot preparedStatementu
			pStatement.setString(1, Integer.toString(cislo));
			pStatement.setString(2, Integer.toString(cislo));
			pStatement.setString(3, station1);
			pStatement.setString(4, Integer.toString(cislo));
			pStatement.setString(5, station2);

			// vykonavanie dopytu
			resultSet = pStatement.executeQuery();

			// vytvaranie listu z vysledkov dopytu
			while (resultSet.next()) {
				stopList.add(new TimeTableStop(resultSet.getString("station"),
						resultSet.getString("arrival"), resultSet
								.getString("departure"), resultSet
								.getBoolean("stop")));

			}
		} catch (SQLException e) {
			logger.error("Doslo k chybe pri vykonavani dopytov", e);

		} finally {
			disconnect();

		}

		return stopList;
	}

	/**
	 * Vrati zoznam moznych tras podla kriterii prijatej poziadavky
	 * 
	 * @param request
	 *            obsahuje informacie o vychodzej a cielovej stanici, a o case
	 *            odchodu
	 * @return zoznam moznych tras
	 */
	public List<Route> fillRequest(Request request) {
		List<Route> routeList = new ArrayList<Route>();

		try {

			String querryString = "(SELECT type, number, name, z1.station AS `zo stanice`, z1.departure AS `odchod`, d1.station AS `do stanice`, "
					+ " (IFNULL(d1.arrival, d1.departure)) AS `prichod`, IF (TIMEDIFF(IFNULL(d1.arrival, d1.departure), z1.departure) < 0, "
					+ " TIMEDIFF('24:00:00', ABS(TIMEDIFF(IFNULL(d1.arrival, d1.departure) "
					+ ", z1.departure))) , TIMEDIFF(IFNULL(d1.arrival, d1.departure), z1.departure)) AS `cestovnύ θas` FROM odchody z1 "
					+ "JOIN odchody d1 ON z1.train_number = d1.train_number "
					+ "JOIN vlaky v ON v.number  = z1.train_number "
					+ "WHERE (z1.station = ? && d1.station = ? && z1.`stop` = true && d1.`stop` = true && z1.id < d1.id && z1.departure >= ?) "
					+ "GROUP BY str_to_date(z1.departure,'%H:%i')) "
					+ "UNION "
					+ "(SELECT type, number, name, z1.station AS `zo stanice`, z1.departure AS `odchod`, d1.station AS `do stanice`, "
					+ "(IFNULL(d1.arrival, d1.departure)) AS `prichod`, IF (TIMEDIFF(IFNULL(d1.arrival, d1.departure), z1.departure) < 0, "
					+ " TIMEDIFF('24:00:00', ABS(TIMEDIFF(IFNULL(d1.arrival, "
					+ "d1.departure), z1.departure))), TIMEDIFF(IFNULL(d1.arrival, d1.departure), z1.departure)) AS `cestovnύ θas` FROM odchody z1 "
					+ "JOIN odchody d1 ON z1.train_number = d1.train_number "
					+ "JOIN vlaky v ON v.number  = z1.train_number "
					+ "WHERE (z1.station = ? && d1.station = ? && z1.`stop` = true && d1.`stop` = true && z1.id < d1.id && z1.departure < ?) "
					+ "GROUP BY str_to_date(z1.departure,'%H:%i'))";

			pStatement = (PreparedStatement) getDBConnection()
					.prepareStatement(querryString);

			// inicializacia hodnot preparedStatementu
			pStatement.setString(1, request.getStationFrom());
			pStatement.setString(2, request.getStationTo());
			pStatement.setString(3, request.getArrivalTime());
			pStatement.setString(4, request.getStationFrom());
			pStatement.setString(5, request.getStationTo());
			pStatement.setString(6, request.getArrivalTime());

			// vykonavanie dopytu
			resultSet = pStatement.executeQuery();

			// vytvaranie listu z vysledkov dopytu
			while (resultSet.next()) {
				routeList.add(new Route(resultSet.getString("zo stanice"),
						resultSet.getString("do stanice"), resultSet
								.getString("odchod"), resultSet
								.getString("prichod"), resultSet
								.getString("cestovnύ θas"), resultSet
								.getString("type"), resultSet.getInt("number"),
						resultSet.getString("name")));

			}
		} catch (SQLException e) {
			logger.error("Doslo k chybe pri vykonavani dopytov", e);

		} finally {
			disconnect();

		}

		return routeList;
	}
}