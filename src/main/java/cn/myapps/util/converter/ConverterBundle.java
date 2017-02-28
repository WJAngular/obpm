package cn.myapps.util.converter;

import cn.myapps.km.disk.ejb.NFile;

/**
 * 文件转换处理包
 * @author xiuwei
 *
 */
public class ConverterBundle {
		
		private String type;
		private NFile file;//TODO 换成泛型，与KM解耦
		private String source;
		private String uuid;
		
		public ConverterBundle(String type, NFile file, String source) {
			super();
			this.type = type;
			this.file = file;
			this.source = source;
			this.uuid = file.getId();
		}
		
		public ConverterBundle(String type, String source, String uuid) {
			super();
			this.type = type;
			this.source = source;
			this.uuid = uuid;
		}


		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public NFile getFile() {
			return file;
		}
		public void setFile(NFile file) {
			this.file = file;
		}
		public String getSource() {
			return source;
		}
		public void setSource(String source) {
			this.source = source;
		}
		public String getUuid() {
			return uuid;
		}
		public void setUuid(String uuid) {
			this.uuid = uuid;
		}
		
	}