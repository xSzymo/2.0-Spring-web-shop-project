package com.shop.data.repositories;

import com.shop.data.tables.Picture;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PicturesRepository extends CrudRepository<Picture, Long> {
    Picture findById(Long pictureId);

    Picture findByName(String name);
}
