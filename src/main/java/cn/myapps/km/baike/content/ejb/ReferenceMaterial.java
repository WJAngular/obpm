package cn.myapps.km.baike.content.ejb;


import java.util.Date;
import cn.myapps.km.base.ejb.NObject;



/**
 * @author abel
 * 参考资料类
 */
public class ReferenceMaterial extends NObject{

	
		private static final long serialVersionUID = -2016559560293170026L;
		
		/**
		 * 词条内容的Id
		 */
		private String entryContentId;
		
		/**
		 * 文章名
		 */
		private String articleName;
		
		/**
		 * URL
		 */
		private String url;
		
		/**
		 * 网站名
		 */
		private String webName;
		
		/**
		 * 发表日期
		 */
		private Date publishDate;
		
		/**
		 * 引用日期
		 */
		private Date referenceDate;

		
		public String getEntryContentId() {
			return entryContentId;
		}

		public void setEntryContentId(String entryContentId) {
			this.entryContentId = entryContentId;
		}

		public String getArticleName() {
			return articleName;
		}

		public void setArticleName(String articleName) {
			this.articleName = articleName;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getWebName() {
			return webName;
		}

		public void setWebName(String webName) {
			this.webName = webName;
		}

		public Date getPublishDate() {
			return publishDate;
		}

		public void setPublishDate(Date publishDate) {
			this.publishDate = publishDate;
		}

		public Date getReferenceDate() {
			return referenceDate;
		}

		public void setReferenceDate(Date referenceDate) {
			this.referenceDate = referenceDate;
		}
		
		/**
		 * 覆盖equals方法
		 */
		@Override
		public boolean equals(Object obj) {
			System.out.println(obj);
			if(obj==null){
				return false;
			}else if(obj instanceof ReferenceMaterial){
				ReferenceMaterial rm=(ReferenceMaterial)obj;
				return (this.getId().equals(rm.getId()))
					 &&(this.getEntryContentId().equals(rm.getEntryContentId()))
					   &&(this.getArticleName().equals(rm.getArticleName()))
					    &&(this.getUrl().equals(rm.getUrl()))
					       &&(this.getWebName().equals(rm.getWebName()));
			}
			return false;
		}
	
	
}
