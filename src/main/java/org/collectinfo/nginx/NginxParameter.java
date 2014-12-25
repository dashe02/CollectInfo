package org.collectinfo.nginx;

import org.collectinfo.mina.config.ConfigParser;

/**
 * Created with IntelliJ IDEA.
 * User: hadoop
 * Date: 14-12-16
 * Time: 下午4:51
 * To change this template use File | Settings | File Templates.
 */
public class NginxParameter {
      private static ConfigParser configParser=new ConfigParser();
      public static String NginxHome=configParser.getPros().get("NginxHome").toString();
      public static String NginxAccessLog=configParser.getPros().get("NginxAccessLog").toString();
      public static String NginxConfFile=configParser.getPros().get("NginxConfFile").toString();
}
