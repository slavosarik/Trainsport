package parsers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.select.Elements;

import entities.Train;
import entities.TrainStop;

/**
 * 
 * Parsovanie udajov o prichodoch a odchodoch z webovej stranky
 * poloha.vlaku.info, ziskavanie casov, nazvov stanic
 * 
 * @author Slavomir Sarik
 * 
 */
public class StationParser implements ParserInterface {

	private List<Train> trainList;
	private List<TrainStop> trainStopList = new ArrayList<TrainStop>();
	private Logger logger = Logger.getLogger(StationParser.class);

	/**
	 * Konstruktor
	 * 
	 * @param trainlist
	 *            zoznam vlakov
	 */
	public StationParser(List<Train> trainlist) {
		this.trainList = trainlist;
	}

	/**
	 * Parsovanie udajov o prichodoch a odchodoch
	 * 
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws NullPointerException
	 */
	@Override
	public void parse() throws MalformedURLException, IOException,
			NullPointerException {

		TrainStop trainStop;
		String buffer1;
		String buffer2;

		for (Train train : trainList) {
			Document doc = null;

			// skladanie url, pre cestovny poriadok vlaku
			String url = "http://poloha.vlaku.info/vlak/"
					+ Integer.toString(train.getNumber()) + "/";

			// priprava dokumentu na parsing

			doc = Jsoup.parse(new URL(url).openStream(), "CP1250", url);
			logger.debug("Otvaram url pre parsovanie prichodov a odchodov: "
					+ url);

			// parsovanie dat o prichodoch a odchodoch
			doc.outputSettings().escapeMode(EscapeMode.xhtml);
			// TODO NEJAKY BUG PRI PARSOVANI!!! INDEX OUT OF ARRAY
			Elements elements = doc.select("table").get(1).select("tr");
			elements.remove(0);
			elements.remove(0);

			// prechadzanie zoznamom zastavok
			for (int j = 0; j < elements.size(); j++) {

				// zistovanie ci je to platna zastavka
				if (elements.get(j).select("strong:contains(#)").isEmpty() == false)
					continue;

				// buffer1 predstavuje aktualnu zastavku
				buffer1 = elements.get(j).select("td:gt(0)").select("td:lt(3)")
						.first().text();
				buffer1 = buffer1.replace(String.valueOf((char) 160), " ");

				// buffer2 predstavuje nasledujucu zastavku
				if (j < elements.size() - 1) {
					buffer2 = elements.get(j + 1).select("td:gt(0)")
							.select("td:lt(3)").first().text();

					buffer2 = buffer2.replace(String.valueOf((char) 160), " ");
				} else
					buffer2 = null;

				// zistujem, ci udaj obsahuje aj cas prichodu na stanicu
				// tiez
				if (j < elements.size() - 1 && buffer2.equals(buffer1)) {

					trainStop = new TrainStop(train.getNumber(),
							buffer1.trim(), elements.get(j).select("td:gt(0)")
									.select("td:lt(3)").last().text(), elements
									.get(j + 1).select("td:gt(0)")
									.select("td:lt(3)").last().text(),
							!(elements.get(j).select("img[src=img/sq.png]")
									.isEmpty()));

					trainStopList.add(trainStop);
					j++;

				} else {

					trainStop = new TrainStop(train.getNumber(),
							buffer1.trim(), elements.get(j).select("td:gt(0)")
									.select("td:lt(3)").last().text(),
							!(elements.get(j).select("img[src=img/sq.png]")
									.isEmpty()));

					trainStopList.add(trainStop);

				}
			}
		}
	}

	/**
	 * vracia zoznam prichodov a odchodov vlakov zo stanic
	 * 
	 * @return zoznam prichodov a odchodov
	 */
	@Override
	public List<TrainStop> getResults() {
		return trainStopList;
	}

}