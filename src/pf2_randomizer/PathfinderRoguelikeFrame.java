/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pf2_randomizer;

import java.awt.BorderLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Random;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author pmele
 */
public class PathfinderRoguelikeFrame extends JFrame {
    
    public PathfinderRoguelikeFrame(String str) {
        super(str);
        setLocationRelativeTo(null);
        setSize(1500,750);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        //getContentPane().add(getBottomPanel(), BorderLayout.SOUTH);
        //getContentPane().add(aTextField, BorderLayout.CENTER);
        setJMenuBar(getMyMenuBar());
        //updateTextField();
        setVisible(true);
        
        
        //ELEMENTS TO INCLUDE:
        //A (searchable?) dropdown for categories
        //For Feats, a level range
        //An area to display the generated art.
        //An area to configure the weighting of rarities
        //An area to enter how many options are wanted
        //Possibly an area to display how many total options are available?
        //An area to indicate what prerequisites met or unmet have been saved
        //If ambitious, a seperate pop-up area to enter key details like Ability Scores or skill trainings, for prerequisite purposes
    }
    
    //INCOMPLETE
    private void saveToFile() {
        JFileChooser jfc = new JFileChooser();
        if(jfc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        if(jfc.getSelectedFile() == null) {
            return;
        }
        File chosenFile = jfc.getSelectedFile();

        if(jfc.getSelectedFile().exists()) {
            String message = "File " + jfc.getSelectedFile().getName() + " exists.  Overwrite?";
            if( JOptionPane.showConfirmDialog(this, message) != JOptionPane.YES_OPTION) {
                return;			
            }
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(chosenFile));
            //ACTUALLY WRITE THE PREREQ DATA
            writer.flush();  writer.close();
        } catch(Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Could not write file", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //INCOMPLETE
    private void loadFromFile() {
        JFileChooser jfc = new JFileChooser();
        if( jfc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        if( jfc.getSelectedFile() == null) {
            return;
        }
        File chosenFile = jfc.getSelectedFile();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(chosenFile));
            //ACTUALLY CHECK AND READ PREREQS
        } catch(Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Could not read file", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //INCOMPLETE
    private void mergeGroups() {
        //Actually implement group merging logic
    }
    
    //INCOMPLETE
    private void importGroup() {
        JFileChooser jfc = new JFileChooser();
        if( jfc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        if( jfc.getSelectedFile() == null) {
            return;
        }
        File chosenFile = jfc.getSelectedFile();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(chosenFile));
            //ACTUALLY CHECK AND READ IMPORTED GROUP
        } catch(Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Could not read file", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //INCOMPLETE
    private void configureWeights() {
        //Actually implement weight configuring logic
    }
    
    //INCOMPLETE
    private void clearPrereqs() {
        //Actually implement prereq clearing logic
    }
    
    private JMenuBar getMyMenuBar() {
        JMenuBar jmenuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        jmenuBar.add(fileMenu);

        //JMenu subMenu = new JMenu("SubMenu");
        //fileMenu.add(subMenu);
        //subMenu.add(new JMenuItem("SubItem"));

        JMenuItem saveItem = new JMenuItem("Save");
        fileMenu.add(saveItem);
        saveItem.setMnemonic('S');

        saveItem.addActionListener(new ActionListener()
        {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                        saveToFile();
                }
        });

        JMenuItem openItem = new JMenuItem("Open");
        openItem.setMnemonic('O');
        fileMenu.add(openItem);

        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadFromFile();
            }
        });

        JMenuItem mergeItem = new JMenuItem("Merge");
        openItem.setMnemonic('M');
        fileMenu.add(mergeItem);

        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mergeGroups();
            }
        });
        
        JMenuItem importItem = new JMenuItem("Import");
        openItem.setMnemonic('I');
        fileMenu.add(importItem);

        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                importGroup();
            }
        });
        
        JMenu toolMenu = new JMenu("Tools");
        fileMenu.setMnemonic('T');
        jmenuBar.add(toolMenu);
        
        JMenuItem weightsItem = new JMenuItem("Weights");
        openItem.setMnemonic('W');
        toolMenu.add(weightsItem);

        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                configureWeights();
            }
        });
        
        JMenuItem clearItem = new JMenuItem("Clear");
        openItem.setMnemonic('C');
        toolMenu.add(clearItem);

        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearPrereqs();
            }
        });

        return jmenuBar;
    }
    
    //Organize panels (regions of the display) here
    
    private JPanel getSomePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0,4)); //Rows, Cols
        
        
        
        return panel;
    }
}
