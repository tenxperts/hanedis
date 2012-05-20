package org.hanedis.internal;

import java.util.concurrent.Future;

/**
 * Created Date: 5/15/12
 */
public interface Connection {

     <R> RedisReply<R> execute(Command<R> command);

    void close();

}
