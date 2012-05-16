package org.hanedis.internal.nettybackport;


import org.jboss.netty.buffer.ChannelBuffer;

import java.io.IOException;

public class PUnsubscribeReply extends UnsubscribeReply {

    public PUnsubscribeReply(byte[][] patterns) {
        super(patterns);
    }

    @Override
    public void write(ChannelBuffer os) throws IOException {
        // Do nothing
    }
}
