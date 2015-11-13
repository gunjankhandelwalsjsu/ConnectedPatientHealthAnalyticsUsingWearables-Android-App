package org.project.healthMeter.db;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TemperatureReading extends RealmObject {

    @PrimaryKey
    private long id;

    private double reading;

    private int user_id;
    private Date created;

    public TemperatureReading() {
    }

    public TemperatureReading(double reading,Date created) {
        this.reading=reading;
        this.created=created;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getReading() {
        return reading;
    }

    public void setReading(double reading) {
        this.reading = reading;
    }



    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}