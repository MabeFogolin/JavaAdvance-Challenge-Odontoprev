    package com.fiap.N.I.B.gateways.Repositories;

    import com.fiap.N.I.B.domains.Usuario;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.query.Procedure;

    import java.time.LocalDate;
    import java.util.List;
    import java.util.Optional;

    public interface UsuarioRepository extends JpaRepository<Usuario, String> {

        Optional<Usuario> findByCpfUser(String cpf);
        List<Usuario> findUsuariosByPlanoUser(String planoUser);
        List<Usuario> findUsuariosByDataNascimentoUser(LocalDate dataNascimentoUser);
    }
