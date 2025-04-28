/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package admin;

import DBConnection.DataBase;
import static DBConnection.DataBase.closeConnection;
import Table.ImageCellEditor;
import Table.TableActionCellEditor;
import Table.TableActionCellRender;
import Table.TableActionEvent;
import Table.userManagement.UserCellPanelAction;
import Table.userManagement.UserTableActionCellEditor;
import Table.userManagement.UserTableActionCellRender;
import Table.userManagement.UserTableActionEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.Image;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author QCU
 */
public class UserManagement extends javax.swing.JFrame {

    /**
     * Creates new form UserManagement
     */
    public UserManagement() {
        initComponents();
        userTable.getColumnModel().getColumn(10).setCellEditor(
                new UserTableActionCellEditor(
                        new UserTableActionEvent() {
                    @Override
                    public void onEdit(int row) {
                        if (editableRowIndex != -1 && editableRowIndex != row) {
                            saveRowData(editableRowIndex); // Custom method below
                        }

                        // Toggle logic
                        if (editableRowIndex == row) {
                            // Save and close editing
                            saveRowData(row);
                            editableRowIndex = -1;
                        } else {
                            editableRowIndex = row;
                        }

                        refreshTableModel();

                    }

                    private void refreshTableModel() {
                        DefaultTableModel currentModel = (DefaultTableModel) userTable.getModel();

                        DefaultTableModel model = new DefaultTableModel() {
                            @Override
                            public boolean isCellEditable(int rowIndex, int columnIndex) {
                                if (columnIndex == 10) {
                                    return true;
                                }
                                return rowIndex == editableRowIndex;
                            }

                            @Override
                            public Class<?> getColumnClass(int columnIndex) {
                                return columnIndex == 1 ? ImageIcon.class : String.class;
                            }
                        };

                        model.setColumnIdentifiers(new Object[]{
                            "", "Profile", "First Name", "Last Name", "Password", "Student Number", "Year and Section",
                            "Email", "Phone", "", "Action",});

                        for (int i = 0; i < currentModel.getRowCount(); i++) {
                            Vector<?> rowData = (Vector<?>) currentModel.getDataVector().elementAt(i);
                            model.addRow((Vector<?>) rowData.clone());
                        }

                        userTable.setModel(model);

                        // Set renderers and editors again
                        userTable.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
                            @Override
                            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                    boolean hasFocus, int row, int column) {
                                JLabel label = new JLabel();
                                if (value instanceof ImageIcon) {
                                    label.setIcon((ImageIcon) value);
                                }
                                label.setHorizontalAlignment(JLabel.CENTER);
                                return label;
                            }
                        });

                        ImageCellEditor imageEditor = new ImageCellEditor(userTable, 1);
                        imageEditor.setEditable(editableRowIndex); // or any logic that sets it as editable
                        userTable.getColumnModel().getColumn(1).setCellEditor(imageEditor);

                        userTable.getColumnModel().getColumn(10).setCellRenderer(
                                new UserTableActionCellRender(() -> editableRowIndex)
                        );

                        userTable.getColumnModel().getColumn(10).setCellEditor(
                                new UserTableActionCellEditor(this, () -> editableRowIndex)
                        );

                        // Hide ID columns
                        TableColumn col0 = userTable.getColumnModel().getColumn(0);
                        col0.setMinWidth(0);
                        col0.setMaxWidth(0);
                        col0.setPreferredWidth(0);
                        col0.setResizable(false);
                        TableColumn co9 = userTable.getColumnModel().getColumn(9);
                        co9.setMinWidth(0);
                        co9.setMaxWidth(0);
                        co9.setPreferredWidth(0);
                        co9.setResizable(false);
                    }

                    private void saveRowData(int row) {
                        AdminPage admin = new AdminPage();
                        try {
                            int reportId = (int) userTable.getValueAt(row, 0);
                            String firstName = userTable.getValueAt(row, 2).toString();
                            String lastName = userTable.getValueAt(row, 3).toString();
                            String password = userTable.getValueAt(row, 4).toString();
                            String studentNumber = userTable.getValueAt(row, 5).toString();
                            String yearSec = userTable.getValueAt(row, 6).toString();
                            String email = userTable.getValueAt(row, 7).toString();
                            String phone = userTable.getValueAt(row, 8).toString();
                            boolean status = (boolean) userTable.getValueAt(row, 9);
                            ImageIcon icon = (ImageIcon) userTable.getValueAt(row, 1);
                            byte[] image = admin.imageIconToBytes(icon);

                            try (Connection conn = DataBase.getConnection()) {
                                String updateQuery = "UPDATE users_tbl SET firstName=?, lastName=?, password=?, schoolId=?, yearSec=?, email=?, phone=?, profile=?, status=? WHERE id=?";
                                PreparedStatement pst = conn.prepareStatement(updateQuery);

                                pst.setString(1, firstName);
                                pst.setString(2, lastName);
                                pst.setString(3, password);
                                pst.setString(4, studentNumber);
                                pst.setString(5, yearSec);
                                pst.setString(6, email);
                                pst.setString(7, phone);
                                pst.setBytes(8, image);
                                pst.setBoolean(9, status);
                                pst.setInt(10, reportId);

                                int rowsUpdated = pst.executeUpdate();
                                if (rowsUpdated > 0) {
                                    JOptionPane.showMessageDialog(null, "Item successfully updated!");
                                }
                            }
                        } catch (HeadlessException | SQLException e) {
                            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage());
                        }
                    }

