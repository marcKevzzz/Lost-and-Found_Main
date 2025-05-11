/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package admin;

import java.awt.Component;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.BoxLayout;

/**
 *
 * @author QCU
 */
public final class ViewClaim extends javax.swing.JPanel {

    /**
     * Creates new form ViewClaim
     * @param id
     */
    public ViewClaim(int id) {
        initComponents();
        loadClaimDetails(id);
    }
    

   void loadClaimDetails(int id) {
        panelClaims1.setLayout(new BoxLayout(panelClaims1, BoxLayout.Y_AXIS));
        setVisible(true);
        try (Connection con = DBConnection.DataBase.getConnection()) {
            String query = "SELECT claims_tbl.* FROM claims_tbl INNER JOIN itemfound ON claims_tbl.claimId = itemfound.id WHERE itemfound.id = ?";

            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
           

            while (rs.next()) {
                byte[] imageData = rs.getBytes("proofImage");
                ClaimCard card = new ClaimCard();
                Timestamp timestamp = rs.getTimestamp("claimDate");

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                String formattedDate = sdf.format(timestamp);
                
                card.setClaimInfo(
                    imageData,
                    formattedDate,
                    rs.getString("fullName"),
                    rs.getString("studentNumber"),
                    rs.getString("yearSec"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("inquiry"),
                    rs.getInt("claimId"),
                    0,
                    rs.getString("status_claim")
                );
                
                System.out.println( rs.getInt("claimId"));

                card.setAlignmentX(Component.CENTER_ALIGNMENT);
                panelClaims1.add(card);
                panelClaims1.add(Box.createVerticalStrut(10));
            }
                // Load proof image if available
                panelClaims1.revalidate();
                panelClaims1.repaint();
            } catch (SQLException ex) {
            Logger.getLogger(ViewClaim.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        panelClaims1 = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setPreferredSize(new java.awt.Dimension(1200, 500));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel5.setOpaque(false);
        jPanel5.setPreferredSize(new java.awt.Dimension(900, 70));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel4.add(jPanel5, java.awt.BorderLayout.PAGE_START);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setPreferredSize(new java.awt.Dimension(1000, 435));
        jPanel6.setLayout(new java.awt.BorderLayout());

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane2.setBorder(null);
        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        panelClaims1.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panelClaims1Layout = new javax.swing.GroupLayout(panelClaims1);
        panelClaims1.setLayout(panelClaims1Layout);
        panelClaims1Layout.setHorizontalGroup(
            panelClaims1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelClaims1Layout.setVerticalGroup(
            panelClaims1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(panelClaims1);

        jPanel6.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel4.add(jPanel6, java.awt.BorderLayout.CENTER);

        add(jPanel4, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel panelClaims1;
    // End of variables declaration//GEN-END:variables
}
