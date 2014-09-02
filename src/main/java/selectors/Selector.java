package selectors;

import gui.LoadingAnimation;
import gui.TableManager;

import org.apache.log4j.Logger;

/**
 * Abstraktna trieda pre spracovavanie poziadavok od pouzivatela Definuje sa
 * loading animacia, ktora sa zobrazuje pocas spracovavania poziadaviek
 * 
 * @author Slavomir Sarik
 * 
 */
public abstract class Selector extends TableManager implements
		SelectorInterface {

	protected volatile boolean running;
	protected Logger logger;
	protected String[] infoTexts = { "Please wait.", "Please wait..",
			"Please wait..." };

	/**
	 * Konstruktor - inicializovanie loggera
	 */
	public Selector() {
		logger = Logger.getLogger(this.getClass());
	}

	/**
	 * Inicializuju sa operacie pre componenty GUI
	 */
	@Override
	public abstract void registerComponents();

	/**
	 * Vlakno pre vykreslovanie loading animacie
	 */
	Thread loadingThread = new Thread(new Runnable() {

		@Override
		public void run() {
			int i = 0;
			LoadingAnimation loading = new LoadingAnimation();
			running = true;
			logger.debug("Spustenie loading animacie");

			// vykreslovanie textu v loading animacii
			while (running) {
				loading.getLblText().setText(infoTexts[(++i) % 3]);

				// po kazdom vykresleni sa vlakno uspi
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					logger.error("Doslo k chybe v loading animacii", e);
				}
			}

			loading.getFrame().dispose();
			logger.debug("Ukoncenie loading animacie");
		}

	});
}