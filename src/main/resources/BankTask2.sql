-- Insertion Statements:

INSERT INTO customers (id, contact_id) VALUES (1001,1001);
INSERT INTO accounts (id, customer_id, account_type, balance, date_opened) VALUES (2001, 1001, 'Savings', 1000.00, '2023-01-01');
INSERT INTO transactions (id, account_id, transaction_type, amount, transaction_date) VALUES (3001, 2001, 'Deposit', 500.00, '2023-01-05');
INSERT INTO branches (id, branch_name, branch_address, manager_id) VALUES (4001, 'Main Branch', '456 Elm St', 1001);
INSERT INTO employees (id, contact_id, position, salary, date_hired, branch_id) VALUES (5001, 7001, 'Manager', 5000.00, '2022-08-01', 4001);
INSERT INTO loans (id, account_id, loan_amount, interest_rate, loan_duration) VALUES (6001, 2001, 10000.00, 5.5, 36);
INSERT INTO loan_payments (id, loan_id, payment_amount, payment_date) VALUES (7001, 6001, 500.00, '2023-02-01');
INSERT INTO cards (id, account_id, card_number, card_type, expiration_date) VALUES (8001, 2001, '1234567812345678', 'Visa', '2025-12-31');
INSERT INTO transfers (id, sender_account_id, receiver_account_id, amount, transfer_date) VALUES (10001, 2001, 3001, 200.00, '2023-03-01');
INSERT INTO contacts(id,first_name,last_name,date_of_birth,address,phone_number,email) VALUES (1001,'John','Smith',05-05-1991,'1234 Maple Street', 2335457784,'john_smith@icloud.com');

-- Updating Statements:


UPDATE customers SET phone_number = '555-432-1234' WHERE id = 1001;
UPDATE accounts SET balance = balance + 100.00 WHERE id = 2001;
UPDATE transactions SET amount = 200.00 WHERE id = 3001;
UPDATE branches SET branch_address = '789 Maple Ave' WHERE id = 4001;
UPDATE employees SET position = 'Senior Manager' WHERE id = 5001;
UPDATE loans SET loan_amount = loan_amount + 5000.00 WHERE id = 6001;
UPDATE loan_payments SET payment_amount = 100.00 WHERE id = 7001;
UPDATE cards SET card_type = 'MasterCard' WHERE id = 8001;
UPDATE transfers SET amount = 300.00 WHERE id = 9001;
UPDATE appointments SET date_time='2023-07-01 09:00:00' WHERE id=1023;


-- Deletion Statements:


DELETE FROM customers WHERE id = 1021;
DELETE FROM accounts WHERE id = 2054;
DELETE FROM transactions WHERE id = 3045;
DELETE FROM branches WHERE id = 4134;
DELETE FROM employees WHERE id = 5561;
DELETE FROM loans WHERE id = 6121;
DELETE FROM loan_payments WHERE id = 7189;
DELETE FROM cards WHERE id = 8897;
DELETE FROM transfers WHERE id = 1056;
DELETE FROM contacts WHERE id=1347;

-- Alter Table Statements:

ALTER TABLE customers ADD email VARCHAR(123);
ALTER TABLE accounts MODIFY balance DECIMAL(10, 2);
ALTER TABLE branches DROP COLUMN manager_id;
ALTER TABLE employees ADD CONSTRAINT fk_branch FOREIGN KEY (branch_id) REFERENCES Branch(branch_id) ON DELETE CASCADE;
ALTER TABLE loan_payments CHANGE COLUMN payment_date payment_date DATE NOT NULL;

-- Join Statement:


SELECT * FROM customers
JOIN accounts ON customers.id = accounts.customer_id
JOIN transactions ON accounts.id = transactions.account_id
JOIN branches ON transactions.branch_id = branches.id
JOIN employees ON branches.id = employees.branch_id
JOIN loans ON accounts.id = loans.account_id
JOIN loan_payments ON loans.id = loan_payments.loan_id
JOIN card ON accounts.id = cards.account_id
JOIN transfers ON accounts.id = transfers.sender_account_id
JOIN appointments ON customers.id = appointments.customer_id
JOIN atm ON branches.id = atm.branch_id;


-- Join Statements with Different Types of Joins:

-- retrieves information about customers and their associated accounts. It selects the customer_id and contact_id from the customers table, and the account_id, account_type, balance, and date_opened from the accounts table.
--  The LEFT JOIN is used to include all customers, even if they don't have any associated accounts.
SELECT c.id, c.contact_id, a.id, a.account_type, a.balance, a.date_opened
FROM customers c
LEFT JOIN accounts a ON c.id = a.customer_id;

