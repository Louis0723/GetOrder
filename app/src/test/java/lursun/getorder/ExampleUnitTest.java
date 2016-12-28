package lursun.getorder;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        String input="xml 或html\n" +
                "<info>\n" +
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
                "</info>\n";
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
        Matcher mOrder=pOrder.matcher(input);
        while (mOrder.find()) {
            String order=mOrder.group();
            String id="",date="",time="",buyerphone="",sumtotal="",product="";
            Matcher mId=pId.matcher(order);
            if(mId.find())id=mId.group();
            Matcher mDate=pDate.matcher(order);
            if(mDate.find())date=mDate.group();
            Matcher mTime=pTime.matcher(order);
            if(mTime.find())time=mTime.group();
            Matcher mBuyerphone=pBuyerphone.matcher(order);
            if(mBuyerphone.find())buyerphone=mBuyerphone.group();
            Matcher mSumtotal=pSumtotal.matcher(order);
            if(mSumtotal.find())sumtotal=mSumtotal.group();
            Matcher mProduct=pProduct.matcher(order);
            while (mProduct.find()){
                product=mProduct.group();
                String name="",detail="",qnt="",uniprice="",price="";
                Matcher mName=pName.matcher(product);
                if(mName.find())name=mName.group();
                Matcher mdetail=pDetail.matcher(product);
                if(mdetail.find())detail=mdetail.group();
                Matcher mQnt=pQnt.matcher(product);
                if(mQnt.find())qnt=mQnt.group();
                Matcher mUniprice=pUniprice.matcher(product);
                if(mUniprice.find())uniprice=mUniprice.group();
                Matcher mPrice=pPrice.matcher(product);
                if(mPrice.find())price=mPrice.group();
                mPrice.group();
            }

        }
    }
}