    package com.fiap.N.I.B.Repositories;

    import com.fiap.N.I.B.model.Usuario;
    import org.springframework.data.jpa.repository.JpaRepository;

    import java.time.LocalDate;
    import java.util.List;
    import java.util.Optional;

    public interface UsuarioRepository extends JpaRepository<Usuario, String> {

        Optional<Usuario> findByCpfUser(String cpf);
        List<Usuario> findUsuariosByPlanoUser(String planoUser);
        List<Usuario> findUsuariosByDataNascimentoUser(LocalDate dataNascimentoUser);
    }
