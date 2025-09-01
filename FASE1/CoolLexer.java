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
    public char indice(int y){
        char x = yytext().charAt(y);
        return x;
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
	private final int STRING = 1;
	private final int ERRORSTRING = 4;
	private final int YYINITIAL = 0;
	private final int COMMENT = 2;
	private final int COMMENTS = 3;
	private final int yy_state_dtrans[] = {
		0,
		77,
		102,
		105,
		108
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
		/* 77 */ YY_NOT_ACCEPT,
		/* 78 */ YY_NO_ANCHOR,
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
		/* 98 */ YY_NOT_ACCEPT,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NOT_ACCEPT,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NOT_ACCEPT,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NO_ANCHOR,
		/* 108 */ YY_NOT_ACCEPT,
		/* 109 */ YY_NO_ANCHOR,
		/* 110 */ YY_NO_ANCHOR,
		/* 111 */ YY_NOT_ACCEPT,
		/* 112 */ YY_NO_ANCHOR,
		/* 113 */ YY_NO_ANCHOR,
		/* 114 */ YY_NO_ANCHOR,
		/* 115 */ YY_NO_ANCHOR,
		/* 116 */ YY_NO_ANCHOR,
		/* 117 */ YY_NO_ANCHOR,
		/* 118 */ YY_NO_ANCHOR,
		/* 119 */ YY_NO_ANCHOR,
		/* 120 */ YY_NO_ANCHOR,
		/* 121 */ YY_NO_ANCHOR,
		/* 122 */ YY_NO_ANCHOR,
		/* 123 */ YY_NO_ANCHOR,
		/* 124 */ YY_NO_ANCHOR,
		/* 125 */ YY_NO_ANCHOR,
		/* 126 */ YY_NO_ANCHOR,
		/* 127 */ YY_NO_ANCHOR,
		/* 128 */ YY_NO_ANCHOR,
		/* 129 */ YY_NO_ANCHOR,
		/* 130 */ YY_NO_ANCHOR,
		/* 131 */ YY_NO_ANCHOR,
		/* 132 */ YY_NO_ANCHOR,
		/* 133 */ YY_NO_ANCHOR,
		/* 134 */ YY_NO_ANCHOR,
		/* 135 */ YY_NO_ANCHOR,
		/* 136 */ YY_NO_ANCHOR,
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
		/* 186 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"47,42:8,51,46,52,51,45,42:18,54,42,41,42:5,48,50,49,55,63,53,65,56,40:10,64" +
",62,58,1,2,42,59,22,23,24,25,26,15,23,27,28,23:2,29,23,30,31,32,23,33,34,8," +
"35,36,37,23:3,42,43,42:2,38,42,14,44,13,18,6,21,39,5,3,39:2,12,39,4,11,10,3" +
"9,7,9,19,20,17,16,39:3,60,42,61,57,42,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,187,
"0,1,2,1,3,4,5,1,6,1,7,8,1:2,9,1:4,10,1:8,11,12:2,13,1:5,12:5,13,12:9,14,1,1" +
"5,1:14,16,1:7,17,18,19,20,13:2,12,13:5,12,13:7,21,22,23,24,25,26,27,28,29,3" +
"0,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,5" +
"5,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,12,72,73,74,75,76,77,78,7" +
"9,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,1" +
"03,104,13,105,106,107,108")[0];

	private int yy_nxt[][] = unpackFromString(109,66,
"1,2,3,4,78,148,157,148,5,148,160,99,162,164,148,79,166,148:2,168,148,103,18" +
"2:2,183,182,184,182,100,147,156,104,185,182:4,186,3,148,6,7,3:2,148,8,9,3,1" +
"0,11,12,8,13,14,15,16,17,18,19,20,21,22,23,24,25,26,-1:68,27,-1:66,148,28,1" +
"48:4,170,148:5,29,148:5,29,148:8,28,148:3,170,148:6,-1:3,148,-1:24,182:2,15" +
"9,182:21,159,182:13,-1:3,182,-1:61,6,-1:70,8,-1:5,8,-1:63,32,-1:66,33,-1:68" +
",34,-1:13,35,-1:51,36,-1:15,148:2,152,148:21,152,148:13,-1:3,148,-1:24,148:" +
"38,-1:3,148,-1:24,182:38,-1:3,182,-1:22,52:40,-1,52,-1,52:2,-1:2,52:18,-1,5" +
"7:3,58,57:14,59,57,60,57:19,61,57,62,63,98,64,65,57:18,-1:45,69,-1:5,69,-1:" +
"2,69,-1:11,1,52:40,53,52,54,52:2,55,56,52:18,-1:3,148:3,106,148:4,109,148:1" +
"4,106,148:4,109,148:9,-1:3,148,-1:24,31,182:24,31,182:12,-1:3,182,-1:24,182" +
":2,175,182:21,175,182:13,-1:3,182,-1:70,72,-1:62,64,-1:22,148:12,30,148:5,3" +
"0,148:19,-1:3,148,-1:24,182,80,182:4,169,182:5,81,182:5,81,182:8,80,182:3,1" +
"69,182:6,-1:3,182,-1:71,73,-1:15,1,66:45,67,66:19,-1:3,83,148:10,126,148:7," +
"126,148:5,83,148:12,-1:3,148,-1:24,182:12,82,182:5,82,182:19,-1:3,182,-1:21" +
",1,68:44,69,70,68,97,101,68,69,71,68,69,68:11,-1:3,148:13,37,148:20,37,148:" +
"3,-1:3,148,-1:24,182:5,86,182:10,86,182:21,-1:3,182,-1:21,1,74:40,75,74:3,1" +
"11,76,74:19,-1:3,148:5,38,148:10,38,148:21,-1:3,148,-1:24,182:13,84,182:20," +
"84,182:3,-1:3,182,-1:67,76,-1:22,148:11,128,148:7,128,148:18,-1:3,148,-1:24" +
",182:5,85,182:10,85,182:21,-1:3,182,-1:24,148:6,130,148:24,130,148:6,-1:3,1" +
"48,-1:24,182,42,182:25,42,182:10,-1:3,182,-1:24,148:8,132,148:19,132,148:9," +
"-1:3,148,-1:24,182:3,92,182:19,92,182:14,-1:3,182,-1:24,148:5,39,148:10,39," +
"148:21,-1:3,148,-1:24,182:10,87,182:10,87,182:16,-1:3,182,-1:24,153,148:24," +
"153,148:12,-1:3,148,-1:24,182:3,88,182:19,88,182:14,-1:3,182,-1:24,148:3,13" +
"7,148:19,137,148:14,-1:3,148,-1:24,182:7,91,182:21,91,182:8,-1:3,182,-1:24," +
"148:17,138,148:14,138,148:5,-1:3,148,-1:24,182:9,90,182:16,90,182:11,-1:3,1" +
"82,-1:24,148:9,154,148:16,154,148:11,-1:3,148,-1:24,182:6,93,182:24,93,182:" +
"6,-1:3,182,-1:24,148:10,40,148:10,40,148:16,-1:3,148,-1:24,182:3,94,182:19," +
"94,182:14,-1:3,182,-1:24,148:3,41,148:19,41,148:14,-1:3,148,-1:24,182:15,95" +
",182:6,95,182:15,-1:3,182,-1:24,148:9,43,148:16,43,148:11,-1:3,148,-1:24,18" +
"2:6,96,182:24,96,182:6,-1:3,182,-1:24,148:7,44,148:21,44,148:8,-1:3,148,-1:" +
"24,148:6,141,148:24,141,148:6,-1:3,148,-1:24,148:3,45,148:19,45,148:14,-1:3" +
",148,-1:24,148,89,148:25,89,148:10,-1:3,148,-1:24,148:3,46,148:19,46,148:14" +
",-1:3,148,-1:24,148:4,155,148:25,155,148:7,-1:3,148,-1:24,144,148:24,144,14" +
"8:12,-1:3,148,-1:24,148:6,47,148:24,47,148:6,-1:3,148,-1:24,148:3,48,148:19" +
",48,148:14,-1:3,148,-1:24,148:3,49,148:19,49,148:14,-1:3,148,-1:24,148:15,5" +
"0,148:6,50,148:15,-1:3,148,-1:24,148:5,146,148:10,146,148:21,-1:3,148,-1:24" +
",148:6,51,148:24,51,148:6,-1:3,148,-1:24,182:3,107,182:4,171,182:14,107,182" +
":4,171,182:9,-1:3,182,-1:24,148:11,135,148:7,135,148:18,-1:3,148,-1:24,148:" +
"6,136,148:24,136,148:6,-1:3,148,-1:24,148:8,134,148:19,134,148:9,-1:3,148,-" +
"1:24,148:3,139,148:19,139,148:14,-1:3,148,-1:24,148:9,142,148:16,142,148:11" +
",-1:3,148,-1:24,148:6,143,148:24,143,148:6,-1:3,148,-1:24,145,148:24,145,14" +
"8:12,-1:3,148,-1:24,182:3,110,182:4,113,182:14,110,182:4,113,182:9,-1:3,182" +
",-1:24,148:6,112,148:2,114,148:16,114,148:4,112,148:6,-1:3,148,-1:24,148:8," +
"140,148:19,140,148:9,-1:3,148,-1:24,182:3,115,182:19,115,182:14,-1:3,182,-1" +
":24,148:8,116,148:19,116,148:9,-1:3,148,-1:24,182:11,174,182:7,174,182:18,-" +
"1:3,182,-1:24,148:3,118,148:4,151,148:14,118,148:4,151,148:9,-1:3,148,-1:24" +
",182:6,117,182:24,117,182:6,-1:3,182,-1:24,148:9,149,148,150,148:7,150,148:" +
"6,149,148:11,-1:3,148,-1:24,182:11,119,182:7,119,182:18,-1:3,182,-1:24,148:" +
"2,120,148:21,120,148:13,-1:3,148,-1:24,182:6,121,182:24,121,182:6,-1:3,182," +
"-1:24,148:2,122,148,124,148:19,122,148:5,124,148:7,-1:3,148,-1:24,182:14,17" +
"6,182:18,176,182:4,-1:3,182,-1:24,148:14,158,148:18,158,148:4,-1:3,148,-1:2" +
"4,182:8,123,182:19,123,182:9,-1:3,182,-1:24,182:8,125,182:19,125,182:9,-1:3" +
",182,-1:24,177,182:24,177,182:12,-1:3,182,-1:24,182:6,127,182:24,127,182:6," +
"-1:3,182,-1:24,182:3,178,182:19,178,182:14,-1:3,182,-1:24,182:8,179,182:19," +
"179,182:9,-1:3,182,-1:24,182:9,129,182:16,129,182:11,-1:3,182,-1:24,182:4,1" +
"80,182:25,180,182:7,-1:3,182,-1:24,131,182:24,131,182:12,-1:3,182,-1:24,181" +
",182:24,181,182:12,-1:3,182,-1:24,182:5,133,182:10,133,182:21,-1:3,182,-1:2" +
"4,182:9,161,182,163,182:7,163,182:6,161,182:11,-1:3,182,-1:24,182:6,165,182" +
":2,167,182:16,167,182:4,165,182:6,-1:3,182,-1:24,182:8,172,182:19,172,182:9" +
",-1:3,182,-1:24,182:2,173,182:21,173,182:13,-1:3,182,-1:21");

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
						{ 
                            return new Symbol(TokenConstants.ERROR, yytext());
                        }
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
                        }
					case -9:
						break;
					case 9:
						{
                            curr_lineno++;
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
                            /* vertical tab */
                        }
					case -14:
						break;
					case 14:
						{
                            return new Symbol(TokenConstants.MINUS);
                        }
					case -15:
						break;
					case 15:
						{
                        }
					case -16:
						break;
					case 16:
						{
                            return new Symbol(TokenConstants.PLUS);
                        }
					case -17:
						break;
					case 17:
						{
                            return new Symbol(TokenConstants.DIV);
                        }
					case -18:
						break;
					case 18:
						{
                            return new Symbol(TokenConstants.NEG);
                        }
					case -19:
						break;
					case 19:
						{
                            return new Symbol(TokenConstants.LT);
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
                            yybegin(COMMENT);
                        }
					case -35:
						break;
					case 35:
						{
                            return new Symbol(TokenConstants.LE);
                        }
					case -36:
						break;
					case 36:
						{
                            return new Symbol(TokenConstants.ASSIGN);
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
                                if (string_buf.length() >= MAX_STR_CONST) {
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
                    } else{
                        yybegin(YYINITIAL);
                        return new Symbol(TokenConstants.STR_CONST, AbstractTable.stringtable.addString(string_buf.toString()));
                    }
                }
					case -54:
						break;
					case 54:
						{
                    string_buf.append(yytext());
                    if (string_buf.length() >= MAX_STR_CONST) {
                        muyLargo = true;
                    }
                }
					case -55:
						break;
					case 55:
						{
                    curr_lineno++;
                    yybegin(YYINITIAL);
                    return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
                }
					case -56:
						break;
					case 56:
						{
                    yybegin(ERRORSTRING);
                    return new Symbol(TokenConstants.ERROR, "String contains null character");
                }
					case -57:
						break;
					case 57:
						{
                    string_buf.append(indice(1));
                    if (string_buf.length() >= MAX_STR_CONST) {
                        muyLargo = true;
                    }
                }
					case -58:
						break;
					case 58:
						{
                    string_buf.append('\n'); 
                    if (string_buf.length() >= MAX_STR_CONST) {
                        muyLargo = true;
                    }
                }
					case -59:
						break;
					case 59:
						{ 
                    string_buf.append('\t'); 
                    if (string_buf.length() >= MAX_STR_CONST) {
                        muyLargo = true;
                    }
                }
					case -60:
						break;
					case 60:
						{ 
                    string_buf.append('\f'); 
                    if (string_buf.length() >= MAX_STR_CONST) {
                        muyLargo = true;
                    }
                }
					case -61:
						break;
					case 61:
						{ 
                    string_buf.append('\"'); 
                    if (string_buf.length() >= MAX_STR_CONST) {
                        muyLargo = true;
                    }
                }
					case -62:
						break;
					case 62:
						{ 
                    string_buf.append('\\'); 
                    if (string_buf.length() >= MAX_STR_CONST) {
                                muyLargo = true;
                    }
                }
					case -63:
						break;
					case 63:
						{
                    string_buf.append('\b'); 
                    if (string_buf.length() >= MAX_STR_CONST) {
                        muyLargo = true;
                    }
                }
					case -64:
						break;
					case 64:
						{ 
                    curr_lineno++; 
                    string_buf.append('\n');
                    if (string_buf.length() >= MAX_STR_CONST) {
                         muyLargo = true;
                    }
                }
					case -65:
						break;
					case 65:
						{
                    yybegin(ERRORSTRING);
                    return new Symbol(TokenConstants.ERROR, "String contains null character");
                }
					case -66:
						break;
					case 66:
						{
                        }
					case -67:
						break;
					case 67:
						{
                            curr_lineno++;
                            yybegin(YYINITIAL);
                        }
					case -68:
						break;
					case 68:
						{  }
					case -69:
						break;
					case 69:
						{
                        }
					case -70:
						break;
					case 70:
						{
                            curr_lineno++;
                        }
					case -71:
						break;
					case 71:
						{ 
                            /* vertical tab en comentario */ 
                        }
					case -72:
						break;
					case 72:
						{
                            cantComents++;
                        }
					case -73:
						break;
					case 73:
						{   
                            cantComents--;
                            if (cantComents == 0) {
                                yybegin(YYINITIAL);
                            }
                            else if (cantComents < 0) {
                                return new Symbol(TokenConstants.ERROR, "Unmatched *)");
                            }
                        }
					case -74:
						break;
					case 74:
						{ 
                        /* comer */ 
                    }
					case -75:
						break;
					case 75:
						{ 
                        yybegin(YYINITIAL); 
                    }
					case -76:
						break;
					case 76:
						{
                        curr_lineno++; 
                        yybegin(YYINITIAL);
                    }
					case -77:
						break;
					case 78:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -78:
						break;
					case 79:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -79:
						break;
					case 80:
						{   
                                                   return new Symbol(TokenConstants.IN);
                                               }
					case -80:
						break;
					case 81:
						{
                                                   return new Symbol(TokenConstants.IF);
                                               }
					case -81:
						break;
					case 82:
						{
                                                   return new Symbol(TokenConstants.OF);
                                               }
					case -82:
						break;
					case 83:
						{
                                                   return new Symbol(TokenConstants.FI);
                                               }
					case -83:
						break;
					case 84:
						{
                                                   return new Symbol(TokenConstants.NEW);
                                               }
					case -84:
						break;
					case 85:
						{
                                                   return new Symbol(TokenConstants.NOT);
                                               }
					case -85:
						break;
					case 86:
						{
                                                   return new Symbol(TokenConstants.LET);
                                               }
					case -86:
						break;
					case 87:
						{
                                                    return new Symbol(TokenConstants.ESAC);
                                               }
					case -87:
						break;
					case 88:
						{
                                                   return new Symbol(TokenConstants.ELSE);
                                               }
					case -88:
						break;
					case 89:
						{
                                                    return new Symbol(TokenConstants.THEN);
                                               }
					case -89:
						break;
					case 90:
						{
                                                   return new Symbol(TokenConstants.POOL);
                                               }
					case -90:
						break;
					case 91:
						{
                                                   return new Symbol(TokenConstants.LOOP);
                                               }
					case -91:
						break;
					case 92:
						{
                                                   return new Symbol(TokenConstants.CASE);
                                               }
					case -92:
						break;
					case 93:
						{
                                                   return new Symbol(TokenConstants.CLASS);
                                               }
					case -93:
						break;
					case 94:
						{
                                                   return new Symbol(TokenConstants.WHILE);
                                               }
					case -94:
						break;
					case 95:
						{
                                                    return new Symbol(TokenConstants.ISVOID);
                                               }
					case -95:
						break;
					case 96:
						{
                                                   return new Symbol(TokenConstants.INHERITS);
                                               }
					case -96:
						break;
					case 97:
						{  }
					case -97:
						break;
					case 99:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -98:
						break;
					case 100:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -99:
						break;
					case 101:
						{  }
					case -100:
						break;
					case 103:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -101:
						break;
					case 104:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -102:
						break;
					case 106:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -103:
						break;
					case 107:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -104:
						break;
					case 109:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -105:
						break;
					case 110:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -106:
						break;
					case 112:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -107:
						break;
					case 113:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -108:
						break;
					case 114:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -109:
						break;
					case 115:
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
					case 118:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -113:
						break;
					case 119:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -114:
						break;
					case 120:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -115:
						break;
					case 121:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -116:
						break;
					case 122:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -117:
						break;
					case 123:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -118:
						break;
					case 124:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -119:
						break;
					case 125:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -120:
						break;
					case 126:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -121:
						break;
					case 127:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -122:
						break;
					case 128:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -123:
						break;
					case 129:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -124:
						break;
					case 130:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -125:
						break;
					case 131:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -126:
						break;
					case 132:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -127:
						break;
					case 133:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -128:
						break;
					case 134:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -129:
						break;
					case 135:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -130:
						break;
					case 136:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -131:
						break;
					case 137:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -132:
						break;
					case 138:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -133:
						break;
					case 139:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -134:
						break;
					case 140:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -135:
						break;
					case 141:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -136:
						break;
					case 142:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -137:
						break;
					case 143:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -138:
						break;
					case 144:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -139:
						break;
					case 145:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -140:
						break;
					case 146:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -141:
						break;
					case 147:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -142:
						break;
					case 148:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -143:
						break;
					case 149:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -144:
						break;
					case 150:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -145:
						break;
					case 151:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -146:
						break;
					case 152:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -147:
						break;
					case 153:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -148:
						break;
					case 154:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -149:
						break;
					case 155:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -150:
						break;
					case 156:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -151:
						break;
					case 157:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -152:
						break;
					case 158:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -153:
						break;
					case 159:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -154:
						break;
					case 160:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -155:
						break;
					case 161:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -156:
						break;
					case 162:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -157:
						break;
					case 163:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -158:
						break;
					case 164:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -159:
						break;
					case 165:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -160:
						break;
					case 166:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -161:
						break;
					case 167:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -162:
						break;
					case 168:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -163:
						break;
					case 169:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -164:
						break;
					case 170:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -165:
						break;
					case 171:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -166:
						break;
					case 172:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -167:
						break;
					case 173:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -168:
						break;
					case 174:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -169:
						break;
					case 175:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -170:
						break;
					case 176:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -171:
						break;
					case 177:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -172:
						break;
					case 178:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -173:
						break;
					case 179:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -174:
						break;
					case 180:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -175:
						break;
					case 181:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -176:
						break;
					case 182:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -177:
						break;
					case 183:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -178:
						break;
					case 184:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -179:
						break;
					case 185:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -180:
						break;
					case 186:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -181:
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
