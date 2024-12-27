package com.example.bistro.backstage.employee;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeService {
	@Autowired
	private PasswordEncoder pwdEncoder;//引入加密並綁定
	
	@Autowired
	private EmployeeRepository employeeRepo;
	
	@Autowired
	private JobTitleRepository jobRepo;
	
	public List<Employee> findAllEmployees() {
		List<Employee> allEmployee = employeeRepo.findAll();
		return allEmployee;
	}
	
	public Employee findEmployeeById(Integer id) {
		Optional<Employee> op = employeeRepo.findById(id);
		return op.isPresent() ? op.get() : null;
	}
	
	public Employee findEmployeeByAccount(String employeeAccount) {
		Optional<Employee> op = employeeRepo.findByEmployeeAccount(employeeAccount);
		return op.isPresent() ? op.get() : null;
	}
	
	public String updateEmployee(Employee employeeBean,Integer jobTitleId) {
		Optional<JobTitle> job = jobRepo.findById(jobTitleId);
		if(job.isPresent()) {
			employeeBean.setJobTitle(job.get());
		}else {
			throw new IllegalArgumentException("JobTitle with ID " + jobTitleId + " not found.");
		}
		employeeRepo.save(employeeBean);
		return "更新完成";
	}
	
	public  Optional<Employee> checkLogin(String loginAccount,String loginPassword) {
		 Optional<Employee> employeeData = employeeRepo.findByEmployeeAccount(loginAccount);
		if (employeeData.isPresent()) {
			Employee employeeBean = employeeData.get();
			String passwordData = employeeBean.getEmployeePassword();
			boolean matchResult = pwdEncoder.matches(loginPassword, passwordData);
			if (matchResult) {//result
				return employeeData;
			}else if(passwordData.equals(loginPassword)){
				return employeeData;
			}
		}
		return Optional.empty();
	}
	
	@Transactional
	public Employee insertEmployee(Employee employeeBean,Integer jobTitleId) {
		Optional<JobTitle> job = jobRepo.findById(jobTitleId);
		if(job.isPresent()) {
		employeeBean.setJobTitle(job.get());
		}else {
			throw new IllegalArgumentException("JobTitle with ID " + jobTitleId + " not found.");
		}
		String status="在職";
		employeeBean.setEmployeeSeniority(0);
		employeeBean.setEmployeeStatus(status);
		String password = employeeBean.getEmployeePassword();
		String encodedPwd = pwdEncoder.encode(password);//加密
		employeeBean.setEmployeePassword(encodedPwd);
		Optional<Employee> checkAccount = employeeRepo.findByEmployeeAccount(employeeBean.getEmployeeAccount());
		if(checkAccount.isPresent()) {
			return null;
		}else {
			return employeeRepo.save(employeeBean);
		}
	}
	
	@Transactional
	public Employee changePassword(Employee employeeBean) {
		String password = employeeBean.getEmployeePassword();
		String encodedPwd = pwdEncoder.encode(password);//加密
		employeeBean.setEmployeePassword(encodedPwd);
		return employeeRepo.save(employeeBean);

	}
	
}
