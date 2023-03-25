package cn.edu.hznu.sqlitedbtest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class DatabaseProvider extends ContentProvider {

	public static final int BOOK_DIR = 0;       //访问Book表中的所有数据
	public static final int BOOK_ITEM = 1;      //访问Book表中的单条数据
	public static final int CATEGORY_DIR = 2;   //访问Category表中的所有数据
	public static final int CATEGORY_ITEM = 3;  //访问Category表中的单条数据
	public static final String AUTHORITY = "cn.edu.hznu.sqlitedbtest.provider";

	private static UriMatcher uriMatcher;
	private MyDatabaseHelper dbHelper;

	//初始化UriMatcher
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, "book", BOOK_DIR);
		uriMatcher.addURI(AUTHORITY, "book/#", BOOK_ITEM);
		uriMatcher.addURI(AUTHORITY, "category", CATEGORY_DIR);
		uriMatcher.addURI(AUTHORITY, "category/#", CATEGORY_ITEM);
	}

	@Override
	public boolean onCreate() {
		dbHelper = new MyDatabaseHelper(getContext(), "BookStore.db", null, 2);
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = null;
		switch (uriMatcher.match(uri)) {
		case BOOK_DIR:
			cursor = db.query("Book", projection, selection, selectionArgs, null, null, sortOrder);
			break;
		case BOOK_ITEM:
			String bookId = uri.getPathSegments().get(1);
			cursor = db.query("Book", projection, "id = ?", new String[] { bookId }, null, null,
					sortOrder);
			break;
		case CATEGORY_DIR:
			cursor = db.query("Category", projection, selection, selectionArgs, null, null,
					sortOrder);
			break;
		case CATEGORY_ITEM:
			String categoryId = uri.getPathSegments().get(1);
			cursor = db.query("Category", projection, "id = ?", new String[] { categoryId }, null,
					null, sortOrder);
			break;
		default:
			break;
		}
		return cursor;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Uri uriReturn = null;
		switch (uriMatcher.match(uri)) {
		case BOOK_DIR:
		case BOOK_ITEM:
			long newBookId = db.insert("Book", null, values);
			uriReturn = Uri.parse("content://" + AUTHORITY + "/book/" + newBookId);
			break;
		case CATEGORY_DIR:
		case CATEGORY_ITEM:
			long newCategoryId = db.insert("Category", null, values);
			uriReturn = Uri.parse("content://" + AUTHORITY + "/category/" + newCategoryId);
			break;
		default:
			break;
		}
		return uriReturn;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int updatedRows = 0;
		switch (uriMatcher.match(uri)) {
		case BOOK_DIR:
			updatedRows = db.update("Book", values, selection, selectionArgs);
			break;
		case BOOK_ITEM:
			String bookId = uri.getPathSegments().get(1);
			updatedRows = db.update("Book", values, "id = ?", new String[] { bookId });
			break;
		case CATEGORY_DIR:
			updatedRows = db.update("Category", values, selection, selectionArgs);
			break;
		case CATEGORY_ITEM:
			String categoryId = uri.getPathSegments().get(1);
			updatedRows = db.update("Category", values, "id = ?", new String[] { categoryId });
			break;
		default:
			break;
		}
		return updatedRows;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int deletedRows = 0;
		switch (uriMatcher.match(uri)) {
		case BOOK_DIR:
			deletedRows = db.delete("Book", selection, selectionArgs);
			break;
		case BOOK_ITEM:
			String bookId = uri.getPathSegments().get(1);
			deletedRows = db.delete("Book", "id = ?", new String[] { bookId });
			break;
		case CATEGORY_DIR:
			deletedRows = db.delete("Category", selection, selectionArgs);
			break;
		case CATEGORY_ITEM:
			String categoryId = uri.getPathSegments().get(1);
			deletedRows = db.delete("Category", "id = ?", new String[] { categoryId });
			break;
		default:
			break;
		}
		return deletedRows;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case BOOK_DIR:
			return "vnd.android.cursor.dir/vnd.cn.edu.hznu.sqlitedbtest.provider.book";
		case BOOK_ITEM:
			return "vnd.android.cursor.item/vnd.cn.edu.hznu.sqlitedbtest.provider.book";
		case CATEGORY_DIR:
			return "vnd.android.cursor.dir/vnd.cn.edu.hznu.sqlitedbtest.provider.category";
		case CATEGORY_ITEM:
			return "vnd.android.cursor.item/vnd.cn.edu.hznu.sqlitedbtest.provider.category";
		}
		return null;
	}

}
