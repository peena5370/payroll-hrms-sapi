package com.company.payroll.employee.service.impl;

import com.company.payroll.common.DepartmentCommonService;
import com.company.payroll.employee.dto.EmployeeBankDetailDTO;
import com.company.payroll.employee.dto.EmployeeDTO;
import com.company.payroll.employee.dto.EmployeeEmergencyContactDTO;
import com.company.payroll.employee.dto.EmployeeInfoDTO;
import com.company.payroll.employee.model.Employee;
import com.company.payroll.employee.model.EmployeeBankDetail;
import com.company.payroll.employee.model.EmployeeEmergencyContact;
import com.company.payroll.employee.repository.EmployeeBankDetailRepository;
import com.company.payroll.employee.repository.EmployeeEmergencyContactRepository;
import com.company.payroll.employee.repository.EmployeeRepository;
import com.company.payroll.employee.service.EmployeeService;
import com.company.payroll.util.util.SnowFlakeIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final String CLASS_NAME = "[EmployeeServiceImpl]";

    private final TransactionTemplate transactionTemplate;
    private final SnowFlakeIdGenerator snowFlakeIdGenerator;
    private final EmployeeRepository employeeRepository;
    private final EmployeeBankDetailRepository employeeBankDetailRepository;
    private final EmployeeEmergencyContactRepository employeeEmergencyContactRepository;
    private final DepartmentCommonService departmentCommonService;

    public EmployeeServiceImpl(TransactionTemplate transactionTemplate,
                               SnowFlakeIdGenerator snowFlakeIdGenerator,
                               EmployeeRepository employeeRepository,
                               EmployeeBankDetailRepository employeeBankDetailRepository,
                               EmployeeEmergencyContactRepository employeeEmergencyContactRepository,
                               DepartmentCommonService departmentCommonService) {
        this.transactionTemplate = transactionTemplate;
        this.snowFlakeIdGenerator = snowFlakeIdGenerator;
        this.employeeRepository = employeeRepository;
        this.employeeBankDetailRepository = employeeBankDetailRepository;
        this.employeeEmergencyContactRepository = employeeEmergencyContactRepository;
        this.departmentCommonService = departmentCommonService;
    }

    @Override
    public int createEmployeeInfo(EmployeeDTO employeeDTO) {
        final String functionName = Thread.currentThread().getStackTrace()[2].getMethodName();
        log.info("{} {} start.", CLASS_NAME, functionName);

        Integer status = transactionTemplate.execute(transactionStatus -> {
            try {
                Optional<Long> existingEmployeeId = employeeRepository.findIdByIcNumber(employeeDTO.icNumber());

                if (existingEmployeeId.isPresent()) {
                    log.info("{} {} employee info exist.", CLASS_NAME, functionName);
                    transactionStatus.setRollbackOnly();
                    return -2;
                }

                Employee newEmployee = new Employee(
                        snowFlakeIdGenerator.nextId(),
                        employeeDTO.firstName(),
                        employeeDTO.lastName(),
                        employeeDTO.dateOfBirth(),
                        employeeDTO.icNumber(),
                        employeeDTO.gender(),
                        employeeDTO.email(),
                        employeeDTO.phoneNumber(),
                        employeeDTO.addressLine1(),
                        employeeDTO.addressLine2(),
                        employeeDTO.city(),
                        employeeDTO.state(),
                        employeeDTO.postalCode(),
                        employeeDTO.country(),
                        employeeDTO.hireDate(),
                        employeeDTO.employmentStatus(),
                        employeeDTO.jobTitle(),
                        employeeDTO.managerId(),
                        LocalDateTime.now(),
                        null
                );

                Employee createdEmployee = employeeRepository.save(newEmployee);
                long employeeId = createdEmployee.getEmployeeId();

                if ((employeeDTO.bankDetails() != null) && (!employeeDTO.bankDetails().isEmpty())) {
                    List<EmployeeBankDetail> newBankDetails = employeeDTO.bankDetails().stream()
                            .map(employeeBankDetailDTO -> new EmployeeBankDetail(
                                    snowFlakeIdGenerator.nextId(),
                                    employeeId,
                                    employeeBankDetailDTO.bankName(),
                                    employeeBankDetailDTO.accountNumber(),
                                    employeeBankDetailDTO.bicCode(),
                                    employeeBankDetailDTO.accountType()
                            )).toList();

                    employeeBankDetailRepository.saveAll(newBankDetails);
                }

                if ((employeeDTO.emergencyContacts() != null) && (!employeeDTO.emergencyContacts().isEmpty())) {
                    List<EmployeeEmergencyContact> newEmergencyContacts = employeeDTO.emergencyContacts().stream()
                            .map(employeeEmergencyContactDTO -> new EmployeeEmergencyContact(
                                    snowFlakeIdGenerator.nextId(),
                                    employeeId,
                                    employeeEmergencyContactDTO.contactPersonName(),
                                    employeeEmergencyContactDTO.relationship(),
                                    employeeEmergencyContactDTO.phoneNumber(),
                                    employeeEmergencyContactDTO.email()
                            )).toList();

                    employeeEmergencyContactRepository.saveAll(newEmergencyContacts);
                }

                return 1;
            } catch (Exception e) {
                log.error("{} {} exception occurred for transaction. Message={}", CLASS_NAME, functionName, e.getMessage());

                transactionStatus.setRollbackOnly();
                return -1;
            }
        });

        int finalStatus = Optional.ofNullable(status).orElse(0);

        log.info("{} {} end. Status={}", CLASS_NAME, functionName, finalStatus);
        return finalStatus;
    }

    @Override
    public List<EmployeeInfoDTO> getAllEmployeesByOffsetAndLimit(int offset, int limit) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start.", CLASS_NAME, functionName);

        List<EmployeeInfoDTO> result = new ArrayList<>();

        Sort sort = Sort.by("employeeId").ascending();
        PageRequest pageRequest = PageRequest.of(offset, limit, sort);

        List<Employee> employees = employeeRepository.findAll(pageRequest).getContent();

        if (!employees.isEmpty()) {
            for (Employee employee : employees) {
                long employeeId = employee.getEmployeeId();

                List<EmployeeBankDetail> bankDetails = employeeBankDetailRepository.getAllByEmployeeId(employeeId);
                List<EmployeeBankDetailDTO> employeeBankDetailDTOS = new ArrayList<>();
                if (bankDetails != null && !bankDetails.isEmpty()) {
                    employeeBankDetailDTOS = bankDetails.stream()
                            .map(employeeBankDetail -> new EmployeeBankDetailDTO(
                                    employeeBankDetail.getBankName(),
                                    employeeBankDetail.getEncryptedAccountNumber(),
                                    employeeBankDetail.getBicCode(),
                                    employeeBankDetail.getAccountType()
                            )).toList();
                }

                List<EmployeeEmergencyContact> emergencyContacts = employeeEmergencyContactRepository.getAllByEmployeeId(employeeId);
                List<EmployeeEmergencyContactDTO> employeeEmergencyContactDTOS = new ArrayList<>();
                if (emergencyContacts != null && !emergencyContacts.isEmpty()) {
                    employeeEmergencyContactDTOS = emergencyContacts.stream()
                            .map(employeeEmergencyContact -> new EmployeeEmergencyContactDTO(
                                    employeeEmergencyContact.getContactName(),
                                    employeeEmergencyContact.getRelationship(),
                                    employeeEmergencyContact.getPhoneNumber(),
                                    employeeEmergencyContact.getEmail()
                            )).toList();
                }

                EmployeeDTO detail = new EmployeeDTO(
                        employee.getFirstName(),
                        employee.getLastName(),
                        employee.getDateOfBirth(),
                        employee.getIcNumber(),
                        employee.getGender(),
                        employee.getEmail(),
                        employee.getPhoneNumber(),
                        employee.getAddressLine1(),
                        employee.getAddressLine2(),
                        employee.getCity(),
                        employee.getStateProvince(),
                        employee.getPostalCode(),
                        employee.getCountry(),
                        employee.getHireDate(),
                        employee.getEmploymentStatus(),
                        employee.getJobTitle(),
                        employee.getManagerId(),
                        employeeBankDetailDTOS,
                        employeeEmergencyContactDTOS
                );

                EmployeeInfoDTO employeeInfoDTO = new EmployeeInfoDTO(
                        employeeId,
                        employee.getCreatedAt(),
                        detail
                );

                result.add(employeeInfoDTO);
            }
        }

        log.info("{} {} end. Result size={}", CLASS_NAME, functionName, result.size());
        return result;
    }

    @Override
    public Optional<EmployeeInfoDTO> getEmployeeInfoById(long employeeId) {
        final String functionName = Thread.currentThread().getStackTrace()[2].getMethodName();

        log.info("{} {} start. employeeId={}", CLASS_NAME, functionName, employeeId);

        Optional<Employee> employee = this.employeeRepository.findById(employeeId);

        if (employee.isPresent()) {
            Employee result = employee.get();
            long resultEmployeeId = result.getEmployeeId();

            List<EmployeeBankDetail> bankDetails = employeeBankDetailRepository.getAllByEmployeeId(resultEmployeeId);
            List<EmployeeBankDetailDTO> employeeBankDetailDTOS = new ArrayList<>();
            if (bankDetails != null && !bankDetails.isEmpty()) {
                employeeBankDetailDTOS = bankDetails.stream()
                        .map(employeeBankDetail -> new EmployeeBankDetailDTO(
                                employeeBankDetail.getBankName(),
                                employeeBankDetail.getEncryptedAccountNumber(),
                                employeeBankDetail.getBicCode(),
                                employeeBankDetail.getAccountType()
                        )).toList();
            }

            List<EmployeeEmergencyContact> emergencyContacts = employeeEmergencyContactRepository.getAllByEmployeeId(resultEmployeeId);
            List<EmployeeEmergencyContactDTO> employeeEmergencyContactDTOS = new ArrayList<>();
            if (emergencyContacts != null && !emergencyContacts.isEmpty()) {
                employeeEmergencyContactDTOS = emergencyContacts.stream()
                        .map(employeeEmergencyContact -> new EmployeeEmergencyContactDTO(
                                employeeEmergencyContact.getContactName(),
                                employeeEmergencyContact.getRelationship(),
                                employeeEmergencyContact.getPhoneNumber(),
                                employeeEmergencyContact.getEmail()
                        )).toList();
            }

            EmployeeDTO detail = new EmployeeDTO(
                    result.getFirstName(),
                    result.getLastName(),
                    result.getDateOfBirth(),
                    result.getIcNumber(),
                    result.getGender(),
                    result.getEmail(),
                    result.getPhoneNumber(),
                    result.getAddressLine1(),
                    result.getAddressLine2(),
                    result.getCity(),
                    result.getStateProvince(),
                    result.getPostalCode(),
                    result.getCountry(),
                    result.getHireDate(),
                    result.getEmploymentStatus(),
                    result.getJobTitle(),
                    result.getManagerId(),
                    employeeBankDetailDTOS,
                    employeeEmergencyContactDTOS
            );

            log.info("{} {} success for employeeId={}.", CLASS_NAME, functionName, employeeId);
            return Optional.of(new EmployeeInfoDTO(
                    resultEmployeeId,
                    result.getCreatedAt(),
                    detail
            ));
        }

        log.info("{} {} fail. Employee info with employeeId={} not exist.", CLASS_NAME, functionName, employeeId);
        return Optional.empty();
    }

    @Override
    public int updateEmployeeInfoById(long employeeId, EmployeeDTO employeeDTO) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. employeeId={}", CLASS_NAME, functionName, employeeId);

        Integer status = transactionTemplate.execute(transactionStatus -> {
            try {
                Optional<Employee> employee = employeeRepository.findById(employeeId);

                if (employee.isEmpty()) {
                    log.info("{} {} not found for employeeId={}", CLASS_NAME, functionName, employeeId);
                    transactionStatus.setRollbackOnly();
                    return -2;
                } else {
                    Optional<Long> duplicateIcNumberEmployeeId = employeeRepository.findIdByIcNumber(employeeDTO.icNumber());

                    if ((duplicateIcNumberEmployeeId.isPresent()) && (employeeId != duplicateIcNumberEmployeeId.get())) {
                        log.info("{} {} duplicated ic number for employeeId={} with other employee", CLASS_NAME, functionName, employeeId);
                        transactionStatus.setRollbackOnly();
                        return -3;
                    } else {
                        Employee updateEmployee = employee.get();
                        updateEmployee.setFirstName(employeeDTO.firstName());
                        updateEmployee.setLastName(employeeDTO.lastName());
                        updateEmployee.setDateOfBirth(employeeDTO.dateOfBirth());
                        updateEmployee.setIcNumber(employeeDTO.icNumber());
                        updateEmployee.setGender(employeeDTO.gender());
                        updateEmployee.setEmail(employeeDTO.email());
                        updateEmployee.setPhoneNumber(employeeDTO.phoneNumber());
                        updateEmployee.setAddressLine1(employeeDTO.addressLine1());
                        updateEmployee.setAddressLine2(employeeDTO.addressLine2());
                        updateEmployee.setCity(employeeDTO.city());
                        updateEmployee.setStateProvince(employeeDTO.state());
                        updateEmployee.setPostalCode(employeeDTO.postalCode());
                        updateEmployee.setCountry(employeeDTO.country());
                        updateEmployee.setHireDate(employeeDTO.hireDate());
                        updateEmployee.setEmploymentStatus(employeeDTO.employmentStatus());
                        updateEmployee.setJobTitle(employeeDTO.jobTitle());
                        updateEmployee.setManagerId(employeeDTO.managerId());
                        updateEmployee.setUpdatedAt(LocalDateTime.now());

                        Employee successUpdateEmployee = employeeRepository.save(updateEmployee);
                        Long successEmployeeId = successUpdateEmployee.getEmployeeId();

                        List<EmployeeBankDetail> existingBankDetails = employeeBankDetailRepository.getAllByEmployeeId(successEmployeeId);
                        if (!existingBankDetails.isEmpty()) {
                            employeeBankDetailRepository.deleteAll(existingBankDetails);
                        }

                        if (employeeDTO.bankDetails() != null && !employeeDTO.bankDetails().isEmpty()) {
                            List<EmployeeBankDetail> newBankDetails = employeeDTO.bankDetails().stream()
                                    .map(employeeBankDetailDTO -> new EmployeeBankDetail(
                                            snowFlakeIdGenerator.nextId(),
                                            successEmployeeId,
                                            employeeBankDetailDTO.bankName(),
                                            employeeBankDetailDTO.accountNumber(),
                                            employeeBankDetailDTO.bicCode(),
                                            employeeBankDetailDTO.accountType()
                                    )).toList();

                            employeeBankDetailRepository.saveAll(newBankDetails);
                        }

                        List<EmployeeEmergencyContact> existingEmergencyContacts = employeeEmergencyContactRepository.getAllByEmployeeId(successEmployeeId);
                        if (!existingEmergencyContacts.isEmpty()) {
                            employeeEmergencyContactRepository.deleteAll(existingEmergencyContacts);
                        }

                        if ((employeeDTO.emergencyContacts() != null) && (!employeeDTO.emergencyContacts().isEmpty())) {
                            List<EmployeeEmergencyContact> newEmergencyContacts = employeeDTO.emergencyContacts().stream()
                                    .map(employeeEmergencyContactDTO -> new EmployeeEmergencyContact(
                                            snowFlakeIdGenerator.nextId(),
                                            successEmployeeId,
                                            employeeEmergencyContactDTO.contactPersonName(),
                                            employeeEmergencyContactDTO.relationship(),
                                            employeeEmergencyContactDTO.phoneNumber(),
                                            employeeEmergencyContactDTO.email()
                                    )).toList();

                            employeeEmergencyContactRepository.saveAll(newEmergencyContacts);
                        }

                        return 1;
                    }
                }
            } catch (Exception e) {
                log.error("{} {} exception occurred for transaction. Message={}", CLASS_NAME, functionName, e.getMessage());

                transactionStatus.setRollbackOnly();
                return -1;
            }
        });

        int finalStatus = Optional.ofNullable(status).orElse(0);

        log.info("{} {} end. employeeId={}, status={}", CLASS_NAME, functionName, employeeId, finalStatus);
        return finalStatus;
    }

    @Override
    public int deleteEmployeeInfoById(long employeeId) {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} start. employeeId={}", CLASS_NAME, functionName, employeeId);

        Integer status = transactionTemplate.execute(transactionStatus -> {

            try {
                Optional<Employee> employee = employeeRepository.findById(employeeId);

                if (employee.isPresent()) {
                    boolean isInUsedByDepartment = departmentCommonService.isDepartmentEmployeeExist(employeeId);

                    // TODO: need 2 more checking on promotion, resignation, and also more, either inused, not allow to delete
                    if(isInUsedByDepartment) {
                        log.info("{} {} for employeeId={} is in used, not allow to delete.", new Object[]{CLASS_NAME, functionName, employeeId});
                        return -2;
                    } else {
                        employeeRepository.delete(employee.get());

                        List<EmployeeBankDetail> bankDetails = employeeBankDetailRepository.getAllByEmployeeId(employeeId);
                        if (!bankDetails.isEmpty()) {
                            employeeBankDetailRepository.deleteAll(bankDetails);
                        }

                        List<EmployeeEmergencyContact> emergencyContacts = employeeEmergencyContactRepository.getAllByEmployeeId(employeeId);
                        if (!emergencyContacts.isEmpty()) {
                            employeeEmergencyContactRepository.deleteAll(emergencyContacts);
                        }

                        return 1;
                    }
                } else {
                    log.info("{} {} for employeeId={} not found.", CLASS_NAME, functionName, employeeId);

                    transactionStatus.setRollbackOnly();
                    return -1;
                }
            } catch (Exception e) {
                log.error("{} {} exception occurred for transaction. Message={}", CLASS_NAME, functionName, e.getMessage());

                transactionStatus.setRollbackOnly();
                return -3;
            }
        });

        int finalStatus = Optional.ofNullable(status).orElse(0);
        log.info("{} {} end. employeeId={}, Status={}", CLASS_NAME, functionName, employeeId, finalStatus);
        return finalStatus;
    }
}
