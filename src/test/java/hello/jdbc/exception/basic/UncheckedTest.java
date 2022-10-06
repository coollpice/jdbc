package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * RuntimeException 을 상속받은 예외는 언체크 예외.
 */
@Slf4j
public class UncheckedTest {

    @Test
    @DisplayName("언체크예외를 Try Catch 로 잡는다. (정상처리)")
    void checked_catch() {
        Service service = new Service();
        service.callCatch();
    }

    @Test
    @DisplayName("언체크 예외를 잡지않으면 자동으로 Throw 한다")
    void checked_throw() {
        Service service = new Service();
        Assertions.assertThatThrownBy(() -> service.callThrow())
                .isInstanceOf(MyUncheckedException.class);
    }

    static class MyUncheckedException extends RuntimeException {
        public MyUncheckedException(String message) {
            super(message);
        }
    }

    /**
     * Unchecked 예외는 예외를 잡거나, 던지지 않아도 된다.
     * 잡지 않을경우, 자동으로 밖으로 던진다.
     */
    static class Service {
        Repository repository = new Repository();

        public void callCatch() {
            try {
                repository.call();
            } catch (MyUncheckedException e) {
                log.info("예외처리. message = {}", e.getMessage(), e);
            }
        }

        public void callThrow() {
            repository.call();
        }
    }


    static class Repository {
        public void call() {
            throw new MyUncheckedException("런타임 ex");
        }
    }


}
