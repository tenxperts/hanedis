package org.hanedis.internal;


/**
 * Created Date: 5/15/12
 */
public class Command {

    private final Type type;

    private Object[] args;

    public Command(Type type) {
        this.type = type;
    }

    public Command(Type type, Object... args) {
        this.type = type;
        this.args = args;
    }

    Type type() {
        return type;
    }


    public enum Type {
        SET,
        GET,
        PING,
        AUTH
    }
}


