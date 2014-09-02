package parsers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Normalizer;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import entities.TimeTableStop;

/**
 * 
 * Ziskavanie vzdialenostami medzi jednotlivymi stanicami pomocou Google
 * Distance API
 * 
 * @author Slavomir Sarik
 * 
 */
public class DistanceParser implements ParserInterface {

	private List<TimeTableStop> timeTableStopList;
	private Logger logger = Logger.getLogger(DistanceParser.class);

	public DistanceParser(List<TimeTableStop> list) {
		this.timeTableStopList = list;
	}

	/**
	 * Parsovanie udajov - vzdialenosti z XML Google Directions
	 * 
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	@Override
	public void parse() throws MalformedURLException, IOException {

		final String URL_BEGINNING = "http://maps.googleapis.com/maps/api/directions/xml?";
		final String URL_END = "&sensor=false";

		String stationFrom = null;
		String stationTo;

		Document doc = null;
		int sum = 0;
		Elements elements;
		String[] distanceDetails;

		// prehladavanie stanic
		for (TimeTableStop stop : timeTableStopList) {
			// zistujem, ci vlak na danej zastavke stoji
			if (stop.isStops() == true) {
				// zistujem ci to je vychodzia stanica
				if (timeTableStopList.indexOf(stop) == 0) {
					stationFrom = stop.getStation();
					stop.setDistance(sum);
				} else {
					stationTo = stop.getStation();

					// vytvaranie url
					String url = URL_BEGINNING + "origin=" + stationFrom
							+ "&destination=" + stationTo + URL_END;
					url = Normalizer.normalize(url, Normalizer.Form.NFD);
					url = url.replaceAll("[^\\p{ASCII}]", "");
					url = url.replaceAll(" ", "%20");

					// otvaranie URL pre parsovanie vzdialenosti

					doc = Jsoup.parse(new URL(url).openStream(), "CP1250", url);

					// parsovanie vzdialenosti
					elements = doc.select("leg>distance").select("text");

					// ziskavanie dat
					try {
						distanceDetails = elements.get(0).text().split("\\s+");
					} catch (IndexOutOfBoundsException a) {
						logger.error("Google vratil prazdne XML - limit pre pocet dopytov bol prekroceny, cakanie na timeout #1");
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							logger.error("Thread Interrupted error", e);
						}
						try {
							// ziskavanie dat - druhy pokus
							doc = Jsoup.parse(new URL(url).openStream(),
									"CP1250", url);
						} catch (MalformedURLException e) {
							logger.error("Malformed URL error: " + e);
						} catch (IOException e) {
							logger.error("IO URL error: " + e);
						}

						elements = doc.select("leg>distance").select("text");
						try {
							distanceDetails = elements.get(0).text()
									.split("\\s+");
						} catch (IndexOutOfBoundsException e) {
							logger.error("Google vratil prazdne XML - limit pre pocet dopytov bol prekroceny, cakanie na timeout #2");
							try {
								Thread.sleep(3500);
							} catch (InterruptedException e1) {
								logger.error("Thread Interrupted error", e1);
							}
							try {
								// ziskavanie dat - treti pokus pokus
								doc = Jsoup.parse(new URL(url).openStream(),
										"CP1250", url);
							} catch (MalformedURLException e1) {
								logger.error("Malformed URL error: " + e1);
							} catch (IOException e1) {
								logger.error("IO URL error: " + e1);
							}

							// ziskavanie vzdialenosti
							elements = doc.select("leg>distance")
									.select("text");
							distanceDetails = elements.get(0).text()
									.split("\\s+");

						}

					}

					// nahradenie desatinne ciarky za bodku
					distanceDetails[0] = distanceDetails[0]
							.replaceAll(",", ".");

					// zistovanie ci vzdialenost je v kilometroch
					if ("km".equals(distanceDetails[1])) {
						sum = sum
								+ Math.round(Float
										.parseFloat(distanceDetails[0]));
					} else {
						sum = sum
								+ Math.round(Float
										.parseFloat(distanceDetails[0]) / 1000);
					}

					// zapisanie vzdilenosti k stanici
					stop.setDistance(sum);

					// presunutie sa na dalsiu zastavku, cyklus zacina odznovu
					stationFrom = stationTo;
				}
			}
		}

		logger.info("Parsovanie vzdialenosti dokoncene");
	}

	/**
	 * vrati zoznam zastavok s priradenymi vzdialenostami
	 * 
	 * @return zoznam zastavok so vzdialenostami
	 */
	@Override
	public List<TimeTableStop> getResults() {
		return timeTableStopList;
	}

}