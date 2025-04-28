/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Table;

import java.awt.Component;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;
import jnafilechooser.api.JnaFileChooser;

/**
 *
 * @author QCU
 */
public class ImageCellEditor extends AbstractCellEditor implements TableCellEditor {

    private JLabel imageLabel;
    private ImageIcon currentIcon;
    private int row;
    private JTable table;
    public int isEditable = -1;
    private int column;
    private File selectedFile;

    public ImageCellEditor(JTable table, int col) {
        this.table = table;
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (isEditable != -1) {
                    int clickedRow = table.rowAtPoint(e.getPoint());
                    int clickedColumn = table.columnAtPoint(e.getPoint());
                    
                    if (clickedColumn == col && clickedRow != -1 && clickedRow == row) {
                        JnaFileChooser ch = new JnaFileChooser();
                        ch.setMode(JnaFileChooser.Mode.Files);
                        ch.addFilter("Image Files", "jpg", "jfif", "jpeg", "png", "gif", "bmp");

                        boolean act = ch.showOpenDialog(null);
                        if (act) {
                            File file = ch.getSelectedFile();
                            String filePath = file.getAbsolutePath();
                            selectedFile = file;
                            
                            ImageIcon originalIcon = new ImageIcon(filePath);
                            Image originalImage = originalIcon.getImage();
                            int originalWidth = originalImage.getWidth(null);
                            int originalHeight = originalImage.getHeight(null);
                            int targetHeight = 100; // smaller for table
                            int targetWidth = (originalWidth * targetHeight) / originalHeight;

                            Image scaledImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
                            currentIcon = new ImageIcon(scaledImage);

                            // update table model directly
                            table.setValueAt(currentIcon, row, col);
                        }
                    }
                }
            }
        });
    }
    
    public File getSelectedFile() {
        return selectedFile;
    }
    
    public byte[] getSelectedFileBytes() {
        if (selectedFile != null) {
            try {
                return java.nio.file.Files.readAllBytes(selectedFile.toPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    // Add this inside ImageCellEditor
    public void setEditable(int editable) {
        this.isEditable = editable;
    }

    @Override
    public Object getCellEditorValue() {
        return currentIcon;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        this.row = row;
        this.column = column;
        if (value instanceof ImageIcon) {
            currentIcon = (ImageIcon) value;
            imageLabel.setIcon(currentIcon);
        }
        return imageLabel;
    }
}
