package org.hanedis.internal;

/**
 * Created Date: 5/15/12
 */
public interface Connection {

    Reply execute(Command command);

    void close();

}
