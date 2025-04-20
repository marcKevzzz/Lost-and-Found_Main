/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package admin;

import DBConnection.DataBase;
import static DBConnection.DataBase.closeConnection;
import Table.TableActionCellEditor;
import Table.ImageCellEditor;
import Table.TableActionCellRender;
import Table.TableActionEvent;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import jnafilechooser.api.JnaFileChooser;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Arrays;
import java.util.EventObject;
import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;
import user.Session;

/**
 *
 * @author user
 */
public final class AdminPage extends javax.swing.JFrame {

    /**
     * Creates new form AdminPage
     */
    public AdminPage() {
        initComponents();
       
        time.addActionListener((ActionEvent ae) -> {
            lostFoundTable.setValueAt(j2.getText() + ":00", crows, column);

        });

        TableActionEvent event = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                isEditable = !isEditable;
                imageEditor.setEditable(isEditable);

                DefaultTableModel currentModel = (DefaultTableModel) lostFoundTable.getModel();

                DefaultTableModel model = new DefaultTableModel() {
                    @Override
                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        if (columnIndex == 12) {
                            return true;
                        }
                        return isEditable && columnIndex != 6;
                    }

                    @Override
                    public Class<?> getColumnClass(int columnIndex) {
                        return columnIndex == 1 ? ImageIcon.class : String.class;
                    }
                };

                // Set column headers
                model.setColumnIdentifiers(new Object[]{
                    "", "Image attach", "Item Name", "Category", "Location", "Date Lost", "Time Lost",
                    "Description", "Name", "Year & Sec", "Email", "Phone", "Action"
                });

                // Copy current data to new model
                for (int i = 0; i < currentModel.getRowCount(); i++) {
                    Vector<?> rowData = (Vector<?>) currentModel.getDataVector().elementAt(i);
                    model.addRow((Vector<?>) rowData.clone());
                }

                // Apply model
                lostFoundTable.setModel(model);

                // Restore custom editors and renderers
                lostFoundTable.getColumnModel().getColumn(12).setCellRenderer(new TableActionCellRender());
                lostFoundTable.getColumnModel().getColumn(12).setCellEditor(new TableActionCellEditor(this));
                lostFoundTable.getColumnModel().getColumn(1).setCellEditor(imageEditor);

                // Set DateCellEditor only when editable
                if (isEditable) {
                    lostFoundTable.getColumnModel().getColumn(5).setCellEditor(new Table.DateCellEditor(lostFoundTable));
                }

                // Hide the ID column
                TableColumn firstColumn = lostFoundTable.getColumnModel().getColumn(0);
                firstColumn.setMinWidth(0);
                firstColumn.setMaxWidth(0);
                firstColumn.setPreferredWidth(0);
                firstColumn.setResizable(false);

                // TextField Editor
                lostFoundTable.setDefaultEditor(Object.class, new DefaultCellEditor(new JTextField()) {
                    @Override
                    public boolean isCellEditable(EventObject e) {
                        return isEditable;
                    }
                });

