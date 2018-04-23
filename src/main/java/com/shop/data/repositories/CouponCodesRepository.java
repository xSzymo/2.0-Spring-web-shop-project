package com.shop.data.repositories;

import com.shop.data.tables.CouponCode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponCodesRepository extends CrudRepository<CouponCode, Long> {
    CouponCode findById(Long id);

    CouponCode findByCode(String code);
}
