package org.glucosio.android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Calendar;

/**
 * Created by ahmar on 10/8/15.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="temperature_db";
    private static final String TABLE_USER="User";
    private static final String TABLE_TEMPERATURE_READING="temperature_readings";

    // columns
    private static final String KEY_ID="id";
    private static final String KEY_NAME="name";

    private static final String KEY_CREATED_AT="created_at";

    //Temperature reading keys
    private static final String KEY_READING="reading";
    private static final String KEY_USER_ID="user_id";

    public DatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    public void createTable(SQLiteDatabase db)
    {
        String CREATE_USER_TABLE="CREATE TABLE "+TABLE_USER+" ("
                +KEY_ID+" INTEGER PRIMARY KEY,"+KEY_NAME+" TEXT)";
        String CREATE_TEMPERATURE_READING_TABLE="CREATE TABLE "+TABLE_TEMPERATURE_READING+" ("
                +KEY_ID+" INTEGER PRIMARY KEY,"+KEY_READING+" TEXT, "+
                KEY_CREATED_AT+" TIMESTAMP DEFAULT (datetime('now','localtime') ),"
                +KEY_USER_ID+" INTEGER DEFAULT 1 )";
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_TEMPERATURE_READING_TABLE);
    }
    public void dropTable(SQLiteDatabase db)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEMPERATURE_READING);
    }

    public void resetTable()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        dropTable(db);
        createTable(db);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTable(db);
        onCreate(db);
    }


    public void addTemperatureReading(TemperatureReading reading)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put(KEY_READING,reading.get_reading());
        Log.d("putting","temp");
        values.put(KEY_CREATED_AT, reading.get_created());
        db.insert(TABLE_TEMPERATURE_READING, null, values);
    }

    public void getTemperatureReading()
    {

    }
    public void updateTemperatureReading()
    {

    }
    public int updateTemperatureReading(TemperatureReading reading)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_READING,reading.get_reading());
        values.put(KEY_CREATED_AT, reading.get_created());
        return db.update(TABLE_TEMPERATURE_READING,values,KEY_ID+" =? ",new String[]{ String.valueOf(reading.get_id()) });
    }
    public void deleteTemperatureReadings(TemperatureReading reading)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_TEMPERATURE_READING, KEY_ID + " =? ", new String[]{String.valueOf(reading.get_id())});
    }
    public List<TemperatureReading> getTemperatureReadings()
    {
        String selectQuery="select * from temperature_readings order by "+KEY_CREATED_AT+" desc";
        return getTemperatureReadingsRecords(selectQuery);
    }
    public List<TemperatureReading> getTemperatureReadings(String where)
    {
        String selectQuery="select * from temperature_readings where "+where+" order by "+KEY_CREATED_AT+" desc";
        return getTemperatureReadingsRecords(selectQuery);
    }
    public List<TemperatureReading> getTemperatureReadingsRecords(String selectQuery)
    {
        List<TemperatureReading> readings=new ArrayList<TemperatureReading>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do{
                TemperatureReading reading=new TemperatureReading();
                reading.set_id(Integer.parseInt(cursor.getString(0)));
                reading.set_reading(Double.parseDouble(cursor.getString(1)));
                reading.set_created(cursor.getString(3));
                readings.add(reading);
            }while(cursor.moveToNext());
        }
        return readings;
    }
    public ArrayList<Integer> getTemperatureIdAsArray(){
        List<TemperatureReading> temperatureReading = getTemperatureReadings();
        ArrayList<Integer> idArray = new ArrayList<Integer>();
        int i;

        for (i = 0; i < temperatureReading.size(); i++){
            int id;
            TemperatureReading singleReading= temperatureReading.get(i);
            id = singleReading.get_id();
            idArray.add(id);
        }

        return idArray;
    }

    public ArrayList<Double> getTemperatureReadingAsArray(){
        List<TemperatureReading> temperatureReading = getTemperatureReadings();
        ArrayList<Double> readingArray = new ArrayList<Double>();
        int i;

        for (i = 0; i < temperatureReading.size(); i++){
            double reading;
            TemperatureReading singleReading= temperatureReading.get(i);
            reading = singleReading.get_reading();
            readingArray.add(reading);
        }

        return readingArray;
    }


    public ArrayList<String> getTemperatureDateTimeAsArray(){
        List<TemperatureReading> temperatureReading = getTemperatureReadings();
        ArrayList<String> datetimeArray = new ArrayList<String>();
        int i;

        for (i = 0; i < temperatureReading.size(); i++){
            String reading;
            TemperatureReading singleReading= temperatureReading.get(i);
            reading = singleReading.get_created();
            Log.d("readingssss",reading);
            datetimeArray.add(reading);
        }

        return datetimeArray;
    }

    public TemperatureReading getTemperatureReadingById(int id){
        return getTemperatureReadings("id = " + id).get(0);
    }

    private ArrayList<Integer> getTemperatureReadingsForLastMonthAsArray(){

        SQLiteDatabase db=this.getReadableDatabase();
        Calendar calendar = Calendar.getInstance();
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String now = inputFormat.format(calendar.getTime());
        calendar.add(Calendar.MONTH, -1);
        String nowString = now.toString();
        String oneMonthAgo = inputFormat.format(calendar.getTime());


        String[] parameters = new String[] { oneMonthAgo, now } ;
        String[] columns = new String[] { "reading" };
        String whereString = "created_at between ? and ?";

        Cursor cursor = db.query(false, "Temperature_readings", columns,whereString, parameters, null, null, null, null);

        ArrayList<Integer> readings = new ArrayList<Integer>();

        if(cursor.moveToFirst()){
            do{
                readings.add(cursor.getInt(0));
            }while(cursor.moveToNext());
        }
        return readings;
    }

    public Integer getAverageTemperatureReadingForLastMonth() {
        ArrayList<Integer> readings = getTemperatureReadingsForLastMonthAsArray();
        int sum = 0;
        int numberOfReadings = readings.size();
        for (int i=0; i < numberOfReadings; i++) {
            sum += readings.get(i);
        }
        if (numberOfReadings > 0){
            return Math.round(sum / numberOfReadings);
        } else {
            return 0;
        }
    }

}