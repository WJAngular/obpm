package cn.myapps.core.resource.action;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import cn.myapps.base.action.BaseHelper;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.constans.Environment;
import cn.myapps.constans.Web;
import cn.myapps.core.domain.ejb.DomainProcess;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.core.permission.action.PermissionHelper;
import cn.myapps.core.resource.ejb.ResourceProcess;
import cn.myapps.core.resource.ejb.ResourceType;
import cn.myapps.core.resource.ejb.ResourceVO;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.sequence.Sequence;

public class ResourceHelper extends BaseHelper<ResourceVO> {

	/**
	 * @SuppressWarnings 工厂方法不支持泛型
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public ResourceHelper() throws ClassNotFoundException {
		super(ProcessFactory.createProcess(ResourceProcess.class));
	}

	public String getName(String type) {
		String name = ResourceType.getName(type);
		return name;
	}

	public Collection<ResourceVO> deepSearchResouece(String resourceid) throws Exception {
		Collection<ResourceVO> rtn = new ArrayList<ResourceVO>();
		ResourceProcess rp = ((ResourceProcess) process);
		ResourceVO start = (ResourceVO) rp.doView(resourceid);
		if (start != null) {
			Collection<ResourceVO> allResource = rp.doSimpleQuery(null, getApplicationid());
			Collection<ResourceVO> underlist = rp.deepSearchResouece(allResource, start, null, 0);
			rtn.add(start); // 包含当前菜单
			for (Iterator<ResourceVO> iter = underlist.iterator(); iter.hasNext();) {
				ResourceVO res = (ResourceVO) iter.next();
					rtn.add(res);
			}
		}
		return rtn;
	}

	/**
	 * 获取当前菜单的直属下级菜单
	 * 
	 * @param resourceid
	 * @param deep
	 * @return
	 * @throws Exception
	 */
	public Collection<ResourceVO> searchSubResource(String resourceid, int deep,String domain) throws Exception {
		Collection<ResourceVO> rtn = new ArrayList<ResourceVO>();
		ResourceProcess rp = ((ResourceProcess) process);
		ParamsTable params = new ParamsTable();
		params.setParameter("_orderby", "orderno");
		params.setParameter("xi_type", ResourceType.RESOURCE_TYPE_MOBILE);
		params.setParameter("xi_showType", ResourceVO.SHOW_TYPE_FLOW_CENTER);
		ResourceVO start = (ResourceVO) rp.doView(resourceid);
		if (start != null) {
			Collection<ResourceVO> allResource = rp.doSimpleQuery(params, getApplicationid());
			Collection<ResourceVO> underlist = rp.deepSearchResouece(allResource, start, null, deep);

			for (Iterator<ResourceVO> iter = underlist.iterator(); iter.hasNext();) {
				ResourceVO res = (ResourceVO) iter.next();
					ResourceAction.replaceDescription(res);
					rtn.add(res);
			}
		}
		return rtn;
	}
	
	/**
	 * 获取当前菜单的直属下级菜单
	 * 
	 * @param resourceid
	 * @param deep
	 * @return
	 * @throws Exception
	 */
	public Collection<ResourceVO> searchSubResource4flowCenter(String resourceid, int deep,String domain) throws Exception {
		Collection<ResourceVO> rtn = new ArrayList<ResourceVO>();
		ResourceProcess rp = ((ResourceProcess) process);
		ParamsTable params = new ParamsTable();
		params.setParameter("_orderby", "orderno");
		params.setParameter("xi_type", ResourceType.RESOURCE_TYPE_MOBILE);
		params.setParameter("xi_showType", ResourceVO.SHOW_TYPE_MENU);
		ResourceVO start = (ResourceVO) rp.doView(resourceid);
		if (start != null) {
			Collection<ResourceVO> allResource = rp.doSimpleQuery(params, getApplicationid());
			Collection<ResourceVO> underlist = rp.deepSearchResouece(allResource, start, null, deep);

			for (Iterator<ResourceVO> iter = underlist.iterator(); iter.hasNext();) {
				ResourceVO res = (ResourceVO) iter.next();
					ResourceAction.replaceDescription(res);
					rtn.add(res);
			}
		}
		return rtn;
	}
	
