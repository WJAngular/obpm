package cn.myapps.core.dynaform.form.ejb;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;
import cn.myapps.core.dynaform.form.ejb.mapping.ColumnMapping;
import cn.myapps.core.dynaform.form.ejb.mapping.TableMapping;

public class FormTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetMapping() {
		Form form = new Form();
		form.setName("test");
		String json = "{\"formName\": \"测试001\", \"tableName\": \"auth_airportcode\", \"columnMappings\": [{\"fieldName\": \"映射A\", \"columnName\": \"ID\"}, {\"fieldName\": \"映射B\", \"columnName\": \"DOC_ID\"}]}";
		form.setMappingStr(json);
		TableMapping mapping = form.getTableMapping();
		Collection<ColumnMapping> columnMappings = mapping.getColumnMappings();
		for (Iterator<ColumnMapping> iterator = columnMappings.iterator(); iterator.hasNext();) {
			ColumnMapping columnMapping = (ColumnMapping) iterator.next();
			assertNotNull(columnMapping);
		}
	}

}
