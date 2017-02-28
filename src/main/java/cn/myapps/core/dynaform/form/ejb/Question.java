package cn.myapps.core.dynaform.form.ejb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

/**
 * 调查问卷的问题项
 * @author Happy
 *
 */
public class Question implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1759031369252044472L;
	
	/**
	 * 问题编号，主键。
	 */
	private int id;

	/**
	 * 标题
	 */
	private String topic;
	
	/**
	 * 选择项集合
	 */
	private List<Question.Option> options = new ArrayList<Question.Option>();
	
	
	public Question(int id,String topic) {
		super();
		this.id = id;
		this.topic = topic;
	}

	/**
	 * 添加选项
	 * @param value
	 * 		选项值
	 * @param text
	 * 		选项显示文本
	 * @param type
	 * 		选项类型
	 */
	public void addOption(String value,String text,String type){
		options.add(new Question.Option(value, text, type));
	}
	/**
	 * 添加复选框类型的选项
	 * @param value
	 * 		选项值
	 * @param text
	 * 		选项显示文本
	 */
	public void addCheckboxOption(String value,String text){
		options.add(new Question.Option(value, text, Question.Option.TYPE_CHECKBOX));
	}
	/**
	 * 添加单选框类型的选项
	 * @param value
	 * 		选项值
	 * @param text
	 * 		选项显示文本
	 */
	public void addRadioOption(String value,String text){
		options.add(new Question.Option(value, text, Question.Option.TYPE_RADIO));
	}
	
	/**
	 * 添加文本输入框选项
	 * @param value
	 * 		内容
	 */
	public void addTextOption(String value){
		addTextOption(value,"");
	}	
	
	/**
	 * 添加文本输入框选项
	 * @param value
	 * 		内容
	 * @param text
	 * 		选项区分文本
	 */
	public void addTextOption(String value,String text){
		options.add(new Question.Option(value, text, Question.Option.TYPE_TEXT));
	}
	
	/**
	 * 添加多行文本输入框选项
	 * @param value
	 * 		内容
	 */
	public void addTextareaOption(String value){
		addTextareaOption(value, "");
	}
	
	/**
	 * 添加多行文本输入框选项
	 * @param value
	 * 		内容
	 * @param text
	 * 		选项区分文本
	 */
	public void addTextareaOption(String value,String text){
		options.add(new Question.Option(value, text, Question.Option.TYPE_TEXTAREA));
	}
	
	/**
	 * 添加自定义框类型的选项
	 * @param text
	 * @param type
	 */
	public void addCustomOption(String text,String type){
		Option option = new Question.Option("", text, type);
		option.setCustom(true);
		options.add(option);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public List<Question.Option> getOptions() {
		return options;
	}

	public void setOptions(List<Question.Option> options) {
		this.options = options;
	}


	/**
	 * 问题的选择项
	 * @author Happy
	 */
	public class Option {
		
		public static final String TYPE_CHECKBOX = "checkbox";
		public static final String TYPE_RADIO = "radio";
		public static final String TYPE_TEXT = "text";
		public static final String TYPE_TEXTAREA = "textarea";
		
		/**
		 * 值
		 */
		private String value;
		
		/**
		 * 显示文本
		 */
		private String text;
		
		/**
		 * 类型
		 */
		private String type;
		
		/**
		 * 是否自定义输入
		 */
		private boolean custom = false;
		
		public Option(String value, String text, String type) {
			super();
			this.value = value;
			this.text = text;
			this.type = type;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public boolean isCustom() {
			return custom;
		}

		public void setCustom(boolean custom) {
			this.custom = custom;
		}
		
	}
	
	public static void main(String[] args) {
		Question q = new Question(1, "你的爱好（“多选”）");
		q.addCheckboxOption("羽毛球", "羽毛球");
		q.addCheckboxOption("游泳", "游泳");
		
		Question q2 = new Question(2, "您的性别？");
		q2.addRadioOption("1", "男");
		q2.addRadioOption("2", "女");
		q2.addRadioOption("0", "保密");
		
		List<Question> qs = new ArrayList<Question>();
		qs.add(q);
		qs.add(q2);
		
		System.out.println(JSONArray.fromObject(qs).toString());
	}
	
}


