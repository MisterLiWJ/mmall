package com.mmall.util;

import com.github.pagehelper.StringUtil;
import com.google.common.collect.Lists;
import com.mmall.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Administrator on 2018-05-25.
 */
@Slf4j
public class JsonUtil {
    private static ObjectMapper objectMapper=new ObjectMapper();
    static {
        /**
         * ALWAYS:对象所有字段全部列如
         * NON_NULL:非空的字段才出来
         * NON_DEFAULT:等于默认值得时候不显示
         * NON_EMPTY:为空不显示,和NON_NULL相比更加严格一点
         */
        objectMapper.setSerializationInclusion(Inclusion.NON_EMPTY);
        /**
         * WRITE_DATES_AS_TIMESTAMPS是否转换为TIMESTAMPS形式
         * false:会展示时分秒,比如:2017-11-09T11:06:29.383+0000
         * true:会展示为时间搓的形式
         */
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS,false);
        //忽略空bean转json的错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,false);
        /**
         * 所有的日期格式都统一为以下样式
         * DateTimeUtil.STANDARD_FORMAT:yyyy-MM-dd HH:mm:ss
         */
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAT));
        //忽略json中存在,但是在java中不存在对应属性情况,防止错误
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_NULL_FOR_PRIMITIVES,false);
    }
    /**
     * 对象转换为字符串(非格式化的,不方便查看)
     * @param obj
     * @param <T>
     * @return
     */
    public static <T>String obj2String(T obj){
        if(obj==null){
            return null;
        }
        try {
            return obj instanceof String?(String) obj:objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("Parse object to String error:",e);
            return null;
        }
    }



    /**
     * 对象转换为字符串(格式化了的,方便查看)
     * @param obj
     * @param <T>
     * @return
     */
    public static <T>String obj2StringPretty(T obj){
        if(obj==null){
            return null;
        }
        try {
            return obj instanceof String?(String) obj:objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            log.error("Parse object to String error:",e);
            return null;
        }
    }

    /**
     * 字符串转换为对象
     * @param str
     * @param clazz
     * @param <T>
     * @return
     */
    //将此方法声明为泛型方法<T> T返回类型 Class<T>限制传入类型
    public static <T> T string2Object(String str,Class<T> clazz){
        //字符串为空或者传入类型为空直接返回null出去
        if(StringUtil.isEmpty(str)||clazz==null){
            return null;
        }
        try {
            return clazz.equals(String.class)?(T) str:objectMapper.readValue(str,clazz);
        } catch (IOException e) {
            log.error("Parse String to Object error:",e);
            return null;
        }

    }

    public static <T> T string2Object(String str, TypeReference<T> typeReference){
        //字符串为空或者传入类型为空直接返回null出去
        if(StringUtil.isEmpty(str)||typeReference==null){
            return null;
        }
        try {
            return (T)(typeReference.getType().equals(String.class)?(T) str:objectMapper.readValue(str,typeReference));
        } catch (IOException e) {
            log.error("Parse String to Object error:",e);
            return null;
        }

    }

    public static <T> T string2Object(String str, Class<?> collectionClass,Class<?> elementClass){
        JavaType javaType=objectMapper.getTypeFactory().constructParametricType(collectionClass,elementClass);
        try {
            return objectMapper.readValue(str,javaType);
        } catch (Exception e) {
            log.error("Parse String to Object error:",e);
            return null;
        }
    }




    ///////////////////////////反序列化/////////////////
    public static void main(String args[]){
        User user=new User();
        User user1=new User();
        String jsonUser1=obj2String(user);
        System.out.println(jsonUser1);
        String jsonUser2=obj2StringPretty(user1);
        System.out.println(jsonUser2);
        List<User> list= Lists.newArrayList();
        list.add(user);
        list.add(user1);
        System.out.println(obj2StringPretty(list));
        String objectList=obj2StringPretty(list);
        List<User> list1=JsonUtil.string2Object(objectList, new TypeReference<List<User>>() {
        });
        List<User> userListObject2=JsonUtil.string2Object(objectList,List.class,User.class);
        System.out.println(userListObject2);

    }

}
