package org.jsmart.zerocode.core.db;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

class DbSqlRunner {
	private static final Logger LOGGER = LoggerFactory.getLogger(DbSqlRunner.class);
	private Connection conn;
	
	public DbSqlRunner(Connection conn) {
		this.conn = conn;
	}
	
	/**
	 * Execute an sql with parameters (optional) and returns a list of maps 
	 * with the ResultSet content (select) or null (insert, update)
	 */
	List<Map<String, Object>> execute(String sql, Object[] params) throws SQLException {
		// As there is only one execute operation instead of separate update and query, 
		// the DbUtils execute method returns a list containing each ResultSet (each is a list of maps):
		// - Empty (insert and update)
		// - With one or more ResultSets (select).
		// - Note that some drivers never return more than one ResultSet (e.g. H2)
		QueryRunner runner = new QueryRunner();
		List<List<Map<String, Object>>> result = runner.execute(conn, sql, new MapListHandler(), params);
		if (result.isEmpty()) {
			return null;
		} else {
			if (result.size() > 1)
				LOGGER.warn("The SQL query returned more than one ResultSet, keeping only the first one");
			return result.get(0);
		}
	}

}