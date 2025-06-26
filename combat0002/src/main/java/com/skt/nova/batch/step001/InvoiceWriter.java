package com.skt.nova.batch.step001;

import com.skt.nova.batch.entity.Invoice;
import com.skt.nova.batch.entity.InvoiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class InvoiceWriter implements ItemWriter<Invoice> {

    private final InvoiceRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(InvoiceWriter.class);

    public InvoiceWriter(InvoiceRepository repository) {
        this.repository = repository;
    }

    @Override
    public void write(@NonNull Chunk<? extends Invoice> items) throws Exception {

        repository.saveAll(items);

        logger.debug(
                "✍️ Write " + String.format("%,d", items.size()) + " records at 🧵"
                + Thread.currentThread().getName()
        );
    }
}