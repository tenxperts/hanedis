package org.hanedis.internal.netty;

import org.hanedis.internal.Command;
import org.hanedis.internal.Connection;
import org.hanedis.internal.ConnectionException;
import org.hanedis.internal.RedisReply;
import org.hanedis.internal.nettybackport.RedisDecoder;
import org.hanedis.internal.nettybackport.RedisEncoder;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Future;

/**
 * Created Date: 5/15/12
 */
public class ConnectionImpl implements Connection {

    private final String host;
    private final int port;

    private RedisReplyHandler redisReplyHandler;
    Channel channel;
    ClientBootstrap cb;


    public ConnectionImpl() {
        this("localhost", 6379);
    }

    public ConnectionImpl(String host) {
        this(host, 6379);
    }

    public ConnectionImpl(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void open() {
        try {
            cb = new ClientBootstrap(new NioClientSocketChannelFactory());
            redisReplyHandler = new RedisReplyHandler();
            cb.setPipelineFactory(new ChannelPipelineFactory() {
                public ChannelPipeline getPipeline() throws Exception {
                    ChannelPipeline pipeline = Channels.pipeline();
                    pipeline.addLast("redisEncoder", new RedisEncoder());
                    pipeline.addLast("redisDecoder", new RedisDecoder());
                    pipeline.addLast("replyReader", redisReplyHandler);
                    return pipeline;
                }
            });
            ChannelFuture channelFuture = cb.connect(new InetSocketAddress(host, port));
            channelFuture.await().rethrowIfFailed();
            channel = channelFuture.getChannel();
        } catch (Exception e) {
            throw new ConnectionException(e);
        } finally {
        }
    }

    public <RETURN_TYPE> RedisReply<RETURN_TYPE> execute(Command<RETURN_TYPE> command) {
        Object[] args = command.args();
        Object[] objects = new Object[(args != null ? args.length : 0) + 1];
        objects[0] = command.type();
        if (objects.length > 1) {
            System.arraycopy(args, 0, objects, 1, args.length);
        }
        RedisReply<RETURN_TYPE> reply = new RedisReply<RETURN_TYPE>();
        try {
            redisReplyHandler.put(reply);
        } catch (InterruptedException e) {
            new ConnectionException(e);
        }
        channel.write(new org.hanedis.internal.nettybackport.Command(objects));
        return reply;
    }

    public void close() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
