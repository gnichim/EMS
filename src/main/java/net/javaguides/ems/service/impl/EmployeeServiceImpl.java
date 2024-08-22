package net.javaguides.ems.service.impl;

import lombok.AllArgsConstructor;
import net.javaguides.ems.dto.EmployeeDto;
import net.javaguides.ems.entity.Employee;
import net.javaguides.ems.exception.ResourceNotFoundException;
import net.javaguides.ems.mapper.EmployeeMapper;
import net.javaguides.ems.repository.EmployeeRepository;
import net.javaguides.ems.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {

        // Convert employeeDto into employee Jpa Entity to store it in the database

        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
        Employee savedEmployee = employeeRepository.save(employee);

        return EmployeeMapper.mapToEmployeeDto(savedEmployee);
    }

    @Override
    public EmployeeDto getEmployeeById(Long employeeId) {
        Employee foundedEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee does not exist with given id: " + employeeId)
                );

        return EmployeeMapper.mapToEmployeeDto(foundedEmployee);
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        List<Employee> allEmployees = employeeRepository.findAll();
        // Convert list of employee JPA entities into list of employeeDto.
        return allEmployees.stream().map((employee) -> EmployeeMapper.mapToEmployeeDto(employee))
                .collect(Collectors.toList());
        /*return allEmployees.stream().map(EmployeeMapper::mapToEmployeeDto)
                .collect(Collectors.toList());*/
    }

    @Override
    public EmployeeDto updateEmployee(Long employeeId, EmployeeDto updatedEmployee) {

        Employee foundedEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee does not exist with given id: " + employeeId)
                );

        foundedEmployee.setFirstName(updatedEmployee.getFirstName());
        foundedEmployee.setLastName(updatedEmployee.getLastName());
        foundedEmployee.setEmail(updatedEmployee.getEmail());

        Employee updatedEmployeeObj = employeeRepository.save(foundedEmployee);

        return EmployeeMapper.mapToEmployeeDto(updatedEmployeeObj);
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        Employee foundedEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee does not exist with given id: " + employeeId)
                );

        employeeRepository.deleteById(employeeId);
    }
}
