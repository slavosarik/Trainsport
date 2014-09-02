package parsers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.select.Elements;

import entities.Train;

/**
 * Parsovanie udajov o vlakoch z webovej stranky poloha.vlaku.info, ziskavanie
 * cisla, typu vlaku a jeho oznacenia
 * 
 * @author Slavomir Sarik
 * 
 */
public class TrainParser implements ParserInterface {

	private List<Train> tranList;
	private Logger logger = Logger.getLogger(TrainParser.class);

	/**
	 * Parsovanie udajov o vlakoch
	 * 
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws NullPointerException
	 */
	@Override
	public void parse() throws MalformedURLException, IOException,
			NullPointerException {

		// url pre zoznam osobnych vlakov, ktore treba zvlast vyparsovat
		String URL_PAGE = "http://poloha.vlaku.info/kategorie/Os/";

		Train train;
		String buffer;
		Document document = null;
		tranList = new ArrayList<Train>();
		List<String> urls = new ArrayList<String>();

		urls.add("http://poloha.vlaku.info/kategorie/EC/");
		urls.add("http://poloha.vlaku.info/kategorie/IC/");
		urls.add("http://poloha.vlaku.info/kategorie/EN/");
		urls.add("http://poloha.vlaku.info/kategorie/Ex/");
		urls.add("http://poloha.vlaku.info/kategorie/R/1/");
		urls.add("http://poloha.vlaku.info/kategorie/R/2/");
		urls.add("http://poloha.vlaku.info/kategorie/Zr/");
		urls.add("http://poloha.vlaku.info/kategorie/Rex/");
		urls.add("http://poloha.vlaku.info/kategorie/Os/20/");

		// otvaranie URL pre parsovanie cisel osobnych vlakov

		document = Jsoup.parse(new URL(URL_PAGE).openStream(), "CP1250",
				URL_PAGE);
		logger.debug("Otvara sa url pre osobne vlaky: " + URL_PAGE);

		// parsovanie dat
		document.outputSettings().escapeMode(EscapeMode.xhtml);
		Elements elements = document.select("a[href^=/kategorie/Os]");
		logger.debug("Parsovanie cisel vlakov pre Osobne vlaky");

		// vytvaranie url odkazov z cisel osobnych vlakov
		for (Element e : elements) {
			String arr[] = e.attributes().toString().split("\"");
			urls.add("http://poloha.vlaku.info" + arr[1]);
		}

		// prechadzanie vsetkych url a ziskavanie udajov o vlakoch
		for (String u : urls) {
			try {
				document = Jsoup.parse(new URL(u).openStream(), "CP1250", u);
				logger.info("Otvara sa url pre parsovanie udajov o vlakoch: "
						+ u);

				// parsovanie dat z otvoreneho url
				document.outputSettings().escapeMode(EscapeMode.xhtml);
				elements = document.select("table").get(1).select("tr");
				elements.remove(0);
				elements.remove(0);

				// ukladanie vlakov s atributmi do zoznamu
				for (Element e : elements) {
					buffer = e.select("strong").get(0).text();

					// zistujem ci ma dany vlak aj svoje meno
					if (e.select("strong").size() < 2) {
						train = new Train(buffer.substring(0,
								buffer.indexOf(" ")), Integer.parseInt(buffer
								.substring(buffer.indexOf(" ") + 1,
										buffer.length())));
					} else {
						train = new Train(buffer.substring(0,
								buffer.indexOf(" ")), Integer.parseInt(buffer
								.substring(buffer.indexOf(" ") + 1,
										buffer.length())), e.select("strong")
								.get(1).text());
					}

					logger.debug("Parsovanie udajov o vlaku dokoncene: "
							+ train.toString());
					tranList.add(train);
				}
			} catch (MalformedURLException e) {
				logger.error("Malformed URL error: " + e);
			} catch (IOException e) {
				logger.error("IO URL error: " + e);
			}
		}
		logger.info("Parsovanie vsetkych udajov o vlakoch dokoncene");

	}

	/**
	 * vracia zoznam vlakov
	 * 
	 * @return zoznam vlakov
	 */
	@Override
	public List<Train> getResults() {
		return tranList;
	}
}