package com.rewardomain.accountcontributionservice.controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rewardomain.accountcontributionservice.component.Distribution;
import com.rewardomain.accountcontributionservice.entities.Account;
import com.rewardomain.accountcontributionservice.entities.Beneficiary;
import com.rewardomain.accountcontributionservice.entities.CreditCard;
import com.rewardomain.accountcontributionservice.repositories.AccountRepository;
import com.rewardomain.accountcontributionservice.repositories.BeneficiaryRepository;
import com.rewardomain.accountcontributionservice.repositories.CreditCardRepository;
import com.rewardomain.accountcontributionservice.request.AccountContributionRequest;
import com.rewardomain.accountcontributionservice.response.AccountContributionResponse;

import io.github.resilience4j.retry.annotation.Retry;

//import io.github.resilience4j.retry.annotation.Retry;

@CrossOrigin
@RestController
public class AccountContributionController {

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private Environment environment;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    @Autowired
    private Distribution distribution;

    public ResponseEntity<ErrorReponse> defaultResponse (RuntimeException e) {
        HttpStatusCode httpStatusCode =HttpStatusCode.valueOf(404);
        return new ResponseEntity<ErrorReponse>(new ErrorReponse(404L, e.getMessage()), httpStatusCode);
    }


     // Créer un compte

    @Retry(name="account-contribution", fallbackMethod ="defaultResponse")
    
    @PostMapping("/account-contribution/accounts")
    public ResponseEntity<AccountContributionResponse> createAccount (@RequestBody AccountContributionRequest request) {
        CreditCard creditCard = new CreditCard(request.getCcnumber());
        Account account = new Account (request.getName(), request.getAnumber());
        account.setCreditCard(creditCard);
        creditCardRepository.save(creditCard);
        accountRepository.save(account);
        return new ResponseEntity<>
        (new AccountContributionResponse(201, "Account created."), HttpStatusCode.valueOf(201));
    }
    // Rechercher un compte à partir du numéro du compte
    @Retry(name="account-contribution", fallbackMethod ="defaultResponse")
    @GetMapping("/account-contribution/accounts/{account_number}")
    public ResponseEntity<Account> getAccount (@PathVariable("account_number") String number) { 
    	Account account = accountRepository.findByNumber(number);
    	System.out.println(account.getCreditCard());
        return new ResponseEntity<Account>(account, HttpStatusCode.valueOf(200));
    }

    // Lister tous les comptes
    @Retry(name="account-contribution", fallbackMethod ="defaultResponse")
//    @CrossOrigin(origins="http://localhost:4200")
    @GetMapping("/account-contribution/accounts")
    public ResponseEntity<List<Account>> getAccounts() {
        List<Account> accounts = accountRepository.findAll();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "http://localhost:4200");
        headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        headers.add("Access-Control-Allow-Headers", "Content-Type, Authorization");
        headers.add("Access-Control-Allow-Credentials", "true");
        
        
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    // Création d'un bénéficiaire pour un compte spécifique
    @Retry(name="account-contribution", fallbackMethod ="defaultResponse")
    @PostMapping("/account-contribution/accounts/{account_number}/beneficiaries")
    public ResponseEntity<AccountContributionResponse> createBeneficiary (
            @RequestBody AccountContributionRequest request, @PathVariable("account_number") String anumber) {
        Beneficiary beneficiary = new Beneficiary(request.getPercentage(), request.getName());
        Account account= accountRepository.findByNumber(anumber);
        if (account != null) {
            beneficiary.setAccount(account);
            beneficiaryRepository.save(beneficiary);
            return new ResponseEntity<AccountContributionResponse> (new AccountContributionResponse (201, "Beneficiary created and added."), HttpStatus.OK);
        }
        return new ResponseEntity<AccountContributionResponse> (new AccountContributionResponse (404, "Account not found."), HttpStatusCode.valueOf(404));
    }
    
