package org.glucosio.android.db;

import android.util.Log;

/**
 * Created by rajeshkhandelwal on 10/15/15.
 */
public class TemperatureReading {
    double _reading;
    int _id;
    int _user_id;
    String _created;

    public TemperatureReading()
    {

    }

    public TemperatureReading(double reading,String created)
    {
        this._reading=reading;
        this._created=created;
    }
    public int get_user_id()
    {
        return this._user_id;
    }
    public void set_user_id(int user_id)
    {
        this._user_id=user_id;
    }
    public int get_id()
    {
        return this._id;
    }
    public void set_id(int id)
    {
        this._id=id;
    }
    public void set_reading(double reading)
    {
        this._reading=reading;
    }

    public double get_reading()
    {
        return this._reading;
    }
    public String get_created()
    {
        Log.d("Getted", get_created());
        return _created;
    }
    public void set_created(String created)
    {
        this._created=created;
    }

}