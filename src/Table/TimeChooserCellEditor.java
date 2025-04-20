//package Table;
//
//import java.awt.Component;
//import javax.swing.AbstractCellEditor;
//import javax.swing.JTable;
//import javax.swing.JTextField;
//import javax.swing.table.TableCellEditor;  // Assuming your library has this
//
//public class TimeChooserCellEditor extends AbstractCellEditor implements TableCellEditor {
//
//    private JTextField textField;
//    private JTable table;
//    private int row;
//    private int column;
//    private TimeChooser timeChooser;
//
//    public TimeChooserCellEditor(JTable table, TimeChooser timeChooser) {
//        this.table = table;
//        this.timeChooser = timeChooser;
//
//        textField = new JTextField();
//        textField.setEditable(false);  // Prevent typing
//
//        // Link the time chooser with the text field
//        timeChooser.setTextField(textField);
//
//        textField.addPropertyChangeListener("text", evt -> {
//            table.setValueAt(textField.getText(), row, column);
//            fireEditingStopped();
//        });
//
//        textField.addMouseListener(new java.awt.event.MouseAdapter() {
//            @Override
//            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                timeChooser.showPopup(); // showPopup can also accept location if needed
//            }
//        });
//    }
//
//    @Override
//    public Object getCellEditorValue() {
//        return textField.getText();
//    }
//
//    @Override
//    public Component getTableCellEditorComponent(JTable table, Object value,
//                                                 boolean isSelected, int row, int column) {
//        this.row = row;
//        this.column = column;
//        textField.setText(value != null ? value.toString() : "");
//        return textField;
//    }
//}
