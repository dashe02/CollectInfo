package org.collectinfo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: hadoop
 * Date: 14-11-6
 * Time: 下午5:51
 * To change this template use File | Settings | File Templates.
 */
public class NIOClient {
    //NIOClient的任务就是按照配置文件中的ip,切分规则将相应的sql语句发送到NIOServer，然后NIOServer那里会重新拼接sql,然后调用Jdbc与mysql集群里
    //进行交互
    /*标识数字*/
    private static int flag=0;
    /*缓冲区大小*/
    private static int BLOCK=4096;
    /*接受数据缓冲区*/
    private static ByteBuffer sendBuffer=ByteBuffer.allocate(BLOCK);
    /*发送数据缓冲区*/
    private static ByteBuffer receiveBuffer=ByteBuffer.allocate(BLOCK);

    public void connect(String ip,int port,String sendText) throws IOException {
        InetSocketAddress SERVER_ADDRESS=new InetSocketAddress(ip,port);
        //打开socket通道
        SocketChannel socketChannel=SocketChannel.open();
        //设置为非阻塞方式
        socketChannel.configureBlocking(false);
        //打开选择器
        Selector selector=Selector.open();
        //注册连接服务socket动作
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        //连接
        socketChannel.connect(SERVER_ADDRESS);
        //分配缓冲区大小内存
        Set<SelectionKey> selectionKeys;
        Iterator<SelectionKey> iterator;
        SelectionKey selectionKey;
        SocketChannel client;
        String receiveText;
        int count=0;
        while(true){
            //选择一组键,其相应的通道已为I/O操作准备就绪
            //此方法执行处于阻塞模式的选择操作
            selector.select();
            //返回此选择器的已选择键集
            selectionKeys=selector.selectedKeys();
            iterator=selectionKeys.iterator();
            while(iterator.hasNext()){
                selectionKey=iterator.next();
                if(selectionKey.isConnectable()){
                 System.out.println("client connect");
                 client=(SocketChannel)selectionKey.channel();
                  //判断此通道上是否正在进行连接操作
                  //完成套接字通道的连接过程
                 if(client.isConnectionPending()){
                     client.finishConnect();
                     System.out.println("完成连接!");
                     sendBuffer.clear();
                     sendBuffer.put(sendText.getBytes());
                     sendBuffer.flip();
                     client.write(sendBuffer);
                 }
                    client.register(selector,SelectionKey.OP_READ);
                }else if(selectionKey.isReadable()){
                    client=(SocketChannel)selectionKey.channel();
                    //将缓冲区清空以备下次读取
                    receiveBuffer.clear();
                    //读取服务器发送来的数据到缓冲区
                    count=client.read(receiveBuffer);
                    if(count>0){
                        receiveText=new String(receiveBuffer.array(),0,count);
                        System.out.println("客户端接受服务器端数据--:" + receiveText);
                        client.register(selector,SelectionKey.OP_WRITE);
                    }
                }else if(selectionKey.isWritable()){
                      sendBuffer.clear();
                      client=(SocketChannel)selectionKey.channel();
                   /*   sendText="message from client--"+(flag++);*/
                      sendBuffer.put(sendText.getBytes());
                    //将缓冲区个标志复位,因为向里面put了数据标志被改变要想从中读取数据发向服务器,就要复位
                    sendBuffer.flip();
                    client.write(sendBuffer);
                    System.out.println("客户端向服务器发送数据--:" + sendText);
                    client.register(selector,SelectionKey.OP_READ);
                }
            }
            selectionKeys.clear();
        }
    }
    public static void main(String[] args) throws IOException{
        String ip="127.0.0.1";
        int port=8888;
        NIOClient nioClient=new NIOClient();
        nioClient.connect(ip,port,"message from client!");
    }
}
