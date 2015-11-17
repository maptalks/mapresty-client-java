package org.maptalks.javasdk.db;
import java.util.List;
import java.util.Map;


public class Layer {
	public static final String TYPE_DB_TABLE = "db_table";
	public static final String TYPE_DB_VIEW = "db_view";
	public static final String TYPE_DB_SPATIAL_TABLE = "db_spatial_table";
	public static final String TYPE_DB_SPATIAL_VIEW = "db_spatial_view";
	public static final String TYPE_FILE_SHP = "file_shp";
    //---------以下定义了配置项常量,即properties属性中的配置项名称
    /**
     * mysql的数据表引擎, innodb或myISAM
     */
    public static final String PROPERTY_MYSQL_ENGINE="engine";
    public static final String PROPERTY_MYSQL_ENGINE_DEFAULT="MyISAM";
    /**
     * shapefile的encoding
     */
    public static final String PROPERTY_SHP_ENCODING="encoding";
    public static final String PROPERTY_SHP_ENCODING_DEFAULT="utf-8";
    /**
     * 载入的DBF属性列表,属性以逗号分隔,默认为空,即不载入
     */
    public static final String PROPERTY_SHP_PROPERTY ="property";
    public static final String PROPERTY_SHP_PROPERTY_DEFAULT =null;
    /**
     * ShapeFile的坐标类型
     */
    public static final String PROPERTY_SHP_CRS="crs";

    /**
     * 是否建立索引
     */
    public static final String PROPERTY_CREATE_INDEX="createindex";
    public static final boolean PROPERTY_CREATE_INDEX_DEFAULT=true;
	// ------------------------------------------------------

	private String id;
	private String name;
	private String type = TYPE_DB_TABLE;
	private String source;
    private Map<String, Object> properties;
	private List<LayerField> fields;

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

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}
