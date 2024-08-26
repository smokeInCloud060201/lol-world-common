package vn.com.lol.common.utils;

import java.util.concurrent.atomic.AtomicLong;

public class IDGeneratorUtil {

    private static final int NODE_ID_BITS = 10;
    private static final int SEQUENCE_BITS = 12;

    private static final long MAX_NODE_ID = (1L << NODE_ID_BITS) - 1;
    private static final long MAX_SEQUENCE = (1L << SEQUENCE_BITS) - 1;

    private static final long CUSTOM_EPOCH = 1627776000000L; // Custom epoch (2021-08-01T00:00:00Z)

    private final long nodeId;
    private final AtomicLong sequence = new AtomicLong(0);
    private long lastTimestamp = -1L;

    public IDGeneratorUtil(long nodeId) {
        if (nodeId < 0 || nodeId > MAX_NODE_ID) {
            throw new IllegalArgumentException(String.format("NodeId must be between 0 and %d", MAX_NODE_ID));
        }
        this.nodeId = nodeId;
    }


    public synchronized long getNextId() {
        long currentTimestamp = timestamp();

        if (currentTimestamp < lastTimestamp) {
            throw new IllegalStateException("Clock moved backwards.");
        }

        if (currentTimestamp == lastTimestamp) {
            sequence.set((sequence.get() + 1) & MAX_SEQUENCE);
            if (sequence.get() == 0) {
                currentTimestamp = waitNextMillis(currentTimestamp);
            }
        } else {
            sequence.set(0);
        }

        lastTimestamp = currentTimestamp;

        return (currentTimestamp - CUSTOM_EPOCH) << (NODE_ID_BITS + SEQUENCE_BITS)
                | (nodeId << SEQUENCE_BITS)
                | sequence.get();
    }

    private long timestamp() {
        return System.currentTimeMillis();
    }

    private long waitNextMillis(long currentTimestamp) {
        while (currentTimestamp == lastTimestamp) {
            currentTimestamp = timestamp();
        }
        return currentTimestamp;
    }
}
