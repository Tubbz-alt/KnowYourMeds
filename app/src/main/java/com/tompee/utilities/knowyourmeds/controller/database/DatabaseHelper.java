package com.tompee.utilities.knowyourmeds.controller.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tompee.utilities.knowyourmeds.model.Medicine;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String FAVORITE_TABLE = "favorite";
    public static final String RECENT_TABLE = "recent";
    private static final String COLUMN_ID = "rxcui";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PRESC = "presc";

    private static final String CREATE_FAVORITE_TABLE = "create table " + FAVORITE_TABLE +
            " (" + COLUMN_ID + " text not null," + COLUMN_NAME + " text not null," +
            COLUMN_PRESC + " integer" + " )";
    private static final String CREATE_RECENT_TABLE = "create table " + RECENT_TABLE +
            " (" + COLUMN_ID + " text not null," + COLUMN_NAME + " text not null," +
            COLUMN_PRESC + " integer" + " )";
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

        SQLiteDatabase db = getWritableDatabase();
        db.insert(tableName, null, values);
        db.close();
    }

    public List<Medicine> getAllEntries(String tableName) {
        List<Medicine> medList = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_NAME, COLUMN_PRESC};
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

    private Medicine cursorToEntry(Cursor cursor) {
        Medicine comment = new Medicine();
        comment.setRxnormId(cursor.getString(0));
        comment.setName(cursor.getString(1));
        comment.setIsPrescribable(cursor.getInt(1) == 1);
        return comment;
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
