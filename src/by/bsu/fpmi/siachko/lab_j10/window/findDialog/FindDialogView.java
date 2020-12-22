package by.bsu.fpmi.siachko.lab_j10.window.findDialog;

import by.bsu.fpmi.siachko.lab_j10.window.GoodsDescription;
import by.bsu.fpmi.siachko.lab_j10.window.GoodsModel;
import by.bsu.fpmi.siachko.lab_j10.window.GoodsView;
import by.bsu.fpmi.siachko.lab_j10.window.dialog.DialogView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class FindDialogView extends JDialog {

    private JTextField nameField;
    private JButton button;

    private GoodsModel model;

    public FindDialogView(String title, GoodsView ancestor, GoodsModel model)
    {
        super(ancestor, ModalityType.APPLICATION_MODAL);

        this.model = model;

        setTitle(title);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 120);

        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        button = new JButton("Find the element");
        add(mainPanel, BorderLayout.CENTER);
        mainPanel.add(button, BorderLayout.SOUTH);

        JPanel fieldPanel = new JPanel();
        mainPanel.add(fieldPanel, BorderLayout.CENTER);
        fieldPanel.setLayout(new GridLayout(1, 1));

        JPanel field1Panel = new JPanel();
        JLabel nameLabel = new JLabel("Enter the name of good to find:");
        nameField = new JTextField();
        field1Panel.setLayout(new BorderLayout());
        field1Panel.add(nameLabel, BorderLayout.NORTH);
        field1Panel.add(nameField, BorderLayout.CENTER);

        fieldPanel.add(field1Panel);
        FindDialogView view = this;

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                Map<String, Integer> goodsMap = new TreeMap<>();
                for (GoodsDescription item : model.getAllData()){
                    if (!item.getGoodsName().equals(name)){
                        continue;
                    }
                    if (goodsMap.containsKey(item.getImportingCountry())){
                        int volume = goodsMap.get(item.getImportingCountry());
                        goodsMap.remove(item.getImportingCountry());
                        goodsMap.put(item.getImportingCountry(), item.getVolume() + volume);
                    }
                    else {
                        goodsMap.put(item.getImportingCountry(), item.getVolume());
                    }
                }
                ArrayList<GoodsDescription> answerList = new ArrayList<>();
                Set<String> keys = goodsMap.keySet();
                Iterator<String> keysIt = keys.iterator();
                while (keysIt.hasNext()){
                    String country = keysIt.next();
                    answerList.add(new GoodsDescription(name, country, goodsMap.get(country)));
                }
                model.setResultData(answerList);
                dispose();
            }
        });

        setVisible(true);

    }

}
