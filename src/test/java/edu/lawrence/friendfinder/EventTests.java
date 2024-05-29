package edu.lawrence.friendfinder;

// In-project includes [DTOs]
import edu.lawrence.friendfinder.interfaces.dtos.EventDTO;
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
public class EventTests {
	
	private static UserDTO userA;
	private static String tokenA;
	private static EventDTO eventA;
	
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
		eventA.setTimeZone("UTC");
		eventA.setStartTime("2024-06-15T15:00:00Z");
		eventA.setEndTime("2024-06-15T18:00:00Z");
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
	public void getUserToken() {
		tokenA = given()
				.contentType("application/json")
				.body(userA)
				.when().post("/users/login")
				.then()
				.statusCode(200)
				.extract().path("token");
	}

	@Test
	@Order(3)
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
	@Order(4)
	public void testGetUserEvents() {
		given()
		.header("Authorization", "Bearer " + tokenA)
		.when().get("/users/events")
		.then()
		.statusCode(HttpStatus.OK.value());
	}
}