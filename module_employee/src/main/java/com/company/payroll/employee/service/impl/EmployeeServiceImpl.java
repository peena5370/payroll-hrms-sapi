package com.company.payroll.employee.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// import org.springframework.transaction.support.TransactionTemplate;

import com.company.payroll.common.service.DepartmentCommonService;
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
import com.company.payroll.exception.classes.BadRequestException;
import com.company.payroll.exception.classes.ResourceNotFoundException;
import com.company.payroll.util.util.SnowFlakeIdGenerator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
public class EmployeeServiceImpl implements EmployeeService {

    // private final TransactionTemplate transactionTemplate;
    private final SnowFlakeIdGenerator snowFlakeIdGenerator;
    private final EmployeeRepository employeeRepository;
    private final EmployeeBankDetailRepository employeeBankDetailRepository;
    private final EmployeeEmergencyContactRepository employeeEmergencyContactRepository;
    private final DepartmentCommonService departmentCommonService;

    public EmployeeServiceImpl(/* TransactionTemplate transactionTemplate, */
            SnowFlakeIdGenerator snowFlakeIdGenerator,
            EmployeeRepository employeeRepository,
            EmployeeBankDetailRepository employeeBankDetailRepository,
            EmployeeEmergencyContactRepository employeeEmergencyContactRepository,
            DepartmentCommonService departmentCommonService) {
        // this.transactionTemplate = transactionTemplate;
        this.snowFlakeIdGenerator = snowFlakeIdGenerator;
        this.employeeRepository = employeeRepository;
        this.employeeBankDetailRepository = employeeBankDetailRepository;
        this.employeeEmergencyContactRepository = employeeEmergencyContactRepository;
        this.departmentCommonService = departmentCommonService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createEmployeeInfo(EmployeeDTO employeeDTO) {
        if (employeeRepository.existsByIcNumber(employeeDTO.icNumber())) {
            throw new BadRequestException("Employee info with icNumber=" + employeeDTO.icNumber() + " already exist.");
        }

        Employee newEmployee = new Employee();
        newEmployee.setEmployeeId(snowFlakeIdGenerator.nextId());
        newEmployee.setFirstName(employeeDTO.firstName());
        newEmployee.setLastName(employeeDTO.lastName());
        newEmployee.setDateOfBirth(employeeDTO.dateOfBirth());
        newEmployee.setIcNumber(employeeDTO.icNumber());
        newEmployee.setGender(employeeDTO.gender());
        newEmployee.setEmail(employeeDTO.email());
        newEmployee.setPhoneNumber(employeeDTO.phoneNumber());
        newEmployee.setAddressLine1(employeeDTO.addressLine1());
        newEmployee.setAddressLine2(employeeDTO.addressLine2());
        newEmployee.setCity(employeeDTO.city());
        newEmployee.setStateProvince(employeeDTO.state());
        newEmployee.setPostalCode(employeeDTO.postalCode());
        newEmployee.setCountry(employeeDTO.country());
        newEmployee.setHireDate(employeeDTO.hireDate());
        newEmployee.setEmploymentStatus(employeeDTO.employmentStatus());
        newEmployee.setJobTitle(employeeDTO.jobTitle());
        newEmployee.setManagerId(employeeDTO.managerId());
        newEmployee.setCreatedAt(Instant.now());
        newEmployee.setUpdatedAt(null);

        employeeRepository.save(newEmployee);

        Long employeeId = newEmployee.getEmployeeId();

        if ((employeeDTO.bankDetails() != null) && (!employeeDTO.bankDetails().isEmpty())) {
            List<EmployeeBankDetail> newBankDetails = employeeDTO.bankDetails().stream()
                    .map(employeeBankDetailDTO -> {
                        EmployeeBankDetail bankDetail = new EmployeeBankDetail();
                        bankDetail.setBankDetailId(snowFlakeIdGenerator.nextId());
                        bankDetail.setEmployeeId(employeeId);
                        bankDetail.setBankName(employeeBankDetailDTO.bankName());
                        bankDetail.setEncryptedAccountNumber(employeeBankDetailDTO.accountNumber());
                        bankDetail.setBicCode(employeeBankDetailDTO.bicCode());
                        bankDetail.setAccountType(employeeBankDetailDTO.accountType());

                        return bankDetail;
                    })
                    .toList();

            employeeBankDetailRepository.saveAll(newBankDetails);
        }

        if ((employeeDTO.emergencyContacts() != null) && (!employeeDTO.emergencyContacts().isEmpty())) {
            List<EmployeeEmergencyContact> newEmergencyContacts = employeeDTO.emergencyContacts().stream()
                    .map(employeeEmergencyContactDTO -> {
                        EmployeeEmergencyContact emergencyContact = new EmployeeEmergencyContact();
                        emergencyContact.setContactId(snowFlakeIdGenerator.nextId());
                        emergencyContact.setEmployeeId(employeeId);
                        emergencyContact.setContactName(employeeEmergencyContactDTO.contactPersonName());
                        emergencyContact.setRelationship(employeeEmergencyContactDTO.relationship());
                        emergencyContact.setPhoneNumber(employeeEmergencyContactDTO.phoneNumber());
                        emergencyContact.setEmail(employeeEmergencyContactDTO.email());

                        return emergencyContact;
                    })
                    .toList();

            employeeEmergencyContactRepository.saveAll(newEmergencyContacts);
        }
    }

    @Override
    public List<EmployeeInfoDTO> getAllEmployeesByOffsetAndLimit(int offset, int limit) {
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
                                    employeeBankDetail.getAccountType()))
                            .toList();
                }

                List<EmployeeEmergencyContact> emergencyContacts = employeeEmergencyContactRepository
                        .getAllByEmployeeId(employeeId);
                List<EmployeeEmergencyContactDTO> employeeEmergencyContactDTOS = new ArrayList<>();
                if (emergencyContacts != null && !emergencyContacts.isEmpty()) {
                    employeeEmergencyContactDTOS = emergencyContacts.stream()
                            .map(employeeEmergencyContact -> new EmployeeEmergencyContactDTO(
                                    employeeEmergencyContact.getContactName(),
                                    employeeEmergencyContact.getRelationship(),
                                    employeeEmergencyContact.getPhoneNumber(),
                                    employeeEmergencyContact.getEmail()))
                            .toList();
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
                        employeeEmergencyContactDTOS);

                EmployeeInfoDTO employeeInfoDTO = new EmployeeInfoDTO(
                        employeeId,
                        employee.getCreatedAt(),
                        detail);

                result.add(employeeInfoDTO);
            }
        }

        return result;
    }

    @Override
    public EmployeeInfoDTO getEmployeeInfoById(long employeeId) {
        Employee employee = this.employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee info with employeeId=" + employeeId + " not found."));

        long resultEmployeeId = employee.getEmployeeId();

        List<EmployeeBankDetail> bankDetails = employeeBankDetailRepository.getAllByEmployeeId(resultEmployeeId);
        List<EmployeeBankDetailDTO> employeeBankDetailDTOS = new ArrayList<>();
        if (bankDetails != null && !bankDetails.isEmpty()) {
            employeeBankDetailDTOS = bankDetails.stream()
                    .map(employeeBankDetail -> new EmployeeBankDetailDTO(
                            employeeBankDetail.getBankName(),
                            employeeBankDetail.getEncryptedAccountNumber(),
                            employeeBankDetail.getBicCode(),
                            employeeBankDetail.getAccountType()))
                    .toList();
        }

        List<EmployeeEmergencyContact> emergencyContacts = employeeEmergencyContactRepository
                .getAllByEmployeeId(resultEmployeeId);
        List<EmployeeEmergencyContactDTO> employeeEmergencyContactDTOS = new ArrayList<>();
        if (emergencyContacts != null && !emergencyContacts.isEmpty()) {
            employeeEmergencyContactDTOS = emergencyContacts.stream()
                    .map(employeeEmergencyContact -> new EmployeeEmergencyContactDTO(
                            employeeEmergencyContact.getContactName(),
                            employeeEmergencyContact.getRelationship(),
                            employeeEmergencyContact.getPhoneNumber(),
                            employeeEmergencyContact.getEmail()))
                    .toList();
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
                employeeEmergencyContactDTOS);

        return new EmployeeInfoDTO(
                resultEmployeeId,
                employee.getCreatedAt(),
                detail);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEmployeeInfoById(long employeeId, EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee info with employeeId=" + employeeId + " not found."));

        if (employee.getIcNumber() != null && !employee.getIcNumber().equals(employeeDTO.icNumber())
                && employeeRepository.existsByIcNumber(employeeDTO.icNumber())) {
            throw new BadRequestException("Employee info with icNumber=" + employeeDTO.icNumber() + " already exist.");
        }

        employee.setFirstName(employeeDTO.firstName());
        employee.setLastName(employeeDTO.lastName());
        employee.setDateOfBirth(employeeDTO.dateOfBirth());
        employee.setIcNumber(employeeDTO.icNumber());
        employee.setGender(employeeDTO.gender());
        employee.setEmail(employeeDTO.email());
        employee.setPhoneNumber(employeeDTO.phoneNumber());
        employee.setAddressLine1(employeeDTO.addressLine1());
        employee.setAddressLine2(employeeDTO.addressLine2());
        employee.setCity(employeeDTO.city());
        employee.setStateProvince(employeeDTO.state());
        employee.setPostalCode(employeeDTO.postalCode());
        employee.setCountry(employeeDTO.country());
        employee.setHireDate(employeeDTO.hireDate());
        employee.setEmploymentStatus(employeeDTO.employmentStatus());
        employee.setJobTitle(employeeDTO.jobTitle());
        employee.setManagerId(employeeDTO.managerId());
        employee.setUpdatedAt(Instant.now());

        employeeRepository.save(employee);

        Long successEmployeeId = employee.getEmployeeId();

        List<EmployeeBankDetail> existingBankDetails = employeeBankDetailRepository
                .getAllByEmployeeId(successEmployeeId);
        if (!existingBankDetails.isEmpty()) {
            employeeBankDetailRepository.deleteAll(existingBankDetails);
        }

        if (employeeDTO.bankDetails() != null && !employeeDTO.bankDetails().isEmpty()) {
            List<EmployeeBankDetail> newBankDetails = employeeDTO.bankDetails().stream()
                    .map(employeeBankDetailDTO -> {
                        EmployeeBankDetail bankDetail = new EmployeeBankDetail();
                        bankDetail.setBankDetailId(snowFlakeIdGenerator.nextId());
                        bankDetail.setEmployeeId(successEmployeeId);
                        bankDetail.setBankName(employeeBankDetailDTO.bankName());
                        bankDetail.setEncryptedAccountNumber(employeeBankDetailDTO.accountNumber());
                        bankDetail.setBicCode(employeeBankDetailDTO.bicCode());
                        bankDetail.setAccountType(employeeBankDetailDTO.accountType());

                        return bankDetail;
                    })
                    .toList();

            employeeBankDetailRepository.saveAll(newBankDetails);
        }

        List<EmployeeEmergencyContact> existingEmergencyContacts = employeeEmergencyContactRepository
                .getAllByEmployeeId(successEmployeeId);
        if (!existingEmergencyContacts.isEmpty()) {
            employeeEmergencyContactRepository.deleteAll(existingEmergencyContacts);
        }

        if ((employeeDTO.emergencyContacts() != null) && (!employeeDTO.emergencyContacts().isEmpty())) {
            List<EmployeeEmergencyContact> newEmergencyContacts = employeeDTO.emergencyContacts()
                    .stream()
                    .map(employeeEmergencyContactDTO -> {
                        EmployeeEmergencyContact emergencyContact = new EmployeeEmergencyContact();
                        emergencyContact.setContactId(snowFlakeIdGenerator.nextId());
                        emergencyContact.setEmployeeId(successEmployeeId);
                        emergencyContact.setContactName(employeeEmergencyContactDTO.contactPersonName());
                        emergencyContact.setRelationship(employeeEmergencyContactDTO.relationship());
                        emergencyContact.setPhoneNumber(employeeEmergencyContactDTO.phoneNumber());
                        emergencyContact.setEmail(employeeEmergencyContactDTO.email());

                        return emergencyContact;
                    })
                    .toList();

            employeeEmergencyContactRepository.saveAll(newEmergencyContacts);
        }
    }

    @Override
    public void deleteEmployeeInfoById(long employeeId) {
        employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee info with employeeId=" + employeeId + " not found."));

        if (departmentCommonService.isDepartmentEmployeeExist(employeeId)) {

            throw new BadRequestException(
                    "Employee info with employeeId=" + employeeId + " is in used by department, not allow to delete.");
        }

        // TODO: need 2 more checking on promotion, resignation, and also more, either
        // inused, not allow to delete
        employeeRepository.deleteById(employeeId);

        List<EmployeeBankDetail> bankDetails = employeeBankDetailRepository
                .getAllByEmployeeId(employeeId);
        if (!bankDetails.isEmpty()) {
            employeeBankDetailRepository.deleteAll(bankDetails);
        }

        List<EmployeeEmergencyContact> emergencyContacts = employeeEmergencyContactRepository
                .getAllByEmployeeId(employeeId);
        if (!emergencyContacts.isEmpty()) {
            employeeEmergencyContactRepository.deleteAll(emergencyContacts);
        }
    }
}
