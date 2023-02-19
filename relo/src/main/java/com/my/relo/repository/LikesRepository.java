package com.my.relo.repository;

import org.springframework.data.repository.CrudRepository;

import com.my.relo.entity.Likes;
import com.my.relo.entity.Style;

public interface LikesRepository extends CrudRepository<Likes, Style> {

}
