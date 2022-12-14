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
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
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
    private int m = 0, s = 20, cs = 69, noPomodoro = 4, noDescanso = 3;
    private boolean pomodoroActivo = true, descansoActivo = false, descansoLargoActivo = false;
    public ArrayList<Tarea> ordenPendientes = (ArrayList<Tarea>) ctrlTarea.buscarEstado("Pendiente");
    private Tarea objTarea;
    public String trabajo = "";
    private Clip clip; 

    public menuPomodoro() {
        initComponents();
        t = new Timer(10, acciones);
        btnModificar.setEnabled(false);
        actualizaTabla(listaTareas);
        setLocationRelativeTo(this);
    }
    public void playsound(){
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/alarma.wav"));
            clip = AudioSystem.getClip( );
            clip.open(audioInputStream);
            clip.start();    
            clip.loop(Clip.LOOP_CONTINUOUSLY);   
            }
            catch(Exception e)  {
                e.printStackTrace( );
            }
    }

     public void dummyMethod(){
        clip.stop();
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
        if (dropBusqueda.getSelectedItem().toString().equalsIgnoreCase("Todo")) {
            modelo.addColumn("Tarea");
            modelo.addColumn("Estado");
        } else if (dropBusqueda.getSelectedItem().toString().equalsIgnoreCase("Terminada")) {
            modelo.addColumn("Tarea");
            modelo.addColumn("Fecha Termino");
        }else {
            modelo.addColumn("Tarea");
        }

        tablaConsulta.setModel(modelo);

        String[] datos = new String[2];
        if (lista != null) {
            for (Tarea a : lista) {
                if(dropBusqueda.getSelectedItem().toString().equalsIgnoreCase("Todo")){
                    datos[0] = String.valueOf(a.getNombre_desc());
                    datos[1] = String.valueOf(a.getEstado());
                    btnArriba.setEnabled(false);
                    btnAbajo.setEnabled(false);
                } else {
                    if (dropBusqueda.getSelectedItem().toString().equalsIgnoreCase(a.getEstado())){
                        datos[0] = String.valueOf(a.getNombre_desc());
                        if(a.getEstado().equalsIgnoreCase("pendiente")){
                            btnArriba.setEnabled(true);
                            btnAbajo.setEnabled(true);
                        } else if (dropBusqueda.getSelectedItem().toString().equalsIgnoreCase("Terminada")) {
                            datos[0] = String.valueOf(a.getNombre_desc());
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
                            String fechaTexto = formatter.format(a.getFechaTermino());
                            datos[1] = fechaTexto;
                            btnArriba.setEnabled(false);
                            btnAbajo.setEnabled(false);
                        } else {
                            btnArriba.setEnabled(false);
                            btnAbajo.setEnabled(false);
                        }
                    }
                }
                
                modelo.addRow(datos);
            }
        }

        Font fuente = new Font("Arial Rounded MT", Font.BOLD, 14);
        tablaConsulta.setFont(fuente);
        tablaConsulta.setModel(modelo);
    }

    public boolean verificarDato() {
        String tarea = txtActividad.getText();
        if (!tarea.isEmpty() && estado.equalsIgnoreCase("Pendiente")) {
            return true;
        }
        return false;
    }
    
    public boolean verificarTarea(Tarea actividad){
        ArrayList<Tarea> lista = (ArrayList<Tarea>) ctrlTarea.consultar();
        if (lista == null) {
            return true;
        } else {
            for (Tarea a : lista) {
                if (a.getNombre_desc().equalsIgnoreCase(actividad.getNombre_desc())){
                    return false;
                }
            }
        }
        return true;
    }

    public void guardar() {
        String tarea = txtActividad.getText();
        if (verificarDato()) {
            Tarea actividad = new Tarea(tarea, estado);
            if (verificarTarea(actividad)) {
                ctrlTarea.guardar(actividad);
                JOptionPane.showMessageDialog(null, "La tarea se ha registrado exitosamente");
                txtActividad.setText("");
                ordenPendientes.add(actividad);
            }else{
                JOptionPane.showMessageDialog(null, "La tarea ya se encuentra registrada");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Asigne un nombre a su tarea");
        }
    }
    
    /*
     * Método para verificar la cantidad de pomodoros y descansos.
     * @return El número de pomodoros para el descanso más largo.
     */
    public String verificarDescanso() {
        boolean descanso = true;
        btnIniciar.setEnabled(false);
        if (noDescanso == 0) {
            noDescanso = 4;
            noPomodoro = 4;
            playsound();
            while (descanso) {
                int res = JOptionPane.showOptionDialog(new JFrame(), "¿Desea omitir el descanso largo?", "Iniciar descanso largo",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new Object[]{"Sí", "No"}, JOptionPane.YES_OPTION);
                if (res ==  JOptionPane.YES_OPTION) {
                    dummyMethod();
                    // yes option
                    int res2 = JOptionPane.showOptionDialog(new JFrame(), "¿Esta seguro de omitir el descanso largo?", "Confirmación" ,
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                    new Object[]{"Sí", "No"}, JOptionPane.YES_OPTION);
                    if (res2 == JOptionPane.YES_OPTION) {
                        break;
                    }
                }
                else {
                    dummyMethod();
                    JOptionPane.showMessageDialog(null, "Iniciar descanso largo");
                    pomodoroActivo = false;
                    descansoLargoActivo = true;
                    this.tiempoDescansoLargo();
                    return "4";
                }
            }
        }
        else if (noPomodoro > noDescanso) {
            noPomodoro--;
            getToolkit().beep();
            playsound();
            while (descanso) {
                int res = JOptionPane.showOptionDialog(new JFrame(), "¿Desea omitir el descanso?", "Iniciar descanso",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new Object[]{"Sí", "No"}, JOptionPane.YES_OPTION);
                if (res == JOptionPane.YES_OPTION) {
                    dummyMethod();
                    int res2 = JOptionPane.showOptionDialog(new JFrame(), "¿Esta seguro de omitir el descanso?", "Confirmación" ,
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                    new Object[]{"Sí", "No"}, JOptionPane.YES_OPTION);
                    if (res2 == JOptionPane.YES_OPTION) {
                        break;
                    }
                }
                else {
                    dummyMethod();
                    JOptionPane.showMessageDialog(null, "Iniciar descanso");
                    pomodoroActivo = false;
                    descansoActivo = true;
                    this.tiempoDescanso();
                    return Integer.toString(noPomodoro);
                }
            }
        }
        getToolkit().beep();
        playsound();
        int exit = JOptionPane.showConfirmDialog(null, "Iniciar pomodoro" , "Pomodoro", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if (exit == JOptionPane.OK_OPTION || exit == JOptionPane.CANCEL_OPTION)
        {
            dummyMethod();
        }
        pomodoroActivo = true;
        descansoActivo = false;
        descansoLargoActivo = false;
        noDescanso--;
        this.tiempoPomodoro();
        return Integer.toString(noPomodoro);
    }
    
    public void tiempoPomodoro() {
        m = 0;
        s = 20;
        cs = 0;
    }
    
    public void tiempoDescansoLargo() {
        m = 0;
        s = 10;
        cs = 0;
    }
    
    public void tiempoDescanso() {
        m = 0;
        s = 5;
        cs = 0;
    }
    
    public ActionListener acciones = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
            --cs;
            if (cs < 0) {
                cs = 69;
                --s;
            }
            if (s < 0) {
                s = 59;
                --m;
            }
            actualizarLabel();
            limiteTimer();
        }

    };
   
    public void actualizarLabel() {
        String tiempo = (m <= 9 ? "0" : "") + m + ":" + (s <= 9 ? "0" : "") + s + ":" + (cs <= 9 ? "0" : "") + cs;
        etiquetaTiempo.setText(tiempo);
    }

    public void limiteTimer() {
        if (etiquetaTiempo.getText().equalsIgnoreCase("00:05:00") && pomodoroActivo) {
            getToolkit().beep();
        //JOptionPane.showMessageDialog(null, "Su tarea está a punto de concluir");
            jTarea.setText("Su tarea está a punto de concluir");
                    if (etiquetaTiempo.getText().equalsIgnoreCase("00:00:00") && pomodoroActivo) {
                            jTarea.setText("");
                            btnPausa.setEnabled(true);
                            btnReiniciar.setEnabled(true);
                            btnIniciar.setEnabled(true);
                            noPomodoros.setText(this.verificarDescanso());
                            actualizarLabel();
                }
        }
        
        else if (etiquetaTiempo.getText().equalsIgnoreCase("00:00:00")) {
                    btnPausa.setEnabled(true);
                    btnReiniciar.setEnabled(true);
                    btnIniciar.setEnabled(true);
                    jTarea.setText("");
                    noPomodoros.setText(this.verificarDescanso());
                    actualizarLabel();
        }
    }
    
    /**
     * Método que regresa la tarea seleccionada en la tabla.
     *
     * @return devuelve la tarea seleccionado
     */
    public Tarea seleccionado() {
        Tarea tarea = new Tarea();
        int seleccion = tablaConsulta.getSelectedRow();
        if(seleccion == -1){
            return tarea;
        }
        String nombreS = String.valueOf(tablaConsulta.getValueAt(seleccion, NORMAL));
        
        ArrayList<Tarea> lista = (ArrayList<Tarea>) ctrlTarea.consultar();
        int i = 0;
        for(Tarea a: lista){
            if(a.getNombre_desc().equalsIgnoreCase(nombreS)){
                break;
            }
            i++;
        }
        tarea.setId(lista.get(i).getId());
        tarea.setNombre_desc(lista.get(i).getNombre_desc());
        tarea.setEstado(lista.get(i).getEstado());
        tarea.setFechaTermino(lista.get(i).getFechaTermino());
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
        jTarea = new javax.swing.JLabel();
        btnReiniciar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnPausa = new javax.swing.JButton();
        lblBombre = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaConsulta = new javax.swing.JTable();
        noPomodoros = new javax.swing.JLabel();
        btnModificar = new javax.swing.JButton();
        btnAbajo = new javax.swing.JButton();
        btnArriba = new javax.swing.JButton();
        dropBusqueda = new javax.swing.JComboBox<>();
        btnIniciar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        etiquetaTiempo = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pomodoro");
        setResizable(false);
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

        jTarea.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jTarea.setForeground(new java.awt.Color(255, 0, 0));
        jTarea.setText(" ");
        getContentPane().add(jTarea, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 300, -1));

        btnReiniciar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/btnRestablecer.png"))); // NOI18N
        btnReiniciar.setToolTipText("Reiniciar");
        btnReiniciar.setContentAreaFilled(false);
        btnReiniciar.setEnabled(false);
        btnReiniciar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnReiniciar.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/btnRestablecer2.png"))); // NOI18N
        btnReiniciar.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/btnRestablecer2.png"))); // NOI18N
        btnReiniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReiniciarActionPerformed(evt);
            }
        });
        getContentPane().add(btnReiniciar, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 540, -1, -1));

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
        btnPausa.setToolTipText("Pausa");
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
        getContentPane().add(btnPausa, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 540, -1, -1));

        lblBombre.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        lblBombre.setText("Trabajando en...");
        getContentPane().add(lblBombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 390, -1));

        jLabel5.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jLabel5.setText("Pomodoros para descanso largo:");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 300, -1));

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

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 130, 460, 370));

        noPomodoros.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        noPomodoros.setText("4");
        getContentPane().add(noPomodoros, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 40, 40, 20));

        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/btnModificar.png"))); // NOI18N
        btnModificar.setContentAreaFilled(false);
        btnModificar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnModificar.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/btnModificar2.png"))); // NOI18N
        btnModificar.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/btnModificar2.png"))); // NOI18N
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });
        getContentPane().add(btnModificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 260, 80, -1));

        btnAbajo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/btnAbajo1.png"))); // NOI18N
        btnAbajo.setContentAreaFilled(false);
        btnAbajo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAbajo.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/btnAbajo2.png"))); // NOI18N
        btnAbajo.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/btnAbajo2.png"))); // NOI18N
        btnAbajo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAbajoActionPerformed(evt);
            }
        });
        getContentPane().add(btnAbajo, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 400, 80, -1));

        btnArriba.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/btnArriba1.png"))); // NOI18N
        btnArriba.setContentAreaFilled(false);
        btnArriba.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnArriba.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/btnArriba2.png"))); // NOI18N
        btnArriba.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/btnArriba2.png"))); // NOI18N
        btnArriba.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArribaActionPerformed(evt);
            }
        });
        getContentPane().add(btnArriba, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 120, 80, -1));

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
        btnIniciar.setToolTipText("Iniciar");
        btnIniciar.setContentAreaFilled(false);
        btnIniciar.setEnabled(false);
        btnIniciar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnIniciar.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/btnIniciar2.png"))); // NOI18N
        btnIniciar.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/btnIniciar2.png"))); // NOI18N
        btnIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniciarActionPerformed(evt);
            }
        });
        getContentPane().add(btnIniciar, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 540, -1, -1));

        jLabel4.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel4.setText("Nombre de la tarea");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 530, -1, -1));

        etiquetaTiempo.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 44)); // NOI18N
        etiquetaTiempo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        etiquetaTiempo.setText("00:00:00");
        getContentPane().add(etiquetaTiempo, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 290, 280, 70));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/FondoPomodoro.png"))); // NOI18N
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 650));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnReiniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReiniciarActionPerformed
        int res = JOptionPane.showOptionDialog(new JFrame(), "¿Deseas reestablecer el pomodoro?", "Restablecer",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new Object[]{"Sí", "No"}, JOptionPane.YES_OPTION);
        if (res == JOptionPane.YES_OPTION) {
            System.out.println("Se restablece");
            noPomodoro = 4;
            noDescanso = 3;
            noPomodoros.setText("4");
            if (t.isRunning()) {
                t.stop();
                btnIniciar.setEnabled(true);
            }
            this.tiempoPomodoro();
            btnPausa.setEnabled(false);
            btnReiniciar.setEnabled(false);
            actualizarLabel();
        } else if (res == JOptionPane.NO_OPTION) {
            System.out.println("Se cancela");
        } else if (res == JOptionPane.CLOSED_OPTION) {
            System.out.println("No contestó");
        }

    }//GEN-LAST:event_btnReiniciarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        guardar();
        String buscarEstado = dropBusqueda.getSelectedItem().toString();
        if (buscarEstado.equalsIgnoreCase("todo")){
            ArrayList<Tarea> lista = (ArrayList<Tarea>) ctrlTarea.consultar();
            actualizaTabla(lista);
        }else if (buscarEstado.equalsIgnoreCase("Pendiente")){
            actualizaTabla(ordenPendientes);
        }
        else{
            ArrayList<Tarea> lista = (ArrayList<Tarea>) ctrlTarea.buscarTerminado();
            actualizaTabla(lista);
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnPausaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPausaActionPerformed
        t.stop();
        JOptionPane.showMessageDialog(null, "Temporizador en pausa");
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
        String buscarEstado = dropBusqueda.getSelectedItem().toString();
        if (buscarEstado.equalsIgnoreCase("En progreso")) {
            Tarea cl = seleccionado();

            String nombre = cl.getNombre_desc();
            String estado = cl.getEstado();
            
            if (estado.equalsIgnoreCase("En progreso")) {
                btnIniciar.setEnabled(true);
                btnPausa.setEnabled(false);
                btnReiniciar.setEnabled(false);
                lblBombre.setText("Trabajando en " + nombre);
            } else {
                btnIniciar.setEnabled(false);
                btnPausa.setEnabled(false);
                btnReiniciar.setEnabled(false);
                lblBombre.setText("Trabajando en...");
            }
        }

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
            if (buscarEstado.equalsIgnoreCase("pendiente")) {
                
                ArrayList<Tarea> listaT = (ArrayList<Tarea>) ctrlTarea.consultar();
                
                for (Tarea a : ordenPendientes) {
                    if(!listaT.contains(a)){
                        ordenPendientes.remove(a);
                    }
                }
                
                for (Tarea a : listaT) {
                    if(ordenPendientes.contains(a)&& !a.getEstado().equalsIgnoreCase("Pendiente")){
                        ordenPendientes.remove(a);
                    }
                }
                
                actualizaTabla(ordenPendientes);
                btnArriba.setEnabled(true);
                btnAbajo.setEnabled(true);
                btnModificar.setEnabled(true);
                
            } else if (buscarEstado.equalsIgnoreCase("Terminada")) {
                ArrayList<Tarea> listaT = (ArrayList<Tarea>) ctrlTarea.buscarTerminado();
                actualizaTabla(listaT);
                btnArriba.setEnabled(false);
                btnAbajo.setEnabled(false);
                btnModificar.setEnabled(false);
            } else {
                ArrayList<Tarea> lista = (ArrayList<Tarea>) ctrlTarea.buscarEstado("En progreso");
                actualizaTabla(lista);
                btnArriba.setEnabled(false);
                btnAbajo.setEnabled(false);
                btnModificar.setEnabled(true);
            }
        }
        else{
            ArrayList<Tarea> lista = (ArrayList<Tarea>) ctrlTarea.consultar();
            actualizaTabla(lista);
            btnArriba.setEnabled(false);
            btnAbajo.setEnabled(false);
            btnModificar.setEnabled(false);
        }
    }//GEN-LAST:event_dropBusquedaActionPerformed

    private void btnArribaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnArribaActionPerformed
        DefaultTableModel model = (DefaultTableModel) tablaConsulta.getModel();
        int index = tablaConsulta.getSelectedRow();
        if (index > 0) {
            model.moveRow(index, index, index - 1);
            tablaConsulta.setRowSelectionInterval(index - 1, index - 1);
        }
        ordenPendientes.clear();

        for (int i = 0; i < model.getRowCount(); i++) {
            ordenPendientes.add(new Tarea(String.valueOf(tablaConsulta.getValueAt(i, NORMAL)), "Pendiente"));
        }
    }//GEN-LAST:event_btnArribaActionPerformed

    private void btnAbajoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAbajoActionPerformed
        DefaultTableModel model = (DefaultTableModel) tablaConsulta.getModel();
        int index = tablaConsulta.getSelectedRow();
        if (index < model.getRowCount() - 1) {
            model.moveRow(index, index, index + 1);
            tablaConsulta.setRowSelectionInterval(index + 1, index + 1);
        }
        ordenPendientes.clear();
        for (int i = 0; i < model.getRowCount(); i++) {
            ordenPendientes.add(new Tarea(String.valueOf(tablaConsulta.getValueAt(i, NORMAL)), "Pendiente"));
        }
    }//GEN-LAST:event_btnAbajoActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        Tarea cl = seleccionado();
        if (cl.getId() != null) {
            actualizarTarea c = new actualizarTarea(cl);
            if (ordenPendientes.contains(cl)) {
                if (!cl.getEstado().equalsIgnoreCase("Pendiente")) {
                    ordenPendientes.remove(cl);
                }
            } else if (cl.getEstado().equalsIgnoreCase("En Progreso")) {
                ordenPendientes.add(cl);
            }

            c.setVisible(true);
        }
        else{
            JOptionPane.showMessageDialog(this, "Seleccionar una tarea para editar");
        }
    }//GEN-LAST:event_btnModificarActionPerformed

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
    private javax.swing.JButton btnAbajo;
    private javax.swing.JButton btnArriba;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnIniciar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnPausa;
    private javax.swing.JButton btnReiniciar;
    private javax.swing.JComboBox<String> dropBusqueda;
    private javax.swing.JLabel etiquetaTiempo;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel jTarea;
    private javax.swing.JLabel lblBombre;
    private javax.swing.JLabel noPomodoros;
    private javax.swing.JTable tablaConsulta;
    private javax.swing.JTextField txtActividad;
    // End of variables declaration//GEN-END:variables
}
