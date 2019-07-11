package registraponto.api.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import registraponto.api.entities.Registro;
import registraponto.api.repositories.RegistroRepository;
import registraponto.api.services.RegistroService;

import java.util.Optional;

@Service
public class RegistroServiceImpl implements RegistroService {

	private static final Logger log = LoggerFactory.getLogger(RegistroServiceImpl.class);

	@Autowired
	private RegistroRepository registroRepository;

	public Page<Registro> getByColaboradorId( Long colaboradorId, PageRequest pageRequest) {
		log.info("Buscando lançamentos para o funcionário ID {}", colaboradorId);
		return this.registroRepository.findByColaboradorId(colaboradorId, pageRequest);
	}
	
	@Cacheable("registroPorId")
	public Optional<Registro> getById(Long id) {
		log.info("Buscando um lançamento pelo ID {}", id);
		return Optional.ofNullable(this.registroRepository.getOne(id));
	}
	
	@CachePut("registroPorId")
	public Registro persistir(Registro registro) {
		log.info("Persistindo o lançamento: {}", registro);
		return this.registroRepository.save(registro);
	}
	
	public void remover(Long id) {
		log.info("Removendo o lançamento ID {}", id);
		this.registroRepository.deleteById(id);
	}

}
