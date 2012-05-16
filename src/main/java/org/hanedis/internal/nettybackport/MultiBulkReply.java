package org.hanedis.internal.nettybackport;


import org.jboss.netty.buffer.ChannelBuffer;

import java.io.IOException;

/**
 * {@link Reply} which contains a bulk of {@link Reply}'s
 * 
 *
 */
public class MultiBulkReply extends Reply {
    public static final char MARKER = '*';

    // State
    public Object[] byteArrays;
    private int size;
    private int num;

    public MultiBulkReply() {
    }

    public MultiBulkReply(Object... values) {
        this.byteArrays = values;
    }
    
    public void read(RedisDecoder rd, ChannelBuffer is) throws IOException {
        // If we attempted to read the size before, skip the '*' and reread it
        if (size == -1) {
            byte star = is.readByte();
            if (star == MARKER) {
                size = 0;
            } else {
                throw new AssertionError("Unexpected character in stream: " + star);
            }
        }
        if (size == 0) {
            // If the read fails, we need to skip the star
            size = -1;
            // Read the size, if this is successful we won't read the star again
            size = RedisDecoder.readInteger(is);
            byteArrays = new Object[size];
            rd.checkpoint();
        }
        for (int i = num; i < size; i++) {
            int read = is.readByte();
            if (read == BulkReply.MARKER) {
                byteArrays[i] = RedisDecoder.readBytes(is);
            } else if (read == IntegerReply.MARKER) {
                byteArrays[i] = RedisDecoder.readInteger(is);
            } else {
                throw new IOException("Unexpected character in stream: " + read);
            }
            num = i + 1;
            rd.checkpoint();
        }
    }

    @Override
    public void write(ChannelBuffer os) throws IOException {
        os.writeByte(MARKER);
        if (byteArrays == null) {
            os.writeBytes(Command.NEG_ONE_AND_CRLF);
        } else {
            os.writeBytes(Command.numAndCRLF(byteArrays.length));
            for (Object value : byteArrays) {
                if (value == null) {
                    os.writeByte(BulkReply.MARKER);
                    os.writeBytes(Command.NEG_ONE_AND_CRLF);
                } else if (value instanceof byte[]) {
                    byte[] bytes = (byte[]) value;
                    os.writeByte(BulkReply.MARKER);
                    int length = bytes.length;
                    os.writeBytes(Command.numAndCRLF(length));
                    os.writeBytes(bytes);
                    os.writeBytes(Command.CRLF);
                } else if (value instanceof Number) {
                    os.writeByte(IntegerReply.MARKER);
                    os.writeBytes(Command.numAndCRLF(((Number) value).longValue()));
                }
            }
        }
    }
}
