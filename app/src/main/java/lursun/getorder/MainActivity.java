package lursun.getorder;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.midi.MidiOutputPort;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;

public class MainActivity extends AppCompatActivity {
    static int page=0;
    static String date="";
    LayoutInflater inflater;

    public void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //檔存在時
                InputStream inStream = new FileInputStream(oldPath);//讀入原檔
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ( (byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //位元組數 檔案大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        }
        catch(Exception e) {
            System.out.println("複製單個檔操作出錯");
            e.printStackTrace();


        }


    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            LinearLayout layout=(LinearLayout)inflater.inflate(R.layout.detailed,null);

            SQLite sqLite=new SQLite(getApplicationContext());



            final SQLiteDatabase db=sqLite.getReadableDatabase();
            final Cursor c=db.rawQuery("Select * from order_history  JOIN product_history USING ( date,time,id) Where id='"+(String) msg.obj+"'" ,null);
            if(c.moveToFirst()) {
                ((TextView) layout.findViewById(R.id.date)).setText(c.getString(c.getColumnIndex("date")));
                ((TextView) layout.findViewById(R.id.time)).setText(c.getString(c.getColumnIndex("time")));
                ((TextView) layout.findViewById(R.id.id)).setText(c.getString(c.getColumnIndex("id")));
                ((TextView) layout.findViewById(R.id.buyerphone)).setText(c.getString(c.getColumnIndex("buyerphone")));
                ((TextView) layout.findViewById(R.id.sumtotal)).setText(c.getString(c.getColumnIndex("sumtotal")));
                Button status_Button = null;
                switch (c.getString(c.getColumnIndex("status"))) {
                    case "0":
                        status_Button = (Button) layout.findViewById(R.id.noactive);
                        break;
                    case "1":
                        status_Button = (Button) layout.findViewById(R.id.active);
                        break;
                    case "2":
                        //AlertDialog ad=new AlertDialog.Builder(getApplicationContext()).setTitle("注意").setMessage("確定製作完成").setPositiveButton("確定",null).setNegativeButton("取消",null).create();
                        //ad.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                        status_Button = (Button) layout.findViewById(R.id.makefinish);
                        break;
                    case "3":
                        status_Button = (Button) layout.findViewById(R.id.getfood);
                        break;
                }
                switch (c.getString(c.getColumnIndex("status"))) {
                    case "0":
                        ((Button) layout.findViewById(R.id.active)).setEnabled(true);
                        ((Button) layout.findViewById(R.id.active)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Cursor self =db.rawQuery("Select * From setting " ,null);
                                self.moveToFirst();

                                new Module().updatestatus(self.getString(self.getColumnIndex("site")),self.getString(self.getColumnIndex("shopID")),c.getString(c.getColumnIndex("id")),date,"1");
                            }
                        });
                    case "1":
                        ((Button) layout.findViewById(R.id.makefinish)).setEnabled(true);
                        ((Button) layout.findViewById(R.id.makefinish)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Cursor self =db.rawQuery("Select * From setting " ,null);
                                self.moveToFirst();
                                new Module().updatestatus(self.getString(self.getColumnIndex("site")),self.getString(self.getColumnIndex("shopID")),c.getString(c.getColumnIndex("id")),date,"2");
                                new Module().notice(self.getString(self.getColumnIndex("site")),self.getString(self.getColumnIndex("shopID")),c.getString(c.getColumnIndex("id")),date);
                                AlertDialog ad=new AlertDialog.Builder(getApplicationContext()).setTitle("注意").setMessage("確定製作完成").setPositiveButton("確定",null).setNegativeButton("取消",null).create();
                                ad.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                                ad.show();
                            }
                        });
                        break;
                    case "2":
                        Cursor self =db.rawQuery("Select * From setting " ,null);
                        self.moveToFirst();
                        new Module().updatestatus(self.getString(self.getColumnIndex("site")),self.getString(self.getColumnIndex("shopID")),c.getString(c.getColumnIndex("id")),date,"3");
                        AlertDialog ad=new AlertDialog.Builder(getApplicationContext()).setTitle("注意").setMessage("確定製作完成").setPositiveButton("確定",null).setNegativeButton("取消",null).create();
                        ((Button) layout.findViewById(R.id.getfood)).setVisibility(View.VISIBLE);
                        ((Button) layout.findViewById(R.id.getfood)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });

                }
                status_Button.setBackgroundColor(Color.YELLOW);


                do {
                    LinearLayout product = (LinearLayout) inflater.inflate(R.layout.tep_product, null);
                    ((TextView) product.findViewById(R.id.name)).setText(c.getString(c.getColumnIndex("name")));
                    ((TextView) product.findViewById(R.id.qnt)).setText(c.getString(c.getColumnIndex("qnt")));
                    ((TextView) product.findViewById(R.id.uniprice)).setText(c.getString(c.getColumnIndex("uniprice")));
                    ((TextView) product.findViewById(R.id.detail)).setText(c.getString(c.getColumnIndex("detail")));
                    ((LinearLayout)layout.findViewById(R.id.list)).addView(product);
                } while (c.moveToNext());




            AlertDialog ad=new AlertDialog.Builder(getApplicationContext()).setPositiveButton("回列表",null).create();
            ad.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            ad.setView(layout);
            ad.show();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        date=date.equals("")?sdf.format(new Date()):date;
        inflater=getLayoutInflater();
        ((TextView)findViewById(R.id.choosedate)).setText(date);
        findViewById(R.id.choosedate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatePicker dp=new DatePicker(getApplicationContext());
                AlertDialog ad=new AlertDialog.Builder(getApplicationContext()).setPositiveButton("確定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int month=dp.getMonth()+1;
                                int day=dp.getDayOfMonth();
                                int year=dp.getYear();
                                date=String.format("%s-%s-%s",year,month,day);
                                Intent intent=new Intent();
                                intent.setClass(getApplicationContext(),MainActivity.class);
                                MainActivity.this.startActivity(intent);
                                MainActivity.this.finish();
                            }
                        }
                ).create();
                ad.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                ad.setView(dp);
                ad.show();
            }
        });


        //copyFile("/data/data/lursun.getorder/databases/SQLite.db","/sdcard/sqlite.db");
        //LinearLayout layout=(LinearLayout)inflater.inflate(R.layout.detailed,null);
        //AlertDialog ad=new AlertDialog.Builder(this,R.style.dialog).setPositiveButton("回列表",null).create();
        //ad.setView(layout);
        //ad.show();
        new Module().info_active(this,"<info>\n" +
                "\t<order>\n" +
                "\t\t<id>000</id>\n" +
                "\t\t<date>2016-12-21</date>\n" +
                "\t\t<time>17:26:59</time>\n" +
                "\t\t<buyerphone>0909515521</buyerphone>\n" +
                "\t\t<sumtotal>1</sumtotal>\n" +
                "\t\t<status>0</status>\n" +
                "\t\t<product>\n" +
                "\t\t\t<name>測試</name>\n" +
                "\t\t\t<detail>只是測試</detail>\n" +
                "\t\t\t<qnt>1</qnt>\n" +
                "\t\t\t<uniprice>1</uniprice>\n" +
                "\t\t\t<price>1</price>\n" +
                "\t\t</product>\n" +
                "\t</order>\n" +
                "\t<order>\n" +
                "\t\t<id>001</id>\n" +
                "\t\t<date>2016-12-21</date>\n" +
                "\t\t<time>17:27:30</time>\n" +
                "\t\t<buyerphone>0909515521</buyerphone>\n" +
                "\t\t<sumtotal>200</sumtotal>\n" +
                "\t\t<status>0</status>\n" +
                "\t\t<product>\n" +
                "\t\t\t<name>測試2</name>\n" +
                "\t\t\t<detail>只是測試2</detail>\n" +
                "\t\t\t<qnt>1</qnt>\n" +
                "\t\t\t<uniprice>100</uniprice>\n" +
                "\t\t\t<price>100</price>\n" +
                "\t\t</product>\n" +
                "\t\t<product>\n" +
                "\t\t\t<name>測試3</name>\n" +
                "\t\t\t<detail>只是測試3</detail>\n" +
                "\t\t\t<qnt>2</qnt>\n" +
                "\t\t\t<uniprice>50</uniprice>\n" +
                "\t\t\t<price>100</price>\n" +
                "\t\t</product>\n" +
                "\t</order>\n" +
                "</info>");
            showList();
    }
    void showList(){
        SQLite sqLite=new SQLite(this);
        SQLiteDatabase db=sqLite.getReadableDatabase();
        ((LinearLayout)findViewById(R.id.list)).removeAllViews();
        Cursor c=db.rawQuery("Select * from order_history Where date='"+date+"' limit "+page+",5",null);

        c.moveToFirst();
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(0,0,0,0);
        View.OnClickListener click=new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Message msg=new Message();
                msg.obj=view.getTag();
                handler.sendMessage(msg);
            }
        };
        for(int i=0;i<c.getCount();i++) {
            LinearLayout insert=(LinearLayout)inflater.inflate(R.layout.tep_order, null);
            ((TextView)insert.findViewById(R.id.id)).setText(c.getString(c.getColumnIndex("id")));
            ((TextView)insert.findViewById(R.id.time)).setText(c.getString(c.getColumnIndex("time")));
            ((TextView)insert.findViewById(R.id.sumtotal)).setText(c.getString(c.getColumnIndex("sumtotal")));

            switch (c.getString(c.getColumnIndex("status")))
            {
                case "0":
                    ((TextView)insert.findViewById(R.id.status)).setText("未處理");
                    break;
                case "1":
                    ((TextView)insert.findViewById(R.id.status)).setText("處理中");
                    break;
                case "2":
                    ((TextView)insert.findViewById(R.id.status)).setText("製作完成");
                    break;
                case "3":
                    ((TextView)insert.findViewById(R.id.status)).setText("已取餐");
                    ((TextView)insert.findViewById(R.id.id)).setTextColor(Color.WHITE);
                    ((TextView)insert.findViewById(R.id.time)).setTextColor(Color.WHITE);
                    ((TextView)insert.findViewById(R.id.sumtotal)).setTextColor(Color.WHITE);
                    ((TextView)insert.findViewById(R.id.status)).setTextColor(Color.WHITE);
                    break;
            }

            switch (c.getString(c.getColumnIndex("status"))){
                case "0":
                case "1":
                case "2":

                    insert.setOnClickListener(click);
            }
            ((LinearLayout)findViewById(R.id.list)).addView(insert);
            insert.setLayoutParams(lp);
            insert.setTag(c.getString(c.getColumnIndex("id")));
            c.moveToNext();
        }

    }
}
