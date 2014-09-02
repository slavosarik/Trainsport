package entities;

/**
 * 
 * Trieda - Meskanie - uchovavaju sa informacie o meskani z danej stanice
 * 
 * @author Slavomir Sarik
 * 
 */
public class Delay {

	private String station;
	private int delayTime;

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public int getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(int delayTime) {
		this.delayTime = delayTime;
	}

	/**
	 * vrati detaily o meskani vlaku zo stanice
	 * 
	 * @return detaily o meskani
	 */
	public String toString() {
		return "Station: " + station + ", Delay time: " + delayTime;
	}

}