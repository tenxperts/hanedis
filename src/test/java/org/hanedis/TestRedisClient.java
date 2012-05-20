package org.hanedis;

import org.junit.Test;

/**
 * Created Date: 5/19/12
 */
public class TestRedisClient {

    @Test
    public void testSetInt() throws InterruptedException {
        RedisClient redisClient = new RedisClient();
        redisClient.setInt("num", 2);
        Thread.sleep(30000);
    }

    @Test
    public void testGetInt() {
        RedisClient redisClient = new RedisClient();
        final Integer num = redisClient.getInt("num");
    }
}
