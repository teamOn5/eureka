package com.example.spring.test.cart.repository;

import com.example.spring.test.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("""
            SELECT c
            FROM Cart c
            JOIN FETCH c.product
            WHERE c.member.id = :memberId
            """
    )
    List<Cart> findAllByMemberId(@Param("memberId") Long memberId);
}
