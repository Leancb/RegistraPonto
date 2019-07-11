package registraponto.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import registraponto.api.entities.Colaborador;

@Transactional(readOnly = true)
public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {

	Colaborador findByCpf(String cpf);
	
	Colaborador findByEmail(String email);
	
	Colaborador findByCpfOrEmail(String cpf, String email);
}
