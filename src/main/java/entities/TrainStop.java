package entities;

/**
 * Trieda pouzivana ako entita pre parsovanie prichodov a odchodov z URL stranky
 * 
 * @author Slavomir Sarik
 * 
 */
public class TrainStop {

	private int trainNumber;
	private String stationName;
	private String arrivalTime, departureTime;
	private boolean stops;

	/**
	 * Konstruktor pre zastavku bez uvedeneho prichodu
	 * 
	 * @param trainNumber
	 *            cislo vlaku
	 * @param stationName
	 *            nazov stanice
	 * @param departureTime
	 *            cas odchodu
	 * @param stops
	 *            stoji v stanici?
	 */
	public TrainStop(int trainNumber, String stationName, String departureTime,
			boolean stops) {
		this.trainNumber = trainNumber;
		this.stationName = stationName;
		this.arrivalTime = null;
		this.departureTime = departureTime;
		this.stops = stops;
	}

	/**
	 * Konstruktor pre zastavku s uvedenym prichodom
	 * 
	 * @param trainNumber
	 *            cislo vlaku
	 * @param stationName
	 *            nazov stanice
	 * @param arrivalTime
	 *            cas prichodu
	 * @param departureTime
	 *            cas odchodu
	 * @param stops
	 *            stoji v stanici?
	 */
	public TrainStop(int trainNumber, String stationName, String arrivalTime,
			String departureTime, boolean stops) {
		this.trainNumber = trainNumber;
		this.stationName = stationName;
		this.arrivalTime = arrivalTime;
		this.departureTime = departureTime;
		this.stops = stops;
	}

	public int getTrainNumber() {
		return trainNumber;
	}

	public void setTrainNumber(int trainNumber) {
		this.trainNumber = trainNumber;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
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

	public String getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

}
