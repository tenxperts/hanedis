package org.hanedis.internal.netty;

import org.hanedis.internal.Command;
import org.hanedis.internal.Connection;
import org.hanedis.internal.Reply;
import org.hanedis.internal.nettybackport.RedisDecoder;
import org.hanedis.internal.nettybackport.RedisEncoder;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.queue.BlockingReadHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created Date: 5/15/12
 */
public class ConnectionImpl implements Connection {

    private final String host;
    private final int port;

    BlockingReadHandler<Reply> blockingReadHandler;
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
        cb = new ClientBootstrap(new NioClientSocketChannelFactory());
        blockingReadHandler = new BlockingReadHandler<Reply>();
        cb.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("redisEncoder", new RedisEncoder());
                pipeline.addLast("redisDecoder", new RedisDecoder());
                pipeline.addLast("result", blockingReadHandler);
                return pipeline;
            }
        });
        ChannelFuture redis = cb.connect(new InetSocketAddress("10.3.1.133", 6379));
        redis.await().rethrowIfFailed();
        channel = redis.getChannel();
    }

    public Reply execute(Command command) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void close() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
