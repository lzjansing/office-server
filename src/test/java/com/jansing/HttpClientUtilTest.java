package com.jansing;

import com.jansing.office.utils.HttpClientUtil;
import org.junit.Test;

/**
 * Created by jansing on 16-12-21.
 */
public class HttpClientUtilTest {

    @Test
    public void testBaidu() throws Exception {
        HttpClientUtil httpClientUtil = new HttpClientUtil(false, null);
        System.out.println(httpClientUtil.doGet("http://localhost:8080/testoffice/front/style.css", null));
    }
}
