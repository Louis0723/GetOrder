package lursun.getorder;


import android.app.AlertDialog;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class BootService extends Service {
	private static Context context=null;
	public static boolean lock=false;
	private static WindowManager windowManager;
	private static RelativeLayout moneyView ,onoffView ;
	private static WindowManager.LayoutParams onoffParms;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}
	public void  handleStart(){
		final SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		Thread loopthread = new Thread() {
			@Override
			public void run() {
				while (true) {
					SQLite sqLite=new SQLite(getApplicationContext());
					SQLiteDatabase db=sqLite.getReadableDatabase();
					Cursor c=db.rawQuery("Select * From setting",null);
					c.moveToFirst();
					String site=c.getString(c.getColumnIndex("site"));
					String shopID=c.getString(c.getColumnIndex("shopID"));
					String date=sdf.format(new Date());
					Module module=new Module();
					String ret=module.getInfo(site,shopID,date,"0");
					try {
						Thread.sleep(10000);
					} catch (Exception e) {
						e = e;
					}

					if (false) break;
				}
			}
		};
		loopthread.start();
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		handleStart();
		return super.onStartCommand(intent, flags, startId);
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
