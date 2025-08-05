/*
 *  The scanner definition for COOL.
 */
import java_cup.runtime.Symbol;


class CoolLexer implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

/*  Stuff enclosed in %{ %} is copied verbatim to the lexer class
 *  definition, all the extra variables/functions you want to use in the
 *  lexer actions should go here.  Don't remove or modify anything that
 *  was there initially.  */
    // Max size of string constants
    static int MAX_STR_CONST = 1025;
    // For assembling string constants
    StringBuffer string_buf = new StringBuffer();
    private int curr_lineno = 1;
    int get_curr_lineno() {
	return curr_lineno;
    }
    private AbstractSymbol filename;
    void set_filename(String fname) {
	filename = AbstractTable.stringtable.addString(fname);
    }
    AbstractSymbol curr_filename() {
	return filename;
    }
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private boolean yy_at_bol;
	private int yy_lexical_state;

	CoolLexer (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	CoolLexer (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private CoolLexer () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */
    // empty for now
	}

	private boolean yy_eof_done = false;
	private final int YYINITIAL = 0;
	private final int yy_state_dtrans[] = {
		0
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"40:9,26:2,40,26:2,40:18,25,40:7,33,34,29,27,38,28,40,30,23:10,39,37,32,1,2," +
"40:2,14,15:3,16,15:6,17,15,18,19,15:2,20,21,13,22,15:5,40:6,8,24:3,6,7,24:5" +
",9,24,11,12,24:2,4,10,3,5,24:5,35,40,36,31,40,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,40,
"0,1,2,1,3,4,5,1,6,1:5,7,1:10,8:3,9,10,4,11,12,13,14,15,16,17,8,18")[0];

	private int yy_nxt[][] = unpackFromString(19,41,
"1,2,3,4,38:3,39,38:3,33,38,5:5,34,5:4,6,38,7,8,9,10,11,12,13,14,15,16,17,18" +
",19,20,21,3,-1:43,22,-1:41,38,35,38:15,35,38:4,-1:19,5:22,-1:39,6,-1:43,8,-" +
"1:15,23,-1:26,24,-1:15,38:22,-1:19,25,38:9,25,38:11,-1:19,30,5:9,30,5:11,-1" +
":19,38:3,26,38:9,26,38:8,-1:19,38:3,27,38:9,27,38:8,-1:19,38:9,28,38:6,28,3" +
"8:5,-1:19,5:9,29,5:6,29,5:5,-1:19,38:2,31,38:16,31,38:2,-1:19,38:6,37,38:7," +
"37,38:7,-1:19,38:7,32,38:10,32,38:3,-1:19,38:5,36,38:5,36,38:10,-1:16");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

/*  Stuff enclosed in %eofval{ %eofval} specifies java code that is
 *  executed when end-of-file is reached.  If you use multiple lexical
 *  states and want to do something special if an EOF is encountered in
 *  one of those states, place your code in the switch statement.
 *  Ultimately, you should return the EOF symbol, or your lexer won't
 *  work.  */
    switch(yy_lexical_state) {
    case YYINITIAL:
	/* nothing special to do in the initial state */
	break;
	/* If necessary, add code for other states here, e.g:
	   case COMMENT:
	   ...
	   break;
	*/
    }
    return new Symbol(TokenConstants.EOF);
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{
                            return new Symbol(TokenConstants.EQ);
                        }
					case -3:
						break;
					case 3:
						{ /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                  System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -4:
						break;
					case 4:
						{
                                                    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                                }
					case -5:
						break;
					case 5:
						{
                                                    return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                                }
					case -6:
						break;
					case 6:
						{
                                                    return new Symbol(TokenConstants.INT_CONST, AbstractTable.inttable.addString(yytext()));
                                                }
					case -7:
						break;
					case 7:
						{
                        }
					case -8:
						break;
					case 8:
						{
                        }
					case -9:
						break;
					case 9:
						{
                            return new Symbol(TokenConstants.PLUS);
                        }
					case -10:
						break;
					case 10:
						{
                            return new Symbol(TokenConstants.MINUS);
                        }
					case -11:
						break;
					case 11:
						{
                            return new Symbol(TokenConstants.MULT);
                        }
					case -12:
						break;
					case 12:
						{
                            return new Symbol(TokenConstants.DIV);
                        }
					case -13:
						break;
					case 13:
						{
                            return new Symbol(TokenConstants.NEG);
                        }
					case -14:
						break;
					case 14:
						{
                            return new Symbol(TokenConstants.LT);
                        }
					case -15:
						break;
					case 15:
						{
                            return new Symbol(TokenConstants.LPAREN);
                        }
					case -16:
						break;
					case 16:
						{
                            return new Symbol(TokenConstants.RPAREN);
                        }
					case -17:
						break;
					case 17:
						{
                            return new Symbol(TokenConstants.LBRACE);
                        }
					case -18:
						break;
					case 18:
						{
                            return new Symbol(TokenConstants.RBRACE);
                        }
					case -19:
						break;
					case 19:
						{
                            return new Symbol(TokenConstants.SEMI);
                        }
					case -20:
						break;
					case 20:
						{
                            return new Symbol(TokenConstants.COMMA);
                        }
					case -21:
						break;
					case 21:
						{
                            return new Symbol(TokenConstants.COLON);
                        }
					case -22:
						break;
					case 22:
						{ /* Sample lexical rule for "=>" arrow.
                                     Further lexical rules should be defined
                                     here, after the last %% separator */
                                  return new Symbol(TokenConstants.DARROW); }
					case -23:
						break;
					case 23:
						{
                            return new Symbol(TokenConstants.LE);
                        }
					case -24:
						break;
					case 24:
						{
                            return new Symbol(TokenConstants.ASSIGN);
                        }
					case -25:
						break;
					case 25:
						{
                                    return new Symbol(TokenConstants.NOT);
                                }
					case -26:
						break;
					case 26:
						{
                                                    return new Symbol(TokenConstants.BOOL_CONST, true);
                                                }
					case -27:
						break;
					case 27:
						{
                                                    return new Symbol(TokenConstants.BOOL_CONST, false);
                                                }
					case -28:
						break;
					case 28:
						{
                                                    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                                }
					case -29:
						break;
					case 29:
						{
                                                    return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                                }
					case -30:
						break;
					case 30:
						{
                                    return new Symbol(TokenConstants.NOT);
                                }
					case -31:
						break;
					case 31:
						{
                                                    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                                }
					case -32:
						break;
					case 32:
						{
                                                    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                                }
					case -33:
						break;
					case 33:
						{
                                                    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                                }
					case -34:
						break;
					case 34:
						{
                                                    return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                                }
					case -35:
						break;
					case 35:
						{
                                                    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                                }
					case -36:
						break;
					case 36:
						{
                                                    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                                }
					case -37:
						break;
					case 37:
						{
                                                    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                                }
					case -38:
						break;
					case 38:
						{
                                                    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                                }
					case -39:
						break;
					case 39:
						{
                                                    return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                                }
					case -40:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
