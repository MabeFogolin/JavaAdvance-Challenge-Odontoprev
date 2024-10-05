package com.fiap.N.I.B.gateways.Endereco;

import com.fiap.N.I.B.domains.Endereco;
import com.fiap.N.I.B.domains.Profissional;
import com.fiap.N.I.B.domains.Usuario;
import com.fiap.N.I.B.gateways.requests.EnderecoPatch;
import com.fiap.N.I.B.gateways.responses.EnderecoPostResponse;
import com.fiap.N.I.B.gateways.responses.HistoricoPostResponse;
import com.fiap.N.I.B.usecases.Endereco.*;
import com.fiap.N.I.B.usecases.Profissional.ProfissionalRepository;
import com.fiap.N.I.B.usecases.Usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EnderecoServiceImpl implements EnderecoDeletar, EnderecoAtualiarParcial, EnderecoAtualizarTotalmente, EnderecoTodos, EnderecoPorProfissional, EnderecoCriar, EnderecoPorUsuario {

    private final UsuarioRepository usuarioRepository;
    private final ProfissionalRepository profissionalRepository;
    private final EnderecoRepository enderecoRepository;


    @Override
    public Optional<Endereco> atualizarParcial(String idPessoa, EnderecoPatch enderecoPatch) {
        Optional<Endereco> enderecoExistenteUsuario = enderecoRepository.findByUsuario_CpfUser(idPessoa);
        Optional<Endereco> enderecoExistenteProfissional = enderecoRepository.findByProfissional_RegistroProfissional(idPessoa);

        if (enderecoExistenteUsuario.isPresent()) {
            Endereco enderecoAtualizado = enderecoExistenteUsuario.get();

            if (enderecoPatch.getRuaEndereco() != null) {
                enderecoAtualizado.setRuaEndereco(enderecoPatch.getRuaEndereco());
            }
            if (enderecoPatch.getNumeroEndereco() != null) {
                enderecoAtualizado.setNumeroEndereco(enderecoPatch.getNumeroEndereco());
            }
            if (enderecoPatch.getComplementoEndereco() != null) {
                enderecoAtualizado.setComplementoEndereco(enderecoPatch.getComplementoEndereco());
            }
            if (enderecoPatch.getBairroEndereco() != null) {
                enderecoAtualizado.setBairroEndereco(enderecoPatch.getBairroEndereco());
            }
            if (enderecoPatch.getCidadeEndereco() != null) {
                enderecoAtualizado.setCidadeEndereco(enderecoPatch.getCidadeEndereco());
            }
            if (enderecoPatch.getCepEndereco() != null) {
                enderecoAtualizado.setCepEndereco(enderecoPatch.getCepEndereco());
            }
            if (enderecoPatch.getEstadoEndereco() != null) {
                enderecoAtualizado.setEstadoEndereco(enderecoPatch.getEstadoEndereco());
            }

            return Optional.of(enderecoRepository.save(enderecoAtualizado));
        } else if (enderecoExistenteProfissional.isPresent()) {

            Endereco enderecoAtualizado = enderecoExistenteProfissional.get();

            if (enderecoPatch.getRuaEndereco() != null) {
                enderecoAtualizado.setRuaEndereco(enderecoPatch.getRuaEndereco());
            }
            if (enderecoPatch.getNumeroEndereco() != null) {
                enderecoAtualizado.setNumeroEndereco(enderecoPatch.getNumeroEndereco());
            }
            if (enderecoPatch.getComplementoEndereco() != null) {
                enderecoAtualizado.setComplementoEndereco(enderecoPatch.getComplementoEndereco());
            }
            if (enderecoPatch.getBairroEndereco() != null) {
                enderecoAtualizado.setBairroEndereco(enderecoPatch.getBairroEndereco());
            }
            if (enderecoPatch.getCidadeEndereco() != null) {
                enderecoAtualizado.setCidadeEndereco(enderecoPatch.getCidadeEndereco());
            }
            if (enderecoPatch.getCepEndereco() != null) {
                enderecoAtualizado.setCepEndereco(enderecoPatch.getCepEndereco());
            }
            if (enderecoPatch.getEstadoEndereco() != null) {
                enderecoAtualizado.setEstadoEndereco(enderecoPatch.getEstadoEndereco());
            }
            return Optional.of(enderecoRepository.save(enderecoAtualizado));
        }

        else{
            return  Optional.empty();
        }
    }

    @Override
    public Optional<Endereco> atualizarTotalmente(String idPessoa, Endereco endereco) {
        Optional<Endereco> enderecoExistenteUsuario = enderecoRepository.findByUsuario_CpfUser(idPessoa);
        Optional<Endereco> enderecoExistenteProfissional = enderecoRepository.findByProfissional_RegistroProfissional(idPessoa);

        if (enderecoExistenteUsuario.isPresent()) {
            Endereco enderecoAtualizado = enderecoExistenteUsuario.get();

            enderecoAtualizado.setRuaEndereco(endereco.getRuaEndereco());
            enderecoAtualizado.setNumeroEndereco(endereco.getNumeroEndereco());
            enderecoAtualizado.setComplementoEndereco(endereco.getComplementoEndereco());
            enderecoAtualizado.setBairroEndereco(endereco.getBairroEndereco());
            enderecoAtualizado.setCidadeEndereco(endereco.getCidadeEndereco());
            enderecoAtualizado.setCepEndereco(endereco.getCepEndereco());
            enderecoAtualizado.setEstadoEndereco(endereco.getEstadoEndereco());
            return Optional.of(enderecoRepository.save(enderecoAtualizado));

        } else if (enderecoExistenteProfissional.isPresent()) {
            Endereco enderecoAtualizado = enderecoExistenteProfissional.get();

            enderecoAtualizado.setRuaEndereco(endereco.getRuaEndereco());
            enderecoAtualizado.setNumeroEndereco(endereco.getNumeroEndereco());
            enderecoAtualizado.setComplementoEndereco(endereco.getComplementoEndereco());
            enderecoAtualizado.setBairroEndereco(endereco.getBairroEndereco());
            enderecoAtualizado.setCidadeEndereco(endereco.getCidadeEndereco());
            enderecoAtualizado.setCepEndereco(endereco.getCepEndereco());
            enderecoAtualizado.setEstadoEndereco(endereco.getEstadoEndereco());
        }
        return Optional.empty();
    }

    @Override
    public EnderecoPostResponse criarEndereco(String idPessoa, Endereco endereco) {
        Optional<Usuario> usuario = usuarioRepository.findUsuarioByCpfUser(idPessoa);
        Optional<Profissional> profissional = profissionalRepository.findProfissionalByRegistroProfissional(idPessoa);

        if(usuario.isPresent()){
            endereco.setUsuario(usuario.get());
            enderecoRepository.save(endereco);
            return new EnderecoPostResponse("Endereço do usuário cadastrado", endereco);
        } else if (profissional.isPresent()) {
            endereco.setProfissional(profissional.get());
            enderecoRepository.save(endereco);
            return new EnderecoPostResponse("Endereço do profissional cadastrado", endereco);
        } else{
            return new EnderecoPostResponse("Cadastro não realizado, revise as informações de usuário/profissional", null);
        }
    }

    @Override
    public boolean deletarEndereco(String idPessoa) {
        Optional<Endereco> enderecoExistenteUsuario = enderecoRepository.findByUsuario_CpfUser(idPessoa);
        Optional<Endereco> enderecoExistenteProfissional = enderecoRepository.findByProfissional_RegistroProfissional(idPessoa);

        if(enderecoExistenteUsuario.isPresent()){
            return enderecoRepository.findByUsuario_CpfUser(idPessoa)
                    .map(enderecoDelete -> {
                        enderecoRepository.delete(enderecoDelete);
                        return true;
                    }).orElse(false);
        } else {
            return enderecoRepository.findByProfissional_RegistroProfissional(idPessoa)
                    .map(enderecoDelete -> {
                        enderecoRepository.delete(enderecoDelete);
                        return true;
                    }).orElse(false);
        }

    }

    @Override
    public Optional<Endereco> buscarEnderecoPorProfissional(String registroProfissional) {
        return enderecoRepository.findByProfissional_RegistroProfissional(registroProfissional);
    }

    @Override
    public Optional<Endereco> buscarEnderecoPorUsuario(String cpfUser) {
        return enderecoRepository.findByUsuario_CpfUser(cpfUser);
    }

    @Override
    public List<Endereco> listarTodos() {
        return enderecoRepository.findAll();
    }
}
