package org.maptalks.javasdk.db;

/**
 * 图层自定义属性类，用来定义图层上的自定义属性信息
 * @author duscin
 *
 */
public class LayerField {
	/**
	 * 属性名
	 */
	private String fieldName;

	/**
	 * 数据类型，例如VARCHAR, INT
	 */
	private String dataType;
	/**
	 * 数据位宽，例如VARCHAR(32)中的32
	 */
	private int fieldSize;
	/**
	 * 小数点后位宽，例如NUMBER(10,3)中的3
	 */
	private int decimalSize;
	/**
	 * 是否可以为空，如果为0，则可以为空，如果为1，则不能为空
	 */
	private int notNull = 0;

	public LayerField() {

	}

	public LayerField(String fieldName, String dataType) {
		super();
		this.fieldName = fieldName;
		this.dataType = dataType;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public int getFieldSize() {
		return fieldSize;
	}

	public void setFieldSize(int fieldSize) {
		this.fieldSize = fieldSize;
	}

	public int getDecimalSize() {
		return decimalSize;
	}

	public void setDecimalSize(int decimalSize) {
		this.decimalSize = decimalSize;
	}

	public int getNotNull() {
		return notNull;
	}

	public void setNotNull(int notNull) {
		this.notNull = notNull;
	}

}
