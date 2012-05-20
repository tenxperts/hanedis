package org.hanedis.internal.netty;

import org.hanedis.internal.RedisReply;
import org.hanedis.internal.nettybackport.IntegerReply;
import org.jboss.netty.channel.*;
import org.jboss.netty.util.internal.QueueFactory;

import java.util.concurrent.BlockingQueue;

/**
 * Created Date: 5/18/12
 */
public class RedisReplyHandler extends SimpleChannelUpstreamHandler {

    private final BlockingQueue<RedisReply> queue;

    public RedisReplyHandler() {
        this(QueueFactory.createQueue(RedisReply.class));
    }

    public RedisReplyHandler(BlockingQueue<RedisReply> queue) {
        this.queue = queue;
    }

    public void put(RedisReply redisReply) throws InterruptedException {
        queue.put(redisReply);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
            throws Exception {
        final Object message = e.getMessage();
        final RedisReply future = queue.take();
        if (message instanceof IntegerReply) {
            future.setReply(message);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
            throws Exception {
    }

    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
            throws Exception {
    }

}
