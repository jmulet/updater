/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.iesapp.updater;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author Josep
 */
public class FileDownloaderPanel extends javax.swing.JPanel implements Observer {
    protected final FileDownloader fileDownloader;

    /**
     * Creates new form FileDownloaderPanel
     */
    public FileDownloaderPanel(String url, File outputFile) {
        initComponents();
        fileDownloader = new FileDownloader(url, outputFile); 
        fileDownloader.addObserver(this);
        jButton2.setVisible(false);
    }

    //Call only once
    public void download()
    {
        getFileDownloader().download();
        jToggleButton1.setSelected(true);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jProgressBar1 = new javax.swing.JProgressBar();
        jButton1 = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        jButton2 = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/iesapp/updater/icons/stop.png"))); // NOI18N
        jButton1.setToolTipText("Stop");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/iesapp/updater/icons/resume.png"))); // NOI18N
        jToggleButton1.setToolTipText("Pause / Resume");
        jToggleButton1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/org/iesapp/updater/icons/pause.png"))); // NOI18N
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/iesapp/updater/icons/downloaded.png"))); // NOI18N
        jButton2.setToolTipText("Open location");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        if(jToggleButton1.isSelected())
        {
            getFileDownloader().resume();
        }
        else
        {
            getFileDownloader().pause();
        }
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
         getFileDownloader().cancel();
         jProgressBar1.setValue(0);
         jProgressBar1.setString("Cancelled");
         jProgressBar1.setStringPainted(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if(getFileDownloader().getStatus()!=FileDownloader.COMPLETE)
        {
            return;
        }
        File file = getFileDownloader().getOutputFile();
        try {
            Desktop.getDesktop().browse(file.getParentFile().toURI());
        } catch (IOException ex) {
            Logger.getLogger(FileDownloaderPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JToggleButton jToggleButton1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void update(Observable o, Object arg) {
        if(getFileDownloader().getStatus()==FileDownloader.DOWNLOADING)
        {
         if(jProgressBar1.getToolTipText()==null)
         {
             jProgressBar1.setToolTipText(getFileDownloader().getFileInfo());
         }
         jProgressBar1.setValue(getFileDownloader().getProgress());
         jProgressBar1.setStringPainted(true);
         jProgressBar1.setString(getFileDownloader().getProgress()+"%");
        }
        else if(getFileDownloader().getStatus()==FileDownloader.COMPLETE)
        {
         jProgressBar1.setValue(0);
         jProgressBar1.setStringPainted(true);
         jProgressBar1.setString("COMPLETE");
         jToggleButton1.setEnabled(false);
         jButton1.setVisible(false);
         jButton2.setVisible(true);
        }
        else if(getFileDownloader().getStatus()==FileDownloader.ERROR)
        {
         jProgressBar1.setValue(0);
         jProgressBar1.setStringPainted(true);
         jProgressBar1.setString("ERROR");
        }
        else if(getFileDownloader().getStatus()==FileDownloader.PAUSED)
        {
          jProgressBar1.setValue(getFileDownloader().getProgress());
          jProgressBar1.setStringPainted(true);
          jProgressBar1.setString("PAUSED "+getFileDownloader().getProgress()+"%");
        }
        
        
    }
    
    
    public static void main(String[] args)
    {
        JFrame frame = new JFrame();
        File f = new File("c:\\kk.zip");
        FileDownloaderPanel panel = new FileDownloaderPanel("https://dl.dropboxusercontent.com/u/92714512/org-iesapp-modules-reserves-2.0.mod", f);
        frame.setLayout(new BorderLayout());
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        panel.download();
    }

    public FileDownloader getFileDownloader() {
        return fileDownloader;
    }
}