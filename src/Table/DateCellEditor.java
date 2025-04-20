package Table;

import com.raven.datechooser.DateChooser;
import com.raven.datechooser.SelectedDate;

import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

public class DateCellEditor extends AbstractCellEditor implements TableCellEditor {

    private JTextField textField;
    private JTable table;
    private int row;
    private int column;
    private DateChooser dateChooser;

    public DateCellEditor(JTable table) {
        this.table = table;
        textField = new JTextField();
        textField.setEditable(false);  // prevent typing
        dateChooser = new DateChooser();
        dateChooser.setTextRefernce(textField);
        dateChooser.setDateFormat("yyyy-MM-dd");

        // When a date is selected, stop editing and update table
        textField.addPropertyChangeListener("text", evt -> {
            table.setValueAt(textField.getText(), row, column);
            fireEditingStopped();
        });

        // Show popup when clicked
        textField.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dateChooser.showPopup();
            }
        });
    }

    @Override
    public Object getCellEditorValue() {
        return textField.getText();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        this.row = row;
        this.column = column;
        textField.setText(value != null ? value.toString() : "");
        return textField;
    }
}