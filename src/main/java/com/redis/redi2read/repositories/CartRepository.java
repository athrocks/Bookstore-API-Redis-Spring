package com.redis.redi2read.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.redis.redi2read.models.Cart;
import org.springframework.data.repository.CrudRepository;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.json.Path;
import org.springframework.stereotype.Repository;

@Repository
public class CartRepository implements CrudRepository<Cart, String> {

    private static final String ID_PREFIX = Cart.class.getName();
    private static final String USER_ID_INDEX = "carts-by-user-id-idx";

    private final JedisPooled jedis;

    public CartRepository() {
        this.jedis = new JedisPooled("localhost", 6379); // Configure host and port as needed
    }

    public <S extends Cart> S save(S cart) {
        // Set cart ID if not already present
        if (cart.getId() == null) {
            cart.setId(UUID.randomUUID().toString());
        }

        String key = getKey(cart);

        // Save cart as JSON in Redis
        jedis.jsonSet(key, cart);

        // Add to set of cart keys
        jedis.sadd(ID_PREFIX, key);

        // Maintain index for user ID to cart ID
        jedis.hset(USER_ID_INDEX, cart.getUserId().toString(), cart.getId());

        return cart;
    }

    public <S extends Cart> Iterable<S> saveAll(Iterable<S> carts) {
        return StreamSupport.stream(carts.spliterator(), false)
                .map(this::save)
                .collect(Collectors.toList());
    }

    public Optional<Cart> findById(String id) {
        String key = getKey(id);
        Cart cart = jedis.jsonGet(key, Cart.class);
        return Optional.ofNullable(cart);
    }

    public boolean existsById(String id) {
        return jedis.exists(getKey(id));
    }

    public Iterable<Cart> findAll() {
        List<String> keys = jedis.smembers(ID_PREFIX).stream().toList();
        return keys.stream()
                .map(key -> jedis.jsonGet(key, Cart.class))
                .collect(Collectors.toList());
    }

    public Iterable<Cart> findAllById(Iterable<String> ids) {
        List<String> keys = StreamSupport.stream(ids.spliterator(), false)
                .map(CartRepository::getKey)
                .toList();
        return keys.stream()
                .map(key -> jedis.jsonGet(key, Cart.class))
                .collect(Collectors.toList());
    }

    public long count() {
        return jedis.scard(ID_PREFIX);
    }

    public void deleteById(String id) {
        String key = getKey(id);
        jedis.jsonDel(key);
        jedis.srem(ID_PREFIX, key);
        jedis.hdel(USER_ID_INDEX, id);
    }

    public void delete(Cart cart) {
        deleteById(cart.getId());
    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    public void deleteAll(Iterable<? extends Cart> carts) {
        List<String> keys = StreamSupport.stream(carts.spliterator(), false)
                .map(cart -> getKey(cart.getId()))
                .toList();
        keys.forEach(this::deleteById);
    }

    public void deleteAll() {
        List<String> keys = jedis.smembers(ID_PREFIX).stream().toList();
        keys.forEach(this::deleteById);
    }

    public Optional<Cart> findByUserId(Long id) {
        String cartId = jedis.hget(USER_ID_INDEX, id.toString());
        return (cartId != null) ? findById(cartId) : Optional.empty();
    }

    private static String getKey(Cart cart) {
        return String.format("%s:%s", ID_PREFIX, cart.getId());
    }

    public static String getKey(String id) {
        return String.format("%s:%s", ID_PREFIX, id);
    }
}