                // Save to DB when disabling edit mode
                if (!isEditable) {
                    int reportId = (int) lostFoundTable.getValueAt(row, 0); // Assuming hidden ID column
                    String itemName = lostFoundTable.getValueAt(row, 2).toString();
                    String category = lostFoundTable.getValueAt(row, 3).toString();
                    String location = lostFoundTable.getValueAt(row, 4).toString();
                    String dateLost = lostFoundTable.getValueAt(row, 5).toString();
                    String timeLost = lostFoundTable.getValueAt(row, 6).toString();
                    String description = lostFoundTable.getValueAt(row, 7).toString();
                    String name = lostFoundTable.getValueAt(row, 8).toString();
                    String yearSec = lostFoundTable.getValueAt(row, 9).toString();
                    String email = lostFoundTable.getValueAt(row, 10).toString();
                    String phone = lostFoundTable.getValueAt(row, 11).toString();
                    ImageIcon icon = (ImageIcon) lostFoundTable.getValueAt(row, 1);
                    byte[] image = imageIconToBytes(icon);

                    try (Connection conn = DataBase.getConnection()) {
                        String updateQuery = "UPDATE itemReport SET itemName=?, category=?, location=?, dateLost=?, timeLost=?, description=?, name=?, yearSec=?, email=?, phone=?, imageAttach=? WHERE id=?";
                        PreparedStatement pst = conn.prepareStatement(updateQuery);

                        pst.setString(1, itemName);
                        pst.setString(2, category);
                        pst.setString(3, location);
                        pst.setString(4, dateLost);
                        pst.setString(5, timeLost);
                        pst.setString(6, description);
                        pst.setString(7, name);
                        pst.setString(8, yearSec);
                        pst.setString(9, email);
                        pst.setString(10, phone);
                        pst.setBytes(11, image); // Set image
                        pst.setInt(12, reportId);

                        int rowsUpdated = pst.executeUpdate();
                        if (rowsUpdated > 0) {
                            JOptionPane.showMessageDialog(null, "Item successfully updated!");
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onDelete(int row) {

                if (lostFoundTable.isEditing()) {
                    lostFoundTable.getCellEditor().stopCellEditing();
                }

                int confirm = JOptionPane.showConfirmDialog(
                        jOptionPane,
                        "Do you want to delete this row?",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        // Get the selected row's ID (assumes ID is in column 0)
                        int id = (int) lostFoundTable.getValueAt(row, 0);

                        String sql = "DELETE FROM itemreport WHERE id = ?";

                        try (Connection conn = DataBase.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

                            stmt.setInt(1, id);
                            int deleted = stmt.executeUpdate();

                            if (deleted > 0) {
                                DefaultTableModel model = (DefaultTableModel) lostFoundTable.getModel();
                                model.removeRow(row);

                                JOptionPane.showMessageDialog(jOptionPane, "Row deleted successfully.");
                            } else {
                                JOptionPane.showMessageDialog(jOptionPane, "No row was deleted from the database.");
                            }

                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(jOptionPane, "Ito nga Error: " + e.getMessage());
                    }
                }

            }

            @Override
            public void onView(int row) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }

        };

        lostFoundTable.getColumnModel().getColumn(12).setCellRenderer(new TableActionCellRender());
        lostFoundTable.getColumnModel().getColumn(12).setCellEditor(new TableActionCellEditor(event));
        
        DefaultTableModel model = (DefaultTableModel) lostFoundTable.getModel();
        sorter = new TableRowSorter<>(model);
        lostFoundTable.setRowSorter(sorter);
        
        
    }
    
    
    private TableRowSorter<DefaultTableModel> sorter;
    private ImageCellEditor imageEditor;
    private boolean isEditable = false;
    int column, crows = 0;
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        time = new com.raven.swing.TimePicker();
        date = new com.raven.datechooser.DateChooser();
        jOptionPane = new javax.swing.JOptionPane();
        j2 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lostFoundTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        searchTxt = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel9 = new javax.swing.JLabel();

