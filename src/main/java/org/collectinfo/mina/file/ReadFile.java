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
    private final JsonUtil jsonUtil=new JsonUtil();
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
    public List<Map<String,List<LinkedHashMap<String,String>>>> readFile(){
         List<Map<String,List<LinkedHashMap<String,String>>>> list=new ArrayList<Map<String, List<LinkedHashMap<String, String>>>>();
         //Map<ip,SystemInfo>
        List<String> strList=new ArrayList<String>();
        try{
        FileReader file=new FileReader(baseDir+ StringUtil.formatDate(new Date())+".txt");
        BufferedReader buf=new BufferedReader(file);
        String line=null;
        while ((line=buf.readLine())!=null){
        strList.add(line);
        }
        buf.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        Set<String> keySet=new HashSet<String>();
        for(String s:strList){
        Map<String,LinkedHashMap<String,String>> map=transferString(s);
        for(Map.Entry e:map.entrySet()){
            keySet.add(e.getKey().toString());
        }
        }
        List<String> strArray=new ArrayList<String>(keySet);
        for(int i=0;i<strArray.size();i++){
            Map<String,List<LinkedHashMap<String,String>>> m=new HashMap<String, List<LinkedHashMap<String, String>>>();
            List<LinkedHashMap<String,String>> s1=new ArrayList<LinkedHashMap<String, String>>();
            String key=strArray.get(i);
            for(String s:strList){
            Map<String,LinkedHashMap<String,String>> map=transferString(s);
            if(map.get(key)!=null){
            LinkedHashMap<String,String> ss=map.get(key);
            s1.add(ss);
            }
            }
            m.put(key,s1);
            list.add(m);
        }
        return list;
    }
    public Map<String,LinkedHashMap<String,String>> transferString(String str){
        Map<String,LinkedHashMap<String,String>> m=jsonUtil.fromJson(str,HashMap.class);
        return m;
    }
    public static void main(String[] args){
        ReadFile readFile=new ReadFile();
        System.out.println(readFile.readFile());
    }
}
