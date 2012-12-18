package com.experiment.services;

import android.app.Activity;
import com.experiment.models.Line;
import com.experiment.models.Matchup;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 12/10/12
 * Time: 8:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImplInteropService implements IInteropsService {

    private static String API_KEY = "b9bd23ce-db42-e211-bd0d-003048dd52d5";

    private Activity _activity;

    public ImplInteropService(Activity activity) {

        //used simply to get access to assets so we can load local copy for testing
        _activity = activity;

    }

    private static final String NS = null;

    public Line getLine(Matchup matchup)
    {
        String search = "Quarterback";
        Line line = null;

        //TODO test code, remove it out!
        matchup.setCategory(Matchup.Sport.NFL);

        try {

            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet("http://xmlfeed.intertops.com/xmloddsfeed/v2/xml/?apikey=" + API_KEY + "&sportId=" + matchup.getCategory().getSportId());
            HttpResponse response = client.execute(request);

            // Check if server response is valid
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() != 200) {
                throw new IOException("Invalid response from server: " + status.toString());
            }

            // Pull content stream from response
            HttpEntity entity = response.getEntity();
            InputStream inputStream = entity.getContent();
            //InputSource inputSource = new InputSource(inputStream);

            InputSource inputSource = new InputSource(_activity.getAssets().open("myxml.xml"));

            try {
                XPath xpath = XPathFactory.newInstance().newXPath();

                //we can have multiple category ids, NFL has games and props which are in separate categories, make them all part of our search
                String expCategoryId= "";
                for(String categoryId:matchup.getCategory().getCategoryIds())
                                        expCategoryId+= "@id='" + categoryId + "' or ";
                expCategoryId = expCategoryId.substring(0, expCategoryId.length() - 4);

                //xpath is verion 1, thus we must hijack translate function to perform case insensitive search
                String exp = "//cat[" + expCategoryId + "]/m[contains(translate(@n, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + search.toLowerCase() + "' )]";
                org.w3c.dom.Node node = (org.w3c.dom.Node)xpath.evaluate(exp, inputSource, XPathConstants.NODE);

                //we cant find the odds
                if(node == null)
                    return null;

                //we found the odds
                line = new Line();
                line.setAsOf(Calendar.getInstance());
                line.setLine1(node.getChildNodes().item(0).getTextContent());
                line.setLine2(node.getChildNodes().item(1).getTextContent());

            } finally {
                inputStream.close();
            }
        } catch(Exception e) {
             String error = e.getMessage();
        }

        return line;
    }
}
