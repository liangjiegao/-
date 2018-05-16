package com.example.gdei;

import java.util.ArrayList;

/**
 * Created by gdei on 2018/5/9.
 */

public interface User {

    public String username = null;
    public int password = 0;
    public ArrayList<Goods> soldGoods = null;
    public int soldGoodsStore = 0;
    public ArrayList<Goods> saleGoods = null;
    public int saleGoodsStore = 0;
    public String selfIntr = null;
    public ArrayList<CommUser> myAtten = null;
    public String myAttenSet = null;
    public ArrayList<CommUser> attrnMe = null;
    public String attrnMeSet = null;
    public int head =0;
    public ArrayList<Goods> collection = null;
    public int collGoodsStore = 0;
    public ArrayList<Goods> myBuy = null;
    public int myBuyGoodsStore = 0;


    public void buy(Goods goods);

    public void sale(Goods goods);

    public void changeHead(int image);

    public void changeName(String newName);

    public void deleteMyAtten(CommUser user);

    public void addMyAtten(CommUser user);

    public void changeIntr(Goods goods);

    public void attenOrder();
}
