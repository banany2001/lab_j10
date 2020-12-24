package by.bsu.fpmi.siachko.lab_j10.window;

import by.bsu.fpmi.siachko.lab_j10.window.dialog.DialogView;
import by.bsu.fpmi.siachko.lab_j10.window.findDialog.FindDialogView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
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
        boolean result = true;

        FileOutputStream os;
        try
        {
            os = new FileOutputStream(model.getFile());
        }
        catch (FileNotFoundException e)
        {
            return false;
        }

        String headString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>";
        try
        {
            os.write(headString.getBytes(), 0, headString.length());
            String toOutput = "<Database>";
            os.write(toOutput.getBytes(), 0, toOutput.length());
            for (GoodsDescription item : model.getAllData()){
                toOutput = "<Good>";
                os.write(toOutput.getBytes(), 0, toOutput.length());

                toOutput = "<Name>";
                os.write(toOutput.getBytes(), 0, toOutput.length());
                toOutput = item.getGoodsName();
                os.write(toOutput.getBytes(), 0, toOutput.length());
                toOutput = "</Name>";
                os.write(toOutput.getBytes(), 0, toOutput.length());

                toOutput = "<Country>";
                os.write(toOutput.getBytes(), 0, toOutput.length());
                toOutput = item.getImportingCountry();
                os.write(toOutput.getBytes(), 0, toOutput.length());
                toOutput = "</Country>";
                os.write(toOutput.getBytes(), 0, toOutput.length());

                toOutput = "<Volume>";
                os.write(toOutput.getBytes(), 0, toOutput.length());
                toOutput = Integer.toString(item.getVolume());
                os.write(toOutput.getBytes(), 0, toOutput.length());
                toOutput = "</Volume>";
                os.write(toOutput.getBytes(), 0, toOutput.length());

                toOutput = "</Good>";
                os.write(toOutput.getBytes(), 0, toOutput.length());
            }
            toOutput = "</Database>";
            os.write(toOutput.getBytes(), 0, toOutput.length());
        }
        catch (IOException e)
        {
            return false;
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
        ArrayList<GoodsDescription> readItems = new ArrayList<>();

        try
        {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(file);

            Node root = document.getDocumentElement();
            NodeList goods = root.getChildNodes();
            for (int i = 0; i < goods.getLength(); i++){
                Node good = goods.item(i);
                if (good.getNodeType() != Node.TEXT_NODE){
                    if (!good.getNodeName().equals("Good")){
                        return false;
                    }
                    NodeList goodProps = good.getChildNodes();
                    String goodName = "";
                    boolean hasName = false;
                    String countryName = "";
                    boolean hasCountry = false;
                    int volume = 0;
                    boolean hasVolume = false;
                    if (goodProps.getLength() != 3){
                        return false;
                    }
                    for (int j = 0; j < goodProps.getLength(); j++){
                        Node goodProp = goodProps.item(j);
                        if (goodProp.getChildNodes().getLength() != 1){
                            return false;
                        }
                        if (goodProp.getNodeType() != Node.TEXT_NODE){
                            if (goodProp.getNodeName().equals("Name")){
                                goodName = goodProp.getChildNodes().item(0).getTextContent();
                                hasName = true;
                            }
                            else if (goodProp.getNodeName().equals("Country")){
                                countryName = goodProp.getChildNodes().item(0).getTextContent();
                                hasCountry = true;
                            }
                            else if (goodProp.getNodeName().equals("Volume")){
                                volume = Integer.parseInt(goodProp.getChildNodes().item(0).getTextContent());
                                hasVolume = true;
                            }
                        }
                    }
                    if (!hasName || !hasCountry || !hasVolume){
                        return false;
                    }
                    readItems.add(new GoodsDescription(goodName, countryName, volume));
                }
            }
        }
        catch (ParserConfigurationException ex)
        {
            return false;
        }
        catch (SAXException ex)
        {
            return false;
        }
        catch (IOException ex)
        {
            return false;
        }
        catch (NumberFormatException ex)
        {
            return false;
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
