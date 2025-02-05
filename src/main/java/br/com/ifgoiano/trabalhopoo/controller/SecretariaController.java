package br.com.ifgoiano.trabalhopoo.controller;

import br.com.ifgoiano.trabalhopoo.models.Aluno;
import br.com.ifgoiano.trabalhopoo.models.Disciplina;
import br.com.ifgoiano.trabalhopoo.models.Professor;
import br.com.ifgoiano.trabalhopoo.services.LoginServices;
import br.com.ifgoiano.trabalhopoo.services.SecretariaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class SecretariaController {

    final LoginServices loginServices;
    final SecretariaService secretariaService;

    @Autowired
    public SecretariaController(LoginServices loginServices, SecretariaService secretariaService) {
        this.loginServices = loginServices;
        this.secretariaService = secretariaService;
    }

    @GetMapping("/secretaria")
    public String secretaria() {
        return "secretaria";
    }

    @GetMapping("/registrarProfessor")
    public String registerProfessor(Model model){
        model.addAttribute("professor", new Professor());
        return "cadastrarProfessorPage";
    }

    @GetMapping("/registrarAluno")
    public String registrerAluno(Model model){
        model.addAttribute("aluno", new Aluno());
        return "cadastrarAlunoPage";
    }

    @GetMapping("/registrarAlunoDisciplina")
    public String registerStudentSubject(Model model, Model model2){
        model.addAttribute("aluno", new Aluno());
        model2.addAttribute(    "disciplina", new Disciplina());
        return "cadastrarAlunoDisciplina";
    }

    @PostMapping("/registrarProfessor")
    public String registerProfessor(@ModelAttribute("professor")Professor professor, Error error){
        if (loginServices.existsProfessor(professor.getEmail(), professor.getIdUser())){
            error.addSuppressed(new Throwable("Usuario já cadastrado"));
            return "/secretaria";
        }

        secretariaService.setRegisterProfessor(professor);
        return "redirect:/secretaria";
    }

    @PostMapping("/registrarAluno")
    public String registerStudent(@ModelAttribute("aluno") Aluno aluno, Error error){
        if (loginServices.existsAluno(aluno.getEmail(), aluno.getIdUser())){
            error.addSuppressed(new Throwable("Usuário já cadastrado"));
            return "redirect:/secretaria";
        }

        secretariaService.setRegisterAluno(aluno);
        return "redirect:/secretaria";
    }

    @PostMapping("/registrarAlunoDisciplina")
    public String registerStudentSubject(@ModelAttribute("disciplina") @Valid Disciplina disciplina,
                                         @ModelAttribute("aluno") @Valid Aluno aluno,
                                         Error error) {
        if (loginServices.existsById(aluno.getIdUser()) && secretariaService.existsBySubjectName(disciplina.getSubjectName())) {
            secretariaService.setAlunoDisciplina(disciplina.getSubjectName(), aluno.getIdUser());
            return "redirect:/secretaria";
        }
        System.out.println(disciplina.getSubjectName());
        System.out.println(aluno.getIdUser());

        error.addSuppressed(new Throwable("Aluno ou Disciplina inexistente"));
        return "redirect:/secretaria";
    }
}
