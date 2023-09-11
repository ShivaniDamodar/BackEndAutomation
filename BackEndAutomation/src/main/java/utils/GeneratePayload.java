package utils;

import pojo.Employee;

public class GeneratePayload {
	
	
	public Employee GeneratePayloadForEmployee(String employeename,String jobtitle)
	{
		Employee employee = new Employee();
		employee.setName(employeename);
		
		employee.setJob(jobtitle);
		
		return employee;
		
	}

}
