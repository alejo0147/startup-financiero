package com.algorian.springboot.transaction.controller;

import com.algorian.springboot.transaction.entities.Transaction;
import com.algorian.springboot.transaction.repository.ITransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor

@RestController
@RequestMapping("/transaction")
public class TransactionRestController {

    private final ITransactionRepository _transactionRepository;

    @GetMapping()
    public ResponseEntity<List<Transaction>> findAll(){ return ResponseEntity.ok(_transactionRepository.findAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable ("id") Long id){
        Optional<Transaction> find = _transactionRepository.findById(id);
        if (find.isPresent()) {
            return new ResponseEntity<>(find.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/id")
    public ResponseEntity<?> modify(@PathVariable (name = "id") Long id, @RequestBody Transaction transaction){
        Optional<Transaction> find = _transactionRepository.findById(id);
        if (find.isPresent()){
            Transaction transaction1 = find.get();

            transaction1.setAmount(transaction.getAmount());
            transaction1.setChannel(transaction.getChannel());
            transaction1.setDate(transaction.getDate());
            transaction1.setDescription(transaction.getDescription());
            transaction1.setFee(transaction.getFee());
            transaction1.setIbanAccount(transaction.getIbanAccount());
            transaction1.setReference(transaction.getReference());
            transaction1.setStatus(transaction.getStatus());

            Transaction save = _transactionRepository.save(transaction1);
            return ResponseEntity.status(HttpStatus.CREATED).body(save);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping()
    public ResponseEntity<?> save(@RequestBody Transaction transaction){
        Transaction save = _transactionRepository.save(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(save);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable ("id") Long id) {
        Optional<Transaction> find = _transactionRepository.findById(id);
        if (find.isPresent()) {
            _transactionRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/customer/transactions")
    public ResponseEntity<List<Transaction>>  findAllByCustumer(@RequestParam (name = "ibanAccount") String ibanAccount){
        List<Transaction> transactionList = _transactionRepository.findByIbanAccount(ibanAccount);
        if (transactionList.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        return ResponseEntity.ok(transactionList);
    }

}
