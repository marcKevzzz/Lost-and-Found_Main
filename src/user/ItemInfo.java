/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package user;

import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * @author QCU
 */

public final class ItemInfo extends javax.swing.JPanel {

    /**
     * Creates new form ItemInfo
     * @param ic
     * @param txts
     */
    public ItemInfo(ImageIcon ic, String txts) {
        initComponents();
        setInfo(ic, txts);
        this.originalIcon = ic;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pic = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txt = new javax.swing.JTextArea();

        setBackground(new java.awt.Color(255, 255, 255));

        pic.setPreferredSize(new java.awt.Dimension(180, 180));

        txt.setEditable(false);
        txt.setBackground(new java.awt.Color(255, 255, 255));
        txt.setColumns(20);
        txt.setRows(5);
        txt.setBorder(null);
        jScrollPane1.setViewportView(txt);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pic, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addComponent(pic, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
  private final ImageIcon originalIcon;

    public void setInfo(ImageIcon icon, String description) {
       
        txt.setText(description);
       

        // Listen for when picLabel is sized (after pack()/show())
        pic.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                rescaleIcon();
            }
        });

        // Try an initial scale (in case it's already sized)
        rescaleIcon();
    }

    private void rescaleIcon() {
        int w = pic.getWidth();
        int h = pic.getHeight();
        // skip if not yet laid out
        if (w <= 0 || h <= 0 || originalIcon == null) {
            return;
        }

        Image img = originalIcon.getImage()
                .getScaledInstance(w, h, Image.SCALE_SMOOTH);
        pic.setIcon(new ImageIcon(img));
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel pic;
    private javax.swing.JTextArea txt;
    // End of variables declaration//GEN-END:variables
}
