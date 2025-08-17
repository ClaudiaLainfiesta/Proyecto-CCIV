/*
 *  The scanner definition for COOL.
 */

import java_cup.runtime.Symbol;

%%

%{

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
%}

%init{

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */

    // empty for now
%init}

%eofval{

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
%eofval}

%class CoolLexer
%cup
%state STRING
%state COMMENT
%state COMMENTS
mayusculas = [A-Z]
minusculas = [a-z]
digito = [0-9]
letra = [A-Za-z]
alphanumerico = [A-Za-z0-9]

%%

<YYINITIAL>"=>"			{ /* Sample lexical rule for "=>" arrow.
                                     Further lexical rules should be defined
                                     here, after the last %% separator */
                                  return new Symbol(TokenConstants.DARROW); }



<YYINITIAL> [Ii][Nn][Hh][Ee][Rr][Ii][Tt][Ss]   {
                                                   return new Symbol(TokenConstants.INHERITS);
                                               }

<YYINITIAL>[Pp][Oo][Oo][Ll]                    {
                                                   return new Symbol(TokenConstants.POOL);
                                               }

<YYINITIAL>[Cc][Aa][Ss][Ee]                    {
                                                   return new Symbol(TokenConstants.CASE);
                                               }

<YYINITIAL>[Ii][Nn]                            {   
                                                   return new Symbol(TokenConstants.IN);
                                               }

<YYINITIAL>[Cc][Ll][Aa][Ss][Ss]                {
                                                   return new Symbol(TokenConstants.CLASS);
                                               }

<YYINITIAL>[Ff][Ii]                            {
                                                   return new Symbol(TokenConstants.FI);
                                               }
                
<YYINITIAL>[Ll][Oo][Oo][Pp]                    {
                                                   return new Symbol(TokenConstants.LOOP);
                                               }

<YYINITIAL>[Ii][Ff]                            {
                                                   return new Symbol(TokenConstants.IF);
                                               }

<YYINITIAL>[Ee][Ll][Ss][Ee]                    {
                                                   return new Symbol(TokenConstants.ELSE);
                                               }

<YYINITIAL>[Ww][Hh][Ii][Ll][Ee]                {
                                                   return new Symbol(TokenConstants.WHILE);
                                               }

<YYINITIAL>[Nn][Ee][Ww]                        {
                                                   return new Symbol(TokenConstants.NEW);
                                               }                                               

<YYINITIAL>[Ii][Ss][Vv][Oo][Ii][Dd]            {
                                                    return new Symbol(TokenConstants.ISVOID);
                                               } 

<YYINITIAL>[Ee][Ss][Aa][Cc]                    {
                                                    return new Symbol(TokenConstants.ESAC);
                                               } 

<YYINITIAL>[Tt][Hh][Ee][Nn]                    {
                                                    return new Symbol(TokenConstants.THEN);
                                               } 

<YYINITIAL>[Oo][Ff]                            {
                                                   return new Symbol(TokenConstants.OF);
                                               }

<YYINITIAL>t[rR][uU][eE]                       {
                                                   return new Symbol(TokenConstants.BOOL_CONST, true);
                                               }   

<YYINITIAL>f[aA][lL][sS][eE]                   {
                                                   return new Symbol(TokenConstants.BOOL_CONST, false);
                                               }

<YYINITIAL>[Nn][Oo][Tt]                        {
                                                   return new Symbol(TokenConstants.NOT);
                                               }                                                

<YYINITIAL>[Ll][Ee][Tt]                        {
                                                   return new Symbol(TokenConstants.LET);
                                               }                                                
                                              
                                               


<YYINITIAL>{mayusculas}[A-Za-z0-9_]*           {
                                                   return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext()));
                                               }     

<YYINITIAL>{minusculas}[A-Za-z0-9_]*           {
                                                   return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext()));
                                               }  

<YYINITIAL>{digito}+                           {
                                                   return new Symbol(TokenConstants.INT_CONST, AbstractTable.inttable.addString(yytext()));
                                               }  




                        
<YYINITIAL>[\"]         {   
                            string_buf.delete(0,string_buf.length());
                            muyLargo = false;
                            yybegin(STRING);
                        }


<STRING>"\0"            {
                            return new Symbol(TokenConstants.ERROR, "String contains null character");
                        }
    