	/**
	 * 获取当前手机菜单的直属下级菜单
	 * 
	 * @param resourceid
	 * @param deep
	 * @return
	 * @throws Exception
	 */
	public Collection<ResourceVO> searchMobileSubResource(String resourceid, int deep,String domain) throws Exception {
		Collection<ResourceVO> rtn = new ArrayList<ResourceVO>();
		ResourceProcess rp = ((ResourceProcess) process);
		ParamsTable params = new ParamsTable();
		params.setParameter("_orderby", "orderno");
		params.setParameter("xi_type", ResourceType.RESOURCE_TYPE_HTML);
		params.setParameter("xi_showType", ResourceVO.SHOW_TYPE_FLOW_CENTER);
		ResourceVO start = (ResourceVO) rp.doView(resourceid);
		if (start != null) {
			Collection<ResourceVO> allResource = rp.doSimpleQuery(params, getApplicationid());
			Collection<ResourceVO> underlist = rp.deepSearchResouece(allResource, start, null, deep);

			for (Iterator<ResourceVO> iter = underlist.iterator(); iter.hasNext();) {
				ResourceVO res = (ResourceVO) iter.next();
					ResourceAction.replaceDescription(res);
					rtn.add(res);
			}
		}
		return rtn;
	}
	
	
	public String getHtml(Collection<ResourceVO> cols) throws Exception{
		Environment ev=Environment.getInstance();
		HttpSession session = ServletActionContext.getRequest().getSession();
		WebUser user = (WebUser) session.getAttribute(Web.SESSION_ATTRIBUTE_FRONT_USER);
		String skinType = (String)session.getAttribute("SKINTYPE");
		StringBuffer html=new StringBuffer();
		PermissionHelper ph=new PermissionHelper();
		Boolean isTop=true;
		html.append(getHtml(cols,ev,user,skinType,ph,isTop));
		return html.toString();
	}
	
	public String getHtml(Collection<ResourceVO> cols,Environment ev,WebUser user,String skinType,PermissionHelper ph,boolean isTop) throws Exception{
		StringBuffer html=new StringBuffer();
		html.append("<ul>");
		for(Iterator<ResourceVO> iter=cols.iterator();iter.hasNext();){
			ResourceVO res=(ResourceVO) iter.next();
			if(ph.checkPermission(res, res.getApplicationid(), user)){
				Collection<ResourceVO> submenus=this.searchSubResource(res.getId(),1,res.getDomainid());
				if(isTop){
					if(submenus.size()>0){
						html.append("<li class='hassubmenus' id='"+res.getId()+"'><img src='"+ev.getContextPath()+"/portal/"+skinType+"/resource/images/down.gif'/>");
					}else{
						html.append("<li class='nosubmenus' id='"+res.getId()+"'>");
					}
				}else{
					if(submenus.size()>0){
						html.append("<li class='subhassubmenus' id='"+res.getId()+"'><img src='"+ev.getContextPath()+"/portal/"+skinType+"/resource/images/down.gif'/>");
					}else{
						html.append("<li id='"+res.getId()+"'>");
					}
				}
				html.append("<a title='"+res.getDescription()+"' href='"+res.toUrlString(user,ServletActionContext.getRequest())+"' class='first'  target='detail'>"+res.getDescription()+"</a>");
				if(submenus.size()>0){
					html.append(getHtml(submenus,ev,user,skinType,ph,false));
				}
				html.append("</li>");
			}
		}
		html.append("</ul>");
		return html.toString();
	}

