package com.skt.nova.batch.common.item;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamReader;

public class NovaItemReaderImpl<T> implements NovaItemReader<T> {

    private final ItemStreamReader<T> delegate;

    public NovaItemReaderImpl(ItemStreamReader<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public T read() throws Exception {
        return delegate.read();
    }

    @Override
    public void open(ExecutionContext executionContext) {
        delegate.open(executionContext);
    }

    @Override
    public void update(ExecutionContext executionContext) {
        delegate.update(executionContext);
    }

    @Override
    public void close() {
        delegate.close();
    }
}