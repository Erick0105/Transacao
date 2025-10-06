CREATE TABLE transactions (
    id UUID NOT NULL DEFAULT RANDOM_UUID(),
    source_account_id UUID,
    destination_account_id UUID,
    amount DECIMAL(19, 2) NOT NULL,
    type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    transaction_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    description VARCHAR(255),

    CONSTRAINT pk_transactions PRIMARY KEY (id),
    CONSTRAINT FK_transactions_source_account FOREIGN KEY (source_account_id) REFERENCES accounts(id),
    CONSTRAINT FK_transactions_destination_account FOREIGN KEY (destination_account_id) REFERENCES accounts(id),
    CONSTRAINT CHK_amount_positive CHECK (amount > 0)
);

CREATE INDEX IDX_transactions_source_account ON transactions(source_account_id);
CREATE INDEX IDX_transactions_destination_account ON transactions(destination_account_id);