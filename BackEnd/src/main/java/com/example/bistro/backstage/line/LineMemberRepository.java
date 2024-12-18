package com.example.bistro.backstage.line;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LineMemberRepository extends JpaRepository<LineMember, Integer> {
	
	Optional<LineMember> findByLineUserId(String lineUserId);
    Optional<LineMember> findByMemberId(Integer memberId);
}