-- selects information from the employees and branches tables, using a RIGHT JOIN to join them based on the branch_id.
-- It retrieves the employee_id, contact_id, position, salary, date_hired from the employees table, and the branch_id, branch_name, and branch_address from the Branches table.
SELECT e.id, e.contact_id, e.position, e.salary, e.date_hired, b.id, b.branch_name, b.branch_address
FROM employees e
RIGHT JOIN branches b ON e.branch_id = b.id;

-- selects information from the accounts and transactions tables, using an INNER JOIN to join them based on the account_id. 
-- It retrieves the account_id, account_type, balance, date_opened from the accounts table, and the transaction_id, transaction_type, amount, transaction_date from the Transactions table.
SELECT a.id, a.account_type, a.balance, a.date_opened, t.id, t.transaction_type, t.amount, t.transaction_date
FROM accounts a
INNER JOIN transactions t ON a.id = t.account_id;

-- selects information from the loan_payments and loans tables, using a LEFT JOIN to join them based on the loan_id.
-- It retrieves the payment_id, loan_id, payment_amount, payment_date from the Loan_Payments table, and the loan_amount, interest_rate, loan_duration from the Loans table.
SELECT lp.payment_id, lp.loan_id, lp.payment_amount, lp.payment_date, l.loan_amount, l.interest_rate, l.loan_duration
FROM loan_payments lp
LEFT JOIN loans l ON lp.loan_id = l.loan_id;


-- Aggregate Function Statements with GROUP BY and without HAVING:

-- selects the account_id and the count of transactions for each account from the transactions table. 
-- It uses the GROUP BY clause to group the results by the account_id.
SELECT account_id, COUNT(*) FROM transactions GROUP BY account_id;

-- selects the branch_id and the sum of balances for all accounts in each branch from the accounts table. 
-- It uses the GROUP BY clause to group the results by the branch_id. 
SELECT branch_id, SUM(balance) FROM accounts GROUP BY branch_id;

-- selects the employee_id and the average salary for each employee. 
-- It uses the GROUP BY clause to group the employees by employee_id. This query will calculate the average salary for each employee.
SELECT employee_id, AVG(salary) FROM employees GROUP BY employee_id;

-- selects the card_type and the maximum balance associated with each card type. It joins the accounts and cards tables using the account_id and groups the results by card_type.
-- This query will give the highest balance among all accounts associated with each card type.
SELECT card_type, MAX(balance) FROM accounts JOIN cards ON accounts.id = cards.account_id GROUP BY card_type;

-- selects the account_type and the minimum balance for each account type. 
-- It uses the GROUP BY clause to group the accounts by account_type. This query will provide the smallest balance among all accounts of each account type.
SELECT account_type, MIN(balance) FROM accounts GROUP BY account_type;

-- Aggregate Function Statements with GROUP BY and HAVING:
-- selects the account_id and the sum of the amount for each account from the transactions table. 
-- It uses the GROUP BY clause to group the transactions by account_id. 
-- The HAVING clause is used to filter the results and retrieve only those accounts where the sum of the amount is greater than 1000. 
SELECT account_id, SUM(amount) FROM transactions GROUP BY account_id HAVING SUM(amount) > 1000;

-- selects the card_type and the average balance for each card type from the accounts and card tables. 
-- It joins the tables using the account_id and groups the results by card_type. 
-- The HAVING clause is used to filter the results and retrieve only those card types where the average balance is greater than 5000.
SELECT card_type, AVG(balance) FROM accounts JOIN Card ON accounts.id = card.account_id GROUP BY card_type HAVING AVG(balance) > 5000;
-- selects the loan_id and the maximum loan_amount for each loan from the loans table. 
-- It uses the GROUP BY clause to group the loans by loan_id. 
-- The HAVING clause is used to filter the results and retrieve only those loans where the maximum loan_amount is less than 50000.
SELECT loan_id, MAX(loan_amount) FROM loans GROUP BY loan_id HAVING MAX(loan_amount) < 5000;

-- selects the branch_id and the minimum salary for each branch from the employees table. 
-- It uses the GROUP BY clause to group the employees by branch_id.
-- The HAVING clause is used to filter the results and retrieve only those branches where the minimum salary is greater than 3000. 
SELECT branch_id, MIN(salary) FROM employees GROUP BY branch_id HAVING MIN(salary) > 3000;





