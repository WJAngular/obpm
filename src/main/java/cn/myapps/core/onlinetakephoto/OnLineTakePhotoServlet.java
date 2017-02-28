package cn.myapps.core.onlinetakephoto;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.constans.Environment;
import cn.myapps.util.StringUtil;
import cn.myapps.util.property.DefaultProperty;
import cn.myapps.util.sequence.Sequence;

/**
 * Servlet implementation class onLineTakePhotoServlet
 */
public class OnLineTakePhotoServlet extends HttpServlet {

	private static final long serialVersionUID = -138681845395518146L;

	private static final Logger Log = Logger
			.getLogger(OnLineTakePhotoServlet.class);

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String type = request.getParameter("type");
		if (!StringUtil.isBlank(type) && type.equals("android")) {
			doAndroidUploadFile(request, response);
		} else {
			processRequest(request, response);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String type = request.getParameter("type");
		if (!StringUtil.isBlank(type) && type.equals("android")) {
			doAndroidUploadFile(request, response);
		} else {
			processRequest(request, response);
		}
	}

	/**
	 * android端在线拍照上传方法
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doAndroidUploadFile(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			// 获得文件项工厂
			FileItemFactory factory = new DiskFileItemFactory();
			// 获得Servlet文件上传对象
			ServletFileUpload upload = new ServletFileUpload(factory);
			String fileName = request.getParameter("fileName");
			response.setContentType("text/html");
			try {
				// 解析请求
				List items = upload.parseRequest(request);
				Iterator iter = items.iterator();
				// 迭代文件域
				while (iter.hasNext()) {
					FileItem item = (FileItem) iter.next();
					// 判断是普通文本域
					if (!item.isFormField()) {
						// 判断是文件域
						response.setContentType("text/xml");
						response.getWriter().write(common(item.get(), ""));
					}
				}

			} catch (Exception e) {
				response.getWriter().write("");
			}
		} catch (Exception e) {
			response.getWriter().write("");
		} finally {
			try {
				PersistenceUtils.closeSessionAndConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private String common(byte[] data, String filePath) throws Exception {
		String path = "";
		if (StringUtil.isBlank(filePath)) {
			// 判断路径是否存在,若不存在则创建路径
			path = Environment.getInstance().getRealPath(
					DefaultProperty.getProperty("ONLINE_TAKE_PHONO_PATH"));
			File upDir = new File(path);
			if (!upDir.exists()) {
				if (!upDir.mkdir()) {
					Log.warn("Failed to create folder (" + filePath + ")");
					throw new IOException("Failed to create folder ("
							+ filePath + ")");
				}
			}
			// 生成随机文件名
			String saveName = Sequence.getSequence();
			String fileName = saveName + ".jpg";
			filePath = DefaultProperty.getProperty("ONLINE_TAKE_PHONO_PATH")
					+ "//" + fileName;
			path += "//" + fileName;
		} else {
			path = Environment.getInstance().getRealPath(filePath);
		}
		// 写图片
		File f = new File(path);
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(f));
		dos.write(data);
		dos.flush();
		dos.close();
		return filePath;
	}

	public void processRequest(HttpServletRequest request,
			HttpServletResponse response)

	throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		String filePath = request.getParameter("filePath");
		String bitmap_data = request.getParameter("bitmap_data");
		int width = Integer.parseInt(request.getParameter("width"));
		int height = Integer.parseInt(request.getParameter("height"));
		BufferedImage img = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		try {
			int w = width;
			int h = height;
			int[] pixels = new int[w * h];
			String[] m_tempPics = bitmap_data.split(",");
			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
					Long pic_argb = Long.parseLong(m_tempPics[x * h + y]);
					int a = (int) (pic_argb >> 24 & 0xFF);
					int r = (int) (pic_argb >> 16 & 0xFF);
					int g = (int) (pic_argb >> 8 & 0xFF);
					int b = (int) (pic_argb & 0xFF);
					pixels[y * w + x] = new Color(r, g, b, a).getRGB();
				}
			}
			img.setRGB(0, 0, w, h, pixels, 0, w);
			img.flush();
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			ImageIO.write(img, "jpg", bao);
			byte[] data = bao.toByteArray();
			if (!StringUtil.isBlank(filePath)) {
				if (filePath.indexOf(DefaultProperty
						.getProperty("ONLINE_TAKE_PHONO_PATH")) == -1) {
					filePath = "";
				}
			} else {
				filePath = "";
			}
			response.setContentType("text/xml");
			response.getWriter().write(common(data, filePath));
		} catch (Exception ex) {
			ex.printStackTrace();
			response.setContentType("text/xml");
			response.getWriter().write("保存失败");
			throw new IOException(ex.getMessage());
		}
	}

}
