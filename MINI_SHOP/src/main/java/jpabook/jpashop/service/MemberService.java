package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원 가입
    @Transactional
    public Member create(String username, String email, String password) {
        Member member = new Member();
        member.setUsername(username);
        member.setEmail(email);
        member.setPassword(passwordEncoder.encode(password));
        memberRepository.save(member);
        return member;
    }

//    private void validateDuplicateMember(Member member) {
//        List<Member> findMembers = memberRepository.findByName(member.getUsername());
//        if (!findMembers.isEmpty()) {
//            throw new IllegalStateException("이미 존재하는 회원입니다.");
//        }
//    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
//
    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
