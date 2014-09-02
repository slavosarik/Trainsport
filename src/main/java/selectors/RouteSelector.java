package selectors;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import dbmanager.DatabaseManager;
import entities.Route;
import entities.TimeTableStop;
import gui.SelectRouteWindow;

/**
 * Trieda na inicializovanie a vykreslenie sekundarneho okna s najdenymi moznymi
 * trasami Spracovava poziadavku od pouzivatela na zobrazenie detailov danej
 * trasy
 * 
 * 
 * @author Slavomir Sarik
 * 
 */

public class RouteSelector extends Selector {
	private List<Route> routeList;
	private SelectRouteWindow routeWindow;

	/**
	 * Konstruktor
	 * 
	 * @param routeList
	 *            zoznam tras
	 */
	public RouteSelector(List<Route> routeList) {
		super();
		this.routeList = routeList;
		init();
	}

	/**
	 * Vykreslenie okna + loading animacia
	 */
	@Override
	public void init() {
		Thread thread1;

		// inicializovanie a spustenie animacie
		this.infoTexts = new String[] { "Please wait.", "Please wait..",
				"Please wait..." };
		loadingThread.start();

		// vykreslenie a inicializovanie okna
		thread1 = new Thread(new Runnable() {

			@Override
			public void run() {
				routeWindow = new SelectRouteWindow();
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
		// prvky tabulky
		final String[][] data = new String[routeList.size()][];

		// priprava udajov pre tabulku
		int i = 0;

		// inicializacia obsahu tabulky
		for (Route route : routeList) {
			data[i++] = new String[] { route.trainType,
					Integer.toString(route.trainNumber), route.trainName,
					route.departureTime, route.arrivalTime, route.travelTime };
		}

		// inicializacia stlpcov tabulky
		final String column[] = { "Typ vlaku", "+Číslo vlaku",
				"Oznaèenie vlaku", "Odchod", "Príchod", "Dåžka cesty" };

		// vykreslenie tabulky
		logger.debug("Vykreslovanie tabulky");
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				routeWindow.getTable().setModel(
						new DefaultTableModel(data, column) {

							private static final long serialVersionUID = 6330313515938603411L;

							@Override
							public boolean isCellEditable(int row, int column) {
								return false;
							}
						});

				setTableAlignment(routeWindow.getTable());
			}
		});

		// tlacidlo na vyber konkretnej trasy
		routeWindow.getBtnShowDetails().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				final Route route;
				final List<TimeTableStop> timeTableStopList;

				// ziskanie vybrateho riadku z tabulky
				route = routeList.get(routeWindow.getTable().getSelectedRow());

				// ziskanie cestovneho poriadku - zoznamu zastavok vybranej
				// trasy
				logger.debug("Ziskavanie zoznamu zastavok z databazy");
				timeTableStopList = DatabaseManager.getInstance().Timetable(
						route.from, route.to, route.trainNumber);

				// spustenie okna pre detaily trasy
				new Thread(new Runnable() {
					@Override
					public void run() {
						logger.debug("Otvara sa TimeTableSelector");
						new TimeTableSelector(timeTableStopList, route);
					}
				}).start();

			}

		});
	}
}