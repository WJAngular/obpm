package cn.myapps.km.permission.ejb;


import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.myapps.km.TestConstant;
import cn.myapps.km.disk.ejb.NFileProcess;
import cn.myapps.km.disk.ejb.NFileProcessBean;
import cn.myapps.km.util.PersistenceUtils;
import cn.myapps.km.util.Sequence;

public class FileAccessProcessBeanTest {
	
	private FileAccessProcess process = null;
	
	private final String id = "fileaccessid-123-123-123-0001";
	private final String fileId = "fileid-123-123-123-0001";
	@Before
	public void setUp() throws Exception {
		process = new FileAccessProcessBean();
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
			vo.setDownloadMode(FileAccess.DOWNLOAD_MODE_TRUE);
			vo.setReadMode(FileAccess.READ_MODE_FALSE);
			vo.setWriteMode(FileAccess.WRITE_MODE_FALSE);
			vo.setPrintMode(FileAccess.PRINT_MODE_TRUE);
			
			process.doCreate(vo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void checkPermission() throws Exception {
		try {
			assert(!process.checkPermission(fileId, FileAccess.SCOPE_USER, TestConstant.user_zhangsanId, PermissionHelper.PERMISSION_TYPE_READ));
			assert(process.checkPermission(fileId, FileAccess.SCOPE_USER, TestConstant.user_zhangsanId, PermissionHelper.PERMISSION_TYPE_DOWNLOAD));
			assert(!process.checkPermission(fileId, FileAccess.SCOPE_USER, TestConstant.user_lisiId, PermissionHelper.PERMISSION_TYPE_DOWNLOAD));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	@Test
	public void check() throws Exception {
		try {
			String fileId = "11e2-8d1f-63bf24f4-83c8-cfe8266203ad";//第三层目录下的文件
			String dirId = "11e2-8d1f-29397470-83c8-cfe8266203ad";//顶级已授权的文件夹
			
			//1第一步 一级文件夹授权给张三（可读 、不可写、 不可下载）
			DirAccessProcess process = new DirAccessProcessBean();
			String id = Sequence.getSequence();
			DirAccess vo = new DirAccess();
			vo.setId(id);
			vo.setFileId(dirId);
			vo.setScope(IFileAccess.SCOPE_USER);
			vo.setOwnerId(TestConstant.user_zhangsanId);
			vo.setDownloadMode(IFileAccess.DOWNLOAD_MODE_FALSE);
			vo.setReadMode(IFileAccess.READ_MODE_TRUE);
			vo.setWriteMode(IFileAccess.WRITE_MODE_FALSE);
			vo.setPrintMode(IFileAccess.PRINT_MODE_FALSE);
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			vo.setStartDate(format.parse("2013-3-18 11:55:00"));
			vo.setEndDate(format.parse("2013-3-18 22:55:00"));
			process.doCreate(vo);
			
			//2.第二步  检查张三是否有第三层目录的文件的一些权限
			
			//用权限继承的方式找到与用户对应的授信息
			IFileAccess access = PermissionHelper.findIFileAccessWithInheritance(PermissionHelper.FILE_TYPE_FILE, fileId, IFileAccess.SCOPE_USER,  TestConstant.user_zhangsanId);
			
			assert(access !=null);
			
			boolean read = PermissionHelper.check(access,PermissionHelper.PERMISSION_TYPE_READ);//是否有阅读的权限
			boolean write = PermissionHelper.check(access, PermissionHelper.PERMISSION_TYPE_WRITE);//是否有写入的权限
			boolean download = PermissionHelper.check(access, PermissionHelper.PERMISSION_TYPE_DOWNLOAD);//是否有下载的权限
			
			assert(read);
			assert(!write);
			assert(!download);
			
			process.doRemove(id);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	

}
