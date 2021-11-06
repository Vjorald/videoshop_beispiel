/*
 * Copyright 2013-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package videoshop.catalog;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;

// (｡◕‿◕｡)
// Diese erbt von Product um die Catalog-Klasse aus Salespoint nutzen zu können.
// Ein Primärschlüssel ist nicht notwendig, da dieser schon in Product definiert ist, alle anderen
// JPA-Anforderungen müssen aber erfüllt werden.
@Entity
public class Lebensmittel extends Product {


	public static enum LebensmittelType {
		Backzutaten, Brotaufstrich, Cerealien, Frische_und_Kuehlung, Getraenke, Getreide, Kaffe_und_Tee
		,Konserven_und_fertiggerichte,Milch_und_Milchersatzprodukte,Oel_Essig_Gewuerze,Snacks,Suessigkeiten;
	}

	// (｡◕‿◕｡)
	// primitive Typen oder Strings müssen nicht extra für JPA annotiert werden
	private String image;
	private LebensmittelType type;

	// (｡◕‿◕｡)
	// Jeder Lebensmittel besitzt mehrere Kommentare, eine "1 zu n"-Beziehung -> @OneToMany für JPA
	// cascade gibt an, was mit den Kindelementen (Comment) passieren soll wenn das Parentelement
	// (Disc) mit der Datenbank "interagiert"
	@OneToMany(cascade = CascadeType.ALL) //
	private List<Comment> comments = new ArrayList<>();

	@SuppressWarnings({ "unused", "deprecation" })
	private Lebensmittel() {}

	public Lebensmittel(String name, String image, Money price, LebensmittelType type) {

		super(name, price);

		this.image = image;
		this.type = type;
	}

	public void addComment(Comment comment) {
		comments.add(comment);
	}

	// (｡◕‿◕｡)
	// Es ist immer sinnvoll sich zu überlegen wie speziell der Rückgabetyp sein sollte
	// Da sowies nur über die Kommentare iteriert wird, ist ein Iterable<T> das sinnvollste.
	// Weil wir keine Liste zurück geben, verhindern wir auch, dass jemand die comments-Liste
	// einfach durch clear() leert. Deswegen geben auch so viele Salespoint Klassen nur
	// Iterable<T> zurück ;)
	public Iterable<Comment> getComments() {
		return comments;
	}

	public String getImage() {
		return image;
	}

	public LebensmittelType getType() {
		return type;
	}
}
