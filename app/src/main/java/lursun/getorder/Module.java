package lursun.getorder;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by admin on 2016/12/21.
 */
public class Module {
    Pattern pStatus=Pattern.compile("(?<=<status>)[\\w\\W]+?(?=</status>)");
    Pattern pOrder=Pattern.compile("(?<=<order>)[\\w\\W]+?(?=</order>)");
    Pattern pId=Pattern.compile("(?<=<id>)[\\w\\W]+?(?=</id>)");
    Pattern pDate=Pattern.compile("(?<=<date>)[\\w\\W]+?(?=</date>)");
    Pattern pTime=Pattern.compile("(?<=<time>)[\\w\\W]+?(?=</time>)");
    Pattern pBuyerphone=Pattern.compile("(?<=<buyerphone>)[\\w\\W]+?(?=</buyerphone>)");
    Pattern pSumtotal=Pattern.compile("(?<=<sumtotal>)[\\w\\W]+?(?=</sumtotal>)");
    Pattern pProduct=Pattern.compile("(?<=<product>)[\\w\\W]+?(?=</product>)");
    Pattern pName=Pattern.compile("(?<=<name>)[\\w\\W]+?(?=</name>)");
    Pattern pDetail=Pattern.compile("(?<=<detail>)[\\w\\W]+?(?=</detail>)");
    Pattern pQnt=Pattern.compile("(?<=<qnt>)[\\w\\W]+?(?=</qnt>)");
    Pattern pUniprice=Pattern.compile("(?<=<uniprice>)[\\w\\W]+?(?=</uniprice>)");
    Pattern pPrice=Pattern.compile("(?<=<price>)[\\w\\W]+?(?=</price>)");
    public void info_active(Context context, String input){
        Matcher mOrder=pOrder.matcher(input);
        SQLite sqLite=new SQLite(context);
        SQLiteDatabase db=sqLite.getReadableDatabase();
        while (mOrder.find()) {
            String order=mOrder.group();
            String status="",id="",date="",time="",buyerphone="",sumtotal="",product="";
            Matcher mStatus=pStatus.matcher(order);

            if(mStatus.find()&&mStatus.group().equals("0")) {
                status = mStatus.group();
                Matcher mId = pId.matcher(order);

                if (mId.find()) {
                    id = mId.group();
                    if(db.rawQuery("Select * From order_history Where id='"+id+"'",null).getCount()>0)continue;
                }
                Matcher mDate = pDate.matcher(order);
                if (mDate.find()) date = mDate.group();
                Matcher mTime = pTime.matcher(order);
                if (mTime.find()) time = mTime.group();
                Matcher mBuyerphone = pBuyerphone.matcher(order);
                if (mBuyerphone.find()) buyerphone = mBuyerphone.group();
                Matcher mSumtotal = pSumtotal.matcher(order);
                if (mSumtotal.find()) sumtotal = mSumtotal.group();
                Matcher mProduct = pProduct.matcher(order);
                ContentValues ordercv=new ContentValues();
                ordercv.put("status",status);
                ordercv.put("id",id);
                ordercv.put("date",date);
                ordercv.put("time",time);
                ordercv.put("buyerphone",buyerphone);
                ordercv.put("sumtotal",sumtotal);
                db.insert("order_history",null,ordercv);
                //db.execSQL(String.format( "Insert Into order_history(status,id,date,time,buyerphone,sumtotal) Values('%s','%s','%s','%s','%s','%s')",status,id,date,time,buyerphone,sumtotal));
                while (mProduct.find()) {
                    product = mProduct.group();
                    String name = "", detail = "", qnt = "", uniprice = "", price = "";
                    Matcher mName = pName.matcher(product);
                    if (mName.find()) name = mName.group();
                    Matcher mdetail = pDetail.matcher(product);
                    if (mdetail.find()) detail = mdetail.group();
                    Matcher mQnt = pQnt.matcher(product);
                    if (mQnt.find()) qnt = mQnt.group();
                    Matcher mUniprice = pUniprice.matcher(product);
                    if (mUniprice.find()) uniprice = mUniprice.group();
                    Matcher mPrice = pPrice.matcher(product);
                    if (mPrice.find()) price = mPrice.group();
                    ContentValues productcv=new ContentValues();
                    productcv.put("id",id);
                    productcv.put("date",date);
                    productcv.put("time",time);
                    productcv.put("name",name);
                    productcv.put("detail",detail);
                    productcv.put("qnt",qnt);
                    productcv.put("uniprice",uniprice);
                    productcv.put("price",price);
                    db.insert("product_history",null,productcv);
                }
            }
        }
        db.close();
        sqLite.close();
    }
    public void notice(String site,String shopID,String orderID,String date){
        final StringBuffer stringBuffer = new StringBuffer();
        final String  url = String.format("%s/notice?shopID=%s&orderID=%s&date=%s",site,shopID,date);
        Thread thread=new Thread(){
            @Override
            public void run() {
                try{
                    URL obj = new URL(url);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                    if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader br= new BufferedReader(new InputStreamReader( con.getInputStream()));

                        String tempStr;
                        while( ( tempStr = br.readLine() ) != null ) {
                            stringBuffer.append( tempStr );
                        }
                    }
                }catch (Exception e){
                    e=e;
                }
            }
        };
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e=e;
        }
        //////////////////////
        if(stringBuffer.toString().equals("ok"))
        {

        }
        /////////////////////
    };
    public void updatestatus(String site,String shopID,String orderID,String date,String status){
        final StringBuffer stringBuffer = new StringBuffer();
        final String  url = String.format("%s/updatestatus?shopID=%s&orderID=%s&date=%s&status=%s",site,shopID,orderID,date,status);
        Thread thread=new Thread(){
            @Override
            public void run() {
                try{
                    URL obj = new URL(url);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                    if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader br= new BufferedReader(new InputStreamReader( con.getInputStream()));

                        String tempStr;
                        while( ( tempStr = br.readLine() ) != null ) {
                            stringBuffer.append( tempStr );
                        }
                    }
                }catch (Exception e){
                    e=e;
                }
            }
        };
        thread.start();
        try{
            thread.join();
        }catch (Exception e){
            e=e;
        }
        //////////////////////
        if(stringBuffer.toString().equals("ok"))
        {

        }
        /////////////////////
    };
    public String getInfo(String site,String shopID,String date,String status){
        try {
            final StringBuffer stringBuffer = new StringBuffer();
            final String  url = String.format("%s/orderlist?shopID=%s&date=%s&status=%s",site,shopID,date,status);
            Thread thread=new Thread(){
                @Override
                public void run() {
                    try{
                        URL obj = new URL(url);
                        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                        if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            BufferedReader br= new BufferedReader(new InputStreamReader( con.getInputStream()));

                            String tempStr;
                            while( ( tempStr = br.readLine() ) != null ) {
                                stringBuffer.append( tempStr );
                            }
                        }
                    }catch (Exception e){
                        e=e;
                    }
                }
            };
            thread.start();
            try{
                thread.join();
            }catch (Exception e){
                e=e;
            }
            return  stringBuffer.toString();
        }catch (Exception e){
            e=e;
        }
        return "error";
    }
}
