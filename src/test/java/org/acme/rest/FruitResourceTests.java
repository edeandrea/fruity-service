package org.acme.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.blankOrNullString;

import java.util.List;

import org.acme.domain.Fruit;
import org.acme.domain.FruitData;
import org.acme.domain.FruitData.Nutritions;
import org.acme.service.FruitService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import io.smallrye.mutiny.Uni;

@QuarkusTest
class FruitResourceTests {
	@InjectMock
	FruitService fruitService;

	@Test
	public void getAll() {
		Mockito.when(this.fruitService.getAllFruits())
			.thenReturn(Uni.createFrom().item(List.of(new Fruit(
				1L,
				"Apple",
				"Hearty Fruit",
				new FruitData(
					"Malus",
					"Rosaceae",
					"Rosales",
					new Nutritions(
						11.4,
						0.3,
						0.4,
						52.0,
						10.3
					)
				)
			))));

		given()
			.when().get("/fruits")
			.then()
				.statusCode(200)
				.contentType(ContentType.JSON)
				.body(
					"$.size()", is(1),
					"[0].id", is(1),
					"[0].name", is("Apple"),
					"[0].description", is("Hearty Fruit"),
					"[0].data.genus", is("Malus"),
					"[0].data.family", is("Rosaceae"),
					"[0].data.order", is("Rosales"),
					"[0].data.nutritions.carbohydrates", is(11.4F),
					"[0].data.nutritions.protein", is(0.3F),
					"[0].data.nutritions.fat", is(0.4F),
					"[0].data.nutritions.calories", is(52F),
					"[0].data.nutritions.sugar", is(10.3F)
				);

		Mockito.verify(this.fruitService).getAllFruits();
		Mockito.verifyNoMoreInteractions(this.fruitService);
	}

	@Test
	public void getFruitFound() {
		Mockito.when(this.fruitService.getFruit(Mockito.eq("Apple")))
			.thenReturn(Uni.createFrom().item(new Fruit(
				1L,
				"Apple",
				"Hearty Fruit",
				new FruitData(
					"Malus",
					"Rosaceae",
					"Rosales",
					new Nutritions(
						11.4,
						0.3,
						0.4,
						52.0,
						10.3
					)
				)
			)));

		given()
			.when().get("/fruits/Apple")
			.then()
				.statusCode(200)
				.contentType(ContentType.JSON)
				.body(
					"id", is(1),
					"name", is("Apple"),
					"description", is("Hearty Fruit"),
					"data.genus", is("Malus"),
					"data.family", is("Rosaceae"),
					"data.order", is("Rosales"),
					"data.nutritions.carbohydrates", is(11.4F),
					"data.nutritions.protein", is(0.3F),
					"data.nutritions.fat", is(0.4F),
					"data.nutritions.calories", is(52F),
					"data.nutritions.sugar", is(10.3F)
				);

		Mockito.verify(this.fruitService).getFruit(Mockito.eq("Apple"));
		Mockito.verifyNoMoreInteractions(this.fruitService);
	}

	@Test
	public void getFruitNotFound() {
		Mockito.when(this.fruitService.getFruit(Mockito.eq("Apple")))
			.thenReturn(Uni.createFrom().nullItem());

		given()
			.when().get("/fruits/Apple")
			.then()
				.statusCode(404)
				.body(blankOrNullString());

		Mockito.verify(this.fruitService).getFruit(Mockito.eq("Apple"));
		Mockito.verifyNoMoreInteractions(this.fruitService);
	}

	@Test
	public void addFruit() {
		Mockito.when(this.fruitService.addFruit(Mockito.any(Fruit.class)))
			.thenReturn(Uni.createFrom().item(new Fruit(1L, "Grapefruit", "Summer fruit")));

		given()
			.when()
				.contentType(ContentType.JSON)
				.body("{\"name\":\"Grapefruit\",\"description\":\"Summer fruit\"}")
				.post("/fruits")
			.then()
				.statusCode(200)
				.contentType(ContentType.JSON)
				.body(
					"id", is(1),
					"name", is("Grapefruit"),
					"description", is("Summer fruit")
				);

		Mockito.verify(this.fruitService).addFruit(Mockito.any(Fruit.class));
		Mockito.verifyNoMoreInteractions(this.fruitService);
	}
}
