package edu.lawrence.friendfinder;

// In-project includes [DTOs]
import edu.lawrence.friendfinder.interfaces.dtos.UserDTO;

// Hamcrest-level includes [Test Helpers]
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

// Jupiter-level includes [Class Annotations]
import org.junit.jupiter.api.TestMethodOrder;
/** import org.junit.jupiter.api.Order; */

// Jupiter-level includes [Function Annotations]
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

// Rest-level includes [Test Helpers]
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;

// Spring-level includes [Class Annotations]
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = Cmsc455FinalApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(1)
public class UserRegistrationTests {
	
	private static UserDTO userA;
	
	@BeforeAll
	public static void setup() {
		RestAssured.port = 8085;
		RestAssured.baseURI = "http://localhost";
		
		userA = new UserDTO();
		userA.setUsername("UserA");
		userA.setPassword("password");
	}
	
	@Test
	@Order(1)
	public void testPostUser() {
		given()
		.contentType("application/json")
		.body(userA)
		.when().post("/users")
		.then()
		.statusCode(anyOf(is(HttpStatus.CREATED.value()), is(HttpStatus.CONFLICT.value())));
	
		
		// Failure states -----------------------------------------------------------------
		
		given() // Duplicate user
		.contentType("application/json")
		.body(userA)
		.when().post("/users")
		.then()
		.statusCode(HttpStatus.CONFLICT.value());
		
		given() // Missing password
		.contentType("application/json")
		.body(
				"{\n"
				+ "\"username\" : \"badUserUsername\",\n"
				+ "\"password\" : \"\"\n"
				+ "}")
		.when().post("/users")
		.then()
		.statusCode(HttpStatus.BAD_REQUEST.value());

		given() // Missing username
		.contentType("application/json")
		.body(
				"{\n"
				+ "\"username\" : \"\",\n"
				+ "\"password\" : \"badUserPassword\"\n"
				+ "}")
		.when().post("/users")
		.then()
		.statusCode(HttpStatus.BAD_REQUEST.value());
	}
	
	// Add profile tests here
	
}