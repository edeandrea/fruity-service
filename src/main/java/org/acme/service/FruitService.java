package org.acme.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.acme.domain.Fruit;
import org.acme.repository.FruitRepository;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;

@ApplicationScoped
public class FruitService {
	private final FruitRepository fruitRepository;
	private final FruityViceClient fruityViceClient;

	public FruitService(FruitRepository fruitRepository, @RestClient FruityViceClient fruityViceClient) {
		this.fruitRepository = fruitRepository;
		this.fruityViceClient = fruityViceClient;
	}

	public Uni<List<Fruit>> getAllFruits() {
		return this.fruitRepository.listAll()
			.onItem().ifNotNull().transformToUni(this::enrichFruits);
	}

	public Uni<Fruit> getFruit(String name) {
		return this.fruitRepository.findByName(name)
			.onItem().ifNotNull().transformToUni(this::enrichFruit);
	}

	public Uni<Fruit> addFruit(Fruit fruit) {
		return Panache.withTransaction(() -> this.fruitRepository.persist(fruit))
			.onItem().ifNotNull().transformToUni(this::enrichFruit);
	}

	private Uni<List<Fruit>> enrichFruits(List<Fruit> fruits) {
		return Uni.join().all(
				fruits.stream()
					.map(this::enrichFruit)
					.collect(Collectors.toList())
			)
			.andFailFast();
	}

	private Uni<Fruit> enrichFruit(Fruit fruit) {
		return this.fruityViceClient.getByName(fruit.getName())
			.onItem().ifNotNull().transform(fruitData -> {
				fruit.setData(fruitData);
				return fruit;
			})
			.onFailure(this::isDataNotFoundError).recoverWithItem(fruit);
	}

	private boolean isDataNotFoundError(Throwable t) {
		return (t instanceof WebApplicationException) &&
			(((WebApplicationException) t).getResponse().getStatus() == Status.NOT_FOUND.getStatusCode());
	}
}
