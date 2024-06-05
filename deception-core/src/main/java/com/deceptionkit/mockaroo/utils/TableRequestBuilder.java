package com.deceptionkit.mockaroo.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class TableRequestBuilder {

    private final String baseJsonColumnAttribute = "{\"null_percentage\": \"0\", \"values\": \"\", \"list_selection_style\": \"random\", \"image_format\": \"png\", \"avatar_height\": 50, \"avatar_width\": 50, \"character_sequence_format\": \"\", \"countries\": \"\", \"include_protocol\": true, \"include_host\": true, \"include_path\": true, \"include_query_string\": true, \"min_date\": \"05/17/2023\", \"max_date\": \"05/17/2024\", \"date_format\": \"%-m/%-d/%Y\", \"image_width_min\": 100, \"image_height_min\": 100, \"image_width_max\": 250, \"file_type\": \"common\", \"file_name_format\": \"camel-caps\", \"formula\": \"\", \"min_items\": \"0\", \"max_items\": \"5\", \"min_words\": 10, \"max_words\": 20, \"min_sentences\": 1, \"max_sentences\": 10, \"min_paragraphs\": 1, \"max_paragraphs\": 3, \"style\": \"A-\", \"min_money\": 0, \"max_money\": 10, \"money_symbol\": \"$\", \"normal_dist_sd\": 1, \"normal_dist_mean\": 0, \"normal_dist_decimals\": 2, \"min\": 1, \"max\": 100, \"decimal_places\": 0, \"expression\": \"\", \"sequence_start\": 1, \"sequence_step\": 1, \"sequence_repeat\": 1, \"sequence_restart\": \"\", \"sql_expression\": \"\", \"only_us_places\": false, \"states\": \"\", \"min_time\": \"12:00 AM\", \"max_time\": \"11:59 PM\", \"time_format\": \"%-l:%M %p\", \"phone_format\": \"###-###-####\", \"dist_probability\": 0.5, \"dist_lambda\": 1, \"password_min_length\": 8, \"password_min_lower\": 1, \"password_min_upper\": 1, \"password_min_numbers\": 1, \"password_min_symbols\": 1, \"name\": \"name\", \"position\": 0, \"data_type_id\": 1}";
    private final String baseJsonSchema = "{\"schema\": {\"num_rows\": 1, \"file_format\": \"sql\", \"line_ending\": \"unix\", \"table_name\": \"MOCK_DATA\", \"include_create_sql\": true, \"array\": true, \"include_nulls\": true, \"delimiter\": \",\", \"quote_char\": \"\\\"\", \"include_header\": true, \"bom\": false, \"xml_root_element\": \"dataset\", \"xml_record_element\": \"record\", \"append_dataset_id\": \"\", \"columns_attributes\": []}}";
    private ObjectNode baseColumnAttribute;
    private ObjectNode schema;

    public TableRequestBuilder(String tableName) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.schema = (ObjectNode) mapper.readTree(this.baseJsonSchema);
            ((ObjectNode) this.schema.get("schema")).put("table_name", tableName);
            this.baseColumnAttribute = (ObjectNode) mapper.readTree(this.baseJsonColumnAttribute);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TableRequestBuilder addColumn(String name, Integer id) {
        ObjectNode column = this.baseColumnAttribute.deepCopy();
        column.put("name", name);
        column.put("data_type_id", id);
        ((ArrayNode) this.schema.get("schema").get("columns_attributes")).add(column);
        return this;
    }

    public ObjectNode getSchema() {
        return this.schema;
    }


}
