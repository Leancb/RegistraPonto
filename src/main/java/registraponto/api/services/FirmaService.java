package registraponto.api.services;

import java.util.Optional;

import registraponto.api.entities.Firma;

public interface FirmaService {

	/**
	 * Retorna uma firma dado um CNPJ.
	 * 
	 * @param cnpj
	 * @return Optional<Firma>
	 */
	Optional<Firma> getByCnpj(String cnpj);
	
	/**
	 * Cadastra uma nova firma na base de dados.
	 * 
	 * @param firma
	 * @return Firma
	 */
	Firma persistir(Firma firma);
	
}
