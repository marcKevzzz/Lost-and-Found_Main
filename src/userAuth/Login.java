/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package userAuth;
import admin.AdminPage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import user.ItemDisplay;


/**
 *
 * @author user
 */
public class Login extends javax.swing.JFrame {

    /**
     * Creates new form NewJFrame
     */
    public Login() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jOptionPane1 = new javax.swing.JOptionPane();
        jPanel1 = new javax.swing.JPanel();
        Login = new javax.swing.JButton();
        passwordTxt = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        studentNumTxt = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(800, 500));
        jPanel1.setLayout(null);

        Login.setText("LOGIN");
        Login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoginActionPerformed(evt);
            }
        });
        jPanel1.add(Login);
        Login.setBounds(330, 260, 100, 38);
        jPanel1.add(passwordTxt);
        passwordTxt.setBounds(50, 170, 380, 35);

        jLabel3.setText("Password");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(50, 140, 50, 16);
        jPanel1.add(studentNumTxt);
        studentNumTxt.setBounds(50, 80, 380, 35);

        jLabel4.setText("Student Number");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(50, 50, 110, 16);

        jLabel1.setText("<html>Don't have an account? <a href='#'>SignUp</a></html>");
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel1);
        jLabel1.setBounds(250, 230, 180, 16);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void LoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LoginActionPerformed

        String sUrl, sUser, sPass;
        sUrl = "jdbc:MySQL://localhost:3306/lostandfound_db"; 
        sUser = "root";
        sPass = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(sUrl, sUser, sPass); // Connecting to database

            String studentNumber = studentNumTxt.getText().trim();
            String password = passwordTxt.getText().trim();
            
            if (studentNumber.isEmpty()) {
                jOptionPane1.showMessageDialog(this, "Student Number is required", "Error", jOptionPane1.ERROR_MESSAGE);
                return;
            }
            if (studentNumber.length() != 7) {
                jOptionPane1.showMessageDialog(this, "Incorrect Student Number eg.(24-XXXX)", "Error", jOptionPane1.ERROR_MESSAGE);
                return;
            }
            if (password.isEmpty()) {
                jOptionPane1.showMessageDialog(this, "Password is required", "Error", jOptionPane1.ERROR_MESSAGE);
                return;
            }
            
            // Query 1: Check if student number exists
            String queryUserExists = "SELECT * FROM users_tbl WHERE schoolID = ?"; //queries
            PreparedStatement pstmtUser = con.prepareStatement(queryUserExists); 
            pstmtUser.setString(1, studentNumber);
            ResultSet rsUser = pstmtUser.executeQuery();

            if (rsUser.next()) {
                // Query 2: Matching schooldId and password
                String queryLogin = "SELECT * FROM users_tbl WHERE schoolID = ? AND password = ?";
                PreparedStatement pstmtLogin = con.prepareStatement(queryLogin);
                pstmtLogin.setString(1, studentNumber);
                pstmtLogin.setString(2, password);
                ResultSet rsLogin = pstmtLogin.executeQuery();

                if (rsLogin.next()) {
                    // Login successful
                    jOptionPane1.showMessageDialog(this, "Login Successful!");

                    if (studentNumber.equals("00-0000") && password.equals("admin123")) {
                        this.dispose();
                        new AdminPage().setVisible(true);
                    } else {
                        this.dispose();
                        new ItemDisplay().setVisible(true);
                    }

                } else {
                    // Password is incorrect
                    jOptionPane1.showMessageDialog(this, "Incorrect Password", "Error", jOptionPane1.ERROR_MESSAGE);
                }

            } else {
                // Student number not found
                jOptionPane1.showMessageDialog(this, "Student Number not found", "Error", jOptionPane1.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            jOptionPane1.showMessageDialog(null, e.getMessage(), "Database Error", jOptionPane1.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_LoginActionPerformed

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
       SignUp signUp = new SignUp();
        signUp.setVisible(true);
  
        this.dispose();
    }//GEN-LAST:event_jLabel1MouseClicked

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
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Login;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JOptionPane jOptionPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField passwordTxt;
    private javax.swing.JTextField studentNumTxt;
    // End of variables declaration//GEN-END:variables
}
