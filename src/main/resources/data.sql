-- Supprimer toutes les lignes des tables
DELETE FROM account;
DELETE FROM credit_card;

DELETE FROM beneficiary;


-- Insérer 10 comptes liés à une carte de crédit existante
INSERT INTO credit_card (credit_card_number) VALUES
('CC00120231221'),
('CC00120231222'),
('CC00120231223'),
('CC00120231224'),
('CC00120231225'),
('CC00120231226'),
('CC00120231227'),
('CC00120231228'),
('CC00120231229'),
('CC00120231230');




-- Insérer 10 comptes liés à une carte de crédit existante
INSERT INTO account (owner,TOTAL_BENEFITS, account_number, credit_card_id)
SELECT
    'John Doe',0.0,  'AN001', id FROM credit_card WHERE credit_card_number = 'CC00120231221'
UNION ALL SELECT
    'Alice Smith',0.0,  'AN002', id FROM credit_card WHERE credit_card_number = 'CC00120231222'
UNION ALL SELECT
    'Bob Johnson',0.0,  'AN003', id FROM credit_card WHERE credit_card_number = 'CC00120231223'
UNION ALL SELECT
    'Emily Davis',0.0,  'AN004', id FROM credit_card WHERE credit_card_number = 'CC00120231224'
UNION ALL SELECT
    'Michael Wilson',0.0,  'AN005', id FROM credit_card WHERE credit_card_number = 'CC00120231225'
UNION ALL SELECT
    'Sophia Martinez',0.0,  'AN006', id FROM credit_card WHERE credit_card_number = 'CC00120231226'
UNION ALL SELECT
    'William Anderson',0.0,  'AN007', id FROM credit_card WHERE credit_card_number = 'CC00120231227'
UNION ALL SELECT
    'Olivia Taylor',0.0,  'AN008', id FROM credit_card WHERE credit_card_number = 'CC00120231228'
UNION ALL SELECT
    'James Thomas',0.0,  'AN009', id FROM credit_card WHERE credit_card_number = 'CC00120231229'
UNION ALL SELECT
    'Charlotte Garcia',0.0,  'AN010', id FROM credit_card WHERE credit_card_number = 'CC00120231230';

    
    
    
  
-- Insérer trois bénéficiaires supplémentaires à chaque compte existant avec une somme de pourcentages égale à 100%
INSERT INTO beneficiary (name,SAVINGS , allocation_percentage, account_id)
SELECT
    CONCAT('Beneficiary ', account.id, '.1'), 0.0,  0.05 * 100, account.id FROM account
UNION ALL SELECT
    CONCAT('Beneficiary ', account.id, '.2'), 0.0, 0.07 * 100, account.id FROM account
UNION ALL SELECT
    CONCAT('Beneficiary ', account.id, '.3'), 0.0, 0.1 * 100, account.id FROM account
