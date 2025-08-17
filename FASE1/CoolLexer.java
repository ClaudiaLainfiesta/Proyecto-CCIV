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
		68,
		91,
		111
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
		/* 68 */ YY_NOT_ACCEPT,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
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
		/* 91 */ YY_NOT_ACCEPT,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NOT_ACCEPT,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NOT_ACCEPT,
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
		/* 114 */ YY_NOT_ACCEPT,
		/* 115 */ YY_NO_ANCHOR,
		/* 116 */ YY_NO_ANCHOR,
		/* 117 */ YY_NOT_ACCEPT,
		/* 118 */ YY_NO_ANCHOR,
		/* 119 */ YY_NO_ANCHOR,
		/* 120 */ YY_NOT_ACCEPT,
		/* 121 */ YY_NO_ANCHOR,
		/* 122 */ YY_NO_ANCHOR,
		/* 123 */ YY_NOT_ACCEPT,
		/* 124 */ YY_NO_ANCHOR,
		/* 125 */ YY_NO_ANCHOR,
		/* 126 */ YY_NOT_ACCEPT,
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
		/* 186 */ YY_NO_ANCHOR,
		/* 187 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"44:9,48,49,44,48:2,44:18,52,44,41,44:5,45,47,46,53,60,51,62,54,43,40:9,61,5" +
"9,50,1,2,44,56,22,23,24,25,26,15,23,27,28,23:2,29,23,30,31,32,23,33,34,8,35" +
",36,37,23:3,44,42,44:2,38,44,14,39,13,18,6,21,39,5,3,39:2,12,39,4,11,10,39," +
"7,9,19,20,17,16,39:3,57,44,58,55,44,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,188,
"0,1,2,1,3,4,5,1,6,7,1,8,1,9,10,1:12,11,12:2,13,1:5,12:5,13,12:9,1:5,14,1:6," +
"15,1:4,16,17,18,19,13:2,12,13:5,12,13:7,20,21,22,23,24,25,26,27,28,29,30,31" +
",32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56" +
",57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,12" +
",81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,1" +
"04,105,106,107,108,109,110,111,112,113,13,114,115,116,117")[0];

	private int yy_nxt[][] = unpackFromString(118,63,
"1,2,3,4,69,149,158,149,5,149,161,92,163,165,149,70,167,149:2,169,149,96,183" +
":2,184,183,185,183,93,148,157,97,186,183:4,187,3,149,6,7,3,6,3,8,9,10,11,12" +
",13,14,15,16,17,18,19,20,21,22,23,24,25,-1:65,26,-1:63,149,27,149:4,171,149" +
":5,28,149:5,28,149:8,27,149:3,171,149:6,-1:2,149,-1:22,183:2,160,183:21,160" +
",183:13,-1:2,183,-1:59,6,-1:2,6,-1:65,31,-1:63,32,-1:63,11,-1:15,33,-1:49,3" +
"4,-1:62,35,-1:14,149:2,153,149:21,153,149:13,-1:2,149,-1:22,149:38,-1:2,149" +
",-1:22,183:38,-1:2,183,-1:60,58,-1:69,63,-1:3,63,-1:10,1,51:40,52,88,51:20," +
"-1:3,149:3,100,149:4,103,149:14,100,149:4,103,149:9,-1:2,149,-1:22,30,183:2" +
"4,30,183:12,-1:2,183,-1:22,183:2,176,183:21,176,183:13,-1:2,183,-1:23,53,-1" +
":2,54,-1:11,55,-1:22,56,57,-1:69,95,-1:58,65,-1:16,1,59:48,60,89,59:12,-1:3" +
",149:12,29,149:5,29,149:19,-1:2,149,-1:22,183,71,183:4,170,183:5,72,183:5,7" +
"2,183:8,71,183:3,170,183:6,-1:2,183,-1:66,66,-1:41,99,-1:39,74,149:10,127,1" +
"49:7,127,149:5,74,149:12,-1:2,149,-1:22,183:12,73,183:5,73,183:19,-1:2,183," +
"-1:69,114,-1:43,102,-1:34,149:13,36,149:20,36,149:3,-1:2,149,-1:22,183:5,77" +
",183:10,77,183:21,-1:2,183,-1:34,105,-1:50,149:5,37,149:10,37,149:21,-1:2,1" +
"49,-1:22,183:13,75,183:20,75,183:3,-1:2,183,-1:21,108,-1:63,149:11,129,149:" +
"7,129,149:18,-1:2,149,-1:22,183:5,76,183:10,76,183:21,-1:2,183,-1:21,61,-1:" +
"63,149:6,131,149:24,131,149:6,-1:2,149,-1:22,183,41,183:25,41,183:10,-1:2,1" +
"83,-1:19,1,62:44,90,94,62,63,64,98,62,63,62:10,-1:3,149:8,133,149:19,133,14" +
"9:9,-1:2,149,-1:22,183:3,83,183:19,83,183:14,-1:2,183,-1:45,117,-1:39,149:5" +
",38,149:10,38,149:21,-1:2,149,-1:22,183:10,78,183:10,78,183:16,-1:2,183,-1:" +
"50,120,-1:34,154,149:24,154,149:12,-1:2,149,-1:22,183:3,79,183:19,79,183:14" +
",-1:2,183,-1:34,123,-1:50,149:3,138,149:19,138,149:14,-1:2,149,-1:22,183:7," +
"82,183:21,82,183:8,-1:2,183,-1:21,126,-1:63,149:17,139,149:14,139,149:5,-1:" +
"2,149,-1:22,183:9,81,183:16,81,183:11,-1:2,183,-1:21,67,-1:63,149:9,155,149" +
":16,155,149:11,-1:2,149,-1:22,183:6,84,183:24,84,183:6,-1:2,183,-1:22,149:1" +
"0,39,149:10,39,149:16,-1:2,149,-1:22,183:3,85,183:19,85,183:14,-1:2,183,-1:" +
"22,149:3,40,149:19,40,149:14,-1:2,149,-1:22,183:15,86,183:6,86,183:15,-1:2," +
"183,-1:22,149:9,42,149:16,42,149:11,-1:2,149,-1:22,183:6,87,183:24,87,183:6" +
",-1:2,183,-1:22,149:7,43,149:21,43,149:8,-1:2,149,-1:22,149:6,142,149:24,14" +
"2,149:6,-1:2,149,-1:22,149:3,44,149:19,44,149:14,-1:2,149,-1:22,149,80,149:" +
"25,80,149:10,-1:2,149,-1:22,149:3,45,149:19,45,149:14,-1:2,149,-1:22,149:4," +
"156,149:25,156,149:7,-1:2,149,-1:22,145,149:24,145,149:12,-1:2,149,-1:22,14" +
"9:6,46,149:24,46,149:6,-1:2,149,-1:22,149:3,47,149:19,47,149:14,-1:2,149,-1" +
":22,149:3,48,149:19,48,149:14,-1:2,149,-1:22,149:15,49,149:6,49,149:15,-1:2" +
",149,-1:22,149:5,147,149:10,147,149:21,-1:2,149,-1:22,149:6,50,149:24,50,14" +
"9:6,-1:2,149,-1:22,183:3,101,183:4,172,183:14,101,183:4,172,183:9,-1:2,183," +
"-1:22,149:11,136,149:7,136,149:18,-1:2,149,-1:22,149:6,137,149:24,137,149:6" +
",-1:2,149,-1:22,149:8,135,149:19,135,149:9,-1:2,149,-1:22,149:3,140,149:19," +
"140,149:14,-1:2,149,-1:22,149:9,143,149:16,143,149:11,-1:2,149,-1:22,149:6," +
"144,149:24,144,149:6,-1:2,149,-1:22,146,149:24,146,149:12,-1:2,149,-1:22,18" +
"3:3,104,183:4,107,183:14,104,183:4,107,183:9,-1:2,183,-1:22,149:6,106,149:2" +
",109,149:16,109,149:4,106,149:6,-1:2,149,-1:22,149:8,141,149:19,141,149:9,-" +
"1:2,149,-1:22,183:3,110,183:19,110,183:14,-1:2,183,-1:22,149:8,112,149:19,1" +
"12,149:9,-1:2,149,-1:22,183:11,175,183:7,175,183:18,-1:2,183,-1:22,149:3,11" +
"5,149:4,152,149:14,115,149:4,152,149:9,-1:2,149,-1:22,183:6,113,183:24,113," +
"183:6,-1:2,183,-1:22,149:9,150,149,151,149:7,151,149:6,150,149:11,-1:2,149," +
"-1:22,183:11,116,183:7,116,183:18,-1:2,183,-1:22,149:2,118,149:21,118,149:1" +
"3,-1:2,149,-1:22,183:6,119,183:24,119,183:6,-1:2,183,-1:22,149:2,121,149,12" +
"4,149:19,121,149:5,124,149:7,-1:2,149,-1:22,183:14,177,183:18,177,183:4,-1:" +
"2,183,-1:22,149:14,159,149:18,159,149:4,-1:2,149,-1:22,183:8,122,183:19,122" +
",183:9,-1:2,183,-1:22,183:8,125,183:19,125,183:9,-1:2,183,-1:22,178,183:24," +
"178,183:12,-1:2,183,-1:22,183:6,128,183:24,128,183:6,-1:2,183,-1:22,183:3,1" +
"79,183:19,179,183:14,-1:2,183,-1:22,183:8,180,183:19,180,183:9,-1:2,183,-1:" +
"22,183:9,130,183:16,130,183:11,-1:2,183,-1:22,183:4,181,183:25,181,183:7,-1" +
":2,183,-1:22,132,183:24,132,183:12,-1:2,183,-1:22,182,183:24,182,183:12,-1:" +
"2,183,-1:22,183:5,134,183:10,134,183:21,-1:2,183,-1:22,183:9,162,183,164,18" +
"3:7,164,183:6,162,183:11,-1:2,183,-1:22,183:6,166,183:2,168,183:16,168,183:" +
"4,166,183:6,-1:2,183,-1:22,183:8,173,183:19,173,183:9,-1:2,183,-1:22,183:2," +
"174,183:21,174,183:13,-1:2,183,-1:19");

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
                            return new Symbol(TokenConstants.LPAREN);
                        }
					case -9:
						break;
					case 9:
						{
                            return new Symbol(TokenConstants.MULT);
                        }
					case -10:
						break;
					case 10:
						{
                            return new Symbol(TokenConstants.RPAREN);
                        }
					case -11:
						break;
					case 11:
						{
                        }
					case -12:
						break;
					case 12:
						{
                            curr_lineno++;
                        }
					case -13:
						break;
					case 13:
						{
                            return new Symbol(TokenConstants.LT);
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
                            return new Symbol(TokenConstants.AT);
                        }
					case -20:
						break;
					case 20:
						{
                            return new Symbol(TokenConstants.LBRACE);
                        }
					case -21:
						break;
					case 21:
						{
                            return new Symbol(TokenConstants.RBRACE);
                        }
					case -22:
						break;
					case 22:
						{
                            return new Symbol(TokenConstants.SEMI);
                        }
					case -23:
						break;
					case 23:
						{
                            return new Symbol(TokenConstants.COMMA);
                        }
					case -24:
						break;
					case 24:
						{
                            return new Symbol(TokenConstants.COLON);
                        }
					case -25:
						break;
					case 25:
						{
                            return new Symbol(TokenConstants.DOT);
                        }
					case -26:
						break;
					case 26:
						{ /* Sample lexical rule for "=>" arrow.
                                     Further lexical rules should be defined
                                     here, after the last %% separator */
                                  return new Symbol(TokenConstants.DARROW); }
					case -27:
						break;
					case 27:
						{   
                                                   return new Symbol(TokenConstants.IN);
                                               }
					case -28:
						break;
					case 28:
						{
                                                   return new Symbol(TokenConstants.IF);
                                               }
					case -29:
						break;
					case 29:
						{
                                                   return new Symbol(TokenConstants.OF);
                                               }
					case -30:
						break;
					case 30:
						{
                                                   return new Symbol(TokenConstants.FI);
                                               }
					case -31:
						break;
					case 31:
						{   
                            cantComents++;
                            yybegin(COMMENTS);
                        }
					case -32:
						break;
					case 32:
						{
                            return new Symbol(TokenConstants.ERROR, "Unmatched *)");
                        }
					case -33:
						break;
					case 33:
						{
                            return new Symbol(TokenConstants.LE);
                        }
					case -34:
						break;
					case 34:
						{
                            return new Symbol(TokenConstants.ASSIGN);
                        }
					case -35:
						break;
					case 35:
						{
                            yybegin(COMMENT);
                        }
					case -36:
						break;
					case 36:
						{
                                                   return new Symbol(TokenConstants.NEW);
                                               }
					case -37:
						break;
					case 37:
						{
                                                   return new Symbol(TokenConstants.NOT);
                                               }
					case -38:
						break;
					case 38:
						{
                                                   return new Symbol(TokenConstants.LET);
                                               }
					case -39:
						break;
					case 39:
						{
                                                    return new Symbol(TokenConstants.ESAC);
                                               }
					case -40:
						break;
					case 40:
						{
                                                   return new Symbol(TokenConstants.ELSE);
                                               }
					case -41:
						break;
					case 41:
						{
                                                    return new Symbol(TokenConstants.THEN);
                                               }
					case -42:
						break;
					case 42:
						{
                                                   return new Symbol(TokenConstants.POOL);
                                               }
					case -43:
						break;
					case 43:
						{
                                                   return new Symbol(TokenConstants.LOOP);
                                               }
					case -44:
						break;
					case 44:
						{
                                                   return new Symbol(TokenConstants.CASE);
                                               }
					case -45:
						break;
					case 45:
						{
                                                   return new Symbol(TokenConstants.BOOL_CONST, true);
                                               }
					case -46:
						break;
					case 46:
						{
                                                   return new Symbol(TokenConstants.CLASS);
                                               }
					case -47:
						break;
					case 47:
						{
                                                   return new Symbol(TokenConstants.WHILE);
                                               }
					case -48:
						break;
					case 48:
						{
                                                   return new Symbol(TokenConstants.BOOL_CONST, false);
                                               }
					case -49:
						break;
					case 49:
						{
                                                    return new Symbol(TokenConstants.ISVOID);
                                               }
					case -50:
						break;
					case 50:
						{
                                                   return new Symbol(TokenConstants.INHERITS);
                                               }
					case -51:
						break;
					case 51:
						{
                            string_buf.append(yytext());
                            if (string_buf.length() > MAX_STR_CONST) {
                                muyLargo = true;
                            }
                        }
					case -52:
						break;
					case 52:
						{
                            if(muyLargo == true){
                                yybegin(YYINITIAL);
                                return new Symbol(TokenConstants.ERROR, "String constant too long");
                            }
                            yybegin(YYINITIAL);
                            return new Symbol(TokenConstants.STR_CONST, AbstractTable.stringtable.addString(string_buf.toString()));
                        }
					case -53:
						break;
					case 53:
						{ 
                            string_buf.append("\n"); 
                        }
					case -54:
						break;
					case 54:
						{ 
                            string_buf.append("\r"); 
                        }
					case -55:
						break;
					case 55:
						{ 
                            string_buf.append("\t"); 
                        }
					case -56:
						break;
					case 56:
						{ 
                            string_buf.append("\\"); 
                        }
					case -57:
						break;
					case 57:
						{
                            return new Symbol(TokenConstants.ERROR, "String contains null character");
                        }
					case -58:
						break;
					case 58:
						{ 
                            string_buf.append('\"'); 
                        }
					case -59:
						break;
					case 59:
						{
                        }
					case -60:
						break;
					case 60:
						{
                            curr_lineno++;
                            yybegin(YYINITIAL);
                        }
					case -61:
						break;
					case 61:
						{ }
					case -62:
						break;
					case 62:
						{  }
					case -63:
						break;
					case 63:
						{
                        }
					case -64:
						break;
					case 64:
						{
                            curr_lineno++;
                        }
					case -65:
						break;
					case 65:
						{
                            cantComents++;
                        }
					case -66:
						break;
					case 66:
						{   
                            cantComents--;
                            if (cantComents == 0) {
                                yybegin(YYINITIAL);
                            }
                            else if (cantComents < 0) {
                                return new Symbol(TokenConstants.ERROR, "Unmatched *)");
                            }
                        }
					case -67:
						break;
					case 67:
						{ return new Symbol(TokenConstants.ERROR, "EOF in comment"); }
					case -68:
						break;
					case 69:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -69:
						break;
					case 70:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -70:
						break;
					case 71:
						{   
                                                   return new Symbol(TokenConstants.IN);
                                               }
					case -71:
						break;
					case 72:
						{
                                                   return new Symbol(TokenConstants.IF);
                                               }
					case -72:
						break;
					case 73:
						{
                                                   return new Symbol(TokenConstants.OF);
                                               }
					case -73:
						break;
					case 74:
						{
                                                   return new Symbol(TokenConstants.FI);
                                               }
					case -74:
						break;
					case 75:
						{
                                                   return new Symbol(TokenConstants.NEW);
                                               }
					case -75:
						break;
					case 76:
						{
                                                   return new Symbol(TokenConstants.NOT);
                                               }
					case -76:
						break;
					case 77:
						{
                                                   return new Symbol(TokenConstants.LET);
                                               }
					case -77:
						break;
					case 78:
						{
                                                    return new Symbol(TokenConstants.ESAC);
                                               }
					case -78:
						break;
					case 79:
						{
                                                   return new Symbol(TokenConstants.ELSE);
                                               }
					case -79:
						break;
					case 80:
						{
                                                    return new Symbol(TokenConstants.THEN);
                                               }
					case -80:
						break;
					case 81:
						{
                                                   return new Symbol(TokenConstants.POOL);
                                               }
					case -81:
						break;
					case 82:
						{
                                                   return new Symbol(TokenConstants.LOOP);
                                               }
					case -82:
						break;
					case 83:
						{
                                                   return new Symbol(TokenConstants.CASE);
                                               }
					case -83:
						break;
					case 84:
						{
                                                   return new Symbol(TokenConstants.CLASS);
                                               }
					case -84:
						break;
					case 85:
						{
                                                   return new Symbol(TokenConstants.WHILE);
                                               }
					case -85:
						break;
					case 86:
						{
                                                    return new Symbol(TokenConstants.ISVOID);
                                               }
					case -86:
						break;
					case 87:
						{
                                                   return new Symbol(TokenConstants.INHERITS);
                                               }
					case -87:
						break;
					case 88:
						{
                            string_buf.append(yytext());
                            if (string_buf.length() > MAX_STR_CONST) {
                                muyLargo = true;
                            }
                        }
					case -88:
						break;
					case 89:
						{
                        }
					case -89:
						break;
					case 90:
						{  }
					case -90:
						break;
					case 92:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -91:
						break;
					case 93:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -92:
						break;
					case 94:
						{  }
					case -93:
						break;
					case 96:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -94:
						break;
					case 97:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -95:
						break;
					case 98:
						{  }
					case -96:
						break;
					case 100:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -97:
						break;
					case 101:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -98:
						break;
					case 103:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -99:
						break;
					case 104:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -100:
						break;
					case 106:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -101:
						break;
					case 107:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -102:
						break;
					case 109:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -103:
						break;
					case 110:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -104:
						break;
					case 112:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -105:
						break;
					case 113:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -106:
						break;
					case 115:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -107:
						break;
					case 116:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -108:
						break;
					case 118:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -109:
						break;
					case 119:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -110:
						break;
					case 121:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -111:
						break;
					case 122:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -112:
						break;
					case 124:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -113:
						break;
					case 125:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -114:
						break;
					case 127:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -115:
						break;
					case 128:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -116:
						break;
					case 129:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -117:
						break;
					case 130:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -118:
						break;
					case 131:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -119:
						break;
					case 132:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -120:
						break;
					case 133:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -121:
						break;
					case 134:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -122:
						break;
					case 135:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -123:
						break;
					case 136:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
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
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
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
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
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
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
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
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
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
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
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
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -145:
						break;
					case 158:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
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
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
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
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
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
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
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
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -154:
						break;
					case 167:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -155:
						break;
					case 168:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
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
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
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
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
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
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
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
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
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
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
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
