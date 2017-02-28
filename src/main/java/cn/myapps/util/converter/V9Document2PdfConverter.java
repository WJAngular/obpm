package cn.myapps.util.converter;

import org.apache.log4j.Logger;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;


/**
 * 基于 WPS V9版本 的PDF文档转换器
 * @author Happy
 *
 */
public class V9Document2PdfConverter extends Document2PdfConverter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6650214905685005929L;
	
	Logger log = Logger.getLogger(getClass());
	

	public void word2Pdf(String source, String destPath, String fileName)
			throws Exception {

		ActiveXComponent wps = null;
		Dispatch documents = null;
		Dispatch document = null;
		try {
			ComThread.InitMTA(true);
			wps = new ActiveXComponent("Kwps.application");
			Dispatch.put(wps.getObject(), "Visible", new Variant(false));
			Dispatch.put(wps.getObject(), "DisplayAlerts", new Variant(0));

			documents = wps.getProperty("Documents").toDispatch();
			document = Dispatch.invoke(
					documents,
					"Open",
					Dispatch.Method,
					new Object[] { source, false, true, false, "", "", true,
							"", "", null, null, null, null, null, true },
					new int[1]).toDispatch();
			Dispatch.invoke(document, "ExportAsFixedFormat", Dispatch.Method,
					new Object[] { destPath + fileName ,17}, new int[1]);

		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (document != null) {
					Dispatch.invoke(document, "Close", Dispatch.Method,
							new Object[] { false }, new int[1]);
					document.safeRelease();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (wps != null) {
						wps.invoke("Quit", new Variant(false));
					}
				} catch (Exception e) {
					wps.invoke("Terminate");
				} finally {
					if (documents != null)
						documents.safeRelease();
					if (wps != null)
						wps.safeRelease();
					
					ComThread.Release();
				}
			}
		}
	}

	public void excel2Pdf(String source, String destPath, String fileName)
			throws Exception {
		ActiveXComponent wps = null;
		ActiveXComponent doc = null;
		try {
			ComThread.InitMTA(true);
			wps = new ActiveXComponent("Ket.application");
			Dispatch.put(wps.getObject(), "Visible", new Variant(false));

			Dispatch.put(wps.getObject(), "DisplayAlerts", new Variant(false));

			doc = wps.invokeGetComponent("Workbooks").invokeGetComponent(
					"Open", new Variant(source));
			doc.invoke("ExportAsFixedFormat", new Variant[]{new Variant(0),new Variant(destPath + fileName)});
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (doc != null) {
				doc.invoke("Close");

				doc.safeRelease();
			}
			if (wps != null) {
				wps.invoke("Quit");
				//wps.invoke("Terminate", new Variant(true));
				wps.safeRelease();
			}
			ComThread.Release();
		}

	}
	
	public void ppt2Pdf(String source, String destPath, String fileName)
	throws Exception {

		ActiveXComponent wps = null;
		Dispatch documents = null;
		Dispatch document = null;
		try {
			ComThread.InitMTA(true);
			wps = new ActiveXComponent("Kwpp.application");
			Dispatch.put(wps.getObject(), "DisplayAlerts", new Variant(0));
		
			documents = wps.getProperty("Presentations").toDispatch();
			document = Dispatch.invoke(
					documents,
					"Open",
					Dispatch.Method,
					new Object[] { source, true,// ReadOnly  
						 true,// Untitled指定文件是否有标题  
	                     false// WithWindow指定文件是否可见  
							},
					new int[1]).toDispatch();
			Dispatch.invoke(document, "SaveAs", Dispatch.Method,
					new Object[] { destPath + fileName ,32}, new int[1]);
		
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (document != null) {
					Dispatch.invoke(document, "Close", Dispatch.Method,
							new Object[] { }, new int[1]);
					document.safeRelease();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (wps != null) {
						wps.invoke("Quit");
					}
				} catch (Exception e) {
					wps.invoke("Terminate");
				} finally {
					if (documents != null)
						documents.safeRelease();
					if (wps != null)
						wps.safeRelease();
					
					ComThread.Release();
				}
			}
		}
	}

	public static void main(String[] args) {
		V9Document2PdfConverter c = new V9Document2PdfConverter();
		try {
			c.convert("d:\\ppt.pptx", "d:\\", "ppt.pdf");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}

