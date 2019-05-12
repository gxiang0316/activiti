package com.xiang.activiti;

import com.xiang.activiti.act.entity.CusModel;
import com.xiang.activiti.act.entity.Leave;
import com.xiang.activiti.act.entity.Start;
import org.apache.commons.collections.map.LinkedMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.activiti.spring.boot.SecurityAutoConfiguration.class})
public class ActivitiApplication {

    public static void main(String[] args) {
        test();
        //SpringApplication.run(ActivitiApplication.class, args);
    }

    protected static void test()
    {
//        CusModel cusModel1 = new CusModel();
//        cusModel1.setDescription("111");
//        cusModel1.setKey("test1");
//        cusModel1.setName("u");
//        CusModel cusModel2 = new CusModel();
//        cusModel2.setName("u");
//        cusModel2.setKey("test1");
//        cusModel2.setDescription("111");
//        CusModel cusModel3 = cusModel2;
//        System.out.println(cusModel1 == cusModel2);
//        System.out.println(cusModel1 == cusModel3);
//        System.out.println(cusModel2 == cusModel3);
//        System.out.println(cusModel1.equals(cusModel2));
//        System.out.println(cusModel1.equals(cusModel3));
//        System.out.println(cusModel2.equals(cusModel3));
//        System.out.println(cusModel1);
//        System.out.println(cusModel2);
//        System.out.println(cusModel3);
//        System.out.println(cusModel1.hashCode());
//        System.out.println(cusModel2.hashCode());
//        System.out.println(cusModel3.hashCode());
//
//        String a = new String("ab");
//        String b = new String("ab");
//        String aa = "ab";
//        String bb = "ab";
//        System.out.println(a == b);
//        System.out.println(aa == bb);
//        System.out.println(a.equals(b));
//        System.out.println(aa.equals(aa));
//        System.out.println(a);
//        System.out.println(b);
//        System.out.println(aa);
//        System.out.println(bb);
//        System.out.println(a.hashCode());
//        System.out.println(b.hashCode());
//        System.out.println(aa.hashCode());
//        System.out.println(bb.hashCode());
//
//        HashSet hashset = new HashSet();
//        hashset.add(cusModel1);
//        hashset.add(cusModel2);
//        hashset.add(a);
//        hashset.add(b);
//        System.out.println(hashset.toString());



        Map<Start,String> map = new LinkedHashMap<>();
        map.put(new Start("Arya"),"1");
        map.put(new Start("Ned"),"2");
        map.put(new Start("Sansa"),"3");
        map.put(new Start("Bran"),"4");
        map.put(new Start("Jaime"),"5");
        System.out.println(1>>> 16);
    }
}
