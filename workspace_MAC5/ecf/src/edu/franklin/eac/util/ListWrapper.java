package edu.franklin.eac.util;

import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class ListWrapper {
	private List list;
	
	public ListWrapper(List list) {
		if(list == null)
			this.list = new ArrayList();
		else
			this.list = list;
	}
	
	public List getList() {
		return list;
	}
	
	public int getSize() {
		return list.size();
	}
}
