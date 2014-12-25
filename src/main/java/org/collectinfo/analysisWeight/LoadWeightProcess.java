package org.collectinfo.analysisWeight;

/*import org.collectinfo.matlab.MatLabProcess;*/
import org.collectinfo.matlab.PredictParameter;
import org.collectinfo.nginx.NginxParameter;
import org.collectinfo.redis.RedisConnector;
import org.collectinfo.util.StringUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: hadoop
 * Date: 14-12-16
 * Time: 下午4:31
 * To change this template use File | Settings | File Templates.
 */
public class LoadWeightProcess {
    private String nginxConf= NginxParameter.NginxHome+NginxParameter.NginxConfFile;
    /*private MatLabProcess matLabProcess=new MatLabProcess();*/
    private RedisConnector redisConnector=new RedisConnector();
    public Map<String,Double> getLoadWeight(PredictParameter predictParameter,Map<String,Map<String,String>> systemInfoMap){    //得到负载权值
       Map<String,Double> map=new HashMap<String, Double>();
       for(Map.Entry e:systemInfoMap.entrySet()){
       String ipKey=e.getKey().toString();
       Map<String,String> systemInfo=(Map<String,String>)e.getValue();
       double loadWeight=0;
       loadWeight=predictParameter.getP1()*Double.parseDouble(systemInfo.get("cpu").toString())+predictParameter.getP2()*Double.parseDouble(systemInfo.get("mem").toString())
               +predictParameter.getP3()*Double.parseDouble(systemInfo.get("net").toString())+predictParameter.getP3()*Double.parseDouble(systemInfo.get("thread").toString());
       map.put(ipKey,loadWeight);
       }
       return map;
    }
    /*
    * List<Map<String,Double>> loadWeightMapList
    */
    public Map<String,Map<String,String>> getCurrentSystemInfoWithIPMapping(String ipKey){ //得到当前系统的负载情况
         String value=redisConnector.get(ipKey);
         String[] str= StringUtil.parseStringByToken(value," ");
         Map<String,String> map=new HashMap<String, String>();
         map.put("cpu",str[0]);
         map.put("mem",str[1]);
         map.put("net",str[2]);
         map.put("thread",str[3]);
         Map<String,Map<String,String>> currMap=new HashMap<String, Map<String, String>>();
         currMap.put(ipKey,map);
         return currMap;
    }
   /* public List<Map<String,Double>> getIPLoadWeightMapping(){
        List<Map<String,PredictParameter>> list=matLabProcess.getIPParaMapping();
        List<Map<String,Double>> weightIPs=new ArrayList<Map<String, Double>>();
        for(Map<String,PredictParameter> map:list){
            for(Map.Entry e:map.entrySet()){
                String ipKey=e.getKey().toString();
                PredictParameter parameter=(PredictParameter)e.getValue();
                Map<String,Map<String,String>> currentSystemInfo=getCurrentSystemInfoWithIPMapping(ipKey);
                Map<String,Double> weightIP=getLoadWeight(parameter,currentSystemInfo);
                weightIPs.add(weightIP);
            }
        }
        return  weightIPs;
    }*/
    public boolean updateNginxLog(){
       //首先停止nginx服务器,然后更新配置文件,然后启动nginx
        try{
        FileReader f=new FileReader(nginxConf);
        BufferedReader buf=new BufferedReader(f);
        String line=null;
        List<String> list=new ArrayList<String>();
        while((line=buf.readLine())!=null){
             list.add(line);
        }
        buf.close();
        for(String s:list){

        }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public static void main(String[] args){
        LoadWeightProcess loadWeightProcess=new LoadWeightProcess();
        loadWeightProcess.updateNginxLog();
    }
}
