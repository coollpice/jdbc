package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class MemberRepositoryV0Test {

    private MemberRepositoryV0 memberRepositoryV0 = new MemberRepositoryV0();

    @Test
    void crud() throws SQLException {

        //save
        Member member = new Member("memberB", 1000);
        memberRepositoryV0.save(member);

        //find
        Member findMember = memberRepositoryV0.findById(member.getMemberId());
        Assertions.assertThat(findMember).isEqualTo(member);

        //update
        memberRepositoryV0.update(member.getMemberId(), 10000);
        Member updateMember = memberRepositoryV0.findById(member.getMemberId());
        Assertions.assertThat(updateMember.getMoney()).isEqualTo(10000);

        //delete
        memberRepositoryV0.delete(member.getMemberId());
        Assertions.assertThatThrownBy(() -> memberRepositoryV0.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);
    }
}