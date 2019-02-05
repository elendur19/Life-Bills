package opp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import opp.domain.ItemCategory;

public interface ItemCategoryRepository extends JpaRepository<ItemCategory, String> {

}