                    @Override
                    public void onStatus(int row) {
                        if (userTable.isEditing()) {
                            userTable.getCellEditor().stopCellEditing();
                            editableRowIndex= -1;
                            
                        }

                        try {
                            int id = (int) userTable.getValueAt(row, 0); // User ID
                            boolean currentStatus = Boolean.TRUE.equals(userTable.getValueAt(row, 9)); // true = active, false = banned
                            boolean newStatus = currentStatus; // Default to no change

                            String message = currentStatus ? "Do you want to ban this user?" : "Do you want to unban this user?";
                            String title = currentStatus ? "Confirm Ban" : "Confirm Unban";

                            int confirm = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
                            if (confirm == JOptionPane.YES_OPTION) {
                                newStatus = !currentStatus;

                                String sql = "UPDATE users_tbl SET Status = ? WHERE id = ?";
                                try (Connection conn = DataBase.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                                    stmt.setBoolean(1, newStatus);
                                    stmt.setInt(2, id);

                                    int update = stmt.executeUpdate();
                                    if (update > 0) {
                                        userTable.setValueAt(newStatus, row, 9);
                                        userTable.repaint();

                                        JOptionPane.showMessageDialog(null, "Status changed successfully.");
                                    } else {
                                        JOptionPane.showMessageDialog(null, "No user was updated in the database.");
                                    }
                                }
                            }
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
                        }
                    }

                },
                        () -> editableRowIndex // <-- this passes the current editable row
                )
        );

        userTable.getColumnModel().getColumn(1).setPreferredWidth(50);
        userTable.getColumnModel().getColumn(10).setCellRenderer(
                new UserTableActionCellRender(() -> editableRowIndex)
        );// Example for the first name column
    }

    private int editableRowIndex = -1;
    private ImageCellEditor imageEditor;

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jOptionPane1 = new javax.swing.JOptionPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        userTable = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1200, 600));
        setPreferredSize(new java.awt.Dimension(1300, 700));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(1300, 700));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(null);
        jScrollPane1.setPreferredSize(new java.awt.Dimension(1133, 537));

        userTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "", "Profile", "First Name", "Last Name", "Password", "Student Number", "Year and Section", "Email", "Phone", "", "Action"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        userTable.setRowHeight(100);
        jScrollPane1.setViewportView(userTable);
        if (userTable.getColumnModel().getColumnCount() > 0) {
            userTable.getColumnModel().getColumn(0).setResizable(false);
            userTable.getColumnModel().getColumn(0).setPreferredWidth(1);
            userTable.getColumnModel().getColumn(1).setResizable(false);
            userTable.getColumnModel().getColumn(1).setPreferredWidth(60);
            userTable.getColumnModel().getColumn(2).setResizable(false);
            userTable.getColumnModel().getColumn(3).setResizable(false);
            userTable.getColumnModel().getColumn(4).setResizable(false);
            userTable.getColumnModel().getColumn(5).setResizable(false);
            userTable.getColumnModel().getColumn(6).setResizable(false);
            userTable.getColumnModel().getColumn(7).setResizable(false);
            userTable.getColumnModel().getColumn(8).setResizable(false);
            userTable.getColumnModel().getColumn(9).setResizable(false);
            userTable.getColumnModel().getColumn(9).setPreferredWidth(1);
            userTable.getColumnModel().getColumn(10).setResizable(false);
        }

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel6.setBackground(new java.awt.Color(0, 0, 204));
        jPanel6.setMinimumSize(new java.awt.Dimension(100, 100));
        jPanel6.setPreferredSize(new java.awt.Dimension(82, 700));

        jPanel7.setOpaque(false);
        jPanel7.setPreferredSize(new java.awt.Dimension(70, 60));

        jLabel1.setFont(new java.awt.Font("Segoe UI Symbol", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("QCU");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        jPanel8.setOpaque(false);
        jPanel8.setLayout(new java.awt.BorderLayout());

        jLabel2.setBackground(new java.awt.Color(51, 51, 255));
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("ITEMS");
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });
        jPanel8.add(jLabel2, java.awt.BorderLayout.CENTER);

        jPanel9.setBackground(new java.awt.Color(0, 51, 255));

        jLabel3.setBackground(new java.awt.Color(153, 153, 255));
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("USERS");
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jTextField1.setText("jTextField1");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(257, 257, 257)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(78, 78, 78)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
            .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1212, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 543, Short.MAX_VALUE))
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, 706, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 706, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        userTable.getColumnModel().getColumn(1).setCellEditor(imageEditor);

        String query = "SELECT * FROM users_tbl";

        try (Connection conn = DataBase.getConnection()) {
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = (DefaultTableModel) userTable.getModel();
            model.setRowCount(0); // Clear table

            while (rs.next()) {
                byte[] imgBytes = rs.getBytes("profile");
                ImageIcon icon;

                if (imgBytes != null) {
                    ImageIcon originalIcon = new ImageIcon(imgBytes);
                    Image scaledImage = originalIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                    icon = new ImageIcon(scaledImage);
                } else {
                    ImageIcon originalIcon = new ImageIcon(getClass().getResource("/Table/UserManagement/user.jpg"));
                    Image scaledImage = originalIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                    icon = new ImageIcon(scaledImage);
                }

                Object[] rows = new Object[]{
                    rs.getInt("id"),
                    icon,
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getString("Password"),
                    rs.getString("schoolId"),
                    rs.getString("yearSec"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getBoolean("Status")
                };

                model.addRow(rows);

                userTable.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value,
                            boolean isSelected, boolean hasFocus, int row, int column) {
                        if (value instanceof ImageIcon) {
                            JLabel label = new JLabel();
                            label.setIcon((ImageIcon) value);
                            label.setHorizontalAlignment(SwingConstants.CENTER);
                            return label;
                        }
                        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    }
                });
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserManagement.class.getName()).log(Level.SEVERE, null, ex);
        }

        TableColumn col0 = userTable.getColumnModel().getColumn(0);
        col0.setMinWidth(0);
        col0.setMaxWidth(0);
        col0.setPreferredWidth(0);
        col0.setResizable(false);
        TableColumn co9 = userTable.getColumnModel().getColumn(9);
        co9.setMinWidth(0);
        co9.setMaxWidth(0);
        co9.setPreferredWidth(0);
        co9.setResizable(false);

       
        userTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                } else {
                    Boolean status = (Boolean) table.getValueAt(row, 9); // Column 9 holds status
                    if (Boolean.FALSE.equals(status)) {
                        c.setBackground(new Color(250, 128, 114)); // Red for banned
                    } else {
                        c.setBackground(table.getBackground()); // Or table.getBackground()
                    }
                    c.setForeground(Color.BLACK);
                }

                return c;
            }
        });


    }//GEN-LAST:event_formComponentShown

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        
    }//GEN-LAST:event_jLabel3MouseClicked

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
       new admin.AdminPage().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jLabel2MouseClicked

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
            java.util.logging.Logger.getLogger(UserManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UserManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UserManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UserManagement.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UserManagement().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JOptionPane jOptionPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTable userTable;
    // End of variables declaration//GEN-END:variables
}
