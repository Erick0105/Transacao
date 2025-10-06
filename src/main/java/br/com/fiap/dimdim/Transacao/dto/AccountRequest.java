package br.com.fiap.dimdim.Transacao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;


public record AccountRequest(
        @NotBlank(message = "O nome do titular é obrigatório.")
        @Size(min = 3, message = "O nome do titular deve ter no mínimo 3 caracteres.")
        String ownerName,

        @NotBlank(message = "O número da conta é obrigatório.")
        String accountNumber,

        BigDecimal initialBalance
) {}
