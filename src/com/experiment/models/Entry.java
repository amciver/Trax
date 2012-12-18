package com.experiment.models;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 8/14/12
 * Time: 3:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class Entry implements Serializable {

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = com.experiment.utils.String.trim(popularity);
    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = com.experiment.utils.String.trim(opponent);
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = com.experiment.utils.String.trim(result);
    }

    public boolean isWinner() {
        return isWinner;
    }

    public void setWinner(boolean winner) {
        isWinner = winner;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    private String opponent = "";
    private String result = "";
    private boolean isWinner = false;
    private String popularity = "";
    private Line line = new Line();

}
