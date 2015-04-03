package com.ca.nolio.model;

import java.util.Comparator;

public class Step {
	long id;
	String name;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static class StepComparator implements Comparator<Step> {

		@Override
		public int compare(Step lhs, Step rhs) {
			return (int)(lhs.getId() - rhs.getId());
		}

	}
}
