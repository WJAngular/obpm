package cn.myapps.util.cache;

public interface IMyCache {
	public IMyElement get(java.lang.Object key);
	public void put(IMyElement element); 
	public void put(Object key, Object value);
}
