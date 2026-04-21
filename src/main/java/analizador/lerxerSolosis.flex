%%

%cup
%class SolosisLexer
%unicode
%line
%column

%{
import java_cup.runtime.Symbol;

private Symbol symbol(int type) {
    return new Symbol(type, yyline, yycolumn);
}

private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline, yycolumn, value);
}
%}

/* ====== DEFINICIONES ====== */

LETRA = [a-zA-Z]
DIGITO = [0-9]
ID = {LETRA}({LETRA}|{DIGITO})*
ENTERO = {DIGITO}+
DECIMAL = {DIGITO}+ "." {DIGITO}+
STRING = \"([^\"\\]|\\.)*\"

/* ====== REGLAS ====== */

%%

/* --- Comentarios --- */
"#".*                 { /* ignorar */ }

/* --- Tipos de datos --- */
"gabite"              { return symbol(sym.GABITE); }
"Falink"              { return symbol(sym.FALINK); }
"espeon"              { return symbol(sym.ESPEON); }

/* --- Operadores --- */
"+"                   { return symbol(sym.SUMA); }
"-"                   { return symbol(sym.RESTA); }
"*"                   { return symbol(sym.MULT); }
"/"                   { return symbol(sym.DIV); }

/* --- Asignación --- */
"?"                   { return symbol(sym.ASIGNACION); }

/* --- Literales --- */
{DECIMAL}             { return symbol(sym.DECIMAL, Double.parseDouble(yytext())); }
{ENTERO}              { return symbol(sym.ENTERO, Integer.parseInt(yytext())); }
{STRING}              { return symbol(sym.STRING, yytext()); }

/* --- Identificadores --- */
{ID}                  { return symbol(sym.ID, yytext()); }

/* --- Espacios --- */
[ \t\r\n]+            { /* ignorar */ }

/* --- Error --- */
.                     { System.out.println("Error léxico: " + yytext()); }
