package id.tech.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.Locale;

public class SLite {
	public static SQLiteDatabase openDatabase(Context context) {
		SQLiteDatabase db;

		db = context.openOrCreateDatabase("db_htc",
				SQLiteDatabase.CREATE_IF_NECESSARY, null);

		db.setVersion(3);
		db.setLocale(Locale.getDefault());
		db.setLockingEnabled(true);
		return db;

	}

	public static void proccessTableRss(SQLiteDatabase db) {
		// delete jika table menu ada
		final String DELETE_TABLE_RSS = "DROP TABLE IF EXISTS tbl_rss ";

		// create ulang table
		final String CREATE_TABLE_RSS = "CREATE TABLE tbl_rss ("
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT," + "idFeed TEXT,"  + "titleFeed TEXT,"
				+ "urlNewsFeed TEXT," + "urlNewsThumb TEXT,"
				+ "urlNewsThumbBig TEXT," + "urlNewsCommentFeed TEXT,"
				+ "publishDate TEXT," + "creatorNews TEXT,"
				+ "categoryNews TEXT, " + "newsDesc TEXT, " + "content TEXT, "
				+ "commentCount TEXT );";

		db.execSQL(DELETE_TABLE_RSS);
		db.execSQL(CREATE_TABLE_RSS);
	}
	
	
	
	public static void processUpdateTableRss(SQLiteDatabase db){
		final String CREATE_TABLE_IF_NOT_EXIST = "CREATE TABLE IF NOT EXISTS tbl_rss ("
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT," + "idFeed TEXT,"  + "titleFeed TEXT,"
				+ "urlNewsFeed TEXT," + "urlNewsThumb TEXT,"
				+ "urlNewsThumbBig TEXT," + "urlNewsCommentFeed TEXT,"
				+ "publishDate TEXT," + "creatorNews TEXT,"
				+ "categoryNews TEXT, " + "newsDesc TEXT, " + "content TEXT, "
				+ "commentCount TEXT );";
		
		db.execSQL(CREATE_TABLE_IF_NOT_EXIST);
	}
	
	public static void processInsertReplaceRss(SQLiteDatabase db, ContentValues values){
		final String INSERT_OR_REPLACE = "INSERT OR REPLACE into tbl_rss ("
				+ "idFeed,"  + "titleFeed,"
				+ "urlNewsFeed," + "urlNewsThumb,"
				+ "urlNewsThumbBig," + "urlNewsCommentFeed,"
				+ "publishDate," + "creatorNews,"
				+ "categoryNews, " + "newsDesc, " + "content, "
				+ "commentCount) VALUES ("
				+ "'(SELECT idFeed FROM tbl_rss WHERE idFeed = " + values.getAsString("idFeed") + ")',"
				+ "'" + values.getAsString("titleFeed") + "',"
				+ "'" + values.getAsString("urlNewsFeed") + "',"
				+ "'" + values.getAsString("urlNewsThumb") + "',"
				+ "'" + values.getAsString("urlNewsThumbBig") + "',"
				+ "'" + values.getAsString("urlNewsCommentFeed") + "',"
				+ "'" + values.getAsString("publishDate") + "',"
				+ "'" + values.getAsString("creatorNews") + "',"
				+ "'" + values.getAsString("categoryNews") + "',"
				+ "'" + values.getAsString("newsDesc") + "',"
				+ "'" + values.getAsString("content") + "',"
				+ "'" + values.getAsString("commentCount") + "')" 
				;
		
		db.execSQL(INSERT_OR_REPLACE);
	}
	
	public static void processUpdateTableRss_home(SQLiteDatabase db){
		final String CREATE_TABLE_IF_NOT_EXIST = "CREATE TABLE IF NOT EXISTS tbl_rss_home ("
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT," + "idFeed TEXT,"  + "titleFeed TEXT,"
				+ "urlNewsFeed TEXT," + "urlNewsThumb TEXT,"
				+ "urlNewsThumbBig TEXT," + "urlNewsCommentFeed TEXT,"
				+ "publishDate TEXT," + "creatorNews TEXT,"
				+ "categoryNews TEXT, " + "newsDesc TEXT, " + "content TEXT, "
				+ "commentCount TEXT );";
		
		db.execSQL(CREATE_TABLE_IF_NOT_EXIST);
	}
	
	

}
