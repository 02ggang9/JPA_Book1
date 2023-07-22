package jpabook.jpabook.domain.service;

import jpabook.jpabook.domain.Member;
import jpabook.jpabook.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    // 생성자 주입을 추천하고 변경할 일이 없기 때문에 final
    // 컴파일 시점에 체크를 할 수 있기 때문에 final 넣는것을 추천하고 Test시에도 생성자 주입을 해야한다는 것을 확인할 수 있음
    // 생성자 주입보다도 더 편한것이 @RequiredArgsConstructor 에노테이션을 추천한다.
    private final MemberRepository memberRepository;


    // 회원가입
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    // 멀티스레드 상황을 고려해 데이터베이스에 memberName을 유니크 제약조건으로 잡는 것을 권장한다.
    private void validateDuplicateMember(Member member) {
        // EXCEPTION
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다");
        }

    }

    //회원 전체 조회
    // 조회하는 경우에는 성능을 올려줌
    // 더티 체킹 등.. 찾아볼 것
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    // 단건 조회
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

}