	public Collection<ResourceVO> searchResourceForMb(ResourceVO startNode, String domain) throws Exception {
		Collection<ResourceVO> rtn = new ArrayList<ResourceVO>();
		ResourceProcess rp = ((ResourceProcess) process);
		if (startNode != null) {
			ParamsTable params = new ParamsTable();
			params.setParameter("_orderby", "orderno");
			params.setParameter("s_type", ResourceType.RESOURCE_TYPE_MOBILE);
			Collection<ResourceVO> allResource = rp.doSimpleQuery(params, getApplicationid());
			Collection<ResourceVO> underlist = rp.deepSearchResouece(allResource, startNode, null, 1);
			
			for (Iterator<ResourceVO> iter = underlist.iterator(); iter.hasNext();) {
				ResourceVO vo = (ResourceVO) iter.next();
//				if (startNode == null) {
//					if (vo.getSuperior() == null
//							&& (vo.getIsview() == null || vo.getIsview().equals("public") || (vo.getIsview().equals(
//									"private")
//									&& vo.getColids() != null && vo.getColids().indexOf(domain) >= 0))) {
//						rtn.add(vo);
//					}
//				} else 
					
					if (vo.getSuperior() != null) {
						ResourceVO superior = vo.getSuperior();
						while (superior != null) {
							if (superior.getId().equals(startNode.getId())) {
								rtn.add(vo);
								break;
							}
							superior = superior.getSuperior();
						}
					}
			}
		}
		return rtn;
	}

	public ResourceVO getResourcById(String resourceid) throws Exception {
		ResourceProcess rp = ((ResourceProcess) process);
		return (ResourceVO) rp.doView(resourceid);

	}

	public ResourceVO getTopResourceByName(String name) throws Exception {
		ResourceProcess rp = ((ResourceProcess) process);
		return (ResourceVO) rp.getTopResourceByName(name, getApplicationid());
	}

	public Map<String, String> getMobileIcons() {
		Map<String, String> map = new HashMap<String, String>();
		String[] names = ResourceType.MOBILEICOS;
		String[] types = ResourceType.ICOTYPES;
		for (int i = 0; i < names.length; i++) {
			map.put(types[i], names[i]);
		}
		return map;
	}

	public Map<String, String> getAllDomain() throws Exception {
		DomainProcess process = (DomainProcess) ProcessFactory.createProcess(DomainProcess.class);
		Collection<DomainVO> domains = process.getAllDomain();
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		if (domains.size() <= 0) {
			return map;
		}
		for (Iterator<DomainVO> iter = domains.iterator(); iter.hasNext();) {
			DomainVO domain = (DomainVO) iter.next();
			map.put(domain.getId(), domain.getName());
		}
		return map;

	}

