package com.example.mycontacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.util.Log;

public class MyContactsDataBase extends SQLiteOpenHelper { 
	/** The name of the database file on the file system */ 
	private static final String DATABASE_NAME = "MyContacts"; 
	/** The version of the database that this class understands. */ 
	private static final int DATABASE_VERSION = 2;
	private static final String LOG_TAG = "MyContactsDataBase";

	/** Keep track of context so that we can load SQL from string resources */ 
	private final Context mContext;

	/** Constructor */ 
	public MyContactsDataBase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION); 
		this.mContext = context;
	}

	/** Called when it is time to create the database */ 
	@Override public void onCreate(SQLiteDatabase db) {
		String[] sql = mContext.getString(R.string.MyContactsDataBase_onCreate).split("\n");
		db.beginTransaction(); 
		try {
			// Create tables and test data 
			execMultipleSQL(db, sql); 
			db.setTransactionSuccessful();
		} catch (SQLException e) { 
			Log.e("Error creating tables and debug data", e.toString());
			throw e; 
		} finally {

			db.endTransaction();
		}
	}

	/** Called when the database must be upgraded */ 
	@Override 
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
		String[] sql = mContext.getString(R.string.MyContactsDataBase_onUpgrade).split("\n");
		db.beginTransaction(); 
		try {
			execMultipleSQL(db, sql);
			db.setTransactionSuccessful(); } 
		catch (SQLException e) {
			Log.e("Error upgrading tables and debug data", e.toString());
			throw e; 
		} finally {

			db.endTransaction();
		}

		// This is cheating. In the real world, you'll need to add columns, not rebuild from scratch.
		onCreate(db);
	}

	/** * Execute all of the SQL statements in the String[] array 
	 * * @param db The database on which to execute the statements 
	 * * @param sql An array of SQL statements to execute */
	private void execMultipleSQL(SQLiteDatabase db, String[] sql){ 
		for( String s : sql )
			if (s.trim().length()>0) 
				db.execSQL(s);
	}
	
	public static class ContactsCursor extends SQLiteCursor {
        public static enum SortBy{
            name,
            surname
        }
        private static final String QUERY =
            "SELECT _id, name " +
                    "FROM contacts " +
                    "ORDER BY ";

        private ContactsCursor(SQLiteDatabase db, SQLiteCursorDriver driver,
                String editTable, SQLiteQuery query) {
            super(db, driver, editTable, query);
        }

        private static class Factory implements SQLiteDatabase.CursorFactory{
            @Override
            public Cursor newCursor(SQLiteDatabase db,
                    SQLiteCursorDriver driver, String editTable,
                    SQLiteQuery query) {
                return new ContactsCursor(db, driver, editTable, query);
            }
        }

        public long getColId() {
        	return getLong(getColumnIndexOrThrow("_id"));
        }
        
        public String getColName() {
            return getString(getColumnIndexOrThrow("name"));
        }
        
        public String getColSurname() {
            return getString(getColumnIndexOrThrow("surname"));
        }
    }
	
	 public static class ContactDetailCursor extends SQLiteCursor {
	        /** The query for this cursor */
	        private static final String QUERY =
	        		  "SELECT _id, name, surname " +
	                  "FROM contacts " +        
	                  "WHERE _id = ";
	        
	        /** Cursor constructor */
	        private ContactDetailCursor(SQLiteDatabase db, SQLiteCursorDriver driver,
	                String editTable, SQLiteQuery query) {
	            super(db, driver, editTable, query);
	        }
	        
	        /** Private factory class necessary for rawQueryWithFactory() call */
	        private static class Factory implements SQLiteDatabase.CursorFactory{
	            @Override
	            public Cursor newCursor(SQLiteDatabase db,
	                    SQLiteCursorDriver driver, String editTable,
	                    SQLiteQuery query) {
	                return new ContactDetailCursor(db, driver, editTable, query);
	            }
	        }
	        
	        /* Accessor functions -- one per database column */
	        public long getColId() {
	        	return getLong(getColumnIndexOrThrow("_id"));
	        }
	        
	        public String getColName() {
	            return getString(getColumnIndexOrThrow("name"));
	        }
	        
	        public String getColSurname() {
	            return getString(getColumnIndexOrThrow("surname"));
	        }
	 }
	
	 /** Return a sorted ContactsCursor
     * @param sortBy the sort criteria
     */
    public ContactsCursor getJobs(ContactsCursor.SortBy sortBy) {
        String sql = ContactsCursor.QUERY+sortBy.toString();
        SQLiteDatabase d = getReadableDatabase();
        ContactsCursor c = (ContactsCursor) d.rawQueryWithFactory(
            new ContactsCursor.Factory(),
            sql,
            null,
            null);
        c.moveToFirst();
        return c;
    }
    
    public ContactDetailCursor getContactDetails(long contactId) {
        String sql = ContactDetailCursor.QUERY + contactId;
        SQLiteDatabase d = getReadableDatabase();
        ContactDetailCursor c = (ContactDetailCursor) d.rawQueryWithFactory(
            new ContactDetailCursor.Factory(),
            sql,
            null,
            null);
        c.moveToFirst();
        return c;
    }
    
    public void addContact(String name, String surname){
        ContentValues map = new ContentValues();
        map.put("name", name);
        map.put("surname", surname);
        try{
            getWritableDatabase().insert("contacts", null, map);
        } catch (SQLException e) {
            Log.e("Error writing new contact", e.toString());
        }
    }
    
    public void deleteContact(long contact_id) {
        String[] whereArgs = new String[]{Long.toString(contact_id)};
        try{
            getWritableDatabase().delete("contacts", "_id=?", whereArgs);
        } catch (SQLException e) {
            Log.e("Error deleting contact", e.toString());
        }
    }

}
