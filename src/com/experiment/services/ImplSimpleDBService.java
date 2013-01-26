package com.experiment.services;

import android.content.Context;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.*;
import com.experiment.listeners.GetLocationsCompleteListener;
import com.experiment.models.Location;
import com.google.android.gms.maps.model.LatLng;
import nu.xom.*;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 12/28/12
 * Time: 2:14 PM
 * To change this template use File | Settings | File Templates.
 */

public class ImplSimpleDBService implements ISimpleDBService {

    final static int RETURN_COUNT = 10;

    List<GetLocationsCompleteListener> mGetLocationsCompleteListeners = new ArrayList<GetLocationsCompleteListener>();

    public void setOnGetLocationCompleteListener(GetLocationsCompleteListener listener) {
        this.mGetLocationsCompleteListeners.add(listener);
    }

    public void getLocationsAsync(Context context, LatLng coordinates) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        String provider = locationManager.getBestProvider(ImplLocationService.createFineCriteria(), true);
        android.location.Location currentLocation = locationManager.getLastKnownLocation(provider);

        new GetLocationsTask().execute(currentLocation);
    }

    private class GetLocationsTask extends AsyncTask<android.location.Location, Integer, TreeMap<Float, com.experiment.models.Location>> {
        protected TreeMap<Float, Location> doInBackground(android.location.Location... location) {

            TreeMap<Float, Location> locations = new TreeMap<Float, com.experiment.models.Location>();

            try {
                android.location.Location currentLocation = (android.location.Location) location[0];

                SelectResult result = SimpleDB.getInstance().select(new SelectRequest("select * from `Locations`"));
                for (Item lot : result.getItems()) {
                    android.location.Location lotLocation = new android.location.Location(currentLocation.getProvider());

                    Location internalLotLocation = new Location();
                    internalLotLocation.setName(lot.getName());

                    for (Attribute attribute : lot.getAttributes()) {

                        if (attribute.getName().equalsIgnoreCase("business"))
                            internalLotLocation.setBusiness(attribute.getValue());
                        if (attribute.getName().equalsIgnoreCase("description"))
                            internalLotLocation.setDescription(attribute.getValue());
                        if (attribute.getName().equalsIgnoreCase("amex"))
                            internalLotLocation.setAcceptsAmex(com.experiment.utils.Boolean.valueOf(attribute.getValue()));
                        if (attribute.getName().equalsIgnoreCase("visa"))
                            internalLotLocation.setAcceptsVisa(com.experiment.utils.Boolean.valueOf(attribute.getValue()));
                        if (attribute.getName().equalsIgnoreCase("mastercard"))
                            internalLotLocation.setAcceptsMastercard(com.experiment.utils.Boolean.valueOf(attribute.getValue()));
                        if (attribute.getName().equalsIgnoreCase("discover"))
                            internalLotLocation.setAcceptsDiscover(com.experiment.utils.Boolean.valueOf(attribute.getValue()));
                        if (attribute.getName().equalsIgnoreCase("phone"))
                            internalLotLocation.setPhone(attribute.getValue());
                        if (attribute.getName().equalsIgnoreCase("lat"))
                            lotLocation.setLatitude(Double.parseDouble(attribute.getValue()));
                        if (attribute.getName().equalsIgnoreCase("lng"))
                            lotLocation.setLongitude(Double.parseDouble(attribute.getValue()));
                    }

                    internalLotLocation.setPoint(new LatLng(lotLocation.getLatitude(), lotLocation.getLongitude()));
                    locations.put(currentLocation.distanceTo(lotLocation), internalLotLocation);
                }
            } catch (Exception e) {
                Log.e("GetLocationsTask", "Failure attempting to get locations", e);
            }

            return locations;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(TreeMap<Float, com.experiment.models.Location> result) {
            Log.d("GetLocationsTask", result.size() + " total locations obtained");

            int count = RETURN_COUNT;
            List<Location> locations = new ArrayList<Location>();
            for (Map.Entry<Float, Location> l : result.entrySet()) {
                locations.add(l.getValue());

                //only return a subset of the overall count
                if (--count == 0)
                    break;
            }

            Log.d("GetLocationsTask", "Passing " + mGetLocationsCompleteListeners.size() + " listeners " + locations.size() + " locations");
            //notify all listeners that work is complete
            for (GetLocationsCompleteListener listener : mGetLocationsCompleteListeners) {
                listener.onLocationFetchComplete(locations);
            }
        }
    }

    public void loadLocationsAsync(String kml) {
        List<Location> locations = new ArrayList<Location>();

        try {
            XMLReader parser = XMLReaderFactory.createXMLReader("org.ccil.cowan.tagsoup.Parser");
            InputStream is = new ByteArrayInputStream(kml.getBytes());

            //build out an XML document using TagSoup
            Document doc = new Builder(parser).build(is);

            //set the ns of the document as the XPathContext or we will not find the elements when we attempt to parse
            XPathContext context = new XPathContext("ns", "http://www.w3.org/1999/xhtml");

            //get the Placemark nodes within the data
            Nodes nodes = doc.query(".//ns:Placemark", context);

            for (int index = 0; index < nodes.size(); index++) {
                Location placemark = new Location();
                Node placemarkNode = nodes.get(index);

                Node nameNode = placemarkNode.query("ns:name", context).get(0);
                if (nameNode != null)
                    placemark.setName(nameNode.getValue());

                Node descriptionNode = placemarkNode.query("ns:description", context).get(0);
                if (descriptionNode != null)
                    placemark.setDescription(descriptionNode.getValue());

                Node lnglatNode = placemarkNode.query("ns:Point/ns:coordinates", context).get(0);
                if (lnglatNode != null) {
                    //get longitude,latitude,altitude, per KML spec
                    String[] points = lnglatNode.getValue().split(",");
                    placemark.setPoint(new LatLng(Double.parseDouble(points[1].trim()), Double.parseDouble(points[0].trim())));
                }

                locations.add(placemark);

            }

            //spin off a new thread and load locations
            new LoadLocationsTask().execute(locations);

        } catch (Exception e) {
            Log.e("LoadLocationsTask", "Failure attempting to load locations", e);
        }

    }

    private class LoadLocationsTask extends AsyncTask<List<Location>, Integer, Void> {
        protected Void doInBackground(List<Location>... locations) {
            try {
                int count = locations[0].size();
                for (Location placemark : locations[0]) {
                    ReplaceableAttribute descriptionAttribute = new ReplaceableAttribute("description", placemark.getDescription(), Boolean.TRUE);
                    ReplaceableAttribute latAttribute = new ReplaceableAttribute("lat", String.valueOf(placemark.getPoint().latitude), Boolean.TRUE);
                    ReplaceableAttribute lngAttribute = new ReplaceableAttribute("lng", String.valueOf(placemark.getPoint().longitude), Boolean.TRUE);

                    List attrs = new ArrayList(3);
                    attrs.add(descriptionAttribute);
                    attrs.add(latAttribute);
                    attrs.add(lngAttribute);

                    PutAttributesRequest par = new PutAttributesRequest("Locations", placemark.getName(), attrs);
                    SimpleDB.getInstance().putAttributes(par);

                    publishProgress((int) ((count-- / (float) locations[0].size()) * 100));
                }
            } catch (Exception e) {
                Log.e("LoadLocationsTask", "Failure attempting to load locations in background task", e);
            }

            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Void result) {
            //returned result will always be null
        }
    }
}


