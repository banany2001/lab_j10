package by.bsu.fpmi.siachko.lab_j10.window.dialog;

import by.bsu.fpmi.siachko.lab_j10.window.GoodsDescription;
import by.bsu.fpmi.siachko.lab_j10.window.GoodsModel;
import by.bsu.fpmi.siachko.lab_j10.window.GoodsView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DialogView extends JDialog {

    private JTextField nameField;
    private JTextField countryField;
    private JTextField volumeField;
    private JButton button;

    private GoodsModel model;

    public DialogView(String title, GoodsView ancestor, GoodsModel model)
    {

        super(ancestor, ModalityType.APPLICATION_MODAL);

        this.model = model;

        setTitle(title);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 200);

        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        button = new JButton("Add new element");
        add(mainPanel, BorderLayout.CENTER);
        mainPanel.add(button, BorderLayout.SOUTH);

        JPanel fieldPanel = new JPanel();
        mainPanel.add(fieldPanel, BorderLayout.CENTER);
        fieldPanel.setLayout(new GridLayout(3, 1));

        JPanel field1Panel = new JPanel();
        JLabel nameLabel = new JLabel("Enter the name of good:");
        nameField = new JTextField();
        field1Panel.setLayout(new BorderLayout());
        field1Panel.add(nameLabel, BorderLayout.NORTH);
        field1Panel.add(nameField, BorderLayout.CENTER);

        JPanel field2Panel = new JPanel();
        JLabel countryLabel = new JLabel("Enter the name of importing country:");
        countryField = new JTextField();
        field2Panel.setLayout(new BorderLayout());
        field2Panel.add(countryLabel, BorderLayout.NORTH);
        field2Panel.add(countryField, BorderLayout.CENTER);

        JPanel field3Panel = new JPanel();
        JLabel volumeLabel = new JLabel("Enter the volume of good:");
        volumeField = new JTextField();
        field3Panel.setLayout(new BorderLayout());
        field3Panel.add(volumeLabel, BorderLayout.NORTH);
        field3Panel.add(volumeField, BorderLayout.CENTER);

        fieldPanel.add(field1Panel);
        fieldPanel.add(field2Panel);
        fieldPanel.add(field3Panel);

        DialogView view = this;

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String country = countryField.getText();
                int volume = 0;
                try {
                    String vol = volumeField.getText();
                    volume = Integer.parseInt(vol);
                }
                catch (NumberFormatException ex)
                {
                    JOptionPane.showMessageDialog(view, "The entered data is wrong (volume must be integer)");
                    return;
                }
                model.getAllData().add(new GoodsDescription(name, country, volume));
                dispose();
            }
        });

        setVisible(true);

    }

    public JTextField getNameField() {
        return nameField;
    }

    public JTextField getCountryField() {
        return countryField;
    }

    public JTextField getVolumeField() {
        return volumeField;
    }

    public JButton getButton() {
        return button;
    }
}
