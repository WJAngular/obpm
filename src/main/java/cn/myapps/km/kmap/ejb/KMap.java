package cn.myapps.km.kmap.ejb;

import cn.myapps.km.base.ejb.NObject;
import cn.myapps.km.disk.ejb.Term;

public class KMap extends NObject {
	
	private String id;
	
	private String fileid;
	
	private Term[] term;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFileid() {
		return fileid;
	}

	public void setFileid(String fileid) {
		this.fileid = fileid;
	}

	public Term[] getTerm() {
		return term;
	}

	public void setTerm(Term[] term) {
		this.term = term;
	}
	
	
}
 
