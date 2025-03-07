package com.redis.redi2read.services;

import java.util.*;
import java.util.stream.LongStream;

import com.redis.redi2read.models.Book;
import com.redis.redi2read.models.Cart;
import com.redis.redi2read.models.CartItem;
import com.redis.redi2read.models.User;
import com.redis.redi2read.repositories.BookRepository;
import com.redis.redi2read.repositories.CartRepository;
import com.redis.redi2read.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    private final Jedis jedis = new Jedis("localhost",6379); // Assuming the default Redis connection

    // Path for cart items in Redis
    private static final String CART_ITEMS_KEY = "cartItems";

    public Cart get(String id) {
        return cartRepository.findById(id).get();
    }

    public void addToCart(String id, CartItem item) {
        Optional<Book> book = bookRepository.findById(item.getIsbn());
        if (book.isPresent()) {
            String cartKey = CartRepository.getKey(id);
            item.setPrice(book.get().getPrice());

            // Use RPUSH to append the cart item to the list in Redis
            jedis.rpush(cartKey + ":" + CART_ITEMS_KEY, item.toString()); // Assuming CartItem is serializable to string
        }
    }

    public void removeFromCart(String id, String isbn) {
        Optional<Cart> cartFinder = cartRepository.findById(id);
        if (cartFinder.isPresent()) {
            Cart cart = cartFinder.get();
            String cartKey = CartRepository.getKey(cart.getId());

            // Get the list of cart items
            List<String> cartItems = jedis.lrange(cartKey + ":" + CART_ITEMS_KEY, 0, -1);

            // Find the item to remove based on ISBN
            OptionalLong cartItemIndex = LongStream.range(0, cartItems.size())
                    .filter(i -> cartItems.get((int) i).contains(isbn))
                    .findFirst();

            if (cartItemIndex.isPresent()) {
                String cartItemToRemove = cartItems.get((int) cartItemIndex.getAsLong());

                // Use LREM to remove the item from the list in Redis
                jedis.lrem(cartKey + ":" + CART_ITEMS_KEY, 0, cartItemToRemove);
            }
        }
    }

    public void checkout(String id) {
        Optional<Cart> optionalCart = cartRepository.findById(id);
        if (!optionalCart.isPresent()) {
            throw new NoSuchElementException("Cart not found with id: " + id);
        }
        Cart cart = optionalCart.get();

        Optional<User> optionalUser = userRepository.findById(cart.getUserId());
        if (!optionalUser.isPresent()) {
            throw new NoSuchElementException("User not found with ID: " + cart.getUserId());
        }
        User user = optionalUser.get();

        cart.getCartItems().forEach(cartItem -> {
            Optional<Book> optionalBook = bookRepository.findById(cartItem.getIsbn());
            if (!optionalBook.isPresent()) {
                throw new NoSuchElementException("Book not found with ISBN: " + cartItem.getIsbn());
            }
            Book book = optionalBook.get();
            user.addBook(book);
        });

        userRepository.save(user);
        // cartRepository.delete(cart);
    }

}
