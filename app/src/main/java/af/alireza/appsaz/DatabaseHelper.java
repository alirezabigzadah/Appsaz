package af.alireza.appsaz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
	public static final String DATABASE_NAME = "list";
	public static final String TABLE_NAME = "APPSAZ";
	public static final String ID = "ID";
	public static final String TEXT = "TEXT";
	public static final String DESCRIPTION = "DESCRIPTION";
	public static final String FONT = "FONT";
	public static final String RICON = "RICON";
	public static final String LICON = "LICON";
	public static final String BACKGROUND = "BACKGROUND";
	public static final String RADIOS = "RADIOS";
	public static final String TEXTCOLOR = "TEXTCOLOR";
	public static final String TEXTSIZE = "TEXTSIZE";
	public static final String ANIMATION = "ANIMATION";
	public static final String ONCLICK = "ONCLICK";
	public static final String DATABASE_PATH = Applications.getInstance().getExternalFilesDir(null).getPath() + "/Project/MyApp/assets/";
	private String             DATABASE_FULL_PATH;
	private String             dbId;
	private Context            mycontext;
	public SQLiteDatabase      db;

	public DatabaseHelper(Context context, String dbName) {
		super(context, DATABASE_NAME, null, 1);
		this.mycontext = context;
		dbId = "menu" + dbName;
		DATABASE_FULL_PATH = DATABASE_PATH + dbName;
	}

	public void onCreate(SQLiteDatabase arg0) {}

	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {}

	public void open() {
		db = SQLiteDatabase.openDatabase(DATABASE_FULL_PATH, null, 0);
	}

	public void close() {
		db.close();
	}

	public boolean checkdb() {
		SQLiteDatabase db = null;
		try {
			db = SQLiteDatabase.openDatabase(DATABASE_FULL_PATH, null, 1);
		} catch (SQLException e) {
		}
		if (db != null) {
			return true;
		}
		return false;
	}

	public boolean insertData(
		String text,
		String desc,
		String font,
		String rIcon,
		String lIcon,
		String background,
		String radios,
		String textColor,
		String textSize,
		String animation,
		String onclick) {

		ContentValues cv = new ContentValues();
		cv.put(TEXT, text);
		cv.put(DESCRIPTION, desc);
		cv.put(FONT, font);
		cv.put(RICON, rIcon);
		cv.put(LICON, lIcon);
		cv.put(BACKGROUND, background);
		cv.put(RADIOS, radios);
		cv.put(TEXTCOLOR, textColor);
		cv.put(TEXTSIZE, textSize);
		cv.put(ANIMATION, animation);
		cv.put(ONCLICK, onclick);

		long result = db.insert(TABLE_NAME, null, cv);

		if (result == -1) return false;
		else return true;
	}

	public Cursor getData(String id) {
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "SELECT * FROM " + TABLE_NAME + " WHERE ID='" + id + "'";
		Cursor cursor = db.rawQuery(query, null);
		return cursor;
	}

	public boolean updateData(
		String id,
		String text,
		String desc,
		String font,
		String rIcon,
		String lIcon,
		String background,
		String radios,
		String textColor,
		String textSize,
		String animation,
		String onclick) {

		ContentValues cv = new ContentValues();
		cv.put(ID, id);
		cv.put(TEXT, text);
		cv.put(DESCRIPTION, desc);
		cv.put(FONT, font);
		cv.put(RICON, rIcon);
		cv.put(LICON, lIcon);
		cv.put(BACKGROUND, background);
		cv.put(RADIOS, radios);
		cv.put(TEXTCOLOR, textColor);
		cv.put(TEXTSIZE, textSize);
		cv.put(ANIMATION, animation);
		cv.put(ONCLICK, onclick);

		db.update(TABLE_NAME, cv, "ID=?", new String[] {id});

		return true;
	}

	public Integer deleteData(String id) {

		return db.delete(TABLE_NAME, "ID = ?", new String[] {id});
	}

	public Cursor getAllData() {

		Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
		return res;
	}

	public boolean updateThisList(
		String bg, String actionBar, String actionBarTitle, String actionBarColor) {
		ContentValues cv = new ContentValues();
		cv.put("Background", bg);
		cv.put("ActionBar", actionBar);
		cv.put("ActionBarTitle", actionBarTitle);
		cv.put("ActionBarColor", actionBarColor);

		db.update("THIS_LIST", cv, "ID=?", new String[] {"1"});

		return true;
	}

	public boolean updateMenu(String mood, String name) {
		ContentValues cv = new ContentValues();
		cv.put("Mood", mood);
		cv.put("Name", name);

		db.update("MENU", cv, "ID=?", new String[] {"1"});

		return true;
	}

	public boolean exists(String table) {
		try {
			Cursor c = db.rawQuery("SELECT Name FROM MENU", null);
			c.moveToFirst();
			while (!c.isAfterLast()) {
				c.moveToNext();
			}
			c.close();
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	public boolean addUpdates() {
		boolean Return = false;
		if (exists("")) {
			Return = false;
		} else {
			db.execSQL("CREATE TABLE IF NOT EXISTS MENU(ID INTEGER, Mood TEXT default Off, Name TEXT)");
			ContentValues cv = new ContentValues();
			cv.put("Mood", "Off");
			cv.put("Name", dbId);
			cv.put("ID", "1");
			db.insert("MENU", null, cv);
			Return = true;
		}
		return Return;
	}

	public String getBG() {
		String str = null;
		Cursor c = db.rawQuery("SELECT Background FROM THIS_LIST", null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			str = (c.getString(0));
			c.moveToNext();
		}
		c.close();
		return str;
	}

	public String getMenuMood() {
		String str = null;
		Cursor c = db.rawQuery("SELECT Mood FROM MENU", null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			str = (c.getString(0));
			c.moveToNext();
		}
		c.close();
		return str;
	}

	public String getMenuName() {
		String str = null;
		Cursor c = db.rawQuery("SELECT Name FROM MENU", null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			str = (c.getString(0));
			c.moveToNext();
		}
		c.close();
		return str;
	}

	public String getActionBar() {
		String str = null;
		Cursor c = db.rawQuery("SELECT ActionBar FROM THIS_LIST", null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			str = (c.getString(0));
			c.moveToNext();
		}
		c.close();
		return str;
	}

	public String getActionBarTitle() {
		String str = null;
		Cursor c = db.rawQuery("SELECT ActionBarTitle FROM THIS_LIST", null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			str = (c.getString(0));
			c.moveToNext();
		}
		c.close();
		return str;
	}

	public String getActionBarColor() {
		String str = null;
		Cursor c = db.rawQuery("SELECT ActionBarColor FROM THIS_LIST", null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			str = (c.getString(0));
			c.moveToNext();
		}
		c.close();
		return str;
	}

	public ArrayList<String> search(int col, String word) {

		Cursor cursor;
		ArrayList<String> list = new ArrayList();
		cursor =
			db.rawQuery(
            "SELECT * FROM "
			+ TABLE_NAME
			+ " WHERE TEXT LIKE '%"
			+ word
			+ "%' or DESCRIPTION LIKE '%"
			+ word
			+ "%'",
            null);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				list.add(cursor.getString(col));
			}
		}

		return list;
	}
}
