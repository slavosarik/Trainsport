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

import entities.Delay;

/**
 * Ziskavanie priemernych meskani za poslednych 7 dni
 * 
 * @author Slavomir Sarik
 * 
 */
public class DelayParser implements ParserInterface {

	private String stationFrom;
	private String stationTo;
	private String url = "http://poloha.vlaku.info/vlak/";
	private List<Delay> delayList = new ArrayList<Delay>();
	private Logger logger = Logger.getLogger(DelayParser.class);

	/**
	 * Konstruktor
	 * 
	 * @param stationFrom
	 *            vychodzia stanica
	 * @param stationTo
	 *            cielova stanica
	 * @param trainNumber
	 *            cislo vlaku
	 */
	public DelayParser(String stationFrom, String stationTo, int trainNumber) {
		this.stationFrom = bugFix(stationFrom);
		this.stationTo = bugFix(stationTo);
		url = url + Integer.toString(trainNumber) + "/";
	}

	/**
	 * opravenie bugov z nekonzicity dat kvoli bugu od Google
	 * 
	 * @param station
	 *            stanica
	 * @return korektny nazov stanice
	 */
	public String bugFix(String station) {
		if ("\u0160a\u013Ea, Slovensko".equals(station))
			return "\u0160ala";
		if ("Kriv\u00E1\u0148, Detva".equals(station))
			return "Kriv\u00E1\u0148";
		if ("Radva\u0148, Slovensko".equals(station))
			return "Radva\u0148";

		return station;
	}

	/**
	 * Parsovanie udajov - meskania - zo stranky
	 * http://poloha.vlaku.info/vlak/CISLO_VLAKU
	 * 
	 * @throws IOException
	 * @throws MalformedURLExceptio
	 * 
	 */
	@Override
	public void parse() throws MalformedURLException, IOException {
		Document doc = null;
		Delay delay;
		String actualStation;
		String buffer;
		boolean isInTimetable = false;
		Elements elements = null;

		doc = Jsoup.parse(new URL(url).openStream(), "CP1250", url);
		logger.debug("Otvara sa url pre meskania: " + url);

		logger.info("Ziskava sa priemerne meskanie");
		// parsovanie

		doc.outputSettings().escapeMode(EscapeMode.xhtml);
		elements = doc.select("table").get(1).select("tr");
		elements.remove(0);
		elements.remove(0);

		// prechadzanie zoznamom zastavok
		for (int j = 0; j < elements.size(); j++) {
			// zistovanie ci je to platna zastavka
			if (elements.get(j).select("img[src=img/sq.png]").isEmpty() == true
					|| elements.get(j).select("strong:contains(#)").isEmpty() == false)
				continue;
			// parsovanie nazvu aktualnej stanice
			buffer = elements.get(j).select("td:gt(0)").select("td:lt(3)")
					.first().text();
			buffer = buffer.replace(String.valueOf((char) 160), " ");
			actualStation = buffer.trim();

			// zistujem ci dana zastavka sa nachadza medzi vychodzou a
			// cielovou
			// stanicou
			if (stationFrom != null && stationFrom.equals(actualStation))
				isInTimetable = true;

			// ci uz som presiel vsetky zastavky z vybranej trasy - ci je
			// posledna pridana zastavka cielova stanica
			if (delayList.size() != 0
					&& delayList.get(delayList.size() - 1).getStation()
							.equals(stationTo)
					&& stationTo.equals(actualStation) == false)
				break;

			// ak uz je vypocitane meskanie aj pre cielovu stanicu, tak sa
			// cyklus konci
			if (isInTimetable == false)
				continue;

			delay = new Delay();
			// pridanie nazvu stanice ako atribut meskania
			buffer = elements.get(j).select("td:gt(0)").select("td:lt(3)")
					.first().text();
			buffer = buffer.replace(String.valueOf((char) 160), " ");

			delay.setStation(buffer.trim());

			// parsovanie meskania
			Elements eles = elements.get(j).select("td.tab.c");

			// pocitanie priemerneho meskania
			int sum = 0;
			for (Element i : eles) {
				sum += Integer.parseInt(i.text());
			}

			// pridanie hodnoty meskania
			delay.setDelayTime(Math.round(sum / (float) eles.size()));

			// ak ma uvedena stanica hodnotu meskania uz aj pri prichode,
			// tak
			// tato hodnota je ignorovana a berie sa do uvahy iba velkost
			// meskania pri vyjazde zo stanice
			if (delayList.size() != 0
					&& delayList.get(delayList.size() - 1).getStation()
							.equals(delay.getStation())) {
				delayList.get(delayList.size() - 1).setDelayTime(
						delay.getDelayTime());

				logger.debug(delayList.get(delayList.size() - 1).toString());
			} else {
				delayList.add(delay);
				logger.debug(delay.toString());
			}
		}
		logger.info("Priemerne meskanie vlaku vypocitane");
	}

	/**
	 * vrati zoznam zastavok s priemernymi meskaniami
	 * 
	 * @return zoznam zastavok s priemernymi meskaniami
	 */
	@Override
	public List<Delay> getResults() {

		return delayList;
	}

}