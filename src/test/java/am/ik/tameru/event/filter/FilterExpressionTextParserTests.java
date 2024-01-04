/*
 * Copyright 2023-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package am.ik.tameru.event.filter;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Christian Tzolov
 */
public class FilterExpressionTextParserTests {

	FilterExpressionTextParser parser = new FilterExpressionTextParser();

	@Test
	public void testEQ() {
		// country == "BG"
		Filter.Expression exp = parser.parse("country == 'BG'");
		assertThat(exp).isEqualTo(
				new Filter.Expression(Filter.ExpressionType.EQ, new Filter.Key("country"), new Filter.Value("BG")));

		assertThat(parser.getCache().get("WHERE " + "country == 'BG'")).isEqualTo(exp);
	}

	@Test
	public void tesEqAndGte() {
		// genre == "drama" AND year >= 2020
		Filter.Expression exp = parser.parse("genre == 'drama' && year >= 2020");
		assertThat(exp).isEqualTo(new Filter.Expression(Filter.ExpressionType.AND,
				new Filter.Expression(Filter.ExpressionType.EQ, new Filter.Key("genre"), new Filter.Value("drama")),
				new Filter.Expression(Filter.ExpressionType.GTE, new Filter.Key("year"), new Filter.Value(2020))));

		assertThat(parser.getCache().get("WHERE " + "genre == 'drama' && year >= 2020")).isEqualTo(exp);
	}

	@Test
	public void tesIn() {
		// genre in ["comedy", "documentary", "drama"]
		Filter.Expression exp = parser.parse("genre in ['comedy', 'documentary', 'drama']");
		assertThat(exp).isEqualTo(new Filter.Expression(Filter.ExpressionType.IN, new Filter.Key("genre"),
				new Filter.Value(List.of("comedy", "documentary", "drama"))));

		assertThat(parser.getCache().get("WHERE " + "genre in ['comedy', 'documentary', 'drama']")).isEqualTo(exp);
	}

	@Test
	public void testNe() {
		// year >= 2020 OR country == "BG" AND city != "Sofia"
		Filter.Expression exp = parser.parse("year >= 2020 OR country == \"BG\" AND city != \"Sofia\"");
		assertThat(exp).isEqualTo(new Filter.Expression(Filter.ExpressionType.OR,
				new Filter.Expression(Filter.ExpressionType.GTE, new Filter.Key("year"), new Filter.Value(2020)),
				new Filter.Expression(Filter.ExpressionType.AND,
						new Filter.Expression(Filter.ExpressionType.EQ, new Filter.Key("country"),
								new Filter.Value("BG")),
						new Filter.Expression(Filter.ExpressionType.NE, new Filter.Key("city"),
								new Filter.Value("Sofia")))));

		assertThat(parser.getCache().get("WHERE " + "year >= 2020 OR country == \"BG\" AND city != \"Sofia\""))
			.isEqualTo(exp);
	}

	@Test
	public void testGroup() {
		// (year >= 2020 OR country == "BG") AND city NIN ["Sofia", "Plovdiv"]
		Filter.Expression exp = parser
			.parse("(year >= 2020 OR country == \"BG\") AND city NIN [\"Sofia\", \"Plovdiv\"]");

		assertThat(exp)
			.isEqualTo(new Filter.Expression(Filter.ExpressionType.AND, new Filter.Group(new Filter.Expression(
					Filter.ExpressionType.OR,
					new Filter.Expression(Filter.ExpressionType.GTE, new Filter.Key("year"), new Filter.Value(2020)),
					new Filter.Expression(Filter.ExpressionType.EQ, new Filter.Key("country"),
							new Filter.Value("BG")))),
					new Filter.Expression(Filter.ExpressionType.NIN, new Filter.Key("city"),
							new Filter.Value(List.of("Sofia", "Plovdiv")))));

		assertThat(parser.getCache()
			.get("WHERE " + "(year >= 2020 OR country == \"BG\") AND city NIN [\"Sofia\", \"Plovdiv\"]"))
			.isEqualTo(exp);
	}

	@Test
	public void tesBoolean() {
		// isOpen == true AND year >= 2020 AND country IN ["BG", "NL", "US"]
		Filter.Expression exp = parser.parse("isOpen == true AND year >= 2020 AND country IN [\"BG\", \"NL\", \"US\"]");

		assertThat(exp)
			.isEqualTo(new Filter.Expression(Filter.ExpressionType.AND, new Filter.Expression(Filter.ExpressionType.AND,
					new Filter.Expression(Filter.ExpressionType.EQ, new Filter.Key("isOpen"), new Filter.Value(true)),
					new Filter.Expression(Filter.ExpressionType.GTE, new Filter.Key("year"), new Filter.Value(2020))),
					new Filter.Expression(Filter.ExpressionType.IN, new Filter.Key("country"),
							new Filter.Value(List.of("BG", "NL", "US")))));
		assertThat(parser.getCache()
			.get("WHERE " + "isOpen == true AND year >= 2020 AND country IN [\"BG\", \"NL\", \"US\"]")).isEqualTo(exp);
	}