<STRING>\\0           {
                            string_buf.append("\\0");
                        }

<STRING>"\\\""          { 
                            string_buf.append('\"'); 
                        }

<STRING>\\n             { 
                            string_buf.append("\n"); 
                        }
<STRING>\\t             { 
                            string_buf.append("\t"); 
                        }
<STRING>\\r             { 
                            string_buf.append("\r"); 
                        }
<STRING>\\\\            { 
                            string_buf.append("\\"); 
                        }



<STRING>[^\"]           {
                            string_buf.append(yytext());

                            if (string_buf.length() > MAX_STR_CONST) {
                                muyLargo = true;
                            }
                        }


    
<STRING>[\"]            {
                            if(muyLargo == true){
                                yybegin(YYINITIAL);
                                return new Symbol(TokenConstants.ERROR, "String constant too long");
                            }
                            yybegin(YYINITIAL);
                            return new Symbol(TokenConstants.STR_CONST, AbstractTable.stringtable.addString(string_buf.toString()));
                        }




<YYINITIAL>"(*"         {   
                            cantComents++;
                            yybegin(COMMENTS);
                        }

<COMMENTS>"(*"          {
                            cantComents++;
                        }


<COMMENTS>"*)"          {   
                            cantComents--;
                            if (cantComents == 0) {
                                yybegin(YYINITIAL);
                            }
                            else if (cantComents < 0) {
                                return new Symbol(TokenConstants.ERROR, "Unmatched *)");
                            }
                        }

<COMMENTS>[ \t\r\f]+    {
        
                        }

<COMMENTS>[\n]          {
                            curr_lineno++;
                        }
    
<COMMENTS>.   {  }

<COMMENTS><<EOF>>  { return new Symbol(TokenConstants.ERROR, "EOF in comment"); }

<YYINITIAL>"*)"         {
                            return new Symbol(TokenConstants.ERROR, "Unmatched *)");
                        }



<YYINITIAL>"--"         {
                            yybegin(COMMENT);
                        }
        
<COMMENT>[^\n]          {

                        }

<COMMENT>\n             {
                            curr_lineno++;
                            yybegin(YYINITIAL);
                        }
                        
<COMMENT><<EOF>>        { }



<YYINITIAL>" "          {
        
                        }

<YYINITIAL>[\t\r\f]+    {
        
                        }

<YYINITIAL>[\n]         {
                            curr_lineno++;
                        }
                        
<YYINITIAL>"+"          {
                            return new Symbol(TokenConstants.PLUS);
                        }

<YYINITIAL>"-"          {
                            return new Symbol(TokenConstants.MINUS);
                        }

<YYINITIAL>"*"          {
                            return new Symbol(TokenConstants.MULT);
                        }

<YYINITIAL>"/"          {
                            return new Symbol(TokenConstants.DIV);
                        }

<YYINITIAL>"~"          {
                            return new Symbol(TokenConstants.NEG);
                        }

<YYINITIAL>"<="         {
                            return new Symbol(TokenConstants.LE);
                        }

<YYINITIAL>"@"         {
                            return new Symbol(TokenConstants.AT);
                        }

<YYINITIAL>"<"          {
                            return new Symbol(TokenConstants.LT);
                        }

<YYINITIAL>"="          {
                            return new Symbol(TokenConstants.EQ);
                        }

<YYINITIAL>"<-"         {
                            return new Symbol(TokenConstants.ASSIGN);
                        }

<YYINITIAL>"("          {
                            return new Symbol(TokenConstants.LPAREN);
                        }

<YYINITIAL>")"          {
                            return new Symbol(TokenConstants.RPAREN);
                        }

<YYINITIAL>"{"          {
                            return new Symbol(TokenConstants.LBRACE);
                        }

<YYINITIAL>"}"          {
                            return new Symbol(TokenConstants.RBRACE);
                        }

<YYINITIAL>";"          {
                            return new Symbol(TokenConstants.SEMI);
                        }

<YYINITIAL>","          {
                            return new Symbol(TokenConstants.COMMA);
                        }

<YYINITIAL>":"          {
                            return new Symbol(TokenConstants.COLON);
                        }

<YYINITIAL>"."          {
                            return new Symbol(TokenConstants.DOT);
                        }

. { return new Symbol(TokenConstants.ERROR, yytext()); }


