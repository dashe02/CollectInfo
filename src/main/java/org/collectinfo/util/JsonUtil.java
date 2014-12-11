package org.collectinfo.util;


import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: hadoop
 * Date: 14-12-11
 * Time: 下午4:55
 * To change this template use File | Settings | File Templates.
 */
public class JsonUtil {
    private ObjectMapper mapper;

    public JsonUtil(){
        mapper=new ObjectMapper();
    }
    public <T> T fromJson(String jsonString, Class<T> clazz) {
        if (jsonString==null) {
            return null;
        }
        try {
            return mapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            return null;
        }
    }
   public static void main(String[] args){
       String jsonString="{\"192.168.100.38\":{\"cpu\":\"0.0\",\"mem\":\"0.702204\",\"net\":\"0.0\",\"thread\":\"8\"}}";
       Map<String,String> map=new HashMap<String, String>();
       map=new JsonUtil().fromJson(jsonString,HashMap.class);
       System.out.println(map);
   }
}
