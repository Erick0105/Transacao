package br.com.fiap.dimdim.Transacao.dto;

import br.com.fiap.dimdim.Transacao.entities.Account;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record AccountResponse(
        UUID id,
        String ownerName,
        String accountNumber,
        BigDecimal balance,
        OffsetDateTime createdAt
) {

    public AccountResponse(Account entity) {
        this(
                entity.getId(),
                entity.getOwnerName(),
                entity.getAccountNumber(),
                entity.getBalance(),
                entity.getCreatedAt()
        );
    }
}