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
	private final int ERRORSTRING = 4;
	private final int YYINITIAL = 0;
	private final int COMMENT = 2;
	private final int COMMENTS = 3;
	private final int yy_state_dtrans[] = {
		0,
		76,
		101,
		104,
		107
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
		/* 76 */ YY_NOT_ACCEPT,
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
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NOT_ACCEPT,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NOT_ACCEPT,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NOT_ACCEPT,
		/* 105 */ YY_NO_ANCHOR,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NOT_ACCEPT,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NO_ANCHOR,
		/* 110 */ YY_NOT_ACCEPT,
		/* 111 */ YY_NO_ANCHOR,
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
		/* 185 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"47,42:8,51,46,52,51,45,42:18,54,42,41,42:5,48,50,49,55,63,53,65,56,40:10,64" +
",62,58,1,2,42,59,22,23,24,25,26,15,23,27,28,23:2,29,23,30,31,32,23,33,34,8," +
"35,36,37,23:3,42,43,42:2,38,42,14,44,13,18,6,21,39,5,3,39:2,12,39,4,11,10,3" +
"9,7,9,19,20,17,16,39:3,60,42,61,57,42,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,186,
"0,1,2,1,3,4,5,1,6,1,7,8,1:2,9,1:4,10,1:8,11,12:2,13,1:5,12:5,13,12:9,14,1,1" +
"5,1:13,16,1:7,17,18,19,20,13:2,12,13:5,12,13:7,21,22,23,24,25,26,27,28,29,3" +
"0,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,5" +
"5,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,12,72,73,74,75,76,77,78,7" +
"9,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,1" +
"03,104,13,105,106,107,108")[0];

	private int yy_nxt[][] = unpackFromString(109,66,
"1,2,3,4,77,147,156,147,5,147,159,98,161,163,147,78,165,147:2,167,147,102,18" +
"1:2,182,181,183,181,99,146,155,103,184,181:4,185,3,147,6,7,3:2,147,8,9,3,10" +
",11,12,8,13,14,15,16,17,18,19,20,21,22,23,24,25,26,-1:68,27,-1:66,147,28,14" +
"7:4,169,147:5,29,147:5,29,147:8,28,147:3,169,147:6,-1:3,147,-1:24,181:2,158" +
",181:21,158,181:13,-1:3,181,-1:61,6,-1:70,8,-1:5,8,-1:63,32,-1:66,33,-1:68," +
"34,-1:13,35,-1:51,36,-1:15,147:2,151,147:21,151,147:13,-1:3,147,-1:24,147:3" +
"8,-1:3,147,-1:24,181:38,-1:3,181,-1:22,52:40,-1,52,-1,52:2,-1:2,52:18,-1,57" +
":3,58,57:14,59,57,60,57:19,61,57,62,63,97,64,57:19,-1:45,68,-1:5,68,-1:2,68" +
",-1:11,1,52:40,53,52,54,52:2,55,56,52:18,-1:3,147:3,105,147:4,108,147:14,10" +
"5,147:4,108,147:9,-1:3,147,-1:24,31,181:24,31,181:12,-1:3,181,-1:24,181:2,1" +
"74,181:21,174,181:13,-1:3,181,-1:70,71,-1:62,64,-1:22,147:12,30,147:5,30,14" +
"7:19,-1:3,147,-1:24,181,79,181:4,168,181:5,80,181:5,80,181:8,79,181:3,168,1" +
"81:6,-1:3,181,-1:71,72,-1:15,1,65:45,66,65:19,-1:3,82,147:10,125,147:7,125," +
"147:5,82,147:12,-1:3,147,-1:24,181:12,81,181:5,81,181:19,-1:3,181,-1:21,1,6" +
"7:44,68,69,67,96,100,67,68,70,67,68,67:11,-1:3,147:13,37,147:20,37,147:3,-1" +
":3,147,-1:24,181:5,85,181:10,85,181:21,-1:3,181,-1:21,1,73:40,74,73:3,110,7" +
"5,73:19,-1:3,147:5,38,147:10,38,147:21,-1:3,147,-1:24,181:13,83,181:20,83,1" +
"81:3,-1:3,181,-1:67,75,-1:22,147:11,127,147:7,127,147:18,-1:3,147,-1:24,181" +
":5,84,181:10,84,181:21,-1:3,181,-1:24,147:6,129,147:24,129,147:6,-1:3,147,-" +
"1:24,181,42,181:25,42,181:10,-1:3,181,-1:24,147:8,131,147:19,131,147:9,-1:3" +
",147,-1:24,181:3,91,181:19,91,181:14,-1:3,181,-1:24,147:5,39,147:10,39,147:" +
"21,-1:3,147,-1:24,181:10,86,181:10,86,181:16,-1:3,181,-1:24,152,147:24,152," +
"147:12,-1:3,147,-1:24,181:3,87,181:19,87,181:14,-1:3,181,-1:24,147:3,136,14" +
"7:19,136,147:14,-1:3,147,-1:24,181:7,90,181:21,90,181:8,-1:3,181,-1:24,147:" +
"17,137,147:14,137,147:5,-1:3,147,-1:24,181:9,89,181:16,89,181:11,-1:3,181,-" +
"1:24,147:9,153,147:16,153,147:11,-1:3,147,-1:24,181:6,92,181:24,92,181:6,-1" +
":3,181,-1:24,147:10,40,147:10,40,147:16,-1:3,147,-1:24,181:3,93,181:19,93,1" +
"81:14,-1:3,181,-1:24,147:3,41,147:19,41,147:14,-1:3,147,-1:24,181:15,94,181" +
":6,94,181:15,-1:3,181,-1:24,147:9,43,147:16,43,147:11,-1:3,147,-1:24,181:6," +
"95,181:24,95,181:6,-1:3,181,-1:24,147:7,44,147:21,44,147:8,-1:3,147,-1:24,1" +
"47:6,140,147:24,140,147:6,-1:3,147,-1:24,147:3,45,147:19,45,147:14,-1:3,147" +
",-1:24,147,88,147:25,88,147:10,-1:3,147,-1:24,147:3,46,147:19,46,147:14,-1:" +
"3,147,-1:24,147:4,154,147:25,154,147:7,-1:3,147,-1:24,143,147:24,143,147:12" +
",-1:3,147,-1:24,147:6,47,147:24,47,147:6,-1:3,147,-1:24,147:3,48,147:19,48," +
"147:14,-1:3,147,-1:24,147:3,49,147:19,49,147:14,-1:3,147,-1:24,147:15,50,14" +
"7:6,50,147:15,-1:3,147,-1:24,147:5,145,147:10,145,147:21,-1:3,147,-1:24,147" +
":6,51,147:24,51,147:6,-1:3,147,-1:24,181:3,106,181:4,170,181:14,106,181:4,1" +
"70,181:9,-1:3,181,-1:24,147:11,134,147:7,134,147:18,-1:3,147,-1:24,147:6,13" +
"5,147:24,135,147:6,-1:3,147,-1:24,147:8,133,147:19,133,147:9,-1:3,147,-1:24" +
",147:3,138,147:19,138,147:14,-1:3,147,-1:24,147:9,141,147:16,141,147:11,-1:" +
"3,147,-1:24,147:6,142,147:24,142,147:6,-1:3,147,-1:24,144,147:24,144,147:12" +
",-1:3,147,-1:24,181:3,109,181:4,112,181:14,109,181:4,112,181:9,-1:3,181,-1:" +
"24,147:6,111,147:2,113,147:16,113,147:4,111,147:6,-1:3,147,-1:24,147:8,139," +
"147:19,139,147:9,-1:3,147,-1:24,181:3,114,181:19,114,181:14,-1:3,181,-1:24," +
"147:8,115,147:19,115,147:9,-1:3,147,-1:24,181:11,173,181:7,173,181:18,-1:3," +
"181,-1:24,147:3,117,147:4,150,147:14,117,147:4,150,147:9,-1:3,147,-1:24,181" +
":6,116,181:24,116,181:6,-1:3,181,-1:24,147:9,148,147,149,147:7,149,147:6,14" +
"8,147:11,-1:3,147,-1:24,181:11,118,181:7,118,181:18,-1:3,181,-1:24,147:2,11" +
"9,147:21,119,147:13,-1:3,147,-1:24,181:6,120,181:24,120,181:6,-1:3,181,-1:2" +
"4,147:2,121,147,123,147:19,121,147:5,123,147:7,-1:3,147,-1:24,181:14,175,18" +
"1:18,175,181:4,-1:3,181,-1:24,147:14,157,147:18,157,147:4,-1:3,147,-1:24,18" +
"1:8,122,181:19,122,181:9,-1:3,181,-1:24,181:8,124,181:19,124,181:9,-1:3,181" +
",-1:24,176,181:24,176,181:12,-1:3,181,-1:24,181:6,126,181:24,126,181:6,-1:3" +
",181,-1:24,181:3,177,181:19,177,181:14,-1:3,181,-1:24,181:8,178,181:19,178," +
"181:9,-1:3,181,-1:24,181:9,128,181:16,128,181:11,-1:3,181,-1:24,181:4,179,1" +
"81:25,179,181:7,-1:3,181,-1:24,130,181:24,130,181:12,-1:3,181,-1:24,180,181" +
":24,180,181:12,-1:3,181,-1:24,181:5,132,181:10,132,181:21,-1:3,181,-1:24,18" +
"1:9,160,181,162,181:7,162,181:6,160,181:11,-1:3,181,-1:24,181:6,164,181:2,1" +
"66,181:16,166,181:4,164,181:6,-1:3,181,-1:24,181:8,171,181:19,171,181:9,-1:" +
"3,181,-1:24,181:2,172,181:21,172,181:13,-1:3,181,-1:21");

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
                    char c = yytext().charAt(1); 
                    string_buf.append(c);
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
                        }
					case -66:
						break;
					case 66:
						{
                            curr_lineno++;
                            yybegin(YYINITIAL);
                        }
					case -67:
						break;
					case 67:
						{  }
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
                        }
					case -70:
						break;
					case 70:
						{ 
                            /* vertical tab en comentario */ 
                        }
					case -71:
						break;
					case 71:
						{
                        cantComents++;
                    }
					case -72:
						break;
					case 72:
						{   
                        cantComents--;
                        if (cantComents == 0) {
                            yybegin(YYINITIAL);
                        }
                        else if (cantComents < 0) {
                            return new Symbol(TokenConstants.ERROR, "Unmatched *)");
                        }
                    }
					case -73:
						break;
					case 73:
						{ 
                        /* comer */ 
                    }
					case -74:
						break;
					case 74:
						{ 
                        yybegin(YYINITIAL); 
                    }
					case -75:
						break;
					case 75:
						{
                        curr_lineno++; 
                        yybegin(YYINITIAL);
                    }
					case -76:
						break;
					case 77:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -77:
						break;
					case 78:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -78:
						break;
					case 79:
						{   
                                                   return new Symbol(TokenConstants.IN);
                                               }
					case -79:
						break;
					case 80:
						{
                                                   return new Symbol(TokenConstants.IF);
                                               }
					case -80:
						break;
					case 81:
						{
                                                   return new Symbol(TokenConstants.OF);
                                               }
					case -81:
						break;
					case 82:
						{
                                                   return new Symbol(TokenConstants.FI);
                                               }
					case -82:
						break;
					case 83:
						{
                                                   return new Symbol(TokenConstants.NEW);
                                               }
					case -83:
						break;
					case 84:
						{
                                                   return new Symbol(TokenConstants.NOT);
                                               }
					case -84:
						break;
					case 85:
						{
                                                   return new Symbol(TokenConstants.LET);
                                               }
					case -85:
						break;
					case 86:
						{
                                                    return new Symbol(TokenConstants.ESAC);
                                               }
					case -86:
						break;
					case 87:
						{
                                                   return new Symbol(TokenConstants.ELSE);
                                               }
					case -87:
						break;
					case 88:
						{
                                                    return new Symbol(TokenConstants.THEN);
                                               }
					case -88:
						break;
					case 89:
						{
                                                   return new Symbol(TokenConstants.POOL);
                                               }
					case -89:
						break;
					case 90:
						{
                                                   return new Symbol(TokenConstants.LOOP);
                                               }
					case -90:
						break;
					case 91:
						{
                                                   return new Symbol(TokenConstants.CASE);
                                               }
					case -91:
						break;
					case 92:
						{
                                                   return new Symbol(TokenConstants.CLASS);
                                               }
					case -92:
						break;
					case 93:
						{
                                                   return new Symbol(TokenConstants.WHILE);
                                               }
					case -93:
						break;
					case 94:
						{
                                                    return new Symbol(TokenConstants.ISVOID);
                                               }
					case -94:
						break;
					case 95:
						{
                                                   return new Symbol(TokenConstants.INHERITS);
                                               }
					case -95:
						break;
					case 96:
						{  }
					case -96:
						break;
					case 98:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -97:
						break;
					case 99:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -98:
						break;
					case 100:
						{  }
					case -99:
						break;
					case 102:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -100:
						break;
					case 103:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -101:
						break;
					case 105:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -102:
						break;
					case 106:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -103:
						break;
					case 108:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -104:
						break;
					case 109:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -105:
						break;
					case 111:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -106:
						break;
					case 112:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -107:
						break;
					case 113:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -108:
						break;
					case 114:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -109:
						break;
					case 115:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -110:
						break;
					case 116:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -111:
						break;
					case 117:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -112:
						break;
					case 118:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -113:
						break;
					case 119:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -114:
						break;
					case 120:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -115:
						break;
					case 121:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -116:
						break;
					case 122:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -117:
						break;
					case 123:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -118:
						break;
					case 124:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -119:
						break;
					case 125:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -120:
						break;
					case 126:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -121:
						break;
					case 127:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -122:
						break;
					case 128:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -123:
						break;
					case 129:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -124:
						break;
					case 130:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -125:
						break;
					case 131:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -126:
						break;
					case 132:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -127:
						break;
					case 133:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
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
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -141:
						break;
					case 147:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
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
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -150:
						break;
					case 156:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
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
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -153:
						break;
					case 159:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -154:
						break;
					case 160:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -155:
						break;
					case 161:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -156:
						break;
					case 162:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -157:
						break;
					case 163:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -158:
						break;
					case 164:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -159:
						break;
					case 165:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -160:
						break;
					case 166:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -161:
						break;
					case 167:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -162:
						break;
					case 168:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -163:
						break;
					case 169:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -164:
						break;
					case 170:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
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
