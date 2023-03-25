package cn.edu.hznu.addressbook;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.net.URI;

public class DatabaseProvider extends ContentProvider {

    private static final UriMatcher uriMatcher;
    private MyDatabaseHelper dbHelper;
    public static final String AUTHORITY = "cn.edu.hznu.addressbook.provider";
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("cn.edu.hznu.addressbook","contact",0);
    }

    public boolean onCreate() {
        dbHelper = new MyDatabaseHelper(getContext(), "addressList", null, 1);
        return true;
    }
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        cursor = db.query("contact",projection,selection,selectionArgs,null,null,sortOrder);
        return cursor;
    }
    public Uri insert(Uri uri, ContentValues values){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri uriReturn = null;
        long name = db.insert("contact",null,values);
        uriReturn = Uri.parse("content://"+AUTHORITY+"/contact/"+name);
        return uriReturn;
    }
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int updatedRows = 0;
        updatedRows = db.update("contact", values, selection, selectionArgs);
        return updatedRows;
    }
    public int delete(Uri uri, String selection, String[] selectionArgs){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deletedRows = 0;
        deletedRows = db.delete("contact", selection, selectionArgs);
        return 0;
    }
    public String getType(Uri uri) {
        return "vnd.android.cursor.dir/vnd.cn.edu.hznu.addressbook.contact";
    }
}
