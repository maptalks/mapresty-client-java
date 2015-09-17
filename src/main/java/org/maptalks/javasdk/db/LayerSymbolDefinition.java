package org.maptalks.javasdk.db;

/**
 * symbol配置类
 * @author fuzhen
 *
 */
public class LayerSymbolDefinition {
	/**
	 * 数据匹配条件
	 */
	private String condition;
	/**
	 * 符合条件的数据的symbol
	 */
	private Symbol symbol;

	public LayerSymbolDefinition() {

	}

	public LayerSymbolDefinition(String condition, Symbol symbol) {
		super();
		this.condition = condition;
		this.symbol = symbol;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public Symbol getSymbol() {
		return symbol;
	}

	public void setSymbol(Symbol symbol) {
		this.symbol = symbol;
	}
}
