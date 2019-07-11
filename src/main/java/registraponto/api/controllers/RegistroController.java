package registraponto.api.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import registraponto.api.dtos.RegistroDto;
import registraponto.api.entities.Colaborador;
import registraponto.api.entities.Registro;
import registraponto.api.enums.TipoEnum;
import registraponto.api.response.Response;
import registraponto.api.services.ColaboradorService;
import registraponto.api.services.RegistroService;

@RestController
@RequestMapping("/api/registros")
@CrossOrigin(origins = "*")
public class RegistroController {

	private static final Logger log = LoggerFactory.getLogger(RegistroController.class);
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private RegistroService registroService;

	@Autowired
	private ColaboradorService colaboradorService;
	
	@Value("${paginacao.qtd_por_pagina}")
	private int qtdPorPagina;

	public RegistroController() {
	}

	/**
	 * Retorna a listagem de lançamentos de um funcionário.
	 * 
	 * @param colaboradorId
	 * @return ResponseEntity<Response<RegistroDto>>
	 */
	@GetMapping(value = "/colaborador/{colaboradorId}")
	public ResponseEntity<Response<Page<RegistroDto>>> listarPorColaboradorId(
			@PathVariable("colaboradorId") Long colaboradorId,
			@RequestParam(value = "pag", defaultValue = "0") int pag,
			@RequestParam(value = "ord", defaultValue = "id") String ord,
			@RequestParam(value = "dir", defaultValue = "DESC") String dir) {
		log.info("Buscando lançamentos por ID do funcionário: {}, página: {}", colaboradorId, pag);
		Response<Page<RegistroDto>> response = new Response<Page<RegistroDto>>();

		PageRequest pageRequest = new PageRequest(pag, this.qtdPorPagina, Direction.valueOf(dir), ord);
		Page<Registro> registros = this.registroService.getByColaboradorId(colaboradorId, pageRequest);
		Page<RegistroDto> registrosDto = registros.map(registro -> this.converterRegistroDto(registro));

		response.setData(registrosDto);
		return ResponseEntity.ok(response);
	}

	/**
	 * Retorna um lançamento por ID.
	 * 
	 * @param id
	 * @return ResponseEntity<Response<RegistroDto>>
	 */
	@GetMapping(value = "/{id}")
	public ResponseEntity<Response<RegistroDto>> listarPorId(@PathVariable("id") Long id) {
		log.info("Buscando lançamento por ID: {}", id);
		Response<RegistroDto> response = new Response<RegistroDto>();
		Optional<Registro> registro = this.registroService.getById(id);

		if (!registro.isPresent()) {
			log.info("Lançamento não encontrado para o ID: {}", id);
			response.getErrors().add("Lançamento não encontrado para o id " + id);
			return ResponseEntity.badRequest().body(response);
		}

		response.setData(this.converterRegistroDto(registro.get()));
		return ResponseEntity.ok(response);
	}

