package com.example.bistro.backstage.employee;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepositoryDao extends JpaRepository<Employee, Integer> {
}
