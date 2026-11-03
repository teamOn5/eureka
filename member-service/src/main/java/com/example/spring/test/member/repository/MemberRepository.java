package com.example.spring.test.member.repository;

import com.example.spring.test.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);
    Optional<Member> findByUuid(String uuid);
}
