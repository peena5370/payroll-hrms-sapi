CREATE TABLE `compensation_structure` (
  `compensation_id` bigint NOT NULL COMMENT 'Compensation structure table id',
  `employee_id` bigint NOT NULL COMMENT 'Employee table id',
  `base_annual_salary` decimal(8, 2) NOT NULL COMMENT 'Employee base annual salary',
  `payment_frequency` varchar(10) NOT NULL COMMENT 'Payment frequency for salary',
  `effective_date` datetime NOT NULL COMMENT 'Salary effective date',
  `is_active` tinyint(1) NOT NULL COMMENT 'Compensation structure active status',
  `epf_employee_rate` decimal(4, 2) NULL COMMENT 'Employee EPF rate, default is null',
  `epf_employer_rate` decimal(4, 2) NULL COMMENT 'Employer EPF rate, default is null',
  `socso_group` varchar(32) NOT NULL COMMENT 'SOCSO group id',
  `created_at` datetime NULL COMMENT 'Data created date',
  `updated_at` datetime NULL COMMENT 'Data updated date',
  PRIMARY KEY (`compensation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;