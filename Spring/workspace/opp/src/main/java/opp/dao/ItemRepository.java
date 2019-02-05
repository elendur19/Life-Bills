package opp.dao;

import opp.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
    boolean existsByIdAndDateCreatedNot(Long id, String dateCreated);
}
