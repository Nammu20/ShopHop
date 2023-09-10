package com.example.ShopHop.service;

import com.example.ShopHop.dto.RequestDto.CustomerRequestDto;
import com.example.ShopHop.dto.RequestDto.UpdateInfoUsingMobNo;
import com.example.ShopHop.dto.ResponseDto.CustomerResponseDto;
import com.example.ShopHop.exception.InvalidEmailException;
import com.example.ShopHop.exception.InvalidMobNoException;
import com.example.ShopHop.model.Customer;
import com.example.ShopHop.repository.CustomerRepository;
import com.example.ShopHop.transformer.CustomerTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;
    //dto -> entity
    public CustomerResponseDto addCustomer(CustomerRequestDto customerRequestDto) throws InvalidEmailException, InvalidMobNoException {

        //check if the customer with the same emailId is registered or what
        if(customerRepository.findByEmailId(customerRequestDto.getEmailId())!=null){
            throw new InvalidEmailException("Sorry ! Customer with this Email-Id is already registered.");
        }
        //check if the mobNo is same mob no exists
        if(customerRequestDto.getMobNo().length()!=10){
            throw new InvalidMobNoException("Invalid mob no!");
        }

        // Checking whether the Customer with the same mob No already Registered
        if(customerRepository.findByMobNo(customerRequestDto.getMobNo())!=null){
            throw new InvalidMobNoException("Customer with this mob no already registered!");
        }


        //creating customer obj
        Customer customer = CustomerTransformer.CustomerRequestDtoToCustomer(customerRequestDto);
        // Saving Customer in the DB
        Customer savedCustomer=customerRepository.save(customer);

        // Creating CustomerResponse using Builder through CustomerTransformer
        return CustomerTransformer.CustomerToCustomerResponseDto(savedCustomer);
    }

    public CustomerResponseDto getCustomerUsingEmail(String emailId) throws InvalidEmailException {

        // Getting Customer Using Email Id from the DB
        Customer customer= customerRepository.findByEmailId(emailId);

        // Checking Whether the emailId is Valid Or Not
        if(customer==null){
            throw new InvalidEmailException("Invalid email id!");
        }

        return CustomerTransformer.CustomerToCustomerResponseDto(customer);


    }

    public CustomerResponseDto updateInfoByMobNo(UpdateInfoUsingMobNo updateInfoUsingMobNo) throws InvalidMobNoException {
        // Checking whether the mob no is valid or not
        if(updateInfoUsingMobNo.getMobNo().length()!=10){
            throw new InvalidMobNoException("Invalid mob no!");
        }
        // Now mob no is valid

        // Getting the Customer Object from the DB
        Customer customer= customerRepository.findByMobNo(updateInfoUsingMobNo.getMobNo());

        // Checking whether the customer has been registered with the given mob no or NOT
        if(customer==null){
            throw new InvalidMobNoException("Invalid mob no!");
        }

        // Now Customer is valid. So, Updating only those attributes of customer whose value are passed by customer
        if(updateInfoUsingMobNo.getNewName()!=null){
            customer.setName(updateInfoUsingMobNo.getNewName());
        }
        if(updateInfoUsingMobNo.getNewDob()!=null){
            customer.setDob(updateInfoUsingMobNo.getNewDob());
        }
        if(updateInfoUsingMobNo.getNewEmailId()!=null){
            customer.setEmailId(updateInfoUsingMobNo.getNewEmailId());
        }
        if(updateInfoUsingMobNo.getNewAddress()!=null){
            customer.setAddress(updateInfoUsingMobNo.getNewAddress());
        }

        // saving the Customer in the DB
        Customer updatedCustomer= customerRepository.save(customer);

        return CustomerTransformer.CustomerToCustomerResponseDto(updatedCustomer);
    }

    public String deleteCustomerEitherBYEmailIdOrByMobNo(String emailId, String mobNo) throws InvalidEmailException, InvalidMobNoException {

        if(emailId.length()==0 && mobNo.length()==0){
            throw new InvalidEmailException("Provide either Email or MobNo!");
        }
        // Now Either Email or MobNo or Both value has been given.
        // Now, have to check whether the user passes the valid data(emailId or MobNo) or Not

        Customer customer;

        // When Deleting By Email
        if(emailId.length()!=0) {
            customer = customerRepository.findByEmailId(emailId);
            // Checking whether the given email Exist or not
            if (customer == null) {
                throw new InvalidEmailException("Invalid Email Id!");
            }

        }

        // When Deleting By Mob No
        else {
            // checking whether the mob no is valid or not
            if(mobNo.length()!=10){
                throw new InvalidMobNoException("Invalid mob no!");
            }

            customer= customerRepository.findByMobNo(mobNo);
            // Checking whether the customer with the given mob no exist or not
            if(customer==null){
                throw new InvalidMobNoException("Invalid MobNo!");
            }

        }
        customerRepository.delete(customer);
        return customer.getName() + " deleted successfully!";
    }

}

