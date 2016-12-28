package lursun.getorder;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by admin on 2016/9/5.
 */
public class SQLite extends SQLiteOpenHelper {
    public SQLiteDatabase db=null;
    private final static int _DBVersion = 1;
    private final static String _DBName = "SQLite.db";

    public SQLite(Context context) {
        super(context, _DBName, null, _DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL = "CREATE TABLE IF NOT EXISTS setting( " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "site TEXT, " +
                "shopID TEXT" +
                ");";
        db.execSQL(SQL);
        SQL = "INSERT INTO setting(site,shopID)" +
                "VALUES('test','le00000115');";
        db.execSQL(SQL);
        SQL = "CREATE TABLE IF NOT EXISTS order_history( " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id TEXT,"+
                "date TEXT, " +
                "time TEXT," +
                "buyerphone TEXT,"+
                "sumtotal TEXT,"+
                "status TEXT"+
                ");";
        db.execSQL(SQL);
        SQL = "CREATE TABLE IF NOT EXISTS product_history( " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id TEXT,"+
                "date TEXT, " +
                "time TEXT," +
                "name TEXT,"+
                "detail TEXT,"+
                "qnt TEXT,"+
                "uniprice TEXT,"+
                "price TEXT"+
                ");";
        db.execSQL(SQL);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String SQL = "DROP TABLE setting";
        db.execSQL(SQL);
        SQL = "DROP TABLE order_history";
        db.execSQL(SQL);
        SQL = "DROP TABLE product_history";
        db.execSQL(SQL);
        onCreate(db);
    }


}