        time.setDisplayText(j2);
        time.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                timeMouseClicked(evt);
            }
        });

        date.setAlignmentX(100.0F);
        date.setAlignmentY(100.0F);
        date.setDateFormat("yyyy-MM-dd");
        date.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dateMouseClicked(evt);
            }
        });
        date.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                dateComponentHidden(evt);
            }
        });

        j2.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                j2InputMethodTextChanged(evt);
            }
        });
        j2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                j2ActionPerformed(evt);
            }
        });
        j2.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                j2PropertyChange(evt);
            }
        });
        j2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                j2KeyReleased(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1000, 660));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jPanel1.setPreferredSize(new java.awt.Dimension(1200, 700));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel3.setMinimumSize(new java.awt.Dimension(1200, 700));
        jPanel3.setPreferredSize(new java.awt.Dimension(1200, 540));
        jPanel3.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jPanel3ComponentShown(evt);
            }
        });
        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setPreferredSize(new java.awt.Dimension(380, 60));
        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 20, 15));

        jLabel7.setFont(new java.awt.Font("Segoe UI Variable", 0, 18)); // NOI18N
        jLabel7.setLabelFor(jButton3);
        jLabel7.setText("ITEM REPORT TABLE");
        jPanel4.add(jLabel7);

        jButton3.setText("Add Report");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton3);

        jPanel3.add(jPanel4, java.awt.BorderLayout.PAGE_START);

        jPanel5.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setPreferredSize(new java.awt.Dimension(1200, 640));

        lostFoundTable.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        lostFoundTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "", "Image attachment", "Item Name", "Category", "Location", "Date Lost", "Time Lost", "Description", "Full Name", "Year & Sec", "Email", "Phone", "Action"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        lostFoundTable.setPreferredSize(new java.awt.Dimension(1200, 540));
        lostFoundTable.setRowHeight(100);
        lostFoundTable.setShowGrid(false);
        lostFoundTable.setUpdateSelectionOnSort(false);
        jScrollPane1.setViewportView(lostFoundTable);
        if (lostFoundTable.getColumnModel().getColumnCount() > 0) {
            lostFoundTable.getColumnModel().getColumn(0).setResizable(false);
            lostFoundTable.getColumnModel().getColumn(0).setPreferredWidth(0);
            lostFoundTable.getColumnModel().getColumn(1).setResizable(false);
            lostFoundTable.getColumnModel().getColumn(1).setPreferredWidth(70);
            lostFoundTable.getColumnModel().getColumn(2).setResizable(false);
            lostFoundTable.getColumnModel().getColumn(2).setPreferredWidth(60);
            lostFoundTable.getColumnModel().getColumn(3).setResizable(false);
            lostFoundTable.getColumnModel().getColumn(3).setPreferredWidth(60);
            lostFoundTable.getColumnModel().getColumn(4).setResizable(false);
            lostFoundTable.getColumnModel().getColumn(4).setPreferredWidth(60);
            lostFoundTable.getColumnModel().getColumn(5).setResizable(false);
            lostFoundTable.getColumnModel().getColumn(5).setPreferredWidth(100);
            lostFoundTable.getColumnModel().getColumn(6).setResizable(false);
            lostFoundTable.getColumnModel().getColumn(6).setPreferredWidth(60);
            lostFoundTable.getColumnModel().getColumn(7).setResizable(false);
            lostFoundTable.getColumnModel().getColumn(7).setPreferredWidth(80);
            lostFoundTable.getColumnModel().getColumn(8).setResizable(false);
            lostFoundTable.getColumnModel().getColumn(8).setPreferredWidth(70);
            lostFoundTable.getColumnModel().getColumn(9).setResizable(false);
            lostFoundTable.getColumnModel().getColumn(10).setResizable(false);
            lostFoundTable.getColumnModel().getColumn(11).setResizable(false);
            lostFoundTable.getColumnModel().getColumn(12).setResizable(false);
        }

        jPanel5.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setMinimumSize(new java.awt.Dimension(1200, 160));
        jPanel2.setPreferredSize(new java.awt.Dimension(1200, 130));
        jPanel2.setLayout(null);

        searchTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchTxtActionPerformed(evt);
            }
        });
        jPanel2.add(searchTxt);
        searchTxt.setBounds(90, 40, 480, 40);

        jButton1.setText("Search");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1);
        jButton1.setBounds(580, 40, 120, 40);

        jLabel8.setFont(new java.awt.Font("Segoe UI Variable", 0, 18)); // NOI18N
        jLabel8.setText("<html><body><a href=''>Logout</a></body></html>");
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel8);
        jLabel8.setBounds(1120, 10, 70, 25);

        jButton2.setText("Clear");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton2);
        jButton2.setBounds(710, 40, 120, 40);

        jSeparator1.setMinimumSize(new java.awt.Dimension(1000, 10));
        jPanel2.add(jSeparator1);
        jSeparator1.setBounds(0, 120, 1200, 10);

        jLabel9.setFont(new java.awt.Font("Segoe UI Variable", 0, 18)); // NOI18N
        jLabel9.setText("Search");
        jPanel2.add(jLabel9);
        jLabel9.setBounds(20, 50, 70, 25);

        jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown

        imageEditor = new ImageCellEditor(lostFoundTable);
        lostFoundTable.getColumnModel().getColumn(1).setCellEditor(imageEditor);

        TableColumnModel columnModel = lostFoundTable.getColumnModel();
        TableColumn firstColumn = columnModel.getColumn(0); // 0 = first column

        firstColumn.setMinWidth(0);
        firstColumn.setMaxWidth(0);
        firstColumn.setPreferredWidth(0);
        firstColumn.setResizable(false);

        time.set24hourMode(true);

        String query = "SELECT id, category, itemName, location, dateLost, timeLost, description, imageAttach, name, yearSec, email, phone FROM itemReport";

        try (Connection conn = DataBase.getConnection();
            PreparedStatement pst = conn.prepareStatement(query)) {
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = (DefaultTableModel) lostFoundTable.getModel();
            model.setRowCount(0); // Clear table

            while (rs.next()) {
                byte[] imgBytes = rs.getBytes("imageAttach");
                ImageIcon icon = null;

                if (imgBytes != null) {
                    ImageIcon originalIcon = new ImageIcon(imgBytes);
                    Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    icon = new ImageIcon(scaledImage);
                }

                Object[] rows = new Object[]{
                    rs.getInt("id"),
                    icon,
                    rs.getString("itemName"),
                    rs.getString("category"),
                    rs.getString("location"),
                    rs.getString("dateLost"),
                    rs.getString("timeLost"),
                    rs.getString("description"),
                    rs.getString("name"),
                    rs.getString("yearSec"),
                    rs.getString("email"),
                    rs.getString("phone")
                };

                model.addRow(rows);
            }

            // Set custom cell renderer AFTER table is populated
            lostFoundTable.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                        boolean isSelected, boolean hasFocus, int row, int column) {
                    if (value instanceof ImageIcon) {
                        JLabel label = new JLabel();
                        label.setIcon((ImageIcon) value);
                        label.setHorizontalAlignment(SwingConstants.CENTER);
                        return label;
                    } else {
                        // Default rendering for non-image values
                        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    }
                }
            });

            lostFoundTable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    crows = lostFoundTable.rowAtPoint(e.getPoint());
                    column = lostFoundTable.columnAtPoint(e.getPoint());

                    // Check if the click is inside a real row (not empty space)
                    if (crows >= 0 && column == 6 && isEditable) {
                        if (lostFoundTable.isEditing()) {
                            lostFoundTable.getCellEditor().stopCellEditing();
                        }

                        // Optional: scroll to make sure it's visible (for better UX)
                        lostFoundTable.setRowSelectionInterval(crows, crows);

                        // Show the time picker at the right position
                        time.showPopup(lostFoundTable, e.getX(), e.getY());
                    }
                }
            });

            closeConnection(conn);
            rs.close();
            pst.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }

    }//GEN-LAST:event_formComponentShown

    private void j2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_j2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_j2ActionPerformed

    private void j2InputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_j2InputMethodTextChanged


    }//GEN-LAST:event_j2InputMethodTextChanged

    private void j2PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_j2PropertyChange

    }//GEN-LAST:event_j2PropertyChange

    private void j2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_j2KeyReleased

    }//GEN-LAST:event_j2KeyReleased

    private void timeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_timeMouseClicked


    }//GEN-LAST:event_timeMouseClicked

    private void dateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dateMouseClicked

    }//GEN-LAST:event_dateMouseClicked

    private void dateComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_dateComponentHidden

    }//GEN-LAST:event_dateComponentHidden

    private void jPanel3ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel3ComponentShown

    }//GEN-LAST:event_jPanel3ComponentShown

    private void searchTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchTxtActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
