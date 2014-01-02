package com.experiment.trax.services;

import android.content.Context;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.util.Log;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.*;
import com.experiment.trax.listeners.GetDropsiteLocationsCompleteListener;
import com.experiment.trax.listeners.GetLotLocationsCompleteListener;
import com.experiment.trax.models.DropsiteLocation;
import com.experiment.trax.models.LotLocation;
import com.google.android.gms.maps.model.LatLng;
import nu.xom.*;
import org.joda.time.DateTime;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 12/28/12
 * Time: 2:14 PM
 * To change this template use File | Settings | File Templates.
 */

public class ImplSimpleDBService implements ISimpleDBService {

    final static int RETURN_COUNT = 10;

    List<GetDropsiteLocationsCompleteListener> mGetDropsiteLocationsCompleteListeners = new ArrayList<GetDropsiteLocationsCompleteListener>();
    List<GetLotLocationsCompleteListener> mGetLotLocationsCompleteListeners = new ArrayList<GetLotLocationsCompleteListener>();

    public void setOnGetLocationCompleteListener(GetDropsiteLocationsCompleteListener listener) {
        this.mGetDropsiteLocationsCompleteListeners.add(listener);
    }

    public void setOnGetLocationCompleteListener(GetLotLocationsCompleteListener listener) {
        this.mGetLotLocationsCompleteListeners.add(listener);
    }

    public void getLocationsAsync(Context context, LatLng coordinates) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        String provider = locationManager.getBestProvider(ImplLocationService.createFineCriteria(), true);
        android.location.Location currentLocation = locationManager.getLastKnownLocation(provider);

