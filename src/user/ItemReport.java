/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package user;
import java.awt.HeadlessException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import jnafilechooser.api.JnaFileChooser;

/**
 *
 * @author QCU
 */
public class ItemReport extends javax.swing.JFrame {

    /**
     * Creates new form UsersPage
     */
    public ItemReport() {
        initComponents();
        
    }
    
    JnaFileChooser ch = new JnaFileChooser();

    /**a
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        date = new com.raven.datechooser.DateChooser();
        time = new com.raven.swing.TimePicker();
        jOptionPane1 = new javax.swing.JOptionPane();
        jPanel1 = new javax.swing.JPanel();
        phoneTxt = new javax.swing.JTextField();
        nameTxt = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        descTxt = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        timeTxt = new javax.swing.JTextField();
        userNameTxt = new javax.swing.JTextField();
        yrsecTxt = new javax.swing.JTextField();
        emailTxt = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        locationTxt = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        dateTxt = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        labelShowImage = new javax.swing.JLabel();
        labelShowFile = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        categoryCb = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();

        date.setDateFormat("yyyy-MM-dd");
        date.setTextRefernce(dateTxt);

        time.setDisplayText(timeTxt);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setPreferredSize(new java.awt.Dimension(1200, 800));
        jPanel1.setLayout(null);
        jPanel1.add(phoneTxt);
        phoneTxt.setBounds(250, 620, 370, 30);
        jPanel1.add(nameTxt);
        nameTxt.setBounds(250, 160, 370, 30);

        descTxt.setColumns(20);
        descTxt.setRows(5);
        jScrollPane1.setViewportView(descTxt);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(250, 320, 370, 60);

        jLabel1.setFont(new java.awt.Font("Segoe UI Variable", 0, 14)); // NOI18N
        jLabel1.setText("Phone");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(60, 620, 140, 20);

        jLabel3.setFont(new java.awt.Font("Segoe UI Variable", 0, 14)); // NOI18N
        jLabel3.setText("Name");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(60, 160, 110, 20);

        jLabel4.setFont(new java.awt.Font("Segoe UI Variable", 0, 14)); // NOI18N
        jLabel4.setText("Time Lost");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(60, 280, 110, 20);

        timeTxt.setEditable(false);
        jPanel1.add(timeTxt);
        timeTxt.setBounds(250, 280, 220, 30);
        jPanel1.add(userNameTxt);
        userNameTxt.setBounds(250, 500, 370, 30);
        jPanel1.add(yrsecTxt);
        yrsecTxt.setBounds(250, 540, 370, 30);
        jPanel1.add(emailTxt);
        emailTxt.setBounds(250, 580, 370, 30);

        jLabel5.setFont(new java.awt.Font("Segoe UI Variable", 0, 14)); // NOI18N
        jLabel5.setText("Description");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(60, 320, 110, 20);

        jLabel6.setFont(new java.awt.Font("Segoe UI Variable", 0, 14)); // NOI18N
        jLabel6.setText("Image Attachment");
        jPanel1.add(jLabel6);
        jLabel6.setBounds(60, 400, 140, 20);

        jLabel7.setFont(new java.awt.Font("Segoe UI Variable", 0, 14)); // NOI18N
        jLabel7.setText("Name");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(60, 500, 140, 20);

        jLabel8.setFont(new java.awt.Font("Segoe UI Variable", 0, 14)); // NOI18N
        jLabel8.setText("Year and Section");
        jPanel1.add(jLabel8);
        jLabel8.setBounds(60, 540, 140, 20);

        jLabel9.setFont(new java.awt.Font("Segoe UI Variable", 0, 14)); // NOI18N
        jLabel9.setText("Email");
        jPanel1.add(jLabel9);
        jLabel9.setBounds(60, 580, 140, 20);

        jButton1.setText("SUBMIT");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(420, 680, 200, 40);

        jLabel10.setFont(new java.awt.Font("Segoe UI Variable", 0, 14)); // NOI18N
        jLabel10.setText("Location");
        jPanel1.add(jLabel10);
        jLabel10.setBounds(60, 200, 110, 20);
        jPanel1.add(locationTxt);
        locationTxt.setBounds(250, 200, 370, 30);

        jButton2.setText("Choose File");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2);
        jButton2.setBounds(250, 400, 120, 23);
        jPanel1.add(dateTxt);
        dateTxt.setBounds(250, 240, 220, 30);

        jLabel11.setFont(new java.awt.Font("Segoe UI Variable", 0, 14)); // NOI18N
        jLabel11.setText("Date Lost");
        jPanel1.add(jLabel11);
        jLabel11.setBounds(60, 240, 110, 20);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelShowImage, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(labelShowImage, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2);
        jPanel2.setBounds(680, 120, 520, 260);

        labelShowFile.setText("No file choosen");
        jPanel1.add(labelShowFile);
        labelShowFile.setBounds(380, 400, 240, 20);

        jButton3.setText("Date Now");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3);
        jButton3.setBounds(530, 240, 90, 30);

        jButton5.setText("Time Now");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton5);
        jButton5.setBounds(530, 280, 90, 30);

        jButton4.setText("...");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton4);
        jButton4.setBounds(480, 280, 40, 30);

        jButton6.setText("...");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton6);
        jButton6.setBounds(480, 240, 40, 30);

        categoryCb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Category", "Purse", "Accessories", "Money", "Pet", "School Material" }));
        categoryCb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                categoryCbActionPerformed(evt);
            }
        });
        jPanel1.add(categoryCb);
        categoryCb.setBounds(250, 120, 220, 30);

        jLabel12.setFont(new java.awt.Font("Segoe UI Variable", 0, 14)); // NOI18N
        jLabel12.setText("Lorem Ipsum Lorem Ipsumm hahsdhh hashd");
        jPanel1.add(jLabel12);
        jLabel12.setBounds(60, 70, 330, 20);

        jLabel13.setFont(new java.awt.Font("Segoe UI Variable", 1, 20)); // NOI18N
        jLabel13.setText("Provide Your Contact Information");
        jPanel1.add(jLabel13);
        jLabel13.setBounds(60, 460, 390, 20);

        jLabel14.setFont(new java.awt.Font("Segoe UI Variable", 0, 14)); // NOI18N
        jLabel14.setText("Category");
        jPanel1.add(jLabel14);
        jLabel14.setBounds(60, 120, 110, 20);

        jLabel15.setFont(new java.awt.Font("Segoe UI Variable", 1, 20)); // NOI18N
        jLabel15.setText("Report a Lost Item");
        jPanel1.add(jLabel15);
        jLabel15.setBounds(60, 40, 240, 20);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        
        //File Chooser Library for better UI
        ch.setMode(JnaFileChooser.Mode.Files);
        ch.addFilter("Image Files", "jpg", "jfif", "jpeg", "png", "gif", "bmp");
        boolean act = ch.showOpenDialog(this);
        if (act){
            File f = ch.getSelectedFile();
            String filePath = f.getAbsolutePath();
            String fileName = f.getName(); 

            ImageIcon originalIcon = new ImageIcon(filePath);
            Image originalImage = originalIcon.getImage();

            int originalWidth = originalImage.getWidth(null);
            int originalHeight = originalImage.getHeight(null);

            int targetHeight = 260;
            int targetWidth = (originalWidth * targetHeight) / originalHeight;

            Image scaledImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(scaledImage);

            labelShowImage.setIcon(resizedIcon);
            labelShowFile.setText(fileName);
        }
       
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        date.toDay(); 
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
       time.now();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
       date.showPopup();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        time.showPopup(this, 100, 100);        
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String sUrl = "jdbc:mysql://localhost:3306/lostandfound_db";
        String sUser = "root";
        String sPass = "";
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); 
            Connection con = DriverManager.getConnection(sUrl, sUser, sPass); //Connecting into databse

            StringBuilder errors = new StringBuilder(); // Collecting all errors for one output

            //Basic Validation
            if (categoryCb.getSelectedItem().toString().equals("Select Category")) {
                errors.append("- Please select a valid category\n");
            }
            if (nameTxt.getText().trim().isEmpty()) {
                errors.append("- Item Name is required\n");
            }
            if (locationTxt.getText().trim().isEmpty()) {
                errors.append("- Location is required\n");
            }
            if (descTxt.getText().trim().isEmpty()) {
                errors.append("- Description is required\n");
            }
            if (labelShowFile.getText().equals("No file choosen") || labelShowFile.getText().isBlank()) {
                errors.append("- Please attach an image\n");
            }
            if (userNameTxt.getText().trim().isEmpty()) {
                errors.append("- Your name is required\n");
            }
            if (yrsecTxt.getText().trim().isEmpty()) {
                errors.append("- Year and Section is required\n");
            }
            if (phoneTxt.getText().trim().isEmpty()) {
                errors.append("- Phone number is required\n");
            }

            // If there are any errors, show them all at once
            if (errors.length() > 0) {
                JOptionPane.showMessageDialog(null, errors.toString(), "Input Errors", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Prepare data
            String category = categoryCb.getSelectedItem().toString();
            String itemName = nameTxt.getText().trim();
            String location = locationTxt.getText().trim();
            String dateLost = dateTxt.getText();// or parse from a date picker
            String timeInput = timeTxt.getText().trim(); // e.g., 10:30AM
            String description = descTxt.getText().trim();
            String userName = userNameTxt.getText().trim();
            String yrSec = yrsecTxt.getText().trim();
            String phone = phoneTxt.getText().trim(); 
            String email = emailTxt.getText().trim();

            SimpleDateFormat inputFormat = new SimpleDateFormat("hh:mm a");
            SimpleDateFormat sqlTimeFormat = new SimpleDateFormat("HH:mm:ss");
            String formattedTime;
            //Try cath for time format
            try {
                Date parsedTime = inputFormat.parse(timeInput);
                formattedTime = sqlTimeFormat.format(parsedTime);
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(null, "Invalid time format. Use hh:mmAM/PM (e.g., 10:30AM)", "Time Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
           
            // Image
            File f = ch.getSelectedFile();
            InputStream in = new FileInputStream(f.getAbsolutePath());
            if (!f.exists()) {
                JOptionPane.showMessageDialog(null, "File does not exist: " + f.getAbsolutePath());
                return;
            }
            

            String query = "INSERT INTO itemReport (category, itemName, location, dateLost, timeLost, description, imageAttach, name, yearSec, email, phone) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, category);
            pst.setString(2, itemName);
            pst.setString(3, location);
            pst.setString(4, dateLost);
            pst.setString(5, formattedTime);
            pst.setString(6, description);
            pst.setBlob(7, in);
            pst.setString(8, userName);
            pst.setString(9, yrSec);
            pst.setString(10, email);
            pst.setString(11, phone);

            int rows = pst.executeUpdate(); 
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Lost item added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                
                this.dispose(); 
                new ItemDisplay().setVisible(true); // Proceed to item display after reporting
            }

            con.close();
        } catch (HeadlessException | ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (FileNotFoundException ex) {
           JOptionPane.showMessageDialog(this, "File not found, please choose a correct file", "Input Error", JOptionPane.ERROR_MESSAGE);  
        } 
    }//GEN-LAST:event_jButton1ActionPerformed

    private void categoryCbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_categoryCbActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_categoryCbActionPerformed

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
            java.util.logging.Logger.getLogger(ItemReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ItemReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ItemReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ItemReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ItemReport().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> categoryCb;
    private com.raven.datechooser.DateChooser date;
    private javax.swing.JTextField dateTxt;
    private javax.swing.JTextArea descTxt;
    private javax.swing.JTextField emailTxt;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JOptionPane jOptionPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelShowFile;
    private javax.swing.JLabel labelShowImage;
    private javax.swing.JTextField locationTxt;
    private javax.swing.JTextField nameTxt;
    private javax.swing.JTextField phoneTxt;
    private com.raven.swing.TimePicker time;
    private javax.swing.JTextField timeTxt;
    private javax.swing.JTextField userNameTxt;
    private javax.swing.JTextField yrsecTxt;
    // End of variables declaration//GEN-END:variables
}
