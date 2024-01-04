// Generated from am/ik/tameru/event/filter/antlr4/Filters.g4 by ANTLR 4.13.1
package am.ik.tameru.event.filter.antlr4;

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

// ############################################################
// # NOTE: This is ANTLR4 auto-generated code. Do not modify! #
// ############################################################

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({ "all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape" })
public class FiltersLexer extends Lexer {

	static {
		RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION);
	}

	protected static final DFA[] _decisionToDFA;

	protected static final PredictionContextCache _sharedContextCache = new PredictionContextCache();

	public static final int WHERE = 1, DOT = 2, COMMA = 3, LEFT_SQUARE_BRACKETS = 4, RIGHT_SQUARE_BRACKETS = 5,
			LEFT_PARENTHESIS = 6, RIGHT_PARENTHESIS = 7, EQUALS = 8, MINUS = 9, PLUS = 10, GT = 11, GE = 12, LT = 13,
			LE = 14, NE = 15, AND = 16, OR = 17, IN = 18, NIN = 19, NOT = 20, BOOLEAN_VALUE = 21, QUOTED_STRING = 22,
			INTEGER_VALUE = 23, DECIMAL_VALUE = 24, IDENTIFIER = 25, WS = 26;

	public static String[] channelNames = { "DEFAULT_TOKEN_CHANNEL", "HIDDEN" };

	public static String[] modeNames = { "DEFAULT_MODE" };

	private static String[] makeRuleNames() {
		return new String[] { "WHERE", "DOT", "COMMA", "LEFT_SQUARE_BRACKETS", "RIGHT_SQUARE_BRACKETS",
				"LEFT_PARENTHESIS", "RIGHT_PARENTHESIS", "EQUALS", "MINUS", "PLUS", "GT", "GE", "LT", "LE", "NE", "AND",
				"OR", "IN", "NIN", "NOT", "BOOLEAN_VALUE", "QUOTED_STRING", "INTEGER_VALUE", "DECIMAL_VALUE",
				"IDENTIFIER", "DECIMAL_DIGITS", "DIGIT", "LETTER", "SYMBOL", "WS" };
	}

	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] { null, null, "'.'", "','", "'['", "']'", "'('", "')'", "'=='", "'-'", "'+'", "'>'", "'>='",
				"'<'", "'<='", "'!='" };
	}

	private static final String[] _LITERAL_NAMES = makeLiteralNames();

	private static String[] makeSymbolicNames() {
		return new String[] { null, "WHERE", "DOT", "COMMA", "LEFT_SQUARE_BRACKETS", "RIGHT_SQUARE_BRACKETS",
				"LEFT_PARENTHESIS", "RIGHT_PARENTHESIS", "EQUALS", "MINUS", "PLUS", "GT", "GE", "LT", "LE", "NE", "AND",
				"OR", "IN", "NIN", "NOT", "BOOLEAN_VALUE", "QUOTED_STRING", "INTEGER_VALUE", "DECIMAL_VALUE",
				"IDENTIFIER", "WS" };
	}

	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();

	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	public FiltersLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
	}

	@Override
	public String getGrammarFileName() {
		return "Filters.g4";
	}

	@Override
	public String[] getRuleNames() {
		return ruleNames;
	}

	@Override
	public String getSerializedATN() {
		return _serializedATN;
	}

	@Override
	public String[] getChannelNames() {
		return channelNames;
	}

	@Override
	public String[] getModeNames() {
		return modeNames;
	}

	@Override
	public ATN getATN() {
		return _ATN;
	}

	public static final String _serializedATN = "\u0004\u0000\u001a\u00ea\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"
			+ "\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002"
			+ "\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002"
			+ "\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002"
			+ "\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e"
			+ "\u0002\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011"
			+ "\u0002\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014"
			+ "\u0002\u0015\u0007\u0015\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017"
			+ "\u0002\u0018\u0007\u0018\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a"
			+ "\u0002\u001b\u0007\u001b\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d"
			+ "\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"
			+ "\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0003\u0000H\b\u0000"
			+ "\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003"
			+ "\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006"
			+ "\u0001\u0007\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\t\u0001\t\u0001"
			+ "\n\u0001\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\r"
			+ "\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0001"
			+ "\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001"
			+ "\u000f\u0003\u000fr\b\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0001"
			+ "\u0010\u0001\u0010\u0001\u0010\u0003\u0010z\b\u0010\u0001\u0011\u0001"
			+ "\u0011\u0001\u0011\u0001\u0011\u0003\u0011\u0080\b\u0011\u0001\u0012\u0001"
			+ "\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0003\u0012\u0088"
			+ "\b\u0012\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001"
			+ "\u0013\u0003\u0013\u0090\b\u0013\u0001\u0014\u0001\u0014\u0001\u0014\u0001"
			+ "\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001"
			+ "\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001"
			+ "\u0014\u0001\u0014\u0001\u0014\u0003\u0014\u00a4\b\u0014\u0001\u0015\u0001"
			+ "\u0015\u0001\u0015\u0001\u0015\u0005\u0015\u00aa\b\u0015\n\u0015\f\u0015"
			+ "\u00ad\t\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015"
			+ "\u0005\u0015\u00b4\b\u0015\n\u0015\f\u0015\u00b7\t\u0015\u0001\u0015\u0003"
			+ "\u0015\u00ba\b\u0015\u0001\u0016\u0004\u0016\u00bd\b\u0016\u000b\u0016"
			+ "\f\u0016\u00be\u0001\u0017\u0001\u0017\u0001\u0018\u0001\u0018\u0001\u0018"
			+ "\u0004\u0018\u00c6\b\u0018\u000b\u0018\f\u0018\u00c7\u0001\u0019\u0004"
			+ "\u0019\u00cb\b\u0019\u000b\u0019\f\u0019\u00cc\u0001\u0019\u0001\u0019"
			+ "\u0005\u0019\u00d1\b\u0019\n\u0019\f\u0019\u00d4\t\u0019\u0001\u0019\u0001"
			+ "\u0019\u0004\u0019\u00d8\b\u0019\u000b\u0019\f\u0019\u00d9\u0003\u0019"
			+ "\u00dc\b\u0019\u0001\u001a\u0001\u001a\u0001\u001b\u0001\u001b\u0001\u001c"
			+ "\u0001\u001c\u0001\u001d\u0004\u001d\u00e5\b\u001d\u000b\u001d\f\u001d"
			+ "\u00e6\u0001\u001d\u0001\u001d\u0000\u0000\u001e\u0001\u0001\u0003\u0002"
			+ "\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006\r\u0007\u000f\b\u0011\t\u0013"
			+ "\n\u0015\u000b\u0017\f\u0019\r\u001b\u000e\u001d\u000f\u001f\u0010!\u0011"
			+ "#\u0012%\u0013\'\u0014)\u0015+\u0016-\u0017/\u00181\u00193\u00005\u0000"
			+ "7\u00009\u0000;\u001a\u0001\u0000\u0006\u0002\u0000\'\'\\\\\u0002\u0000"
			+ "\"\"\\\\\u0001\u000009\u0002\u0000AZaz\u0002\u0000//__\u0003\u0000\t\n"
			+ "\r\r  \u00fe\u0000\u0001\u0001\u0000\u0000\u0000\u0000\u0003\u0001\u0000"
			+ "\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000\u0007\u0001\u0000"
			+ "\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000\u000b\u0001\u0000\u0000"
			+ "\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f\u0001\u0000\u0000\u0000"
			+ "\u0000\u0011\u0001\u0000\u0000\u0000\u0000\u0013\u0001\u0000\u0000\u0000"
			+ "\u0000\u0015\u0001\u0000\u0000\u0000\u0000\u0017\u0001\u0000\u0000\u0000"
			+ "\u0000\u0019\u0001\u0000\u0000\u0000\u0000\u001b\u0001\u0000\u0000\u0000"
			+ "\u0000\u001d\u0001\u0000\u0000\u0000\u0000\u001f\u0001\u0000\u0000\u0000"
			+ "\u0000!\u0001\u0000\u0000\u0000\u0000#\u0001\u0000\u0000\u0000\u0000%"
			+ "\u0001\u0000\u0000\u0000\u0000\'\u0001\u0000\u0000\u0000\u0000)\u0001"
			+ "\u0000\u0000\u0000\u0000+\u0001\u0000\u0000\u0000\u0000-\u0001\u0000\u0000"
			+ "\u0000\u0000/\u0001\u0000\u0000\u0000\u00001\u0001\u0000\u0000\u0000\u0000"
			+ ";\u0001\u0000\u0000\u0000\u0001G\u0001\u0000\u0000\u0000\u0003I\u0001"
			+ "\u0000\u0000\u0000\u0005K\u0001\u0000\u0000\u0000\u0007M\u0001\u0000\u0000"
			+ "\u0000\tO\u0001\u0000\u0000\u0000\u000bQ\u0001\u0000\u0000\u0000\rS\u0001"
			+ "\u0000\u0000\u0000\u000fU\u0001\u0000\u0000\u0000\u0011X\u0001\u0000\u0000"
			+ "\u0000\u0013Z\u0001\u0000\u0000\u0000\u0015\\\u0001\u0000\u0000\u0000"
			+ "\u0017^\u0001\u0000\u0000\u0000\u0019a\u0001\u0000\u0000\u0000\u001bc"
			+ "\u0001\u0000\u0000\u0000\u001df\u0001\u0000\u0000\u0000\u001fq\u0001\u0000"
			+ "\u0000\u0000!y\u0001\u0000\u0000\u0000#\u007f\u0001\u0000\u0000\u0000"
			+ "%\u0087\u0001\u0000\u0000\u0000\'\u008f\u0001\u0000\u0000\u0000)\u00a3"
			+ "\u0001\u0000\u0000\u0000+\u00b9\u0001\u0000\u0000\u0000-\u00bc\u0001\u0000"
			+ "\u0000\u0000/\u00c0\u0001\u0000\u0000\u00001\u00c5\u0001\u0000\u0000\u0000"
			+ "3\u00db\u0001\u0000\u0000\u00005\u00dd\u0001\u0000\u0000\u00007\u00df"
			+ "\u0001\u0000\u0000\u00009\u00e1\u0001\u0000\u0000\u0000;\u00e4\u0001\u0000"
			+ "\u0000\u0000=>\u0005W\u0000\u0000>?\u0005H\u0000\u0000?@\u0005E\u0000"
			+ "\u0000@A\u0005R\u0000\u0000AH\u0005E\u0000\u0000BC\u0005w\u0000\u0000"
			+ "CD\u0005h\u0000\u0000DE\u0005e\u0000\u0000EF\u0005r\u0000\u0000FH\u0005"
			+ "e\u0000\u0000G=\u0001\u0000\u0000\u0000GB\u0001\u0000\u0000\u0000H\u0002"
			+ "\u0001\u0000\u0000\u0000IJ\u0005.\u0000\u0000J\u0004\u0001\u0000\u0000"
			+ "\u0000KL\u0005,\u0000\u0000L\u0006\u0001\u0000\u0000\u0000MN\u0005[\u0000"
			+ "\u0000N\b\u0001\u0000\u0000\u0000OP\u0005]\u0000\u0000P\n\u0001\u0000"
			+ "\u0000\u0000QR\u0005(\u0000\u0000R\f\u0001\u0000\u0000\u0000ST\u0005)"
			+ "\u0000\u0000T\u000e\u0001\u0000\u0000\u0000UV\u0005=\u0000\u0000VW\u0005"
			+ "=\u0000\u0000W\u0010\u0001\u0000\u0000\u0000XY\u0005-\u0000\u0000Y\u0012"
			+ "\u0001\u0000\u0000\u0000Z[\u0005+\u0000\u0000[\u0014\u0001\u0000\u0000"
			+ "\u0000\\]\u0005>\u0000\u0000]\u0016\u0001\u0000\u0000\u0000^_\u0005>\u0000"
			+ "\u0000_`\u0005=\u0000\u0000`\u0018\u0001\u0000\u0000\u0000ab\u0005<\u0000"
			+ "\u0000b\u001a\u0001\u0000\u0000\u0000cd\u0005<\u0000\u0000de\u0005=\u0000"
			+ "\u0000e\u001c\u0001\u0000\u0000\u0000fg\u0005!\u0000\u0000gh\u0005=\u0000"
			+ "\u0000h\u001e\u0001\u0000\u0000\u0000ij\u0005A\u0000\u0000jk\u0005N\u0000"
			+ "\u0000kr\u0005D\u0000\u0000lm\u0005a\u0000\u0000mn\u0005n\u0000\u0000"
			+ "nr\u0005d\u0000\u0000op\u0005&\u0000\u0000pr\u0005&\u0000\u0000qi\u0001"
			+ "\u0000\u0000\u0000ql\u0001\u0000\u0000\u0000qo\u0001\u0000\u0000\u0000"
			+ "r \u0001\u0000\u0000\u0000st\u0005O\u0000\u0000tz\u0005R\u0000\u0000u"
			+ "v\u0005o\u0000\u0000vz\u0005r\u0000\u0000wx\u0005|\u0000\u0000xz\u0005"
			+ "|\u0000\u0000ys\u0001\u0000\u0000\u0000yu\u0001\u0000\u0000\u0000yw\u0001"
			+ "\u0000\u0000\u0000z\"\u0001\u0000\u0000\u0000{|\u0005I\u0000\u0000|\u0080"
			+ "\u0005N\u0000\u0000}~\u0005i\u0000\u0000~\u0080\u0005n\u0000\u0000\u007f"
			+ "{\u0001\u0000\u0000\u0000\u007f}\u0001\u0000\u0000\u0000\u0080$\u0001"
			+ "\u0000\u0000\u0000\u0081\u0082\u0005N\u0000\u0000\u0082\u0083\u0005I\u0000"
			+ "\u0000\u0083\u0088\u0005N\u0000\u0000\u0084\u0085\u0005n\u0000\u0000\u0085"
			+ "\u0086\u0005i\u0000\u0000\u0086\u0088\u0005n\u0000\u0000\u0087\u0081\u0001"
			+ "\u0000\u0000\u0000\u0087\u0084\u0001\u0000\u0000\u0000\u0088&\u0001\u0000"
			+ "\u0000\u0000\u0089\u008a\u0005N\u0000\u0000\u008a\u008b\u0005O\u0000\u0000"
			+ "\u008b\u0090\u0005T\u0000\u0000\u008c\u008d\u0005n\u0000\u0000\u008d\u008e"
			+ "\u0005o\u0000\u0000\u008e\u0090\u0005t\u0000\u0000\u008f\u0089\u0001\u0000"
			+ "\u0000\u0000\u008f\u008c\u0001\u0000\u0000\u0000\u0090(\u0001\u0000\u0000"
			+ "\u0000\u0091\u0092\u0005T\u0000\u0000\u0092\u0093\u0005R\u0000\u0000\u0093"
			+ "\u0094\u0005U\u0000\u0000\u0094\u00a4\u0005E\u0000\u0000\u0095\u0096\u0005"
			+ "t\u0000\u0000\u0096\u0097\u0005r\u0000\u0000\u0097\u0098\u0005u\u0000"
			+ "\u0000\u0098\u00a4\u0005e\u0000\u0000\u0099\u009a\u0005F\u0000\u0000\u009a"
			+ "\u009b\u0005A\u0000\u0000\u009b\u009c\u0005L\u0000\u0000\u009c\u009d\u0005"
			+ "S\u0000\u0000\u009d\u00a4\u0005E\u0000\u0000\u009e\u009f\u0005f\u0000"
			+ "\u0000\u009f\u00a0\u0005a\u0000\u0000\u00a0\u00a1\u0005l\u0000\u0000\u00a1"
			+ "\u00a2\u0005s\u0000\u0000\u00a2\u00a4\u0005e\u0000\u0000\u00a3\u0091\u0001"
			+ "\u0000\u0000\u0000\u00a3\u0095\u0001\u0000\u0000\u0000\u00a3\u0099\u0001"
			+ "\u0000\u0000\u0000\u00a3\u009e\u0001\u0000\u0000\u0000\u00a4*\u0001\u0000"
			+ "\u0000\u0000\u00a5\u00ab\u0005\'\u0000\u0000\u00a6\u00aa\b\u0000\u0000"
			+ "\u0000\u00a7\u00a8\u0005\\\u0000\u0000\u00a8\u00aa\t\u0000\u0000\u0000"
			+ "\u00a9\u00a6\u0001\u0000\u0000\u0000\u00a9\u00a7\u0001\u0000\u0000\u0000"
			+ "\u00aa\u00ad\u0001\u0000\u0000\u0000\u00ab\u00a9\u0001\u0000\u0000\u0000"
			+ "\u00ab\u00ac\u0001\u0000\u0000\u0000\u00ac\u00ae\u0001\u0000\u0000\u0000"
			+ "\u00ad\u00ab\u0001\u0000\u0000\u0000\u00ae\u00ba\u0005\'\u0000\u0000\u00af"
			+ "\u00b5\u0005\"\u0000\u0000\u00b0\u00b4\b\u0001\u0000\u0000\u00b1\u00b2"
			+ "\u0005\\\u0000\u0000\u00b2\u00b4\t\u0000\u0000\u0000\u00b3\u00b0\u0001"
			+ "\u0000\u0000\u0000\u00b3\u00b1\u0001\u0000\u0000\u0000\u00b4\u00b7\u0001"
			+ "\u0000\u0000\u0000\u00b5\u00b3\u0001\u0000\u0000\u0000\u00b5\u00b6\u0001"
			+ "\u0000\u0000\u0000\u00b6\u00b8\u0001\u0000\u0000\u0000\u00b7\u00b5\u0001"
			+ "\u0000\u0000\u0000\u00b8\u00ba\u0005\"\u0000\u0000\u00b9\u00a5\u0001\u0000"
			+ "\u0000\u0000\u00b9\u00af\u0001\u0000\u0000\u0000\u00ba,\u0001\u0000\u0000"
			+ "\u0000\u00bb\u00bd\u00035\u001a\u0000\u00bc\u00bb\u0001\u0000\u0000\u0000"
			+ "\u00bd\u00be\u0001\u0000\u0000\u0000\u00be\u00bc\u0001\u0000\u0000\u0000"
			+ "\u00be\u00bf\u0001\u0000\u0000\u0000\u00bf.\u0001\u0000\u0000\u0000\u00c0"
			+ "\u00c1\u00033\u0019\u0000\u00c10\u0001\u0000\u0000\u0000\u00c2\u00c6\u0003"
			+ "7\u001b\u0000\u00c3\u00c6\u00035\u001a\u0000\u00c4\u00c6\u00039\u001c"
			+ "\u0000\u00c5\u00c2\u0001\u0000\u0000\u0000\u00c5\u00c3\u0001\u0000\u0000"
			+ "\u0000\u00c5\u00c4\u0001\u0000\u0000\u0000\u00c6\u00c7\u0001\u0000\u0000"
			+ "\u0000\u00c7\u00c5\u0001\u0000\u0000\u0000\u00c7\u00c8\u0001\u0000\u0000"
			+ "\u0000\u00c82\u0001\u0000\u0000\u0000\u00c9\u00cb\u00035\u001a\u0000\u00ca"
			+ "\u00c9\u0001\u0000\u0000\u0000\u00cb\u00cc\u0001\u0000\u0000\u0000\u00cc"
			+ "\u00ca\u0001\u0000\u0000\u0000\u00cc\u00cd\u0001\u0000\u0000\u0000\u00cd"
			+ "\u00ce\u0001\u0000\u0000\u0000\u00ce\u00d2\u0005.\u0000\u0000\u00cf\u00d1"
			+ "\u00035\u001a\u0000\u00d0\u00cf\u0001\u0000\u0000\u0000\u00d1\u00d4\u0001"
			+ "\u0000\u0000\u0000\u00d2\u00d0\u0001\u0000\u0000\u0000\u00d2\u00d3\u0001"
			+ "\u0000\u0000\u0000\u00d3\u00dc\u0001\u0000\u0000\u0000\u00d4\u00d2\u0001"
			+ "\u0000\u0000\u0000\u00d5\u00d7\u0005.\u0000\u0000\u00d6\u00d8\u00035\u001a"
			+ "\u0000\u00d7\u00d6\u0001\u0000\u0000\u0000\u00d8\u00d9\u0001\u0000\u0000"
			+ "\u0000\u00d9\u00d7\u0001\u0000\u0000\u0000\u00d9\u00da\u0001\u0000\u0000"
			+ "\u0000\u00da\u00dc\u0001\u0000\u0000\u0000\u00db\u00ca\u0001\u0000\u0000"
			+ "\u0000\u00db\u00d5\u0001\u0000\u0000\u0000\u00dc4\u0001\u0000\u0000\u0000"
			+ "\u00dd\u00de\u0007\u0002\u0000\u0000\u00de6\u0001\u0000\u0000\u0000\u00df"
			+ "\u00e0\u0007\u0003\u0000\u0000\u00e08\u0001\u0000\u0000\u0000\u00e1\u00e2"
			+ "\u0007\u0004\u0000\u0000\u00e2:\u0001\u0000\u0000\u0000\u00e3\u00e5\u0007"
			+ "\u0005\u0000\u0000\u00e4\u00e3\u0001\u0000\u0000\u0000\u00e5\u00e6\u0001"
			+ "\u0000\u0000\u0000\u00e6\u00e4\u0001\u0000\u0000\u0000\u00e6\u00e7\u0001"
			+ "\u0000\u0000\u0000\u00e7\u00e8\u0001\u0000\u0000\u0000\u00e8\u00e9\u0006"
			+ "\u001d\u0000\u0000\u00e9<\u0001\u0000\u0000\u0000\u0015\u0000Gqy\u007f"
			+ "\u0087\u008f\u00a3\u00a9\u00ab\u00b3\u00b5\u00b9\u00be\u00c5\u00c7\u00cc"
			+ "\u00d2\u00d9\u00db\u00e6\u0001\u0000\u0001\u0000";

	public static final ATN _ATN = new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}

}