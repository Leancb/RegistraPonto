package registraponto.api.security.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import registraponto.api.entities.Colaborador;
import registraponto.api.security.JwtUserFactory;
import registraponto.api.services.ColaboradorService;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private ColaboradorService colaboradorService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Colaborador> colaborador = colaboradorService.getByEmail(username);

		if (colaborador.isPresent()) {
			return JwtUserFactory.create(colaborador.get());
		}

		throw new UsernameNotFoundException("Email não encontrado.");
	}

}
