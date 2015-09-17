package org.maptalks.javasdk;

public class QueryFilter {
	/**
	 * 全部自定义属性,即查询结果返回所有的自定义属性 例子:  queryFilter.setResultFields(QueryFilter.ALL_FIELDS);
	 */
	public final static String[] ALL_FIELDS = new String[] { "*" };

	/**
	 * 查询条件
	 */
	private String condition;

	/**
	 * 空间过滤filter
	 */
	private SpatialFilter spatialFilter;

	/**
	 * 坐标系类型
	 */
	private String coordinateType;

	/**
	 * 要返回的自定义属性
	 */
	private String[] resultFields;


	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public SpatialFilter getSpatialFilter() {
		return spatialFilter;
	}

	public void setSpatialFilter(SpatialFilter spatialFilter) {
		this.spatialFilter = spatialFilter;
	}

	public String[] getResultFields() {
		return resultFields;
	}

	public void setResultFields(String[] resultFields) {
		this.resultFields = resultFields;
	}

	public String getCoordinateType() {
		return coordinateType;
	}

	public void setCoordinateType(String coordinateType) {
		this.coordinateType = coordinateType;
	}
}
