package cn.myapps.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.deploy.application.ejb.ApplicationProcess;
import cn.myapps.core.deploy.application.ejb.ApplicationVO;
import cn.myapps.core.permission.ejb.PermissionVO;
import cn.myapps.core.resource.ejb.ResourceProcess;
import cn.myapps.core.resource.ejb.ResourceType;
import cn.myapps.core.resource.ejb.ResourceVO;
import cn.myapps.core.role.ejb.RoleProcess;
import cn.myapps.core.role.ejb.RoleVO;
import cn.myapps.util.sequence.Sequence;

public class MobileMenuUtil {

	public static final String DEFAULT_MENU_DES = "Mobile";

	public static void createAllMenus() throws Exception {
		ApplicationProcess process = (ApplicationProcess) ProcessFactory
				.createProcess(ApplicationProcess.class);
		Collection<ApplicationVO> apps = process.doSimpleQuery(null);
		if (apps == null)
			return;
		for (Iterator<ApplicationVO> iterator = apps.iterator(); iterator
				.hasNext();) {
			ApplicationVO app = iterator.next();
			ResourceProcess resprocess = (ResourceProcess) ProcessFactory
					.createProcess(ResourceProcess.class);
			ResourceVO vo = resprocess.getTopResourceByName("Mobile", app
					.getId());
			if (vo == null) {
				vo = new ResourceVO();
				vo.setId(Sequence.getSequence());
				vo.setType(ResourceType.RESOURCE_TYPE_MOBILE);
				vo.setApplication(app.getId());
				vo.setApplicationid(app.getId());
				vo.setMobileIco("001");
				vo.setDescription("Mobile");
				resprocess.doCreate(vo);
			}
			createFromResource(null, vo, app.getId());
		}
	}

	public static void createFromResource(ResourceVO copiedRes,
			ResourceVO destSuperior, String application) throws Exception {
		createFromResource(copiedRes, destSuperior, application, true, null);
	}

	public static void createFromResource(ResourceVO copiedRes,
			ResourceVO destSuperior, String application, boolean copyChilds,
			final List<String> include) throws Exception {
		if (StringUtil.isBlank(application))
			throw new Exception("Application can't be null!");
		ResourceProcess process = (ResourceProcess) ProcessFactory
				.createProcess(ResourceProcess.class);
		destSuperior = destSuperior == null ? process.getTopResourceByName(
				"Mobile", application) : destSuperior;
		/**
		 * 如果目标菜单为null,则默认创建名为'Mobile'手机菜单
		 */
		if (destSuperior == null) {
			destSuperior = new ResourceVO();
			destSuperior.setId(Sequence.getSequence());
			destSuperior.setType(ResourceType.RESOURCE_TYPE_MOBILE);
			destSuperior.setApplication(application);
			destSuperior.setApplicationid(application);
			destSuperior.setMobileIco("001");
			destSuperior.setDescription("Mobile");
			process.doCreate(destSuperior);
		}
		/**
		 * 如果
		 */
		if (copiedRes == null) {
			return;
		}
		/**
		 * 当被复制的菜单不为手机菜单时,则复制
		 */
		if (!ResourceType.RESOURCE_TYPE_MOBILE.equals(copiedRes.getType())) {
			/**
			 * 校验被复制菜单,是否在同级菜单下已经被复制过(即在同级菜单下是否存在同名)
			 */
			ResourceVO po = queryResourceByDes(copiedRes.getDescription(),
					destSuperior, application);
			String copiedId = copiedRes.getId();
			if (po == null
					&& (include == null || include.contains(copiedId) || include
							.size() == 0)) {
				po = new ResourceVO();
				createResource(copiedRes, po, destSuperior);
				/**
				 * 如果允许复制下级菜单,则复制
				 */
				if (copyChilds) {
					ParamsTable params = new ParamsTable();
					params.setParameter("s_superior", copiedRes.getId());
					Collection<ResourceVO> ress = process.doSimpleQuery(params,
							application);
					for (Iterator<ResourceVO> iterator = ress.iterator(); iterator
							.hasNext();) {
						ResourceVO vo = iterator.next();
						/**
						 * 迭代复制下级菜单,因为‘copyChilds’为真,因此所有的下级也被复制
						 */
						createFromResource(vo, po, application, true, include);
					}
				}
			}
		}
	}

	private static void createResource(ResourceVO vo, ResourceVO po,
			ResourceVO destSuperior) throws Exception {
		ObjectUtil.copyProperties(po, vo);
		po.setId(Sequence.getSequence());
		po.setSuperior(destSuperior);
		po.setType(ResourceType.RESOURCE_TYPE_MOBILE);
		po.setMobileIco("001");
		ResourceProcess process = (ResourceProcess) ProcessFactory
				.createProcess(ResourceProcess.class);
		PersistenceUtils.currentSession().clear();//清除session
		process.doCreate(po);
	}

	/**
	 * 根据菜单描述和菜单上级查找菜单
	 * 
	 * @param description
	 * @param superior
	 * @param application
	 * @return
	 */
	public static ResourceVO queryResourceByDes(String description,
			ResourceVO superior, String application) {
		try {
			ResourceProcess process = (ResourceProcess) ProcessFactory
					.createProcess(ResourceProcess.class);
			ParamsTable params = new ParamsTable();
			params.setParameter("s_superior", superior.getId());
			params.setParameter("s_description", description);
			Collection<ResourceVO> ress = process.doSimpleQuery(params,
					application);
			if (ress != null && !ress.isEmpty()) {
				return ress.iterator().next();
			}
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 检查或创建手机最顶级菜单'Mobile'
	 * 
	 * @param application
	 *            软件ID
	 * @return 手机最顶级菜单对象
	 * @throws Exception
	 */
	public static ResourceVO checkOrCreateMobileMenu(String application)
			throws Exception {
		ResourceProcess process = (ResourceProcess) ProcessFactory
				.createProcess(ResourceProcess.class);
		ResourceVO destSuperior = process.getTopResourceByName(
				DEFAULT_MENU_DES, application);
		if (destSuperior == null) {
			destSuperior = new ResourceVO();
			destSuperior.setId(Sequence.getSequence());
			destSuperior.setType(ResourceType.RESOURCE_TYPE_MOBILE);
			destSuperior.setApplication(application);
			destSuperior.setApplicationid(application);
			destSuperior.setMobileIco("001");
			destSuperior.setDescription(DEFAULT_MENU_DES);
			process.doCreate(destSuperior);
		}
		return destSuperior;
	}

	public static void main(String[] args) throws Exception {
		// String application = "01b84470-acfa-4f40-ae3a-a077dc6f4078";
		String application = "01b807a0-8c7f-3f80-aa27-c741f5c90259";
		ResourceProcess resprocess = (ResourceProcess) ProcessFactory
				.createProcess(ResourceProcess.class);
		ResourceVO vo = resprocess.getTopResourceByName("Mobile", application);
		createFromResource(null, vo, application);
	}
}
