package com.finartz.intern.campaignlogic.repository;

import com.finartz.intern.campaignlogic.model.entity.CartEntity;

import java.util.Optional;

public interface CartRepository {
  Optional<CartEntity> findCart(String cartId);

  CartEntity createCart(int accountId);

  CartEntity updateCart(CartEntity cartEntity);
}
