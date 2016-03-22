package module3;

//Java utilities libraries
import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

//Processing library
import processing.core.PApplet;

//Unfolding libraries
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;

//Parsing library
import parsing.ParseFeed;

/** EarthquakeCityMap
 * An application with an interactive map displaying earthquake data.
 * Author: UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 * Date: July 17, 2015
 * */
public class EarthquakeCityMap extends PApplet {

	// You can ignore this.  It's to keep eclipse from generating a warning.
	private static final long serialVersionUID = 1L;

	// IF YOU ARE WORKING OFFLINE, change the value of this variable to true
	private static final boolean offline = false;
	
	// Less than this threshold is a light earthquake
	public static final float THRESHOLD_MODERATE = 5;
	// Less than this threshold is a minor earthquake
	public static final float THRESHOLD_LIGHT = 4;

	/** This is where to find the local tiles, for working without an Internet connection */
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	
	// The map
	private UnfoldingMap map;
	
	//feed with magnitude 2.5+ Earthquakes
	private String earthquakesURL = "http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/2.5_week.atom";

	
	public void setup() {
		size(1020, 600, OPENGL);

		if (offline) {
		    map = new UnfoldingMap(this, 240, 50, 760, 500, new MBTilesMapProvider(mbTilesString));
		    earthquakesURL = "2.5_week.atom"; 	// Same feed, saved Aug 7, 2015, for working offline
		}
		else {
			map = new UnfoldingMap(this, 240, 50, 760, 500, new Google.GoogleMapProvider());
			// IF YOU WANT TO TEST WITH A LOCAL FILE, uncomment the next line
			//earthquakesURL = "2.5_week.atom";
		}
		
	    map.zoomToLevel(2);
	    MapUtils.createDefaultEventDispatcher(this, map);	
			
	    // The List you will populate with new SimplePointMarkers
	    List<Marker> markers = new ArrayList<Marker>();

	    //Use provided parser to collect properties for each earthquake
	    //PointFeatures have a getLocation method
	    List<PointFeature> earthquakes = ParseFeed.parseEarthquake(this, earthquakesURL);
	    
	    // These print statements show you (1) all of the relevant properties 
	    // in the features, and (2) how to get one property and use it
	    if (earthquakes.size() > 0) {
	    	PointFeature f = earthquakes.get(0);
	    	System.out.println(f.getProperties());
	    	Object magObj = f.getProperty("magnitude");
	    	float mag = Float.parseFloat(magObj.toString());
	    	// PointFeatures also have a getLocation method
	    }
	    
	    // Here is an example of how to use Processing's color method to generate 
	    // an int that represents the color yellow.  
	    int yellow = color(255, 255, 0);

	    //TODO: Add code here as appropriate

        for (PointFeature pf: earthquakes) {
            markers.add(createMarker(pf));
        }

        //defineMarkersView(markers);

        map.addMarkers(markers);
	}
		
	// A suggested helper method that takes in an earthquake feature and 
	// returns a SimplePointMarker for that earthquake
	// TODO: Implement this method and call it from setUp, if it helps
	private SimplePointMarker createMarker(PointFeature feature)
	{
		// finish implementing and use this method, if it helps.

        HashMap<String, Object> property = new HashMap<String, Object>();
        property.put("magnitude", feature.getProperty("magnitude"));

        SimplePointMarker newMarker = new SimplePointMarker(feature.getLocation(), property);

        defineMarkerView(newMarker);

		//return new SimplePointMarker(feature.getLocation(), property);

        return newMarker;
	}
	
	public void draw() {
	    background(183, 178, 178);
	    map.draw();
	    addKey();
	}


	// helper method to draw key in GUI
	// TODO: Implement this method to draw the key
	private void addKey()
	{	
		// Remember you can use Processing's graphics methods here

        //rect
        fill(255, 255, 255);
        rect(20, 50, 200, 270, 7);

        //title
        fill(0, 0, 0);
        textSize(14);
        text("Earthquake Key", 60, 70);

        //red mark
        fill(255, 0, 0);
        ellipse(45, 120, 18, 18);
        fill(0, 0, 0);
        textSize(14);
        text("5.0 + Magnitude", 75, 125);

        //yellow mark
        fill(255, 255, 0);
        ellipse(45, 150, 13, 13);
        fill(0, 0, 0);
        textSize(14);
        text("4.0 + Magnitude", 75, 155);

        //blue mark
        fill(0, 128, 255);
        ellipse(45, 180, 8, 8);
        fill(0, 0, 0);
        textSize(14);
        text("Below 4.0", 75, 185);
	}

    private void defineMarkerView(SimplePointMarker marker)
    {
        // Remember you can use Processing's graphics methods here

        float lowMark = 4.0f;
        float highMark = 5.0f;
        float smallRadius = 8.0f;
        float mediumRadius = 13.0f;
        float bigRadius = 18.0f;

        Object magObj = marker.getProperty("magnitude");
        float mag = Float.parseFloat(magObj.toString());

        int comparisonToLow = Float.compare(mag, lowMark);
        if (comparisonToLow < 0) {
            // magnitude less than 4.0
            // fill marker with blue and small radius

            marker.setColor(color(0, 128, 255));
            marker.setStrokeColor(color(0, 0, 0));
            //marker.setStrokeWeight(4);
            marker.setRadius(smallRadius);
        }
        if (comparisonToLow >= 0) {
            // magnitude more than 4.0
            int comparisonToHigh = Float.compare(mag, highMark);
            if (comparisonToHigh < 0) {
                // magnitude more than 4.0, but less than 5.0
                // fill marker with yellow and medium radius

                marker.setColor(color(255, 255, 0));
                marker.setStrokeColor(color(0, 0, 0));
                //marker.setStrokeWeight(4);
                marker.setRadius(mediumRadius);
            }
            else {
                // magnitude more than 5.0
                // fill marker with red and big radius

                marker.setColor(color(255, 0, 0));
                marker.setStrokeColor(color(0, 0, 0));
                //marker.setStrokeWeight(4);
                marker.setRadius(bigRadius);
            }
        }
    }
}
