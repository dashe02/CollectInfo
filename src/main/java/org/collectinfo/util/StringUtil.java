package org.collectinfo.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: hadoop
 * Date: 14-12-11
 * Time: 下午6:48
 * To change this template use File | Settings | File Templates.
 */
public class StringUtil {
    public static String formatDate(Date d){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(d);
    }
    public static String[] parseStringByToken(String s,String token){
         String[] strings=null;
         if(s!=null){
          strings=s.split(token);
         }
         return strings;
    }
    public static Date StringToDate(String s){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date d=null;
        try{
            d=simpleDateFormat.parse(s);
        }catch (Exception e){
            e.printStackTrace();
        }
        return d;
    }
}
