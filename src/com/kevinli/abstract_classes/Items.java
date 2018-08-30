package com.kevinli.abstract_classes;

import java.awt.Graphics;

public abstract class Items {
	
	protected int buy_price, sell_price;
	protected int id;
	
	public Items(int id, int buy, int sell)
	{
		this.buy_price = buy;
		this.sell_price = sell;
		this.id = id;
	}
	
	public abstract void drawIcon(Graphics g);
	public abstract void drawItem(Graphics g);
	
	public boolean equalsTo(int id)
	{
		return this.id == id;
	}
	
}
