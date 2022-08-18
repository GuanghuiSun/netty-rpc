package com.rpcclient.generator;

import java.util.concurrent.atomic.AtomicLong;

/**
 * id生成
 */
public class SequenceIdGenerator {
    private static final AtomicLong id = new AtomicLong();

    public static long nextId() {
        return id.incrementAndGet();
    }
}
