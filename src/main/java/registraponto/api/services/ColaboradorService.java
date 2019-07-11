package registraponto.api.services;

import java.util.Optional;

import registraponto.api.entities.Colaborador;

public interface ColaboradorService {
	
	/**
	 * Persiste um funcionário na base de dados.
	 * 
	 * @param colaborador
	 * @return Colaborador
	 */
	Colaborador persistir(Colaborador colaborador);
	
	/**
	 * Busca e retorna um funcionário dado um CPF.
	 * 
	 * @param cpf
	 * @return Optional<Colaborador>
	 */
	Optional<Colaborador> getByCpf(String cpf);
	
	/**
	 * Busca e retorna um funcionário dado um email.
	 * 
	 * @param email
	 * @return Optional<Colaborador>
	 */
	Optional<Colaborador> getByEmail(String email);
	
	/**
	 * Busca e retorna um funcionário por ID.
	 * 
	 * @param id
	 * @return Optional<Colaborador>
	 */
	Optional<Colaborador> getById(Long id);

}
