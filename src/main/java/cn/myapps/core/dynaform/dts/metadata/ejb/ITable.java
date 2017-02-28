package cn.myapps.core.dynaform.dts.metadata.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import cn.myapps.base.OBPMValidateException;

public class ITable {

	private String name;

	private Collection<IColumn> columns = new ArrayList<IColumn>();
	private Collection<IIndex> indexs = new ArrayList<IIndex>();
	private Collection<IForeignKey> foreignKeys =  new ArrayList<IForeignKey>();

	public ITable(String name) {
		this.name = name;
	}

	public Collection<IColumn> getColumns() {
		return columns;
	}

	public void setColumns(Collection<IColumn> columns) {
		this.columns = columns;
	}

	public void addColumn(IColumn column) {
		columns.add(column);
	}

	public IColumn findColumn(String name) throws Exception {
		ArrayList<IColumn> tmp = new ArrayList<IColumn>();

		for (Iterator<IColumn> iter = columns.iterator(); iter.hasNext();) {
			IColumn column = (IColumn) iter.next();
			if (column.getName().equalsIgnoreCase(name)) {
				tmp.add(column);
			}
		}

		if (tmp.size() > 1) {
			IColumn column = (IColumn) tmp.get(0);
			throw new OBPMValidateException("(" + column.getName() + ") {*[core.field.name.was.duplicate]*}");
		}

		if (tmp.size() > 0) {
			return (IColumn) tmp.get(0);
		}

		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Collection<IIndex> getIndexs() {
		return indexs;
	}

	public void setIndexs(Collection<IIndex> indexs) {
		this.indexs = indexs;
	}
	
	public Collection<IForeignKey> getForeignKeys() {
		return foreignKeys;
	}

	public void setForeignKeys(Collection<IForeignKey> foreignKeys) {
		this.foreignKeys = foreignKeys;
	}
	
	public int getIndexTotal(){
		return getIndexs().size();
	}
	
	public String getIndexColumnNames(){
		StringBuffer msg = new StringBuffer();
		for (Iterator<IIndex> iterator = getIndexs().iterator(); iterator.hasNext();) {
			IIndex index = iterator.next();
			msg.append(index.getColumnName()).append(",");
		}
		if(msg.length()>0) msg.setLength(msg.length()-1);
		return msg.toString();
	}
	
	public String getSuggestMsg(){
		String msg = "<font>--</font>";
		if("T_ACTORHIS".equalsIgnoreCase(getName())){
			msg = "<font color='green'>{*[core.dts.metadata.hasOptimization]*}</font>";
			for(String column:new String[]{"FLOWSTATERT_ID","DOC_ID","ACTORID","NODEHIS_ID"}){
				boolean find = false;
				for (IIndex index : getIndexs()){
					if(column.equalsIgnoreCase(index.getColumnName())) {
						find = true;
						break;
					}
				}
				if(find == false){
					msg = "<font color='red'>{*[core.dts.metadata.suggestOptimization]*}</font>";
					break;
				}
			}
		} else if("T_ACTORRT".equalsIgnoreCase(getName())){
			msg = "<font color='green'>{*[core.dts.metadata.hasOptimization]*}</font>";
			for(String column:new String[]{"DEADLINE","DOC_ID","LASTOVERDUEREMINDER","PENDING","ACTORID","FLOWSTATERT_ID","NODERT_ID"}){
				boolean find = false;
				for (IIndex index : getIndexs()){
					if(column.equalsIgnoreCase(index.getColumnName())) {
						find = true;
						break;
					}
				}
				if(find == false){
					msg = "<font color='red'>{*[core.dts.metadata.suggestOptimization]*}</font>";
					break;
				}
			}
		} else if("T_CIRCULATOR".equalsIgnoreCase(getName())){
			msg = "<font color='green'>{*[core.dts.metadata.hasOptimization]*}</font>";
			for(String column:new String[]{"FLOWSTATERT_ID","DOC_ID","ISREAD","USERID"}){
				boolean find = false;
				for (IIndex index : getIndexs()){
					if(column.equalsIgnoreCase(index.getColumnName())) {
						find = true;
						break;
					}
				}
				if(find == false){
					msg = "<font color='red'>{*[core.dts.metadata.suggestOptimization]*}</font>";
					break;
				}
			}
		} else if("T_COUNTER".equalsIgnoreCase(getName())){
			msg = "<font color='green'>{*[core.dts.metadata.hasOptimization]*}</font>";
			for(String column:new String[]{"APPLICATIONID","DOMAINID","NAME"}){
				boolean find = false;
				for (IIndex index : getIndexs()){
					if(column.equalsIgnoreCase(index.getColumnName())) {
						find = true;
						break;
					}
				}
				if(find == false){
					msg = "<font color='red'>{*[core.dts.metadata.suggestOptimization]*}</font>";
					break;
				}
			}
		} else if("T_DOCUMENT".equalsIgnoreCase(getName())){
			msg = "<font color='green'>{*[core.dts.metadata.hasOptimization]*}</font>";
			for(String column:new String[]{"STATE","PARENT","MAPPINGID","STATELABEL","ISTMP","FORMNAME"}){
				boolean find = false;
				for (IIndex index : getIndexs()){
					if(column.equalsIgnoreCase(index.getColumnName())) {
						find = true;
						break;
					}
				}
				if(find == false){
					msg = "<font color='red'>{*[core.dts.metadata.suggestOptimization]*}</font>";
					break;
				}
			}
		} else if("T_FLOW_INTERVENTION".equalsIgnoreCase(getName())){
			msg = "<font color='green'>{*[core.dts.metadata.hasOptimization]*}</font>";
			for(String column:new String[]{"DOCID","FLOWID","APPLICATIONID","STATUS","DOMAINID","LASTAUDITOR","FLOWNAME","INITIATOR","STATELABEL"}){
				boolean find = false;
				for (IIndex index : getIndexs()){
					if(column.equalsIgnoreCase(index.getColumnName())) {
						find = true;
						break;
					}
				}
				if(find == false){
					msg = "<font color='red'>{*[core.dts.metadata.suggestOptimization]*}</font>";
					break;
				}
			}
		} else if("T_FLOW_PROXY".equalsIgnoreCase(getName())){
			msg = "<font color='green'>{*[core.dts.metadata.hasOptimization]*}</font>";
			for(String column:new String[]{"OWNER","APPLICATIONID","DOMAINID","FLOWID","STATE"}){
				boolean find = false;
				for (IIndex index : getIndexs()){
					if(column.equalsIgnoreCase(index.getColumnName())) {
						find = true;
						break;
					}
				}
				if(find == false){
					msg = "<font color='red'>{*[core.dts.metadata.suggestOptimization]*}</font>";
					break;
				}
			}
		} else if("T_FLOWSTATERT".equalsIgnoreCase(getName())){
			msg = "<font color='green'>{*[core.dts.metadata.hasOptimization]*}</font>";
			for(String column:new String[]{"PARENT","COMPLETE","FLOWID","DOCID"}){
				boolean find = false;
				for (IIndex index : getIndexs()){
					if(column.equalsIgnoreCase(index.getColumnName())) {
						find = true;
						break;
					}
				}
				if(find == false){
					msg = "<font color='red'>{*[core.dts.metadata.suggestOptimization]*}</font>";
					break;
				}
			}
		} else if("T_NODERT".equalsIgnoreCase(getName())){
			msg = "<font color='green'>{*[core.dts.metadata.hasOptimization]*}</font>";
			for(String column:new String[]{"FLOWSTATERT_ID","NODEID","DOCID"}){
				boolean find = false;
				for (IIndex index : getIndexs()){
					if(column.equalsIgnoreCase(index.getColumnName())) {
						find = true;
						break;
					}
				}
				if(find == false){
					msg = "<font color='red'>{*[core.dts.metadata.suggestOptimization]*}</font>";
					break;
				}
			}
		} else if("T_PENDING".equalsIgnoreCase(getName())){
			msg = "<font color='green'>{*[core.dts.metadata.hasOptimization]*}</font>";
			for(String column:new String[]{"AUDITUSER","APPLICATIONID","STATE","DOMAINID","DOCID","FORMNAME","FORMID","AUTHOR"}){
				boolean find = false;
				for (IIndex index : getIndexs()){
					if(column.equalsIgnoreCase(index.getColumnName())) {
						find = true;
						break;
					}
				}
				if(find == false){
					msg = "<font color='red'>{*[core.dts.metadata.suggestOptimization]*}</font>";
					break;
				}
			}
		} else if("T_PENDING_ACTOR_SET".equalsIgnoreCase(getName())){
			msg = "<font color='green'>{*[core.dts.metadata.hasOptimization]*}</font>";
			for(String column:new String[]{"DOCID"}){
				boolean find = false;
				for (IIndex index : getIndexs()){
					if(column.equalsIgnoreCase(index.getColumnName())) {
						find = true;
						break;
					}
				}
				if(find == false){
					msg = "<font color='red'>{*[core.dts.metadata.suggestOptimization]*}</font>";
					break;
				}
			}
		} else if("T_RELATIONHIS".equalsIgnoreCase(getName())){
			msg = "<font color='green'>{*[core.dts.metadata.hasOptimization]*}</font>";
			for(String column:new String[]{"AUDITOR","STARTNODEID","ENDNODEID","APPLICATIONID","FLOWSTATERT_ID","FLOWOPERATION","STARTNODENAME","ACTIONTIME","DOCID","FLOWNAME","FLOWID"}){
				boolean find = false;
				for (IIndex index : getIndexs()){
					if(column.equalsIgnoreCase(index.getColumnName())) {
						find = true;
						break;
					}
				}
				if(find == false){
					msg = "<font color='red'>{*[core.dts.metadata.suggestOptimization]*}</font>";
					break;
				}
			}
		} else if("T_UPLOAD".equalsIgnoreCase(getName())){
			msg = "<font color='green'>{*[core.dts.metadata.hasOptimization]*}</font>";
			for(String column:new String[]{"PATH","NAME"}){
				boolean find = false;
				for (IIndex index : getIndexs()){
					if(column.equalsIgnoreCase(index.getColumnName())) {
						find = true;
						break;
					}
				}
				if(find == false){
					msg = "<font color='red'>{*[core.dts.metadata.suggestOptimization]*}</font>";
					break;
				}
			}
		} else if(getName().toUpperCase().startsWith("AUTH_")){
			msg = "<font color='red'>{*[core.dts.metadata.suggestOptimization]*}</font>";
			for (Iterator<IIndex> iterator = getIndexs().iterator(); iterator.hasNext();) {
				IIndex index = iterator.next();
				if(index.getColumnName().equalsIgnoreCase("DOC_ID")){
					msg = "<font color='green'>{*[core.dts.metadata.hasOptimization]*}</font>";
					break;
				}
				
			}
		}
		return msg.toString();	
	}

	public boolean equals(Object obj) {
		if (obj instanceof ITable) {
			ITable anTable = (ITable) obj;
			if (this.getName().equalsIgnoreCase(anTable.getName())) {
				for (Iterator<IColumn> iterator = getColumns().iterator(); iterator.hasNext();) {
					try {
						IColumn column = (IColumn) iterator.next();
						IColumn anColumn = anTable.findColumn(column.getName());
						if (!column.equals(anColumn)) {
							return false;
						}
					} catch (Exception e) {
						return false;
					}
				}
				return true;
			}
		}

		return super.equals(obj);
	}

	public int hashCode() {
		return super.hashCode();
	}

}
