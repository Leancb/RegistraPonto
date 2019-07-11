package registraponto.api.repositories;

import static org.junit.Assert.assertEquals;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import registraponto.api.entities.Firma;
import registraponto.api.entities.Colaborador;
import registraponto.api.entities.Registro;
import registraponto.api.enums.PerfilEnum;
import registraponto.api.enums.TipoEnum;
import registraponto.api.utils.PasswordUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class RegistroRepositoryTest {
	
	@Autowired
	private RegistroRepository registroRepository;
	
	@Autowired
	private ColaboradorRepository colaboradorRepository;
	
	@Autowired
	private FirmaRepository firmaRepository;
	
	private Long colaboradorId;

	@Before
	public void setUp() throws Exception {
		Firma firma = this.firmaRepository.save(getDadosFirma());
		
		Colaborador colaborador = this.colaboradorRepository.save(getDadosColaborador(firma));
		this.colaboradorId = colaborador.getId();
		
		this.registroRepository.save(getDadosRegs(colaborador));
		this.registroRepository.save(getDadosRegs(colaborador));
	}

	@After
	public void tearDown() throws Exception {
		this.firmaRepository.deleteAll();
	}

	@Test
	public void testGetRegistrosPorColaboradorId() {
		List<Registro> registros = this.registroRepository.findByColaboradorId(colaboradorId);
		
		assertEquals(2, registros.size());
	}
	
	@Test
	public void testGetRegistrosPorColaboradorIdPaginado() {
		PageRequest page = new PageRequest(0, 10);
		Page<Registro> registros = this.registroRepository.findByColaboradorId(colaboradorId, page);
		
		assertEquals(2, registros.getTotalElements());
	}
	
	private Registro getDadosRegs(Colaborador colaborador) {
		Registro lancameto = new Registro();
		lancameto.setData(new Date());
		lancameto.setTipo(TipoEnum.INICIO_ALMOCO);
		lancameto.setColaborador(colaborador);
		return lancameto;
	}

	private Colaborador getDadosColaborador(Firma firma) throws NoSuchAlgorithmException {
		Colaborador colaborador = new Colaborador();
		colaborador.setNome("Fulano de Tal");
		colaborador.setPerfil(PerfilEnum.ROLE_USUARIO);
		colaborador.setSenha(PasswordUtils.gerarBCrypt("123456"));
		colaborador.setCpf("24291173474");
		colaborador.setEmail("email@email.com");
		colaborador.setFirma(firma);
		return colaborador;
	}

	private Firma getDadosFirma() {
		Firma firma = new Firma();
		firma.setRazaoSocial("Firma de exemplo");
		firma.setCnpj("51463645000100");
		return firma;
	}

}
