package org.maptalks.javasdk.db;
import java.util.List;


public class Layer {
	public static final String TYPE_DB_TABLE = "db_table";
	public static final String TYPE_DB_VIEW = "db_view";
	public static final String TYPE_DB_SPATIAL_TABLE = "db_spatial_table";
	public static final String TYPE_DB_SPATIAL_VIEW = "db_spatial_view";
	public static final String TYPE_FILE_SHP = "file_shp";
	// ------------------------------------------------------
	private String id;
	private String name;
	private String type = TYPE_DB_TABLE;
	private String source;
	private List<LayerField> fields;
	private LayerSymbolConfig symbolConfig;

	public LayerSymbolConfig getSymbolConfig() {
		if (symbolConfig == null)
			symbolConfig = new LayerSymbolConfig();
		return symbolConfig;
	}

	public void setSymbolConfig(LayerSymbolConfig symbolConfig) {
		this.symbolConfig = symbolConfig;
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}


	public List<LayerField> getFields() {
		return fields;
	}

	public void setFields(List<LayerField> fields) {
		this.fields = fields;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

}
