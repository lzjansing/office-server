package com.jansing;

import com.google.common.collect.Lists;
import com.jansing.office.utils.Collections3;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by jansing on 16-11-18.
 */
public class FooTest {


    @Test
    public void test01() {
        StringBuilder sb = new StringBuilder();
        sb.append("123").append("abc");
        System.out.println(sb);
    }

    @Test
    public void test02(){
        List<String> list = Lists.newArrayList();
        list.add("aaa");
        list.add(0, "bbb");
        System.out.println(list);
    }

    @Test
    public void test03() throws IOException {
        System.out.println(LocalDate.now());
    }


}
