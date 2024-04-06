package com.rewardomain.accountcontributionservice.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rewardomain.accountcontributionservice.entities.Account;
import com.rewardomain.accountcontributionservice.entities.CreditCard;

public interface AccountRepository extends JpaRepository<Account, Long> {
    public Account findByNumber(String number);
    Optional<Account> findByCreditCard_Number(String cardNumber);
    CreditCard findCreditCardByNumber(String accountNumber);
    
}
