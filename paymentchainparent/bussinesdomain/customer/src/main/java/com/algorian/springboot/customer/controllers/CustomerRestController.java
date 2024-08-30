package com.algorian.springboot.customer.controllers;

import com.algorian.springboot.customer.entities.Customer;
import com.algorian.springboot.customer.entities.CustomerProduct;
import com.algorian.springboot.customer.repository.ICustomerRepository;
import com.fasterxml.jackson.databind.JsonNode;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.time.Duration;
import java.util.Collections;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

/**
 *
 * @author Alejo0147
 */
@RequiredArgsConstructor

@RestController
@RequestMapping("/customer")
public class CustomerRestController {

    private final ICustomerRepository _customerRepository;

    private final WebClient.Builder _webClientbuilder;

    @GetMapping()
    public ResponseEntity<List<Customer>> findAll() {
        return ResponseEntity.ok(_customerRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") Long id) {
        Optional<Customer> find = _customerRepository.findById(id);
        if (find.isPresent()) {
            return new ResponseEntity<>(find.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable("id") Long id, @RequestBody Customer customer) {
        Optional<Customer> find = _customerRepository.findById(id);
        if (find.isPresent()) {
            Customer newCustomer = find.get();

            newCustomer.setCode(customer.getCode());
            newCustomer.setIban(customer.getIban());
            newCustomer.setName(customer.getName());
            newCustomer.setSurname(customer.getSurname());
            newCustomer.setPhone(customer.getPhone());
            newCustomer.setAddress(customer.getAddress());

            Customer save = _customerRepository.save(newCustomer);
            return ResponseEntity.status(HttpStatus.OK).body(save);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> post(@RequestBody Customer customer) {
        customer.getProducts().forEach(t -> t.setCustomer(customer));
        Customer save = _customerRepository.save(customer);
        return ResponseEntity.ok(save);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        Optional<Customer> find = _customerRepository.findById(id);
        if (find.isPresent()) {
            _customerRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/full")
    public ResponseEntity<Customer> getByCode(@RequestParam (name = "code") String code) {
        Optional<Customer> cusOptional = _customerRepository.findByCodeIgnoreCase(code);
        if (cusOptional.isPresent()) {
            Customer customer = cusOptional.get();
            List<CustomerProduct> products = customer.getProducts();
            if (!products.isEmpty()) {
                products.forEach(p -> {
                    String productName = getProductName(p.getProductId());
                    p.setProductName(productName);
                });

                List<?> transactions = findAllTransactions(customer.getIban());
                customer.setTransactions(transactions);
                return new ResponseEntity<>(customer, HttpStatus.OK);
            }
            return new ResponseEntity<>(customer, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Métodos externa service

    /**
     * Llama al microservicio product, por medio del id del product y retorna un nombre
     * @param id es el id del producto
     * @return name que es el nombre del producto
     */
    private String getProductName(Long id) {
        // Usar el WebClient inyectado y configurado a través de WebClientConfig
        WebClient build = _webClientbuilder
                .baseUrl("http://localhost:8083/product") // Establecer la URL base
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        // Hacer la solicitud GET y extraer el campo "name"
        JsonNode block = build.method(HttpMethod.GET)
                .uri("/" + id)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        // Extraer el nombre del producto del JsonNode recibido
        String name = block.get("name").asText();
        return name;
    }

    /**
     * Llamas el microservicio de transaction para retornar una lista de transacciones por medio del N° de cuenta
     * @param iban es el N° de cuenta  de la transacción
     * @return transactions que es la lista de transacciones pertenecientes al N° de cuenta
     */
    private List<?> findAllTransactions(String iban){
        WebClient build = _webClientbuilder
                .baseUrl("http://localhost:8082/transaction")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        List<?> transactions = build.method(HttpMethod.GET)
                .uri(uriBuilder -> uriBuilder
                        .path("/customer/transactions")
                        .queryParam("ibanAccount", iban)
                        .build())
                .retrieve()
                .bodyToFlux(Object.class).collectList().block();
        return transactions;
    }

}
