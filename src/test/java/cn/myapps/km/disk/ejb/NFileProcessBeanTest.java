package cn.myapps.km.disk.ejb;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.myapps.km.util.PersistenceUtils;

public class NFileProcessBeanTest {
	
	NFileProcessBean process = null;
	
	String domainId = "";
	String userId = "11e2-7a5e-1b04097c-97b3-75c50e87d952";
	String userId2 = "11e2-7a5e-1b04097c-97b3-75c50e87d954";
	String ndirId = "11e2-807e-a9c28c38-b20b-3b4eb17c701e";
	String fileId = "11e2-7f2d-cade7b00-a2ec-91e92d8dbd1b";
	String savePath = "/workspace/KM/";
	
	
	@Before
	public void setUp() throws Exception {
		process = new NFileProcessBean();
	}

	@After
	public void tearDown() throws Exception {
		PersistenceUtils.closeConnection();
	}
	
	//@Test
	public void createNFile() throws Exception {
		// 创建一个文件
		NFile file = new NFile();
		file.setName("file.txt");
		process.doCreate(file,userId,ndirId,savePath);
	}
	
	//@Test
	public void findNFile() throws Exception {
		// 2. find
		process.doView(fileId);
	}
	
	//@Test
	public void updateFile() throws Exception {
		// 3. update
		NFile newFile = (NFile) process.doView(fileId);
		newFile.setName("ios开发手册.pdf");
		newFile.setState(IFile.STATE_PRIVATE);
		process.doUpdate(newFile);
	}
	
	//@Test
	public void removeFile() throws Exception {
		//4. remove
		process.doRemove("11e2-8086-6a338406-be01-ede5b1087029");
	}
	
	//@Test
	public void queryFile() throws Exception {
		// 5.list
		process.doQuery(userId,ndirId);
	}
	
	//@Test
	public void shareFile() throws Exception{
		//process.doShareFile(fileId,userId,1);
	}
	
	//@Test
	public void copyNFile() throws Exception {
		//3. 转存
		process.doCopyFile(fileId,userId2,ndirId,savePath);
	}
	
	@Test
	public void fullTextSearch() throws Exception {
		//4. 全文搜索
		//process.fullTextSearch("目录");
	}
	
}
