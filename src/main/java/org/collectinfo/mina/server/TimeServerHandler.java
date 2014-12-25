package org.collectinfo.mina.server;

/**
 * Created with IntelliJ IDEA.
 * User: hadoop
 * Date: 14-12-8
 * Time: 下午2:01
 * To change this template use File | Settings | File Templates.
 */
import java.util.Date;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.collectinfo.mina.config.ConfigParser;
import org.collectinfo.mina.file.FileUtil;
import org.collectinfo.redis.RedisConnector;
import org.collectinfo.util.StringUtil;

public class TimeServerHandler implements IoHandler {
    private FileUtil fileUtil=new FileUtil();
    private ConfigParser configParser=new ConfigParser();
    private RedisConnector redisConnector=new RedisConnector();
    @Override
    public void exceptionCaught(IoSession arg0, Throwable arg1)
            throws Exception {
        arg1.printStackTrace();

    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        String str = message.toString();
        System.out.println("接受到的消息:"+str);
        //将介绍到的信息写入文件
        //检查以日期为名的文件夹是否存在,若不存在,就建立一个，如果存在,就写文件
        String ipKey=StringUtil.parseStringByToken(str,":")[0];
        String currentValue=StringUtil.parseStringByToken(str,":")[1];
        if(redisConnector.get(ipKey)!=null){
            redisConnector.remove(ipKey);
        }
        redisConnector.set(ipKey,Integer.parseInt(configParser.getPros().get("redis.expire").toString()),currentValue);
        fileUtil.writeFile(str);
        if( str.trim().equalsIgnoreCase("quit") ) {
            session.close(true);
            return;
        }
        Date date = new Date();
        session.write(date.toString());
        System.out.println("Message written...");
    }

    @Override
    public void messageSent(IoSession arg0, Object arg1) throws Exception {
        // TODO Auto-generated method stub
        System.out.println("发送信息:"+arg1.toString());
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        // TODO Auto-generated method stub
        System.out.println("IP:"+session.getRemoteAddress().toString()+"断开连接");
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        // TODO Auto-generated method stub
        System.out.println("IP:"+session.getRemoteAddress().toString());
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        // TODO Auto-generated method stub
        System.out.println( "IDLE " + session.getIdleCount( status ));

    }

    @Override
    public void sessionOpened(IoSession arg0) throws Exception {
        // TODO Auto-generated method stub

    }

}
