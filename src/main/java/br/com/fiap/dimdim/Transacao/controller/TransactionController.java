package br.com.fiap.dimdim.Transacao.controller;

import br.com.fiap.dimdim.Transacao.dto.TransactionRequest;
import br.com.fiap.dimdim.Transacao.dto.TransactionResponse;
import br.com.fiap.dimdim.Transacao.enums.TransactionStatus;
import br.com.fiap.dimdim.Transacao.enums.TransactionType;
import br.com.fiap.dimdim.Transacao.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "API para gerenciamento de transações")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transfer")
    @Operation(summary = "Realiza uma transferência entre contas", description = "Transfere um valor de uma conta de origem para uma conta de destino e registra a transação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transferência realizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos, saldo insuficiente ou contas iguais"),
            @ApiResponse(responseCode = "404", description = "Conta de origem ou destino não encontrada")
    })
    public ResponseEntity<TransactionResponse> performTransfer(@Valid @RequestBody TransactionRequest request) {
        TransactionResponse response = transactionService.performTransfer(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma transação por ID", description = "Retorna os detalhes de uma transação específica com base no seu UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transação encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Transação não encontrada")
    })
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable UUID id) {
        TransactionResponse response = transactionService.getTransactionById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/account/{accountId}")
    @Operation(summary = "Busca transações de uma conta específica", description = "Retorna uma lista de todas as transações (seja como origem ou destino) de uma conta específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transações encontradas com sucesso"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    public ResponseEntity<List<TransactionResponse>> getTransactionsByAccountId(@PathVariable UUID accountId) {
        List<TransactionResponse> responses = transactionService.getTransactionsForAccount(accountId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping
    @Operation(summary = "Lista todas as transações com filtros opcionais",
            description = "Retorna uma lista de transações. Pode ser filtrada por 'type' e/ou 'status'.")
    public ResponseEntity<List<TransactionResponse>> getAllTransactions(
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) TransactionStatus status) {

        List<TransactionResponse> responses = transactionService.getAllTransactions(type, status);
        return ResponseEntity.ok(responses);
    }
}