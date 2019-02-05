package opp.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import opp.domain.Bill;

public interface BillRepository extends JpaRepository<Bill, Long> {
	@Override
	Optional<Bill> findById(Long id);
	
    boolean existsByIdAndAndDateCreatedNot(Long id, String dateCreated);

    @Override
    void deleteById(Long id);
}
