package com.skt.nova.batch.step001;

import com.skt.nova.batch.entity.Invoice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class InvoiceProcessor implements ItemProcessor<Invoice, Invoice> {

    private static final String STATUS_PROCESSED = "PROCESSED";

    private static final Logger logger = LoggerFactory.getLogger(InvoiceProcessor.class);

    @Override
    public Invoice process(Invoice invoice) throws Exception {

        logger.debug(
                "\uD83D\uDCBB Process " + invoice.getAmount() + " " + invoice.getCurrency()
        );
        invoice.setStatus(STATUS_PROCESSED);
        return invoice;
    }
}