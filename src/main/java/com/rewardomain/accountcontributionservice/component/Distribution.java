package com.rewardomain.accountcontributionservice.component;

import com.rewardomain.accountcontributionservice.entities.Account;
import com.rewardomain.accountcontributionservice.entities.Beneficiary;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class Distribution {
    public Distribution() {}
    public void distribute(List<Beneficiary> beneficiaries, double reward){
        for (Beneficiary beneficiary: beneficiaries) {
            beneficiary.setSavings (beneficiary.getSavings() +
                    reward * (beneficiary.getPercentage() / 100.0));
        }
    }



}
