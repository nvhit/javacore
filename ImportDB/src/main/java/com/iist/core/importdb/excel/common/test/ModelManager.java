package com.iist.core.importdb.excel.common.test;

import com.iist.core.importdb.arr.common.annotation.Table;

public class ModelManager {
	protected Class<?> type;
	protected String id;
	protected String name;

	// List save fields
	//private List<Field> columns = new ArrayList<Field>();

	// Contructor
	public ModelManager(Class<?> type) {
		this.type = type;
		Table an = type.getAnnotation(Table.class);
		if (an != null) {
			id = an.id();
			name = an.name();
		} else {
			id = Table.ID_NAME;
			name = type.getSimpleName();
		}
	}
}
