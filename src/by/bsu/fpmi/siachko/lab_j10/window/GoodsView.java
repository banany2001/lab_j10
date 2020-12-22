package by.bsu.fpmi.siachko.lab_j10.window;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class GoodsView extends JFrame {

    private JMenuItem newButton;
    private JMenuItem add;
    private JMenuItem save;
    private JMenuItem saveAs;
    private JMenuItem open;
    private JMenuItem findByGoods;
    private JMenuItem buildAll;
    private JMenuItem buildAllByImport;

    private JPanel panel1;
    private JPanel panel2;
    private DefaultListModel<GoodsDescription> listModel1;
    private DefaultListModel<GoodsDescription> listModel2;

    public GoodsView(String title)
    {

        setTitle(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 500);

        try {
            BufferedImage image = ImageIO.read(new File("logo1cfm.png"));
            setIconImage(image);
        }
        catch (IOException ex)
        {

        }

        setLayout(new GridLayout(1, 2));

        panel1 = new JPanel();
        panel2 = new JPanel();

        panel1.setLayout(new BorderLayout());
        panel2.setLayout(new BorderLayout());

        JLabel label1 = new JLabel("All objects:");
        JLabel label2 = new JLabel("Result of last operation:");

        panel1.add(label1, BorderLayout.NORTH);
        panel2.add(label2, BorderLayout.NORTH);

        listModel1 = new DefaultListModel<>();
        listModel2 = new DefaultListModel<>();

        JList<GoodsDescription> list1 = new JList<>(listModel1);
        JList<GoodsDescription> list2 = new JList<>(listModel2);

        panel1.add(new JScrollPane(list1, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
        panel2.add(new JScrollPane(list2, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);

        add(panel1);
        add(panel2);

        setJMenuBar(createMenuBar());

        setVisible(true);

    }

    private JMenuBar createMenuBar()
    {

        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu actions = new JMenu("Actions");

        newButton = new JMenuItem("New");
        save = new JMenuItem("Save");
        saveAs = new JMenuItem("Save as");
        open = new JMenuItem("Open");

        file.add(newButton);
        file.addSeparator();
        file.add(save);
        file.add(saveAs);
        file.add(open);

        add = new JMenuItem("Add new goods");
        findByGoods = new JMenuItem("Find countries by goods");
        buildAll = new JMenuItem("Build list of all goods");
        buildAllByImport = new JMenuItem("Build list of all importing countries");

        actions.add(add);
        actions.addSeparator();
        actions.add(findByGoods);
        actions.add(buildAll);
        actions.add(buildAllByImport);

        menuBar.add(file);
        menuBar.add(actions);

        return menuBar;

    }

    public JMenuItem getNewButton() {
        return newButton;
    }

    public JMenuItem getAdd() {
        return add;
    }

    public JMenuItem getSave() {
        return save;
    }

    public JMenuItem getSaveAs() {
        return saveAs;
    }

    public JMenuItem getOpen() {
        return open;
    }

    public JMenuItem getFindByGoods() {
        return findByGoods;
    }

    public JMenuItem getBuildAll() {
        return buildAll;
    }

    public JMenuItem getBuildAllByImport() {
        return buildAllByImport;
    }

    public DefaultListModel<GoodsDescription> getListModel1() {
        return listModel1;
    }

    public DefaultListModel<GoodsDescription> getListModel2() {
        return listModel2;
    }
}
