package selectors;

/**
 * Rozhranie pre triedy aplikacnej logiky Dochadza k inicializacii udajov a k
 * ovladanie komponentov grafickeho rozhrania a databazy
 * 
 * @author Slavomir Sarik
 * 
 */
public interface SelectorInterface {

	/**
	 * ovladanie komponentov grafickeho rozhrania a databazy
	 */
	void registerComponents();

	/**
	 * inicializacia udajov
	 */
	void init();
}