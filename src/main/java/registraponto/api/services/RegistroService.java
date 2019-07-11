package registraponto.api.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import registraponto.api.entities.Registro;

public interface RegistroService {

	/**
	 * Retorna uma lista paginada de lançamentos de um determinado funcionário.
	 * 
	 * @param colaboradorId
	 * @param pageRequest
	 * @return Page<Registro>
	 */
	Page<Registro> getByColaboradorId(Long colaboradorId, PageRequest pageRequest);
	
	/**
	 * Retorna um lançamento por ID.
	 * 
	 * @param id
	 * @return Optional<Registro>
	 */
	Optional<Registro> getById(Long id);
	
	/**
	 * Persiste um lançamento na base de dados.
	 * 
	 * @param registro
	 * @return Registro
	 */
	Registro persistir(Registro registro);
	
	/**
	 * Remove um lançamento da base de dados.
	 * 
	 * @param id
	 */
	void remover(Long id);
	
}