//           String query = "SELECT * FROM itemreport WHERE ";
//
//        try (Connection conn = DataBase.getConnection(); 
//            PreparedStatement pst = conn.prepareStatement(query)) {
//            ResultSet rs = pst.executeQuery();
//            
//        } catch(Exception e){
//            
//        }
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

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
       new user.ItemReport().setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked
         user.Session.currentUsername = "";
         user.Session.userSchoolId = "";
         JOptionPane.showMessageDialog(this, "Logout Successfully");
         new userAuth.Login().setVisible(true);
         this.dispose();
    }//GEN-LAST:event_jLabel8MouseClicked

    public byte[] imageIconToBytes(ImageIcon icon) {
        try {
            Image img = icon.getImage();
            BufferedImage bufferedImage = new BufferedImage(
                    img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB
            );
            Graphics2D g2d = bufferedImage.createGraphics();
            g2d.drawImage(img, 0, 0, null);
            g2d.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", baos); // or "png"
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void isLogin(){
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
            java.util.logging.Logger.getLogger(AdminPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdminPage().setVisible(true);
            }
        });
         isLogin();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private com.raven.datechooser.DateChooser date;
    private javax.swing.JTextField j2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JOptionPane jOptionPane;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable lostFoundTable;
    private javax.swing.JTextField searchTxt;
    private com.raven.swing.TimePicker time;
    // End of variables declaration//GEN-END:variables
}
