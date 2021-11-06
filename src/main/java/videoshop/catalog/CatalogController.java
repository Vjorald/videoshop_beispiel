/*
 * Copyright 2013-2017 the original author or authors.
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

import videoshop.catalog.Lebensmittel.LebensmittelType;

import java.time.LocalDateTime;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Range;
import org.salespointframework.inventory.InventoryItem;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.time.BusinessTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
class CatalogController {

	private static final Quantity NONE = Quantity.of(0);

	private final LebensmittelCatalog catalog;
	private final UniqueInventory<UniqueInventoryItem> inventory;
	private final BusinessTime businessTime;

	CatalogController(LebensmittelCatalog lebensmittelCatalog, UniqueInventory<UniqueInventoryItem> inventory,
					  BusinessTime businessTime) {

		this.catalog = lebensmittelCatalog;
		this.inventory = inventory;
		this.businessTime = businessTime;
	}

	@GetMapping("/backzutaten")
	String backzutatenCatalog(Model model) {

		model.addAttribute("catalog", catalog.findByType(LebensmittelType.Backzutaten));
		model.addAttribute("title", "catalog.backzutaten.title");

		return "catalog";
	}

	@GetMapping("/brotaufstrich")
	String brotaufstrichCatalog(Model model) {

		model.addAttribute("catalog", catalog.findByType(LebensmittelType.Brotaufstrich));
		model.addAttribute("title", "catalog.brotaufstriche.title");

		return "catalog";
	}

	@GetMapping("/cerealien")
	String cerealienCatalog(Model model) {

		model.addAttribute("catalog", catalog.findByType(LebensmittelType.Cerealien));
		model.addAttribute("litle", "catalog.cerealien.title");

		return "catalog";
	}

	@GetMapping("/frische_und_kuehlung")
	String frische_und_kuehlungCatalog(Model model) {

		model.addAttribute("catalog", catalog.findByType(LebensmittelType.Frische_und_Kuehlung));
		model.addAttribute("title", "catalog.frische_und_kuehlung.title");

		return "catalog";
	}

	@GetMapping("/getraenke")
	String getraenkeCatalog(Model model) {

		model.addAttribute("catalog", catalog.findByType(LebensmittelType.Getraenke));
		model.addAttribute("title", "catalog.getraenke.title");

		return "catalog";
	}

	@GetMapping("/getreide")
	String getreideCatalog(Model model) {

		model.addAttribute("catalog", catalog.findByType(LebensmittelType.Getreide));
		model.addAttribute("title", "catalog.getreide.title");

		return "catalog";
	}

	@GetMapping("/kaffe_und_tee")
	String kaffe_und_tee_Catalog(Model model) {

		model.addAttribute("catalog", catalog.findByType(LebensmittelType.Kaffe_und_Tee));
		model.addAttribute("title", "catalog.kaffe_und_tee.title");

		return "catalog";
	}

	@GetMapping("/konserven_und_fertiggerichte")
	String Konserven_und_fertiggerichte_Catalog(Model model) {

		model.addAttribute("catalog", catalog.findByType(LebensmittelType.Konserven_und_fertiggerichte));
		model.addAttribute("title", "catalog.konserven_und_fertiggerichte.title");

		return "catalog";
	}

	@GetMapping("/milch_und_milchersatzprodukte")
				String milch_und_milchersatzprodukte_Catalog(Model model) {

			model.addAttribute("catalog", catalog.findByType(LebensmittelType.Milch_und_Milchersatzprodukte));
			model.addAttribute("title", "catalog.milch_und_milchersatzprodukte.title");

			return "catalog";
	}

	@GetMapping("/oel_essig_gewuerze")
	String oel_essig_gewuerze_Catalog(Model model) {

		model.addAttribute("catalog", catalog.findByType(LebensmittelType.Oel_Essig_Gewuerze));
		model.addAttribute("title", "catalog.oel_essig_gewuerze.title");

		return "catalog";
	}

	@GetMapping("/snacks")
	String snacksCatalog(Model model) {

		model.addAttribute("catalog", catalog.findByType(LebensmittelType.Snacks));
		model.addAttribute("title", "catalog.snacks.title");

		return "catalog";
	}

	@GetMapping("/suessigkeiten")
	String suessigkeitenCatalog(Model model) {

		model.addAttribute("catalog", catalog.findByType(LebensmittelType.Suessigkeiten));
		model.addAttribute("title", "catalog.suessigkeiten.title");

		return "catalog";
	}

	// (｡◕‿◕｡)
	// Befindet sich die angesurfte Url in der Form /foo/5 statt /foo?bar=5 so muss man @PathVariable benutzen
	// Lektüre: http://spring.io/blog/2009/03/08/rest-in-spring-3-mvc/
	@GetMapping("/lebensmittel/{lebensmittel}")
	String detail(@PathVariable Lebensmittel lebensmittel, Model model) {

		var quantity = inventory.findByProductIdentifier(lebensmittel.getId()) //
				.map(InventoryItem::getQuantity) //
				.orElse(NONE);

		model.addAttribute("lebensmittel", lebensmittel);
		model.addAttribute("quantity", quantity);
		model.addAttribute("orderable", quantity.isGreaterThan(NONE));

		return "detail";
	}

	// (｡◕‿◕｡)
	// Der Katalog bzw die Datenbank "weiß" nicht, dass die Disc mit einem Kommentar versehen wurde,
	// deswegen wird die update-Methode aufgerufen
	@PostMapping("/lebensmittel/{lebensmittel}/comments")
	public String comment(@PathVariable Lebensmittel lebensmittel, @Valid CommentAndRating payload) {

		lebensmittel.addComment(payload.toComment(businessTime.getTime()));
		catalog.save(lebensmittel);

		return "redirect:/lebensmittel/" + lebensmittel.getId();
	}

	/**
	 * Describes the payload to be expected to add a comment.
	 *
	 * @author Oliver Gierke
	 */
	interface CommentAndRating {

		@NotEmpty
		String getComment();

		@Range(min = 1, max = 5)
		int getRating();

		default Comment toComment(LocalDateTime time) {
			return new Comment(getComment(), getRating(), time);
		}
	}
}
