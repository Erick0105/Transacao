package br.com.fiap.dimdim.Transacao.service;

import br.com.fiap.dimdim.Transacao.dto.AccountRequest;
import br.com.fiap.dimdim.Transacao.dto.AccountResponse;
import br.com.fiap.dimdim.Transacao.entities.Account;
import br.com.fiap.dimdim.Transacao.exception.BusinessException;
import br.com.fiap.dimdim.Transacao.exception.ResourceNotFoundException;
import br.com.fiap.dimdim.Transacao.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional
    public AccountResponse createAccount(AccountRequest request) {
        if (accountRepository.existsByAccountNumber(request.accountNumber())) {
            throw new BusinessException("O número da conta " + request.accountNumber() + " já existe.");
        }

        Account newAccount = new Account();
        newAccount.setOwnerName(request.ownerName());
        newAccount.setAccountNumber(request.accountNumber());
        newAccount.setBalance(request.initialBalance() != null ? request.initialBalance() : BigDecimal.ZERO);

        Account savedAccount = accountRepository.save(newAccount);
        return new AccountResponse(savedAccount);
    }

    @Transactional(readOnly = true)
    public AccountResponse getAccountById(UUID id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conta com ID " + id + " não encontrada."));
        return new AccountResponse(account);
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> getAllAccounts() {
        return accountRepository.findAll()
                .stream()
                .map(AccountResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public AccountResponse updateAccountOwner(UUID id, String newOwnerName) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conta com ID " + id + " não encontrada."));

        account.setOwnerName(newOwnerName);
        Account updatedAccount = accountRepository.save(account);
        return new AccountResponse(updatedAccount);
    }

    @Transactional
    public void deleteAccount(UUID id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conta com ID " + id + " não encontrada."));
        if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new BusinessException("Não é possível excluir conta com saldo. Saldo atual: " + account.getBalance());
        }
        accountRepository.delete(account);
    }
}