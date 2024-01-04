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
public class FilterExpressionBuilderTests {

	FilterExpressionBuilder b = new FilterExpressionBuilder();

	@Test
	public void testEQ() {
		// country == "BG"
		assertThat(b.eq("country", "BG").build()).isEqualTo(
				new Filter.Expression(Filter.ExpressionType.EQ, new Filter.Key("country"), new Filter.Value("BG")));
	}

	@Test
	public void tesEqAndGte() {
		// genre == "drama" AND year >= 2020
		Filter.Expression exp = b.and(b.eq("genre", "drama"), b.gte("year", 2020)).build();
		assertThat(exp).isEqualTo(new Filter.Expression(Filter.ExpressionType.AND,
				new Filter.Expression(Filter.ExpressionType.EQ, new Filter.Key("genre"), new Filter.Value("drama")),
				new Filter.Expression(Filter.ExpressionType.GTE, new Filter.Key("year"), new Filter.Value(2020))));
	}

	@Test
	public void testIn() {
		// genre in ["comedy", "documentary", "drama"]
		var exp = b.in("genre", "comedy", "documentary", "drama").build();
		assertThat(exp).isEqualTo(new Filter.Expression(Filter.ExpressionType.IN, new Filter.Key("genre"),
				new Filter.Value(List.of("comedy", "documentary", "drama"))));
	}

	@Test
	public void testNe() {
		// year >= 2020 OR country == "BG" AND city != "Sofia"
		var exp = b.and(b.or(b.gte("year", 2020), b.eq("country", "BG")), b.ne("city", "Sofia")).build();

		assertThat(exp).isEqualTo(new Filter.Expression(Filter.ExpressionType.AND, new Filter.Expression(
				Filter.ExpressionType.OR,
				new Filter.Expression(Filter.ExpressionType.GTE, new Filter.Key("year"), new Filter.Value(2020)),
				new Filter.Expression(Filter.ExpressionType.EQ, new Filter.Key("country"), new Filter.Value("BG"))),
				new Filter.Expression(Filter.ExpressionType.NE, new Filter.Key("city"), new Filter.Value("Sofia"))));
	}

	@Test
	public void testGroup() {
		// (year >= 2020 OR country == "BG") AND city NIN ["Sofia", "Plovdiv"]
		var exp = b.and(b.group(b.or(b.gte("year", 2020), b.eq("country", "BG"))), b.nin("city", "Sofia", "Plovdiv"))
			.build();

		assertThat(exp)
			.isEqualTo(new Filter.Expression(Filter.ExpressionType.AND, new Filter.Group(new Filter.Expression(
					Filter.ExpressionType.OR,
					new Filter.Expression(Filter.ExpressionType.GTE, new Filter.Key("year"), new Filter.Value(2020)),
					new Filter.Expression(Filter.ExpressionType.EQ, new Filter.Key("country"),
							new Filter.Value("BG")))),
					new Filter.Expression(Filter.ExpressionType.NIN, new Filter.Key("city"),
							new Filter.Value(List.of("Sofia", "Plovdiv")))));
	}

	@Test
	public void tesIn2() {
		// isOpen == true AND year >= 2020 AND country IN ["BG", "NL", "US"]
		var exp = b.and(b.and(b.eq("isOpen", true), b.gte("year", 2020)), b.in("country", "BG", "NL", "US")).build();

		assertThat(exp)
			.isEqualTo(new Filter.Expression(Filter.ExpressionType.AND, new Filter.Expression(Filter.ExpressionType.AND,
					new Filter.Expression(Filter.ExpressionType.EQ, new Filter.Key("isOpen"), new Filter.Value(true)),
					new Filter.Expression(Filter.ExpressionType.GTE, new Filter.Key("year"), new Filter.Value(2020))),
					new Filter.Expression(Filter.ExpressionType.IN, new Filter.Key("country"),
							new Filter.Value(List.of("BG", "NL", "US")))));
	}

	@Test
	public void tesNot() {
		// isOpen == true AND year >= 2020 AND country IN ["BG", "NL", "US"]
		var exp = b.not(b.and(b.and(b.eq("isOpen", true), b.gte("year", 2020)), b.in("country", "BG", "NL", "US")))
			.build();

		assertThat(exp).isEqualTo(new Filter.Expression(Filter.ExpressionType.NOT,
				new Filter.Expression(Filter.ExpressionType.AND,
						new Filter.Expression(Filter.ExpressionType.AND,
								new Filter.Expression(Filter.ExpressionType.EQ, new Filter.Key("isOpen"),
										new Filter.Value(true)),
								new Filter.Expression(Filter.ExpressionType.GTE, new Filter.Key("year"),
										new Filter.Value(2020))),
						new Filter.Expression(Filter.ExpressionType.IN, new Filter.Key("country"),
								new Filter.Value(List.of("BG", "NL", "US")))),
				null));
	}

}