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
	private final int STR_ERRSKIP = 4;
	private final int STRING = 1;
	private final int YYINITIAL = 0;
	private final int COMMENT = 2;
	private final int COMMENTS = 3;
	private final int yy_state_dtrans[] = {
		0,
		79,
		107,
		126,
		144
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
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NOT_ACCEPT,
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
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NOT_ACCEPT,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NO_ANCHOR,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NOT_ACCEPT,
		/* 108 */ YY_NO_ANCHOR,
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
		/* 129 */ YY_NOT_ACCEPT,
		/* 130 */ YY_NO_ANCHOR,
		/* 131 */ YY_NO_ANCHOR,
		/* 132 */ YY_NOT_ACCEPT,
		/* 133 */ YY_NO_ANCHOR,
		/* 134 */ YY_NO_ANCHOR,
		/* 135 */ YY_NOT_ACCEPT,
		/* 136 */ YY_NO_ANCHOR,
		/* 137 */ YY_NO_ANCHOR,
		/* 138 */ YY_NOT_ACCEPT,
		/* 139 */ YY_NO_ANCHOR,
		/* 140 */ YY_NO_ANCHOR,
		/* 141 */ YY_NOT_ACCEPT,
		/* 142 */ YY_NO_ANCHOR,
		/* 143 */ YY_NO_ANCHOR,
		/* 144 */ YY_NOT_ACCEPT,
		/* 145 */ YY_NO_ANCHOR,
		/* 146 */ YY_NO_ANCHOR,
		/* 147 */ YY_NOT_ACCEPT,
		/* 148 */ YY_NO_ANCHOR,
		/* 149 */ YY_NO_ANCHOR,
		/* 150 */ YY_NOT_ACCEPT,
		/* 151 */ YY_NO_ANCHOR,
		/* 152 */ YY_NOT_ACCEPT,
		/* 153 */ YY_NO_ANCHOR,
		/* 154 */ YY_NOT_ACCEPT,
		/* 155 */ YY_NO_ANCHOR,
		/* 156 */ YY_NOT_ACCEPT,
		/* 157 */ YY_NO_ANCHOR,
		/* 158 */ YY_NOT_ACCEPT,
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
		/* 197 */ YY_NO_ANCHOR,
		/* 198 */ YY_NO_ANCHOR,
		/* 199 */ YY_NO_ANCHOR,
		/* 200 */ YY_NO_ANCHOR,
		/* 201 */ YY_NO_ANCHOR,
		/* 202 */ YY_NO_ANCHOR,
		/* 203 */ YY_NO_ANCHOR,
		/* 204 */ YY_NO_ANCHOR,
		/* 205 */ YY_NO_ANCHOR,
		/* 206 */ YY_NO_ANCHOR,
		/* 207 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"47,46:8,52,42,53,52,45,46:18,55,46,41,46:5,49,51,50,56,63,54,65,57,40:10,64" +
",62,48,1,2,46,59,22,23,24,25,26,15,23,27,28,23:2,29,23,30,31,32,23,33,34,8," +
"35,36,37,23:3,46,43,46:2,38,46,14,44,13,18,6,21,39,5,3,39:2,12,39,4,11,10,3" +
"9,7,9,19,20,17,16,39:3,60,46,61,58,46,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,208,
"0,1,2,1,3,4,5,1:2,6,7,8,9,1:2,10,1:12,11,12:2,13,1:5,12:5,13,12:9,14,1:3,14" +
",1:13,15,1:8,16,17,18,19,13:2,12,13:5,12,13:7,20,21,22,23,24,25,26,27,28,29" +
",30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54" +
",55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79" +
",80,81,82,83,84,85,86,87,88,89,12,90,91,92,93,94,95,96,97,98,99,100,101,102" +
",103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,12" +
"1,122,13,123,124,125,126")[0];

	private int yy_nxt[][] = unpackFromString(127,66,
"1,2,3,4,80,169,178,169,5,169,181,104,183,185,169,81,187,169:2,189,169,108,2" +
"03:2,204,203,205,203,105,168,177,109,206,203:4,207,3,169,6,7,8,3,169,9,3:2," +
"10,11,12,13,9,14,15,16,17,18,19,20,21,22,23,24,25,26,-1:68,27,-1:66,169,28," +
"169:4,191,169:5,29,169:5,29,169:8,28,169:3,191,169:6,-1:3,169,-1:24,203:2,1" +
"80,203:21,180,203:13,-1:3,203,-1:61,6,-1:70,9,-1:6,9,-1:14,32,-1:52,33,-1:6" +
"1,34,-1:66,35,-1:68,36,-1:14,169:2,173,169:21,173,169:13,-1:3,169,-1:24,169" +
":38,-1:3,169,-1:24,203:38,-1:3,203,-1:22,56:40,-1:3,56:3,-1,56:18,-1:45,70," +
"-1:6,70,-1:2,70,-1:10,1,52:40,53,54,99,52:3,55,52:18,-1:3,169:3,112,169:4,1" +
"15,169:14,112,169:4,115,169:9,-1:3,169,-1:24,31,203:24,31,203:12,-1:3,203,-" +
"1:24,203:2,196,203:21,196,203:13,-1:3,203,-1:22,57:3,58,57:14,59,57,60,57:1" +
"9,61,62,63,64,103,57:20,-1:48,111,-1:65,129,-1:65,150,-1:59,62,-1:26,169:12" +
",30,169:5,30,169:19,-1:3,169,-1:24,203,82,203:4,190,203:5,83,203:5,83,203:8" +
",82,203:3,190,203:6,-1:3,203,-1:71,72,-1:15,1,65:41,66,65:5,100,65:17,-1:3," +
"85,169:10,139,169:7,139,169:5,85,169:12,-1:3,169,-1:24,203:12,84,203:5,84,2" +
"03:19,-1:3,203,-1:72,73,-1:40,114,-1:42,169:13,37,169:20,37,169:3,-1:3,169," +
"-1:24,203:5,88,203:10,88,203:21,-1:3,203,-1:52,117,-1:37,169:5,38,169:10,38" +
",169:21,-1:3,169,-1:24,203:13,86,203:20,86,203:3,-1:3,203,-1:36,120,-1:53,1" +
"69:11,142,169:7,142,169:18,-1:3,169,-1:24,203:5,87,203:10,87,203:21,-1:3,20" +
"3,-1:23,123,-1:66,169:6,145,169:24,145,169:6,-1:3,169,-1:24,203,42,203:25,4" +
"2,203:10,-1:3,203,-1:23,67,-1:66,169:8,148,169:19,148,169:9,-1:3,169,-1:24," +
"203:3,94,203:19,94,203:14,-1:3,203,-1:21,1,68:41,69,68:2,70,68:2,101,106,11" +
"0,68,70,71,68,70,68:10,-1:3,169:5,39,169:10,39,169:21,-1:3,169,-1:24,203:10" +
",89,203:10,89,203:16,-1:3,203,-1:47,132,-1:42,174,169:24,174,169:12,-1:3,16" +
"9,-1:24,203:3,90,203:19,90,203:14,-1:3,203,-1:52,135,-1:37,169:3,157,169:19" +
",157,169:14,-1:3,169,-1:24,203:7,93,203:21,93,203:8,-1:3,203,-1:36,138,-1:5" +
"3,169:17,159,169:14,159,169:5,-1:3,169,-1:24,203:9,92,203:16,92,203:11,-1:3" +
",203,-1:23,141,-1:66,169:9,175,169:16,175,169:11,-1:3,169,-1:24,203:6,95,20" +
"3:24,95,203:6,-1:3,203,-1:23,74,-1:66,169:10,40,169:10,40,169:16,-1:3,169,-" +
"1:24,203:3,96,203:19,96,203:14,-1:3,203,-1:21,1,75:40,76,77,75:2,147,75:2,1" +
"02,75:17,-1:3,169:3,41,169:19,41,169:14,-1:3,169,-1:24,203:15,97,203:6,97,2" +
"03:15,-1:3,203,-1:63,77,-1:26,169:9,43,169:16,43,169:11,-1:3,169,-1:24,203:" +
"6,98,203:24,98,203:6,-1:3,203,-1:47,152,-1:42,169:7,44,169:21,44,169:8,-1:3" +
",169,-1:52,154,-1:37,169:6,162,169:24,162,169:6,-1:3,169,-1:36,156,-1:53,16" +
"9:3,45,169:19,45,169:14,-1:3,169,-1:23,158,-1:66,169,91,169:25,91,169:10,-1" +
":3,169,-1:23,78,-1:66,169:3,46,169:19,46,169:14,-1:3,169,-1:24,169:4,176,16" +
"9:25,176,169:7,-1:3,169,-1:24,165,169:24,165,169:12,-1:3,169,-1:24,169:6,47" +
",169:24,47,169:6,-1:3,169,-1:24,169:3,48,169:19,48,169:14,-1:3,169,-1:24,16" +
"9:3,49,169:19,49,169:14,-1:3,169,-1:24,169:15,50,169:6,50,169:15,-1:3,169,-" +
"1:24,169:5,167,169:10,167,169:21,-1:3,169,-1:24,169:6,51,169:24,51,169:6,-1" +
":3,169,-1:24,203:3,113,203:4,192,203:14,113,203:4,192,203:9,-1:3,203,-1:24," +
"169:11,153,169:7,153,169:18,-1:3,169,-1:24,169:6,155,169:24,155,169:6,-1:3," +
"169,-1:24,169:8,151,169:19,151,169:9,-1:3,169,-1:24,169:3,160,169:19,160,16" +
"9:14,-1:3,169,-1:24,169:9,163,169:16,163,169:11,-1:3,169,-1:24,169:6,164,16" +
"9:24,164,169:6,-1:3,169,-1:24,166,169:24,166,169:12,-1:3,169,-1:24,203:3,11" +
"6,203:4,119,203:14,116,203:4,119,203:9,-1:3,203,-1:24,169:6,118,169:2,121,1" +
"69:16,121,169:4,118,169:6,-1:3,169,-1:24,169:8,161,169:19,161,169:9,-1:3,16" +
"9,-1:24,203:3,122,203:19,122,203:14,-1:3,203,-1:24,169:8,124,169:19,124,169" +
":9,-1:3,169,-1:24,203:11,195,203:7,195,203:18,-1:3,203,-1:24,169:3,127,169:" +
"4,172,169:14,127,169:4,172,169:9,-1:3,169,-1:24,203:6,125,203:24,125,203:6," +
"-1:3,203,-1:24,169:9,170,169,171,169:7,171,169:6,170,169:11,-1:3,169,-1:24," +
"203:11,128,203:7,128,203:18,-1:3,203,-1:24,169:2,130,169:21,130,169:13,-1:3" +
",169,-1:24,203:6,131,203:24,131,203:6,-1:3,203,-1:24,169:2,133,169,136,169:" +
"19,133,169:5,136,169:7,-1:3,169,-1:24,203:14,197,203:18,197,203:4,-1:3,203," +
"-1:24,169:14,179,169:18,179,169:4,-1:3,169,-1:24,203:8,134,203:19,134,203:9" +
",-1:3,203,-1:24,203:8,137,203:19,137,203:9,-1:3,203,-1:24,198,203:24,198,20" +
"3:12,-1:3,203,-1:24,203:6,140,203:24,140,203:6,-1:3,203,-1:24,203:3,199,203" +
":19,199,203:14,-1:3,203,-1:24,203:8,200,203:19,200,203:9,-1:3,203,-1:24,203" +
":9,143,203:16,143,203:11,-1:3,203,-1:24,203:4,201,203:25,201,203:7,-1:3,203" +
",-1:24,146,203:24,146,203:12,-1:3,203,-1:24,202,203:24,202,203:12,-1:3,203," +
"-1:24,203:5,149,203:10,149,203:21,-1:3,203,-1:24,203:9,182,203,184,203:7,18" +
"4,203:6,182,203:11,-1:3,203,-1:24,203:6,186,203:2,188,203:16,188,203:4,186," +
"203:6,-1:3,203,-1:24,203:8,193,203:19,193,203:9,-1:3,203,-1:24,203:2,194,20" +
"3:21,194,203:13,-1:3,203,-1:21");

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
                            return new Symbol(TokenConstants.LT);
                        }
					case -11:
						break;
					case 11:
						{
                            return new Symbol(TokenConstants.LPAREN);
                        }
					case -12:
						break;
					case 12:
						{
                            return new Symbol(TokenConstants.MULT);
                        }
					case -13:
						break;
					case 13:
						{
                            return new Symbol(TokenConstants.RPAREN);
                        }
					case -14:
						break;
					case 14:
						{ /* vertical tab */ }
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
                            return new Symbol(TokenConstants.LE);
                        }
					case -33:
						break;
					case 33:
						{
                            return new Symbol(TokenConstants.ASSIGN);
                        }
					case -34:
						break;
					case 34:
						{   
                            cantComents++;
                            yybegin(COMMENTS);
                        }
					case -35:
						break;
					case 35:
						{
                            return new Symbol(TokenConstants.ERROR, "Unmatched *)");
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
                        curr_lineno++;
                        yybegin(YYINITIAL);
                            return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
}
					case -55:
						break;
					case 55:
						{
    yybegin(STR_ERRSKIP);
    return new Symbol(TokenConstants.ERROR, "String contains null character");
}
					case -56:
						break;
					case 56:
						{
    string_buf.append(yytext());
    if (string_buf.length() > MAX_STR_CONST) {
        muyLargo = true;
    }
}
					case -57:
						break;
					case 57:
						{
    char c = yytext().charAt(1); 
    string_buf.append(c);
}
					case -58:
						break;
					case 58:
						{ string_buf.append('\n'); }
					case -59:
						break;
					case 59:
						{ string_buf.append('\t'); }
					case -60:
						break;
					case 60:
						{ string_buf.append('\f'); }
					case -61:
						break;
					case 61:
						{ string_buf.append('\"'); }
					case -62:
						break;
					case 62:
						{ curr_lineno++; }
					case -63:
						break;
					case 63:
						{ string_buf.append('\\'); }
					case -64:
						break;
					case 64:
						{ string_buf.append('\b'); }
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
						{ }
					case -68:
						break;
					case 68:
						{  }
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
                        }
					case -71:
						break;
					case 71:
						{ /* vertical tab en comentario */ }
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
						{ return new Symbol(TokenConstants.ERROR, "EOF in comment"); }
					case -75:
						break;
					case 75:
						{ /* comer */ }
					case -76:
						break;
					case 76:
						{ yybegin(YYINITIAL); }
					case -77:
						break;
					case 77:
						{ curr_lineno++; yybegin(YYINITIAL); }
					case -78:
						break;
					case 78:
						{ yybegin(YYINITIAL); }
					case -79:
						break;
					case 80:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -80:
						break;
					case 81:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -81:
						break;
					case 82:
						{   
                                                   return new Symbol(TokenConstants.IN);
                                               }
					case -82:
						break;
					case 83:
						{
                                                   return new Symbol(TokenConstants.IF);
                                               }
					case -83:
						break;
					case 84:
						{
                                                   return new Symbol(TokenConstants.OF);
                                               }
					case -84:
						break;
					case 85:
						{
                                                   return new Symbol(TokenConstants.FI);
                                               }
					case -85:
						break;
					case 86:
						{
                                                   return new Symbol(TokenConstants.NEW);
                                               }
					case -86:
						break;
					case 87:
						{
                                                   return new Symbol(TokenConstants.NOT);
                                               }
					case -87:
						break;
					case 88:
						{
                                                   return new Symbol(TokenConstants.LET);
                                               }
					case -88:
						break;
					case 89:
						{
                                                    return new Symbol(TokenConstants.ESAC);
                                               }
					case -89:
						break;
					case 90:
						{
                                                   return new Symbol(TokenConstants.ELSE);
                                               }
					case -90:
						break;
					case 91:
						{
                                                    return new Symbol(TokenConstants.THEN);
                                               }
					case -91:
						break;
					case 92:
						{
                                                   return new Symbol(TokenConstants.POOL);
                                               }
					case -92:
						break;
					case 93:
						{
                                                   return new Symbol(TokenConstants.LOOP);
                                               }
					case -93:
						break;
					case 94:
						{
                                                   return new Symbol(TokenConstants.CASE);
                                               }
					case -94:
						break;
					case 95:
						{
                                                   return new Symbol(TokenConstants.CLASS);
                                               }
					case -95:
						break;
					case 96:
						{
                                                   return new Symbol(TokenConstants.WHILE);
                                               }
					case -96:
						break;
					case 97:
						{
                                                    return new Symbol(TokenConstants.ISVOID);
                                               }
					case -97:
						break;
					case 98:
						{
                                                   return new Symbol(TokenConstants.INHERITS);
                                               }
					case -98:
						break;
					case 99:
						{
                            string_buf.append(yytext());
                            if (string_buf.length() > MAX_STR_CONST) {
                                muyLargo = true;
                            }
                        }
					case -99:
						break;
					case 100:
						{
                        }
					case -100:
						break;
					case 101:
						{  }
					case -101:
						break;
					case 102:
						{ /* comer */ }
					case -102:
						break;
					case 104:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -103:
						break;
					case 105:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -104:
						break;
					case 106:
						{  }
					case -105:
						break;
					case 108:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -106:
						break;
					case 109:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -107:
						break;
					case 110:
						{  }
					case -108:
						break;
					case 112:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -109:
						break;
					case 113:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -110:
						break;
					case 115:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -111:
						break;
					case 116:
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
					case 121:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -115:
						break;
					case 122:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -116:
						break;
					case 124:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -117:
						break;
					case 125:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -118:
						break;
					case 127:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -119:
						break;
					case 128:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -120:
						break;
					case 130:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -121:
						break;
					case 131:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -122:
						break;
					case 133:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -123:
						break;
					case 134:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -124:
						break;
					case 136:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -125:
						break;
					case 137:
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
					case 142:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -129:
						break;
					case 143:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -130:
						break;
					case 145:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -131:
						break;
					case 146:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -132:
						break;
					case 148:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -133:
						break;
					case 149:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -134:
						break;
					case 151:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -135:
						break;
					case 153:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -136:
						break;
					case 155:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -137:
						break;
					case 157:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -138:
						break;
					case 159:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -139:
						break;
					case 160:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -140:
						break;
					case 161:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -141:
						break;
					case 162:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -142:
						break;
					case 163:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -143:
						break;
					case 164:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -144:
						break;
					case 165:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -145:
						break;
					case 166:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -146:
						break;
					case 167:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -147:
						break;
					case 168:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -148:
						break;
					case 169:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -149:
						break;
					case 170:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -150:
						break;
					case 171:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -151:
						break;
					case 172:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -152:
						break;
					case 173:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -153:
						break;
					case 174:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -154:
						break;
					case 175:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -155:
						break;
					case 176:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -156:
						break;
					case 177:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -157:
						break;
					case 178:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -158:
						break;
					case 179:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -159:
						break;
					case 180:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -160:
						break;
					case 181:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -161:
						break;
					case 182:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -162:
						break;
					case 183:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -163:
						break;
					case 184:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -164:
						break;
					case 185:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -165:
						break;
					case 186:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -166:
						break;
					case 187:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -167:
						break;
					case 188:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -168:
						break;
					case 189:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -169:
						break;
					case 190:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -170:
						break;
					case 191:
						{
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -171:
						break;
					case 192:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -172:
						break;
					case 193:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -173:
						break;
					case 194:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -174:
						break;
					case 195:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -175:
						break;
					case 196:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -176:
						break;
					case 197:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -177:
						break;
					case 198:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -178:
						break;
					case 199:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -179:
						break;
					case 200:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -180:
						break;
					case 201:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -181:
						break;
					case 202:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -182:
						break;
					case 203:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -183:
						break;
					case 204:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -184:
						break;
					case 205:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -185:
						break;
					case 206:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -186:
						break;
					case 207:
						{
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }
					case -187:
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
