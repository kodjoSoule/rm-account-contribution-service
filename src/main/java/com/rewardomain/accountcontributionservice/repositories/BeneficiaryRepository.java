package com.rewardomain.accountcontributionservice.repositories;

import com.rewardomain.accountcontributionservice.entities.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {
}
