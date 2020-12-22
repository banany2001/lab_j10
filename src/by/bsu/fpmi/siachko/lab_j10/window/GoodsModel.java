package by.bsu.fpmi.siachko.lab_j10.window;

import java.io.File;
import java.util.ArrayList;

public class GoodsModel {

    private ArrayList<GoodsDescription> allData;
    private ArrayList<GoodsDescription> resultData;
    private File file;
    boolean isModified;

    public GoodsModel()
    {
        allData = new ArrayList<>();
        resultData = new ArrayList<>();
        file = null;
        isModified = false;
    }

    public ArrayList<GoodsDescription> getAllData() {
        return allData;
    }

    public void setAllData(ArrayList<GoodsDescription> allData) {
        this.allData = allData;
    }

    public ArrayList<GoodsDescription> getResultData() {
        return resultData;
    }

    public void setResultData(ArrayList<GoodsDescription> resultData) {
        this.resultData = resultData;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isModified() {
        return isModified;
    }

    public void setModified(boolean modified) {
        isModified = modified;
    }
}