        new GetLocationsTask().execute(currentLocation);
    }

    public void getDropsitesAsync(Context context, LatLng coordinates) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        String provider = locationManager.getBestProvider(ImplLocationService.createFineCriteria(), true);
        android.location.Location currentLocation = locationManager.getLastKnownLocation(provider);

        new GetDropsitesTask().execute(currentLocation);
    }

    private class GetDropsitesTask extends AsyncTask<android.location.Location, Integer, TreeMap<Float, DropsiteLocation>> {
        protected TreeMap<Float, DropsiteLocation> doInBackground(android.location.Location... location) {

            TreeMap<Float, DropsiteLocation> locations = new TreeMap<Float, DropsiteLocation>();

            try {

                if (location[0] != null) {
                    android.location.Location currentLocation = location[0];

                    SelectResult result = SimpleDB.getInstance().select(new SelectRequest("select * from `Dropsites`"));
                    for (Item dropsite : result.getItems()) {
                        android.location.Location dropsiteLocation = new android.location.Location(currentLocation.getProvider());

                        DropsiteLocation internalDropsiteLocation = new DropsiteLocation();
                        internalDropsiteLocation.setName(dropsite.getName());

                        //used to temporarily store hours of open and close for each location
                        Hashtable<Integer, String> dateOpen = new Hashtable<Integer, String>();
                        Hashtable<Integer, String> dateClose = new Hashtable<Integer, String>();

                        for (Attribute attribute : dropsite.getAttributes()) {

                            if (attribute.getName().equalsIgnoreCase("id"))
                                internalDropsiteLocation.setId(attribute.getValue());
                            if (attribute.getName().equalsIgnoreCase("description"))
                                internalDropsiteLocation.setDescription(attribute.getValue());

                            if (attribute.getName().equalsIgnoreCase("start_collection"))
                                internalDropsiteLocation.setDateOpen(DateTime.parse(attribute.getValue()));
                            if (attribute.getName().equalsIgnoreCase("end_collection"))
                                internalDropsiteLocation.setDateClose(DateTime.parse(attribute.getValue()));

                            if (attribute.getName().equalsIgnoreCase("lat"))
                                dropsiteLocation.setLatitude(Double.parseDouble(attribute.getValue()));
                            if (attribute.getName().equalsIgnoreCase("lng"))
                                dropsiteLocation.setLongitude(Double.parseDouble(attribute.getValue()));
                        }

                        //store the location of the lot
                        internalDropsiteLocation.setPoint(new LatLng(dropsiteLocation.getLatitude(), dropsiteLocation.getLongitude()));

                        //save it to our locations listing to return back to the consumer
                        locations.put(currentLocation.distanceTo(dropsiteLocation), internalDropsiteLocation);
                    }
                }
            } catch (Exception e) {
                Log.e("GetDropsitesTask", "Failure attempting to get locations", e);
            }

            return locations;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(TreeMap<Float, DropsiteLocation> result) {
            Log.d("GetDropsiteTask", result.size() + " total locations obtained");

            int count = RETURN_COUNT;
            List<DropsiteLocation> locations = new ArrayList<DropsiteLocation>();
            for (Map.Entry<Float, DropsiteLocation> l : result.entrySet()) {
                locations.add(l.getValue());

                //only return a subset of the overall count
                if (--count == 0)
                    break;
            }

            Log.d("GetDropsitesTaskl", "Passing " + mGetDropsiteLocationsCompleteListeners.size() + " listeners " + locations.size() + " locations");
            //notify all listeners that work is complete
            for (GetDropsiteLocationsCompleteListener listener : mGetDropsiteLocationsCompleteListeners) {
                listener.onDropsiteLocationFetchComplete(locations);
            }
        }
    }

    private class GetLocationsTask extends AsyncTask<android.location.Location, Integer, TreeMap<Float, LotLocation>> {
        protected TreeMap<Float, LotLocation> doInBackground(android.location.Location... location) {

            TreeMap<Float, LotLocation> locations = new TreeMap<Float, LotLocation>();

            try {

                if (location[0] != null) {
                    android.location.Location currentLocation = location[0];

                    SelectResult result = SimpleDB.getInstance().select(new SelectRequest("select * from `Locations`"));
                    for (Item lot : result.getItems()) {
                        android.location.Location lotLocation = new android.location.Location(currentLocation.getProvider());

                        LotLocation internalLotLocation = new LotLocation();
                        internalLotLocation.setName(lot.getName());

                        //used to temporarily store hours of open and close for each location
                        Hashtable<Integer, String> hoursOpen = new Hashtable<Integer, String>();
                        Hashtable<Integer, String> hoursClose = new Hashtable<Integer, String>();

                        for (Attribute attribute : lot.getAttributes()) {

                            if (attribute.getName().equalsIgnoreCase("rating"))
                                internalLotLocation.setRating(Float.parseFloat(attribute.getValue()));
                            if (attribute.getName().equalsIgnoreCase("business"))
                                internalLotLocation.setBusiness(attribute.getValue());
                            if (attribute.getName().equalsIgnoreCase("description"))
                                internalLotLocation.setDescription(attribute.getValue());
                            if (attribute.getName().equalsIgnoreCase("amex"))
                                internalLotLocation.setAcceptsAmex(com.experiment.trax.utils.Boolean.valueOf(attribute.getValue()));
                            if (attribute.getName().equalsIgnoreCase("visa"))
                                internalLotLocation.setAcceptsVisa(com.experiment.trax.utils.Boolean.valueOf(attribute.getValue()));
                            if (attribute.getName().equalsIgnoreCase("mastercard"))
                                internalLotLocation.setAcceptsMastercard(com.experiment.trax.utils.Boolean.valueOf(attribute.getValue()));
                            if (attribute.getName().equalsIgnoreCase("discover"))
                                internalLotLocation.setAcceptsDiscover(com.experiment.trax.utils.Boolean.valueOf(attribute.getValue()));
                            if (attribute.getName().equalsIgnoreCase("phone"))
                                internalLotLocation.setPhone(attribute.getValue());

                            //create a key -> value to day of week and time opened or closed
                            if (attribute.getName().equalsIgnoreCase("1_open"))
                                hoursOpen.put(1, attribute.getValue());
                            if (attribute.getName().equalsIgnoreCase("1_close"))
                                hoursClose.put(1, attribute.getValue());
                            if (attribute.getName().equalsIgnoreCase("2_open"))
                                hoursOpen.put(2, attribute.getValue());
                            if (attribute.getName().equalsIgnoreCase("2_close"))
                                hoursClose.put(2, attribute.getValue());
                            if (attribute.getName().equalsIgnoreCase("3_open"))
                                hoursOpen.put(3, attribute.getValue());
                            if (attribute.getName().equalsIgnoreCase("3_close"))
                                hoursClose.put(3, attribute.getValue());
                            if (attribute.getName().equalsIgnoreCase("4_open"))
                                hoursOpen.put(4, attribute.getValue());
                            if (attribute.getName().equalsIgnoreCase("4_close"))
                                hoursClose.put(4, attribute.getValue());
                            if (attribute.getName().equalsIgnoreCase("5_open"))
                                hoursOpen.put(5, attribute.getValue());
                            if (attribute.getName().equalsIgnoreCase("5_close"))
                                hoursClose.put(5, attribute.getValue());
                            if (attribute.getName().equalsIgnoreCase("6_open"))
                                hoursOpen.put(6, attribute.getValue());
                            if (attribute.getName().equalsIgnoreCase("6_close"))
                                hoursClose.put(6, attribute.getValue());
                            if (attribute.getName().equalsIgnoreCase("7_open"))
                                hoursOpen.put(7, attribute.getValue());
                            if (attribute.getName().equalsIgnoreCase("7_close"))
                                hoursClose.put(7, attribute.getValue());

                            if (attribute.getName().equalsIgnoreCase("lat"))
                                lotLocation.setLatitude(Double.parseDouble(attribute.getValue()));
                            if (attribute.getName().equalsIgnoreCase("lng"))
                                lotLocation.setLongitude(Double.parseDouble(attribute.getValue()));
                        }

                        //store the times the lot is opened and closed
                        internalLotLocation.setHoursOpen(hoursOpen);
                        internalLotLocation.setHoursClose(hoursClose);

                        //store the location of the lot
                        internalLotLocation.setPoint(new LatLng(lotLocation.getLatitude(), lotLocation.getLongitude()));

                        //save it to our locations listing to return back to the consumer
                        locations.put(currentLocation.distanceTo(lotLocation), internalLotLocation);
                    }
                }
            } catch (Exception e) {
                Log.e("GetLocationsTask", "Failure attempting to get locations", e);
            }

            return locations;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(TreeMap<Float, LotLocation> result) {
            Log.d("GetLocationsTask", result.size() + " total locations obtained");

            int count = RETURN_COUNT;
            List<LotLocation> locations = new ArrayList<LotLocation>();
            for (Map.Entry<Float, LotLocation> l : result.entrySet()) {
                locations.add(l.getValue());

                //only return a subset of the overall count
                if (--count == 0)
                    break;
            }

            Log.d("GetLocationsTask", "Passing " + mGetLotLocationsCompleteListeners.size() + " listeners " + locations.size() + " locations");
            //notify all listeners that work is complete
            for (GetLotLocationsCompleteListener listener : mGetLotLocationsCompleteListeners) {
                listener.onLotLocationFetchComplete(locations);
            }
        }
    }

    public void loadLocationsAsync(String kml) {
        List<LotLocation> locations = new ArrayList<LotLocation>();

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
                LotLocation placemark = new LotLocation();
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

    private class LoadLocationsTask extends AsyncTask<List<LotLocation>, Integer, Void> {
        protected Void doInBackground(List<LotLocation>... locations) {
            try {
                int count = locations[0].size();
                for (LotLocation placemark : locations[0]) {
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


