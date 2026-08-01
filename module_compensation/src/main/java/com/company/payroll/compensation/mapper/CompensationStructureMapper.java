package com.company.payroll.compensation.mapper;

import java.time.Clock;
import java.time.Instant;

import org.springframework.stereotype.Component;

import com.company.payroll.compensation.dto.CompensationStructureDTO;
import com.company.payroll.compensation.dto.CompensationStructureDetailDTO;
import com.company.payroll.compensation.model.CompensationStructure;

@Component
public class CompensationStructureMapper {

    private final Clock clock;

    // Inject Clock bean for testable timestamp generation
    public CompensationStructureMapper(Clock clock) {
        this.clock = clock;
    }
    
    public CompensationStructureDTO toDto(CompensationStructure entity) {
        return new CompensationStructureDTO(
                entity.getEmployeeId(),
                entity.getBaseAnnualSalary(),
                entity.getPaymentFrequency(),
                entity.getEffectiveDate(),
                entity.getActive(),
                entity.getEpfEmployeeRate(),
                entity.getEpfEmployerRate(),
                entity.getSocsoGroup()
        );
    }

    public CompensationStructureDetailDTO toDetailDto(CompensationStructure entity) {
        return new CompensationStructureDetailDTO(
                entity.getCompensationId(),
                entity.getCreatedAt(),
                toDto(entity)
        );
    }

    public CompensationStructure toEntity(CompensationStructureDTO dto, long generatedId) {
        CompensationStructure entity = new CompensationStructure();
        entity.setCompensationId(generatedId);
        entity.setEmployeeId(dto.employeeId());
        
        // Map common mutable fields
        mapDtoToEntityFields(dto, entity);
        
        entity.setCreatedAt(Instant.now(clock));
        return entity;
    }

    public void updateEntityFromDto(CompensationStructureDTO dto, CompensationStructure entity) {
        mapDtoToEntityFields(dto, entity);
        entity.setUpdatedAt(Instant.now(clock));
    }

    // Helper method to avoid duplicating field assignment logic
    private void mapDtoToEntityFields(CompensationStructureDTO dto, CompensationStructure entity) {
        entity.setBaseAnnualSalary(dto.baseAnnualSalary());
        entity.setPaymentFrequency(dto.paymentFrequency());
        entity.setEffectiveDate(dto.effectiveDate());
        entity.setActive(dto.isActive());
        entity.setEpfEmployeeRate(dto.epfEmployeeRate());
        entity.setEpfEmployerRate(dto.epfEmployerRate());
        entity.setSocsoGroup(dto.socsoGroup());
    }
}
