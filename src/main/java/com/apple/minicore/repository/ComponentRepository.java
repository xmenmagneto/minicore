package com.apple.minicore.repository;

import com.apple.minicore.model.Component;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface ComponentRepository extends JpaRepository<Component, Integer> {

    List<Component> findByLevelLike(String level);

    List<Component> findByLevelStartingWith(String prefix);
}
