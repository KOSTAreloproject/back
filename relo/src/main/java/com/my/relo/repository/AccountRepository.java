package com.my.relo.repository;

import org.springframework.data.repository.CrudRepository;

import com.my.relo.entity.Account;

public interface AccountRepository extends CrudRepository<Account, Long> {

}
