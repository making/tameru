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

import am.ik.tameru.event.filter.converter.PrintFilterExpressionConverter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Christian Tzolov
 */
public class FilterHelperTests {

	@Test
	public void negateEQ() {
		assertThat(Filter.parser().parse("NOT key == 'UK' ")).isEqualTo(new Filter.Expression(Filter.ExpressionType.NOT,
				new Filter.Expression(Filter.ExpressionType.EQ, new Filter.Key("key"), new Filter.Value("UK")), null));

		assertThat(FilterHelper.negate(Filter.parser().parse("NOT key == 'UK' ")))
			.isEqualTo(new Filter.Expression(Filter.ExpressionType.NE, new Filter.Key("key"), new Filter.Value("UK")));

		assertThat(FilterHelper.negate(Filter.parser().parse("NOT (key == 'UK') "))).isEqualTo(new Filter.Group(
				new Filter.Expression(Filter.ExpressionType.NE, new Filter.Key("key"), new Filter.Value("UK"))));
	}

	@Test
	public void negateNE() {
		var exp = Filter.parser().parse("NOT key != 'UK' ");
		assertThat(FilterHelper.negate(exp))
			.isEqualTo(new Filter.Expression(Filter.ExpressionType.EQ, new Filter.Key("key"), new Filter.Value("UK")));

	}

	@Test
	public void negateGT() {
		var exp = Filter.parser().parse("NOT key > 13 ");
		assertThat(FilterHelper.negate(exp))
			.isEqualTo(new Filter.Expression(Filter.ExpressionType.LTE, new Filter.Key("key"), new Filter.Value(13)));

	}

	@Test
	public void negateGTE() {
		var exp = Filter.parser().parse("NOT key >= 13 ");
		assertThat(FilterHelper.negate(exp))
			.isEqualTo(new Filter.Expression(Filter.ExpressionType.LT, new Filter.Key("key"), new Filter.Value(13)));
	}

	@Test
	public void negateLT() {
		var exp = Filter.parser().parse("NOT key < 13 ");
		assertThat(FilterHelper.negate(exp))
			.isEqualTo(new Filter.Expression(Filter.ExpressionType.GTE, new Filter.Key("key"), new Filter.Value(13)));
	}

	@Test
	public void negateLTE() {
		var exp = Filter.parser().parse("NOT key <= 13 ");
		assertThat(FilterHelper.negate(exp))
			.isEqualTo(new Filter.Expression(Filter.ExpressionType.GT, new Filter.Key("key"), new Filter.Value(13)));
	}

	@Test
	public void negateIN() {
		var exp = Filter.parser().parse("NOT key IN [11, 12, 13] ");
		assertThat(FilterHelper.negate(exp)).isEqualTo(new Filter.Expression(Filter.ExpressionType.NIN,
				new Filter.Key("key"), new Filter.Value(List.of(11, 12, 13))));
	}

	@Test
	public void negateNIN() {
		var exp = Filter.parser().parse("NOT key NIN [11, 12, 13] ");
		assertThat(FilterHelper.negate(exp)).isEqualTo(new Filter.Expression(Filter.ExpressionType.IN,
				new Filter.Key("key"), new Filter.Value(List.of(11, 12, 13))));
	}

	@Test
	public void negateNIN2() {
		var exp = Filter.parser().parse("NOT key NOT IN [11, 12, 13] ");
		assertThat(FilterHelper.negate(exp)).isEqualTo(new Filter.Expression(Filter.ExpressionType.IN,
				new Filter.Key("key"), new Filter.Value(List.of(11, 12, 13))));
	}

	@Test
	public void negateAND() {
		var exp = Filter.parser().parse("NOT(key >= 11 AND key < 13)");
		assertThat(FilterHelper.negate(exp)).isEqualTo(new Filter.Group(new Filter.Expression(Filter.ExpressionType.OR,
				new Filter.Expression(Filter.ExpressionType.LT, new Filter.Key("key"), new Filter.Value(11)),
				new Filter.Expression(Filter.ExpressionType.GTE, new Filter.Key("key"), new Filter.Value(13)))));
	}

	@Test
	public void negateOR() {
		var exp = Filter.parser().parse("NOT(key >= 11 OR key < 13)");
		assertThat(FilterHelper.negate(exp)).isEqualTo(new Filter.Group(new Filter.Expression(Filter.ExpressionType.AND,
				new Filter.Expression(Filter.ExpressionType.LT, new Filter.Key("key"), new Filter.Value(11)),
				new Filter.Expression(Filter.ExpressionType.GTE, new Filter.Key("key"), new Filter.Value(13)))));
	}

	@Test
	public void negateNot() {
		var exp = Filter.parser().parse("NOT NOT(key >= 11)");
		assertThat(FilterHelper.negate(exp)).isEqualTo(new Filter.Group(
				new Filter.Expression(Filter.ExpressionType.LT, new Filter.Key("key"), new Filter.Value(11))));
	}

	@Test
	public void negateNestedNot() {
		var exp = Filter.parser().parse("NOT(NOT(key >= 11))");
		assertThat(exp).isEqualTo(new Filter.Expression(Filter.ExpressionType.NOT,
				new Filter.Group(new Filter.Expression(Filter.ExpressionType.NOT,
						new Filter.Group(new Filter.Expression(Filter.ExpressionType.GTE, new Filter.Key("key"),
								new Filter.Value(11)))))));

		assertThat(FilterHelper.negate(exp)).isEqualTo(new Filter.Group(
				new Filter.Expression(Filter.ExpressionType.LT, new Filter.Key("key"), new Filter.Value(11))));
	}

	@Test
	public void expandIN() {
		var exp = Filter.parser().parse("key IN [11, 12, 13] ");
		Assertions.assertThat(new InNinTestConverter().convertExpression(exp))
			.isEqualTo("key EQ 11 OR key EQ 12 OR key EQ 13");
	}

	@Test
	public void expandNIN() {
		var exp1 = Filter.parser().parse("key NIN [11, 12, 13] ");
		var exp2 = Filter.parser().parse("key NOT IN [11, 12, 13] ");
		assertThat(exp1).isEqualTo(exp2);
		Assertions.assertThat(new InNinTestConverter().convertExpression(exp1))
			.isEqualTo("key NE 11 AND key NE 12 AND key NE 13");
	}

	private static class InNinTestConverter extends PrintFilterExpressionConverter {

		@Override
		public void doExpression(Filter.Expression expression, StringBuilder context) {
			if (expression.type() == Filter.ExpressionType.IN) {
				FilterHelper.expandIn(expression, context, this);
			}
			else if (expression.type() == Filter.ExpressionType.NIN) {
				FilterHelper.expandNin(expression, context, this);
			}
			else {
				super.doExpression(expression, context);
			}
		}

	};

}