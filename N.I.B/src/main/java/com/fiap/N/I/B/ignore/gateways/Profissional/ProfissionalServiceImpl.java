package com.fiap.N.I.B.ignore.gateways.Profissional;

import com.fiap.N.I.B.model.Profissional;
import com.fiap.N.I.B.ignore.gateways.requests.ProfissionalPatch;
import com.fiap.N.I.B.ignore.gateways.responses.ProfissionalPostResponse;
import com.fiap.N.I.B.Repositories.ProfissionalRepository;
import com.fiap.N.I.B.ignore.usecases.Profissional.ProfissionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfissionalServiceImpl implements ProfissionalService {

    private final ProfissionalRepository profissionalRepository;


    @Override
    public ProfissionalPostResponse criarProfissional(Profissional profissionalParaCriar) {
        Optional<Profissional> profissionalBusca = profissionalRepository.findProfissionalByRegistroProfissional(profissionalParaCriar.getRegistroProfissional());
        if(profissionalBusca.isEmpty()){
            profissionalRepository.save(profissionalParaCriar);
            return new ProfissionalPostResponse("Novo profissional cadastrado", profissionalParaCriar);
        }
        return new ProfissionalPostResponse("Registro profissional já cadastrado no sistema", profissionalParaCriar);
    }

    @Override
    public Optional<Profissional> buscarProfissional(String registroProfissional) {
        return profissionalRepository.findProfissionalByRegistroProfissional(registroProfissional);
    }

    @Override
    public List<Profissional> buscarTodos() {
        return profissionalRepository.findAll();
    }

    @Override
    public List<Profissional> buscarPorCategoria(String tipoProfissional) {
        return profissionalRepository.findUsuariosByTipoProfissional(tipoProfissional);
    }

    @Override
    public Optional<Profissional> atualizarProfissional(String registroProfissional, Profissional profissionalParaAtualizar) {
        return profissionalRepository.findProfissionalByRegistroProfissional(registroProfissional).map(profissional ->{
            profissional.setNomeProfissional(profissionalParaAtualizar.getNomeProfissional());
            profissional.setSobrenomeProfissional(profissionalParaAtualizar.getSobrenomeProfissional());
            profissional.setEmailProfissional(profissionalParaAtualizar.getEmailProfissional());
            profissional.setRegistroProfissional(profissional.getRegistroProfissional());
            profissional.setTelefoneProfissional(profissional.getTelefoneProfissional());
            profissional.setDataInscricaoProfissional(profissionalParaAtualizar.getDataInscricaoProfissional());
            profissional.setTipoProfissional(profissionalParaAtualizar.getTipoProfissional());
            return  profissionalRepository.save(profissional);
        });
    }

    @Override
    public boolean deletarProfissional(String registroProfissional) {
        return profissionalRepository.findProfissionalByRegistroProfissional(registroProfissional)
                .map(profissional -> {
                    profissionalRepository.delete(profissional);
                    return true;
                }).orElse(false);
    }


    @Override
    public Optional<Profissional> atualizarEmailTelefone(String registroProfissional, ProfissionalPatch profissionalPatch) {
        Optional<Profissional> profissionalExistente = profissionalRepository.findProfissionalByRegistroProfissional(registroProfissional);

        if (profissionalExistente.isPresent()) {
            Profissional profissionalNovo = profissionalExistente.get();

            profissionalNovo.setEmailProfissional(profissionalPatch.getEmailProfissional());
            profissionalNovo.setTelefoneProfissional(profissionalPatch.getTelefoneProfissional());

           Profissional profissionalAtualizado = profissionalRepository.save(profissionalNovo);

            return Optional.of(profissionalAtualizado);
        } else {
            return Optional.empty();
        }
    }

//    @Override
//    public boolean deletarProfissionalProcedure(String registroProfissional) {
//        Optional<Profissional> profissional = profissionalRepository.findProfissionalByRegistroProfissional(registroProfissional);
//        if (profissional.isPresent()) {
//            try {
//                profissionalRepository.deletarProfissionalProcedure(registroProfissional);
//                return true;
//            } catch (Exception e) {
//                System.err.println("Erro ao deletar profissional: " + e.getMessage());
//                return false;
//            }
//        }
//        return false;
//    }
}
