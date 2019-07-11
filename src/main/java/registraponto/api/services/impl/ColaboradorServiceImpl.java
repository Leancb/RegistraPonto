package registraponto.api.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import registraponto.api.entities.Colaborador;
import registraponto.api.repositories.ColaboradorRepository;
import registraponto.api.services.ColaboradorService;

import java.util.Optional;

@Service
public class ColaboradorServiceImpl implements ColaboradorService {
	
	private static final Logger log = LoggerFactory.getLogger(ColaboradorServiceImpl.class);

	@Autowired
	private ColaboradorRepository colaboradorRepository;
	
	public Colaborador persistir(Colaborador colaborador) {
		log.info("Persistindo funcionário: {}", colaborador);
		return this.colaboradorRepository.save(colaborador);
	}
	
	public Optional<Colaborador> getByCpf(String cpf) {
		log.info("Buscando funcionário pelo CPF {}", cpf);
		return Optional.ofNullable(this.colaboradorRepository.findByCpf(cpf));
	}
	
	public Optional<Colaborador> getByEmail(String email) {
		log.info("Buscando funcionário pelo email {}", email);
		return Optional.ofNullable(this.colaboradorRepository.findByEmail(email));
	}
	
	public Optional<Colaborador> getById(Long id) {
		log.info("Buscando funcionário pelo IDl {}", id);
		return Optional.ofNullable(this.colaboradorRepository.getOne(id));
	}

}
