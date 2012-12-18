package com.experiment.services;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import com.experiment.models.*;
import nu.xom.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 8/14/12
 * Time: 3:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImplSFTCService implements ISFTCService {

    @Override
    public List<Matchup> getMatchups(int year, int month, int day)
    {
        List<Matchup> matchups = new ArrayList<Matchup>();

        try {

            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet("http://streak.espn.go.com/en/?date=" + year + month + day);
            HttpResponse response = client.execute(request);

            // Check if server response is valid
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() != 200) {
                throw new IOException("Invalid response from server: " + status.toString());
            }

            // Pull content stream from response
            HttpEntity entity = response.getEntity();
            InputStream inputStream = entity.getContent();

            ByteArrayOutputStream content = new ByteArrayOutputStream();

            //read response into a buffered stream
            int readBytes = 0;
            byte[] sBuffer = new byte[512];
            while ((readBytes = inputStream.read(sBuffer)) != -1) {
                content.write(sBuffer, 0, readBytes);
            }

            //return result from buffered stream
            String dataAsString = new String(content.toByteArray());
            System.out.print(dataAsString);
            try {
                XMLReader parser = XMLReaderFactory.createXMLReader("org.ccil.cowan.tagsoup.Parser");
                InputStream is = new ByteArrayInputStream( dataAsString.getBytes() );

                //build out an XML document using TagSoup
                Document doc = new Builder(parser).build(is);
                //Resources resources = this.getResources();
                //Document doc = new Builder(parser).build(resources.getAssets().open("myhtml.htm"));

                //set the ns of the document as the XPathContext or we will not find the elements when we attempt to parse
                XPathContext context = new XPathContext("ns", "http://www.w3.org/1999/xhtml");

                //get the listing of matchup containers which are all the matchups for the given date
                Nodes nodes = doc.query(".//ns:div[@class='matchup-container']", context);

                for (int index = 0; index < nodes.size(); index++)
                {

                    Matchup matchup = new Matchup();
                    Node matchupNode = nodes.get(index);

                    //get matchup question
                    Node gameQuestionNode = matchupNode.query(".//ns:div[@class='gamequestion']", context).get(0);
                    if(gameQuestionNode != null)
                        matchup.setDescription(gameQuestionNode.getValue().trim());

                    //get the date and time of the matchup, all times are ET and store it for scheduling
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd hh:mm a");
                    formatter.setTimeZone(TimeZone.getTimeZone("America/New_York"));
                    Calendar calendar  = Calendar.getInstance();
                    calendar.setTime((Date)formatter.parse(year + "/" + month + "/" + day + " " + matchupNode.query(".//ns:div[@class='matchupDate']", context).get(0).getValue()));
                    matchup.setDate(calendar);

                    //ran into HTML where the divs were not there 5/2011
                    Node sportNode = matchupNode.query(".//ns:div[@class='sport-description']", context).get(0);//.query("//*[local-name='div' and position()=2]").get(0);
                    if (sportNode != null)
                        matchup.setCategory(matchup.ToSport(sportNode.getValue()));
                    else
                        matchup.setCategory(Matchup.Sport.Unknown);

                    //create the entry for the first opponent
                    Entry e1 = new Entry();
                    //set the details of the first opponent in the entry
                    Node opponent1 = matchupNode.query(".//ns:td[@class='mg-column3 opponents']", context).get(0);

                    e1.setOpponent(opponent1.getValue());
                    e1.setResult(matchupNode.query(".//ns:td[@class='mg-column4 result']", context).get(0).getValue());
                    e1.setPopularity(matchupNode.query(".//ns:tr[1]/ns:td[@class='mg-column6 wpw']", context).get(0).getValue());
                    //checking to see if opponent1 is a winner by the presence of arrow image
                    if (opponent1.query("ns:img", context) != null)
                        e1.setWinner(true);

                    //set first entry of matchup
                    matchup.setEntry1(e1);

                    //create the entry for the second opponent
                    Entry e2 = new Entry();
                    //set the details of the second opponent in the entry
                    Node opponent2 = matchupNode.query(".//ns:td[@class='mg-column3 opponents last']", context).get(0);
                    e2.setOpponent(opponent2.getValue());
                    e2.setResult(matchupNode.query(".//ns:td[@class='mg-column4 result last']", context).get(0).getValue());
                    e2.setPopularity(matchupNode.query(".//ns:tr[2]/ns:td[@class='mg-column6 wpw']", context).get(0).getValue());
                    //checking to see if opponent1 is a winner by the presence of arrow image
                    if (opponent2.query("ns:img", context) != null)
                        e2.setWinner(true);

                    //set second entry of matchup
                    matchup.setEntry2(e2);

                    //add matchup to list
                    matchups.add(matchup);

                }

            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

        return matchups;
    }
}
