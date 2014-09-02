package selectors;

import gui.MainWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

import logic.Planner;
import logic.PlannerInterface;
import dbmanager.DatabaseManager;
import dbmanager.DatabaseUpdater;
import entities.Route;

/**
 * Trieda na inicializovanie a vykreslenie hlavneho okna a dolovanie dat z
 * databazy Spracovava poziadavku od pouzivatela na update a vyber parametrov
 * spojenia
 * 
 * @author Slavomir Sarik
 * 
 */
public class MainWindowSelector extends Selector {

	private MainWindow mainWindow;
	private List<String> checkboxList;

	/**
	 * Konstruktor - inicializacia dat a grafickeho rozhrania
	 */
	public MainWindowSelector() {
		super();
		init();
	}

	/**
	 * Spusti sa animacia "loadingu" pocas toho ako sa bude vykreslovat okno
	 */
	@Override
	public void init() {
		Thread thread1;

		// inicializovanie a spustenie animacie
		this.infoTexts = new String[] { "Please wait.", "Please wait..",
				"Please wait..." };
		loadingThread.start();

		// vlakno pre vytvorenie a inicializovanie okna
		thread1 = new Thread(new Runnable() {

			@Override
			public void run() {
				logger.info("Vykresluje sa okno");
				mainWindow = new MainWindow();
				logger.info("Inicializuju sa prvky okna");
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

		// metoda na inicializovanie a resetnutie comboboxu
		reset();

		// akcia nasledujuca po vybere vychodzej stanice
		ActionListener actionListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				final List<String> stationToList;
				String[] stationsSelected;

				// zistovanie, do ktorych stanic sa dokazem dostat z vybratej
				// vychodzej stanice
				stationToList = DatabaseManager.getInstance()
						.getSelectionStationNames(
								mainWindow.getCmbBoxStationFrom()
										.getSelectedItem().toString());
				stationsSelected = new String[stationToList.size()];
				stationToList.toArray(stationsSelected);

				// vlozenie moznych cielovych stanic do comboboxu pre cielovu
				// stanicu
				mainWindow.getCmbBoxStationTo().setModel(
						new DefaultComboBoxModel<String>(stationsSelected));
			}

		};
		mainWindow.getCmbBoxStationFrom().addActionListener(actionListener);

		// akcia tlacidla "vyhladat" - vyhladaju sa spojenia
		mainWindow.getBtnSearch().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				checkboxList = new ArrayList<String>();

				logger.info("Tlacidlo vyhladat bolo stlacene");
				for (JCheckBox jcheckbox : mainWindow.getCheckboxes()) {
					if (jcheckbox.isSelected()) {
						checkboxList.add(jcheckbox.getName());
						jcheckbox.setSelected(false);
					}
				}
				if (checkboxList.isEmpty() == true) {
					for (JCheckBox jcheckbox : mainWindow.getCheckboxes()) {
						checkboxList.add(jcheckbox.getName());
					}
				}
				procced();
			}

		});

		// akcia zresetovanie comboboxov a checkboxov
		mainWindow.getBtnReset().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				reset();
			}

		});

		/**
		 * Ukoncenie programu
		 */
		mainWindow.getFrame().addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// odpojenie databazy
				logger.info("Odpaja sa databaza");
				DatabaseManager.getInstance().closeConnection();
			}
		});

		/**
		 * Update databazy
		 */
		mainWindow.getMenuItemUpdateDatabase().addActionListener(
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						new Thread(new Runnable() {
							public void run() {
								// updatovanie databazy
								logger.info("Start aktualizacie udajov v databaze");
								new DatabaseUpdater();
								// refresh okna - znovu nacitanie stanic
								logger.info("Inicializuju sa prvky okna");
								reset();
							}
						}).start();
					}

				});
	}

	/**
	 * Vykonanie akcie pouzivatela - ziskanie hodnot a nasledne spracovanie,
	 * vypocet spojeni a nasledne odoslanie udajov do noveho okna
	 */
	private void procced() {
		final String arrivalTime;
		PlannerInterface planner = null;
		final List<Route> routeList;

		// ziskanie casu zo spinneru
		arrivalTime = mainWindow.getSpinnerArrivalTime().getValue().toString()
				.split(" ")[3];

		// vytvorenie planovaca pre cestu
		try {
			planner = new Planner(mainWindow.getCmbBoxStationFrom()
					.getSelectedItem().toString(), mainWindow
					.getCmbBoxStationTo().getSelectedItem().toString(),
					arrivalTime.substring(0, arrivalTime.lastIndexOf(":")),
					checkboxList);
		} catch (NullPointerException e) {
			JOptionPane.showMessageDialog(null,
					"Ziadna stanica nebola zvolena", null,
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		logger.info("Poziadavka - " + planner.toString());

		planner.createRoute();

		// ziskanie zoznamu moznych tras
		routeList = planner.getResults();
		logger.info("Bolo najdenych " + routeList.size() + " moznych tras");

		// spustenie noveho okna
		new Thread(new Runnable() {

			@Override
			public void run() {
				logger.debug("Otvara sa RouteSelector");
				new RouteSelector(routeList);
			}

		}).start();
	}

	/**
	 * Inicializacia a resetnutie comboboxu
	 */
	public void reset() {

		final String[] stations;
		final List<String> stationFromList;

		// nacitanie zoznamu stanic z databazy
		logger.info("Nacitavaju sa stanice z databazy");
		stationFromList = DatabaseManager.getInstance().getAllStationNames();
		stations = new String[stationFromList.size()];
		stationFromList.toArray(stations);

		// vlozenie pola stanic do comboboxu pre vychodziu stanicu
		logger.debug("Vlozenie dat do componentu CmbBoxStationFrom");

		mainWindow.getCmbBoxStationFrom().setModel(
				new DefaultComboBoxModel<String>(stations));

		mainWindow.getCmbBoxStationTo().setSelectedIndex(-1);

		for (JCheckBox jcheckbox : mainWindow.getCheckboxes()) {
			if (jcheckbox.isSelected()) {
				jcheckbox.setSelected(false);
			}
		}

	}

}