package com.example.ShopHop.service;

import com.example.ShopHop.Enum.CardType;
import com.example.ShopHop.dto.RequestDto.CardRequestDto;
import com.example.ShopHop.dto.ResponseDto.CardResponseDto;
import com.example.ShopHop.exception.CardNotFoundException;
import com.example.ShopHop.exception.InvalidCardException;
import com.example.ShopHop.exception.InvalidCustomerException;
import com.example.ShopHop.exception.InvalidMobNoException;
import com.example.ShopHop.model.Card;
import com.example.ShopHop.model.Customer;
import com.example.ShopHop.repository.CardRepository;
import com.example.ShopHop.repository.CustomerRepository;
import com.example.ShopHop.transformer.CardTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CardService {

    @Autowired
    CardRepository cardRepository;

    @Autowired
    CustomerRepository customerRepository;

    public CardResponseDto addCard(CardRequestDto cardRequestDto) throws InvalidMobNoException, InvalidCustomerException, InvalidCardException {

            // 1st Step:- checking whether the mob no is valid or not
            if(cardRequestDto.getMobNo().length()!=10){
                throw new InvalidMobNoException("Invalid mob no!");
            }
            // now mobNo is valid

            // 2nd Step:-  Getting Customer Object from the DB and checking whether the customer exist or Not
            Customer customer = customerRepository.findByMobNo(cardRequestDto.getMobNo());
            if (customer == null) {
                throw new InvalidCustomerException("Sorry! The mob no doesn't registered with any customer");
            }
            // Now Customer is valid


            // 3rd Step:- Validating cardNo, cvv and expiryDate
            //           a . validating cardNo length
            if (cardRequestDto.getCardNo().length() != 16) {
                throw new InvalidCardException("Incorrect Card no!");
            }
            //           b. Checking Whether the Card no already existing in the DB or NOT
            if (cardRepository.findByCardNo(cardRequestDto.getCardNo()) != null) {
                throw new InvalidCardException("Card no already exist!");
            }
            //           c. Checking whether the CVV is Valid or Not
            if (cardRequestDto.getCvv().length() != 3) {
                throw new InvalidCardException("Invalid cvv!");
            }
            //           d. Checking Whether the date of expiry is valid or not
            LocalDate todayDate = LocalDate.now();
            LocalDate expiry = new Date(cardRequestDto.getExpiryDate().getTime()).toLocalDate();
            if (expiry.equals(todayDate) || expiry.isBefore(todayDate)) {
                throw new InvalidCardException("Your card is already EXPIRED!");
            }
            //           e. validating card type
            CardType cardType;
            try {
                cardType= CardType.valueOf(cardRequestDto.getCardType());
            }
            catch (Exception e){
                throw new InvalidCardException("Invalid card type!");
            }
            // Now We can successfully create card

            // Creating Card Object and setting its Customer attribute
            Card card = CardTransformer.CardRequestToCard(cardRequestDto);
            card.setCardType(cardType);
            card.setCustomer(customer);

            // Adding card in the Cards List of Customer
            customer.getCards().add(card);

            customerRepository.save(customer);  // It will also save Card

            // Preparing CardResponse using Builder through CardTransformer
            return CardTransformer.CardToCardResponse(card);
        }

    public List<CardResponseDto> cardDetailWithEmail(String emailId) throws InvalidCardException, CardNotFoundException {


            // Getting List of Cards from DB

            List<Card> cardList=new ArrayList<>();

            Customer customer = customerRepository.findByEmailId(emailId);

            cardList=customer.getCards();
            //checking if card found or not
            if(cardList == null){
                throw new CardNotFoundException("Card not found");
            }

            List<CardResponseDto> cardResponseList= new ArrayList<>();

            for (Card card:cardList){
                cardResponseList.add(CardTransformer.CardToCardResponse(card));
            }

            return cardResponseList;
        }

    public String removeCard(String emailId, String cardNo) throws InvalidCustomerException, InvalidCardException {

        // Checking whether Customer with the given email id exist or not
        Customer customer = customerRepository.findByEmailId(emailId);
        if (customer == null) {
            throw new InvalidCustomerException("Invalid Customer Id!");
        }

        // fetching card after validating it
        //            a . Validating cardNo length
        if(cardNo.length()!=16){
            throw new InvalidCardException("Incorrect card no!");
        }
        //            b. validating cardNo
        Card card= cardRepository.findByCardNo(cardNo);
        if(card==null){
            throw new InvalidCardException("Invalid card no!");
        }
        //            c. validating card and customer
        if(card.getCustomer()!=customer){
            throw new InvalidCardException("Card doesn't belong to you!");
        }

        // Now Card and Customer both are valid
        cardRepository.delete(card);

        return "Card " +card.getCardNo()+ " of " +customer.getName()+ " deleted successfully!";
    }

}


