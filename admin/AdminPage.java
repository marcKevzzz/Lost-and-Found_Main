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
import java.awt.Color;
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
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.TableModelEvent;
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

        lostFoundTable.getColumnModel().getColumn(15).setCellEditor(
                new TableActionCellEditor(
                        new TableActionEvent() {
                    @Override
                    public void onEdit(int row) {
                        // If another row was being edited, save it first
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
                        DefaultTableModel currentModel = (DefaultTableModel) lostFoundTable.getModel();

                        DefaultTableModel model = new DefaultTableModel() {
                            @Override
                            public boolean isCellEditable(int rowIndex, int columnIndex) {
                                if (columnIndex == 15) {
                                    return true;
                                }
                                return rowIndex == editableRowIndex && columnIndex != 7;
                            }

                            @Override
                            public Class<?> getColumnClass(int columnIndex) {
                                return columnIndex == 2 ? ImageIcon.class : String.class;
                            }
                        };

                        model.setColumnIdentifiers(new Object[]{
                            "", "", "Image attach", "Item Name", "Category", "Location", "Date Lost", "Time Lost",
                            "Description", "Name", "", "Year & Sec", "Email", "Phone", "", "Action",});

                        for (int i = 0; i < currentModel.getRowCount(); i++) {
                            Vector<?> rowData = (Vector<?>) currentModel.getDataVector().elementAt(i);
                            model.addRow((Vector<?>) rowData.clone());
                        }

                        lostFoundTable.setModel(model);

                        // Set renderers and editors again
                        lostFoundTable.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
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

                        imageEditor = new ImageCellEditor(lostFoundTable, 2);
                        imageEditor.setEditable(editableRowIndex); // or any logic that sets it as editable
                        lostFoundTable.getColumnModel().getColumn(2).setCellEditor(imageEditor);

                        lostFoundTable.getColumnModel().getColumn(15).setCellRenderer(
                                new TableActionCellRender(() -> editableRowIndex)
                        );

                        lostFoundTable.getColumnModel().getColumn(15).setCellEditor(
                                new TableActionCellEditor(this, () -> editableRowIndex)
                        );

                        if (editableRowIndex != -1) {
                            lostFoundTable.getColumnModel().getColumn(6).setCellEditor(new Table.DateCellEditor(lostFoundTable));
                        }

                        // Hide ID columns
                        hideColumn(0);
                        hideColumn(1);
                        hideColumn(10);
                        hideColumn(14);
                    }

                    private void saveRowData(int row) {
                        try {
                            int Id = (int) lostFoundTable.getValueAt(row, 0);
                            String itemName = getSafeString(row, 3);
                            String category = getSafeString(row, 4);
                            String location = getSafeString(row, 5);
                            String dateLost = getSafeString(row, 6);
                            String timeLost = getSafeString(row, 7);
                            String description = getSafeString(row, 8);
                            String name = getSafeString(row, 9);
                            String yearSec = getSafeString(row, 11);
                            String email = getSafeString(row, 12);
                            String phone = getSafeString(row, 13);

                            File selectedFile = imageEditor.getSelectedFile();
                            InputStream image = null;
                            boolean hasImage = false;

                            if (selectedFile != null && selectedFile.exists()) {
                                image = new FileInputStream(selectedFile);
                                hasImage = true;
                            }

                            try (Connection conn = DataBase.getConnection()) {
                                String updateQuery = hasImage
                                        ? "UPDATE itemfound SET itemName=?, category=?, location=?, dateLost=?, timeLost=?, description=?, name=?, yearSec=?, email=?, phone=?, imageAttach=? WHERE id=?"
                                        : "UPDATE itemfound SET itemName=?, category=?, location=?, dateLost=?, timeLost=?, description=?, name=?, yearSec=?, email=?, phone=? WHERE id=?";

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

                                if (hasImage) {
                                    pst.setBlob(11, image);
                                    pst.setInt(12, Id);
                                } else {
                                    pst.setInt(11, Id);
                                }

                                int rowsUpdated = pst.executeUpdate();
                                if (rowsUpdated > 0) {
                                    JOptionPane.showMessageDialog(null, "Item successfully updated!");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage());
                        }
                    }

                    private String getSafeString(int row, int col) {
                        Object val = lostFoundTable.getValueAt(row, col);
                        return val != null ? val.toString() : "";
                    }

                    @Override
                    public void onStatus(int row) {
                        int itemId = (int) lostFoundTable.getValueAt(row, 0); // assuming first column is ID
                        String currentStatus = lostFoundTable.getValueAt(row, 1).toString();

                        String newStatus = null;
                        Color color;

                        // Confirmation and new status
                        if (currentStatus.equalsIgnoreCase("Pending")) {
                            int confirm = JOptionPane.showConfirmDialog(null,
                                    "Mark this item as Posted?", "Confirm Status Change",
                                    JOptionPane.YES_NO_OPTION);
                            if (confirm == JOptionPane.YES_OPTION) {
                                newStatus = "Posted";
                                color = new Color(144, 238, 144); // Light green
                            } else {
                                return;
                            }
                        } else if (currentStatus.equalsIgnoreCase("Posted")) {
                            int confirm = JOptionPane.showConfirmDialog(null,
                                    "Mark this item as Pending?", "Confirm Status Change",
                                    JOptionPane.YES_NO_OPTION);
                            if (confirm == JOptionPane.YES_OPTION) {
                                newStatus = "Pending";
                                color = lostFoundTable.getBackground();
                            } else {
                                return;
                            }
                        } else {
                            JOptionPane.showConfirmDialog(null, "Can't modify");
                            return;
                        }

                        // Update the database
                        try (Connection conn = DBConnection.DataBase.getConnection()) {
                            String updateQuery = "UPDATE itemfound SET status = ? WHERE id = ?";
                            PreparedStatement pst = conn.prepareStatement(updateQuery);
                            pst.setString(1, newStatus); // e.g., "Accepted"
                            pst.setInt(2, itemId);
                            int updated = pst.executeUpdate();

                            if (updated > 0) {
                                lostFoundTable.setValueAt(newStatus, row, 1);

                                lostFoundTable.repaint();
                                JOptionPane.showMessageDialog(null, "Status updated to " + newStatus);
                            } else {
                                JOptionPane.showMessageDialog(null, "Database update failed.", "Error", JOptionPane.ERROR_MESSAGE);
                            }

                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }

                    }

                    @Override
                    public void onView(int row) {

                        int userId = getUserIdFromRow(row);

                        ViewClaim claim = new ViewClaim(userId);
                        // the form-generated panel
                        jDialog1.setContentPane(claim);
                        jDialog1.pack();
                        jDialog1.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width) / 2 - getWidth() / 2, (Toolkit.getDefaultToolkit().getScreenSize().height) / 2 - getHeight() / 2);
                        jDialog1.setVisible(true);
                        System.out.println(userId);

                    }
                },
                        () -> editableRowIndex // <-- this passes the current editable row
                )
        );

        lostFoundTable.getColumnModel().getColumn(15).setCellRenderer(
                new TableActionCellRender(() -> editableRowIndex)
        );

        DefaultTableModel model = (DefaultTableModel) lostFoundTable.getModel();
        sorter = new TableRowSorter<>(model);
        lostFoundTable.setRowSorter(sorter);

