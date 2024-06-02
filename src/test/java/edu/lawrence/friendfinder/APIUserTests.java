package edu.lawrence.friendfinder;

import java.util.List;

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
public class APIUserTests {
	
	private static UserDTO userA;
	private static UserDTO userB;
	private static String tokenA;
	private static String tokenB;
	private static ProfileDTO profileA;
	private static ProfileDTO profileB;
	private static ProfileDTO searchProfile;
	private static String profileId;
	
	@BeforeAll
	public static void setup() {
		RestAssured.port = 8085;
		RestAssured.baseURI = "http://localhost";
		
		userA = new UserDTO();
		userA.setUsername("UserA");
		userA.setPassword("password");
		userB = new UserDTO();
		userB.setUsername("UserB");
		userB.setPassword("testing");

		profileA = new ProfileDTO();
		profileA.setFullname("Test User");
		profileA.setEmailaddress("userA@test.com");
		profileA.setCountrycode(1);
		profileA.setPhonenumber("4444444444");
		profileA.setBio("I am a test user");
		profileA.setGenres(List.of("Test Genre 1", "Test Genre 2"));
		profileA.setPlatforms(List.of("Test Platform 1", "Test Platform 2"));
		profileB = new ProfileDTO();
		profileB.setFullname("Mr. Tester");
		profileB.setEmailaddress("userB@test.com");
		profileB.setCountrycode(1);
		profileB.setPhonenumber("5555555555");
		profileB.setBio("This is a second test user");
		profileB.setGenres(List.of("Test Genre 2", "Test Genre 3"));
		profileB.setPlatforms(List.of("Test Platform 2", "Test Platform 3"));
		searchProfile = new ProfileDTO();
		searchProfile.setGenres(List.of("Test Genre 2", "Test Genre 3"));
		searchProfile.setPlatforms(List.of("Test Platform 2", "Test Platform 3"));
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
		.statusCode(anyOf(is(201), is(401), is(409)));
	}
	
	@Test
	@Order(10)
	public void testGetProfile() {
		given()
		.header("Authorization", "Bearer " + tokenA)
		.when().get("/users/profile")
		.then()
		.statusCode(HttpStatus.OK.value());
	}

	@Test
	@Order(11)
	public void setupSecondUser() {
		given()
		.contentType("application/json")
		.body(userB)
		.when().post("/users")
		.then()
		.statusCode(anyOf(is(HttpStatus.CREATED.value()), is(HttpStatus.CONFLICT.value())));

		tokenB = given()
				.contentType("application/json")
				.body(userB)
				.when().post("/users/login")
				.then()
				.statusCode(200)
				.extract().path("token");

		given()
		.header("Authorization","Bearer "+tokenB)
		.contentType("application/json")
		.body(profileB)
		.when().post("/users/profile")
		.then()
		.statusCode(anyOf(is(201), is(401), is(409)));
	}

	@Test
	@Order(12)
	public void testGetProfilesQuery() {
		// Getting a 403 HTTP error for some reason even with authentication removed
		profileId =  given()
				.body(searchProfile)
				.when().get("/users/profiles?ex=true")
				.then()
				.statusCode(200)
				.extract().path("[0].emailaddress").toString();
		System.out.println(profileId);
	}
}