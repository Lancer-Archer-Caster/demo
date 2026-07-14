package com.jd.tech.stack.study.serverdemo.common;

import com.jd.tech.stack.study.serverdemo.utils.NetUtils;
import org.junit.jupiter.api.Test;

public class NetUtilsTest {

    @Test
    public void testGetLocalIp() {
        System.out.println(NetUtils.getLocalIp());
    }
}
