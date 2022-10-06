package hello.jdbc.exception.basic;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

/**
 * Checked Exception 을 잡아서 Runtime Exception 으로 변환 -> 의존관계 제거. ( Controller, Service 에서 해결할 수 없는 예외를 의존하던 것을 개선함 )
 */
public class UnCheckedAppTest {

    @Test
    void checked() {
        Controller controller = new Controller();
        Assertions.assertThatThrownBy(() -> controller.request())
                .isInstanceOf(RuntimeException.class);
    }


    static class Controller {

        Service service = new Service();

        public void request() {
            service.logic();
        }
    }


    static class Service {
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void logic() {
            repository.call();
            networkClient.call();
        }

    }

    static class NetworkClient {
        public void call() {
            throw new RuntimeConnectionException("예외 발생");
        }
    }

    static class Repository{
        public void call(){

            try {
                runSQL();
            } catch (SQLException e) {
                throw new RuntimeSQLException(e);   // 체크 Exception 을 잡아서 Runtime Exception 으로 바꿔서 던진다. 이때 꼭 기존 예외를 넣어주어야 한다.
            }
        }

        private void runSQL() throws SQLException {
            throw new SQLException("ex");
        }
    }

    static class RuntimeConnectionException extends RuntimeException {

        public RuntimeConnectionException(String message) {
            super(message);
        }
    }

    static class RuntimeSQLException extends RuntimeException {
        public RuntimeSQLException(Throwable cause) {
            super(cause);
        }
    }


}
