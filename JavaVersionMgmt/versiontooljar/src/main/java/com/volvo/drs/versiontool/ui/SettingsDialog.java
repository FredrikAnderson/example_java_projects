package com.volvo.drs.versiontool.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.volvo.drs.versiontool.Settings;
import com.volvo.drs.versiontool.VersionTool;
import com.volvo.drs.versiontool.model.VolvoEnv;
import com.volvo.drs.versiontool.model.WASEnv;

/**
 * Settings dialog.
 */
public class SettingsDialog extends JDialog {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private JLabel localTempDirLbl = new JLabel("Local temp dir:");
    
    private String[] tempDirs = { "C:\\Temp\\", "H:\\Temp\\" };
    private JComboBox localTempDirsCbx = new JComboBox(tempDirs);

    private JCheckBox checkNewJenkinsBuildsCbx = new JCheckBox("Check new Jenkins builds");

    private Collection<JCheckBox> volvoEnvChks = new ArrayList<JCheckBox>();
    private JTextField intervalCheck = new JTextField(20);
    
    private JButton okBtn = new JButton("OK");
    private JButton cancelBtn = new JButton("Cancel");
    
    public SettingsDialog() {

        setLayout(new BorderLayout());

        JPanel tempDirPnl = new JPanel(new FlowLayout());
        tempDirPnl.add(localTempDirLbl);
        tempDirPnl.add(localTempDirsCbx);

        JPanel checkJenkinsBuildsPnl = new JPanel(new BorderLayout());
        checkJenkinsBuildsPnl.add(checkNewJenkinsBuildsCbx, BorderLayout.NORTH);

        checkJenkinsBuildsPnl.add(new JLabel("Select envs to check:"), BorderLayout.CENTER);
        
        JPanel wasEnvsPnl = new JPanel(new FlowLayout());
        for (VolvoEnv wasEnv : Settings.getInstance().getCurrentProject().getVolvoEnvs()) {
            JCheckBox jCheckBox = new JCheckBox(wasEnv.getName());
            volvoEnvChks.add(jCheckBox);
            wasEnvsPnl.add(jCheckBox);            
        }
        JPanel jenkinsPnl = new JPanel(new BorderLayout());
        jenkinsPnl.add(wasEnvsPnl, BorderLayout.NORTH);
        JLabel intervalLbl = new JLabel("Interval between env. checks:");
        jenkinsPnl.add(intervalLbl, BorderLayout.CENTER);
        
        intervalCheck.setText("" + VersionTool.getInstance().getEnvChecker().getIntervalCheck());
        intervalCheck.setPreferredSize(new Dimension(100, 20));
        jenkinsPnl.add(intervalCheck, BorderLayout.SOUTH);
        checkJenkinsBuildsPnl.add(jenkinsPnl, BorderLayout.SOUTH);
        
        JPanel btnPnl = new JPanel(new FlowLayout());
        btnPnl.add(okBtn);
        btnPnl.add(cancelBtn);

        add(tempDirPnl, BorderLayout.NORTH);
        add(checkJenkinsBuildsPnl, BorderLayout.CENTER);
        add(btnPnl, BorderLayout.SOUTH);
        
        setMinimumSize(new Dimension(260, 80));
        pack();
        setLocationRelativeTo(null);

        updateViewWithModel();

        okBtn.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                updateModelWithView();
                setVisible(false);
            }

        });

        cancelBtn.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        
        setVisible(true);
    }

    private void updateViewWithModel() {
        localTempDirsCbx.setSelectedItem(Settings.getInstance().getLocalTempDir());
        checkNewJenkinsBuildsCbx.setSelected(Settings.getInstance().getCheckNewJenkinsBuilds());        
        for (VolvoEnv volvoEnv : Settings.getInstance().getUserSelectedEnvsToCheck()) {
            for (JCheckBox cbk : volvoEnvChks) {
                if (cbk.getText().equals(volvoEnv.getName())) {
                    cbk.setSelected(true);
                }
            }
        }
    }

    private void updateModelWithView() {
        Settings.getInstance().setLocalTempDir((String) localTempDirsCbx.getSelectedItem());
        Settings.getInstance().setCheckNewJenkinsBuilds(checkNewJenkinsBuildsCbx.isSelected());        
        
        Collection<VolvoEnv> userSelectedEnvsToCheck = new ArrayList<VolvoEnv>();
        for (JCheckBox wasEnvCheckBox : volvoEnvChks) {
            for (VolvoEnv availVolvoEnv : Settings.getInstance().getCurrentProject().getVolvoEnvs()) {                
                if (wasEnvCheckBox.isSelected() && wasEnvCheckBox.getText().equals(availVolvoEnv.getName())) {
                    userSelectedEnvsToCheck.add(availVolvoEnv);
                }
            }
        }
        Settings.getInstance().setUserSelectedEnvsToCheck(userSelectedEnvsToCheck);                    
                    
        Integer checkInt = Integer.parseInt(intervalCheck.getText());
        VersionTool.getInstance().getEnvChecker().setIntervalCheck(checkInt);
        
        VersionTool.getInstance().getCheckerThread().interrupt();
    }

}
