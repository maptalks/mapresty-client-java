package org.maptalks.javasdk.db;

import java.util.ArrayList;
import java.util.List;

public class LayerSymbolConfig {
	private List<LayerSymbolDefinition> symbolDefs;

	public LayerSymbolConfig() {
		symbolDefs = new ArrayList<LayerSymbolDefinition>();
	}

	public List<LayerSymbolDefinition> getSymbolDefs() {
		return symbolDefs;
	}

	public void setSymbolDefs(List<LayerSymbolDefinition> symbolDefs) {
		this.symbolDefs = symbolDefs;
	}

	/**
	 * 加入新的symbol定义，如果condition已经定义过，则覆盖之前的symbol配置
	 * @param condition
	 * @param symbol
	 */
	public void saveOrUpdateSymbolDefinition(String condition, Symbol symbol) {
		if (condition == null)
			return;
		int hitIndex = matchSymbolDef(condition);
		if (hitIndex < 0) {
			symbolDefs.add(new LayerSymbolDefinition(condition, symbol));
			return;
		}
		if (symbol == null) {
			removeSymbolDefinition(condition);
			return;
		}
		symbolDefs.get(hitIndex).setSymbol(symbol);

	}

	/**
	 * 删除condition配置
	 * @param condition
	 */
	public void removeSymbolDefinition(String condition) {
		if (condition == null)
			return;
		int hitIndex = matchSymbolDef(condition);
		if (hitIndex > -1) {
			symbolDefs.remove(hitIndex);
		}
	}

	private int matchSymbolDef(String condition) {
		int hitIndex = -1;
		for (int i = 0, len = symbolDefs.size(); i < len; i++) {
			LayerSymbolDefinition symbolDef = symbolDefs.get(i);
			if (condition.trim().equals(symbolDef.getCondition().trim())) {
				hitIndex = i;
				break;
			}
		}
		return hitIndex;
	}

}
