package cn.myapps.km.base.init;

import java.sql.Connection;
import java.util.Collection;
import java.util.Iterator;

import cn.myapps.core.domain.ejb.DomainProcess;
import cn.myapps.core.domain.ejb.DomainProcessBean;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.km.base.init.dao.AbstractKmInitDAO;
import cn.myapps.km.base.init.dao.DB2KmInitDAO;
import cn.myapps.km.base.init.dao.MssqlKmInitDAO;
import cn.myapps.km.base.init.dao.MysqlKmInitDAO;
import cn.myapps.km.base.init.dao.OraclelKmInitDAO;
import cn.myapps.km.disk.ejb.NDisk;
import cn.myapps.km.disk.ejb.NDiskProcessBean;
import cn.myapps.km.org.ejb.NRole;
import cn.myapps.km.org.ejb.NRoleProcessBean;
import cn.myapps.km.util.NDataSource;
import cn.myapps.km.util.Sequence;

/**
 * KM初始器
 * @author xiuwei
 *
 */
public class Initiator {
	
	private AbstractKmInitDAO kmInitDAO;
	
	private Connection conn;
	
	
	public void init() throws Exception {
		try {
			initKmSchema();
			initKmBaseData();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			if(conn !=null && !conn.isClosed()){
				conn.close();
			}
		}
	}

	public AbstractKmInitDAO getKmInitDAO() {
		if(kmInitDAO ==null){
			try {
				String dbType = NDataSource._dbTye;
				if("MSSQL".equals(dbType)){  
					kmInitDAO = new MssqlKmInitDAO(getConn());
				}else if("ORACLE".equals(dbType)){
					kmInitDAO = new OraclelKmInitDAO(getConn());
				}else if("DB2".equals(dbType)){
					kmInitDAO = new DB2KmInitDAO(getConn());
				}else if("MYSQL".equals(dbType)){
					kmInitDAO = new MysqlKmInitDAO(getConn());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return kmInitDAO;
	}

	public Connection getConn() {
		if(conn ==null){
			try {
				conn = NDataSource.getConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return conn;
	}
	
	public void initKmSchema() throws Exception {
		
		try {
			getKmInitDAO().initTables();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
	}
	
	public void initKmBaseData() throws Exception {
		try {
			initPublicDisk();
			initArchiveDisk();
			initRoles();
		} catch (Exception e) {
			throw e;
		}
	}
	
	private void initArchiveDisk() throws Exception{
		try {
			NDiskProcessBean process = new NDiskProcessBean();
			
			DomainProcess domainProcess = new DomainProcessBean();
			Collection<DomainVO> domains = domainProcess.queryDomainsByStatus(1);
			
			for (Iterator<DomainVO> iter = domains.iterator(); iter.hasNext();) {
				DomainVO domain = iter.next();
				NDisk pDisk = process.getArchiveDisk(domain.getId());
				if(pDisk ==null){
					NDisk disk = new NDisk();
					disk.setId(Sequence.getSequence());
					disk.setName(NDisk.NAME_ARCHIVE);
					disk.setType(NDisk.TYPE_ARCHIVE);
					disk.setDomainId(domain.getId());
					process.doCreate(disk);
				}
			}
			
			
		} catch (Exception e) {
			throw e;
		}
	}

	private void initPublicDisk() throws Exception {
		try {
			NDiskProcessBean process = new NDiskProcessBean();
			
			DomainProcess domainProcess = new DomainProcessBean();
			Collection<DomainVO> domains = domainProcess.queryDomainsByStatus(1);
			

			for (Iterator<DomainVO> iter = domains.iterator(); iter.hasNext();) {
				DomainVO domain = iter.next();
				NDisk pDisk = process.getPublicDisk(domain.getId());
				if(pDisk ==null){
					NDisk disk = new NDisk();
					disk.setId(Sequence.getSequence());
					disk.setName(NDisk.NAME_PUBLIC);
					disk.setType(NDisk.TYPE_PUBLIC);
					disk.setDomainId(domain.getId());
					process.doCreate(disk);
				}
			}
			
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	private void initRoles() throws Exception {
		NRoleProcessBean process = new NRoleProcessBean();
		NRole role = null;
		try {
			if(process.doGetRoles().isEmpty()){
				role = new NRole();
				role.setId(Sequence.getSequence());
				role.setLevel(NRole.LEVEL_EMPLOYEE);
				role.setName("cn.myapps.km.dir.staff");
				process.doCreate(role);
				
				role = new NRole();
				role.setId(Sequence.getSequence());
				role.setLevel(NRole.LEVEL_DEPT_ADMIN);
				role.setName("cn.myapps.km.dir.admin_department_document");
				process.doCreate(role);
				
				role = new NRole();
				role.setId(Sequence.getSequence());
				role.setLevel(NRole.LEVEL_DOMAIN_ADMIN);
				role.setName("cn.myapps.km.dir.enterprise_document_manager");
				process.doCreate(role);
			}
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	public static void main(String[] args) {
		try {
			new Initiator().init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
