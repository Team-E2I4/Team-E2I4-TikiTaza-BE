package com.pgms.coreinfraredis.repository;

import org.springframework.data.repository.CrudRepository;

import com.pgms.coreinfraredis.entity.Guest;

public interface GuestRepository extends CrudRepository<Guest, Long> {
}
