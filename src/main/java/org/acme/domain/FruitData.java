package org.acme.domain;

import java.util.StringJoiner;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class FruitData {
	private String genus;
	private String family;
	private String order;
	private Nutritions nutritions;

	public FruitData() {
	}

	public FruitData(String genus, String family, String order, Nutritions nutritions) {
		this.genus = genus;
		this.family = family;
		this.order = order;
		this.nutritions = nutritions;
	}

	public String getGenus() {
		return this.genus;
	}

	public void setGenus(String genus) {
		this.genus = genus;
	}

	public String getFamily() {
		return this.family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	public String getOrder() {
		return this.order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public Nutritions getNutritions() {
		return this.nutritions;
	}

	public void setNutritions(Nutritions nutritions) {
		this.nutritions = nutritions;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", FruitData.class.getSimpleName() + "[", "]")
			.add("genus='" + this.genus + "'")
			.add("family='" + this.family + "'")
			.add("order='" + this.order + "'")
			.add("nutritions=" + this.nutritions)
			.toString();
	}

	public static class Nutritions {
		private Double carbohydrates;
		private Double protein;
		private Double fat;
		private Double calories;
		private Double sugar;

		public Nutritions() {
		}

		public Nutritions(Double carbohydrates, Double protein, Double fat, Double calories, Double sugar) {
			this.carbohydrates = carbohydrates;
			this.protein = protein;
			this.fat = fat;
			this.calories = calories;
			this.sugar = sugar;
		}

		public Double getCarbohydrates() {
			return this.carbohydrates;
		}

		public void setCarbohydrates(Double carbohydrates) {
			this.carbohydrates = carbohydrates;
		}

		public Double getProtein() {
			return this.protein;
		}

		public void setProtein(Double protein) {
			this.protein = protein;
		}

		public Double getFat() {
			return this.fat;
		}

		public void setFat(Double fat) {
			this.fat = fat;
		}

		public Double getCalories() {
			return this.calories;
		}

		public void setCalories(Double calories) {
			this.calories = calories;
		}

		public Double getSugar() {
			return this.sugar;
		}

		public void setSugar(Double sugar) {
			this.sugar = sugar;
		}

		@Override
		public String toString() {
			return new StringJoiner(", ", Nutritions.class.getSimpleName() + "[", "]")
				.add("carbohydrates=" + this.carbohydrates)
				.add("protein=" + this.protein)
				.add("fat=" + this.fat)
				.add("calories=" + this.calories)
				.add("sugar=" + this.sugar)
				.toString();
		}
	}
}
