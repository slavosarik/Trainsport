package logic;

import java.util.List;

import entities.Route;

/**
 * Rozhranie pre planovac
 * 
 * @author Slavomir Sarik
 * 
 */
public interface PlannerInterface {

	/**
	 * vypocet moznosti variantov cesty
	 */
	void createRoute();

	/**
	 * vrati najdene varianty tras
	 * 
	 * @return zoznam tras
	 */
	List<Route> getResults();

}