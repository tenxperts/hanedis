package org.hanedis.internal.nettybackport;


import org.jboss.netty.buffer.ChannelBuffer;

import java.io.IOException;

/**
 * {@link Reply} which will get returned if a {@link Integer} was requested via <code>GET</code>
 *
 */
public class IntegerReply extends Reply {
    public static final char MARKER = ':';
    public final long integer;

    public IntegerReply(long integer) {
        this.integer = integer;
    }

    @Override
    public void write(ChannelBuffer os) throws IOException {
        os.writeByte(MARKER);
        os.writeBytes(Command.numAndCRLF(integer));
    }
}
