%%

%cup
%class SolosisLexer
%unicode
%line
%column
%type String

%{

// Puedes agregar código Java aquí si lo necesitas

%}

import java_cup.runtime.Sysbol;

private Sysbol

/* ====== DEFINICIONES ====== */

LETRA = [a-zA-Z]
DIGITO = [0-9]
ID = {LETRA}({LETRA}|{DIGITO})*
ENTERO = {DIGITO}+{10}
DECIMAL = {DIGITO}+{10} "." {DIGITO}+{8}
STRING = \"([^\"\\]|\\.)*\"

/* ====== REGLAS ====== */

%%

/* --- Comentarios --- */
"#".*                 { return "COMENTARIO: " + yytext(); }

/* --- Tipos de datos --- */
"gabite"              { return "TIPO_INT"; }
"Falink"              { return "TIPO_STRING"; }
"espeon"              { return "TIPO_DOUBLE"; }

/* --- Operadores --- */
"+"                   { return "SUMA"; }
"-"                   { return "RESTA"; }
"*"                   { return "MULT"; }
"/"                   { return "DIV"; }

/* --- Asignación --- */
"?"                   { return "ASIGNACION"; }

/* --- Literales --- */
{DECIMAL}             { return "DECIMAL: " + yytext(); }
{ENTERO}              { return "ENTERO: " + yytext(); }
{STRING}              { return "STRING: " + yytext(); }

/* --- Identificadores --- */
{ID}                  { return "ID: " + yytext(); }

/* --- Espacios --- */
[ \t\r\n]+            { /* ignorar */ }

/* --- Error --- */
.                     { return "ERROR: " + yytext(); }