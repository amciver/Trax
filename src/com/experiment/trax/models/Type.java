package com.experiment.trax.models;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 5/8/13
 * Time: 3:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class Type {

    private String id;
    private String name;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
