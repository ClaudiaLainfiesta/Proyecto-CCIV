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
		70,
		93,
		113
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
		/* 70 */ YY_NOT_ACCEPT,
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
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NOT_ACCEPT,
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
		/* 113 */ YY_NOT_ACCEPT,
		/* 114 */ YY_NO_ANCHOR,
		/* 115 */ YY_NO_ANCHOR,
		/* 116 */ YY_NOT_ACCEPT,
		/* 117 */ YY_NO_ANCHOR,
		/* 118 */ YY_NO_ANCHOR,
		/* 119 */ YY_NOT_ACCEPT,
		/* 120 */ YY_NO_ANCHOR,
		/* 121 */ YY_NO_ANCHOR,
		/* 122 */ YY_NOT_ACCEPT,
		/* 123 */ YY_NO_ANCHOR,
		/* 124 */ YY_NO_ANCHOR,
		/* 125 */ YY_NOT_ACCEPT,
		/* 126 */ YY_NO_ANCHOR,
		/* 127 */ YY_NO_ANCHOR,
		/* 128 */ YY_NOT_ACCEPT,
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
		/* 187 */ YY_NO_ANCHOR,
		/* 188 */ YY_NO_ANCHOR,
		/* 189 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"44:9,48,50,49,48:2,44:18,53,44,41,44:5,45,47,46,54,61,52,63,55,43,40:9,62,6" +
"0,51,1,2,44,57,22,23,24,25,26,15,23,27,28,23:2,29,23,30,31,32,23,33,34,8,35" +
",36,37,23:3,44,42,44:2,38,44,14,39,13,18,6,21,39,5,3,39:2,12,39,4,11,10,39," +
"7,9,19,20,17,16,39:3,58,44,59,56,44,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,190,
"0,1,2,1,3,4,5,1,6,7,1,8,1:2,9,10,1:12,11,12:2,13,1:5,12:5,13,12:9,1:5,14,1:" +
"6,15,1:5,16,17,18,19,13:2,12,13:5,12,13:7,20,21,22,23,24,25,26,27,28,29,30," +
"31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55," +
"56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80," +
"12,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103" +
",104,105,106,107,108,109,110,111,112,113,13,114,115,116,117")[0];

	private int yy_nxt[][] = unpackFromString(118,64,
"1,2,3,4,71,151,160,151,5,151,163,94,165,167,151,72,169,151:2,171,151,98,185" +
":2,186,185,187,185,95,150,159,99,188,185:4,189,3,151,6,7,3,6,3,8,9,10,11,12" +
",13,14,15,16,17,18,19,20,21,22,23,24,25,26,-1:66,27,-1:64,151,28,151:4,173," +
"151:5,29,151:5,29,151:8,28,151:3,173,151:6,-1:2,151,-1:23,185:2,162,185:21," +
"162,185:13,-1:2,185,-1:60,6,-1:2,6,-1:66,32,-1:64,33,-1:64,11,-1:16,34,-1:5" +
"0,35,-1:63,36,-1:14,151:2,155,151:21,155,151:13,-1:2,151,-1:23,151:38,-1:2," +
"151,-1:23,185:38,-1:2,185,-1:61,59,-1:70,64,-1:4,64,-1:10,1,52:40,53,90,52:" +
"21,-1:3,151:3,102,151:4,105,151:14,102,151:4,105,151:9,-1:2,151,-1:23,31,18" +
"5:24,31,185:12,-1:2,185,-1:23,185:2,178,185:21,178,185:13,-1:2,185,-1:24,54" +
",-1:2,55,-1:11,56,-1:22,57,58,-1:71,97,-1:58,67,-1:17,1,60:49,61,91,60:12,-" +
"1:3,151:12,30,151:5,30,151:19,-1:2,151,-1:23,185,73,185:4,172,185:5,74,185:" +
"5,74,185:8,73,185:3,172,185:6,-1:2,185,-1:67,68,-1:42,101,-1:40,76,151:10,1" +
"29,151:7,129,151:5,76,151:12,-1:2,151,-1:23,185:12,75,185:5,75,185:19,-1:2," +
"185,-1:71,116,-1:43,104,-1:35,151:13,37,151:20,37,151:3,-1:2,151,-1:23,185:" +
"5,79,185:10,79,185:21,-1:2,185,-1:35,107,-1:51,151:5,38,151:10,38,151:21,-1" +
":2,151,-1:23,185:13,77,185:20,77,185:3,-1:2,185,-1:22,110,-1:64,151:11,131," +
"151:7,131,151:18,-1:2,151,-1:23,185:5,78,185:10,78,185:21,-1:2,185,-1:22,62" +
",-1:64,151:6,133,151:24,133,151:6,-1:2,151,-1:23,185,42,185:25,42,185:10,-1" +
":2,185,-1:20,1,63:44,92,96,63,64,65,66,100,63,64,63:10,-1:3,151:8,135,151:1" +
"9,135,151:9,-1:2,151,-1:23,185:3,85,185:19,85,185:14,-1:2,185,-1:46,119,-1:" +
"40,151:5,39,151:10,39,151:21,-1:2,151,-1:23,185:10,80,185:10,80,185:16,-1:2" +
",185,-1:51,122,-1:35,156,151:24,156,151:12,-1:2,151,-1:23,185:3,81,185:19,8" +
"1,185:14,-1:2,185,-1:35,125,-1:51,151:3,140,151:19,140,151:14,-1:2,151,-1:2" +
"3,185:7,84,185:21,84,185:8,-1:2,185,-1:22,128,-1:64,151:17,141,151:14,141,1" +
"51:5,-1:2,151,-1:23,185:9,83,185:16,83,185:11,-1:2,185,-1:22,69,-1:64,151:9" +
",157,151:16,157,151:11,-1:2,151,-1:23,185:6,86,185:24,86,185:6,-1:2,185,-1:" +
"23,151:10,40,151:10,40,151:16,-1:2,151,-1:23,185:3,87,185:19,87,185:14,-1:2" +
",185,-1:23,151:3,41,151:19,41,151:14,-1:2,151,-1:23,185:15,88,185:6,88,185:" +
"15,-1:2,185,-1:23,151:9,43,151:16,43,151:11,-1:2,151,-1:23,185:6,89,185:24," +
"89,185:6,-1:2,185,-1:23,151:7,44,151:21,44,151:8,-1:2,151,-1:23,151:6,144,1" +
"51:24,144,151:6,-1:2,151,-1:23,151:3,45,151:19,45,151:14,-1:2,151,-1:23,151" +
",82,151:25,82,151:10,-1:2,151,-1:23,151:3,46,151:19,46,151:14,-1:2,151,-1:2" +
"3,151:4,158,151:25,158,151:7,-1:2,151,-1:23,147,151:24,147,151:12,-1:2,151," +
"-1:23,151:6,47,151:24,47,151:6,-1:2,151,-1:23,151:3,48,151:19,48,151:14,-1:" +
"2,151,-1:23,151:3,49,151:19,49,151:14,-1:2,151,-1:23,151:15,50,151:6,50,151" +
":15,-1:2,151,-1:23,151:5,149,151:10,149,151:21,-1:2,151,-1:23,151:6,51,151:" +
"24,51,151:6,-1:2,151,-1:23,185:3,103,185:4,174,185:14,103,185:4,174,185:9,-" +
"1:2,185,-1:23,151:11,138,151:7,138,151:18,-1:2,151,-1:23,151:6,139,151:24,1" +
"39,151:6,-1:2,151,-1:23,151:8,137,151:19,137,151:9,-1:2,151,-1:23,151:3,142" +
",151:19,142,151:14,-1:2,151,-1:23,151:9,145,151:16,145,151:11,-1:2,151,-1:2" +
"3,151:6,146,151:24,146,151:6,-1:2,151,-1:23,148,151:24,148,151:12,-1:2,151," +
"-1:23,185:3,106,185:4,109,185:14,106,185:4,109,185:9,-1:2,185,-1:23,151:6,1" +
"08,151:2,111,151:16,111,151:4,108,151:6,-1:2,151,-1:23,151:8,143,151:19,143" +
",151:9,-1:2,151,-1:23,185:3,112,185:19,112,185:14,-1:2,185,-1:23,151:8,114," +
"151:19,114,151:9,-1:2,151,-1:23,185:11,177,185:7,177,185:18,-1:2,185,-1:23," +
"151:3,117,151:4,154,151:14,117,151:4,154,151:9,-1:2,151,-1:23,185:6,115,185" +
":24,115,185:6,-1:2,185,-1:23,151:9,152,151,153,151:7,153,151:6,152,151:11,-" +
"1:2,151,-1:23,185:11,118,185:7,118,185:18,-1:2,185,-1:23,151:2,120,151:21,1" +
"20,151:13,-1:2,151,-1:23,185:6,121,185:24,121,185:6,-1:2,185,-1:23,151:2,12" +
"3,151,126,151:19,123,151:5,126,151:7,-1:2,151,-1:23,185:14,179,185:18,179,1" +
"85:4,-1:2,185,-1:23,151:14,161,151:18,161,151:4,-1:2,151,-1:23,185:8,124,18" +
"5:19,124,185:9,-1:2,185,-1:23,185:8,127,185:19,127,185:9,-1:2,185,-1:23,180" +
",185:24,180,185:12,-1:2,185,-1:23,185:6,130,185:24,130,185:6,-1:2,185,-1:23" +
",185:3,181,185:19,181,185:14,-1:2,185,-1:23,185:8,182,185:19,182,185:9,-1:2" +
",185,-1:23,185:9,132,185:16,132,185:11,-1:2,185,-1:23,185:4,183,185:25,183," +
"185:7,-1:2,185,-1:23,134,185:24,134,185:12,-1:2,185,-1:23,184,185:24,184,18" +
"5:12,-1:2,185,-1:23,185:5,136,185:10,136,185:21,-1:2,185,-1:23,185:9,164,18" +
"5,166,185:7,166,185:6,164,185:11,-1:2,185,-1:23,185:6,168,185:2,170,185:16," +
"170,185:4,168,185:6,-1:2,185,-1:23,185:8,175,185:19,175,185:9,-1:2,185,-1:2" +
"3,185:2,176,185:21,176,185:13,-1:2,185,-1:20");

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
						{ /* vertical tab */ }
					case -13:
						break;
					case 13:
						{
                            curr_lineno++;
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
                            return new Symbol(TokenConstants.MINUS);
                        }
					case -16:
						break;
					case 16:
						{
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
                            string_buf.append("\n"); 
                        }
					case -55:
						break;
					case 55:
						{ 
                            string_buf.append("\r"); 
                        }
					case -56:
						break;
					case 56:
						{ 
                            string_buf.append("\t"); 
                        }
					case -57:
						break;
					case 57:
						{ 
                            string_buf.append("\\"); 
                        }
					case -58:
						break;
					case 58:
						{
                            return new Symbol(TokenConstants.ERROR, "String contains null character");
                        }
					case -59:
						break;
					case 59:
						{ 
                            string_buf.append('\"'); 
                        }
					case -60:
						break;
					case 60:
						{
                        }
					case -61:
						break;
					case 61:
						{
                            curr_lineno++;
                            yybegin(YYINITIAL);
                        }
					case -62:
						break;
					case 62:
						{ }
					case -63:
						break;
					case 63:
						{  }
					case -64:
						break;
					case 64:
						{
                        }
					case -65:
						break;
					case 65:
						{ /* vertical tab en comentario */ }
					case -66:
						break;
					case 66:
						{
                            curr_lineno++;
                        }
					case -67:
						break;
					case 67:
						{
                            cantComents++;
                        }
					case -68:
						break;
					case 68:
						{   
                            cantComents--;
                            if (cantComents == 0) {
                                yybegin(YYINITIAL);
                            }
                            else if (cantComents < 0) {
                                return new Symbol(TokenConstants.ERROR, "Unmatched *)");
                            }
                        }
					case -69:
						break;
					case 69:
						{ return new Symbol(TokenConstants.ERROR, "EOF in comment"); }
					case -70:
						break;
					case 71:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -71:
						break;
					case 72:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -72:
						break;
					case 73:
						{   
                                                   return new Symbol(TokenConstants.IN);
                                               }
					case -73:
						break;
					case 74:
						{
                                                   return new Symbol(TokenConstants.IF);
                                               }
					case -74:
						break;
					case 75:
						{
                                                   return new Symbol(TokenConstants.OF);
                                               }
					case -75:
						break;
					case 76:
						{
                                                   return new Symbol(TokenConstants.FI);
                                               }
					case -76:
						break;
					case 77:
						{
                                                   return new Symbol(TokenConstants.NEW);
                                               }
					case -77:
						break;
					case 78:
						{
                                                   return new Symbol(TokenConstants.NOT);
                                               }
					case -78:
						break;
					case 79:
						{
                                                   return new Symbol(TokenConstants.LET);
                                               }
					case -79:
						break;
					case 80:
						{
                                                    return new Symbol(TokenConstants.ESAC);
                                               }
					case -80:
						break;
					case 81:
						{
                                                   return new Symbol(TokenConstants.ELSE);
                                               }
					case -81:
						break;
					case 82:
						{
                                                    return new Symbol(TokenConstants.THEN);
                                               }
					case -82:
						break;
					case 83:
						{
                                                   return new Symbol(TokenConstants.POOL);
                                               }
					case -83:
						break;
					case 84:
						{
                                                   return new Symbol(TokenConstants.LOOP);
                                               }
					case -84:
						break;
					case 85:
						{
                                                   return new Symbol(TokenConstants.CASE);
                                               }
					case -85:
						break;
					case 86:
						{
                                                   return new Symbol(TokenConstants.CLASS);
                                               }
					case -86:
						break;
					case 87:
						{
                                                   return new Symbol(TokenConstants.WHILE);
                                               }
					case -87:
						break;
					case 88:
						{
                                                    return new Symbol(TokenConstants.ISVOID);
                                               }
					case -88:
						break;
					case 89:
						{
                                                   return new Symbol(TokenConstants.INHERITS);
                                               }
					case -89:
						break;
					case 90:
						{
                            string_buf.append(yytext());
                            if (string_buf.length() > MAX_STR_CONST) {
                                muyLargo = true;
                            }
                        }
					case -90:
						break;
					case 91:
						{
                        }
					case -91:
						break;
					case 92:
						{  }
					case -92:
						break;
					case 94:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -93:
						break;
					case 95:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -94:
						break;
					case 96:
						{  }
					case -95:
						break;
					case 98:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -96:
						break;
					case 99:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -97:
						break;
					case 100:
						{  }
					case -98:
						break;
					case 102:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -99:
						break;
					case 103:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -100:
						break;
					case 105:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -101:
						break;
					case 106:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -102:
						break;
					case 108:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -103:
						break;
					case 109:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -104:
						break;
					case 111:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -105:
						break;
					case 112:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -106:
						break;
					case 114:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -107:
						break;
					case 115:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -108:
						break;
					case 117:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -109:
						break;
					case 118:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -110:
						break;
					case 120:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -111:
						break;
					case 121:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -112:
						break;
					case 123:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -113:
						break;
					case 124:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -114:
						break;
					case 126:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -115:
						break;
					case 127:
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
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
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
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -146:
						break;
					case 159:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
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
