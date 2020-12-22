package by.bsu.fpmi.siachko.lab_j10.window;

import by.bsu.fpmi.siachko.lab_j10.window.dialog.DialogView;
import by.bsu.fpmi.siachko.lab_j10.window.findDialog.FindDialogView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

public class GoodsController {

    private GoodsView view;
    private GoodsModel model;

    public GoodsController(GoodsView view, GoodsModel model) {
        this.view = view;
        this.model = model;

        view.getNewButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.isModified()){
                    int decision = JOptionPane.showConfirmDialog(view, "Do you want to save the database?");
                    if (decision == JOptionPane.OK_OPTION){
                        boolean result = save();
                        if (!result){
                            JOptionPane.showMessageDialog(view, "The database was not saved.");
                            return;
                        }
                    }
                    if (decision == JOptionPane.CANCEL_OPTION){
                        return;
                    }
                }
                model.setModified(false);
                model.setFile(null);
                model.setAllData(new ArrayList<>());
                model.setResultData(new ArrayList<>());
                view.getListModel1().clear();
                view.getListModel2().clear();
            }
        });

        view.getSave().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean result = save();
                if (!result){
                    JOptionPane.showMessageDialog(view, "The database was not saved.");
                }
            }
        });

        view.getSaveAs().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean result = saveAs();
                if (!result){
                    JOptionPane.showMessageDialog(view, "The database was not saved.");
                }
            }
        });

        view.getOpen().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.isModified()){
                    int decision = JOptionPane.showConfirmDialog(view, "Do you want to save the database?");
                    if (decision == JOptionPane.OK_OPTION){
                        boolean result = save();
                        if (!result){
                            JOptionPane.showMessageDialog(view, "The database was not saved.");
                            return;
                        }
                    }
                    if (decision == JOptionPane.CANCEL_OPTION){
                        return;
                    }
                }
                boolean result = open();
                if (!result){
                    JOptionPane.showMessageDialog(view, "The database wasn't opened.");
                }
            }
        });

        view.getAdd().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int beforeSize = model.getAllData().size();
                new DialogView("Adding new item", view, model);
                int afterSize = model.getAllData().size();

                if (beforeSize != afterSize){
                    view.getListModel1().addElement(model.getAllData().get(beforeSize));
                    model.setModified(true);
                }
            }
        });

        view.getFindByGoods().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FindDialogView("Find by name of good", view, model);
                view.getListModel2().clear();
                for (GoodsDescription item: model.getResultData()){
                    view.getListModel2().addElement(item);
                }
            }
        });

        view.getBuildAll().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Map<String, Integer> goodsMap = new TreeMap<>();
                for (GoodsDescription item : model.getAllData()){
                    if (goodsMap.containsKey(item.getGoodsName())){
                        int volume = goodsMap.get(item.getGoodsName());
                        goodsMap.remove(item.getGoodsName());
                        goodsMap.put(item.getGoodsName(), item.getVolume() + volume);
                    }
                    else {
                        goodsMap.put(item.getGoodsName(), item.getVolume());
                    }
                }
                ArrayList<GoodsDescription> toSort = new ArrayList<>();
                Set<String> keys = goodsMap.keySet();
                Iterator<String> keysIt = keys.iterator();
                while (keysIt.hasNext()){
                    String name = keysIt.next();
                    toSort.add(new GoodsDescription(name, "Not specified", goodsMap.get(name)));
                }
                Collections.sort(toSort, new Comparator<GoodsDescription>() {
                    @Override
                    public int compare(GoodsDescription o1, GoodsDescription o2) {
                        if (o1.getVolume() != o2.getVolume()){
                            return -Integer.compare(o1.getVolume(), o2.getVolume());
                        }
                        return o1.getGoodsName().compareTo(o2.getGoodsName());
                    }
                });
                model.setResultData(toSort);
                view.getListModel2().clear();
                for (GoodsDescription item : model.getResultData()){
                    view.getListModel2().addElement(item);
                }
            }
        });

        view.getBuildAllByImport().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Map<String, Integer> goodsMap = new TreeMap<>();
                for (GoodsDescription item : model.getAllData()){
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
                    answerList.add(new GoodsDescription("Not specified", country, goodsMap.get(country)));
                }
                Collections.sort(answerList, new Comparator<GoodsDescription>() {
                    @Override
                    public int compare(GoodsDescription o1, GoodsDescription o2) {
                        return -Integer.compare(o1.getVolume(), o2.getVolume());
                    }
                });
                model.setResultData(answerList);
                view.getListModel2().clear();
                for (GoodsDescription item : model.getResultData()){
                    view.getListModel2().addElement(item);
                }
            }
        });

    }

    private boolean save()
    {
        boolean result = false;
        if (model.getFile() == null){
            result = saveAs();
        }
        else {
            result = saveData();
        }
        return result;
    }

    private boolean saveAs()
    {
        boolean result = false;
        JFileChooser chooser = new JFileChooser();
        int selectionResult = chooser.showSaveDialog(view);
        if (selectionResult == JFileChooser.APPROVE_OPTION){
            model.setFile(chooser.getSelectedFile());
            result = saveData();
        }
        return result;
    }

    private boolean saveData()
    {
        OutputStream os = null;
        boolean result = true;
        try
        {
            os = new FileOutputStream(model.getFile());
            String s = "";
            s = Integer.toString(model.getAllData().size());
            os.write(s.getBytes(), 0, s.length());
            os.write('\n');
            for (GoodsDescription item : model.getAllData()){
                s = item.getGoodsName();
                os.write(s.getBytes(), 0, s.length());
                os.write('\n');
                s = item.getImportingCountry();
                os.write(s.getBytes(), 0, s.length());
                os.write('\n');
                s = Integer.toString(item.getVolume());
                os.write(s.getBytes(), 0, s.length());
                os.write('\n');
                os.write('\n');
            }
            os.close();
        }
        catch (IOException ex)
        {
            result = false;
        }
        model.setModified(false);
        return result;
    }

    private boolean open()
    {

        JFileChooser chooser = new JFileChooser();
        int selectionResult = chooser.showOpenDialog(view);
        if (selectionResult != JFileChooser.APPROVE_OPTION){
            return false;
        }

        File file = chooser.getSelectedFile();
        Scanner read = null;
        try
        {
            read = new Scanner(file);
        }
        catch (FileNotFoundException ex)
        {
            return false;
        }

        ArrayList<GoodsDescription> readItems = new ArrayList<>();

        int n;
        try {
            n = read.nextInt();
        }
        catch (NoSuchElementException | IllegalStateException ex)
        {
            return false;
        }

        for (int i = 0; i < n; i++){
            String s = "";
            try
            {
                s = read.nextLine();
                String name = read.nextLine();
                String country = read.nextLine();
                int volume = read.nextInt();
                s = read.nextLine();
                readItems.add(new GoodsDescription(name, country, volume));
            }
            catch (NoSuchElementException | IllegalStateException ex)
            {
                return false;
            }
        }

        model.setAllData(readItems);
        model.setResultData(new ArrayList<>());
        view.getListModel1().clear();
        view.getListModel2().clear();

        for (GoodsDescription item : model.getAllData()){
            view.getListModel1().addElement(item);
        }

        model.setModified(false);
        model.setFile(file);
        return true;

    }

}