 // Modification de taux d'allocation d'un bénéficiaire
    @PutMapping("/account-contribution/beneficiaries/{id}")
    public ResponseEntity<AccountContributionResponse> updateBeneficiary
    (@RequestBody AccountContributionRequest request, @PathVariable long id) { Optional<Beneficiary> beneficiary = beneficiaryRepository.findById(id);
        if (beneficiary.isPresent()) {
            beneficiary.get().setPercentage(request.getPercentage());
            beneficiaryRepository.save(beneficiary.get()); return new ResponseEntity<AccountContributionResponse>
                    (new AccountContributionResponse(200, "Beneficiary updated."), HttpStatusCode.valueOf(200));
        } else return new ResponseEntity<AccountContributionResponse>
        (new AccountContributionResponse(404, "Beneficiary not found."), HttpStatusCode.valueOf(404));
    }

//    // Modification de taux d'allocation d'un bénéficiaire
//    @Retry(name="account-contribution", fallbackMethod ="defaultResponse")
//    @PutMapping("/account-contribution/beneficiaries/{id}")
//    public ResponseEntity<AccountContributionResponse> updateBeneficiary
//    (@RequestBody AccountContributionRequest request, @PathVariable long id) { Optional<Beneficiary> beneficiary = beneficiaryRepository.findById(id);
//        if (beneficiary.isPresent()) {
//            beneficiary.get().setPercentage(request.getPercentage());
//            beneficiaryRepository.save(beneficiary.get()); return new ResponseEntity<AccountContributionResponse>
//                    (new AccountContributionResponse(200, "Beneficiary updated."), HttpStatusCode.valueOf(200));
//        } else return new ResponseEntity<AccountContributionResponse>
//        (new AccountContributionResponse(404, "Beneficiary not found."), HttpStatusCode.valueOf(404));
//    }
    
    
//    @Retry(name="benefit-restaurant")
//	@PutMapping("/benefit-restaurant/merchants/{merchant_number}/{availability}")
//	public ResponseEntity<Object> updateAvailability(@PathVariable long merchant_number,
//			@PathVariable String availability) {
//		logger.info("Benefit Restaurant Service call received for updating availability.");
//		
//		String port = environment.getProperty("local.server.port");
//		
//		Restaurant restaurant = repository.findByNumber(merchant_number);
//		if (restaurant == null) {
//			throw new RuntimeException("Restaurant non trouvé ! "+merchant_number);
//		}
//		restaurant.setAvailability(availability);
//		restaurant.setExecutionChain("restaurant-service instance : "+port);
//		try {
//			repository.save(restaurant);
//		} catch (Exception e) {
//			// Créer un objet Map pour représenter le corps de la réponse JSON
//			Map<String, Object> responseBody = new HashMap<>();
//			responseBody.put("execution_chain",restaurant.getExecutionChain());
//			responseBody.put("status code", 500);
//			responseBody.put("message", "Erreur lors de la mise à jour de la disponibilité du bénéfice.");
//			return ResponseEntity.status(500).body(responseBody);
//		}
//			// Créer un objet Map pour représenter le corps de la réponse JSON
//		Map<String, Object> responseBody = new HashMap<>();
//		responseBody.put("execution_chain",restaurant.getExecutionChain());
//		responseBody.put("status code", 200);
//		responseBody.put("message", "Benefit availability updated.");
//
//		return ResponseEntity.status(200).body(responseBody);
//	}

    
    
 // Rechercher un compte à partir du numéro du compte
    @Retry(name="account-contribution", fallbackMethod ="defaultResponse")
    @GetMapping("/account-contribution/accounts/{account_number}/credit-card/")
//    this.http.get<string>(`${this.apiUrl}/accounts/${accountNumber}/credit-card`);
    public ResponseEntity<Object> getCrediCard (
    		@PathVariable("account_number") String number)
    { 
    	
    	Account account = accountRepository.findByNumber(number);
    	// Créer un objet Map pour représenter le corps de la réponse JSON
		
    	Map<String, Object> responseBody = new HashMap<>();
    	CreditCard credit = account.getCreditCard();
    	String creditNumber = credit.getNumber();
    	String accountNumber = account.getNumber();
		responseBody.put("credit_card_number",creditNumber);
		responseBody.put("account_number", accountNumber);
    	
    	//CreditCard creditCard = account.getCreditCard();
        return new ResponseEntity<Object>(responseBody, HttpStatusCode.valueOf(200));
    }
    
    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptionsRequest() {
        return ResponseEntity.ok().build();
    }
    
    // Liste des bénéficiaires d'un compte spécifique
    @Retry(name="account-contribution", fallbackMethod ="defaultResponse")
    @GetMapping("/account-contribution/accounts/{account_number}/beneficiaries") public ResponseEntity<List<Beneficiary>>
    getAccounts (@PathVariable("account_number") String number) {
        Account account = accountRepository.findByNumber(number);
        if (account != null) {
            return new ResponseEntity<List<Beneficiary>> (account.getBeneficiaries(), HttpStatusCode.valueOf(200));
        }
        return new ResponseEntity<List<Beneficiary>> (new ArrayList<>(), HttpStatusCode.valueOf(404));
    }

    //Lecture d'un bénéficiaire dans d'un compte spécifique
    @Retry(name="account-contribution", fallbackMethod ="defaultResponse")
    @GetMapping("/account-contribution/{account_number}/beneficiaries/{id}")
    public ResponseEntity<Beneficiary> getBeneficiary (@PathVariable long id) {
        Optional<Beneficiary> optional = beneficiaryRepository.findById(id);
        if (optional.isPresent()) {
            return new ResponseEntity<Beneficiary> (optional.get(), HttpStatusCode.valueOf(200));
        }
        return new ResponseEntity<Beneficiary> (new Beneficiary(), HttpStatusCode.valueOf(404));
    }

