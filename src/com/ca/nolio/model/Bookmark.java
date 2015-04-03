package com.ca.nolio.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name="bookmark")
public class Bookmark extends Model {
	@Column(name="name")
	public String name;
	
	@Column(name="json")
	public String postJson;

}