	/**
	 * Adiciona um novo lançamento.
	 * 
	 * @param registro
	 * @param result
	 * @return ResponseEntity<Response<RegistroDto>>
	 * @throws ParseException 
	 */
	@PostMapping
	public ResponseEntity<Response<RegistroDto>> adicionar(@Valid @RequestBody RegistroDto registroDto,
			BindingResult result) throws ParseException {
		log.info("Adicionando lançamento: {}", registroDto.toString());
		Response<RegistroDto> response = new Response<RegistroDto>();
		validarColaborador(registroDto, result);
		Registro registro = this.converterDtoParaRegistro(registroDto, result);

		if (result.hasErrors()) {
			log.error("Erro validando lançamento: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		registro = this.registroService.persistir(registro);
		response.setData(this.converterRegistroDto(registro));
		return ResponseEntity.ok(response);
	}

	/**
	 * Atualiza os dados de um lançamento.
	 * 
	 * @param id
	 * @param registroDto
	 * @return ResponseEntity<Response<Registro>>
	 * @throws ParseException 
	 */
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<RegistroDto>> atualizar(@PathVariable("id") Long id,
			@Valid @RequestBody RegistroDto registroDto, BindingResult result) throws ParseException {
		log.info("Atualizando lançamento: {}", registroDto.toString());
		Response<RegistroDto> response = new Response<RegistroDto>();
		validarColaborador(registroDto, result);
		registroDto.setId(Optional.of(id));
		Registro registro = this.converterDtoParaRegistro(registroDto, result);

		if (result.hasErrors()) {
			log.error("Erro validando lançamento: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		registro = this.registroService.persistir(registro);
		response.setData(this.converterRegistroDto(registro));
		return ResponseEntity.ok(response);
	}

	/**
	 * Remove um lançamento por ID.
	 * 
	 * @param id
	 * @return ResponseEntity<Response<Registro>>
	 */
	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<String>> remover(@PathVariable("id") Long id) {
		log.info("Removendo lançamento: {}", id);
		Response<String> response = new Response<String>();
		Optional<Registro> registro = this.registroService.getById(id);

		if (!registro.isPresent()) {
			log.info("Erro ao remover devido ao lançamento ID: {} ser inválido.", id);
			response.getErrors().add("Erro ao remover lançamento. Registro não encontrado para o id " + id);
			return ResponseEntity.badRequest().body(response);
		}

		this.registroService.remover(id);
		return ResponseEntity.ok(new Response<String>());
	}

	/**
	 * Valida um funcionário, verificando se ele é existente e válido no
	 * sistema.
	 * 
	 * @param registroDto
	 * @param result
	 */
	private void validarColaborador(RegistroDto registroDto, BindingResult result) {
		if (registroDto.getColaboradorId() == null) {
			result.addError(new ObjectError("colaborador", "Funcionário não informado."));
			return;
		}

		log.info("Validando funcionário id {}: ", registroDto.getColaboradorId());
		Optional<Colaborador> colaborador = this.colaboradorService.getById(registroDto.getColaboradorId());
		if (!colaborador.isPresent()) {
			result.addError(new ObjectError("colaborador", "Funcionário não encontrado. ID inexistente."));
		}
	}

	/**
	 * Converte uma entidade lançamento para seu respectivo DTO.
	 * 
	 * @param registro
	 * @return RegistroDto
	 */
	private RegistroDto converterRegistroDto(Registro registro) {
		RegistroDto registroDto = new RegistroDto();
		registroDto.setId(Optional.of(registro.getId()));
		registroDto.setData(this.dateFormat.format(registro.getData()));
		registroDto.setTipo(registro.getTipo().toString());
		registroDto.setDescricao(registro.getDescricao());
		registroDto.setLocalizacao(registro.getLocalizacao());
		registroDto.setColaboradorId(registro.getColaborador().getId());

		return registroDto;
	}

	/**
	 * Converte um RegistroDto para uma entidade Registro.
	 * 
	 * @param registroDto
	 * @param result
	 * @return Registro
	 * @throws ParseException 
	 */
	private Registro converterDtoParaRegistro(RegistroDto registroDto, BindingResult result) throws ParseException {
		Registro registro = new Registro();

		if (registroDto.getId().isPresent()) {
			Optional<Registro> lanc = this.registroService.getById(registroDto.getId().get());
			if (lanc.isPresent()) {
				registro = lanc.get();
			} else {
				result.addError(new ObjectError("registro", "Lançamento não encontrado."));
			}
		} else {
			registro.setColaborador(new Colaborador());
			registro.getColaborador().setId(registroDto.getColaboradorId());
		}

		registro.setDescricao(registroDto.getDescricao());
		registro.setLocalizacao(registroDto.getLocalizacao());
		registro.setData(this.dateFormat.parse(registroDto.getData()));

		if (EnumUtils.isValidEnum(TipoEnum.class, registroDto.getTipo())) {
			registro.setTipo(TipoEnum.valueOf(registroDto.getTipo()));
		} else {
			result.addError(new ObjectError("tipo", "Tipo inválido."));
		}

		return registro;
	}

}
