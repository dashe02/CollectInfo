package org.collectinfo;

import org.collectinfo.mina.server.MinaTimeServer;

/**
 * Created with IntelliJ IDEA.
 * User: hadoop
 * Date: 14-12-9
 * Time: 下午10:30
 * To change this template use File | Settings | File Templates.
 */
public class StartCollect {
    public static void main(String[] args){
       new MinaTimeServer().startServer();
    }
}
