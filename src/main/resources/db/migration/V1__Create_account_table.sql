CREATE TABLE accounts (
    id UUID NOT NULL DEFAULT RANDOM_UUID(),
    owner_name VARCHAR(255) NOT NULL,
    account_number VARCHAR(20) NOT NULL UNIQUE,
    balance DECIMAL(19, 2) NOT NULL CHECK (balance >= 0),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_accounts PRIMARY KEY (id)
);

INSERT INTO accounts (owner_name, account_number, balance) VALUES ('João Silva', '12345-6', 1500.75);
INSERT INTO accounts (owner_name, account_number, balance) VALUES ('Maria Souza', '98765-4', 3250.00);