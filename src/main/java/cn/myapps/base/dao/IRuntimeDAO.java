package cn.myapps.base.dao;


public interface IRuntimeDAO extends IBaseDAO {
	public void create(ValueObject vo) throws Exception;

	public void remove(String pk) throws Exception;

	public void update(ValueObject vo) throws Exception;

	public ValueObject find(String id) throws Exception;
}
