package findclass.web_application.controller;

import jakarta.validation.Valid;
import findclass.web_application.domain.RegraDeNegocioException;
import findclass.web_application.domain.aula.AulaService;
import findclass.web_application.domain.aula.DadosAgendamentoAula;
import findclass.web_application.domain.professor.Especialidade;
import findclass.web_application.domain.usuario.Usuario;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("aulas")
public class AulaController {

    private static final String PAGINA_LISTAGEM = "aula/listagem-aulas";
    private static final String PAGINA_CADASTRO = "aula/formulario-aula";
    private static final String REDIRECT_LISTAGEM = "redirect:/aulas?sucesso";

    private final AulaService service;

    public AulaController(AulaService aulaService){
        this.service = aulaService;
    }

    @ModelAttribute("especialidades")
    public Especialidade[] especialidades() {
        return Especialidade.values();
    }

    @GetMapping
    public String carregarPaginaListagem(@PageableDefault Pageable paginacao, Model model, @AuthenticationPrincipal Usuario logado) {
        var aulasAtivas = service.listar(paginacao, logado);
        model.addAttribute("aulas", aulasAtivas);
        return PAGINA_LISTAGEM;
    }

    @GetMapping("formulario")
    @PreAuthorize("hasRole('ATENDENTE') OR hasRole('ALUNO')")
    public String carregarPaginaAgendaAula(Long id, Model model) {
        if (id != null) {
            model.addAttribute("dados", service.carregarPorId(id));
        } else {
            model.addAttribute("dados", new DadosAgendamentoAula(null, null, null, null, null));
        }

        return PAGINA_CADASTRO;
    }

    @PostMapping
    @PreAuthorize("hasRole('ATENDENTE') || hasRole('ALUNO')")
    public String cadastrar(@Valid @ModelAttribute("dados") DadosAgendamentoAula dados, BindingResult result, Model model, @AuthenticationPrincipal Usuario logado) {
        if (result.hasErrors()) {
            model.addAttribute("dados", dados);
            return PAGINA_CADASTRO;
        }

        try {
            service.cadastrar(dados, logado);
            return REDIRECT_LISTAGEM;
        } catch (RegraDeNegocioException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("dados", dados);
            return PAGINA_CADASTRO;
        }
    }

    @DeleteMapping
    public String excluir(Long id) {
        service.excluir(id);
        return REDIRECT_LISTAGEM;
    }

}
