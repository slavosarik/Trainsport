package entities;

/**
 * Trieda - Poziadavka - sluzi na ulozenie atributov pouzivatelskych kriterii
 * pre najdenie trasy
 * 
 * @author Slavomir Sarik
 * 
 */
public class Request {

	private String stationFrom;
	private String stationTo;
	private String arrivalTime;

	/**
	 * 
	 * @param stationFrom
	 *            vychodzia stanica
	 * @param stationTo
	 *            cielova stanica
	 * @param arrivalTime
	 *            cas zaciatku cesty
	 */
	public Request(String stationFrom, String stationTo, String arrivalTime) {
		this.setStationFrom(stationFrom);
		this.setStationTo(stationTo);
		this.setArrivalTime(arrivalTime);
	}

	public String getStationFrom() {
		return stationFrom;
	}

	public void setStationFrom(String stationFrom) {
		this.stationFrom = stationFrom;
	}

	public String getStationTo() {
		return stationTo;
	}

	public void setStationTo(String stationTo) {
		this.stationTo = stationTo;
	}

	public String getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

}