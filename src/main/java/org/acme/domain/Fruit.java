package org.acme.domain;

import java.util.Objects;
import java.util.StringJoiner;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "fruits")
public class Fruit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	@NotBlank(message = "Name is mandatory")
	private String name;
	private String description;

	@Transient
	private FruitData data;

	public Fruit() {
	}

	public Fruit(Long id, String name, String description) {
		this(id, name, description, null);
	}

	public Fruit(Long id, String name, String description, FruitData data) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.data = data;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public FruitData getData() {
		return this.data;
	}

	public void setData(FruitData data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", Fruit.class.getSimpleName() + "[", "]")
			.add("id=" + this.id)
			.add("name='" + this.name + "'")
			.add("description='" + this.description + "'")
			.add("data=" + this.data)
			.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if ((o == null) || (getClass() != o.getClass())) {
			return false;
		}

		Fruit fruit = (Fruit) o;
		return this.id.equals(fruit.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id);
	}
}
