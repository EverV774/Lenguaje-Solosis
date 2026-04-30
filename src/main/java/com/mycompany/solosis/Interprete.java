/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.solosis;

    import com.mycompany.solosis.AnalizadorLexico.TipoToken;
    import com.mycompany.solosis.AnalizadorLexico.Token;
    import java.util.HashMap;
    import java.util.List;
    /**
     *
     * @author Heber
     */
    public class Interprete {
        private HashMap<String, Object> tablaSimbolos = new HashMap<>();
        private StringBuilder logEjecucion = new StringBuilder();

        public void ejecutar(List<Token> tokens) {
            logEjecucion.setLength(0); 
            logEjecucion.append(">>> INICIANDO INTERPRETACIÓN <<<\n");

            for (int i = 0; i < tokens.size(); i++) {
                Token tokenActual = tokens.get(i);

                // 1. Lógica para GABITE
                if (tokenActual.tipo == TipoToken.GABITE) {
                    try {
                        String nombreVar = tokens.get(i + 1).valor;
                        int valor = Integer.parseInt(tokens.get(i + 3).valor);

                        gabite nuevaVar = new gabite(valor); 
                        tablaSimbolos.put(nombreVar, nuevaVar); // Ahora ya existe tablaSimbolos

                        logEjecucion.append("[OK] Variable GABITE '").append(nombreVar)
                                    .append("' creada con valor: ").append(valor).append("\n");

                        i += 4; 
                    } catch (Exception e) {
                        logEjecucion.append("[ERROR] Fallo en declaración GABITE\n");
                    }
                }

                // 2. Lógica para ESPEON
                else if (tokenActual.tipo == TipoToken.ESPEON) {
                    try {
                        String nombreVar = tokens.get(i + 1).valor;
                        double valor = Double.parseDouble(tokens.get(i + 3).valor);

                        Espeon nuevaVar = new Espeon(valor);
                        tablaSimbolos.put(nombreVar, nuevaVar);

                        logEjecucion.append("[OK] Variable ESPEON '").append(nombreVar)
                                    .append("' creada con valor: ").append(valor).append("\n");

                        i += 4;
                    } catch (Exception e) {
                        logEjecucion.append("[ERROR] Fallo en declaración ESPEON\n");
                    }
                }

                // 3. Lógica para FALINK
                else if (tokenActual.tipo == TipoToken.FALINK) {
                    try {
                        String nombreVar = tokens.get(i + 1).valor;
                        String valor = tokens.get(i + 3).valor;

                        Falink nuevaVar = new Falink(valor);
                        tablaSimbolos.put(nombreVar, nuevaVar);

                        logEjecucion.append("[OK] Variable FALINK '").append(nombreVar)
                                    .append("' creada con valor: ").append(valor).append("\n");

                        i += 4;
                    } catch (Exception e) {
                        logEjecucion.append("[ERROR] Fallo en declaración FALINK\n");
                    }
                }
            }

            if (tablaSimbolos.isEmpty()) {
                logEjecucion.append("No se detectaron declaraciones válidas.\n");
            }
            logEjecucion.append(">>> FIN DE LA EJECUCIÓN <<<");
        }

        public String obtenerLogEjecucion() {
            return logEjecucion.toString();
        }
    }
