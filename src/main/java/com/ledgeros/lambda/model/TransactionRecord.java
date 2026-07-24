package com.ledgeros.lambda.model;

import java.math.BigDecimal;

/**
 * Custom domain record example for LedgerOS.
 */
public record TransactionRecord(
    String id,
    String description,
    BigDecimal amount,
    String currency,
    String category
) {}
