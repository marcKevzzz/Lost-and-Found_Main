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
    public boolean isEditable = false;
    private int column;

    public ImageCellEditor(JTable table) {
        this.table = table;
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (isEditable) {
                    int clickedRow = table.rowAtPoint(e.getPoint());
                    int clickedColumn = table.columnAtPoint(e.getPoint());
                    
                    if (clickedColumn == 1 && clickedRow != -1) {
                        JnaFileChooser ch = new JnaFileChooser();
                        ch.setMode(JnaFileChooser.Mode.Files);
                        ch.addFilter("Image Files", "jpg", "jfif", "jpeg", "png", "gif", "bmp");

                        boolean act = ch.showOpenDialog(null);
                        if (act) {
                            File file = ch.getSelectedFile();
                            String filePath = file.getAbsolutePath();

                            ImageIcon originalIcon = new ImageIcon(filePath);
                            Image originalImage = originalIcon.getImage();
                            int originalWidth = originalImage.getWidth(null);
                            int originalHeight = originalImage.getHeight(null);
                            int targetHeight = 100; // smaller for table
                            int targetWidth = (originalWidth * targetHeight) / originalHeight;

                            Image scaledImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
                            currentIcon = new ImageIcon(scaledImage);

                            // update table model directly
                            table.setValueAt(currentIcon, row, 1);
                        }
                    }
                }
            }
        });
    }

    // Add this inside ImageCellEditor
    public void setEditable(boolean editable) {
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
