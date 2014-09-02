package entities;

import java.util.List;


/**
 * Trieda - Trasa - sluzi na ulozenie atributov najdenej trasy
 * 
 * @author Slavomir Sarik
 * 
 */
public class Route {

	public int trainNumber;
	public String departureTime;
	public String arrivalTime;
	public String travelTime;
	public String trainType;
	public String trainName;
	public String from;
	public String to;
	public List<TimeTableStop> timeTableStopList;

	/**
	 * 
	 * @param from
	 *            vychodzia stanica
	 * @param to
	 *            cielova stanica
	 * @param departureTime
	 *            cas prichodu do cielovej stanice
	 * @param arrivalTime
	 *            cas odchodu z vychodzej stanice
	 * @param travelTime
	 *            celkovy cas cesty
	 * @param trainType
	 *            typ vlaku
	 * @param trainNumber
	 *            cislo vlaku
	 * @param trainName
	 *            nazov vlaku
	 */
	public Route(String from, String to, String departureTime,
			String arrivalTime, String travelTime, String trainType,
			int trainNumber, String trainName) {
		this.departureTime = departureTime;
		this.arrivalTime = arrivalTime;
		this.travelTime = travelTime;
		this.trainType = trainType;
		this.trainNumber = trainNumber;
		if (trainName == null) {
			this.trainName = "";
		} else {
			this.trainName = trainName;
		}
		this.to = to;
		this.from = from;
	}

	public String toString() {
		return "Train: " + trainType + trainNumber + "From: " + from
				+ ", Arrival time: " + arrivalTime + ", To: " + to
				+ ", Departure time: " + departureTime;
	}

	public String getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}

	public String getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public String getTravelTime() {
		return travelTime;
	}

	public void setTravelTime(String travelTime) {
		this.travelTime = travelTime;
	}

	public String getTrainType() {
		return trainType;
	}

	public void setTrainType(String trainType) {
		this.trainType = trainType;
	}

	public int getTrainNumber() {
		return trainNumber;
	}

	public void setTrainNumber(int trainNumber) {
		this.trainNumber = trainNumber;
	}

	public String getTrainName() {
		return trainName;
	}

	public void setTrainName(String trainName) {
		this.trainName = trainName;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public List<TimeTableStop> getTimeTableStopList() {
		return timeTableStopList;
	}

	public void setTimeTableStopList(List<TimeTableStop> list) {
		this.timeTableStopList = list;
	}

}