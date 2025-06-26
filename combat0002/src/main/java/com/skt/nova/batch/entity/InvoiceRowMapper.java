package com.skt.nova.batch.entity;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class InvoiceRowMapper implements RowMapper<Invoice> {

    @Override
    public Invoice mapRow(ResultSet rs, int rowNum) throws SQLException {
        Invoice invoice = new Invoice();
        invoice.setId(UUID.fromString(rs.getString("id")));
        invoice.setStatus(rs.getString("status"));
        invoice.setCurrency(rs.getString("currency"));
        invoice.setAmount(rs.getLong("amount"));
        invoice.setDescription(rs.getString("description"));
        return invoice;
    }
}