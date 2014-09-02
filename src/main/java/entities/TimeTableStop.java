package entities;

/**
 * Trieda - Zastavka v cestovnom poriadku danej najdenej trasy
 * 
 * @author Slavomir Sarik
 * 
 */
public class TimeTableStop {

	private int distance = 0;
	private String station;
	private String arrivalTime;
	private String departureTime;
	private boolean stops = false;

	/**
	 * Konstruktor
	 * 
	 * @param name
	 *            nazov zastavky
	 * @param arrival
	 *            cas prichodu
	 * @param departure
	 *            cas odchodu
	 * @param stop
	 *            boolean ci stoji na zastavke
	 */
	public TimeTableStop(String name, String arrival, String departure,
			boolean stop) {
		this.station = name;
		this.arrivalTime = arrival;
		this.departureTime = departure;
		this.stops = stop;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public String getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public String getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}

	public boolean isStops() {
		return stops;
	}

	public void setStops(boolean stops) {
		this.stops = stops;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

}