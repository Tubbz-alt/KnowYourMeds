package com.tompee.utilities.knowyourmeds.controller.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tompee.utilities.knowyourmeds.model.Medicine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String FAVORITE_TABLE = "favorite";
    public static final String RECENT_TABLE = "recent";
    private static final String MAIN_TABLE = "main";
    private static final String DATE_FORMAT = "yyyyMMddHHmmss";
    private static final String COLUMN_ID = "rxcui";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PRESC = "presc";
    private static final String COLUMN_TTY = "isingredient";
    private static final String COLUMN_URL = "url";
    private static final String COLUMN_INGREDIENTS = "ingredients";
    private static final String COLUMN_SOURCES = "sources";
    private static final String COLUMN_BRANDS = "brands";
    private static final String COLUMN_SCDC = "SCDC";
    private static final String COLUMN_SBDC = "SBDC";
    private static final String COLUMN_SBDG = "SBDG";
    private static final String COLUMN_SCD = "SCD";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_SCDG = "SCDG";
    private static final String COLUMN_SBD = "SBD";
    private static final String COLUMN_SPL_SET = "SPL_SET";
    private static final String COLUMN_INTERACTION = "INTERACTION";

    private static final String CREATE_MAIN_TABLE = "create table " + MAIN_TABLE +
            " (" + COLUMN_ID + " text not null," + COLUMN_NAME + " text not null collate nocase," +
            COLUMN_PRESC + " integer," + COLUMN_TTY + " text, " +
            COLUMN_URL + " text not null," + COLUMN_INGREDIENTS + " text," +
            COLUMN_SOURCES + " text," + COLUMN_BRANDS + " text," + COLUMN_SCDC + " text," +
            COLUMN_SBDC + " text," + COLUMN_SBDG + " text, " + COLUMN_SCD + " text," +
            COLUMN_DATE + " text not null," + COLUMN_SCDG + " text," + COLUMN_SBD + " text," +
            COLUMN_SPL_SET + " text," + COLUMN_INTERACTION + " text" + " )";
    private static final String CREATE_FAVORITE_TABLE = "create table " + FAVORITE_TABLE +
            " (" + COLUMN_ID + " text not null," + COLUMN_NAME + " text not null collate nocase," +
            COLUMN_PRESC + " integer" + " )";
    private static final String CREATE_RECENT_TABLE = "create table " + RECENT_TABLE +
            " (" + COLUMN_ID + " text not null," + COLUMN_NAME + " text not null collate nocase," +
            COLUMN_PRESC + " integer" + " )";
    private static final String DROP_MAIN_TABLE = "DROP TABLE IF EXISTS " + MAIN_TABLE;
    private static final String DROP_FAVORITE_TABLE = "DROP TABLE IF EXISTS " + FAVORITE_TABLE;
    private static final String DROP_RECENT_TABLE = "DROP TABLE IF EXISTS " + RECENT_TABLE;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "KnowYourMeds.db";
    private static DatabaseHelper mSingleton;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (mSingleton == null) {
            mSingleton = new DatabaseHelper(context);
        }
        return mSingleton;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_MAIN_TABLE);
        sqLiteDatabase.execSQL(CREATE_FAVORITE_TABLE);
        sqLiteDatabase.execSQL(CREATE_RECENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_MAIN_TABLE);
        sqLiteDatabase.execSQL(DROP_FAVORITE_TABLE);
        sqLiteDatabase.execSQL(DROP_RECENT_TABLE);
        onCreate(sqLiteDatabase);
    }

    public void createEntry(String tableName, Medicine med) {
        /** Add info to main table first */
        deleteEntry(MAIN_TABLE, med);
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, med.getRxnormId());
        values.put(COLUMN_NAME, med.getName());
        values.put(COLUMN_PRESC, med.isPrescribable() ? 1 : 0);
        values.put(COLUMN_TTY, med.getTty());
        values.put(COLUMN_URL, med.getUrl());
        values.put(COLUMN_INGREDIENTS, convertToJsonString(COLUMN_INGREDIENTS, med.getIngredients()));
        values.put(COLUMN_SOURCES, convertToJsonString(COLUMN_SOURCES, med.getSources()));
        values.put(COLUMN_BRANDS, convertToJsonString(COLUMN_BRANDS, med.getBrands()));
        values.put(COLUMN_SCDC, convertToJsonString(COLUMN_SCDC, med.getScdc()));
        values.put(COLUMN_SBDC, convertToJsonString(COLUMN_SBDC, med.getSbdc()));
        values.put(COLUMN_SBDG, convertToJsonString(COLUMN_SBDG, med.getSbdg()));
        values.put(COLUMN_SCD, convertToJsonString(COLUMN_SCD, med.getScd()));
        values.put(COLUMN_DATE, convertToDateString(med.getDate()));
        values.put(COLUMN_SCDG, convertToJsonString(COLUMN_SCDG, med.getScdg()));
        values.put(COLUMN_SBD, convertToJsonString(COLUMN_SBD, med.getSbd()));
        values.put(COLUMN_SPL_SET, convertToJsonString(COLUMN_SBD, med.getSplSetId()));
        values.put(COLUMN_INTERACTION, convertRecursiveMapToJsonString(COLUMN_INTERACTION,
                med.getInteractions()));
        SQLiteDatabase db = getWritableDatabase();
        db.insert(MAIN_TABLE, null, values);
        db.close();

        /** Add entry to specific table */
        deleteEntry(tableName, med);
        values = new ContentValues();
        values.put(COLUMN_ID, med.getRxnormId());
        values.put(COLUMN_NAME, med.getName());
        values.put(COLUMN_PRESC, med.isPrescribable() ? 1 : 0);
        db = getWritableDatabase();
        db.insert(tableName, null, values);
        db.close();
    }

    public void updateEntry(String tableName, Medicine med) {
        /** Update main table first */
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, med.getName());
        values.put(COLUMN_PRESC, med.isPrescribable() ? 1 : 0);
        values.put(COLUMN_TTY, med.getTty());
        values.put(COLUMN_URL, med.getUrl());
        values.put(COLUMN_INGREDIENTS, convertToJsonString(COLUMN_INGREDIENTS, med.getIngredients()));
        values.put(COLUMN_SOURCES, convertToJsonString(COLUMN_SOURCES, med.getSources()));
        values.put(COLUMN_BRANDS, convertToJsonString(COLUMN_BRANDS, med.getBrands()));
        values.put(COLUMN_SCDC, convertToJsonString(COLUMN_SCDC, med.getScdc()));
        values.put(COLUMN_SBDC, convertToJsonString(COLUMN_SBDC, med.getSbdc()));
        values.put(COLUMN_SBDG, convertToJsonString(COLUMN_SBDG, med.getSbdg()));
        values.put(COLUMN_SCD, convertToJsonString(COLUMN_SCD, med.getScd()));
        values.put(COLUMN_DATE, convertToDateString(med.getDate()));
        values.put(COLUMN_SCDG, convertToJsonString(COLUMN_SCDG, med.getScdg()));
        values.put(COLUMN_SBD, convertToJsonString(COLUMN_SBD, med.getSbd()));
        values.put(COLUMN_SPL_SET, convertToJsonString(COLUMN_SBD, med.getSplSetId()));
        values.put(COLUMN_INTERACTION, convertRecursiveMapToJsonString(COLUMN_INTERACTION,
                med.getInteractions()));
        SQLiteDatabase db = getWritableDatabase();
        db.update(MAIN_TABLE, values, COLUMN_ID + "='" + med.getRxnormId() + "'", null);

        /** Update specific table */
        values = new ContentValues();
        values.put(COLUMN_NAME, med.getName());
        values.put(COLUMN_PRESC, med.isPrescribable() ? 1 : 0);
        values.put(COLUMN_TTY, med.getTty());
        db.update(tableName, values, COLUMN_ID + "='" + med.getRxnormId() + "'", null);
        db.close();
    }

    private String convertToJsonString(String column, ArrayList<String> list) {
        if (list == null) {
            return null;
        }
        JSONObject json = new JSONObject();
        try {
            json.put(column, new JSONArray(list));
            return json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String convertToJsonString(String column, Map<String, String> map) {
        if (map == null) {
            return null;
        }
        JSONObject json = new JSONObject();
        try {
            json.put(column, new JSONObject(map));
            return json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String convertRecursiveMapToJsonString(String column, Map<String,
            Map<String, String>> map) {
        if (map == null) {
            return null;
        }
        JSONObject json = new JSONObject();
        try {
            json.put(column, new JSONObject(map));
            return json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String convertToDateString(Date date) {
        SimpleDateFormat format1 = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        return format1.format(date);
    }

    public List<Medicine> getAllShortEntries(String tableName) {
        List<Medicine> medList = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_NAME, COLUMN_PRESC};
        Cursor cursor = db.query(tableName, columns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Medicine med = cursorToEntry(cursor, true);
            medList.add(med);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return medList;
    }

    public List<Medicine> getAllShortEntriesByName(String name) {
        List<Medicine> medList = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_NAME, COLUMN_PRESC};
        Cursor cursor = db.query(MAIN_TABLE, columns, COLUMN_NAME + "='" + name + "'",
                null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Medicine med = cursorToEntry(cursor, true);
            medList.add(med);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return medList;
    }

    public Medicine getEntry(String rxcui) {
        SQLiteDatabase db = getWritableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_NAME, COLUMN_PRESC, COLUMN_TTY, COLUMN_URL,
                COLUMN_INGREDIENTS, COLUMN_SOURCES, COLUMN_BRANDS, COLUMN_SCDC, COLUMN_SBDC,
                COLUMN_SBDG, COLUMN_SCD, COLUMN_DATE, COLUMN_SCDG, COLUMN_SBD, COLUMN_SPL_SET,
                COLUMN_INTERACTION};
        Cursor cursor = db.query(MAIN_TABLE, columns, COLUMN_ID + "='" + rxcui + "'", null, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            return null;
        }
        Medicine med = cursorToEntry(cursor, false);
        cursor.close();
        db.close();
        return med;
    }

    private Medicine cursorToEntry(Cursor cursor, boolean isShort) {
        Medicine medicine = new Medicine();
        medicine.setRxnormId(cursor.getString(0));
        medicine.setName(cursor.getString(1));
        medicine.setIsPrescribable(cursor.getInt(2) == 1);
        if (!isShort) {
            medicine.setTty(cursor.getString(3));
            medicine.setUrl(cursor.getString(4));
            medicine.setIngredients(jsonToList(COLUMN_INGREDIENTS, cursor.getString(5)));
            medicine.setSources(jsonToList(COLUMN_SOURCES, cursor.getString(6)));
            medicine.setBrands(jsonToList(COLUMN_BRANDS, cursor.getString(7)));
            medicine.setScdc(jsonToList(COLUMN_SCDC, cursor.getString(8)));
            medicine.setSbdc(jsonToList(COLUMN_SBDC, cursor.getString(9)));
            medicine.setSbdg(jsonToList(COLUMN_SBDG, cursor.getString(10)));
            medicine.setScd(jsonToList(COLUMN_SCD, cursor.getString(11)));
            medicine.setDate(convertFromDateString(cursor.getString(12)));
            medicine.setScdg(jsonToList(COLUMN_SCDG, cursor.getString(13)));
            medicine.setSbd(jsonToList(COLUMN_SBD, cursor.getString(14)));
            medicine.setSplSetId(jsonToMap(COLUMN_SBD, cursor.getString(15)));
            medicine.setInteractions(jsonToRecursiveMap(COLUMN_INTERACTION, cursor.getString(16)));
        }
        return medicine;
    }

    private ArrayList<String> jsonToList(String column, String string) {
        try {
            JSONObject json = new JSONObject(string);
            JSONArray array = json.optJSONArray(column);
            ArrayList<String> list = new ArrayList<>();
            for (int index = 0; index < array.length(); index++) {
                list.add(array.optString(index));
            }
            return list;
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Map<String, String> jsonToMap(String column, String string) {
        Map<String, String> retMap = new LinkedHashMap<>();
        try {
            JSONObject json = new JSONObject(string).getJSONObject(column);
            Iterator<String> keysItr = json.keys();
            while (keysItr.hasNext()) {
                String key = keysItr.next();
                String value = (String) json.get(key);
                retMap.put(key, value);
            }
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }
        return retMap;
    }

    private Map<String, Map<String, String>> jsonToRecursiveMap(String column, String string) {
        Map<String, Map<String, String>> retMap = new LinkedHashMap<>();
        try {
            JSONObject json = new JSONObject(string).getJSONObject(column);
            Iterator<String> keysItr = json.keys();
            while (keysItr.hasNext()) {
                String key = keysItr.next();
                Object value = json.get(key);
                Map<String, String> map = new LinkedHashMap<>();
                if (value instanceof JSONObject) {
                    JSONObject jsonObject = (JSONObject) value;
                    Iterator<String> iterator = jsonObject.keys();
                    while (iterator.hasNext()) {
                        String innerKey = iterator.next();
                        map.put(innerKey, (String) jsonObject.get(innerKey));
                    }
                }
                retMap.put(key, map);
            }
            Log.d("RxNav", retMap.toString());
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }
        return retMap;
    }


    private Date convertFromDateString(String string) {
        DateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        try {
            return format.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteEntry(String tableName, Medicine med) {
        String id = med.getRxnormId();
        SQLiteDatabase db = getWritableDatabase();
        db.delete(tableName, COLUMN_ID + " = " + id, null);
        db.close();
    }

    public void deleteAll(String tableName) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(tableName, null, null);
        db.close();
    }

    public void deleteAll() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(MAIN_TABLE, null, null);
        db.close();
    }
}
