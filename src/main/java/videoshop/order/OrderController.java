/*
 * Copyright 2013-2018 the original author or authors.
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
package videoshop.order;

import videoshop.catalog.Lebensmittel;

import java.util.Optional;

import org.salespointframework.catalog.Product;
import org.salespointframework.core.AbstractEntity;
import org.salespointframework.order.Cart;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderManagement;
import org.salespointframework.order.OrderStatus;
import org.salespointframework.payment.Cash;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * A Spring MVC controller to manage the {@link Cart}. {@link Cart} instances are held in the session as they're
 * specific to a certain user. That's also why the entire controller is secured by a {@code PreAuthorize} clause.
 *
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Controller
@PreAuthorize("isAuthenticated()")
@SessionAttributes("cart")
class OrderController {

	private final OrderManagement<Order> orderManagement;

	/**
	 * Creates a new {@link OrderController} with the given {@link OrderManagement}.
	 *
	 * @param orderManagement must not be {@literal null}.
	 */
	OrderController(OrderManagement<Order> orderManagement) {

		Assert.notNull(orderManagement, "OrderManagement must not be null!");
		this.orderManagement = orderManagement;
	}

	/**
	 * Creates a new {@link Cart} instance to be stored in the session (see the class-level {@link SessionAttributes}
	 * annotation).
	 *
	 * @return a new {@link Cart} instance.
	 */
	@ModelAttribute("cart")
	Cart initializeCart() {
		return new Cart();
	}

	/**
	 * Adds a {@link Lebensmittel} to the {@link Cart}. Note how the type of the parameter taking the request parameter
	 * {@code pid} is {@link Lebensmittel}. For all domain types extending {@link AbstractEntity} (directly or indirectly) a tiny
	 * Salespoint extension will directly load the object instance from the database. If the identifier provided is
	 * invalid (invalid format or no {@link Product} with the id found), {@literal null} will be handed into the method.
	 *
	 * @param lebensmittel the food that should be added to the cart (may be {@literal null}).
	 * @param number number of discs that should be added to the cart.
	 * @param cart must not be {@literal null}.
	 * @return the view name.
	 */
	@PostMapping("/cart")
	String addLebensmittel(@RequestParam("pid") Lebensmittel lebensmittel, @RequestParam("number") int number, @ModelAttribute Cart cart) {

		// (｡◕‿◕｡)
		// Das Inputfeld im View ist eigentlich begrenzt, allerdings sollte man immer auch serverseitig validieren
		int amount = number <= 0 || number > 5 ? 1 : number;

		// (｡◕‿◕｡)
		// Wir fügen dem Warenkorb die Lebensmittel in entsprechender Anzahl hinzu.
		cart.addOrUpdateItem(lebensmittel, Quantity.of(amount));

		// (｡◕‿◕｡)
		// Je nachdem ob disc eine DVD oder eine Bluray ist, leiten wir auf die richtige Seite weiter

		switch (lebensmittel.getType()) {
			case Backzutaten:
				return "redirect:backzutaten";
			case Brotaufstrich:
				return "redirect:brotaufstrich";
			case Cerealien:
				return "redirect:cerealien";
			case Frische_und_Kuehlung:
				return "redirect:frische_und_kuehlung";
			case Getraenke:
				return "redirect:getraenke";
			case Getreide:
				return "redirect:getreide";
			case Kaffe_und_Tee:
				return "redirect:kaffe_und_tee";
			case Konserven_und_fertiggerichte:
				return "redirect:konserven_und_fertiggerichte";
			case Milch_und_Milchersatzprodukte:
				return "redirect:milch_und_milchersatzprodukte";
			case Oel_Essig_Gewuerze:
				return "redirect:oel_essig_gewuerze";
			case Snacks:
				return "redirect:snacks";
			case Suessigkeiten:
				return "redirect:suessigkeiten";
			default:
				return "redirect:backzutaten";
		}
	}

	@GetMapping("/cart")
	String basket() {
		return "cart";
	}

	/**
	 * Checks out the current state of the {@link Cart}. Using a method parameter of type {@code Optional<UserAccount>}
	 * annotated with {@link LoggedIn} you can access the {@link UserAccount} of the currently logged in user.
	 *
	 * @param cart will never be {@literal null}.
	 * @param userAccount will never be {@literal null}.
	 * @return the view name.
	 */
	@PostMapping("/checkout")
	String buy(@ModelAttribute Cart cart, @LoggedIn Optional<UserAccount> userAccount) {

		return userAccount.map(account -> {

			// (｡◕‿◕｡)
			// Mit completeOrder(…) wird der Warenkorb in die Order überführt, diese wird dann bezahlt und abgeschlossen.
			// Orders können nur abgeschlossen werden, wenn diese vorher bezahlt wurden.
			var order = new Order(account, Cash.CASH);

			cart.addItemsTo(order);

			orderManagement.payOrder(order);
			orderManagement.completeOrder(order);

			cart.clear();

			return "redirect:/";
		}).orElse("redirect:/cart");
	}

	@GetMapping("/orders")
	@PreAuthorize("hasRole('BOSS')")
	String orders(Model model) {

		model.addAttribute("ordersCompleted", orderManagement.findBy(OrderStatus.COMPLETED));

		return "orders";
	}
	
	
}
