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

import am.ik.tameru.event.filter.Filter;

/**
 * Converts {@link Filter.Expression} into SQLite3 metadata filter expression format.
 * (<a href="https://www.sqlite.org/json1.html">https://www.sqlite.org/json1.html</a>)
 *
 * @author Toshiaki Maki
 */
public class Sqlite3FilterExpressionConverter extends AbstractFilterExpressionConverter {

	@Override
	protected void doExpression(Filter.Expression expression, StringBuilder context) {
		this.convertOperand(expression.left(), context);
		context.append(getOperationSymbol(expression));
		this.convertOperand(expression.right(), context);
	}

	private String getOperationSymbol(Filter.Expression exp) {
		switch (exp.type()) {
			case AND:
				return " AND ";
			case OR:
				return " OR ";
			case EQ:
				return " == ";
			case NE:
				return " != ";
			case LT:
				return " < ";
			case LTE:
				return " <= ";
			case GT:
				return " > ";
			case GTE:
				return " >= ";
			case IN:
				return " IN ";
			case NIN:
				return " NIN ";
			default:
				throw new RuntimeException("Not supported expression type: " + exp.type());
		}
	}

	@Override
	protected void doKey(Filter.Key key, StringBuilder context) {
		context.append("json_extract(metadata, '$." + key.key() + "')");
	}

	@Override
	protected void doStartGroup(Filter.Group group, StringBuilder context) {
		context.append("(");
	}

	@Override
	protected void doEndGroup(Filter.Group group, StringBuilder context) {
		context.append(")");
	}

}