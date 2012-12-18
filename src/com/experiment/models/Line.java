package com.experiment.models;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 12/12/12
 * Time: 7:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class Line implements Serializable {

    public Calendar getAsOf() {
        return asOf;
    }

    public void setAsOf(Calendar asOf) {
        this.asOf = asOf;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    private Calendar asOf;
    private String line1;
    private String line2;
}