    // Suppression d'un bénéficiaire
    @Retry(name="account-contribution", fallbackMethod ="defaultResponse")
    @DeleteMapping("/account-contribution/beneficiaries/{id}")
    public ResponseEntity<AccountContributionResponse> deleteBeneficiary (@PathVariable long id) {
        Optional<Beneficiary> optional = beneficiaryRepository.findById(id);
        if (optional.isPresent()) {
            beneficiaryRepository.delete(optional.get());
            return new ResponseEntity<AccountContributionResponse>
                    (new AccountContributionResponse(200, "Beneficiary deleted."), HttpStatusCode.valueOf(200));
        }
        return new ResponseEntity<AccountContributionResponse>
                (new AccountContributionResponse(404, "Beneficiary not found."), HttpStatusCode.valueOf(404));
    }

    // Suppression d'un compte
    @Retry(name="account-contribution", fallbackMethod ="defaultResponse")
    @DeleteMapping("/account-contribution/accounts/{account_number}")
    public ResponseEntity<AccountContributionResponse> deleteAccount (@PathVariable("account_number") String number) {
        Account account = accountRepository.findByNumber(number);
        if (account != null) {
            accountRepository.delete(account);
            return new ResponseEntity<AccountContributionResponse>
                    (new AccountContributionResponse(200, "Account deleted."), HttpStatusCode.valueOf(200));
        }
        return new ResponseEntity<AccountContributionResponse>
                (new AccountContributionResponse(404, "Account not found."), HttpStatusCode.valueOf(404));
    }


    // Distribution de la récompense à tous les bénéficiaires d'un compte spécifique @PutMapping("/account-contribution/{credit_card_number}/reward/{reward)")
    @PutMapping("/account-contribution/{credit_card_number}/reward/{reward}")
    public ResponseEntity<AccountContributionResponse> distributeReward (
            @PathVariable("credit_card_number") String number, @PathVariable double reward) {
        Optional<Account> optional = accountRepository.findByCreditCard_Number(number);
        if (optional.isEmpty()) {
            return new ResponseEntity<>(new AccountContributionResponse(404, "Account not found."), HttpStatus.NOT_FOUND);
        } else {
            Account account = optional.get();
            String port = environment.getProperty("local.server.port");
            String executionChain = "account-contribution-service instance:" + port;

            if (account.isValid()) {
                account.setBenefits(account.getBenefits() + reward);
                List<Beneficiary> beneficiaries = account.getBeneficiaries();
                distribution.distribute(beneficiaries, reward);
                accountRepository.save(account);
                return new ResponseEntity<>(new AccountContributionResponse(200, "Reward distributed."), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new AccountContributionResponse(403, "Account is invalid."), HttpStatus.FORBIDDEN);
            }
        } }
    

    //Mise à jour du pourcentage d’allocation d'un bénéficiaire dans un compte spécifique
    @Retry(name="account-contribution", fallbackMethod ="defaultResponse")
    @PutMapping("/account-contribution/{account_number}/beneficiaries/{id}/{percentage}")
    public ResponseEntity<AccountContributionResponse> updateBeneficiaryPercentage
    (@PathVariable("account_number") String anumber, @PathVariable long id, @PathVariable double percentage) {
        Account account = accountRepository.findByNumber(anumber);
        if (account != null) {
            Optional<Beneficiary> optional = beneficiaryRepository.findById(id);
            if (optional.isPresent()) {
                Beneficiary beneficiary = optional.get();
                beneficiary.setPercentage(percentage);
                beneficiaryRepository.save(beneficiary);
                return new ResponseEntity<AccountContributionResponse>
                        (new AccountContributionResponse(200, "Beneficiary updated."), HttpStatusCode.valueOf(200));
            }
            return new ResponseEntity<AccountContributionResponse>
                    (new AccountContributionResponse(404, "Beneficiary not found."), HttpStatusCode.valueOf(404));
        }
        return new ResponseEntity<AccountContributionResponse>
                (new AccountContributionResponse(404, "Account not found."), HttpStatusCode.valueOf(404));
    }
    
    @Retry(name="account-contribution", fallbackMethod ="defaultResponse")
    
    @DeleteMapping("/account-contribution/accounts/{account_number}/beneficiaries/{id}")
    public ResponseEntity<AccountContributionResponse> deleteBeneficiaryFromAccount(
            @PathVariable("account_number") String anumber,
            @PathVariable long id) {
        Account account = accountRepository.findByNumber(anumber);
        if (account != null) {
            Optional<Beneficiary> optional = beneficiaryRepository.findById(id);
            if (optional.isPresent()) {
                Beneficiary beneficiary = optional.get();
                beneficiaryRepository.delete(beneficiary);
                return new ResponseEntity<>(new AccountContributionResponse(200, "Beneficiary deleted."), HttpStatus.OK);
            }
            return new ResponseEntity<>(new AccountContributionResponse(404, "Beneficiary not found."), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new AccountContributionResponse(404, "Account not found."), HttpStatus.NOT_FOUND);
    }

   
}


