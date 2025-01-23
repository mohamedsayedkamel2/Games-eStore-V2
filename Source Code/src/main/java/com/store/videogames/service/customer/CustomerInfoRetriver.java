package com.store.videogames.service.customer;

import com.store.videogames.exceptions.exception.CustomerNotFoundException;
import com.store.videogames.entites.CustomerMoneyHistory;
import com.store.videogames.entites.Order;
import com.store.videogames.repository.CustomerMoneyHistoryRepository;
import com.store.videogames.entites.Customer;
import com.store.videogames.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
public class CustomerInfoRetriver
{
    /**
     * Warning: Tight coupling to Spring Data JPA
     */
    private final CustomerRepository customerRepository;
    private final CustomerMoneyHistoryRepository customerMoneyHistoryRepository;

    @Autowired
    public CustomerInfoRetriver(CustomerRepository customerRepository,
                                CustomerMoneyHistoryRepository customerMoneyHistoryRepository)
    {
        this.customerRepository = customerRepository;
        this.customerMoneyHistoryRepository = customerMoneyHistoryRepository;
    }

    public Customer getCustomerById(int id)
    {
        try
        {
            return customerRepository.findById(id).get();
        }
        catch(EntityNotFoundException exception)
        {
            throw new CustomerNotFoundException("The customer has not been found");
        }
    }

    @CachePut("Customer")
    public void saveCustomerIntoDB(Customer customer)
    {
        customerRepository.save(customer);
    }

    public void updateCustomer(Customer customer)
    {
        customerRepository.save(customer);
    }

    public Customer getCustomerbyEmail(String email)
    {
        return customerRepository.getCustomerByEmail(email);
    }

    public Customer getCustomerByResetPasswordToken(String token)
    {
        return customerRepository.getCustomerByResetPasswordToken(token);
    }

    @Cacheable("Customer")
    public List<Customer> getAll()
    {
        return customerRepository.findAll();
    }

}