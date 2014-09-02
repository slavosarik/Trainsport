package parsers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

/**
 * Rozhranie pre pracu s parsermi
 * 
 * @author Slavomir Sarik
 * 
 */
public interface ParserInterface {

	/**
	 * Vykonava sa parsovanie
	 * 
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public void parse() throws MalformedURLException, IOException;

	/**
	 * Vracia vysledok parsovania
	 * 
	 * @return vysledok parsovania typu List
	 */
	public List<?> getResults();

}