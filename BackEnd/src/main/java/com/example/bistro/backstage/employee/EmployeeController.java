package com.example.bistro.backstage.employee;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private JobTitleService jobTitleService;
	
    @GetMapping("/Bistro/Employee/findAllEmployees")
    public String findAllEmployee(Model model) {
    	List<JobTitle> allJobTitle = jobTitleService.findAllJobTitle();
    	List<Employee> allEmployees = employeeService.findAllEmployees();
    	model.addAttribute("allJobTitle",allJobTitle);
    	model.addAttribute("allEmployees",allEmployees);
        return "employee/employeesView";
    }
    @PostMapping("/Bistro/Employee/postemployee")
    public String createEmployee(@ModelAttribute Employee employee,@RequestParam Integer jobId) {
    	Employee employeeData = employeeService.findEmployeeByAccount(employee.getEmployeeAccount());
    	if(employeeData==null) {
    		employeeService.insertEmployee(employee,jobId);
    	}else {
    		System.out.println("員工帳號已存在");
    	}
        return "redirect:/Bistro/Employee/findAllEmployees";
    }
    @PostMapping("/Bistro/Employee/UpdateEmployee")
    public String updateEmployee(@ModelAttribute Employee employee,@RequestParam Integer jobId) {
    	Employee employeeData = employeeService.findEmployeeById(employee.getId());
    	employee.setCreatedAt(employeeData.getCreatedAt());
    	employee.setEmployeeStatus(employeeData.getEmployeeStatus());
    	employee.setEmployeeSeniority(employeeData.getEmployeeSeniority());
    	employeeService.updateEmployee(employee,jobId);
    	return "redirect:/Bistro/Employee/findAllEmployees";
    }
    @PostMapping("/Employee/resetPassword")
    public String resetEmployeePassword(@ModelAttribute EmployeeDTO employee) {
        Employee employeeDate = employeeService.findEmployeeByAccount(employee.getEmployeeAccount());
        if(employeeDate!=null) {
        	employeeDate.setEmployeePassword(employee.getConfirmPassword());
        	employeeService.changePassword(employeeDate);
        }
        return "redirect:/";
    }
    
	
	@PostMapping("/employee/createEmployee")
	@ResponseBody
	public String addEmployee(String employeeAccount,String employeePassword,
			String employeeName,String employeeGender,String employeeBorn,
			String employeeTel,Integer employeeSeniority,Integer employeeSalary,Integer jobtitleId) {
		
		String dateRegex = "^\\d{4}-\\d{2}-\\d{2}$";
		String regex = "^[-]?\\d+$";
		if(!employeeGender.equals("男")&&!employeeGender.equals("女")) {
			return "employeeGender need to be 男 or 女";
		}else if(!employeeBorn.matches(dateRegex)) {
			return "employeeBorn need like 1911-01-01";
		}else if(!employeeTel.matches(regex)) {
			return "employeeTel need to be number,example 09999999 or 06-5777777";
		}
		
		
		Employee employeeBean = new Employee();
		employeeBean.setEmployeeAccount(employeeAccount);
		employeeBean.setEmployeePassword(employeePassword);
		employeeBean.setEmployeeName(employeeName);
		employeeBean.setEmployeeGender(employeeGender);
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date employeeBornDate;
			employeeBornDate = dateFormat.parse(employeeBorn);
			employeeBean.setEmployeeBorn(employeeBornDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		employeeBean.setEmployeeTel(employeeTel);
		employeeBean.setEmployeeSeniority(employeeSeniority);
		employeeBean.setEmployeeSalary(employeeSalary);
				
		Employee result = employeeService.insertEmployee(employeeBean,jobtitleId);
		if(result==null) {
			return "Employee already exists.";
		}else {
			return "Insert employee OK.";
		}
	}
	
    @Transactional
    @PostMapping("/Bistro/Employee/cancel")
    public String cancelMember(@RequestParam Integer id) {
    	Employee employeeBean = employeeService.findEmployeeById(id);
    	String employeeStatus="離職";
    	System.out.println(employeeBean.getEmployeeStatus());
    	employeeBean.setEmployeeStatus(employeeStatus);
    	String result = employeeService.updateEmployee(employeeBean,employeeBean.getJobTitle().getId());
    	System.out.println(result);
    	return "redirect:/Bistro/Employee/findAllEmployees";
    }
    @Transactional
    @PostMapping("/Bistro/Employee/active")
    public String activeMember(@RequestParam Integer id) {
    	Employee employeeBean = employeeService.findEmployeeById(id);
    	String employeeStatus="在職";
    	System.out.println(employeeBean.getEmployeeStatus());
    	employeeBean.setEmployeeStatus(employeeStatus);
    	String result = employeeService.updateEmployee(employeeBean,employeeBean.getJobTitle().getId());
    	System.out.println(result);
    	return "redirect:/Bistro/Employee/findAllEmployees";
    }
    
    @GetMapping("/Employee/changePassword")
    @ResponseBody
    public String changeEmployeePasswords() {
        employeeService.updateAllEmployeePasswords();
        return "Passwords updated successfully";
    }
}
