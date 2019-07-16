package com.iist.core.importdb.arr.common.annotation;

public abstract class Model {
	@Header( indexHeader = 2)
	public int indexHeader = 2;

	public int getIndexHeader() {
		return indexHeader;
	}

	public void setIndexHeader(int indexHeader) {
		this.indexHeader = indexHeader;
	}
	

}
