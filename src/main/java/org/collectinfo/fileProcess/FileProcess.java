package org.collectinfo.fileProcess;

import org.collectinfo.mina.config.ConfigParser;
import org.collectinfo.nginx.NginxParameter;
import org.collectinfo.util.StringUtil;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: hadoop
 * Date: 14-12-14
 * Time: 下午4:24
 * To change this template use File | Settings | File Templates.
 */
public class FileProcess {
     //对文件进行处理
    private ConfigParser configParser=new ConfigParser();
    private String baseDir=configParser.getPros().get("filePath")+"//";
    private String mergeBaseDir=configParser.getPros().get("mergeFileDir")+"//";
    private String nginxHome= NginxParameter.NginxHome;
    private String nginxAccessLog=NginxParameter.NginxAccessLog;
    public List<Map<String,List<String>>> readLogFile(){
        List<Map<String,List<String>>> mList=new ArrayList<Map<String, List<String>>>();
        try{
        String nginxLogPath=nginxHome+nginxAccessLog;
        FileReader f=new FileReader(nginxLogPath);
        BufferedReader buf=new BufferedReader(f);
        String line=null;
        List<String> list=new ArrayList<String>();
        while((line=buf.readLine())!=null){
        list.add(line);
        }
        buf.close();
        List<Map<String,String>> mapList=new ArrayList<Map<String, String>>();
        for(String s:list){
        Map<String,String> map=parseString0(s);
        mapList.add(map);
        }
            Set<String> a=new HashSet<String>();
            for(Map<String,String> map:mapList){
            for(Map.Entry e:map.entrySet()){
                a.add(e.getKey().toString());
            }
            }
            List<String> l=new ArrayList<String>(a);
                Map<String,List<String>> listMap=new HashMap<String, List<String>>();
                for(String ss:l){
                List<String>l1=new ArrayList<String>();
                for(Map<String,String> map:mapList){
                for(Map.Entry entry:map.entrySet()){
                      if(entry.getKey().toString().equals(ss)){
                         l1.add(entry.getValue().toString());
                      }
                }
                }
                listMap.put(ss,l1);
                mList.add(listMap);
                }
        }catch (Exception e){
            e.printStackTrace();
        }
        return mList;
    }
    public Map<String,String> parseString(String s){
        Map<String,String> map=new HashMap<String, String>();
        String regex="^(\\S+).+\"(\\S+)\"$";
        Pattern p=Pattern.compile(regex);
        Matcher m=p.matcher(s);
        while(m.find()){
            map.put(m.group(1),m.group(2));
        }
        return map;
    }
    public Map<String,String>parseString0(String s){
        Map<String,String> map=new HashMap<String, String>();
        String regex = "^(\\S+ \\S+) \\S+ \\S+ \\S+ (\\S+ \\S+) (\\S+) \\S+ \\S+$";
        Pattern p=Pattern.compile(regex);
        Matcher m=p.matcher(s);
        boolean b=m.find();
        String Log_time=null;
        String ip=null;
        String accept_time=null;
        if(b){
            Log_time=m.group(1);
            accept_time=m.group(2);
            ip=m.group(3);
        }
        long seconds=(StringUtil.StringToDate(Log_time).getTime()-StringUtil.StringToDate(accept_time).getTime())/1000;
        map.put(StringUtil.parseStringByToken(ip,":")[0],String.valueOf(seconds));
        return map;
    }
    public void mergeFile(){
        List<Map<String,List<String>>> logFileMapList=readLogFile();
        List<Map<String,List<String>>> systemInfoMapList=new ArrayList<Map<String, List<String>>>();
        for(Map<String,List<String>> k:logFileMapList){
        Map<String,List<String>> systemInfoMap=new HashMap<String, List<String>>();
        for(Map.Entry e:k.entrySet()){
            File f=new File(baseDir+ StringUtil.formatDate(new Date())+"//"+e.getKey().toString()+".txt");
            if(f.exists()){
            try{
            FileReader file=new FileReader(baseDir+ StringUtil.formatDate(new Date())+"//"+e.getKey().toString()+".txt");
            BufferedReader buf=new BufferedReader(file);
            String line=null;
            List<String> systemInfoList=new ArrayList<String>();
            while((line=buf.readLine())!=null){
            systemInfoList.add(line);
            }
            buf.close();
            systemInfoMap.put(e.getKey().toString(),systemInfoList);
            systemInfoMapList.add(systemInfoMap);
            }catch (Exception e1){
                e1.printStackTrace();
            }
            }
        }
        // logFileMapList 日志文件读取的结果，systemInfoMapList 系统信息读取的结果
        mergeFile1(logFileMapList,systemInfoMapList);    //将文件合并并写入到指定的文件中
        }
    }
    public void mergeFile1(List<Map<String,List<String>>> logFileMapList,List<Map<String,List<String>>> systemInfoMapList){
          int logFileSize=logFileMapList.size();
          int systemInfoSize=systemInfoMapList.size();
          if(logFileSize==systemInfoSize){   //按照比较少的合并和计算
          for(Map<String,List<String>> logFileMap:logFileMapList){
          for(Map.Entry e:logFileMap.entrySet()){
              List<String> logFileInfo=(List)e.getValue();
              List<String> systemInfo=getSystemInfoByIPKey(systemInfoMapList,e.getKey().toString());
              writeToFile(e.getKey().toString(),logFileInfo,systemInfo);
          }
          }
          }else{
              System.out.println("日志文件收集的ip数和系统信息采集的ip数不同!");
          }
    }
    public  List<String> getSystemInfoByIPKey(List<Map<String,List<String>>> systemInfoMapList,String ipKey){
        List<String> list=new ArrayList<String>();
        for(Map<String,List<String>> systemInfoMap:systemInfoMapList){
             for(Map.Entry e:systemInfoMap.entrySet()){
                 if(e.getKey().toString().equals(ipKey)){
                     list=(List)e.getValue();
                 }
             }
         }
        return list;
    }
    public void writeToFile(String ipKey,List<String> logFileList,List<String> systemInfoList){
        File file=new File(mergeBaseDir+ipKey+".txt");
        try{
        FileWriter writer=new FileWriter(file);
        BufferedWriter fw=new BufferedWriter(writer);
        for(int i=0;i<logFileList.size();i++){
            fw.write(logFileList.get(i)+" "+systemInfoList.get(i));
            fw.newLine();
        }
        fw.close();
        writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args){
        String s="2014/12/20 16:31:09 [673] 124.207.100.146 0.0.0.0:8888 2014/12/20 16:30:09 121.42.136.211:8066 139 195";
        FileProcess fileProcess=new FileProcess();
        System.out.print(fileProcess.parseString0(s));
    }
}
