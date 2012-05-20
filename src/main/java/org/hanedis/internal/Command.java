package org.hanedis.internal;


/**
 * Created Date: 5/15/12
 */
public class Command<R> {

    private final Type type;

    private Object[] args;

    public Command(Type type) {
        this.type = type;
    }

    public Command(Type type, Object... args) {
        this.type = type;
        this.args = args;
    }

    public Type type() {
        return type;
    }

    public Object[] args() {
        return args;
    }

    public enum Type {
        SET,
        GET,
        PING,
        AUTH
    }
}


