package cn.myapps.km.permission.ejb;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.myapps.km.TestConstant;
import cn.myapps.km.util.PersistenceUtils;

public class DirAccessProcessBeanTest {
	
	private DirAccessProcess process = null;
	
	private final String id = "diraccessid-123-123-123-0001";
	private final String fileId = "dir-123-123-123-0001";
	
	@Before
	public void setUp() throws Exception {
		process = new DirAccessProcessBean();
	}

	@After
	public void tearDown() throws Exception {
		PersistenceUtils.closeConnection();
	}
	
	//@Test
	public void createFileAccess() throws Exception {
		try {
			FileAccess vo = new FileAccess();
			vo.setId(id);
			vo.setFileId(fileId);
			vo.setScope(FileAccess.SCOPE_USER);
			vo.setOwnerId(TestConstant.user_zhangsanId);
			vo.setDownloadMode(FileAccess.DOWNLOAD_MODE_FALSE);
			vo.setReadMode(FileAccess.WRITE_MODE_TRUE);
			vo.setWriteMode(FileAccess.WRITE_MODE_TRUE);
			vo.setPrintMode(FileAccess.PRINT_MODE_TRUE);
			
			process.doCreate(vo);
		} catch (Exception e) {
		}
	}
	
	@Test
	public void checkPermission() throws Exception {
		try {
			assert(process.checkPermission(fileId, FileAccess.SCOPE_USER, TestConstant.user_zhangsanId, PermissionHelper.PERMISSION_TYPE_READ));
			assert(!process.checkPermission(fileId, FileAccess.SCOPE_USER, TestConstant.user_zhangsanId, PermissionHelper.PERMISSION_TYPE_DOWNLOAD));
			assert(!process.checkPermission(fileId, FileAccess.SCOPE_USER, TestConstant.user_lisiId, PermissionHelper.PERMISSION_TYPE_READ));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

}
