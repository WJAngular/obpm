package cn.myapps.km.disk.ejb;

import java.sql.Connection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.myapps.km.util.NDataSource;
import cn.myapps.km.util.PersistenceUtils;
import cn.myapps.km.util.Sequence;

public class NdirProcessBeanTest {
	
	NDirProcessBean process = null;
	
	
	@Before
	public void setUp() throws Exception {
		process = new NDirProcessBean();
	}

	@After
	public void tearDown() throws Exception {
		PersistenceUtils.closeConnection();
	}
	
	@Test
	public void createNDir() throws Exception {
		NDir dir = new NDir();
		String dirid = Sequence.getSequence();
		String parentid = "11e2-823b-34af4893-a5e8-fb314a54b2d2";
		String diskid = "11e2-823b-34af4890-a5e8-fb314a54b2d2";
		String parentPath = "\\11e2-823b-34af4890-a5e8-fb314a54b2d2";
		dir.setId(dirid);
		dir.setName("我的图片");
		dir.setOwnerId(Sequence.getSequence());
		dir.setParentId(parentid);
		dir.setnDiskId(diskid);
		dir.setPath(parentPath + "\\" + dir.getName());
		
		process.doCreate(dir);
	}
	
	//@Test
	public void changeNDirName() throws Exception {
		String dirId = "1361947006562";
		NDir dir = (NDir) process.doView(dirId);
		dir.setName("个人上传");
		process.doUpdate(dir);
	}
	
	//@Test
	public void deleteNDir() throws Exception {
		String dirId = "1361947006562";
		process.doRemove(dirId);
	}

}
