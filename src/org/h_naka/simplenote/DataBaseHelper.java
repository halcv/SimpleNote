package org.h_naka.simplenote;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.database.Cursor;
import android.content.ContentValues;

public class DataBaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase m_database;
    
    public DataBaseHelper(MainActivity activity) {
        super(activity,DBAdapter.DATABASE_NAME,null,DBAdapter.DATABASE_VERSION);
        m_database = null;
    }
    
	@Override
	public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("CREATE TABLE " + DBAdapter.TABLE_NAME);
            builder.append(" (");
            builder.append(DBAdapter.COL_ID + " INTEGER PRIMARY KEY NOT NULL,");
            builder.append(DBAdapter.COL_NOTE + " TEXT NOT NULL,");
            builder.append(DBAdapter.COL_LASTUPDATE + " TEXT NOT NULL");
            builder.append(");");
            db.execSQL(builder.toString());
            db.setTransactionSuccessful();
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        try {
            db.execSQL("DROP TAbLE IF EXISTS " + DBAdapter.TABLE_NAME);
            onCreate(db);
        } catch(SQLException e) {
            e.printStackTrace();
        }
	}

    public boolean dbOpen() {
        boolean ret = true;;
        try {
            m_database = getWritableDatabase();
        } catch(SQLiteException e) {
            e.printStackTrace();
            ret = false;
        }
        
        return ret;
    }

    public void dbClose() {
        m_database.close();
    }

    public boolean deleteAllNotes() {
        boolean ret = false;
        if (m_database.delete(DBAdapter.TABLE_NAME,null,null) > 0) {
            ret = true;
        }
        
        return ret;
    }

    public boolean deleteNote(String date) {
        boolean ret = false;
        String[] array = {date};
        if (m_database.delete(DBAdapter.TABLE_NAME,DBAdapter.COL_LASTUPDATE + " = ?",array) > 0) {
            ret = true;
        }

        return ret;
    }

    public Cursor getAllNotes() {
    	return m_database.query(DBAdapter.TABLE_NAME,null,null,null,null,null,null);
    }
    
    public boolean saveNote(String note,String date) {
        boolean ret = true;

        ContentValues values = new ContentValues();
        values.put(DBAdapter.COL_NOTE,note);
        values.put(DBAdapter.COL_LASTUPDATE,date);

        try {
            m_database.insertOrThrow(DBAdapter.TABLE_NAME,null,values);
        } catch(SQLException e) {
            ret = false;
            e.printStackTrace();
        }

        return ret;
    }
}