	public void addReportResource(String applicationid) throws Exception {
			ResourceProcess process = (ResourceProcess) ProcessFactory.createProcess(ResourceProcess.class);
			ParamsTable params = new ParamsTable();
			params.setParameter("s_applicationid", applicationid);
			Collection<ResourceVO> colls = process.doSimpleQuery(params, null);

			if (colls.isEmpty()) {

				// 时效报表的上级菜单
				ResourceVO statReport = new ResourceVO();
				statReport.setId(Sequence.getSequence());
				statReport.setDescription("报表");
				statReport.setOrderno(2);
				statReport.setSuperior(null);
				statReport.setType("00");
				statReport.setApplication("");
				statReport.setReport("");
				statReport.setReportAppliction("");
				statReport.setReportModule("");
				statReport.setSortId(Sequence.getTimeSequence());
				statReport.setApplicationid(applicationid);
				process.doUpdate(statReport);

				// 时效报表菜单
				ResourceVO resource = new ResourceVO();
				resource.setId(Sequence.getSequence());
				resource.setDescription("时效报表");
				resource.setOrderno(2);
				resource.setSuperior(statReport);
				resource.setType("00");
				resource.setApplication("");
				resource.setReport("");
				resource.setReportAppliction("");
				resource.setReportModule("");
				resource.setSortId(Sequence.getTimeSequence());
				resource.setApplicationid(applicationid);
				process.doUpdate(resource);

				// 流程仪表盘
				ResourceVO resource2 = new ResourceVO();
				resource2.setId(Sequence.getSequence());
				resource2.setDescription("流程仪表盘");
				resource2.setOrderno(3);
				resource2.setSuperior(statReport);
				resource2.setType("00");
				resource2.setApplication("");
				resource2.setReport("");
				resource2.setReportAppliction("");
				resource2.setReportModule("");
				resource2.setSortId(Sequence.getTimeSequence());
				resource2.setApplicationid(applicationid);
				process.doUpdate(resource2);
			}
	}
	
	
	/**
	 * 获取图标库的所有图标
	 * @return
	 * @throws Exception
	 */
	public Collection<IconLibFile> getIcons(String path) throws Exception {
		Collection<IconLibFile> rtn = new ArrayList<IconLibFile>();
		Environment ev = Environment.getInstance();
		path = StringUtil.isBlank(path)? "lib/icon" : path;
		if (ev != null) {
			String contentPath = ev.getRealPath("") + path;
			File dir = new File(contentPath);
			File[] files = dir.listFiles();
			if (files != null && files.length > 0) {
				DecimalFormat format =  new DecimalFormat("#.##");
				for (int i = 0; i < files.length; i++) {
					if(files[i].isDirectory()){
						IconLibFile icon = new IconLibFile();
						icon.setName(files[i].getName());
						icon.setPath(path+"/"+files[i].getName());
						icon.setFileType(IconLibFile.DIR);
						rtn.add(icon);
					}
					else if (isImageFile(files[i].getName())) {
						IconLibFile icon = new IconLibFile();
						BufferedImage bufferedImage = ImageIO.read(files[i]);
						icon.setSize(bufferedImage.getWidth()+" x "+bufferedImage.getHeight());
						icon.setLength(format.format(files[i].length()/1024.0)+" KB");
						icon.setName(files[i].getName());
						icon.setWidth(bufferedImage.getWidth());
						icon.setPath(path+"/"+files[i].getName());
						icon.setFileType(IconLibFile.IMAGE);
						rtn.add(icon);
					}
				}
			}
		}
		return rtn;
	}
	
	/**
	 * 创建文件夹
	 * @param name
	 * @param path
	 */
	public void createFolder(String name,String path){
		Environment ev = Environment.getInstance();
		path = StringUtil.isBlank(path)? "lib/icon" : path;
		if (ev != null) {
			String contentPath = ev.getRealPath("") + path+"/"+name;
			File folder = new File(contentPath);
			if(!folder.exists()){
				folder.mkdirs();
			}
		}
	}
	
	/**
	 * 判断文件名是否为合法的图片文件格式
	 * @param name
	 * @return
	 */
	private boolean isImageFile(String name){
		name = name.toLowerCase();
		return name.indexOf("png") >= 0 || name.indexOf("ico") >= 0
		|| name.indexOf(".gif") >= 0
		|| name.indexOf(".jpg") >= 0
		|| name.indexOf(".jpge") >= 0
		|| name.indexOf(".bmp") >= 0;
	}
	
	/**
	 * 图标库文件对象
	 * @author Happy
	 *
	 */
	public class IconLibFile {
		public static final int IMAGE = 1;
		public static final int DIR =2;
		private String name;
		private String size;
		private String length;
		private int width;
		private String path;
		private int fileType;
		
		public IconLibFile() {
			super();
		}
		
		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getSize() {
			return size;
		}

		public void setSize(String size) {
			this.size = size;
		}

		public String getLength() {
			return length;
		}

		public void setLength(String length) {
			this.length = length;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public int getFileType() {
			return fileType;
		}

		public void setFileType(int fileType) {
			this.fileType = fileType;
		}
	}
	
}
