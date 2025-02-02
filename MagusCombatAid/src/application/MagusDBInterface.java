package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;


/*
 * SINGLETON DB INTERFACE TO MANAGE DB QUERIES
 */
public class MagusDBInterface implements IDaoDamageAid{

	private static MagusDBInterface dbInterface;
	private static Connection conn = null;
	private static PreparedStatement pstmt = null;
	
	private MagusDBInterface(){	
		try {
			Class.forName(JDBC_DRIVER_FORMAGUS);
			conn = DriverManager.getConnection(JDBC_URLTO_DAMAGEDB);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	/*
	 * DB INTERFACE SINGLETON DELEGATOR METHOD
	 */
	public static MagusDBInterface getDBConnection(){
		if(dbInterface == null){
			dbInterface = new MagusDBInterface();
		}
		return dbInterface;
	}
	
	/*
	 * THIS METHOD IS TO REACH VALUES OF DOUBLE COLUMNS WITH PREPARED VALUES OR WITHOUT THAT
	 * -> VALUES EX. dice/area OR weapon_type/damages_group COLUMNS
	 */
	public static Map<String, String> getDoubleColumnContent(String sql, String[] queryDatas){
		
		ResultSet datas = executeNewQuery(finishCreateSql(sql, queryDatas));
		return processOfParalelColumnResult(datas);
	}
	
	/*
	 * THIS METHOD IS TO REACH VALUES OF ROW WITH PREPARED VALUES
	 * -> VALUES FROM x_damage_group DETAILS
	 */
	public static Map<String, String> getSingleRowContnet_WithColumnTitle(String sql, String[] queryDatas){
		
		ResultSet datas = executeNewQuery(finishCreateSql(sql, queryDatas));
		return processOfRowResult_WithColumnNames(datas);
	}

	/*
	 * THIS METHOD IS TO REACH VALUE PAIRS FROM A DEFINED TABLE
	 * -> STRICT VAULES THAT USER DESIRED TWO damage_x/dice PAIR OF CELLS
	 */
	public static String[] getTheValuePairOfSpecificCells(String sql, String[] queryDatas){
		
		ResultSet datas = executeNewQuery(finishCreateSql(sql, queryDatas));
		return processOfParirOfCellResult(datas);
	}

	/*
	 * THIS METHOD IS TO LOAD IN COMMENT FROM DB
	 */
	public static String getTheValueOfASpecificCell(String sql, String[] queryDatas){
		
		ResultSet datas = executeNewQuery(finishCreateSql(sql, queryDatas));
		try {
			return datas.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/*
	 * THIS METHOD IS TO CLOSE THE EXISTING CONNECTION WITH EMBEDED DB 
	 */
	public static void closeDBConnection(){
		
		try {
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
		
	//SQL CREATION
	private static String finishCreateSql(String sql, String[] queryDatas) {
		
		for(int i = 0; i< queryDatas.length; i++){
			sql = sql.replaceFirst("@", queryDatas[i]);
		}
		//System.out.println(sql);
		return sql;
	}
	
	//DATA RECEIVING FROM DB
	private static ResultSet executeNewQuery(String finalSql){
		
		try {
			Statement stmt = conn.createStatement();
			return stmt.executeQuery(finalSql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	//DATA PROCESSINGS
	private static Map<String, String> processOfParalelColumnResult(ResultSet datas){
		
		Map<String, String> result = new LinkedHashMap<String, String>();
		try {
			while (datas.next()){
				result.put(datas.getString(1), datas.getString(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	private static Map<String, String> processOfRowResult_WithColumnNames(ResultSet datas){
		
		Map<String, String> result = new LinkedHashMap<String, String>();
		try {
			Integer columnAmount = datas.getMetaData().getColumnCount();
			for(int i = 1; i< columnAmount+1; i++){
				result.put(datas.getString(i), datas.getMetaData().getColumnName(i));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	private static String[] processOfParirOfCellResult(ResultSet datas){
		
		String[] result = new String[2];
		try {
			result[0] = datas.getString(1);
			result[1] = datas.getString(2);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		if(result[1] == null)
			result[1] = "";
		return result;
	}
	
}
