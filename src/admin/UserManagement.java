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
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

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
         DefaultTableModel model = (DefaultTableModel) userTable.getModel();
         sorter = new TableRowSorter<>(model);
        userTable.setRowSorter(sorter);
    }

    private int editableRowIndex = -1;
    private ImageCellEditor imageEditor;
    
     
         private final TableRowSorter<DefaultTableModel> sorter;

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
        jPanel3 = new javax.swing.JPanel();
        searchTxt = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();

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

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        searchTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchTxtActionPerformed(evt);
            }
        });

        jButton1.setText("Search");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Clear");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(searchTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(searchTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel14.setBackground(new java.awt.Color(0, 51, 153));
        jPanel14.setMinimumSize(new java.awt.Dimension(100, 100));
        jPanel14.setPreferredSize(new java.awt.Dimension(82, 700));
        jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel15.setBackground(new java.awt.Color(0, 51, 255));
        jPanel15.setMinimumSize(new java.awt.Dimension(32, 20));
        jPanel15.setOpaque(false);
        jPanel15.setLayout(new java.awt.BorderLayout());
        jPanel14.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(126, 105, -1, 35));

        jPanel20.setBackground(new java.awt.Color(102, 102, 255));
        jPanel20.setOpaque(false);

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel14.add(jPanel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(126, 100, -1, -1));

        jPanel21.setBackground(new java.awt.Color(102, 102, 255));
        jPanel21.setOpaque(false);

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel14.add(jPanel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(152, 482, -1, -1));

        jLabel18.setBackground(new java.awt.Color(255, 255, 255));
        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel18.setText("  Lost Items");
        jLabel18.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel18MouseClicked(evt);
            }
        });
        jPanel14.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 160, 35));

        jLabel19.setBackground(new java.awt.Color(153, 153, 255));
        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel19.setText("  Users");
        jLabel19.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel19.setPreferredSize(new java.awt.Dimension(35, 20));
        jLabel19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel19MouseClicked(evt);
            }
        });
        jPanel14.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 150, 30));

        jPanel9.setBackground(new java.awt.Color(0, 51, 255));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 160, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 35, Short.MAX_VALUE)
        );

        jPanel14.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 160, -1));

        jLabel21.setBackground(new java.awt.Color(153, 153, 255));
        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel21.setText("  Found Items");
        jLabel21.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel21.setPreferredSize(new java.awt.Dimension(35, 20));
        jLabel21.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel21MouseClicked(evt);
            }
        });
        jPanel14.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 150, 35));

        jPanel7.setBackground(new java.awt.Color(0, 39, 117));
        jPanel7.setForeground(new java.awt.Color(51, 51, 51));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/qcu-logo.png"))); // NOI18N

        jLabel22.setBackground(new java.awt.Color(0, 51, 153));
        jLabel22.setFont(new java.awt.Font("Segoe UI Symbol", 1, 24)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("Lost and Found");

        jLabel23.setBackground(new java.awt.Color(0, 51, 153));
        jLabel23.setFont(new java.awt.Font("Segoe UI Symbol", 1, 18)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("ADMIN");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(343, 343, 343)
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(515, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(6, 6, 6))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1114, Short.MAX_VALUE)))
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, 694, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 504, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 768, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void jLabel18MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel18MouseClicked
        new LostItems().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jLabel18MouseClicked

    private void jLabel21MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel21MouseClicked
        new AdminPage().setVisible(true);
        this.dispose();     
    }//GEN-LAST:event_jLabel21MouseClicked

    private void jLabel19MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel19MouseClicked
       
    }//GEN-LAST:event_jLabel19MouseClicked

    private void searchTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchTxtActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //        String query = "SELECT * FROM itemreport WHERE ";
        //
        //        try (Connection conn = DataBase.getConnection(); PreparedStatement pst = conn.prepareStatement(query)) {
            //            ResultSet rs = pst.executeQuery();
            //
            //        } catch (Exception e) {
            //            //
            //            //        }
        String searchText = searchTxt.getText().trim();

        if (searchText.isEmpty()) {
            sorter.setRowFilter(null);  // Show all rows
        } else {
            // Filter across all columns (case-insensitive)
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        searchTxt.setText("");          // clear textfield
        sorter.setRowFilter(null);
    }//GEN-LAST:event_jButton2ActionPerformed

    public static void isLogin() {
        if (user.Session.currentUsername == null) {
            JOptionPane.showMessageDialog(null, "Please login first.");
            new userAuth.Login().setVisible(true);
        } else {
            new AdminPage().setVisible(true);
        }

    }
    
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
        isLogin();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JOptionPane jOptionPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField searchTxt;
    private javax.swing.JTable userTable;
    // End of variables declaration//GEN-END:variables
}
