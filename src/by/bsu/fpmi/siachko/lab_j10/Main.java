package by.bsu.fpmi.siachko.lab_j10;

import by.bsu.fpmi.siachko.lab_j10.window.*;

public class Main {

    public static void main(String[] args) {

        new GoodsController(new GoodsView("Goods"), new GoodsModel());

    }
}
