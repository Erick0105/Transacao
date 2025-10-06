package br.com.fiap.dimdim.Transacao.repository;

import br.com.fiap.dimdim.Transacao.entities.Transaction;
import br.com.fiap.dimdim.Transacao.enums.TransactionStatus;
import br.com.fiap.dimdim.Transacao.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository  extends JpaRepository<Transaction, UUID> {
    List<Transaction> findBySourceAccountIdOrDestinationAccountId(UUID sourceId, UUID destinationId);

    @Query("SELECT t FROM Transaction t WHERE " +
            "(:type IS NULL OR t.type = :type) AND " +
            "(:status IS NULL OR t.status = :status)")
    List<Transaction> findWithFilters(
            @Param("type") TransactionType type,
            @Param("status") TransactionStatus status
    );
}
