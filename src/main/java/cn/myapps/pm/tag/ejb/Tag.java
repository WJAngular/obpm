package cn.myapps.pm.tag.ejb;

import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.sequence.Sequence;

/**
 * 标签
 * 
 * @author Happy
 *
 */
public class Tag extends ValueObject {

	private static final long serialVersionUID = 1547943138448596767L;
	
	private String name;
	
	public Tag(){
		super();
	}
	
	public Tag(String name,WebUser user){
		try {
			this.id = Sequence.getSequence();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.name = name;
		this.domainid = user.getDomainid();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
