package com.skt.nova.batch.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class NovaBatchProperties {

    private int chunkSize;
    private boolean usePartitioner;
    private int gridSize;
    private boolean useVirtualThread;

    private int normalThreadPoolMinSize;
    private int normalThreadPoolMaxSize;
    private int normalThreadPoolQueueCapacity;

    public int getChunkSize() { return chunkSize; }
    public void setChunkSize(int chunkSize) { this.chunkSize = chunkSize; }

    public int getGridSize() { return gridSize; }
    public void setGridSize(int gridSize) { this.gridSize = gridSize; }

    public boolean isUsePartitioner() { return usePartitioner; }
    public void setUsePartitioner(boolean usePartitioner) { this.usePartitioner = usePartitioner; }

    public boolean isUseVirtualThread() { return useVirtualThread; }
    public void setUseVirtualThread(boolean useVirtualThread) { this.useVirtualThread = useVirtualThread; }

    public int getNormalThreadPoolMinSize() { return normalThreadPoolMinSize; }
    public void setNormalThreadPoolMinSize(int normalThreadPoolMinSize) {
        this.normalThreadPoolMinSize = normalThreadPoolMinSize;
    }

    public int getNormalThreadPoolMaxSize() { return normalThreadPoolMaxSize; }
    public void setNormalThreadPoolMaxSize(int normalThreadPoolMaxSize) {
        this.normalThreadPoolMaxSize = normalThreadPoolMaxSize;
    }

    public int getNormalThreadPoolQueueCapacity() { return normalThreadPoolQueueCapacity; }
    public void setNormalThreadPoolQueueCapacity(int normalThreadPoolQueueCapacity) {
        this.normalThreadPoolQueueCapacity = normalThreadPoolQueueCapacity;
    }
}
