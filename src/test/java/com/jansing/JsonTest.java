package com.jansing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jansing.common.mapper.JsonMapper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jansing on 16-12-23.
 */
public class JsonTest {



    @Test
    public void test01(){
        String fileInfoStr = "{SHA256=gkX+ni0SygDnNlgSfj+kgBlCjdEK9ibUojBzBFVkE5c=, OwnerId=jansing, Version=1.0, Size=11775, BaseFileName=1.docx}";
        HashMap<String, String> fileInfo = JsonMapper.getInstance().fromJson(fileInfoStr, HashMap.class);
        System.out.println(fileInfo);
    }

    @Test
    public void test02(){
        ArrayList list = Lists.newArrayList();
        HashMap map = Maps.newHashMap();
        map.put("id", Integer.valueOf(1));
        map.put("pId", Integer.valueOf(-1));
        map.put("name", "根节点");
        list.add(map);
        map = Maps.newHashMap();
        map.put("id", Integer.valueOf(2));
        map.put("pId", Integer.valueOf(1));
        map.put("name", "你好");
        map.put("open", Boolean.valueOf(true));
        list.add(map);
        String json = JsonMapper.getInstance().toJson(list);
        System.out.println(json);
    }

}
