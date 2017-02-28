package cn.myapps.util.xml.mapper;

import com.thoughtworks.xstream.alias.ClassMapper;
import com.thoughtworks.xstream.mapper.Mapper;
import com.thoughtworks.xstream.mapper.MapperWrapper;

@SuppressWarnings("deprecation")
public class OmitNotExistsFiledMapper extends MapperWrapper {

	public OmitNotExistsFiledMapper(Mapper wrapped) {
		super(wrapped);
	}

	public OmitNotExistsFiledMapper(ClassMapper wrapped) {
		this((Mapper) wrapped);
	}

	/**
	 * @SuppressWarnings shouldSerializeMember方法不支持泛型
	 */
	@SuppressWarnings("unchecked")
	public boolean shouldSerializeMember(Class definedIn, String fieldName) {
		return isFieldExists(definedIn, fieldName)
				&& super.shouldSerializeMember(definedIn, fieldName);
	}

	private boolean isFieldExists(Class<?> definedIn, String fieldName) {
		try {
			definedIn.getDeclaredField(fieldName);
		} catch (SecurityException e) {
			e.printStackTrace();
			return false;
		} catch (NoSuchFieldException e) {
			return false;
		}
		return true;
	}
}
