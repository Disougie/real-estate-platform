package com.disougie.virtual_bank;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransationRepository extends JpaRepository<Transaction, Long>{

}
