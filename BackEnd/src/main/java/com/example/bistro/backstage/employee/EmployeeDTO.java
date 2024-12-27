package com.example.bistro.backstage.employee;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeDTO {
	private String employeeAccount;
	private String employeePassword;
	private String confirmPassword;
}
