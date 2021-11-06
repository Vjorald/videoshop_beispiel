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

import static org.salespointframework.core.Currencies.*;

import videoshop.catalog.Lebensmittel.LebensmittelType;

import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * A {@link DataInitializer} implementation that will create dummy data for the application on application startup.
 *
 * @author Paul Henke
 * @author Oliver Gierke
 * @see DataInitializer
 */
@Component
@Order(20)
class CatalogDataInitializer implements DataInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(CatalogDataInitializer.class);

	private final LebensmittelCatalog lebensmittelCatalog;

	CatalogDataInitializer(LebensmittelCatalog lebensmittelCatalog) {

		Assert.notNull(lebensmittelCatalog, "LebensmittelCatalog must not be null!");

		this.lebensmittelCatalog = lebensmittelCatalog;
	}

	/*
	 * (non-Javadoc)
	 * @see org.salespointframework.core.DataInitializer#initialize()
	 */
	@Override
	public void initialize() {

		if (lebensmittelCatalog.findAll().iterator().hasNext()) {
			return;
		}

		LOG.info("Creating default catalog entries.");

		lebensmittelCatalog.save(new Lebensmittel("Demerara Zucker", "", Money.of(3.99, EURO), LebensmittelType.Backzutaten));
		lebensmittelCatalog.save(new Lebensmittel("Rohrzucker", "", Money.of(2.57, EURO), LebensmittelType.Backzutaten));
		lebensmittelCatalog.save(new Lebensmittel("Agavendicksaft", "", Money.of(6.92, EURO), LebensmittelType.Backzutaten));
		lebensmittelCatalog.save(new Lebensmittel("Ahornsirup", "", Money.of(11.75, EURO), LebensmittelType.Backzutaten));
		lebensmittelCatalog.save(new Lebensmittel("Dattelsirup", "", Money.of(9.99, EURO), LebensmittelType.Backzutaten));
		lebensmittelCatalog.save(new Lebensmittel("Artischokencreme", "", Money.of(4.99, EURO), LebensmittelType.Brotaufstrich));
		lebensmittelCatalog
				.save(new Lebensmittel("Aubergine Streichcreme", "", Money.of(3.97, EURO), LebensmittelType.Brotaufstrich));
		lebensmittelCatalog.save(new Lebensmittel("Bohnen Paprika Chili Aufstrich", "", Money.of(7.52, EURO),
				LebensmittelType.Brotaufstrich));

		lebensmittelCatalog.save(new Lebensmittel("Curry Mango Streichcreme", "", Money.of(3.92, EURO), LebensmittelType.Brotaufstrich));
		lebensmittelCatalog.save(new Lebensmittel("Cornflakes gesüsst", "", Money.of(5.99, EURO), LebensmittelType.Cerealien));
		lebensmittelCatalog
				.save(new Lebensmittel("Cornflakes, Vollkorn", "", Money.of(4.13, EURO), LebensmittelType.Cerealien));
		lebensmittelCatalog
				.save(new Lebensmittel("Dinkelflakes, Vollkorn", "", Money.of(6.22, EURO), LebensmittelType.Cerealien));
		lebensmittelCatalog.save(new Lebensmittel("Crunchy Klassik", "", Money.of(5.73, EURO), LebensmittelType.Cerealien));
		lebensmittelCatalog
				.save(new Lebensmittel("Artichoken", "", Money.of(7.34, EURO), LebensmittelType.Frische_und_Kuehlung));
		lebensmittelCatalog.save(new Lebensmittel("CousCous-Salat", "", Money.of(6.34, EURO), LebensmittelType.Frische_und_Kuehlung));
		lebensmittelCatalog.save(new Lebensmittel("Getrocknete Tomaten", "", Money.of(3.44, EURO), LebensmittelType.Frische_und_Kuehlung));
		lebensmittelCatalog.save(new Lebensmittel("Grüne Oliven mit Mandeln", "", Money.of(5.89, EURO), LebensmittelType.Frische_und_Kuehlung));
	}
}
