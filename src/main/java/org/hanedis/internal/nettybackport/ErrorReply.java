package org.hanedis.internal.nettybackport;


import org.jboss.netty.buffer.ChannelBuffer;

import java.io.IOException;

/**
 * {@link Reply} which will be returned if an error was detected
 * 
 *
 */
public class ErrorReply extends Reply {
    public static final char MARKER = '-';
    private static final byte[] ERR = "ERR ".getBytes();
    public final ChannelBuffer error;

    public ErrorReply(ChannelBuffer error) {
        this.error = error;
    }

    @Override
    public void write(ChannelBuffer os) throws IOException {
        os.writeByte(MARKER);
        os.writeBytes(ERR);
        os.writeBytes(error, 0, error.readableBytes());
        os.writeBytes(Command.CRLF);
    }
}
