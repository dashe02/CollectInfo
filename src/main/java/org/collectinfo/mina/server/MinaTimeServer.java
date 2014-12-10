package org.collectinfo.mina.server;

/**
 * Created with IntelliJ IDEA.
 * User: hadoop
 * Date: 14-12-8
 * Time: 下午2:01
 * To change this template use File | Settings | File Templates.
 */
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.prefixedstring.PrefixedStringCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.collectinfo.mina.config.ConfigParser;

public class MinaTimeServer {
    private final static ConfigParser configParser=new ConfigParser();
    private final static int PORT=Integer.parseInt(configParser.getPros().get("port").toString());
    public void startServer(){
       try{
        IoAcceptor acceptor = new NioSocketAcceptor();
        acceptor.getFilterChain().addLast("logger",new LoggingFilter());
        acceptor.getFilterChain().addLast("codec",new ProtocolCodecFilter(new PrefixedStringCodecFactory(Charset.forName("UTF-8"))));
        acceptor.setHandler(new TimeServerHandler() );
        acceptor.getSessionConfig().setReadBufferSize(4098);
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
        acceptor.bind(new InetSocketAddress(PORT));
       }catch (Exception e){
           e.printStackTrace();
       }
    }
    public static void main(String[] args){

}
}

