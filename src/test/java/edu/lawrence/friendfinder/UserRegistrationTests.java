package edu.lawrence.friendfinder;

// In-project includes [DTOs]
import edu.lawrence.friendfinder.interfaces.dtos.ProfileDTO;
import edu.lawrence.friendfinder.interfaces.dtos.UserDTO;

// Hamcrest-level includes [Test Helpers]
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

// Jupiter-level includes [Class Annotations]
import org.junit.jupiter.api.TestMethodOrder;

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
public class UserRegistrationTests {
	
	private static UserDTO userA;
	private static String tokenA;
	private static ProfileDTO profileA;
	
	@BeforeAll
	public static void setup() {
		RestAssured.port = 8085;
		RestAssured.baseURI = "http://localhost";
		
		userA = new UserDTO();
		userA.setUsername("UserA");
		userA.setPassword("password");

		profileA = new ProfileDTO();
		profileA.setFullname("Test User");
		profileA.setEmailaddress("user@test.com");
		profileA.setCountrycode(1);
		profileA.setPhonenumber("4444444444");
		profileA.setBio("I am a test user");
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
	}
	
	@Test
	@Order(2)
	public void testPostUserFailureDuplicate() {
		given() // Duplicate user
		.contentType("application/json")
		.body(userA)
		.when().post("/users")
		.then()
		.statusCode(HttpStatus.CONFLICT.value());
	}
	
	@Test
	@Order(3)
	public void testPostUserFailureMissingPassword() {
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
	}
	
	@Test
	@Order(4)
	public void testPostUserFailureMissingUsername() {
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
	
	@Test
	@Order(5)
	public void testLogin() {
		tokenA = given()
				.contentType("application/json")
				.body(userA)
				.when().post("/users/login")
				.then()
				.statusCode(200)
				.extract().path("token");
	}

	@Test
	@Order(6)
	public void testLoginFailureBadPassword() {
		given() // User exists in db but wrong password
		.contentType("application/json")
		.body(
				"{\n"
				+ "\"username\" : \"UserA\",\n"
				+ "\"password\" : \"badpassword\"\n"
				+ "}")
		.when().post("/users/login")
		.then()
		.statusCode(HttpStatus.UNAUTHORIZED.value());
	}
	
	@Test
	@Order(7)
	public void testLoginFailureNonUser() {
		given() // User doesn't exist in db
		.contentType("application/json")
		.body(
				"{\n"
				+ "\"username\" : \"BadUser\",\n"
				+ "\"password\" : \"badpassword\"\n"
				+ "}")
		.when().post("/users/login")
		.then()
		.statusCode(HttpStatus.UNAUTHORIZED.value());
	}
	
	@Test
	@Order(8)
	public void testLoginFailureMissingFields() {
		// Frontend should prevent these submissions altogether
		given() // Username missing
		.contentType("application/json")
		.body(
				"{\n"
				+ "\"username\" : \"\",\n"
				+ "\"password\" : \"badPassword\"\n"
				+ "}")
		.when().post("/users/login")
		.then()
		.statusCode(HttpStatus.UNAUTHORIZED.value());
		
		given() // Password missing
		.contentType("application/json")
		.body(
				"{\n"
				+ "\"username\" : \"badUsername\",\n"
				+ "\"password\" : \"\"\n"
				+ "}")
		.when().post("users/login")
		.then()
		.statusCode(HttpStatus.UNAUTHORIZED.value());
		
		given() // All missing
		.contentType("application/json")
		.body(
				"{\n"
				+ "\"username\" : \"\",\n"
				+ "\"password\" : \"\"\n"
				+ "}")
		.when().post("users/login")
		.then()
		.statusCode(HttpStatus.UNAUTHORIZED.value());
	}
	
	@Test
	@Order(9)
	public void testPostProfile() {
		given()
		.header("Authorization","Bearer "+tokenA)
		.contentType("application/json")
		.body(profileA)
		.when().post("/users/profile")
		.then()
		.statusCode(anyOf(is(201),is(409)));
	}
}