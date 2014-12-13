package org.collectinfo.mina.file;

import org.collectinfo.mina.config.ConfigParser;
import org.collectinfo.util.JsonUtil;
import org.collectinfo.util.StringUtil;

import java.io.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: hadoop
 * Date: 14-12-9
 * Time: 下午9:26
 * To change this template use File | Settings | File Templates.
 */
public class ReadFile {
    private ConfigParser configParser=new ConfigParser();
    private final int HOUR=Integer.parseInt(configParser.getPros().get("HOUR").toString());
    private final int MINUTE=Integer.parseInt(configParser.getPros().get("MINUTE").toString());
    private final int SECOND=Integer.parseInt(configParser.getPros().get("SECOND").toString());
    private final int delay=Integer.parseInt(configParser.getPros().get("delay").toString());
    private final String baseDir=configParser.getPros().get("filePath").toString()+"//";
    public void ReadFileTask(){
        Calendar cal=Calendar.getInstance();
        //每天定点执行
        cal.set(Calendar.HOUR_OF_DAY,HOUR);     //控制时
        cal.set(Calendar.MINUTE,MINUTE);           //控制分
        cal.set(Calendar.SECOND,SECOND);           //控制秒
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
              //读文件并处理 hadoop可能处理
            }                           //cal.getTime()为得出执行任务的时间,为今天的12:00:00
        },cal.getTime(),delay);
    }
    public static void main(String[] args){

    }
}
