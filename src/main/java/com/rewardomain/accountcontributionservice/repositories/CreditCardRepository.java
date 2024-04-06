package com.rewardomain.accountcontributionservice.repositories;

import com.rewardomain.accountcontributionservice.entities.Account;
import com.rewardomain.accountcontributionservice.entities.CreditCard;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
	
	
}
