package cn.myapps.km.search;

import cn.myapps.km.disk.ejb.NFile;

/**
 * 索引转换包
 * @author Richard
 *
 */
public class IndexBundle {
	private NFile nfile;
	
	private String realPath;
	
	private String ndiskid;
	
	public IndexBundle(){
		super();
	}

	public IndexBundle(NFile nfile, String realPath, String ndiskid){
		this.nfile = nfile;
		this.realPath = realPath;
		this.ndiskid = ndiskid;
	}
	
	public NFile getNfile() {
		return nfile;
	}

	public void setNfile(NFile nfile) {
		this.nfile = nfile;
	}

	public String getRealPath() {
		return realPath;
	}

	public void setRealPath(String realPath) {
		this.realPath = realPath;
	}

	public String getNdiskid() {
		return ndiskid;
	}

	public void setNdiskid(String ndiskid) {
		this.ndiskid = ndiskid;
	}
	
}
