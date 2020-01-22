package Dao;

import bean.Evaluate;
import util.DBConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EvaluateDao {

    /**
     *  添加评论
     * @param
     * @return
     */
    public static int addEvaluate(Evaluate evaluate) throws Exception {
        Connection conn;
        int result = -1;
            conn = DBConnectionUtil.getConnection();
            String sql = "INSERT INTO `playappserver`.`evaluate` (`evaluate_account`, `be_evaluated_account`, `content`, `star_rank`, `evaluate_date`, `evaluate_start`, `evaluate_end`) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, evaluate.getEvaluateAccount());
            pstmt.setString(2, evaluate.getBeEvaluatedAccount());
            pstmt.setString(3, evaluate.getContent());
            pstmt.setInt(4, evaluate.getStarRank());
            pstmt.setString(5, evaluate.getEvaluateDate());
            pstmt.setString(6, evaluate.getEvaluateStart());
            pstmt.setString(7, evaluate.getEvaluateEnd());
            result = pstmt.executeUpdate();
            DBConnectionUtil.close(pstmt, conn);

        return result;
    }
}
