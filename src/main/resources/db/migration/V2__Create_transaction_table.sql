
CREATE TABLE transactions (
    id UNIQUEIDENTIFIER NOT NULL DEFAULT NEWID(),
    source_account_id UNIQUEIDENTIFIER NULL,
    destination_account_id UNIQUEIDENTIFIER NULL,
    amount DECIMAL(19, 2) NOT NULL CHECK (amount > 0),
    type NVARCHAR(50) NOT NULL,
    status NVARCHAR(50) NOT NULL,
    transaction_date DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
    description NVARCHAR(255) NULL,

    CONSTRAINT pk_transactions PRIMARY KEY (id),
    CONSTRAINT FK_transactions_source_account FOREIGN KEY (source_account_id) REFERENCES accounts(id),
    CONSTRAINT FK_transactions_destination_account FOREIGN KEY (destination_account_id) REFERENCES accounts(id)
);

-- Índices para performance
CREATE INDEX IDX_transactions_source_account ON transactions(source_account_id);
CREATE INDEX IDX_transactions_destination_account ON transactions(destination_account_id);
