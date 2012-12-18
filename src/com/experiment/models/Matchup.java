package com.experiment.models;

import java.io.Serializable;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 8/14/12
 * Time: 3:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class Matchup implements Serializable{

    public enum Sport {

        //1=American Football
        //1018=NFL Football Lines
        //1203=NFL Game Props
        NFL ("1", Collections.unmodifiableList(Arrays.asList("1018", "1203"))),

        //1=American Football
        NCF ("1", Collections.unmodifiableList(new ArrayList<String>())),

        //12=Soccer
        Soccer ("12", Collections.unmodifiableList(new ArrayList<String>())),

        //5=Basketball
        //1068=College Basketball Lines
        NCW ("5", Collections.unmodifiableList(Arrays.asList("1068"))),

        //5=Basketball
        //1068=College Basketball Lines
        NCB ("5", Collections.unmodifiableList(Arrays.asList("1068"))),

        //5=Basketball
        //1070=NBA Basketball Lines
        //1241=NBA Props
        NBA ("5", Collections.unmodifiableList(Arrays.asList("1070", "1241"))),

        W_Vol ("-1", Collections.unmodifiableList(new ArrayList<String>())),
        M_Vol ("28", Collections.unmodifiableList(new ArrayList<String>())),

        Unknown ("-1", Collections.unmodifiableList(new ArrayList<String>()));

        private final String sportId;
        private final List<String> categoryIds;
        private final String lineId = "2";
        Sport(String sportId, List<String> categoryIds) {
            this.sportId = sportId;
            this.categoryIds = categoryIds;
        }
        public String getSportId() { return sportId; }
        public List<String> getCategoryIds() { return categoryIds; }
        public String getLineId() { return lineId; }
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public Sport getCategory() {
        return category;
    }

    public void setCategory(Sport category) {
        this.category = category;
    }

    public Entry getEntry1() {
        return entry1;
    }

    public void setEntry1(Entry entry1) {
        this.entry1 = entry1;
    }

    public Entry getEntry2() {
        return entry2;
    }

    public void setEntry2(Entry entry2) {
        this.entry2 = entry2;
    }

    public Entry getPrediction() {
        return prediction;
    }

    public void setPrediction(Entry prediction) {
        this.prediction = prediction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;
    private Calendar date;
    private Sport category;

    private Entry entry1;
    private Entry entry2;

    private Entry prediction;

    public Sport ToSport(String sport)
    {
        String trimmedSport =  com.experiment.utils.String.trim(sport);
        if (trimmedSport.equalsIgnoreCase("nba"))
            return Matchup.Sport.NBA;
//        if (trimmedSport.equalsIgnoreCase("wnba"))
//            return Matchup.Sport.WNBA;
//        if (trimmedSport.equalsIgnoreCase("soccer"))
//            return Matchup.Sport.Soccer;
        if (trimmedSport.equalsIgnoreCase("nfl"))
            return Matchup.Sport.NFL;
        if (trimmedSport.equalsIgnoreCase("ncf"))
            return Matchup.Sport.NCF;
//        if (trimmedSport.equalsIgnoreCase("mlb"))
//            return Matchup.Sport.MLB;
//        if (trimmedSport.equalsIgnoreCase("nhl"))
//            return Matchup.Sport.NHL;
//        if (trimmedSport== "golf"))
//            return Matchup.Sport.Golf;
//        if (trimmedSport.equalsIgnoreCase("hoops"))
//            return Matchup.Sport.Golf;
//        if (trimmedSport.equalsIgnoreCase("tennis"))
//            return Matchup.Sport.Golf;
        if (trimmedSport.equalsIgnoreCase("ncb"))
            return Matchup.Sport.NCB;
//        if (trimmedSport.equalsIgnoreCase("lax"))
//            return Matchup.Sport.LAX;
//        if (trimmedSport== "nascar"))
//            return Matchup.Sport.NASCAR;
        if (trimmedSport.equalsIgnoreCase("ncw"))
            return Matchup.Sport.NCW;
//        if (trimmedSport.equalsIgnoreCase("hog"))
//            return Matchup.Sport.Hog;
//        if (trimmedSport.equalsIgnoreCase("mma"))
//            return Matchup.Sport.MMA;
        if (trimmedSport.equalsIgnoreCase("m vol"))
            return Matchup.Sport.M_Vol;
        if (trimmedSport.equalsIgnoreCase("w vol"))
            return Matchup.Sport.W_Vol;
//        if (trimmedSport.equalsIgnoreCase("horse"))
//            return Matchup.Sport.Horse;
//        if (trimmedSport.equalsIgnoreCase("boxing"))
//            return Matchup.Sport.Boxing;
//        if (trimmedSport.equalsIgnoreCase("soft"))
//            return Matchup.Sport.SOFT;
//        if (trimmedSport.equalsIgnoreCase("autos"))
//            return Matchup.Sport.Autos;
//        if (trimmedSport.equalsIgnoreCase("cricket"))
//            return Matchup.Sport.Autos;
//        if (trimmedSport.equalsIgnoreCase("hockey"))
//            return Matchup.Sport.Autos;

        return Sport.Unknown;
    }

//    @Override
//    public String toString()
//    {
//        return this.getEntry1().getOpponent() + " versus " + this.getEntry2().getOpponent();
//    }
}
