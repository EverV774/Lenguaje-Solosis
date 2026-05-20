%%

%cup
%class Lexer
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

LETRA   = [a-zA-Z_]
DIGITO  = [0-9]
ID      = {LETRA}({LETRA}|{DIGITO})*
ENTERO  = {DIGITO}+
DECIMAL = {DIGITO}+ "." {DIGITO}+
STRING  = \"([^\"\\]|\\.)*\"

/* ====== REGLAS ====== */

%%

/* --- Comentarios (línea completa ignorada desde '#') --- */
"#".*                 { /* ignorar comentario */ }

/* --- Palabras reservadas (deben ir antes que ID) --- */
"gabite"              { return symbol(sym.GABITE); }
"Falink"              { return symbol(sym.FALINK); }
"falink"              { return symbol(sym.FALINK); }
"espeon"              { return symbol(sym.ESPEON); }

/* --- Operadores aritméticos --- */
"+"                   { return symbol(sym.SUMA);   }
"-"                   { return symbol(sym.RESTA);  }
"*"                   { return symbol(sym.MULT);   }
"/"                   { return symbol(sym.DIV);    }

/* --- Asignación --- */
"?"                   { return symbol(sym.ASIGNACION); }

/* --- Punto y coma (signo de cierre obligatorio) --- */
";"                   { return symbol(sym.PUNTO_COMA); }

/* --- Literales --- */
{DECIMAL}             { return symbol(sym.DECIMAL, Double.parseDouble(yytext())); }
{ENTERO}              { return symbol(sym.ENTERO,  Integer.parseInt(yytext()));   }
{STRING}              { return symbol(sym.STRING,  yytext());                     }

/* --- Identificadores --- */
{ID}                  { return symbol(sym.ID, yytext()); }

/* --- Espacios en blanco (ignorar) --- */
[ \t\r\n]+            { /* ignorar */ }

/* --- Símbolo no reconocido: lanzar error léxico personalizado --- */
.                     {
                          throw new RuntimeException(
                              "[Error Léxico] Símbolo no reconocido: '" + yytext() +
                              "'  (línea " + (yyline+1) + ", col " + (yycolumn+1) + ")"
                          );
                      }
