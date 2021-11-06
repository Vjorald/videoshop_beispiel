/*
 * Copyright 2013-2019 the original author or authors.
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
package videoshop.inventory;

import org.javamoney.moneta.Money;
import org.salespointframework.inventory.InventoryItem;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Quantity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import videoshop.catalog.Lebensmittel;

import static org.salespointframework.core.Currencies.EURO;

import java.util.*;
// Straight forward?

@Controller
class InventoryController {

	private final UniqueInventory<UniqueInventoryItem> inventory;
	private String corrected_name;
	private int number;
	
	InventoryController(UniqueInventory<UniqueInventoryItem> inventory) {
		this.inventory = inventory;
	}

	/**
	 * Displays all {@link InventoryItem}s in the system
	 *
	 * @param model will never be {@literal null}.
	 * @return the view name.
	 */
	@GetMapping("/stock")
	@PreAuthorize("hasRole('BOSS')")
	String stock(Model model) {

		model.addAttribute("stock", inventory.findAll());

		return "stock";
	}
	
    
	@PostMapping("/Nachbestellen")
	@PreAuthorize("hasRole('BOSS')")
	String stock_after(Model model, @RequestBody String name) {
		
		int length = name.length();
		String corrected_number;
		int equalIndex = name.indexOf("=");
		
		//Deserialising the incoming string
		String new_name = name.substring(0,equalIndex);
		new_name = new_name.replace("+", " ");
		new_name = new_name.replace("%3A", ":");
		new_name = new_name.replace("%26","&");
		new_name = new_name.replace("%27", "'");
		
		corrected_name = new_name; // Name of the article that have to be ordered
		corrected_number= name.substring(equalIndex+1, length);   
		
		number = Integer.parseInt(corrected_number);  // Number of articles that have to be ordered
		
		
		
		inventory.findAll().forEach(uniqueInventoryItem ->{
			
			if(uniqueInventoryItem.getProduct().getName().equals(corrected_name))   
				uniqueInventoryItem.increaseQuantity(Quantity.of(number));
		}	
				
				);
			
		model.addAttribute("stock", inventory.findAll());
		
		return "stock";
		
	}

	private Object UniqueInventoryItem(Lebensmittel lebensmittel, Quantity of) {
		// TODO Auto-generated method stub
		return null;
	}


}
