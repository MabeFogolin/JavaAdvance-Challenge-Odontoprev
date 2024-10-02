    package com.fiap.N.I.B.usecases.Usuario;

    import com.fiap.N.I.B.domains.Usuario;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.jpa.repository.JpaRepository;

    import java.sql.Date;
    import java.time.LocalDate;
    import java.util.List;
    import java.util.Optional;

    public interface UsuarioRepository extends JpaRepository<Usuario, String> {

        Optional<Usuario> findUsuarioByCpfUser(String cpfUser);
        List<Usuario> findUsuariosByPlanoUser(String planoUser);
        Page<Usuario> findUsuariosByPlanoUser(String planoUser, Pageable pageable);
        Page<Usuario> findUsuariosByDataNascimentoUser(LocalDate dataNascimentoUser, Pageable pageable);
        List<Usuario> findUsuariosByDataNascimentoUser(LocalDate dataNascimentoUser);

    }
