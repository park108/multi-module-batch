package com.skt.nova.batch.common.item;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.lang.Nullable;

public interface NovaItemReader<T> extends ItemStreamReader<T> {

    @Nullable
    T read() throws Exception;

    void open(@Nullable ExecutionContext executionContext) throws ItemStreamException;

    void update(@Nullable ExecutionContext executionContext) throws ItemStreamException;

    void close() throws ItemStreamException;
}