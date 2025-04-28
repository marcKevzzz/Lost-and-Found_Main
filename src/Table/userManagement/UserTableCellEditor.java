/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Table.userManagement;

import Table.TableActionEvent;
import java.awt.Component;
import java.util.function.Supplier;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;

/**
 *
 * @author QCU
 */
public class UserTableCellEditor extends DefaultCellEditor{
     private final UserTableActionEvent event;
    private final Supplier<Integer> editableRowSupplier;
    
    public UserTableCellEditor(UserTableActionEvent event, Supplier<Integer> editableRowSupplier){
          super(new JCheckBox());
        this.event = event;
        this.editableRowSupplier = editableRowSupplier;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        UserCellPanelAction action = new UserCellPanelAction();
        action.initEvent(event, row);
        action.setEditModeIcon(row == editableRowSupplier.get());
        action.setBackground(table.getSelectionBackground());
        return action;
    }
    
}
