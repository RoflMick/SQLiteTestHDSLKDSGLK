package com.example.radosek.sqlitetest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by radosek on 8/12/2015.
 */
public class DBHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "MyDBName03.db";
    public static final String CONTACTS_TABLE_NAME = "contacts";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_NAME = "name";
    public static final String CONTACTS_COLUMN_TYPE = "type";
    public static final String CONTACTS_COLUMN_COST = "cost";

    public static ArrayList<String> arrayList = new ArrayList<String>();

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("create table contacts " + "(id integer primary key, name text)");
        db.execSQL("CREATE TABLE contacts " + "(id INTEGER PRIMARY KEY, name TEXT, type INTEGER, cost INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public boolean insertContact(String name, String type, String cost) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_COLUMN_NAME, name);
        contentValues.put(CONTACTS_COLUMN_TYPE, type);
        contentValues.put(CONTACTS_COLUMN_COST, cost);
        db.insert("contacts", null, contentValues);
        return true;
    }

    //Cursor representuje vracena data
    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from contacts where id=" + id + "", null);
        //Cursor res =  db.rawQuery( "select * from contacts LIMIT 1 OFFSET "+id+"", null );
        return res;
    }

    public boolean updateContact (Integer id, String name, String type, String cost) {
        //TODO update zaznam
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_COLUMN_ID, id);
        contentValues.put(CONTACTS_COLUMN_NAME, name);
        contentValues.put(CONTACTS_COLUMN_TYPE, type);
        contentValues.put(CONTACTS_COLUMN_COST, cost);
        db.update(CONTACTS_TABLE_NAME, contentValues, "id = ?", new String[]{String.valueOf(id)});
        return true;

    }

    public void setAllContacs() {
        arrayList.clear();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            arrayList.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
            res.moveToNext();
            arrayList.get(0);
        }
    }

    public ArrayList<String> getAllContacsName() {
        return arrayList;
    }

    public void removeAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CONTACTS_TABLE_NAME, "1", null);
    }

    public void removeContact(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
//        String whereClause = "id=?";
//        String[] whereArgs = new String[] { String.valueOf(id) };
//        db.delete(CONTACTS_TABLE_NAME, whereClause, whereArgs);
//        String query =  "DELETE FROM " + CONTACTS_TABLE_NAME + " WHERE "
//                + CONTACTS_COLUMN_ID + " = '" + id + "'";
//        db.execSQL(query);
        db.delete(CONTACTS_TABLE_NAME, "id = ?", new String[]{String.valueOf(id)});
        db.execSQL("UPDATE " + CONTACTS_TABLE_NAME + " SET id = id - 1 WHERE id > " + id + ";");
        Log.d("del", "Item of id " + id + " successfully deleted" );

    }
}
