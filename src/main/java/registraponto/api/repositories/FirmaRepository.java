package registraponto.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import registraponto.api.entities.Firma;

public interface FirmaRepository extends JpaRepository<Firma, Long> {
	
	@Transactional(readOnly = true)
	Firma findByCnpj(String cnpj);

}
