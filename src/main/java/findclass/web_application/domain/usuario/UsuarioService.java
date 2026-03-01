package findclass.web_application.domain.usuario;

import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import findclass.web_application.domain.RegraDeNegocioException;
import findclass.web_application.domain.usuario.email.EmailService;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder encriptador;

    private final EmailService emailService;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder encriptador, EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.encriptador = encriptador;
        this.emailService = emailService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("O usuário não foi encontrado!"));
    }

    public Long salvarUsuario(String nome, String email, Perfil perfil) { 
        String primeiraSenha = UUID.randomUUID().toString().substring(0, 8);
        System.out.println("Senha gerada: " + primeiraSenha);
        String senhaCriptografada = encriptador.encode(primeiraSenha);
        var usuario =usuarioRepository.save(new Usuario(nome, email, senhaCriptografada, perfil));
        return usuario.getId();
    }

    public void excluir(Long id) {
        usuarioRepository.deleteById(id);
    }

    public void alterarSenha(DadosAlteracaoSenha dados, Usuario logado){
        if(!encriptador.matches(dados.senhaAtual(), logado.getPassword())){
            throw new RegraDeNegocioException("Senha digitada não confere com senha atual!");
        }

        if(!dados.novaSenha().equals(dados.novaSenhaConfirmacao())){
            throw new RegraDeNegocioException("Senha e confirmação não conferem!");
        }

        String senhaCriptografada = encriptador.encode(dados.novaSenha());
        logado.alterarSenha(senhaCriptografada);

        logado.setSenhaAlterada(true);

        usuarioRepository.save(logado);
    }

    public void enviarToken(String email) {
        var usuario = usuarioRepository.findByEmailIgnoreCase(email).orElseThrow(
            () -> new RegraDeNegocioException("E-mail não encontrado!")
        );

        String token = UUID.randomUUID().toString();
        usuario.setToken(token);
        usuario.setExpiracaoToken(java.time.LocalDateTime.now().plusMinutes(15));
        usuarioRepository.save(usuario);

        emailService.enviarEmailSenha(usuario);
    }
    
    private static void atualizarUsuarioSpringSecurity(Usuario logado) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var newAuth = new UsernamePasswordAuthenticationToken(logado, null, authentication.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    public void recuperarConta(String codigo, DadosRecuperacaoConta dados) {
        Usuario usuario = usuarioRepository.findByTokenIgnoreCase(codigo)
            .orElseThrow(
                    () -> new RegraDeNegocioException("Código de recuperação inválido!")
            );
        if(usuario.getExpiracaoToken().isBefore(java.time.LocalDateTime.now())){
            throw new RegraDeNegocioException("Código de recuperação expirado!");
        }

        if(!dados.novaSenha().equals(dados.novaSenhaConfirmacao())){
            throw new RegraDeNegocioException("Senha e confirmação não conferem!");
        }

        String senhaCriptografada = encriptador.encode(dados.novaSenha());
        usuario.alterarSenha(senhaCriptografada);

        usuario.setToken(null);
        usuario.setExpiracaoToken(null);
        usuarioRepository.save(usuario);
    }

}
