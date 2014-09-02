package selectors;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

import logic.UrlMapBuilder;

import org.jfree.data.category.DefaultCategoryDataset;

import parsers.DelayParser;
import parsers.DistanceParser;
import parsers.ParserInterface;
import entities.Delay;
import entities.Route;
import entities.TimeTableStop;
import gui.RouteDetailsWindow;

/**
 * 
 * Trieda na ziskanie detailov o vybratej trase, vykreslenie mapy, grafu
 * meskania a podrobneho cestovenho poriadku
 * 
 * 
 * @author Slavomir Sarik
 * 
 */
public class TimeTableSelector extends Selector {

	private String NO_INTERNET_IMAGE = "/no_internet.jpg";

	private List<TimeTableStop> timeTableStopList;
	private RouteDetailsWindow routeDetailWindow;
	private String url;
	private Route route;
	private DefaultCategoryDataset dataset;
	private UrlMapBuilder uMapBuilder;
	private ParserInterface distanceParser;

	/**
	 * Konstruktor
	 * 
	 * @param list
	 *            zoznam zastavok
	 * @param route
	 *            podrobnosti o trase
	 */
	public TimeTableSelector(List<TimeTableStop> list, Route route) {
		super();
		this.timeTableStopList = list;
		this.route = route;
		init();
	}

	/**
	 * Vykreslenie okna + loading animacia
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void init() {
		Thread distanceParserThread, urlBuilderThread, delayParserThread, thread1;

		// inicializovanie a spustenie animacie
		this.infoTexts = new String[] { "Please wait.", "Please wait..",
				"Please wait..." };
		loadingThread.start();

		// vlakno na parsovanie vzdialenosti medzi stanicami
		distanceParserThread = new Thread(new Runnable() {

			@Override
			public void run() {
				distanceParser = new DistanceParser(timeTableStopList);
				try {
					distanceParser.parse();

				} catch (NullPointerException e) {
					logger.error("Chyba pri ziskavani vzdialenosti: ", e);

				} catch (MalformedURLException e) {
					logger.error("Chyba pri ziskavani vzdialenosti: ", e);

				} catch (IOException e) {
					logger.error("Chyba pri ziskavani vzdialenosti: ", e);

				}

			}

		});
		distanceParserThread.start();

		// vlakno na ziskanie URL pre google mapu
		urlBuilderThread = new Thread(new Runnable() {

			@Override
			public void run() {
				uMapBuilder = new UrlMapBuilder(timeTableStopList);
				uMapBuilder.buildStringUrlImage();
			}

		});
		urlBuilderThread.start();

		// vlakno na ziskanie statistik meskania vlaku
		delayParserThread = new Thread(new Runnable() {

			@Override
			public void run() {
				DelayParser delayparser = new DelayParser(route.from, route.to,
						route.trainNumber);

				try {
					delayparser.parse();

					List<Delay> delayList = delayparser.getResults();

					dataset = new DefaultCategoryDataset();
					for (Delay d : delayList) {
						dataset.setValue(d.getDelayTime(), "", d.getStation());
					}
				} catch (NullPointerException e) {
					logger.error("Chyba pri ziskavani meskania vlaku", e);

				} catch (MalformedURLException e) {
					logger.error("Chyba pri ziskavani meskania vlaku", e);

				} catch (IOException e) {
					logger.error("Chyba pri ziskavani meskania vlaku", e);

				}
			}

		});
		delayParserThread.start();

		try {
			distanceParserThread.join();
		} catch (InterruptedException e) {
			logger.error("Interrupted Thread error - join(): ", e);
		}

		try {
			urlBuilderThread.join();
		} catch (InterruptedException e) {
			logger.error("Interrupted Thread error - join(): ", e);
		}

		try {
			delayParserThread.join();
		} catch (InterruptedException e) {
			logger.error("Interrupted Thread error - join(): ", e);
		}

		timeTableStopList = (List<TimeTableStop>) distanceParser.getResults();
		url = uMapBuilder.getUrl();

		// vlakno pre vytvorenie a inicializovanie okna
		thread1 = new Thread(new Runnable() {

			@Override
			public void run() {
				routeDetailWindow = new RouteDetailsWindow();
				registerComponents();
			}

		});
		thread1.start();
		try {
			thread1.join();
		} catch (InterruptedException e) {
			logger.error("Interrupted Thread error - join(): ", e);
		}

		// ukoncenie animacie
		running = false;
	}

	/**
	 * Inicializuju sa operacie pre componenty GUI
	 */
	@Override
	public void registerComponents() {
		BufferedImage googleMapImage = null;

		// stlpce tabulky
		final String columns[] = { "Oznacenie", "Zastavka", "Prichod",
				"Odchod", "Vzdialenost" };

		// obsah tabulky
		String data[][] = getTableValues(timeTableStopList);

		// vytvorenie tabulky
		logger.debug("Vykreslovanie tabulky");
		routeDetailWindow.getTable().setModel(
				new DefaultTableModel(data, columns));
		routeDetailWindow.getTable().setEnabled(false);
		routeDetailWindow.getTable().getColumnModel().getColumn(1)
				.setMinWidth(150);
		setTableAlignment(routeDetailWindow.getTable());

		// nacitanie mapy
		try {
			googleMapImage = ImageIO.read(new URL(url));
			logger.debug("Nacitanie mapy z url");

			// zobrazenie mapy
			routeDetailWindow.getLblGooglemap().setIcon(
					new ImageIcon(googleMapImage));
			logger.debug("Vykreslovanie mapy");

		} catch (MalformedURLException e) {
			logger.error("Google map MalformedURL error: ", e);
			routeDetailWindow.getLblGooglemap().setIcon(
					new ImageIcon(this.getClass()
							.getResource(NO_INTERNET_IMAGE)));

		} catch (IOException e) {
			logger.error("Google map IO error: ", e);
			routeDetailWindow.getLblGooglemap().setIcon(
					new ImageIcon(this.getClass()
							.getResource(NO_INTERNET_IMAGE)));
		}

		/* zozbrazenie detailov o trase v okne */
		routeDetailWindow.getLblRouteDetails().setText(
				"Detaily spojenia: " + route.trainType + route.trainNumber
						+ " " + route.trainName);

		/* vytvori sa graf meskania NULL dataset nesposobuje pad */
		logger.debug("Vykreslovanie grafu meskania");
		routeDetailWindow.createChartBar(dataset);

	}

	/**
	 * vytvori vektor pre tabulku
	 * 
	 * @param list
	 *            zoznam zastavok
	 * @return vektor pre tabulku
	 */
	public String[][] getTableValues(List<TimeTableStop> list) {
		String[][] data;
		int count = 0;
		int i = 0;

		logger.debug("Inicializovanie dat tabulky");

		// zistenie poctu zastavok, na ktorych vlak stoji
		for (TimeTableStop stop : list)
			if (stop.isStops() == true)
				count++;

		// vytvorenie vektora
		data = new String[count][];

		// inicializovanie vektora
		for (TimeTableStop stop : list) {
			if (stop.isStops() == true) {
				// vektor pozostava z oznacenia znacky na mape, nazvu stanice,
				// prichodu / odchodu zo stanice a vzdialenosti do danej stanice
				data[i] = new String[] { Character.toString((char) (65 + i)),
						stop.getStation(), stop.getArrivalTime(),
						stop.getDepartureTime(),
						Integer.toString(stop.getDistance()) + " km" };
				i++;
			}
		}
		return data;
	}
}