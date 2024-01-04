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

package lol.maki.tameru.event.filter.converter;

import java.util.List;

import lol.maki.tameru.event.filter.Filter.Expression;
import lol.maki.tameru.event.filter.Filter.Group;
import lol.maki.tameru.event.filter.Filter.Key;
import lol.maki.tameru.event.filter.Filter.Value;
import org.junit.jupiter.api.Test;

import static lol.maki.tameru.event.filter.Filter.ExpressionType.AND;
import static lol.maki.tameru.event.filter.Filter.ExpressionType.EQ;
import static lol.maki.tameru.event.filter.Filter.ExpressionType.GTE;
import static lol.maki.tameru.event.filter.Filter.ExpressionType.IN;
import static lol.maki.tameru.event.filter.Filter.ExpressionType.LTE;
import static lol.maki.tameru.event.filter.Filter.ExpressionType.NE;
import static lol.maki.tameru.event.filter.Filter.ExpressionType.NIN;
import static lol.maki.tameru.event.filter.Filter.ExpressionType.OR;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Toshiaki Maki
 */
public class Sqlite3FilterExpressionConverterTests {

	FilterExpressionConverter converter = new Sqlite3FilterExpressionConverter();

	@Test
	public void testEQ() {
		// country == "BG"
		String vectorExpr = converter.convertExpression(new Expression(EQ, new Key("country"), new Value("BG")));
		assertThat(vectorExpr).isEqualTo("json_extract(metadata, '$.country') == \"BG\"");
	}

	@Test
	public void tesEqAndGte() {
		// genre == "drama" AND year >= 2020
		String vectorExpr = converter
			.convertExpression(new Expression(AND, new Expression(EQ, new Key("genre"), new Value("drama")),
					new Expression(GTE, new Key("year"), new Value(2020))));
		assertThat(vectorExpr)
			.isEqualTo("json_extract(metadata, '$.genre') == \"drama\" && json_extract(metadata, '$.year') >= 2020");
	}

	@Test
	public void tesIn() {
		// genre in ["comedy", "documentary", "drama"]
		String vectorExpr = converter.convertExpression(
				new Expression(IN, new Key("genre"), new Value(List.of("comedy", "documentary", "drama"))));
		assertThat(vectorExpr).isEqualTo("json_extract(metadata, '$.genre') in [\"comedy\",\"documentary\",\"drama\"]");
	}

	@Test
	public void testNe() {
		// year >= 2020 OR country == "BG" AND city != "Sofia"
		String vectorExpr = converter
			.convertExpression(new Expression(OR, new Expression(GTE, new Key("year"), new Value(2020)),
					new Expression(AND, new Expression(EQ, new Key("country"), new Value("BG")),
							new Expression(NE, new Key("city"), new Value("Sofia")))));
		assertThat(vectorExpr).isEqualTo(
				"json_extract(metadata, '$.year') >= 2020 || json_extract(metadata, '$.country') == \"BG\" && json_extract(metadata, '$.city') != \"Sofia\"");
	}

	@Test
	public void testGroup() {
		// (year >= 2020 OR country == "BG") AND city NIN ["Sofia", "Plovdiv"]
		String vectorExpr = converter.convertExpression(new Expression(AND,
				new Group(new Expression(OR, new Expression(GTE, new Key("year"), new Value(2020)),
						new Expression(EQ, new Key("country"), new Value("BG")))),
				new Expression(NIN, new Key("city"), new Value(List.of("Sofia", "Plovdiv")))));
		assertThat(vectorExpr).isEqualTo(
				"(json_extract(metadata, '$.year') >= 2020 || json_extract(metadata, '$.country') == \"BG\") && json_extract(metadata, '$.city') nin [\"Sofia\",\"Plovdiv\"]");
	}

	@Test
	public void tesBoolean() {
		// isOpen == true AND year >= 2020 AND country IN ["BG", "NL", "US"]
		String vectorExpr = converter.convertExpression(new Expression(AND,
				new Expression(AND, new Expression(EQ, new Key("isOpen"), new Value(true)),
						new Expression(GTE, new Key("year"), new Value(2020))),
				new Expression(IN, new Key("country"), new Value(List.of("BG", "NL", "US")))));

		assertThat(vectorExpr).isEqualTo(
				"json_extract(metadata, '$.isOpen') == true && json_extract(metadata, '$.year') >= 2020 && json_extract(metadata, '$.country') in [\"BG\",\"NL\",\"US\"]");
	}

	@Test
	public void testDecimal() {
		// temperature >= -15.6 && temperature <= +20.13
		String vectorExpr = converter
			.convertExpression(new Expression(AND, new Expression(GTE, new Key("temperature"), new Value(-15.6)),
					new Expression(LTE, new Key("temperature"), new Value(20.13))));

		assertThat(vectorExpr).isEqualTo(
				"json_extract(metadata, '$.temperature') >= -15.6 && json_extract(metadata, '$.temperature') <= 20.13");
	}

	@Test
	public void testComplexIdentifiers() {
		String vectorExpr = converter
			.convertExpression(new Expression(EQ, new Key("\"country 1 2 3\""), new Value("BG")));
		assertThat(vectorExpr).isEqualTo("json_extract(metadata, '$.\"country 1 2 3\"') == \"BG\"");
	}

}