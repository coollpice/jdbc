package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@RequiredArgsConstructor
public class MemberServiceV2 {

    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {

        Connection con = dataSource.getConnection();
        try {

            con.setAutoCommit(false); // 트랜잭션 시작

            bizLogic(con, fromId, toId, money);
            con.commit();

        } catch (Exception e) {
            con.rollback();
            throw new IllegalStateException(e);
        } finally {
            connectionRelease(con);
        }
    }

    private void bizLogic(Connection con, String fromId, String toId, int money) throws SQLException {

        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        memberRepository.update(con, fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(con, toId, toMember.getMoney() + money);

    }


    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체중 예외 발생");
        }
    }

    private void connectionRelease(Connection con) {

        if (con != null) {
            try {
                con.setAutoCommit(true); // 커넥션풀에 돌아갈떄 기본값으로 다시 세팅
                con.close();
            } catch (Exception e) {

            }
        }
    }
}
