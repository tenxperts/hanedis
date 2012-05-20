package org.hanedis;

import org.hanedis.internal.Command;
import org.hanedis.internal.Connection;
import org.hanedis.internal.netty.ConnectionImpl;

/**
 * Created Date: 5/19/12
 */
public class RedisClient {

    private Connection connection;

    public RedisClient() {
        connection = new ConnectionImpl();
        ((ConnectionImpl)connection).open();
    }

    public RedisClient(Connection connection) {
        this.connection = connection;
    }


    public void setInt(String key, Integer value) {
        Command<Integer> cmd = new Command<Integer>(Command.Type.SET, key, value);
        connection.execute(cmd);
    }

    public Integer getInt(String key) {
        Command<Integer> cmd = new Command<Integer>(Command.Type.GET, key);
        return connection.execute(cmd).get();
    }
}
