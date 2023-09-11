package test;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import io.restassured.response.Response;
import utils.CommonUtils;
public class APITest {
	
	static final Logger logger = LogManager.getLogger(APITest.class.getName());
	CommonUtils commonUtils = new CommonUtils();
	 
	Map<String, String[]> employeedata = new HashMap<>();
	
	
	@Test(priority=1)
	 @Parameters ({"id", "firstname", "lastname"})
	public void VerifyEmpolyeeDetailsTest(String id, String firstname, String lastname)
	{
		logger.info("Verifying Employee's Firstname and last by Employee ID.................!!!");
		String response =commonUtils.ListEmployees();
	 
		logger.info("Employee details has been traced" +response);
		 
		JSONObject JO = new JSONObject(response);		
		employeedata =commonUtils.PopulateResonseToHashmap(employeedata,  JO);
		boolean status = commonUtils.VerifyEmployeeDetails(employeedata,id ,firstname, lastname); 
		Assert.assertEquals(status, true);
	}
	
	 @Test(priority=2)
	 @Parameters ({"employeename", "jobtitle"})
	public void CreateEmployeeTest(String employeename, String jobtitle)
	{
		 Response response =commonUtils.CreateEmployee(employeename, jobtitle);
		 logger.info( response.prettyPrint());
		 Assert.assertEquals(response.statusCode(), 201);
		 
		 
	}
	


	  
	 
	
	
	
	
	
	
	
	
	

	   
 
	    	 

}
