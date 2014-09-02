package logic;

import java.io.InputStream;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import selectors.MainWindowSelector;

/**
 * Hlavna trieda pre spustenie programu
 * 
 * @author Slavomir Sarik
 * 
 */

public class MainClass {

	/**
	 * Spusti aplikaciu
	 * 
	 * @param args
	 *            argumenty main funkcie
	 */

	public void init() {
		// konfiguracia loggera
		InputStream is = this.getClass().getResourceAsStream(
				"/properties/log4j.properties");

		PropertyConfigurator.configure(is);

		final Logger logger = Logger.getLogger(MainClass.class);
		logger.info("Spusta sa aplikacia");

		// spustenie hlavneho okna
		new Thread(new Runnable() {

			@Override
			public void run() {
				logger.debug("Otvara sa MainWindowSelector");
				new MainWindowSelector();
			}

		}).start();
	}

	public static void main(String[] args) {
		MainClass mc = new MainClass();
		mc.init();

	}
}