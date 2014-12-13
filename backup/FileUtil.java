package org.collectinfo.mina.file;

import org.collectinfo.mina.config.ConfigParser;
import org.collectinfo.util.StringUtil;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: hadoop
 * Date: 14-12-9
 * Time: 下午2:00
 * To change this template use File | Settings | File Templates.
 */
public class FileUtil {
    //读取文件
    private ConfigParser configParser=new ConfigParser();
    private Calendar calendar=Calendar.getInstance();
    private PrintStream printStream;
    private String filePath=null;
    private String baseDirPath=configParser.getPros().get("filePath")+"//";
    public void setPrintStream(PrintStream printStream) {
        synchronized (this){
            this.printStream = printStream;
        }
    }
    public void writeFile(String str){
        checkDate();
        synchronized (this){
            if(printStream==null){
                try{
                printStream=new PrintStream(new FileOutputStream(baseDirPath+ StringUtil.formatDate(new Date())+".txt"));
                }catch (Exception e){
                    e.printStackTrace();
                }
                }
            printStream.append(str+"\r\n");
        }
    }
    protected void checkDate(){
        Calendar c=Calendar.getInstance();
        synchronized (this){
            if(calendar.get(Calendar.DATE)!=c.get(Calendar.DATE)){
                try{
                 calendar=c;
                 filePath=baseDirPath+StringUtil.formatDate(new Date())+".txt";
                 setPrintStream(new PrintStream(new FileOutputStream(filePath)));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args){
        FileUtil fileUtil=new FileUtil();
        for(int i=0;i<1000000000;i++){
            fileUtil.writeFile("hello!"+i);
        }
    }
}
