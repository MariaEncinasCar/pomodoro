/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import objetos.Tarea;
import negocio.CtrlTarea;
import negocio.FabricaNegocios;

/**
 *
 * @author Arguello, Encinas, García
 */
public class menuPomodoro extends javax.swing.JFrame {

    /**
     * Creates new form menuPomodoro
     */
    private FabricaNegocios f = new FabricaNegocios();
    private CtrlTarea ctrlTarea = f.getCtrlTarea();
    private ArrayList<Tarea> listaTareas = (ArrayList<Tarea>) ctrlTarea.consultar();
        String estado = "Pendiente";
        private Timer t;
        private int h, m, s, cs;
       
            
    public menuPomodoro() {
        initComponents();
        t = new Timer(10, acciones);
        actualizaTabla(listaTareas);
        setLocationRelativeTo(this);
    }
    
    /**
     * Método que actualiza la tabla.
     */
    public void actualizaTabla(ArrayList<Tarea> lista) {
        DefaultTableModel modelo = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int row, int colum) {
                return false;
            }
        };

        modelo.addColumn("Tarea");

        tablaConsulta.setModel(modelo);

        String[] datos = new String[1];
        if (lista != null) {
            for (Tarea a : lista) {
                if(dropBusqueda.getSelectedItem().toString().equalsIgnoreCase("Todo")){
                    datos[0] = String.valueOf(a.getNombre_desc());
                }else{
                    if(dropBusqueda.getSelectedItem().toString().equalsIgnoreCase(a.getEstado())){
                        datos[0] = String.valueOf(a.getNombre_desc());
                    }
                }
                
                modelo.addRow(datos);
            }
        }

        Font fuente = new Font("Arial Rounded MT", Font.BOLD, 14);
        tablaConsulta.setFont(fuente);
        tablaConsulta.setModel(modelo);
    }

    //Falta hacer que no se puedan repetir las mismas tareas o.o
    private boolean verificarDato() {
        String tarea = txtActividad.getText();
        if (!tarea.isEmpty() && estado.equalsIgnoreCase("Pendiente")) {
            return true;
        }
        return false;
    }

    private void guardar() {
        String tarea = txtActividad.getText();
        //ComprobarRepetida();
        if (verificarDato()) {
            Tarea actividad = new Tarea(tarea, estado);
            ctrlTarea.guardar(actividad);
            JOptionPane.showMessageDialog(null, "La tarea se ha registrado exitosamente");
            txtActividad.setText("");
        } else {
            JOptionPane.showMessageDialog(null, "Asigne un nombre a su tarea");
        }
    }
    
    private ActionListener acciones = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
            ++cs;
            if (cs == 100) {
                cs = 0;
                ++s;
            }
            if (s == 60) {
                s = 0;
                ++m;
            }
            if (m == 60) {
                m = 0;
                ++h;
            }
            actualizarLabel();
            limiteTimer();
        }

    };

    private void actualizarLabel() {
        String tiempo = (h <= 9 ? "0" : "") + h + ":" + (m <= 9 ? "0" : "") + m + ":" + (s <= 9 ? "0" : "") + s + ":" + (cs <= 9 ? "0" : "") + cs;
        etiquetaTiempo.setText(tiempo);
    }

    private void limiteTimer() {
        if (etiquetaTiempo.getText().equalsIgnoreCase("00:00:05:00")) {
            int res = JOptionPane.showOptionDialog(new JFrame(), "Tu tarea ha finalizado, ¿deseas marcarla como finalizada?", "Notificación de tarea",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                    new Object[]{"Sí", "No"}, JOptionPane.YES_OPTION);
            if (res == JOptionPane.YES_OPTION) {
                System.out.println("Cambiando estado a finalizado");
                    if (t.isRunning()) {
                        t.stop();
                    }
                    btnPausa.setEnabled(true);
                    btnReiniciar.setEnabled(true);
                    btnIniciar.setEnabled(true);
                    h = 0;
                    m = 0;
                    s = 0;
                    cs = 0;
                    actualizarLabel();
            } else if (res == JOptionPane.NO_OPTION) {
                System.out.println("Dejando estado en pendientes");
            } else if (res == JOptionPane.CLOSED_OPTION) {
                System.out.println("pq cerró y no contestó la pregunta q grosero e");
            }
        }
    }
    
    /**
     * Método que regresa la tarea seleccionada en la tabla.
     *
     * @return devuelve la tarea seleccionado
     */
    public Tarea seleccionado() {
        int seleccion = tablaConsulta.getSelectedRow();
        Tarea tarea = new Tarea();
        tarea.setId(listaTareas.get(seleccion).getId());
        tarea.setNombre_desc(listaTareas.get(seleccion).getNombre_desc());
        tarea.setEstado(listaTareas.get(seleccion).getEstado());
        return tarea;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        txtActividad = new javax.swing.JTextField();
        btnTerminar = new javax.swing.JButton();
        btnReiniciar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnPausa = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaConsulta = new javax.swing.JTable();
        noPomodoros = new javax.swing.JLabel();
        dropBusqueda = new javax.swing.JComboBox<>();
        btnIniciar = new javax.swing.JButton();
        etiquetaTiempo = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 52)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 51, 51));
        jLabel3.setText("Pomodoro");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 40, 280, -1));

        txtActividad.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        txtActividad.setBorder(null);
        txtActividad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtActividadKeyTyped(evt);
            }
        });
        getContentPane().add(txtActividad, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 560, 300, 50));

        btnTerminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/btnTerminar.png"))); // NOI18N
        btnTerminar.setContentAreaFilled(false);
        btnTerminar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnTerminar.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/btnTerminar2.png"))); // NOI18N
        btnTerminar.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/btnTerminar2.png"))); // NOI18N
        getContentPane().add(btnTerminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 540, -1, -1));

        btnReiniciar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/btnRestablecer.png"))); // NOI18N
        btnReiniciar.setContentAreaFilled(false);
        btnReiniciar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReiniciar.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/btnRestablecer2.png"))); // NOI18N
        btnReiniciar.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/btnRestablecer2.png"))); // NOI18N
        btnReiniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReiniciarActionPerformed(evt);
            }
        });
        getContentPane().add(btnReiniciar, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 540, -1, -1));

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/btnRegistrar.png"))); // NOI18N
        btnGuardar.setContentAreaFilled(false);
        btnGuardar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGuardar.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/btnRegistrar2.png"))); // NOI18N
        btnGuardar.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/btnRegistrar2.png"))); // NOI18N
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        getContentPane().add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 520, 220, -1));

        btnPausa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/btnPausa.png"))); // NOI18N
        btnPausa.setContentAreaFilled(false);
        btnPausa.setEnabled(false);
        btnPausa.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPausa.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/btnPausa2.png"))); // NOI18N
        btnPausa.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/btnPausa2.png"))); // NOI18N
        btnPausa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPausaActionPerformed(evt);
            }
        });
        getContentPane().add(btnPausa, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 540, -1, -1));

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel1.setText("Número de pomodoros:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, 290, -1));

        tablaConsulta.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        tablaConsulta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "Tareas"
            }
        ));
        tablaConsulta.setRequestFocusEnabled(false);
        tablaConsulta.getTableHeader().setReorderingAllowed(false);
        tablaConsulta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaConsultaMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tablaConsulta);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 130, 550, 370));

        noPomodoros.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        noPomodoros.setText("0");
        getContentPane().add(noPomodoros, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 20, 50, 30));

        dropBusqueda.setBackground(new java.awt.Color(255, 51, 51));
        dropBusqueda.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        dropBusqueda.setForeground(new java.awt.Color(255, 255, 255));
        dropBusqueda.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todo", "Pendiente", "En progreso", "Terminada" }));
        dropBusqueda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dropBusquedaActionPerformed(evt);
            }
        });
        getContentPane().add(dropBusqueda, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 60, 220, 30));

        btnIniciar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/btnIniciar.png"))); // NOI18N
        btnIniciar.setContentAreaFilled(false);
        btnIniciar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIniciar.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/btnIniciar2.png"))); // NOI18N
        btnIniciar.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/btnIniciar2.png"))); // NOI18N
        btnIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniciarActionPerformed(evt);
            }
        });
        getContentPane().add(btnIniciar, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 540, -1, -1));

        etiquetaTiempo.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 44)); // NOI18N
        etiquetaTiempo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        etiquetaTiempo.setText("00:00:00:00");
        getContentPane().add(etiquetaTiempo, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 290, 280, 70));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/FondoPomodoro.png"))); // NOI18N
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 650));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnReiniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReiniciarActionPerformed
        if (t.isRunning()) {
            t.stop();
            btnIniciar.setEnabled(true);
        }
        btnPausa.setEnabled(false);
        btnReiniciar.setEnabled(false);
        h = 0;
        m = 0;
        s = 0;
        cs = 0;
        actualizarLabel();
    }//GEN-LAST:event_btnReiniciarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        guardar();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnPausaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPausaActionPerformed
        t.stop();
        btnIniciar.setEnabled(true);
        btnPausa.setEnabled(false);
    }//GEN-LAST:event_btnPausaActionPerformed

    private void btnIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIniciarActionPerformed
        t.start();
        btnIniciar.setEnabled(false);
        btnPausa.setEnabled(true);
        btnReiniciar.setEnabled(true);
    }//GEN-LAST:event_btnIniciarActionPerformed

    private void tablaConsultaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaConsultaMouseClicked
        Tarea cl = seleccionado();
        actualizarTarea c = new actualizarTarea(cl);
        c.setVisible(true);
    }//GEN-LAST:event_tablaConsultaMouseClicked

    private void txtActividadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtActividadKeyTyped
        if (txtActividad.getText().length() == 100) {
            getToolkit().beep();
            evt.consume();
            JOptionPane.showMessageDialog(this, "Sólo se permiten 100 caracteres");
        }
    }//GEN-LAST:event_txtActividadKeyTyped

    private void dropBusquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dropBusquedaActionPerformed
        String buscarEstado = dropBusqueda.getSelectedItem().toString();
        if (!buscarEstado.equalsIgnoreCase("todo")){
            ArrayList<Tarea> lista = (ArrayList<Tarea>) ctrlTarea.buscarEstado(buscarEstado);
            actualizaTabla(lista);
        }
        else{
            ArrayList<Tarea> lista = (ArrayList<Tarea>) ctrlTarea.consultar();
            actualizaTabla(lista);
        }
    }//GEN-LAST:event_dropBusquedaActionPerformed

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
            java.util.logging.Logger.getLogger(menuPomodoro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(menuPomodoro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(menuPomodoro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(menuPomodoro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new menuPomodoro().setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnIniciar;
    private javax.swing.JButton btnPausa;
    private javax.swing.JButton btnReiniciar;
    private javax.swing.JButton btnTerminar;
    private javax.swing.JComboBox<String> dropBusqueda;
    private javax.swing.JLabel etiquetaTiempo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel noPomodoros;
    private javax.swing.JTable tablaConsulta;
    private javax.swing.JTextField txtActividad;
    // End of variables declaration//GEN-END:variables
}
