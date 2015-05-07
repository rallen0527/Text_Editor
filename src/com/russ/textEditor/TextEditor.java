package com.russ.textEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.text.*;

/**
 * Created by Russell on 5/4/2015.
 */
public class TextEditor extends JFrame {

    private JTextArea area = new JTextArea(50, 120);
    private JFileChooser dialog = new JFileChooser(System.getProperty("user.dir"));
    private String currentFile = "Untitled";
    private boolean changed = false;

    private KeyListener k1 = new KeyAdapter() {
        public void keyPressed(KeyEvent e) {
            changed = true;
            Save.setEnabled(true);
            SaveAs.setEnabled(true);
        }
    };

    Action New = new AbstractAction("New", new ImageIcon("Path/image.gif")) {
        public void actionPerformed(ActionEvent e) {
            saveOld();
            area.setText("");
            currentFile = "Untitled";
            setTitle(currentFile);
            changed = false;
            Save.setEnabled(false);
            SaveAs.setEnabled(false);
        }
    };

    Action SaveAs = new AbstractAction("Save as...") {
        public void actionPerformed(ActionEvent e) {
            saveFileAs();
        }
    };

    Action Quit = new AbstractAction("Quit") {
        public void actionPerformed(ActionEvent e) {
            saveOld();
            System.exit(0);
        }
    };

    ActionMap m = area.getActionMap();
    Action Cut = m.get(DefaultEditorKit.cutAction);
    Action Copy = m.get(DefaultEditorKit.copyAction);
    Action Paste = m.get(DefaultEditorKit.pasteAction);

    Action Open = new AbstractAction("Open", new ImageIcon("open.gif")) {
        @Override
        public void actionPerformed(ActionEvent e) {
            saveOld();
            if(dialog.showOpenDialog(null)==JFileChooser.APPROVE_OPTION) {
                readInFile(dialog.getSelectedFile().getAbsolutePath());
            }
            SaveAs.setEnabled(true);
        }
    };

    Action Save = new AbstractAction("Save", new ImageIcon("save.gif")) {
        public void actionPerformed(ActionEvent e) {
            if(!currentFile.equals("Untitled"))
                saveFile(currentFile);
            else
                saveFileAs();
        }
    };

    public TextEditor() {
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(area, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(scroll, BorderLayout.CENTER);
        JMenuBar JMB = new JMenuBar();
        setJMenuBar(JMB);
        JMenu file = new JMenu("File");
        JMenu edit = new JMenu("Edit");
        JMB.add(file); JMB.add(edit);

        file.add(New);file.add(Open);file.add(Save);
        file.add(Quit); file.add(SaveAs);
        file.addSeparator();

        for(int i = 0; i < 4; i++) file.getItem(i).setIcon(null);

        edit.add(Cut);edit.add(Copy);edit.add(Paste);
        edit.getItem(0).setText("Cut out");
        edit.getItem(1).setText("Copy");
        edit.getItem(2).setText("Paste");

        JToolBar toolBar = new JToolBar();
        add(toolBar, BorderLayout.NORTH);
        toolBar.add(New); toolBar.add(Open); toolBar.add(Save);
        toolBar.addSeparator();
        JButton cut = toolBar.add(Cut), cop = toolBar.add(Copy), pas = toolBar.add(Paste);

        cut.setText(null); cut.setIcon(new ImageIcon("cut.gif"));
        cop.setText(null); cop.setIcon(new ImageIcon("cop.gif"));
        pas.setText(null); pas.setIcon(new ImageIcon("paste.gif"));

        Save.setEnabled(false);
        SaveAs.setEnabled(false);




        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        area.addKeyListener(k1);
        setTitle(currentFile);
        setVisible(true);


    }

    private void saveFileAs() {
        if(dialog.showSaveDialog(null)==JFileChooser.APPROVE_OPTION)
            saveFile(dialog.getSelectedFile().getAbsolutePath());
    }

    private void saveOld() {
        if(changed) {
            if(JOptionPane.showConfirmDialog(this, "Would you like to save "+ currentFile +" ?","Save",JOptionPane.YES_NO_OPTION)== JOptionPane.YES_OPTION)
                saveFile(currentFile);
        }
    }

    private void readInFile(String fileName) {
        try {
            FileReader r = new FileReader(fileName);
            area.read(r,null);
            r.close();
            currentFile = fileName;
            setTitle(currentFile);
            changed = false;
        }
        catch(IOException e) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(this,"Editor can't find the file called "+fileName);
        }
    }

    private void saveFile(String fileName) {
        try {
            FileWriter w = new FileWriter(fileName);
            area.write(w);
            w.close();
            currentFile = fileName;
            setTitle(currentFile);
            changed = false;
            Save.setEnabled(false);
        }
        catch(IOException e) {
        }
    }

    public  static void main(String[] arg) {
        new TextEditor();
    }
}



