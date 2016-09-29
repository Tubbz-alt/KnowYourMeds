package com.tompee.utilities.knowyourmeds.controller.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tompee.utilities.knowyourmeds.model.Medicine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String FAVORITE_TABLE = "favorite";
    public static final String RECENT_TABLE = "recent";
    private static final String DATE_FORMAT = "yyyyMMddHHmmss";
    private static final String COLUMN_ID = "rxcui";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PRESC = "presc";
    private static final String COLUMN_IS_INGREDIENT = "isingredient";
    private static final String COLUMN_URL = "url";
    private static final String COLUMN_INGREDIENTS = "ingredients";
    private static final String COLUMN_SOURCES = "sources";
    private static final String COLUMN_BRANDS = "brands";
    private static final String COLUMN_SCDC = "SCDC";
    private static final String COLUMN_SBDC = "SBDC";
    private static final String COLUMN_SBDG = "SBDG";
    private static final String COLUMN_SCD = "SCD";
    private static final String COLUMN_DATE = "date";

    private static final String CREATE_FAVORITE_TABLE = "create table " + FAVORITE_TABLE +
            " (" + COLUMN_ID + " text not null," + COLUMN_NAME + " text not null," +
            COLUMN_PRESC + " integer," + COLUMN_IS_INGREDIENT + " integer, " +
            COLUMN_URL + " text not null," + COLUMN_INGREDIENTS + " text," +
            COLUMN_SOURCES + " text," + COLUMN_BRANDS + " text," + COLUMN_SCDC + " text," +
            COLUMN_SBDC + " text," + COLUMN_SBDG + " text, " + COLUMN_SCD + " text," +
            COLUMN_DATE + " text not null" + " )";

    private static final String CREATE_RECENT_TABLE = "create table " + RECENT_TABLE +
            " (" + COLUMN_ID + " text not null," + COLUMN_NAME + " text not null," +
            COLUMN_PRESC + " integer," + COLUMN_IS_INGREDIENT + " integer, " +
            COLUMN_URL + " text not null," + COLUMN_INGREDIENTS + " text," +
            COLUMN_SOURCES + " text," + COLUMN_BRANDS + " text," + COLUMN_SCDC + " text," +
            COLUMN_SBDC + " text," + COLUMN_SBDG + " text, " + COLUMN_SCD + " text," +
            COLUMN_DATE + " text not null" + " )";

    private static final String DROP_FAVORITE_TABLE = "DROP TABLE IF EXISTS " + FAVORITE_TABLE;
    private static final String DROP_RECENT_TABLE = "DROP TABLE IF EXISTS " + RECENT_TABLE;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "KnowYourMeds.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_FAVORITE_TABLE);
        sqLiteDatabase.execSQL(CREATE_RECENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_FAVORITE_TABLE);
        sqLiteDatabase.execSQL(DROP_RECENT_TABLE);
        onCreate(sqLiteDatabase);
    }

    public void createEntry(String tableName, Medicine med) {
        deleteEntry(tableName, med);
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, med.getRxnormId());
        values.put(COLUMN_NAME, med.getName());
        values.put(COLUMN_PRESC, med.isPrescribable() ? 1 : 0);
        values.put(COLUMN_IS_INGREDIENT, med.isIngredient() ? 1 : 0);
        values.put(COLUMN_URL, med.getUrl());
        values.put(COLUMN_INGREDIENTS, convertToJsonString(COLUMN_INGREDIENTS, med.getIngredients()));
        values.put(COLUMN_SOURCES, convertToJsonString(COLUMN_SOURCES, med.getSources()));
        values.put(COLUMN_BRANDS, convertToJsonString(COLUMN_BRANDS, med.getBrands()));
        values.put(COLUMN_SCDC, convertToJsonString(COLUMN_SCDC, med.getScdc()));
        values.put(COLUMN_SBDC, convertToJsonString(COLUMN_SBDC, med.getSbdc()));
        values.put(COLUMN_SBDG, convertToJsonString(COLUMN_SBDG, med.getSbdg()));
        values.put(COLUMN_SCD, convertToJsonString(COLUMN_SCD, med.getScd()));
        values.put(COLUMN_DATE, convertToDateString(med.getDate()));

        SQLiteDatabase db = getWritableDatabase();
        db.insert(tableName, null, values);
        db.close();
    }

    public void updateEntry(String tableName, Medicine med) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, convertToDateString(med.getDate()));

        SQLiteDatabase db = getWritableDatabase();
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

    private String convertToDateString(Date date) {
        SimpleDateFormat format1 = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        return format1.format(date);
    }

    public List<Medicine> getAllEntries(String tableName) {
        List<Medicine> medList = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_NAME, COLUMN_PRESC, COLUMN_IS_INGREDIENT, COLUMN_URL,
                COLUMN_INGREDIENTS, COLUMN_SOURCES, COLUMN_BRANDS, COLUMN_SCDC, COLUMN_SBDC,
                COLUMN_SBDG, COLUMN_SCD, COLUMN_DATE};
        Cursor cursor = db.query(tableName, columns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Medicine med = cursorToEntry(cursor);
            medList.add(med);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return medList;
    }

    public Medicine getEntry(String tableName, String name) {
        SQLiteDatabase db = getWritableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_NAME, COLUMN_PRESC, COLUMN_IS_INGREDIENT, COLUMN_URL,
                COLUMN_INGREDIENTS, COLUMN_SOURCES, COLUMN_BRANDS, COLUMN_SCDC, COLUMN_SBDC,
                COLUMN_SBDG, COLUMN_SCD, COLUMN_DATE};
        Cursor cursor = db.query(tableName, columns, COLUMN_NAME + "='" + name + "'", null, null, null, null);
        cursor.moveToFirst();
        Medicine med = cursorToEntry(cursor);
        cursor.close();
        db.close();
        return med;
    }

    private Medicine cursorToEntry(Cursor cursor) {
        Medicine medicine = new Medicine();
        medicine.setRxnormId(cursor.getString(0));
        medicine.setName(cursor.getString(1));
        medicine.setIsPrescribable(cursor.getInt(2) == 1);
        medicine.setIsIngredient(cursor.getInt(3) == 1);
        medicine.setUrl(cursor.getString(4));
        medicine.setIngredients(convertFromJsonString(COLUMN_INGREDIENTS, cursor.getString(5)));
        medicine.setSources(convertFromJsonString(COLUMN_SOURCES, cursor.getString(6)));
        medicine.setBrands(convertFromJsonString(COLUMN_BRANDS, cursor.getString(7)));
        medicine.setScdc(convertFromJsonString(COLUMN_SCDC, cursor.getString(8)));
        medicine.setSbdc(convertFromJsonString(COLUMN_SBDC, cursor.getString(9)));
        medicine.setSbdg(convertFromJsonString(COLUMN_SBDG, cursor.getString(10)));
        medicine.setScd(convertFromJsonString(COLUMN_SCD, cursor.getString(11)));
        medicine.setDate(convertFromDateString(cursor.getString(12)));
        return medicine;
    }

    private ArrayList<String> convertFromJsonString(String column, String string) {
        try {
            JSONObject json = new JSONObject(string);
            JSONArray array = json.optJSONArray(column);
            ArrayList<String> list = new ArrayList<>();
            for (int index = 0; index < array.length(); index++) {
                list.add(array.optString(index));
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
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
}
