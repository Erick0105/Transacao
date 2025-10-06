package br.com.fiap.dimdim.Transacao.service;

import br.com.fiap.dimdim.Transacao.dto.TransactionRequest;
import br.com.fiap.dimdim.Transacao.dto.TransactionResponse;
import br.com.fiap.dimdim.Transacao.entities.Account;
import br.com.fiap.dimdim.Transacao.entities.Transaction;
import br.com.fiap.dimdim.Transacao.enums.TransactionStatus;
import br.com.fiap.dimdim.Transacao.enums.TransactionType;
import br.com.fiap.dimdim.Transacao.exception.BusinessException;
import br.com.fiap.dimdim.Transacao.exception.ResourceNotFoundException;
import br.com.fiap.dimdim.Transacao.repository.AccountRepository;
import br.com.fiap.dimdim.Transacao.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;


    @Transactional
    public TransactionResponse performTransfer(TransactionRequest request) {
        if (request.fromAccountNumber().equals(request.toAccountNumber())) {
            throw new BusinessException("A conta de origem e destino não podem ser a mesma.");
        }

        Account sourceAccount = accountRepository.findByAccountNumber(request.fromAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Conta de origem não encontrada."));

        Account destinationAccount = accountRepository.findByAccountNumber(request.toAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Conta de destino não encontrada."));

        if (sourceAccount.getBalance().compareTo(request.amount()) < 0) {
            throw new BusinessException("Saldo insuficiente na conta de origem.");
        }

        sourceAccount.setBalance(sourceAccount.getBalance().subtract(request.amount()));
        destinationAccount.setBalance(destinationAccount.getBalance().add(request.amount()));

        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);

        Transaction transaction = new Transaction();
        transaction.setSourceAccount(sourceAccount);
        transaction.setDestinationAccount(destinationAccount);
        transaction.setAmount(request.amount());
        transaction.setType(TransactionType.TRANSFER);
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setDescription(request.description());

        Transaction savedTransaction = transactionRepository.save(transaction);

        return new TransactionResponse(savedTransaction);
    }

    @Transactional(readOnly = true)
    public TransactionResponse getTransactionById(UUID id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transação com ID " + id + " não encontrada."));
        return new TransactionResponse(transaction);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionsForAccount(UUID accountId) {
        if (!accountRepository.existsById(accountId)) {
            throw new ResourceNotFoundException("Conta com ID " + accountId + " não encontrada.");
        }
        return transactionRepository.findBySourceAccountIdOrDestinationAccountId(accountId, accountId)
                .stream()
                .map(TransactionResponse::new)
                .collect(Collectors.toList());
    }


    @Transactional
    public void deleteTransaction(UUID id) {
        if (!transactionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Transação com ID " + id + " não encontrada.");
        }
        transactionRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getAllTransactions(TransactionType type, TransactionStatus status) {
        List<Transaction> transactions = transactionRepository.findWithFilters(type, status);

        return transactions.stream()
                .map(TransactionResponse::new)
                .collect(Collectors.toList());
    }
}