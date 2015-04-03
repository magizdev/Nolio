package com.ca.nolio.model;

import android.util.Pair;

public class NameIdItem extends Pair<String, Long> {

	public NameIdItem(String first, Long second) {
		super(first, second);
	}
	
	@Override
	public String toString(){
		return first;
	}

}
