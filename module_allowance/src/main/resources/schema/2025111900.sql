CREATE TABLE `allowance_type` (
  `allowance_id` bigint NOT NULL COMMENT 'Allowance type table id',
  `type_name` varchar(100) NOT NULL COMMENT 'Allowance type name',
  `description` text NULL COMMENT 'Description for allowance type',
  `is_taxable` tinyint(1) NOT NULL COMMENT 'Allowance type is taxable',
  `recurrence` varchar(15) NOT NULL COMMENT 'Allowance type recurrence frequency(monthly, daily, yearly)',
  `created_at` datetime NULL COMMENT 'Data created date',
  `updated_at` datetime NULL COMMENT 'Data updated date',
  PRIMARY KEY (`allowance_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `allowance_employee` (
  `allowance_eid` bigint NOT NULL COMMENT 'Allowance employee table id',
  `employee_id` bigint NOT NULL COMMENT 'Employee table id',
  `allowance_id` bigint NOT NULL COMMENT 'Allowance type table id',
  `amount` decimal(8, 2) NOT NULL COMMENT 'Amount for the allowance type',
  `formula` text NULL COMMENT 'Formula for calculating the allowance',
  `effective_start_date` datetime NOT NULL COMMENT 'Allowance effective start date',
  `effective_end_date` datetime NULL COMMENT 'Allowance effective end date',
  `created_at` datetime NULL COMMENT 'Data created date',
  `updated_at` datetime NULL COMMENT 'Data updated date',
  PRIMARY KEY (`allowance_eid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `one_time_payment_type` (
  `payment_type_id` bigint NOT NULL COMMENT 'One time payment type table id',
  `type_name` varchar(100) NOT NULL COMMENT 'One time payment type name',
  `description` text NULL COMMENT 'Description for one time payment type',
  `is_taxable` tinyint(1) NOT NULL COMMENT 'One time payment type is taxable',
  `created_at` datetime NULL COMMENT 'Data created date',
  `updated_at` datetime NULL COMMENT 'Data updated date',
  PRIMARY KEY (`payment_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `one_time_payment_employee` (
  `payment_eid` bigint NOT NULL COMMENT 'One time payment employee table id',
  `employee_id` bigint NOT NULL COMMENT 'Employee table id',
  `payment_type_id` bigint NOT NULL COMMENT 'One time payment type table id',
  `amount` decimal(8, 2) NOT NULL COMMENT 'Amount for the one time payment',
  `approval_date` datetime NULL COMMENT 'One time payment approval date',
  `scheduled_payment_date` datetime NOT NULL COMMENT 'Payment schedule date',
  `payment_status` varchar(20) NOT NULL COMMENT 'One time payment status',
  `created_at` datetime NULL COMMENT 'Data created date',
  `updated_at` datetime NULL COMMENT 'Data updated date',
  PRIMARY KEY (`payment_eid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;