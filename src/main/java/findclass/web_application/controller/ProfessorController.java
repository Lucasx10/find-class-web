package findclass.web_application.controller;

import jakarta.validation.Valid;
import findclass.web_application.domain.RegraDeNegocioException;
import findclass.web_application.domain.professor.DadosCadastroProfessor;
import findclass.web_application.domain.professor.DadosListagemProfessor;
import findclass.web_application.domain.professor.Especialidade;
import findclass.web_application.domain.professor.ProfessorService;
import findclass.web_application.domain.usuario.Perfil;
import findclass.web_application.domain.usuario.Usuario;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("professores")
public class ProfessorController {

    private static final String PAGINA_LISTAGEM = "professor/listagem-professores";
    private static final String PAGINA_CADASTRO = "professor/formulario-professor";
    private static final String REDIRECT_LISTAGEM = "redirect:/professores?sucesso";

    private final ProfessorService service;

    public ProfessorController(ProfessorService service) {
        this.service = service;
    }

    @ModelAttribute("especialidades")
    public Especialidade[] especialidades() {
        return Especialidade.values();
    }

    @GetMapping
    @PreAuthorize("hasRole('ATENDENTE') OR hasRole('ALUNO')")
    public String carregarPaginaListagem(@PageableDefault Pageable paginacao, Model model) {
        var professoresCadastrados = service.listar(paginacao);
        model.addAttribute("professores", professoresCadastrados);
        return PAGINA_LISTAGEM;
    }

    @GetMapping("formulario")
    @PreAuthorize("hasRole('ATENDENTE')")
    public String carregarPaginaCadastro(Long id, Model model) {
        if (id != null) {
            model.addAttribute("dados", service.carregarPorId(id));
        } else {
            model.addAttribute("dados", new DadosCadastroProfessor(null, "", "", "", "", null));
        }

        return PAGINA_CADASTRO;
    }

    @PostMapping
    @PreAuthorize("hasRole('ATENDENTE')")
    public String cadastrar(@Valid @ModelAttribute("dados") DadosCadastroProfessor dados, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("dados", dados);
            return PAGINA_CADASTRO;
        }

        try {
            service.cadastrar(dados);
            return REDIRECT_LISTAGEM;
        } catch (RegraDeNegocioException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("dados", dados);
            return PAGINA_CADASTRO;
        }
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ATENDENTE')")
    public String excluir(Long id) {
        service.excluir(id);
        return REDIRECT_LISTAGEM;
    }

    @GetMapping("{especialidade}")
    @ResponseBody
    public List<DadosListagemProfessor> listarProfessorsPorEspecialidade(@PathVariable String especialidade) {
        return service.listarPorEspecialidade(Especialidade.valueOf(especialidade));
    }

}
