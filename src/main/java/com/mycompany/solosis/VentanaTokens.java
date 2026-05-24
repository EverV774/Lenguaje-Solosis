/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.mycompany.solosis;

/**
 *
 * @author Heber
 */
public class VentanaTokens extends javax.swing.JDialog {
 
    public VentanaTokens(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setTitle("Análisis Léxico – Tokens Solosis");
    }
 
    /** Convierte el nombre interno del patrón al patrón regex legible. */
    private static String resolverPatronLegible(String nombrePatron) {

    switch (nombrePatron) {

        // =========================================
        // PALABRAS RESERVADAS
        // =========================================
        case "PALABRA_RESERVADA_GABITE":
            return "gabite";

        case "PALABRA_RESERVADA_ESPEON":
            return "espeon";

        case "PALABRA_RESERVADA_FALINK":
            return "falink";

        case "PALABRA_RESERVADA_MEOWL":
            return "meowl";

        case "PALABRA_RESERVADA_SPINDA":
            return "spinda";

        case "PALABRA_RESERVADA_LIZARD":
            return "LIZARD";

        case "PALABRA_RESERVADA_PURPLE_LIZARD":
            return "purple_lizard";

        // =========================================
        // LITERALES
        // =========================================
        case "LITERAL_ENTERO":
            return "[0-9]{1,10}";

        case "LITERAL_DECIMAL":
            return "[0-9]{1,10}\\.[0-9]+";

        case "LITERAL_STRING":
            return "\"[^\"]*\"";

        case "IDENTIFICADOR":
            return "[a-zA-Z_][a-zA-Z0-9_]*";

        // =========================================
// SÍMBOLOS
// =========================================
case "ASIGNACION":
case "?":
    return "\\?";

case "OPERADOR_SUMA":
case "+":
    return "\\+";

case "OPERADOR_RESTA":
case "-":
    return "\\-";

case "OPERADOR_MULT":
case "*":
    return "\\*";

case "OPERADOR_DIV":
case "/":
    return "\\/";

case "PARENTESIS_IZQ":
case "(":
    return "\\(";

case "PARENTESIS_DER":
case ")":
    return "\\)";

case "LLAVE_IZQ":
case "{":
    return "\\{";

case "LLAVE_DER":
case "}":
    return "\\}";

case "PUNTO_COMA":
case ";":
    return "\\;";

case "MAYOR":
case ">":
    return "\\>";

case "MENOR":
case "<":
    return "\\<";

case "MAYOR_IGUAL":
case ">=":
    return "\\>\\=";

case "MENOR_IGUAL":
case "<=":
    return "\\<\\=";

case "IGUAL_IGUAL":
case "==":
    return "\\=\\=";

case "DIFERENTE":
case "!=":
    return "\\!\\=";
        default:
            return nombrePatron;
    }
}
 
    /** Llena la tabla con los datos de los tokens incluyendo lexema y patrón. */
public void llenarTabla(java.util.List<Object[]> datos) {

    javax.swing.table.DefaultTableModel modelo =
            (javax.swing.table.DefaultTableModel) tblTokens.getModel();

    modelo.setRowCount(0);

    java.util.Set<String> reservadas =
            new java.util.HashSet<>(java.util.Arrays.asList(
                    "gabite",
                    "espeon",
                    "falink",
                    "meowl",
                    "spinda",
                    "lizard",
                    "purple_lizard"
            ));

    int contador = 1;

    for (Object[] fila : datos) {

        // =========================================
        // TOKEN
        // =========================================
        String tipo =
                fila[0] != null
                        ? fila[0].toString()
                        : "";

        // =========================================
        // LEXEMA
        // =========================================
        String lexema =
                fila[1] != null
                        ? fila[1].toString()
                        : "";

        // =========================================
        // PATRÓN
        // =========================================
        String patron =
                fila[2] != null
                        ? fila[2].toString()
                        : "";

        // =========================================
        // LÍNEA Y COLUMNA
        // =========================================
        Object linea   = fila[3];
        Object columna = fila[4];

        // =========================================
        // PATRÓN LEGIBLE
        // =========================================
        String patronLegible =
                resolverPatronLegible(patron);

        // =========================================
        // PALABRA RESERVADA
        // =========================================
        String reservada =
                reservadas.contains(
                        lexema.toLowerCase()
                )
                ? "Sí"
                : "No";

        // =========================================
        // AGREGAR FILA
        // =========================================
        modelo.addRow(new Object[]{

            contador,
            lexema,
            lexema,
            patronLegible,
            linea,
            columna,
            reservada
        });

        contador++;
    }
}

 
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ScrollPane = new javax.swing.JScrollPane();
        tblTokens = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tblTokens.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "No. Lista", "Token", "Lexema", "Patrón", "Linea", "Columna", "Palabra reservada"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ScrollPane.setViewportView(tblTokens);
        if (tblTokens.getColumnModel().getColumnCount() > 0) {
            tblTokens.getColumnModel().getColumn(0).setResizable(false);
            tblTokens.getColumnModel().getColumn(1).setResizable(false);
            tblTokens.getColumnModel().getColumn(2).setResizable(false);
            tblTokens.getColumnModel().getColumn(3).setResizable(false);
        }

        jButton1.setText("Regresar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 980, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addGap(445, 445, 445))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(ScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 524, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addGap(0, 15, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaTokens.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaTokens.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaTokens.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaTokens.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                VentanaTokens dialog = new VentanaTokens(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane ScrollPane;
    private javax.swing.JButton jButton1;
    private javax.swing.JTable tblTokens;
    // End of variables declaration//GEN-END:variables
}
