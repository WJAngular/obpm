package cn.myapps.core.workflow.engine.instruction;


import java.lang.reflect.Method;

import cn.myapps.base.dao.ValueObject;
import cn.myapps.base.ejb.IRunTimeProcess;

/**
 * 数据库持久化指令
 * @author Happy
 *
 */
public class PersistentInstruction implements Instruction {

	/**
	 * 保存指令
	 */
	public static final int ACTION_CREATE = 1;
	/**
	 * 更新指令
	 */
	public static final int ACTION_UPDATE = 2;
	/**
	 * 删除指令
	 */
	public static final int ACTION_DELETE = 3;
	
	/**
	 * 自定义指令
	 */
	public static final int ACTION_CUSTOM = 4;
	
	private IRunTimeProcess<?> process;

	/**
	 * 指令内容（参数）
	 */
	private Object content;

	/**
	 * 指令类型
	 */
	private int actionType;
	
	/**
	 * 自定义指令名称
	 */
	private String methodName;
	
	/**
	 * 自定义指令参数
	 */
	private Object[] args;
	
	
	/**
	 * @param process
	 * 		业务类实例对象
	 * @param content
	 * 		指令内容（参数）
	 * @param actionType
	 * 		指令类型
	 */
	public PersistentInstruction(IRunTimeProcess<?> process, Object content,
			int actionType) {
		super();
		this.process = process;
		this.content = content;
		this.actionType = actionType;
	}
	
	/**
	 * 
	 * @param process
	 * 		业务类实例对象
	 * @param methodName
	 * 		自定义指令名称
	 * @param args
	 * 		自定义指令参数
	 */
	public PersistentInstruction(IRunTimeProcess<?> process,
			String methodName, Object[] args) {
		super();
		this.process = process;
		this.actionType = ACTION_CUSTOM;
		this.methodName = methodName;
		this.args = args;
	}



	@Override
	public void setContent(Object content) {
		this.content = content;
	}

	@Override
	public void run() throws InstructionException {
		switch (actionType) {
		case PersistentInstruction.ACTION_CREATE:
			this.create();
			break;
		case PersistentInstruction.ACTION_UPDATE:
			this.update();
			break;
		case PersistentInstruction.ACTION_DELETE:
			this.delete();
			break;
		case PersistentInstruction.ACTION_CUSTOM:
			this.custom();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 创建
	 * @throws InstructionException
	 */
	private void create() throws InstructionException{
		
		try {
			process.doCreate((ValueObject) content);
		} catch (Exception e) {
			throw new cn.myapps.core.workflow.engine.instruction.InstructionException(e.getMessage(), e);
		}
	}
	
	/**
	 * 更新
	 * @throws InstructionException
	 */
	private void update() throws InstructionException{
		
		try {
			process.doUpdate((ValueObject) content);
		} catch (Exception e) {
			throw new cn.myapps.core.workflow.engine.instruction.InstructionException(e.getMessage(), e);
		}
	}
	
	/**
	 * 删除
	 * @throws InstructionException
	 */
	private void delete() throws InstructionException {
		
		try {
			process.doRemove((String) content);
		} catch (Exception e) {
			throw new cn.myapps.core.workflow.engine.instruction.InstructionException(e.getMessage(), e);
		}
		
	}
	
	/**
	 * 执行自定义指令动作
	 * @throws InstructionException
	 */
	private void custom() throws InstructionException {
		try{
		    Class<?>[] argsClass = new Class[args.length];
		 
		    for (int i = 0, j = args.length; i < j; i++) {
		        argsClass[i] = args[i].getClass();
		    }
		 
		    Method method = process.getClass().getMethod(methodName, argsClass);
		    method.invoke(process, args);
		}catch (Exception e) {
			throw new cn.myapps.core.workflow.engine.instruction.InstructionException(e.getMessage(), e);
		}
	}
	
}