//         r.addWindowListener(new java.awt.event.WindowAdapter() {
//            @Override
//            public void windowClosed(java.awt.event.WindowEvent e) {
//                // 🔁 Refresh your table or panel here
//                formComponentShown(e); // or your refresh method
//            }
//        });
    }

    private final TableRowSorter<DefaultTableModel> sorter;
    private ImageCellEditor imageEditor;
    int column, crows = 0;
    private int editableRowIndex = -1;
//    user.ItemReport r = new user.ItemReport();

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
        jDialog1 = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
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
        jPanel6 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();

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
        setMinimumSize(new java.awt.Dimension(1300, 660));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(1300, 700));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setMinimumSize(new java.awt.Dimension(1100, 500));
        jPanel3.setPreferredSize(new java.awt.Dimension(1200, 565));
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
        jLabel7.setText("ITEM FOUND REPORT TABLE");
        jPanel4.add(jLabel7);

        jPanel3.add(jPanel4, java.awt.BorderLayout.PAGE_START);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setPreferredSize(new java.awt.Dimension(1200, 0));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setPreferredSize(new java.awt.Dimension(1133, 0));

        lostFoundTable.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        lostFoundTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "id", "status", "Image attachment", "Item Name", "Category", "Location", "Date Lost", "Time Lost", "Description", "Full Name", "Student Number", "Year & Sec", "Email", "Phone", "reportId", "Action"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        lostFoundTable.setToolTipText("");
        lostFoundTable.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        lostFoundTable.setRequestFocusEnabled(false);
        lostFoundTable.setRowHeight(100);
        lostFoundTable.setRowMargin(2);
        lostFoundTable.setShowGrid(false);
        lostFoundTable.setUpdateSelectionOnSort(false);
        jScrollPane1.setViewportView(lostFoundTable);
        if (lostFoundTable.getColumnModel().getColumnCount() > 0) {
            lostFoundTable.getColumnModel().getColumn(0).setResizable(false);
            lostFoundTable.getColumnModel().getColumn(0).setPreferredWidth(1);
            lostFoundTable.getColumnModel().getColumn(1).setResizable(false);
            lostFoundTable.getColumnModel().getColumn(1).setPreferredWidth(1);
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
            lostFoundTable.getColumnModel().getColumn(13).setResizable(false);
            lostFoundTable.getColumnModel().getColumn(13).setPreferredWidth(100);
        }

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1154, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 524, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 57, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setMinimumSize(new java.awt.Dimension(1120, 130));
        jPanel2.setPreferredSize(new java.awt.Dimension(1120, 130));
        jPanel2.setLayout(null);

        searchTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchTxtActionPerformed(evt);
            }
        });
        jPanel2.add(searchTxt);
        searchTxt.setBounds(320, 10, 480, 30);

        jButton1.setText("Search");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1);
        jButton1.setBounds(810, 10, 120, 30);

        jLabel8.setFont(new java.awt.Font("Segoe UI Variable", 0, 18)); // NOI18N
        jLabel8.setText("<html><body><a href=''>Logout</a></body></html>");
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel8);
        jLabel8.setBounds(1080, 10, 70, 25);

        jButton2.setText("Clear");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton2);
        jButton2.setBounds(940, 10, 120, 30);

        jSeparator1.setMinimumSize(new java.awt.Dimension(1000, 10));
        jPanel2.add(jSeparator1);
        jSeparator1.setBounds(0, 120, 1200, 10);

        jLabel9.setFont(new java.awt.Font("Segoe UI Variable", 0, 18)); // NOI18N
        jLabel9.setText("Search");
        jPanel2.add(jLabel9);
        jLabel9.setBounds(250, 10, 70, 25);

        jPanel6.setBackground(new java.awt.Color(0, 51, 153));
        jPanel6.setMinimumSize(new java.awt.Dimension(100, 100));
        jPanel6.setPreferredSize(new java.awt.Dimension(82, 700));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel8.setBackground(new java.awt.Color(0, 51, 255));
        jPanel8.setMinimumSize(new java.awt.Dimension(32, 20));
        jPanel8.setOpaque(false);
        jPanel8.setLayout(new java.awt.BorderLayout());
        jPanel6.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(126, 105, -1, 35));

        jPanel17.setBackground(new java.awt.Color(102, 102, 255));
        jPanel17.setOpaque(false);

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel6.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(126, 100, -1, -1));

        jPanel18.setBackground(new java.awt.Color(102, 102, 255));
        jPanel18.setOpaque(false);

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel6.add(jPanel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(152, 482, -1, -1));

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
        jPanel6.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 150, 35));

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

        jPanel6.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 160, -1));

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
        jPanel6.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 150, 30));

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
        jPanel6.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 160, 35));

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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 1160, Short.MAX_VALUE)))
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 694, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        checkUserLogin(this);
        lostFoundTable.getColumnModel().getColumn(1).setCellEditor(imageEditor);

        hideColumn(0);
        hideColumn(1);
        hideColumn(10);
        hideColumn(14);

        time.set24hourMode(true);

        String query = "SELECT * FROM itemfound";

        try (Connection conn = DataBase.getConnection(); PreparedStatement pst = conn.prepareStatement(query); ResultSet rs = pst.executeQuery()) {

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
                    rs.getString("status"),
                    icon,
                    rs.getString("itemName"),
                    rs.getString("category"),
                    rs.getString("location"),
                    rs.getString("dateLost"),
                    rs.getString("timeLost"),
                    rs.getString("description"),
                    rs.getString("name"),
                    rs.getString("studentNum"),
                    rs.getString("yearSec"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("reportId")

                };

                model.addRow(rows);
            }

            // Set custom cell renderer AFTER table is populated
            lostFoundTable.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
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
                    if (column == 7 && editableRowIndex != -1) {
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

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }

        lostFoundTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus,
                    int rowIndex, int columnIndex) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, rowIndex, columnIndex);

                if (isSelected) {
                    // Use the selection background and foreground colors for selected rows
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                } else {
                    // Get the status from the second column (assuming index 1)
                    String statusObj = table.getValueAt(rowIndex, 1).toString();

                    // Apply custom background colors based on status
                    if (statusObj.equalsIgnoreCase("Posted")) {
                        c.setBackground(new Color(89, 146, 201)); // Accepted
                        c.setForeground(Color.BLACK); // Ensure black text
                    } else if (statusObj.equalsIgnoreCase("Claimed")) {
                        c.setBackground(new Color(45, 226, 0)); // Claimed
                        c.setForeground(Color.BLACK); // Ensure black text
                    } else if (statusObj.equalsIgnoreCase("Claim Pending")) {
                        c.setBackground(new Color(255, 255, 102)); // Pending Claim
                        c.setForeground(Color.BLACK); // Ensure black text
                    } else if (statusObj.equalsIgnoreCase("Pending")) {
                        c.setBackground(new Color(255, 204, 0));
                        c.setForeground(Color.BLACK);
                    } else if (statusObj.equalsIgnoreCase("Claim Requested")) {
                        c.setBackground(new Color(128, 0, 128));
                        c.setForeground(Color.BLACK);
                    } else if (statusObj.equalsIgnoreCase("Claim Approved")) {
                        c.setBackground(new Color(168, 168, 168));
                        c.setForeground(Color.BLACK);
                    } else if (statusObj.equalsIgnoreCase("Cancel Claimed")) {
                        c.setBackground(new Color(255, 41, 41));
                        c.setForeground(Color.BLACK);
                    } else {
                        c.setBackground(table.getBackground()); // Default background for unselected rows
                        c.setForeground(Color.BLACK); // Default text color
                    }
                }

                return c;
            }
        });


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

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked
        user.Session.currentUsername = null;
        user.Session.userId = null;
        user.Session.userSchoolId = null;
        JOptionPane.showMessageDialog(this, "Logout Successfully");
        new userAuth.Login().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jLabel8MouseClicked

    private void jLabel18MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel18MouseClicked
        new LostItems().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jLabel18MouseClicked

    private void jLabel21MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel21MouseClicked

    }//GEN-LAST:event_jLabel21MouseClicked

    private void jLabel19MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel19MouseClicked
        new UserManagement().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jLabel19MouseClicked

    public byte[] imageIconToBytes(ImageIcon icon) {
        if (icon instanceof ImageIcon) {
            try {
                BufferedImage bufferedImage = new BufferedImage(
                        icon.getIconWidth(),
                        icon.getIconHeight(),
                        BufferedImage.TYPE_INT_ARGB
                );

                // Paint the Icon into the BufferedImage
                icon.paintIcon(null, bufferedImage.getGraphics(), 0, 0);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "png", baos); // you can change "png" to "jpg" if needed
                return baos.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    public boolean checkUserLogin(JFrame parentFrame) {
        if (Session.currentUsername == null) {
            int choice = JOptionPane.showConfirmDialog(
                    parentFrame,
                    "You are not logged in. Do you want to login first?",
                    "Login Required",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (choice == JOptionPane.YES_OPTION) {
                parentFrame.dispose(); // Only close if they choose to log in
                new userAuth.Login().setVisible(true);
                return false; // Allow continuing if logged in
            } else if (choice == JOptionPane.NO_OPTION || choice == JOptionPane.CLOSED_OPTION) {
                parentFrame.dispose(); // Only close if they choose to log in
                new userAuth.Login().setVisible(true);
                return false; // Block further action
            }
        }
        return true;

    }

    public int getUserIdFromRow(int row) {
        // Assuming tableModel is your JTable's model
        return (int) lostFoundTable.getValueAt(row, 0); // Column 0 holds user ID
    }

    private void hideColumn(int index) {
        TableColumn col = lostFoundTable.getColumnModel().getColumn(index);
        col.setMinWidth(0);
        col.setMaxWidth(0);
        col.setPreferredWidth(0);
        col.setResizable(false);
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
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private com.raven.datechooser.DateChooser date;
    private javax.swing.JTextField j2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JOptionPane jOptionPane;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable lostFoundTable;
    private javax.swing.JTextField searchTxt;
    private com.raven.swing.TimePicker time;
    // End of variables declaration//GEN-END:variables
}
