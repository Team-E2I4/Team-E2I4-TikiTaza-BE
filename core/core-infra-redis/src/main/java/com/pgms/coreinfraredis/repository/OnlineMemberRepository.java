package com.pgms.coreinfraredis.repository;

import org.springframework.data.repository.CrudRepository;

import com.pgms.coreinfraredis.entity.OnlineMember;

public interface OnlineMemberRepository extends CrudRepository<OnlineMember, Long> {
}
