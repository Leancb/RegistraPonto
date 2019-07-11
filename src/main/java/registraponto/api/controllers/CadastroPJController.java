package registraponto.api.controllers;

import java.security.NoSuchAlgorithmException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import registraponto.api.dtos.CadastroPJDto;
import registraponto.api.entities.Firma;
import registraponto.api.entities.Colaborador;
import registraponto.api.enums.PerfilEnum;
import registraponto.api.response.Response;
import registraponto.api.services.FirmaService;
import registraponto.api.services.ColaboradorService;
import registraponto.api.utils.PasswordUtils;

@RestController
@RequestMapping("/api/cadastrar-pj")
@CrossOrigin(origins = "*")
public class CadastroPJController {

	private static final Logger log = LoggerFactory.getLogger(CadastroPJController.class);

	@Autowired
	private ColaboradorService colaboradorService;

	@Autowired
	private FirmaService firmaService;

	public CadastroPJController() {
	}

	/**
	 * Cadastra uma pessoa jurídica no sistema.
	 * 
	 * @param cadastroPJDto
	 * @param result
	 * @return ResponseEntity<Response<CadastroPJDto>>
	 * @throws NoSuchAlgorithmException
	 */
	@PostMapping
	public ResponseEntity<Response<CadastroPJDto>> cadastrar( @Valid @RequestBody CadastroPJDto cadastroPJDto,
															  BindingResult result) throws NoSuchAlgorithmException {
		log.info("Cadastrando PJ: {}", cadastroPJDto.toString());
		Response<CadastroPJDto> response = new Response<CadastroPJDto>();

		validarDadosExistentes(cadastroPJDto, result);
		Firma firma = this.converterDtoParaFirma(cadastroPJDto);
		Colaborador colaborador = this.converterDtoParaColaborador(cadastroPJDto, result);

		if (result.hasErrors()) {
			log.error("Erro validando dados de cadastro PJ: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		this.firmaService.persistir(firma);
		colaborador.setFirma(firma);
		this.colaboradorService.persistir(colaborador);

		response.setData(this.converterCadastroPJDto(colaborador));
		return ResponseEntity.ok(response);
	}

	/**
	 * Verifica se a firma ou funcionário já existem na base de dados.
	 * 
	 * @param cadastroPJDto
	 * @param result
	 */
	private void validarDadosExistentes(CadastroPJDto cadastroPJDto, BindingResult result) {
		this.firmaService.getByCnpj(cadastroPJDto.getCnpj())
				.ifPresent(emp -> result.addError(new ObjectError("firma", "Firma já existente.")));

		this.colaboradorService.getByCpf(cadastroPJDto.getCpf())
				.ifPresent(func -> result.addError(new ObjectError("colaborador", "CPF já existente.")));

		this.colaboradorService.getByEmail(cadastroPJDto.getEmail())
				.ifPresent(func -> result.addError(new ObjectError("colaborador", "Email já existente.")));
	}

	/**
	 * Converte os dados do DTO para firma.
	 * 
	 * @param cadastroPJDto
	 * @return Firma
	 */
	private Firma converterDtoParaFirma(CadastroPJDto cadastroPJDto) {
		Firma firma = new Firma();
		firma.setCnpj(cadastroPJDto.getCnpj());
		firma.setRazaoSocial(cadastroPJDto.getRazaoSocial());

		return firma;
	}

	/**
	 * Converte os dados do DTO para funcionário.
	 * 
	 * @param cadastroPJDto
	 * @param result
	 * @return Colaborador
	 * @throws NoSuchAlgorithmException
	 */
	private Colaborador converterDtoParaColaborador(CadastroPJDto cadastroPJDto, BindingResult result)
			throws NoSuchAlgorithmException {
		Colaborador colaborador = new Colaborador();
		colaborador.setNome(cadastroPJDto.getNome());
		colaborador.setEmail(cadastroPJDto.getEmail());
		colaborador.setCpf(cadastroPJDto.getCpf());
		colaborador.setPerfil(PerfilEnum.ROLE_ADMIN);
		colaborador.setSenha(PasswordUtils.gerarBCrypt(cadastroPJDto.getSenha()));

		return colaborador;
	}

	/**
	 * Popula o DTO de cadastro com os dados do funcionário e firma.
	 * 
	 * @param colaborador
	 * @return CadastroPJDto
	 */
	private CadastroPJDto converterCadastroPJDto(Colaborador colaborador) {
		CadastroPJDto cadastroPJDto = new CadastroPJDto();
		cadastroPJDto.setId(colaborador.getId());
		cadastroPJDto.setNome(colaborador.getNome());
		cadastroPJDto.setEmail(colaborador.getEmail());
		cadastroPJDto.setCpf(colaborador.getCpf());
		cadastroPJDto.setRazaoSocial(colaborador.getFirma().getRazaoSocial());
		cadastroPJDto.setCnpj(colaborador.getFirma().getCnpj());

		return cadastroPJDto;
	}

}
