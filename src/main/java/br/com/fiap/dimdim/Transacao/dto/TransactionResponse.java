package br.com.fiap.dimdim.Transacao.dto;

import br.com.fiap.dimdim.Transacao.entities.Transaction;
import br.com.fiap.dimdim.Transacao.enums.TransactionStatus;
import br.com.fiap.dimdim.Transacao.enums.TransactionType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;


public record TransactionResponse(
        UUID id,
        String sourceAccountNumber,
        String destinationAccountNumber,
        BigDecimal amount,
        TransactionType type,
        TransactionStatus status,
        String description,
        OffsetDateTime transactionDate
) {
    public TransactionResponse(Transaction entity) {
        this(
                entity.getId(),
                entity.getSourceAccount() != null ? entity.getSourceAccount().getAccountNumber() : null,
                entity.getDestinationAccount() != null ? entity.getDestinationAccount().getAccountNumber() : null,
                entity.getAmount(),
                entity.getType(),
                entity.getStatus(),
                entity.getDescription(),
                entity.getTransactionDate()
        );
    }
}