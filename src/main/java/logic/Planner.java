package logic;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import dbmanager.DatabaseManager;
import entities.Request;
import entities.Route;

/**
 * Trieda - planovac Vytahuju sa data za databazy a nasledne sa tieto spracuvaju
 * podla poziadavky od pouzivatela a vracia sa zoznam tras ktore splnaju dane
 * kriteria
 * 
 * @author Slavomir Sarik
 * 
 */
public class Planner implements PlannerInterface {

	private Request request;
	private List<String> trainTypes;
	private List<Route> results = new ArrayList<Route>();
	private static Logger logger = Logger.getLogger(Planner.class);

	/**
	 * 
	 * @param from
	 *            vychodzia stanica
	 * @param to
	 *            cielova stanica
	 * @param time
	 *            zaciatok cesty
	 * @param types
	 *            typ vlakov, ktore su poziadavkou na planovac
	 */
	public Planner(String from, String to, String time, List<String> types) {
		this.trainTypes = types;
		this.request = new Request(from, to, time);
	}

	/**
	 * vypocet moznosti variantov cesty
	 */
	public void createRoute() {
		List<Route> routes;

		// ziskanie moznych tras z databazy
		logger.info("Ziskavanie moznych tras z databazy");
		routes = DatabaseManager.getInstance().fillRequest(request);

		// prehladavnie zoznamu a vyberanie tras podla typov vlakov, ktore boli
		// zvolene
		for (Route t : routes)
			if (trainTypes.contains(t.trainType)) {
				results.add(t);
			}
	}

	/**
	 * vrati najdene varianty tras
	 * 
	 * @return zoznam tras
	 */
	public List<Route> getResults() {
		return results;
	}

	/**
	 * vrati podrobnosti o poziadavke
	 * 
	 * @return podrobnosti
	 */
	public String toString() {
		return "From: " + request.getStationFrom() + ", To: "
				+ request.getStationTo() + ", Time: "
				+ request.getArrivalTime();
	}
}