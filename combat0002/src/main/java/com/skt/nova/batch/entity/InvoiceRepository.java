package com.skt.nova.batch.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
}