package org.collectinfo.matlab;

import ProcessFunc.ProcessFunc;
import org.collectinfo.fileProcess.FileProcess;
import org.collectinfo.mina.config.ConfigParser;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: hadoop
 * Date: 14-12-16
 * Time: 下午3:55
 * To change this template use File | Settings | File Templates.
 */
public class MatLabProcess {
   /*  //对合并的数据样本进行处理,得到预测模型
     private FileProcess fileProcess=new FileProcess();
     private ConfigParser configParser=new ConfigParser();
     private String mergeFileDir= configParser.getPros().get("mergeFileDir").toString();
     private ProcessFunc processFunc=new ProcessFunc();
    public PredictParameter getPredictModelFromDataSample(String ipKey){ //从数据样本通过matlab得到预测模型
        PredictParameter predictParameter=new PredictParameter();
        Object[] result=null;
        try{
            result=processFunc.operation(4,mergeFileDir + ipKey + ".txt");
            predictParameter.setP1(Double.parseDouble(result[0].toString()));
            predictParameter.setP2(Double.parseDouble(result[1].toString()));
            predictParameter.setP3(Double.parseDouble(result[2].toString()));
            predictParameter.setP4(Double.parseDouble(result[3].toString()));
        }catch (Exception e){
            e.printStackTrace();
        }
        return predictParameter;
    }
    public List<Map<String,PredictParameter>>getIPParaMapping(){
         File file=new File(mergeFileDir);
         List<Map<String,PredictParameter>> list=new ArrayList<Map<String, PredictParameter>>();
         if(file.isDirectory()){
             File[] f=file.listFiles();
             for(int i=0;i<f.length;i++){
                 Map<String,PredictParameter> map=new HashMap<String, PredictParameter>();
                 String ipKey=f[i].getAbsolutePath();
                 PredictParameter parameter=getPredictModelFromDataSample(ipKey);
                 map.put(ipKey,parameter);
                 list.add(map);
             }
         }
        return list;
    }*/
}
