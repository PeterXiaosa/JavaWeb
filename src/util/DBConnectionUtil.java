package util;

import java.sql.*;

public class DBConnectionUtil {
    private static final String DBDRIVER = "com.mysql.jdbc.Driver";
    private static final String DBURL = "jdbc:mysql://47.100.210.98:3306/mydbsystem";
    private static final String DBUSER = "root";
    private static final String DBPASSWORD = "root";

    public static Connection getConnection() throws Exception{
        Connection conn = null;
        Class.forName(DBDRIVER);
        conn = DriverManager.getConnection(DBURL, DBUSER, DBPASSWORD);
        return conn;
    }

    public static void closeConnection(Connection connection){
        if (connection != null){
            try{
                connection.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public static void closeResuleSet(ResultSet resultSet){
        if (resultSet != null){
            try {
                resultSet.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public static void close(Statement statement, Connection connection) throws Exception{
        if (statement != null){
            statement.close();
            if (connection != null){
                connection.close();
            }
        }
    }

    /**
     * 关闭连接
     * @param cstmt
     * @param conn
     * @throws Exception
     */
    public static void close(CallableStatement cstmt, Connection conn) throws Exception{
        if(cstmt!=null){
            cstmt.close();
            if(conn!=null){
                conn.close();
            }
        }
    }


    /**
     * 关闭连接
     * @param pstmt
     * @param conn
     * @throws SQLException
     */
    public static void close(PreparedStatement pstmt, Connection conn) throws SQLException{
        if(pstmt!=null){
            pstmt.close();
            if(conn!=null){
                conn.close();
            }
        }
    }


    /**
     * 重载关闭方法
     * @param pstmt
     * @param conn
     * @throws Exception
     */
    public void close(ResultSet rs,PreparedStatement pstmt, Connection conn) throws Exception{
        if(rs!=null){
            rs.close();
            if(pstmt!=null){
                pstmt.close();
                if(conn!=null){
                    conn.close();
                }

            }
        }
    }
}
