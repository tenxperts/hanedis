package org.hanedis.internal;

import org.hanedis.internal.ConnectionException;

import java.util.concurrent.*;

/**
 * Created Date: 5/18/12
 */
public class RedisReply<R> implements Future<R> {

    private R reply;
    private CountDownLatch latch;

    public RedisReply() {
        latch = new CountDownLatch(1);
    }

    public void setReply(R r) {
        this.reply = r;
        latch.countDown();
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        //TODO: Not sure yet about the boolean arg coming in
        if (latch.getCount() == 1) {
            latch.countDown();
            reply = null;
        }
        return true;
    }

    public boolean isCancelled() {
        return isDone() && reply != null;
    }

    public boolean isDone() {
        return latch.getCount() == 0;
    }

    public R get() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new ConnectionException(e);
        }
        return reply;
    }

    public R get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (!latch.await(timeout, unit)) {
            throw new TimeoutException("Timed out while waiting for reply");
        }
        return reply;
    }
}
