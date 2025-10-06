package br.com.fiap.dimdim.Transacao.controller;

import br.com.fiap.dimdim.Transacao.dto.AccountRequest;
import br.com.fiap.dimdim.Transacao.dto.AccountResponse;
import br.com.fiap.dimdim.Transacao.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Tag(name = "Accounts", description = "API para gerenciamento de contas")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    @Operation(summary = "Cria uma nova conta bancária", description = "Registra uma nova conta no sistema com os dados fornecidos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Conta criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou número de conta já existente")
    })
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountRequest request) {
        AccountResponse response = accountService.createAccount(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma conta por ID", description = "Retorna os detalhes de uma conta específica com base no seu UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conta encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable UUID id) {
        AccountResponse response = accountService.getAccountById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Lista todas as contas", description = "Retorna uma lista com todas as contas cadastradas no sistema.")
    @ApiResponse(responseCode = "200", description = "Lista de contas retornada com sucesso")
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        List<AccountResponse> responses = accountService.getAllAccounts();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}/owner")
    @Operation(summary = "Atualiza o nome do titular da conta", description = "Altera o nome do titular (ownerName) de uma conta existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Nome do titular atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    public ResponseEntity<AccountResponse> updateAccountOwner(@PathVariable UUID id, @RequestBody String newOwnerName) {
        AccountResponse response = accountService.updateAccountOwner(id, newOwnerName);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui uma conta", description = "Remove uma conta do sistema. A operação só é permitida se a conta não tiver saldo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Conta excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada"),
            @ApiResponse(responseCode = "400", description = "Não é possível excluir conta com saldo")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@PathVariable UUID id) {
        accountService.deleteAccount(id);
    }
}