	@Test
	public void tesNot() {
		// NOT(isOpen == true AND year >= 2020 AND country IN ["BG", "NL", "US"])
		Filter.Expression exp = parser
			.parse("not(isOpen == true AND year >= 2020 AND country IN [\"BG\", \"NL\", \"US\"])");

		assertThat(exp).isEqualTo(new Filter.Expression(Filter.ExpressionType.NOT,
				new Filter.Group(new Filter.Expression(Filter.ExpressionType.AND,
						new Filter.Expression(Filter.ExpressionType.AND,
								new Filter.Expression(Filter.ExpressionType.EQ, new Filter.Key("isOpen"),
										new Filter.Value(true)),
								new Filter.Expression(Filter.ExpressionType.GTE, new Filter.Key("year"),
										new Filter.Value(2020))),
						new Filter.Expression(Filter.ExpressionType.IN, new Filter.Key("country"),
								new Filter.Value(List.of("BG", "NL", "US"))))),
				null));

		assertThat(parser.getCache()
			.get("WHERE " + "not(isOpen == true AND year >= 2020 AND country IN [\"BG\", \"NL\", \"US\"])"))
			.isEqualTo(exp);
	}

	@Test
	public void tesNotNin() {
		// NOT(country NOT IN ["BG", "NL", "US"])
		Filter.Expression exp = parser.parse("not(country NOT IN [\"BG\", \"NL\", \"US\"])");

		assertThat(
				exp)
			.isEqualTo(new Filter.Expression(Filter.ExpressionType.NOT,
					new Filter.Group(new Filter.Expression(Filter.ExpressionType.NIN, new Filter.Key("country"),
							new Filter.Value(List.of("BG", "NL", "US")))),
					null));
	}

	@Test
	public void tesNotNin2() {
		// NOT country NOT IN ["BG", "NL", "US"]
		Filter.Expression exp = parser.parse("NOT country NOT IN [\"BG\", \"NL\", \"US\"]");

		assertThat(exp)
			.isEqualTo(new Filter.Expression(Filter.ExpressionType.NOT, new Filter.Expression(Filter.ExpressionType.NIN,
					new Filter.Key("country"), new Filter.Value(List.of("BG", "NL", "US"))), null));
	}

	@Test
	public void tesNestedNot() {
		// NOT(isOpen == true AND year >= 2020 AND NOT(country IN ["BG", "NL", "US"]))
		Filter.Expression exp = parser
			.parse("not(isOpen == true AND year >= 2020 AND NOT(country IN [\"BG\", \"NL\", \"US\"]))");

		assertThat(exp).isEqualTo(new Filter.Expression(Filter.ExpressionType.NOT,
				new Filter.Group(new Filter.Expression(Filter.ExpressionType.AND,
						new Filter.Expression(Filter.ExpressionType.AND,
								new Filter.Expression(Filter.ExpressionType.EQ, new Filter.Key("isOpen"),
										new Filter.Value(true)),
								new Filter.Expression(Filter.ExpressionType.GTE, new Filter.Key("year"),
										new Filter.Value(2020))),
						new Filter.Expression(Filter.ExpressionType.NOT,
								new Filter.Group(new Filter.Expression(Filter.ExpressionType.IN,
										new Filter.Key("country"), new Filter.Value(List.of("BG", "NL", "US")))),
								null))),
				null));

		assertThat(parser.getCache()
			.get("WHERE " + "not(isOpen == true AND year >= 2020 AND NOT(country IN [\"BG\", \"NL\", \"US\"]))"))
			.isEqualTo(exp);
	}

	@Test
	public void testDecimal() {
		// temperature >= -15.6 && temperature <= +20.13
		String expText = "temperature >= -15.6 && temperature <= +20.13";
		Filter.Expression exp = parser.parse(expText);

		assertThat(exp).isEqualTo(new Filter.Expression(Filter.ExpressionType.AND,
				new Filter.Expression(Filter.ExpressionType.GTE, new Filter.Key("temperature"),
						new Filter.Value(-15.6)),
				new Filter.Expression(Filter.ExpressionType.LTE, new Filter.Key("temperature"),
						new Filter.Value(20.13))));

		assertThat(parser.getCache().get("WHERE " + expText)).isEqualTo(exp);
	}

	@Test
	public void testIdentifiers() {
		Filter.Expression exp = parser.parse("'country.1' == 'BG'");
		assertThat(exp).isEqualTo(
				new Filter.Expression(Filter.ExpressionType.EQ, new Filter.Key("'country.1'"), new Filter.Value("BG")));

		exp = parser.parse("'country_1_2_3' == 'BG'");
		assertThat(exp).isEqualTo(new Filter.Expression(Filter.ExpressionType.EQ, new Filter.Key("'country_1_2_3'"),
				new Filter.Value("BG")));

		exp = parser.parse("\"country 1 2 3\" == 'BG'");
		assertThat(exp).isEqualTo(new Filter.Expression(Filter.ExpressionType.EQ, new Filter.Key("\"country 1 2 3\""),
				new Filter.Value("BG")));

		exp = parser.parse("_id1 == 100");
		assertThat(exp)
			.isEqualTo(new Filter.Expression(Filter.ExpressionType.EQ, new Filter.Key("_id1"), new Filter.Value(100)));

		exp = parser.parse("example.com/foo == 'bar'");
		assertThat(exp).isEqualTo(new Filter.Expression(Filter.ExpressionType.EQ, new Filter.Key("example.com/foo"),
				new Filter.Value("bar")));
	}

}