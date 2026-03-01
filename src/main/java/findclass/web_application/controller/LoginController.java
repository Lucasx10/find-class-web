package findclass.web_application.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import findclass.web_application.domain.RegraDeNegocioException;
import findclass.web_application.domain.usuario.DadosAlteracaoSenha;
import findclass.web_application.domain.usuario.Usuario;
import findclass.web_application.domain.usuario.UsuarioService;
import jakarta.validation.Valid;

@Controller
public class LoginController {
    private final UsuarioService service;

    private static final String FORMULARIO_ALTERAR_SENHA = "autenticacao/formulario-alterar-senha";

    public LoginController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping("/login")
    public String carregaPaginaLogin(){
        return "autenticacao/login";
    }

    @GetMapping("/alterar-senha")
    public String carregaPaginaAlteracaoSenha(){
        return FORMULARIO_ALTERAR_SENHA;
    }

    @PostMapping("/alterar-senha")
    public String alterarSenha(@Valid @ModelAttribute("dados") DadosAlteracaoSenha dados, BindingResult result, Model model, @AuthenticationPrincipal Usuario logado) {
        if (result.hasErrors()) {
            model.addAttribute("dados", dados);
            return FORMULARIO_ALTERAR_SENHA;
        }

        try {
            service.alterarSenha(dados, logado);
            return "redirect:home";
        } catch (RegraDeNegocioException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("dados", dados);
            return FORMULARIO_ALTERAR_SENHA;
        }
    }
}
