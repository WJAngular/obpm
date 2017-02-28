package cn.myapps.km.disk.ejb;

import java.sql.Connection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.myapps.km.util.NDataSource;
import cn.myapps.km.util.PersistenceUtils;
import cn.myapps.km.util.Sequence;

public class NdiskProcessBeanTest {
	
	NDiskProcess process = null;
	
	String domainId = "";
	String userId = "";
	
	@Before
	public void setUp() throws Exception {
		process = new NDiskProcessBean();
	}

	@After
	public void tearDown() throws Exception {
		PersistenceUtils.closeConnection();
	}
	
	@Test
	public void createNDisk() throws Exception {
		// 创建一个网盘Ndisk（级联创建一个网盘的根目录NDir）。
		//1.设置网盘基础属性
		String diskId = Sequence.getSequence();
		NDisk disk = new NDisk();
		disk.setId(diskId);
		disk.setType(NDisk.TYPE_PERSONAL);
		disk.setOwnerId(Sequence.getSequence());
		disk.setDomainId(Sequence.getSequence());
		disk.setName(NDisk.NAME_PERSONAL);
		
		process.doCreate(disk);
	}

}
