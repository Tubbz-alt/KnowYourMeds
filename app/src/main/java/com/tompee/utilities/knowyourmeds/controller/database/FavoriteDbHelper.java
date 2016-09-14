package com.tompee.utilities.knowyourmeds.controller.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tompee.utilities.knowyourmeds.model.Medicine;

import java.util.ArrayList;
import java.util.List;

public class FavoriteDbHelper extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "favorites";
    private static final String COLUMN_ID = "rxcui";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PRESC = "presc";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Favorite.db";
    private static final String SQL_CREATE_ENTRIES = "create table" +
            TABLE_NAME + " (" + COLUMN_ID + "text not null," + COLUMN_NAME + "text not null," +
            COLUMN_PRESC + "integer" + " )";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public FavoriteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    public void createEntry(Medicine med) {
        ContentValues values = new ContentValues();
        values.put(FavoriteDbHelper.COLUMN_ID, med.getRxnormId());
        values.put(FavoriteDbHelper.COLUMN_NAME, med.getName());
        values.put(FavoriteDbHelper.COLUMN_PRESC, med.isPrescribable() ? 1 : 0);

        SQLiteDatabase db = getWritableDatabase();
        db.insert(FavoriteDbHelper.TABLE_NAME, null, values);
        db.close();
    }

    public List<Medicine> getAllEntries() {
        List<Medicine> medList = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_NAME, COLUMN_PRESC};
        Cursor cursor = db.query(TABLE_NAME, columns, null, null, null, null, null);

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

    public void deleteEntry(Medicine med) {
        String id = med.getRxnormId();
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = " + id, null);
    }
}
