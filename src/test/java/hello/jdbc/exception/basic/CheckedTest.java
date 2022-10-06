package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
public class CheckedTest {

    @Test
    @DisplayName("체크예외를 Try Catch 로 잡는다. (정상처리)")
    void checked_catch() {
        MyService myService = new MyService();
        myService.callCatch();
    }

    @Test
    @DisplayName("체크예외를 Throws 로 던진다. (예외던짐)")
    void checked_throw() {
        MyService myService = new MyService();
        Assertions.assertThatThrownBy(() -> myService.callThrow())
                .isInstanceOf(MyCheckedException.class);
    }

    static class MyCheckedException extends Exception {
        public MyCheckedException(String message) {
            super(message);
        }
    }

    static class MyService{
        MyRepository myRepository = new MyRepository();

        /**
         * 예외를 잡아서 처리
         */
        public void callCatch() {
            try {
                myRepository.call();
            } catch (MyCheckedException e) {
                log.info("예외처리, message = {}",e.getMessage(), e);
            }
        }

        /**
         * 체크 예외를 던짐
         * throws 를 필수로 선언해야한다.
         */
        public void callThrow() throws MyCheckedException {
            myRepository.call();
        }
        
        
    }

    static class MyRepository {
        public void call() throws MyCheckedException {
            throw new MyCheckedException("ex");
        }
    }
}
