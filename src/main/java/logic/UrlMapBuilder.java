package logic;

import java.text.Normalizer;
import java.util.List;

import org.apache.log4j.Logger;

import entities.TimeTableStop;

/**
 * Trieda na skladanie hypertextoveho odkazu na Google mapu
 * 
 * @author Slavomir Sarik
 * 
 */
public class UrlMapBuilder {

	private List<TimeTableStop> timeTableStopList;
	private String url;
	private Logger logger = Logger.getLogger(UrlMapBuilder.class);

	/**
	 * Konstruktor
	 * 
	 * @param list
	 *            zoznam zastavok
	 */
	public UrlMapBuilder(List<TimeTableStop> list) {
		this.timeTableStopList = list;
	}

	/**
	 * vytvori skratene url az potom co nastane vynimka
	 * 
	 */
	public void buildStringUrlImage() {

		final String base = "https://maps.googleapis.com/maps/api/staticmap?&scale=3&path=color:0x001155|weight:4";
		final String end = "&maptype=roadmap&size=500x500&format=png&sensor=false&region=sk&language=sk";
		int ratio = 1;
		int i;
		String places;
		StringBuilder builderPlaces;
		StringBuilder builderMarkers;

		logger.info("Prebieha skladanie URL pre Google Mapu");
		while (true) {

			i = 65;
			String markers;
			builderPlaces = new StringBuilder();
			builderMarkers = new StringBuilder();

			for (TimeTableStop z : timeTableStopList) {
				// kedze URL link je limitovany svojou dlzkou, treba vyhadzovat
				// z URL zastavky na ktorych vlak nestoji, vzdy v nejakom pomere
				if (i % ratio == 0
						|| (timeTableStopList.indexOf(z) == timeTableStopList
								.size() - 1)
						|| (timeTableStopList.indexOf(z) == 0)
						|| z.isStops() == true) {
					builderPlaces.append("|");
					builderPlaces.append(z.getStation());
				}

				// ak vlak stoji na stanici, treba v URL vytvorit aj marker pre
				// tuto stanicu
				if (z.isStops() == true) {
					builderMarkers
							.append("&markers=size:mid%7Ccolor:green%7Clabel:");
					builderMarkers.append((char) (i++));
					builderMarkers.append("%7C");
					builderMarkers.append(z.getStation());
				}
			}

			places = builderPlaces.toString();
			markers = builderMarkers.toString();

			// ak je URL priliz dlhe - vysoke ratio, tak z URL sa vytvara iba z
			// marker-ov
			if (ratio > 32) {
				url = base + markers + end;
			} else {
				url = base + places + markers + end;
			}

			// uprava URL pre vhodny tvar
			url = Normalizer.normalize(url, Normalizer.Form.NFD);
			url = url.replaceAll("[^\\p{ASCII}]", "");
			url = url.replaceAll(" ", "%20");

			if (url.length() < 1950)
				break;

			// v pripade ak je URL dlhsie nez je mozne, zvysuje sa ratio a
			// sklada sa URL odznova
			if (url.length() > 1950) {
				ratio += 1;
			}
			if (url.length() > 2500) {
				ratio += 1;
			}
			if (url.length() > 3000) {
				ratio += 2;
			}
			logger.debug("URL length: " + url.length() + ", Ratio: " + ratio);
		}
		logger.debug("URL pre Google mapu vytvorene: " + url);
	}

	/**
	 * vrati URL pre Google mapu
	 * 
	 * @return url
	 */
	public String getUrl() {
		return url;
	}
}