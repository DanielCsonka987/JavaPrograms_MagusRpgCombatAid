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
	private static String starterTableOfDB = "type_damages";
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
	public static Map<String, String> getDoubleColumnContent(String sql, String[] prepDatas){
		ResultSet datas = null;
		if(prepDatas == null)
			datas = executeNewPrepQuery_WithStarterDatas(sql);
		else
			datas = executeNewPrepQuery(sql, prepDatas);
		return processOfParalelColumnResult(datas);
	}
	
	/*
	 * THIS METHOD IS TO REACH VALUES OF ROW WITH PREPARED VALUES
	 * -> VALUES FROM x_damage_group DETAILS
	 */
	public static Map<String, String> getSingleRowContnet_WithColumnTitle(String sql, String[] prepDatas){
		
		ResultSet datas = executeNewPrepQuery(sql, prepDatas);
		return processOfRowResult_WithColumnNames(datas);
	}
	
	/*
	 * THIS METHOD IS TO REACH VALUE PAIRS FROM A DEFINED TABLE
	 * -> STRICT VAULES THAT USER DESIRED TWO damage_x/dice PAIR OF CELLS
	 */
	public static String[] getTheValuePairOfSpecificCells(String sql, String[] prepDatas){
		
		ResultSet datas = executeNewPrepQuery(sql, prepDatas);
		return processOfParirOfCellResult(datas);
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
		
	//DATA RECEIVING FROM DB
	private static ResultSet executeNewPrepQuery(String sql, String[] prepDatas){
		try {
			pstmt = conn.prepareStatement(sql);
			for(int i = 0; i< prepDatas.length; i++ ){
				pstmt.setString(i, prepDatas[i]);
			}
			return pstmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static ResultSet executeNewPrepQuery_WithStarterDatas(String sql){
		try {
			/*
			Statement stmt = conn.createStatement();
			return stmt.executeQuery(sql);
			*/
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(0, starterTableOfDB);
			return pstmt.executeQuery();
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
				result.put(datas.getString(0), datas.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	private static Map<String, String> processOfRowResult_WithColumnNames(ResultSet datas){
		Map<String, String> result = new TreeMap<String, String>();
		try {
			Integer columnAmount = datas.getMetaData().getColumnCount();
			for(int i = 0; i< columnAmount; i++){
				result.put(datas.getMetaData().getColumnName(i), datas.getString(i));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	private static String[] processOfParirOfCellResult(ResultSet datas){
		String[] result = new String[2];
		try {
			result[0] = datas.getString(0);
			result[1] = datas.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return result;
	}
	
}