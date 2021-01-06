package com.project.base.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * Created by yinshaobo at 2018/6/16
 */
public class XmlUtils {

    /**
     * 将对象转化为xml
     * @param obj 对象
     * @return xml
     */
    public static String beanToXml(Object obj) {
        XStream xstream = new XStream();
        //必须使用注解方式
        xstream.processAnnotations(obj.getClass());
        return xstream.toXML(obj);
    }

    /**
     * 将xml转化为bean
     * @param xml xml
     * @param clazz class
     * @param <T> T
     * @return obj
     */
    public static <T> T xmlToBean(String xml, Class<T> clazz) {
        XStream xstream = new XStream(new DomDriver());
        xstream.processAnnotations(clazz);
        return (T) xstream.fromXML(xml);
    }

}
