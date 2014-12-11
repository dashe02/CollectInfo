package org.collectinfo.mina.config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: hadoop
 * Date: 14-12-8
 * Time: 下午3:52
 * To change this template use File | Settings | File Templates.
 */
public class ConfigParser {
    //private static final String clientInfoPath="../conf/conf.properties";
    private static final String clientInfoPath="conf/conf.properties";
    public Properties getPros(){
       Properties pro=new Properties();
       try{
           InputStream in=new FileInputStream(clientInfoPath);
           pro.load(in);
       }catch (Exception e){
           e.printStackTrace();
       }
        return pro;
    }
    public static void main(String[] args){
        ConfigParser config=new ConfigParser();
        System.out.println(config.getPros());
    }
}
