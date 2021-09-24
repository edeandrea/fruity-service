package org.acme.service;

import static org.assertj.core.api.Assertions.*;

import java.time.Duration;
import java.util.List;

import javax.inject.Inject;

import org.acme.domain.Fruit;
import org.acme.domain.FruitData;
import org.acme.domain.FruitData.Nutritions;
import org.acme.repository.FruitRepository;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.smallrye.mutiny.Uni;

@QuarkusTest
class FruitServiceTests {
	@InjectMock
	FruitRepository fruitRepository;

	@InjectMock
	@RestClient
	FruityViceClient fruityViceClient;

	@Inject
	FruitService fruitService;

	@Test
	public void getAllFruits() {
		Mockito.when(this.fruitRepository.listAll())
			.thenReturn(Uni.createFrom().item(List.of(new Fruit(1L, "Apple", "Hearty Fruit"))));

		Mockito.when(this.fruityViceClient.getByName(Mockito.eq("Apple")))
			.thenReturn(Uni.createFrom().item(
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
			));

		List<Fruit> fruits = this.fruitService.getAllFruits().await().atMost(Duration.ofSeconds(5));

		assertThat(fruits)
			.isNotNull()
			.hasSize(1)
			.extracting(Fruit::getId, Fruit::getName, Fruit::getDescription)
			.containsExactly(tuple(1L, "Apple", "Hearty Fruit"));

		Mockito.verify(this.fruitRepository).listAll();
		Mockito.verify(this.fruityViceClient).getByName(Mockito.eq("Apple"));
		Mockito.verifyNoMoreInteractions(this.fruitRepository);
	}

	@Test
	public void getFruitFound() {
		Mockito.when(this.fruitRepository.findByName(Mockito.eq("Apple")))
			.thenReturn(Uni.createFrom().item(new Fruit(1L, "Apple", "Hearty Fruit")));

		FruitData fruitData = new FruitData("Malus", "Rosaceae", "Rosales", new Nutritions(11.4, 0.3, 0.4, 52.0, 10.3));

		Mockito.when(this.fruityViceClient.getByName(Mockito.eq("Apple")))
			.thenReturn(Uni.createFrom().item(fruitData
			));

		Fruit fruit = this.fruitService.getFruit("Apple").await().atMost(Duration.ofSeconds(5));

		assertThat(fruit)
			.isNotNull()
			.extracting(Fruit::getId, Fruit::getName, Fruit::getDescription)
			.containsExactly(1L, "Apple", "Hearty Fruit");

		assertThat(fruit.getData())
			.isNotNull()
			.usingRecursiveComparison()
			.isEqualTo(fruitData);

		Mockito.verify(this.fruitRepository).findByName(Mockito.eq("Apple"));
		Mockito.verify(this.fruityViceClient).getByName(Mockito.eq("Apple"));
		Mockito.verifyNoMoreInteractions(this.fruitRepository);
	}

	@Test
	public void getFruitNotFound() {
		Mockito.when(this.fruitRepository.findByName(Mockito.eq("Apple")))
			.thenReturn(Uni.createFrom().nullItem());

		assertThat(this.fruitService.getFruit("Apple").await().atMost(Duration.ofSeconds(5)))
			.isNull();

		Mockito.verify(this.fruitRepository).findByName(Mockito.eq("Apple"));
		Mockito.verifyNoInteractions(this.fruityViceClient);
		Mockito.verifyNoMoreInteractions(this.fruitRepository);
	}

	@Test
	public void addFruit() {
		Mockito.when(this.fruitRepository.persist(Mockito.any(Fruit.class)))
			.thenReturn(Uni.createFrom().item(new Fruit(1L, "Grapefruit", "Summer fruit")));

		FruitData fruitData = new FruitData("Malus", "Rosaceae", "Rosales", new Nutritions(11.4, 0.3, 0.4, 52.0, 10.3));

		Mockito.when(this.fruityViceClient.getByName(Mockito.eq("Grapefruit")))
			.thenReturn(Uni.createFrom().item(fruitData
			));

		Fruit fruit = this.fruitService.addFruit(new Fruit(null, "Grapefruit", "Summer fruit")).await().atMost(Duration.ofSeconds(5));

		assertThat(fruit)
			.isNotNull()
			.extracting(Fruit::getId, Fruit::getName, Fruit::getDescription)
			.containsExactly(1L, "Grapefruit", "Summer fruit");

		assertThat(fruit.getData())
			.isNotNull()
			.usingRecursiveComparison()
			.isEqualTo(fruitData);

		Mockito.verify(this.fruitRepository).persist(Mockito.any(Fruit.class));
		Mockito.verify(this.fruityViceClient).getByName(Mockito.eq("Grapefruit"));
		Mockito.verifyNoMoreInteractions(this.fruitRepository);
	}
}
