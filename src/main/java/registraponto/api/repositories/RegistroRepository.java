package registraponto.api.repositories;

import java.util.List;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import registraponto.api.entities.Registro;

@Transactional(readOnly = true)
@NamedQueries({
		@NamedQuery(name = "RegistroRepository.findByColaboradorId", 
				query = "SELECT lanc FROM Registro lanc WHERE lanc.colaborador.id = :colaboradorId") })
public interface RegistroRepository extends JpaRepository<Registro, Long> {

	List<Registro> findByColaboradorId(@Param("colaboradorId") Long colaboradorId);

	Page<Registro> findByColaboradorId(@Param("colaboradorId") Long colaboradorId, Pageable pageable);
}
