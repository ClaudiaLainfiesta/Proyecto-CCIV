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
    int cantComents = 0;
    boolean muyLargo = false;
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
	private final int STRING = 1;
	private final int YYINITIAL = 0;
	private final int COMMENT = 2;
	private final int COMMENTS = 3;
	private final int yy_state_dtrans[] = {
		0,
		78,
		101,
		121
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
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NOT_ACCEPT,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NOT_ACCEPT,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NOT_ACCEPT,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NO_ANCHOR,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NOT_ACCEPT,
		/* 110 */ YY_NO_ANCHOR,
		/* 111 */ YY_NO_ANCHOR,
		/* 112 */ YY_NOT_ACCEPT,
		/* 113 */ YY_NO_ANCHOR,
		/* 114 */ YY_NO_ANCHOR,
		/* 115 */ YY_NOT_ACCEPT,
		/* 116 */ YY_NO_ANCHOR,
		/* 117 */ YY_NO_ANCHOR,
		/* 118 */ YY_NOT_ACCEPT,
		/* 119 */ YY_NO_ANCHOR,
		/* 120 */ YY_NO_ANCHOR,
		/* 121 */ YY_NOT_ACCEPT,
		/* 122 */ YY_NO_ANCHOR,
		/* 123 */ YY_NO_ANCHOR,
		/* 124 */ YY_NOT_ACCEPT,
		/* 125 */ YY_NO_ANCHOR,
		/* 126 */ YY_NO_ANCHOR,
		/* 127 */ YY_NOT_ACCEPT,
		/* 128 */ YY_NO_ANCHOR,
		/* 129 */ YY_NO_ANCHOR,
		/* 130 */ YY_NOT_ACCEPT,
		/* 131 */ YY_NO_ANCHOR,
		/* 132 */ YY_NO_ANCHOR,
		/* 133 */ YY_NOT_ACCEPT,
		/* 134 */ YY_NO_ANCHOR,
		/* 135 */ YY_NO_ANCHOR,
		/* 136 */ YY_NOT_ACCEPT,
		/* 137 */ YY_NO_ANCHOR,
		/* 138 */ YY_NO_ANCHOR,
		/* 139 */ YY_NO_ANCHOR,
		/* 140 */ YY_NO_ANCHOR,
		/* 141 */ YY_NO_ANCHOR,
		/* 142 */ YY_NO_ANCHOR,
		/* 143 */ YY_NO_ANCHOR,
		/* 144 */ YY_NO_ANCHOR,
		/* 145 */ YY_NO_ANCHOR,
		/* 146 */ YY_NO_ANCHOR,
		/* 147 */ YY_NO_ANCHOR,
		/* 148 */ YY_NO_ANCHOR,
		/* 149 */ YY_NO_ANCHOR,
		/* 150 */ YY_NO_ANCHOR,
		/* 151 */ YY_NO_ANCHOR,
		/* 152 */ YY_NO_ANCHOR,
		/* 153 */ YY_NO_ANCHOR,
		/* 154 */ YY_NO_ANCHOR,
		/* 155 */ YY_NO_ANCHOR,
		/* 156 */ YY_NO_ANCHOR,
		/* 157 */ YY_NO_ANCHOR,
		/* 158 */ YY_NO_ANCHOR,
		/* 159 */ YY_NO_ANCHOR,
		/* 160 */ YY_NO_ANCHOR,
		/* 161 */ YY_NO_ANCHOR,
		/* 162 */ YY_NO_ANCHOR,
		/* 163 */ YY_NO_ANCHOR,
		/* 164 */ YY_NO_ANCHOR,
		/* 165 */ YY_NO_ANCHOR,
		/* 166 */ YY_NO_ANCHOR,
		/* 167 */ YY_NO_ANCHOR,
		/* 168 */ YY_NO_ANCHOR,
		/* 169 */ YY_NO_ANCHOR,
		/* 170 */ YY_NO_ANCHOR,
		/* 171 */ YY_NO_ANCHOR,
		/* 172 */ YY_NO_ANCHOR,
		/* 173 */ YY_NO_ANCHOR,
		/* 174 */ YY_NO_ANCHOR,
		/* 175 */ YY_NO_ANCHOR,
		/* 176 */ YY_NO_ANCHOR,
		/* 177 */ YY_NO_ANCHOR,
		/* 178 */ YY_NO_ANCHOR,
		/* 179 */ YY_NO_ANCHOR,
		/* 180 */ YY_NO_ANCHOR,
		/* 181 */ YY_NO_ANCHOR,
		/* 182 */ YY_NO_ANCHOR,
		/* 183 */ YY_NO_ANCHOR,
		/* 184 */ YY_NO_ANCHOR,
		/* 185 */ YY_NO_ANCHOR,
		/* 186 */ YY_NO_ANCHOR,
		/* 187 */ YY_NO_ANCHOR,
		/* 188 */ YY_NO_ANCHOR,
		/* 189 */ YY_NO_ANCHOR,
		/* 190 */ YY_NO_ANCHOR,
		/* 191 */ YY_NO_ANCHOR,
		/* 192 */ YY_NO_ANCHOR,
		/* 193 */ YY_NO_ANCHOR,
		/* 194 */ YY_NO_ANCHOR,
		/* 195 */ YY_NO_ANCHOR,
		/* 196 */ YY_NO_ANCHOR,
		/* 197 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"49:8,46,45,42,54,47,48,49:18,53,49,41,49:5,50,52,51,57,64,56,66,58,40:10,65" +
",63,55,1,2,49,60,22,23,24,25,26,15,23,27,28,23:2,29,23,30,31,32,23,33,34,8," +
"35,36,37,23:3,49,43,49:2,38,49,14,44,13,18,6,21,39,5,3,39:2,12,39,4,11,10,3" +
"9,7,9,19,20,17,16,39:3,61,49,62,59,49,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,198,
"0,1,2,1,3,4,5,1:2,6,7,8,1:3,9,10,1:11,11,12:2,13,1:5,12:5,13,12:9,1:9,14,1:" +
"11,15,1:4,16,17,18,19,13:2,12,13:5,12,13:7,20,21,22,23,24,25,26,27,28,29,30" +
",31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55" +
",56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80" +
",12,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,10" +
"3,104,105,106,107,108,109,110,111,112,113,13,114,115,116,117")[0];

	private int yy_nxt[][] = unpackFromString(118,67,
"1,2,3,4,79,159,168,159,5,159,171,102,173,175,159,80,177,159:2,179,159,106,1" +
"93:2,194,193,195,193,103,158,167,107,196,193:4,197,3,159,6,7,8,3,159,9,3,9:" +
"2,3,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,-1:69,27,-1:67,159,2" +
"8,159:4,181,159:5,29,159:5,29,159:8,28,159:3,181,159:6,-1:3,159,-1:25,193:2" +
",170,193:21,170,193:13,-1:3,193,-1:62,6,-1:71,9,-1,9:2,-1:69,32,-1:67,33,-1" +
":15,34,-1:54,35,-1:66,36,-1:13,159:2,163,159:21,163,159:13,-1:3,159,-1:25,1" +
"59:38,-1:3,159,-1:25,193:38,-1:3,193,-1:63,67,-1:70,73,-1,73:2,-1:4,73,-1:1" +
"3,1,52:40,53,54,98,52:23,-1:3,159:3,110,159:4,113,159:14,110,159:4,113,159:" +
"9,-1:3,159,-1:25,31,193:24,31,193:12,-1:3,193,-1:25,193:2,186,193:21,186,19" +
"3:13,-1:3,193,-1:23,55:3,56,55:2,57,55:11,58,55,59,55:20,60,61,62,63,64,65," +
"66,55:18,-1:55,105,-1:62,75,-1:15,1,68:41,69,68:12,99,68:11,-1:3,159:12,30," +
"159:5,30,159:19,-1:3,159,-1:25,193,81,193:4,180,193:5,82,193:5,82,193:8,81," +
"193:3,180,193:6,-1:3,193,-1:74,76,-1:40,109,-1:43,84,159:10,137,159:7,137,1" +
"59:5,84,159:12,-1:3,159,-1:25,193:12,83,193:5,83,193:19,-1:3,193,-1:77,124," +
"-1:42,112,-1:38,159:13,37,159:20,37,159:3,-1:3,159,-1:25,193:5,87,193:10,87" +
",193:21,-1:3,193,-1:37,115,-1:54,159:5,38,159:10,38,159:21,-1:3,159,-1:25,1" +
"93:13,85,193:20,85,193:3,-1:3,193,-1:24,118,-1:67,159:11,139,159:7,139,159:" +
"18,-1:3,159,-1:25,193:5,86,193:10,86,193:21,-1:3,193,-1:24,70,-1:67,159:6,1" +
"41,159:24,141,159:6,-1:3,159,-1:25,193,42,193:25,42,193:10,-1:3,193,-1:22,1" +
",71:41,72,71:2,73,71,73:2,71,100,104,71,73,74,108,71:11,-1:3,159:8,143,159:" +
"19,143,159:9,-1:3,159,-1:25,193:3,93,193:19,93,193:14,-1:3,193,-1:48,127,-1" +
":43,159:5,39,159:10,39,159:21,-1:3,159,-1:25,193:10,88,193:10,88,193:16,-1:" +
"3,193,-1:53,130,-1:38,164,159:24,164,159:12,-1:3,159,-1:25,193:3,89,193:19," +
"89,193:14,-1:3,193,-1:37,133,-1:54,159:3,148,159:19,148,159:14,-1:3,159,-1:" +
"25,193:7,92,193:21,92,193:8,-1:3,193,-1:24,136,-1:67,159:17,149,159:14,149," +
"159:5,-1:3,159,-1:25,193:9,91,193:16,91,193:11,-1:3,193,-1:24,77,-1:67,159:" +
"9,165,159:16,165,159:11,-1:3,159,-1:25,193:6,94,193:24,94,193:6,-1:3,193,-1" +
":25,159:10,40,159:10,40,159:16,-1:3,159,-1:25,193:3,95,193:19,95,193:14,-1:" +
"3,193,-1:25,159:3,41,159:19,41,159:14,-1:3,159,-1:25,193:15,96,193:6,96,193" +
":15,-1:3,193,-1:25,159:9,43,159:16,43,159:11,-1:3,159,-1:25,193:6,97,193:24" +
",97,193:6,-1:3,193,-1:25,159:7,44,159:21,44,159:8,-1:3,159,-1:25,159:6,152," +
"159:24,152,159:6,-1:3,159,-1:25,159:3,45,159:19,45,159:14,-1:3,159,-1:25,15" +
"9,90,159:25,90,159:10,-1:3,159,-1:25,159:3,46,159:19,46,159:14,-1:3,159,-1:" +
"25,159:4,166,159:25,166,159:7,-1:3,159,-1:25,155,159:24,155,159:12,-1:3,159" +
",-1:25,159:6,47,159:24,47,159:6,-1:3,159,-1:25,159:3,48,159:19,48,159:14,-1" +
":3,159,-1:25,159:3,49,159:19,49,159:14,-1:3,159,-1:25,159:15,50,159:6,50,15" +
"9:15,-1:3,159,-1:25,159:5,157,159:10,157,159:21,-1:3,159,-1:25,159:6,51,159" +
":24,51,159:6,-1:3,159,-1:25,193:3,111,193:4,182,193:14,111,193:4,182,193:9," +
"-1:3,193,-1:25,159:11,146,159:7,146,159:18,-1:3,159,-1:25,159:6,147,159:24," +
"147,159:6,-1:3,159,-1:25,159:8,145,159:19,145,159:9,-1:3,159,-1:25,159:3,15" +
"0,159:19,150,159:14,-1:3,159,-1:25,159:9,153,159:16,153,159:11,-1:3,159,-1:" +
"25,159:6,154,159:24,154,159:6,-1:3,159,-1:25,156,159:24,156,159:12,-1:3,159" +
",-1:25,193:3,114,193:4,117,193:14,114,193:4,117,193:9,-1:3,193,-1:25,159:6," +
"116,159:2,119,159:16,119,159:4,116,159:6,-1:3,159,-1:25,159:8,151,159:19,15" +
"1,159:9,-1:3,159,-1:25,193:3,120,193:19,120,193:14,-1:3,193,-1:25,159:8,122" +
",159:19,122,159:9,-1:3,159,-1:25,193:11,185,193:7,185,193:18,-1:3,193,-1:25" +
",159:3,125,159:4,162,159:14,125,159:4,162,159:9,-1:3,159,-1:25,193:6,123,19" +
"3:24,123,193:6,-1:3,193,-1:25,159:9,160,159,161,159:7,161,159:6,160,159:11," +
"-1:3,159,-1:25,193:11,126,193:7,126,193:18,-1:3,193,-1:25,159:2,128,159:21," +
"128,159:13,-1:3,159,-1:25,193:6,129,193:24,129,193:6,-1:3,193,-1:25,159:2,1" +
"31,159,134,159:19,131,159:5,134,159:7,-1:3,159,-1:25,193:14,187,193:18,187," +
"193:4,-1:3,193,-1:25,159:14,169,159:18,169,159:4,-1:3,159,-1:25,193:8,132,1" +
"93:19,132,193:9,-1:3,193,-1:25,193:8,135,193:19,135,193:9,-1:3,193,-1:25,18" +
"8,193:24,188,193:12,-1:3,193,-1:25,193:6,138,193:24,138,193:6,-1:3,193,-1:2" +
"5,193:3,189,193:19,189,193:14,-1:3,193,-1:25,193:8,190,193:19,190,193:9,-1:" +
"3,193,-1:25,193:9,140,193:16,140,193:11,-1:3,193,-1:25,193:4,191,193:25,191" +
",193:7,-1:3,193,-1:25,142,193:24,142,193:12,-1:3,193,-1:25,192,193:24,192,1" +
"93:12,-1:3,193,-1:25,193:5,144,193:10,144,193:21,-1:3,193,-1:25,193:9,172,1" +
"93,174,193:7,174,193:6,172,193:11,-1:3,193,-1:25,193:6,176,193:2,178,193:16" +
",178,193:4,176,193:6,-1:3,193,-1:25,193:8,183,193:19,183,193:9,-1:3,193,-1:" +
"25,193:2,184,193:21,184,193:13,-1:3,193,-1:22");

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
    case STRING:
        yybegin(YYINITIAL);
        return new Symbol(TokenConstants.ERROR, "EOF in string constant");
    case COMMENTS:
        yybegin(YYINITIAL);
        return new Symbol(TokenConstants.ERROR, "EOF in comment");
    case COMMENT:
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
						{ return new Symbol(TokenConstants.ERROR, yytext()); }
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
                            string_buf.delete(0,string_buf.length());
                            muyLargo = false;
                            yybegin(STRING);
                        }
					case -8:
						break;
					case 8:
						{
                            curr_lineno++;
                        }
					case -9:
						break;
					case 9:
						{
                        }
					case -10:
						break;
					case 10:
						{
                            return new Symbol(TokenConstants.LPAREN);
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
                            return new Symbol(TokenConstants.RPAREN);
                        }
					case -13:
						break;
					case 13:
						{
                        }
					case -14:
						break;
					case 14:
						{ /* vertical tab */ }
					case -15:
						break;
					case 15:
						{
                            return new Symbol(TokenConstants.LT);
                        }
					case -16:
						break;
					case 16:
						{
                            return new Symbol(TokenConstants.MINUS);
                        }
					case -17:
						break;
					case 17:
						{
                            return new Symbol(TokenConstants.PLUS);
                        }
					case -18:
						break;
					case 18:
						{
                            return new Symbol(TokenConstants.DIV);
                        }
					case -19:
						break;
					case 19:
						{
                            return new Symbol(TokenConstants.NEG);
                        }
					case -20:
						break;
					case 20:
						{
                            return new Symbol(TokenConstants.AT);
                        }
					case -21:
						break;
					case 21:
						{
                            return new Symbol(TokenConstants.LBRACE);
                        }
					case -22:
						break;
					case 22:
						{
                            return new Symbol(TokenConstants.RBRACE);
                        }
					case -23:
						break;
					case 23:
						{
                            return new Symbol(TokenConstants.SEMI);
                        }
					case -24:
						break;
					case 24:
						{
                            return new Symbol(TokenConstants.COMMA);
                        }
					case -25:
						break;
					case 25:
						{
                            return new Symbol(TokenConstants.COLON);
                        }
					case -26:
						break;
					case 26:
						{
                            return new Symbol(TokenConstants.DOT);
                        }
					case -27:
						break;
					case 27:
						{ /* Sample lexical rule for "=>" arrow.
                                     Further lexical rules should be defined
                                     here, after the last %% separator */
                                  return new Symbol(TokenConstants.DARROW); }
					case -28:
						break;
					case 28:
						{   
                                                   return new Symbol(TokenConstants.IN);
                                               }
					case -29:
						break;
					case 29:
						{
                                                   return new Symbol(TokenConstants.IF);
                                               }
					case -30:
						break;
					case 30:
						{
                                                   return new Symbol(TokenConstants.OF);
                                               }
					case -31:
						break;
					case 31:
						{
                                                   return new Symbol(TokenConstants.FI);
                                               }
					case -32:
						break;
					case 32:
						{   
                            cantComents++;
                            yybegin(COMMENTS);
                        }
					case -33:
						break;
					case 33:
						{
                            return new Symbol(TokenConstants.ERROR, "Unmatched *)");
                        }
					case -34:
						break;
					case 34:
						{
                            return new Symbol(TokenConstants.LE);
                        }
					case -35:
						break;
					case 35:
						{
                            return new Symbol(TokenConstants.ASSIGN);
                        }
					case -36:
						break;
					case 36:
						{
                            yybegin(COMMENT);
                        }
					case -37:
						break;
					case 37:
						{
                                                   return new Symbol(TokenConstants.NEW);
                                               }
					case -38:
						break;
					case 38:
						{
                                                   return new Symbol(TokenConstants.NOT);
                                               }
					case -39:
						break;
					case 39:
						{
                                                   return new Symbol(TokenConstants.LET);
                                               }
					case -40:
						break;
					case 40:
						{
                                                    return new Symbol(TokenConstants.ESAC);
                                               }
					case -41:
						break;
					case 41:
						{
                                                   return new Symbol(TokenConstants.ELSE);
                                               }
					case -42:
						break;
					case 42:
						{
                                                    return new Symbol(TokenConstants.THEN);
                                               }
					case -43:
						break;
					case 43:
						{
                                                   return new Symbol(TokenConstants.POOL);
                                               }
					case -44:
						break;
					case 44:
						{
                                                   return new Symbol(TokenConstants.LOOP);
                                               }
					case -45:
						break;
					case 45:
						{
                                                   return new Symbol(TokenConstants.CASE);
                                               }
					case -46:
						break;
					case 46:
						{
                                                   return new Symbol(TokenConstants.BOOL_CONST, true);
                                               }
					case -47:
						break;
					case 47:
						{
                                                   return new Symbol(TokenConstants.CLASS);
                                               }
					case -48:
						break;
					case 48:
						{
                                                   return new Symbol(TokenConstants.WHILE);
                                               }
					case -49:
						break;
					case 49:
						{
                                                   return new Symbol(TokenConstants.BOOL_CONST, false);
                                               }
					case -50:
						break;
					case 50:
						{
                                                    return new Symbol(TokenConstants.ISVOID);
                                               }
					case -51:
						break;
					case 51:
						{
                                                   return new Symbol(TokenConstants.INHERITS);
                                               }
					case -52:
						break;
					case 52:
						{
                            string_buf.append(yytext());
                            if (string_buf.length() > MAX_STR_CONST) {
                                muyLargo = true;
                            }
                        }
					case -53:
						break;
					case 53:
						{
                            if(muyLargo == true){
                                yybegin(YYINITIAL);
                                return new Symbol(TokenConstants.ERROR, "String constant too long");
                            }
                            yybegin(YYINITIAL);
                            return new Symbol(TokenConstants.STR_CONST, AbstractTable.stringtable.addString(string_buf.toString()));
                        }
					case -54:
						break;
					case 54:
						{
                        yybegin(YYINITIAL);
                            return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
}
					case -55:
						break;
					case 55:
						{
    char c = yytext().charAt(1); 
    string_buf.append(c);
}
					case -56:
						break;
					case 56:
						{ 
                            string_buf.append("\n");
                        }
					case -57:
						break;
					case 57:
						{ 
                            string_buf.append("\r"); 
                        }
					case -58:
						break;
					case 58:
						{ 
                            string_buf.append("\t"); 
                        }
					case -59:
						break;
					case 59:
						{ string_buf.append('\f'); }
					case -60:
						break;
					case 60:
						{ curr_lineno++; }
					case -61:
						break;
					case 61:
						{ 
                            string_buf.append("\\"); 
                        }
					case -62:
						break;
					case 62:
						{ string_buf.append('\b'); }
					case -63:
						break;
					case 63:
						{ string_buf.append("\t"); }
					case -64:
						break;
					case 64:
						{  string_buf.append('\b'); }
					case -65:
						break;
					case 65:
						{  string_buf.append('\f'); }
					case -66:
						break;
					case 66:
						{ string_buf.append("\r");}
					case -67:
						break;
					case 67:
						{ 
                            string_buf.append('\"'); 
                        }
					case -68:
						break;
					case 68:
						{
                        }
					case -69:
						break;
					case 69:
						{
                            curr_lineno++;
                            yybegin(YYINITIAL);
                        }
					case -70:
						break;
					case 70:
						{ }
					case -71:
						break;
					case 71:
						{  }
					case -72:
						break;
					case 72:
						{
                            curr_lineno++;
                        }
					case -73:
						break;
					case 73:
						{
                        }
					case -74:
						break;
					case 74:
						{ /* vertical tab en comentario */ }
					case -75:
						break;
					case 75:
						{
                            cantComents++;
                        }
					case -76:
						break;
					case 76:
						{   
                            cantComents--;
                            if (cantComents == 0) {
                                yybegin(YYINITIAL);
                            }
                            else if (cantComents < 0) {
                                return new Symbol(TokenConstants.ERROR, "Unmatched *)");
                            }
                        }
					case -77:
						break;
					case 77:
						{ return new Symbol(TokenConstants.ERROR, "EOF in comment"); }
					case -78:
						break;
					case 79:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -79:
						break;
					case 80:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -80:
						break;
					case 81:
						{   
                                                   return new Symbol(TokenConstants.IN);
                                               }
					case -81:
						break;
					case 82:
						{
                                                   return new Symbol(TokenConstants.IF);
                                               }
					case -82:
						break;
					case 83:
						{
                                                   return new Symbol(TokenConstants.OF);
                                               }
					case -83:
						break;
					case 84:
						{
                                                   return new Symbol(TokenConstants.FI);
                                               }
					case -84:
						break;
					case 85:
						{
                                                   return new Symbol(TokenConstants.NEW);
                                               }
					case -85:
						break;
					case 86:
						{
                                                   return new Symbol(TokenConstants.NOT);
                                               }
					case -86:
						break;
					case 87:
						{
                                                   return new Symbol(TokenConstants.LET);
                                               }
					case -87:
						break;
					case 88:
						{
                                                    return new Symbol(TokenConstants.ESAC);
                                               }
					case -88:
						break;
					case 89:
						{
                                                   return new Symbol(TokenConstants.ELSE);
                                               }
					case -89:
						break;
					case 90:
						{
                                                    return new Symbol(TokenConstants.THEN);
                                               }
					case -90:
						break;
					case 91:
						{
                                                   return new Symbol(TokenConstants.POOL);
                                               }
					case -91:
						break;
					case 92:
						{
                                                   return new Symbol(TokenConstants.LOOP);
                                               }
					case -92:
						break;
					case 93:
						{
                                                   return new Symbol(TokenConstants.CASE);
                                               }
					case -93:
						break;
					case 94:
						{
                                                   return new Symbol(TokenConstants.CLASS);
                                               }
					case -94:
						break;
					case 95:
						{
                                                   return new Symbol(TokenConstants.WHILE);
                                               }
					case -95:
						break;
					case 96:
						{
                                                    return new Symbol(TokenConstants.ISVOID);
                                               }
					case -96:
						break;
					case 97:
						{
                                                   return new Symbol(TokenConstants.INHERITS);
                                               }
					case -97:
						break;
					case 98:
						{
                            string_buf.append(yytext());
                            if (string_buf.length() > MAX_STR_CONST) {
                                muyLargo = true;
                            }
                        }
					case -98:
						break;
					case 99:
						{
                        }
					case -99:
						break;
					case 100:
						{  }
					case -100:
						break;
					case 102:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -101:
						break;
					case 103:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -102:
						break;
					case 104:
						{  }
					case -103:
						break;
					case 106:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -104:
						break;
					case 107:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -105:
						break;
					case 108:
						{  }
					case -106:
						break;
					case 110:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -107:
						break;
					case 111:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -108:
						break;
					case 113:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -109:
						break;
					case 114:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -110:
						break;
					case 116:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -111:
						break;
					case 117:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -112:
						break;
					case 119:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -113:
						break;
					case 120:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -114:
						break;
					case 122:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -115:
						break;
					case 123:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -116:
						break;
					case 125:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -117:
						break;
					case 126:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -118:
						break;
					case 128:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -119:
						break;
					case 129:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -120:
						break;
					case 131:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -121:
						break;
					case 132:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -122:
						break;
					case 134:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -123:
						break;
					case 135:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -124:
						break;
					case 137:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -125:
						break;
					case 138:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -126:
						break;
					case 139:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -127:
						break;
					case 140:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -128:
						break;
					case 141:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -129:
						break;
					case 142:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -130:
						break;
					case 143:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -131:
						break;
					case 144:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -132:
						break;
					case 145:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -133:
						break;
					case 146:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -134:
						break;
					case 147:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -135:
						break;
					case 148:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -136:
						break;
					case 149:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -137:
						break;
					case 150:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -138:
						break;
					case 151:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -139:
						break;
					case 152:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -140:
						break;
					case 153:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -141:
						break;
					case 154:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -142:
						break;
					case 155:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -143:
						break;
					case 156:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -144:
						break;
					case 157:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -145:
						break;
					case 158:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -146:
						break;
					case 159:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -147:
						break;
					case 160:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -148:
						break;
					case 161:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -149:
						break;
					case 162:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -150:
						break;
					case 163:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -151:
						break;
					case 164:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -152:
						break;
					case 165:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -153:
						break;
					case 166:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -154:
						break;
					case 167:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -155:
						break;
					case 168:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -156:
						break;
					case 169:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -157:
						break;
					case 170:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -158:
						break;
					case 171:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -159:
						break;
					case 172:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -160:
						break;
					case 173:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -161:
						break;
					case 174:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -162:
						break;
					case 175:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -163:
						break;
					case 176:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -164:
						break;
					case 177:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -165:
						break;
					case 178:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -166:
						break;
					case 179:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -167:
						break;
					case 180:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -168:
						break;
					case 181:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -169:
						break;
					case 182:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -170:
						break;
					case 183:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -171:
						break;
					case 184:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -172:
						break;
					case 185:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -173:
						break;
					case 186:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -174:
						break;
					case 187:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -175:
						break;
					case 188:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -176:
						break;
					case 189:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -177:
						break;
					case 190:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -178:
						break;
					case 191:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -179:
						break;
					case 192:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -180:
						break;
					case 193:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -181:
						break;
					case 194:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -182:
						break;
					case 195:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -183:
						break;
					case 196:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -184:
						break;
					case 197:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -185:
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
