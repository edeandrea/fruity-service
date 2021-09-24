package org.acme.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;

import javax.inject.Inject;

import org.acme.TestTransaction;
import org.acme.domain.Fruit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class FruitRepositoryTests {
	@Inject
	FruitRepository fruitRepository;

	@ParameterizedTest(name = ParameterizedTest.DISPLAY_NAME_PLACEHOLDER + "[" + ParameterizedTest.INDEX_PLACEHOLDER + "] (" + ParameterizedTest.ARGUMENTS_WITH_NAMES_PLACEHOLDER + ")")
	@DisplayName("findByName")
	@ValueSource(strings = { "Grapefruit", "grapefruit", "GrApEfRuIt" })
	public void findByName(String name) {
		Fruit fruit = TestTransaction.withRollback(() ->
			this.fruitRepository
				.persist(new Fruit(null, "Grapefruit", "Summer fruit"))
				.replaceWith(this.fruitRepository.findByName(name))
		)
			.await()
			.atMost(Duration.ofSeconds(10));

		assertThat(fruit)
			.isNotNull()
			.extracting(Fruit::getName, Fruit::getDescription)
			.containsExactly("Grapefruit", "Summer fruit");

		assertThat(fruit.getId())
			.isNotNull()
			.isGreaterThan(2L);
	}
}
