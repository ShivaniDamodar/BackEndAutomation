package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
 

public class CommonUtils

{
	static final Logger logger = LogManager.getLogger(CommonUtils.class.getName());
	GeneratePayload generatePayload = new GeneratePayload() ;


	public String ListEmployees() {
		RestAssured.baseURI = getPropertyValue("GET_url");
		String response = RestAssured.
				given().
					param("page", 2).
				when().
					get("/api/users").
				then().extract().asPrettyString();
		
		return response;
	}
	
	
	
	public Response CreateEmployee(String employeename, String jobtitle)
	 {
		RestAssured.baseURI = getPropertyValue("POST_url");
		Response response =RestAssured.
		given().contentType(ContentType.JSON).
		body(generatePayload.GeneratePayloadForEmployee(employeename, jobtitle)).
		when().
		post().
		then().extract().response() ;
		return response;
	
	 }

	public Map<String, String[]> PopulateResonseToHashmap(Map<String, String[]> employeedata, JSONObject jsonObject) {
		employeedata = new HashMap<>();
		for (int i = 0; i < jsonObject.getJSONArray("data").length(); i++)

		{
			String id = jsonObject.getJSONArray("data").getJSONObject(i).get("id").toString();
			String FirstName = jsonObject.getJSONArray("data").getJSONObject(i).get("first_name").toString();
			String LastName = jsonObject.getJSONArray("data").getJSONObject(i).get("last_name").toString();

			employeedata.put(id, new String[] { FirstName, LastName });
		}

		return employeedata;
	}

	public boolean VerifyEmployeeDetails(Map<String, String[]> employeedata, String id, String firstname,
			String lastname) {
		boolean isIdPresent = false;
		boolean status = false;
		for (Map.Entry<String, String[]> entry : employeedata.entrySet()) {
			{
				String employeeId = entry.getKey();
				if (employeeId.contentEquals(id)) {
					isIdPresent = true;
					String[] actualName = entry.getValue();
					String actualFirstName = actualName[0];
					String actualLastName = actualName[1];
					 
					logger.info("Actual Emp Details: id-" + employeeId + " " + " FirstName-" + actualFirstName
							+ " LastName-" + actualLastName);
					if (actualFirstName.equalsIgnoreCase(firstname) && actualLastName.equalsIgnoreCase(lastname)) {
						status = true;
					}

				}

			}

		}

		if (!isIdPresent) {
			logger.info("Id is  not present");
			 
		} else if (status) {
			logger.info(
					"Expected FirstName-" + "" + firstname + " & LastName-" + lastname + ":Details are matching");
			 
		} else {
			logger.info(
					"Expected FirstName-" + "" + firstname + " & LastName-" + lastname + "  Details are not matching");
			
		}

		return status;

	}
	
	public String getPropertyValue(String key) {

		Properties prop = new Properties();
		FileInputStream fis;
		try {
			fis = new FileInputStream(
					"./src/test/resources/config/env.properties");
			prop.load(fis);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		

		return prop.getProperty(key);

	}

}
