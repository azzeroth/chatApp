/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.petrik.chatapp;

import hu.petrik.chatlib.client.ChatClient;
import hu.petrik.chatlib.server.ChatServer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author hali
 */
public class ChatJFrame extends javax.swing.JFrame {
    private static final int PORT = 46000;

    private ChatServer server = null;
    private ChatClient client = null;
    
    /**
     * Creates new form ChatJFrame
     */
    public ChatJFrame() {
        initComponents();
        
        startStopServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (server == null) {
                    server = new ChatServer("0.0.0.0", PORT);
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                server.listen();
                            }
                            catch (Exception ex) {
                                showErrorMessage(ex.getMessage());
                            }
                        }
                    });
                    thread.start();
                    serverStatusLabel.setText("Started");
                }
                else {
                    try {
                        server.stop();
                    } catch (IOException ex) {
                        showErrorMessage(ex.getMessage());
                    }
                    server = null;
                    serverStatusLabel.setText("Stopped");
                }
            }
        });
        
        
        
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (client != null) {
                    try {
                        client.close();
                    } catch (IOException ex) {
                        showErrorMessage(ex.getMessage());
                    }
                }
                
                String ip = serverIpTextField.getText();
                client = new ChatClient(ip, PORT);
                client.addMessageReceivedListener(new ChatClient.MessageReceivedListener() {
                    @Override
                    public void messageReceived(String string) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                messagesTextArea.append(string + "\n");
                            }
                        });
                    }

                    @Override
                    public void error(IOException ioe) {
                        showErrorMessage(ioe.getMessage());
                    }
                });
                
                try {
                    client.connect();
                    messagesTextArea.append("Sikeresen csatlakoztál a szerverhez!\n");
                } catch (IOException ex) {
                    showErrorMessage(ex.getMessage());
                }
            }
        });
        
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!newMessageTextField.getText().toString().equals(""))
                    sendMessage();
            }
        });
        
        newMessageTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if(!newMessageTextField.getText().toString().equals(""))
                        sendMessage();
                }
            }
        });
    }
    
    private void sendMessage() {
        String msg = newMessageTextField.getText().trim();
        try {
            client.sendMessage(msg);
            newMessageTextField.setText("");
        } catch (IOException ex) {
            showErrorMessage(msg);
        }
    }
    
    private void showErrorMessage(String msg) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //JOptionPane.showMessageDialog(ChatJFrame.this, msg, "Error", JOptionPane.ERROR_MESSAGE);
                messagesTextArea.append("ERROR: " + msg + "\n");
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        startStopServerButton = new javax.swing.JButton();
        serverStatusLabel = new javax.swing.JLabel();
        newMessageTextField = new javax.swing.JTextField();
        sendButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        messagesTextArea = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        serverIpTextField = new javax.swing.JTextField();
        connectButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        startStopServerButton.setText("Start/Stop Server");

        serverStatusLabel.setText("Stopped");

        sendButton.setText("Send");

        messagesTextArea.setEditable(false);
        messagesTextArea.setColumns(20);
        messagesTextArea.setRows(5);
        jScrollPane1.setViewportView(messagesTextArea);

        jLabel1.setText("Server IP:");

        connectButton.setText("Connect");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(newMessageTextField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sendButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(startStopServerButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(serverStatusLabel))
                            .addComponent(jLabel1))
                        .addGap(0, 325, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(serverIpTextField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(connectButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startStopServerButton)
                    .addComponent(serverStatusLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(serverIpTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(connectButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(newMessageTextField))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sendButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(ChatJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChatJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChatJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChatJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ChatJFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton connectButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea messagesTextArea;
    private javax.swing.JTextField newMessageTextField;
    private javax.swing.JButton sendButton;
    private javax.swing.JTextField serverIpTextField;
    private javax.swing.JLabel serverStatusLabel;
    private javax.swing.JButton startStopServerButton;
    // End of variables declaration//GEN-END:variables
}
