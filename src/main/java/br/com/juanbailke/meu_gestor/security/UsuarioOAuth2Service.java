package br.com.juanbailke.meu_gestor.security;

import br.com.juanbailke.meu_gestor.model.Usuario;
import br.com.juanbailke.meu_gestor.model.enums.ProvedorLogin;
import br.com.juanbailke.meu_gestor.repository.UsuarioRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioOAuth2Service extends DefaultOAuth2UserService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioOAuth2Service(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public OAuth2User loadUser(@NonNull OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String nome = oAuth2User.getAttribute("name");
        String idGoogle = oAuth2User.getAttribute("sub");
        String fotoPerfil = oAuth2User.getAttribute("picture");

        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(email);

        if (usuarioExistente.isEmpty()) {
            Usuario novoUsuario = new Usuario();
            novoUsuario.setEmail(email);
            novoUsuario.setNome(nome);
            novoUsuario.setIdProvedor(idGoogle);
            novoUsuario.setUrlFotoPerfil(fotoPerfil);
            novoUsuario.setProvedorLogin(ProvedorLogin.GOOGLE);

            usuarioRepository.save(novoUsuario);
        }

        return oAuth2User;
    }
}
