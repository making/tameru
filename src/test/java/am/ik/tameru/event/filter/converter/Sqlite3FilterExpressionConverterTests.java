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

package am.ik.tameru.event.filter.converter;

import java.util.List;

import am.ik.tameru.event.filter.Filter;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Toshiaki Maki
 */
public class Sqlite3FilterExpressionConverterTests {

	FilterExpressionConverter converter = new Sqlite3FilterExpressionConverter();

	@Test
	public void testEQ() {
		// country == "BG"
		String vectorExpr = converter.convertExpression(
				new Filter.Expression(Filter.ExpressionType.EQ, new Filter.Key("country"), new Filter.Value("BG")));
		assertThat(vectorExpr).isEqualTo("json_extract(metadata, '$.country') == \"BG\"");
	}

	@Test
	public void tesEqAndGte() {
		// genre == "drama" AND year >= 2020
		String vectorExpr = converter.convertExpression(new Filter.Expression(Filter.ExpressionType.AND,
				new Filter.Expression(Filter.ExpressionType.EQ, new Filter.Key("genre"), new Filter.Value("drama")),
				new Filter.Expression(Filter.ExpressionType.GTE, new Filter.Key("year"), new Filter.Value(2020))));
		assertThat(vectorExpr)
			.isEqualTo("json_extract(metadata, '$.genre') == \"drama\" AND json_extract(metadata, '$.year') >= 2020");
	}

	@Test
	public void tesIn() {
		// genre in ["comedy", "documentary", "drama"]
		String vectorExpr = converter.convertExpression(new Filter.Expression(Filter.ExpressionType.IN,
				new Filter.Key("genre"), new Filter.Value(List.of("comedy", "documentary", "drama"))));
		assertThat(vectorExpr).isEqualTo("json_extract(metadata, '$.genre') IN [\"comedy\",\"documentary\",\"drama\"]");
	}

	@Test
	public void testNe() {
		// year >= 2020 OR country == "BG" AND city != "Sofia"
		String vectorExpr = converter.convertExpression(new Filter.Expression(Filter.ExpressionType.OR,
				new Filter.Expression(Filter.ExpressionType.GTE, new Filter.Key("year"), new Filter.Value(2020)),
				new Filter.Expression(Filter.ExpressionType.AND,
						new Filter.Expression(Filter.ExpressionType.EQ, new Filter.Key("country"),
								new Filter.Value("BG")),
						new Filter.Expression(Filter.ExpressionType.NE, new Filter.Key("city"),
								new Filter.Value("Sofia")))));
		assertThat(vectorExpr).isEqualTo(
				"json_extract(metadata, '$.year') >= 2020 OR json_extract(metadata, '$.country') == \"BG\" AND json_extract(metadata, '$.city') != \"Sofia\"");
	}

	@Test
	public void testGroup() {
		// (year >= 2020 OR country == "BG") AND city NIN ["Sofia", "Plovdiv"]
		String vectorExpr = converter
			.convertExpression(new Filter.Expression(Filter.ExpressionType.AND, new Filter.Group(new Filter.Expression(
					Filter.ExpressionType.OR,
					new Filter.Expression(Filter.ExpressionType.GTE, new Filter.Key("year"), new Filter.Value(2020)),
					new Filter.Expression(Filter.ExpressionType.EQ, new Filter.Key("country"),
							new Filter.Value("BG")))),
					new Filter.Expression(Filter.ExpressionType.NIN, new Filter.Key("city"),
							new Filter.Value(List.of("Sofia", "Plovdiv")))));
		assertThat(vectorExpr).isEqualTo(
				"(json_extract(metadata, '$.year') >= 2020 OR json_extract(metadata, '$.country') == \"BG\") AND json_extract(metadata, '$.city') NIN [\"Sofia\",\"Plovdiv\"]");
	}

	@Test
	public void tesBoolean() {
		// isOpen == true AND year >= 2020 AND country IN ["BG", "NL", "US"]
		String vectorExpr = converter
			.convertExpression(new Filter.Expression(Filter.ExpressionType.AND, new Filter.Expression(
					Filter.ExpressionType.AND,
					new Filter.Expression(Filter.ExpressionType.EQ, new Filter.Key("isOpen"), new Filter.Value(true)),
					new Filter.Expression(Filter.ExpressionType.GTE, new Filter.Key("year"), new Filter.Value(2020))),
					new Filter.Expression(Filter.ExpressionType.IN, new Filter.Key("country"),
							new Filter.Value(List.of("BG", "NL", "US")))));

		assertThat(vectorExpr).isEqualTo(
				"json_extract(metadata, '$.isOpen') == true AND json_extract(metadata, '$.year') >= 2020 AND json_extract(metadata, '$.country') IN [\"BG\",\"NL\",\"US\"]");
	}

	@Test
	public void testDecimal() {
		// temperature >= -15.6 && temperature <= +20.13
		String vectorExpr = converter.convertExpression(new Filter.Expression(Filter.ExpressionType.AND,
				new Filter.Expression(Filter.ExpressionType.GTE, new Filter.Key("temperature"),
						new Filter.Value(-15.6)),
				new Filter.Expression(Filter.ExpressionType.LTE, new Filter.Key("temperature"),
						new Filter.Value(20.13))));

		assertThat(vectorExpr).isEqualTo(
				"json_extract(metadata, '$.temperature') >= -15.6 AND json_extract(metadata, '$.temperature') <= 20.13");
	}

	@Test
	public void testComplexIdentifiers() {
		String vectorExpr = converter.convertExpression(new Filter.Expression(Filter.ExpressionType.EQ,
				new Filter.Key("\"country 1 2 3\""), new Filter.Value("BG")));
		assertThat(vectorExpr).isEqualTo("json_extract(metadata, '$.\"country 1 2 3\"') == \"BG\"");
	}

}