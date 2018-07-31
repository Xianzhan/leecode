package architecture.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import architecture.model.Customer;
import architecture.util.PropsUtil;

/**
 * 数据库操作助手类
 * 
 * @author Lee
 *
 */
public final class DatabaseHelper {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DatabaseHelper.class);

	// 定义一个用于放置数据库连接的局部线程变量（使每个线程都拥有自己的连接）
	private static final ThreadLocal<Connection> CONNECTION_HOLDER = new ThreadLocal<>();

	private static Properties props = PropsUtil
			.loadProps("architecture.properties");

	// 数据库配置
	private static final String DRIVER = PropsUtil.getString(props,
			"architecture.jdbc.driver");
	private static final String URL = PropsUtil.getString(props,
			"architecture.jdbc.url");
	private static final String USERNAME = PropsUtil.getString(props,
			"architecture.jdbc.username");
	private static final String PASSWORD = PropsUtil.getString(props,
			"architecture.jdbc.password");

	/**
	 * 获取连接
	 * 
	 * @return
	 */
	public static Connection getConnection() {
		Connection conn = CONNECTION_HOLDER.get();
		try {
			if (null == conn) {
				Class.forName(DRIVER);
				conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			}
		} catch (Exception e) {
			LOGGER.error("get connection failure", e);
			throw new RuntimeException(e);
		} finally {
			CONNECTION_HOLDER.set(conn);
		}
		return conn;
	}

	/**
	 * 开启事务
	 */
	public static void beginTransaction() {
		Connection conn = getConnection();
		if (conn != null) {
			try {
				conn.setAutoCommit(false);
			} catch (SQLException e) {
				LOGGER.error("begin transaction failure", e);
				throw new RuntimeException(e);
			} finally {
				CONNECTION_HOLDER.set(conn);
			}
		}
	}

	/**
	 * 提交事务
	 */
	public static void commitTransaction() {
		Connection conn = getConnection();
		if (null != conn) {
			try {
				conn.commit();
				conn.close();
			} catch (SQLException e) {
				LOGGER.error("commit transaction failure", e);
				throw new RuntimeException(e);
			} finally {
				CONNECTION_HOLDER.remove();
			}
		}
	}

	/**
	 * 回滚事务
	 */
	public static void rollbackTransaction() {
		Connection conn = getConnection();
		if (null != conn) {
			try {
				conn.rollback();
				conn.close();
			} catch (SQLException e) {
				LOGGER.error("rollback transaction failure", e);
				throw new RuntimeException(e);
			} finally {
				CONNECTION_HOLDER.remove();
			}
		}
	}

	public static <T> List<T> queryEntityList(Class<T> cls,
			String sql) {
		// TODO Auto-generated method stub
		return null;
	}

	public static <T> T queryEntity(Class<T> cls, String sql,
			long id) {
		// TODO Auto-generated method stub
		return null;
	}

	public static <T> boolean insertEntity(Class<T> cls,
			Map<String, Object> fieldMap) {
		// TODO Auto-generated method stub
		return false;
	}

	public static <T> boolean updateEntity(Class<T> cls, long id,
			Map<String, Object> fieldMap) {
		// TODO Auto-generated method stub
		return false;
	}

	public static <T> boolean deleteEntity(Class<T> cls, long id) {
		// TODO Auto-generated method stub
		return false;
	}

}
