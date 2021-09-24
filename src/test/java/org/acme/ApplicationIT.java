package org.acme;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.http.ContentType;

@QuarkusIntegrationTest
@QuarkusTestResource(WiremockFruityVice.class)
@TestMethodOrder(OrderAnnotation.class)
public class ApplicationIT {
	@Test
	@Order(1)
	public void getAll() {
		given()
			.when().get("/fruits")
			.then()
				.statusCode(200)
				.contentType(ContentType.JSON)
				.body(
					"$.size()", is(2),
					"[0].id", is(1),
					"[0].name", is("Apple"),
					"[0].description", is("Hearty fruit"),
					"[0].data.genus", is("Malus"),
					"[0].data.family", is("Rosaceae"),
					"[0].data.order", is("Rosales"),
					"[0].data.nutritions.carbohydrates", is(11.4F),
					"[0].data.nutritions.protein", is(0.3F),
					"[0].data.nutritions.fat", is(0.4F),
					"[0].data.nutritions.calories", is(52F),
					"[0].data.nutritions.sugar", is(10.3F),
					"[1].name", is("Pear"),
					"[1].description", is("Juicy fruit"),
					"[1].data", emptyOrNullString()
				);
	}

	@Test
	@Order(2)
	public void getFruitFound() {
		given()
			.when().get("/fruits/Apple")
			.then()
				.statusCode(200)
				.contentType(ContentType.JSON)
				.body(
					"id", is(1),
					"name", is("Apple"),
					"description", is("Hearty fruit"),
					"data.genus", is("Malus"),
					"data.family", is("Rosaceae"),
					"data.order", is("Rosales"),
					"data.nutritions.carbohydrates", is(11.4F),
					"data.nutritions.protein", is(0.3F),
					"data.nutritions.fat", is(0.4F),
					"data.nutritions.calories", is(52F),
					"data.nutritions.sugar", is(10.3F)
				);
	}

	@Test
	@Order(3)
	public void getFruitNotFound() {
		given()
			.when().get("/fruits/Lemon")
			.then()
				.statusCode(404)
				.body(blankOrNullString());
	}

	@Test
	@Order(4)
	public void addFruit() {
		given()
			.when()
				.contentType(ContentType.JSON)
				.body("{\"name\":\"Lemon\",\"description\":\"Tangy fruit\"}")
				.post("/fruits")
			.then()
				.statusCode(200)
				.contentType(ContentType.JSON)
				.body(
					"id", is(3),
					"name", is("Lemon"),
					"description", is("Tangy fruit"),
					"data.genus", is("Citrus"),
					"data.family", is("Rutaceae"),
					"data.order", is("Sapindales"),
					"data.nutritions.carbohydrates", is(9F),
					"data.nutritions.protein", is(1.1F),
					"data.nutritions.fat", is(0.3F),
					"data.nutritions.calories", is(29F),
					"data.nutritions.sugar", is(2.5F)
				);
	}
}
