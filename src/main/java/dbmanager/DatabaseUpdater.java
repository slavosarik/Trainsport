package dbmanager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import parsers.ParserInterface;
import parsers.StationParser;
import parsers.TrainParser;
import entities.Train;
import entities.TrainStop;
import gui.LoadingAnimation;

/**
 * Trieda pre spravu aktualizacie databazy - nacitanie vlakov a stanic do
 * databazy odznova
 * 
 * @author Slavomir Sarik
 * 
 */
public class DatabaseUpdater {

	private Thread loadingAnimaThread;
	private Thread thread1;
	private Thread thread2;
	private Thread thread3;
	private ParserInterface trainParser;
	private ParserInterface stationParser;
	private List<Train> trainList;
	private List<TrainStop> trainStopList;

	public LoadingAnimation loading;
	public volatile boolean running;

	private Logger logger = Logger.getLogger(DatabaseUpdater.class);

	/**
	 * Kontruktor pre inicializaciu
	 */
	public DatabaseUpdater() {
		init();
	}

	/**
	 * Inicializacia a spustenie aktualizacie databazy
	 */
	private void init() {
		running = true;

		// vykreslenie a spustenie animacie
		loadingAnimaThread = new Thread(new Runnable() {

			@Override
			public void run() {
				int i = 0;

				final String[] infoTexts = {
						"Database is being updated, please wait.",
						"Database is being updated, please wait..",
						"Database is being updated, please wait..." };

				loading = new LoadingAnimation();

				logger.debug("Spustenie loading animacie");

				while (running) {
					loading.getLblText().setText(infoTexts[(++i) % 3]);

					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						logger.error(
								"Error - Interrupted, Doslo k chybe v loading animacii",
								e);
					}
				}

				loading.getFrame().dispose();
				logger.debug("Ukoncenie loading animacie");

			}
		});
		loadingAnimaThread.start();

		// vymazanie a vytvorenie novej databazy
		try {
			DatabaseManager.getInstance().initDatabaseSystem();

		} catch (SQLException e) {
			logger.error("Databaza je prazna, nemozno vykonat opravu chyb", e);
			running = false;

			JOptionPane.showMessageDialog(null,
					"Error has occcured during updating database",
					"Update Error", JOptionPane.ERROR_MESSAGE);
			return;

		}

		// parsovanie vlakov
		thread1 = new Thread(new Runnable() {

			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				trainParser = new TrainParser();
				try {
					logger.info("Prebieha parsovanie vlakov");
					trainParser.parse();
					trainList = (List<Train>) trainParser.getResults();
					logger.info("Parsovanie vlakov dokoncene");

				} catch (MalformedURLException e) {
					logger.error("Chyba pri parsovani vlakov", e);

				} catch (IOException e) {
					logger.error("Chyba pri parsovani vlakov", e);

				} catch (NullPointerException e) {
					logger.error("Chyba pri parsovani vlakov", e);

				}

			}
		});
		thread1.start();

		try {
			thread1.join();
		} catch (InterruptedException e) {
			logger.error("Interrupted Thread error - join(): ", e);
		}

		// vlozenie vlakov do databazy a nasledne parsovanie stanic
		thread2 = new Thread(new Runnable() {

			@SuppressWarnings("unchecked")
			@Override
			public void run() {

				try {
					// vlozenie vlakov do databazy
					logger.info("Vkladanie vlakov do databazy");
					DatabaseManager.getInstance().initializeTrainDb(trainList);

				} catch (NullPointerException e) {
					logger.error(
							"Pokus o zapisanie prazdneho zoznamu vlakov do databazy",
							e);

				}
				// vytvorenie a incializacia parsera na stanice
				stationParser = new StationParser(trainList);

				try {
					// parsovanie udajov
					logger.info("Prebieha parsovanie stanic");
					stationParser.parse();
					trainStopList = (List<TrainStop>) stationParser
							.getResults();
					logger.info("Parsovanie stanic dokoncene");

				} catch (MalformedURLException e) {
					logger.error("Chyba pri parsovani zastavok", e);

				} catch (IOException e) {
					logger.error("Chyba pri parsovani zastavok", e);

				} catch (NullPointerException e) {
					logger.error("Chyba pri parsovani zastavok", e);

				}

			}

		});
		thread2.start();

		try {
			thread2.join();

		} catch (InterruptedException e) {
			logger.error("Interrupted Thread error - join(): ", e);

		}

		// vlozenie stanic do databazy a vykonanie naslednych oprav bugov
		thread3 = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					logger.info("Prebieha vkladanie stanic do databazy");
					// vlozenie stanic do databazy
					DatabaseManager.getInstance().initStationDb(trainStopList);

					// vykonanie oprav nad databazou stanic
					DatabaseManager.getInstance().bugFixes();

					logger.info("Databaza zaktualizovana");

				} catch (NullPointerException e) {
					logger.error(
							"Databaza je prazna, nemozno vykonat opravu chyb",
							e);

					JOptionPane.showMessageDialog(null,
							"Error has occcured during updating database",
							"Update Error", JOptionPane.ERROR_MESSAGE);

				}
			}

		});
		thread3.start();
		try {
			thread3.join();
		} catch (InterruptedException e) {
			logger.error("Interrupted Thread error - join(): ", e);
		}

		// ukoncenie animacie
		running = false;

	}
}