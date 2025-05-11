/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package admin;

import java.awt.Image;
import javax.swing.ImageIcon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import user.Session;

/**
 *
 * @author QCU
 */
public class ClaimCard extends javax.swing.JPanel {

    /**
     * Creates new form ClaimCard
     */
    public ClaimCard() {
        initComponents();
    }

    private int claimId;
    private int index;

    public void setClaimInfo(byte[] imgBytes, String dateLost, String userName,
            String studentId, String yrSec, String email, String phone, String inquiry, int claimId, int index, String status) {
        this.claimId = claimId;
        this.index = index;
        dateLost1.setText(dateLost);
        userName1.setText(userName);
        studentId1.setText(studentId);
        YrSec1.setText(yrSec);
        email1.setText(email);
        phone1.setText(phone);
        inquiry2.setText(inquiry);

        if (imgBytes != null) {
            ImageIcon originalIcon = new ImageIcon(imgBytes);
            Image scaledImage = originalIcon.getImage().getScaledInstance(230, 230, Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(scaledImage);
            proof.setIcon(icon);
        }
                System.out.println(status);

        check(status);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        proof1 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        inquiry2 = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        proof = new javax.swing.JLabel();
        dateLost1 = new javax.swing.JLabel();
        userName1 = new javax.swing.JLabel();
        YrSec1 = new javax.swing.JLabel();
        email1 = new javax.swing.JLabel();
        more1 = new javax.swing.JButton();
        accept1 = new javax.swing.JButton();
        studentId1 = new javax.swing.JLabel();
        phone1 = new javax.swing.JLabel();

        jDialog1.setBackground(new java.awt.Color(255, 255, 255));

        proof1.setBackground(new java.awt.Color(255, 255, 255));
        proof1.setPreferredSize(new java.awt.Dimension(700, 300));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        inquiry2.setEditable(false);
        inquiry2.setBackground(new java.awt.Color(255, 255, 255));
        inquiry2.setColumns(20);
        inquiry2.setRows(5);
        inquiry2.setBorder(null);
        inquiry2.setFocusable(false);
        jScrollPane2.setViewportView(inquiry2);

        jLabel5.setFont(new java.awt.Font("Segoe UI Variable", 0, 18)); // NOI18N
        jLabel5.setText("Inquiry");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 427, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2)
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setFont(new java.awt.Font("Segoe UI Variable", 0, 18)); // NOI18N
        jLabel6.setText("Receipt / Proof Image");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(proof, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(proof, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout proof1Layout = new javax.swing.GroupLayout(proof1);
        proof1.setLayout(proof1Layout);
        proof1Layout.setHorizontalGroup(
            proof1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(proof1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        proof1Layout.setVerticalGroup(
            proof1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(proof1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(proof1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jDialog1.getContentPane().add(proof1, java.awt.BorderLayout.CENTER);

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(1190, 40));
        setMinimumSize(new java.awt.Dimension(1190, 40));
        setName(""); // NOI18N
        setPreferredSize(new java.awt.Dimension(1190, 40));
        setRequestFocusEnabled(false);

        dateLost1.setBackground(new java.awt.Color(255, 204, 204));
        dateLost1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        userName1.setBackground(new java.awt.Color(255, 204, 204));
        userName1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        YrSec1.setBackground(new java.awt.Color(255, 204, 204));
        YrSec1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        email1.setBackground(new java.awt.Color(255, 204, 204));
        email1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        more1.setText("More");
        more1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                more1ActionPerformed(evt);
            }
        });

        accept1.setBackground(new java.awt.Color(0, 255, 0));
        accept1.setForeground(new java.awt.Color(51, 51, 51));
        accept1.setText("Approved");
        accept1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accept1ActionPerformed(evt);
            }
        });

        studentId1.setBackground(new java.awt.Color(255, 204, 204));
        studentId1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        phone1.setBackground(new java.awt.Color(255, 255, 255));
        phone1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dateLost1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(userName1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(studentId1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(YrSec1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(email1, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(phone1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 156, Short.MAX_VALUE)
                .addComponent(more1, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(accept1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                        .addComponent(accept1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(more1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(dateLost1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(userName1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(studentId1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(YrSec1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(email1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(phone1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(11, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void accept1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accept1ActionPerformed
      

            String decision = "Claim Request";
            String getQuery = "";
            String updateItemQuery = "";
            String updateClaimQuery = "";
            try (Connection con = DBConnection.DataBase.getConnection()) {
                con.setAutoCommit(false); // Start transaction

                // Decide which table to query and update based on index
                if (index == 0) {
                    getQuery = "SELECT * FROM itemFound WHERE id = ?";
                    updateItemQuery = "UPDATE itemFound SET status = ?, timestamp = CURRENT_TIMESTAMP WHERE id = ?";
                    updateClaimQuery = "UPDATE claims_tbl SET status_claim = ?, timestamp = CURRENT_TIMESTAMP WHERE claimId = ?";
                } else if (index == 1) {
                    getQuery = "SELECT * FROM itemreport WHERE id = ?";
                    updateItemQuery = "UPDATE itemreport SET status = ?, timestamp = CURRENT_TIMESTAMP WHERE id = ?";
                    updateClaimQuery = "UPDATE claimsLost_tbl SET status_claim = ?, timestamp = CURRENT_TIMESTAMP WHERE claimId = ?";
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid index provided.");
                    return;
                }

                // Get the report data
                PreparedStatement psts = con.prepareStatement(getQuery);
                psts.setInt(1, claimId);
                ResultSet rs = psts.executeQuery();

                int reportId = -1;
                if (rs.next()) {
                    reportId = rs.getInt("reportId");
                } else {
                    JOptionPane.showMessageDialog(this, "Report ID not found for the claim.");
                    return;
                }

                // Update item status
                PreparedStatement ps = con.prepareStatement(updateItemQuery);
                ps.setString(1, decision);
                ps.setInt(2, claimId);
                int psU = ps.executeUpdate();

                // Update claim status
                PreparedStatement pst = con.prepareStatement(updateClaimQuery);
                pst.setString(1, decision);
                pst.setInt(2, claimId);
                int claimUpdate = pst.executeUpdate();

                if (claimUpdate > 0 && psU > 0) {
                    con.commit();
                    accept1.setText("Approved");
                    accept1.setEnabled(false);
                    accept1.setBorderPainted(false);
                    accept1.setContentAreaFilled(false);
                    repaint();
                    revalidate();
                    JOptionPane.showMessageDialog(this, "Confirmed Claim");
                } else {
                    con.rollback();
                    JOptionPane.showMessageDialog(this, "Failed to confirm claim. Please try again.");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }



    }//GEN-LAST:event_accept1ActionPerformed

    private void more1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_more1ActionPerformed
        jDialog1.pack();                         // Fit size
        jDialog1.setLocationRelativeTo(this); // Center on parent
        jDialog1.setVisible(true);

    }//GEN-LAST:event_more1ActionPerformed
    
    private void check(String status){
          if (!status.equalsIgnoreCase("Claim Pending")) {
                 accept1.setText("Approved");
            accept1.setEnabled(false);
            accept1.setBorderPainted(false);
            accept1.setContentAreaFilled(false);
            repaint();
            revalidate();
          }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel YrSec1;
    private javax.swing.JButton accept1;
    private javax.swing.JLabel dateLost1;
    private javax.swing.JLabel email1;
    private javax.swing.JTextArea inquiry2;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton more1;
    private javax.swing.JLabel phone1;
    private javax.swing.JLabel proof;
    private javax.swing.JPanel proof1;
    private javax.swing.JLabel studentId1;
    private javax.swing.JLabel userName1;
    // End of variables declaration//GEN-END:variables
}
