CREATE TABLE `loan_application` (
  `loan_id` bigint NOT NULL COMMENT 'Loan application table id',
  `employee_id` bigint NOT NULL COMMENT 'Employee table id',
  `amount` decimal(8, 2) NOT NULL COMMENT 'Loan application amount',
  `repayment_term` varchar(20) NOT NULL COMMENT 'Loan repayment term',
  `eligibility` varchar(20) NOT NULL COMMENT 'Employee loan eligibility status(tenure based, salary based)',
  `approver_id` bigint NULL COMMENT 'Loan application approver id',
  `status` varchar(15) NOT NULL COMMENT 'Loan application approval status',
  `created_at` datetime NULL COMMENT 'Data created date',
  `updated_at` datetime NULL COMMENT 'Data updated date',
  PRIMARY KEY (`loan_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;