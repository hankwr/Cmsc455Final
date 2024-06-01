package edu.lawrence.friendfinder;

// In-project includes [DTOs]
import edu.lawrence.friendfinder.interfaces.dtos.EventDTO;
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
public class EventTests {
	
	private static UserDTO userA;
	private static String tokenA;
	private static EventDTO eventA;
	private static String eventId;
	
	@BeforeAll
	public static void setup() {
		RestAssured.port = 8085;
		RestAssured.baseURI = "http://localhost";
		
		userA = new UserDTO();
		userA.setUsername("UserA");
		userA.setPassword("password");

		eventA = new EventDTO();
		eventA.setName("test event");
		eventA.setDescription("This is a test event");
		eventA.setLocation("800 E Boldt Way");
		eventA.setStartTime("12:00 PM, 06/30/2024 UTC");
		eventA.setEndTime("3:00 PM, 06/30/2024 UTC");
	}
	
	@Test
	@Order(1)
	public void prerequisites() {
		// Post user event and get authentication token
		given()
		.contentType("application/json")
		.body(userA)
		.when().post("/users")
		.then()
		.statusCode(anyOf(is(HttpStatus.CREATED.value()), is(HttpStatus.CONFLICT.value())));

		tokenA = given()
				.contentType("application/json")
				.body(userA)
				.when().post("/users/login")
				.then()
				.statusCode(200)
				.extract().path("token");
	}

	@Test
	@Order(2)
	public void testPostEvent() {
		given()
		.header("Authorization","Bearer "+tokenA)
		.contentType("application/json")
		.body(eventA)
		.when().post("/events")
		.then()
		.statusCode(anyOf(is(HttpStatus.CREATED.value()), is(HttpStatus.CONFLICT.value())));
	}

	@Test
	@Order(3)
	public void testGetUserEvents() {
		given()
		.header("Authorization", "Bearer " + tokenA)
		.when().get("/users/events")
		.then()
		.statusCode(HttpStatus.OK.value());
	}

	@Test
	@Order(4)
	public void testGetFutureEvents() {
		eventId =  given()
				.header("Authorization", "Bearer " + tokenA)
				.when().get("/events")
				.then()
				.statusCode(200)
				.extract().path("[0].eventId").toString();
	}
	
	@Test
	@Order(5)
	public void testPostRegistration() {
		given().
		header("Authorization", "Bearer " + tokenA).
		when().post("/events/" + eventId).
		then().
		statusCode(anyOf(is(HttpStatus.OK.value()),  
				is(HttpStatus.CONFLICT.value())));
		
	}
	
	@Test
	@Order(6)
	public void testGetRegistrations() {
		given().
		header("Authorization", "Bearer " + tokenA).
		when().get("/events/" + eventId + "/registrations").
		then().
		statusCode(HttpStatus.OK.value());
	}
}