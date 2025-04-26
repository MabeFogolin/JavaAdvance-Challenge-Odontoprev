package com.fiap.N.I.B;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fiap.N.I.B.Repositories.*;
import com.fiap.N.I.B.ignore.Endereco;
import com.fiap.N.I.B.ignore.EnderecoRepository;
import com.fiap.N.I.B.ignore.Historico;
import com.fiap.N.I.B.ignore.HistoricoRepository;
import com.fiap.N.I.B.model.*;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import jdk.jfr.Enabled;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@SpringBootApplication
@EnableAdminServer
@Configuration
public class Application {

	private final UsuarioRepository usuarioRepository;
	private final EnderecoRepository enderecoRepository;
	private final ProfissionalRepository profissionalRepository;
	private final HistoricoRepository historicoRepository;
	private final UsuarioSecurityRepository usuarioSecurityRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;


	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}


	@EventListener(value = ApplicationReadyEvent.class)
	public void setupDados() {
		LocalDate now = LocalDate.now();

		Usuario usuarioSalvo = Usuario.builder()
				.cpfUser("59997259092")
				.nomeUser("Teste inicial")
				.sobrenomeUser("Spring")
				.telefoneUser("1234567890")
				.dataNascimentoUser(LocalDate.of(1990, 1, 1))
				.planoUser("Premium")
				.emailUser("mariateste.fogolin@example.com")
				.diarios(new ArrayList<>())
				.consultas(new ArrayList<>())
				.build();
		usuarioSalvo = usuarioRepository.save(usuarioSalvo);


		Endereco endereco = Endereco.builder()
				.ruaEndereco("Rua 1")
				.numeroEndereco(101)
				.complementoEndereco(null)
				.bairroEndereco("Bairro 1")
				.cidadeEndereco("Cidade 1")
				.cepEndereco("12345-01")
				.estadoEndereco("SP")
				.usuario(usuarioSalvo)
				.build();

		endereco = enderecoRepository.save(endereco);
		usuarioSalvo.setEndereco(endereco);
		usuarioRepository.save(usuarioSalvo);

		Profissional profissional = Profissional.builder()
				.nomeProfissional("João")
				.sobrenomeProfissional("Silva")
				.telefoneProfissional("11987654321")
				.tipoProfissional("Dentista")
				.dataInscricaoProfissional(Date.valueOf(LocalDate.of(2024, 9, 27)))
				.registroProfissional("1234567890")
				.emailProfissional("joao.silva@exemplo.com")
				.consultas(new ArrayList<>())
				.build();
		profissional = profissionalRepository.save(profissional);

		Endereco endereco2 = Endereco.builder()
				.ruaEndereco("Rua 2")
				.numeroEndereco(102)
				.complementoEndereco("Bloco 2")
				.bairroEndereco("Bairro 2")
				.cidadeEndereco("Cidade 2")
				.cepEndereco("12345-02")
				.estadoEndereco("SP")
				.profissional(profissional)
				.build();
		endereco2 = enderecoRepository.save(endereco2);
		profissional.setEndereco(endereco2);
		profissionalRepository.save(profissional);


        Historico historico = Historico.builder()
				.tratamentoHistorico(1)
				.canalHistorico(1)
				.limpezaHistorico(0)
				.carieHistorico(0)
				.ortodonticoHistorico(1)
				.cirurgiaHistorico(1)
				.usuario(usuarioSalvo)
				.build();
		historicoRepository.save(historico);
		usuarioSalvo.setHistorico(historico);
		usuarioRepository.save(usuarioSalvo);

		Diario diario = Diario.builder()
				.dataRegistro(LocalDate.of(2024, 10, 3))
				.escovacaoDiario(1)
				.usoFioDiario(1)
				.usoEnxaguanteDiario(1)
				.sintomaDiario("Registro com mês diferente")
				.usuario(usuarioSalvo)
				.build();

		usuarioSalvo.adicionarDiario(diario);


		Consulta consulta = Consulta.builder()
				.dataConsulta(LocalDate.of(2024, 10, 2))
				.descricaoConsulta("Consulta de teste")
				.build();

		usuarioSalvo.adicionarConsulta(consulta);
		profissional.adicionarConsulta(consulta);

		usuarioRepository.save(usuarioSalvo);

		System.out.println("Usuário, profissional e consulta criados com sucesso.");
		RoleModel role = roleRepository.findByRoleName(RoleName.ROLE_USER)
				.orElseGet(() -> {
					RoleModel novaRole = new RoleModel();
					novaRole.setRoleName(RoleName.valueOf("ROLE_USER"));
					return roleRepository.save(novaRole);
				});

		UsuarioSecurity usuarioSecurity = new UsuarioSecurity();
		usuarioSecurity.setCpfUser("75481951096");
		usuarioSecurity.setNomeUser("Usuário Segurança");
		usuarioSecurity.setSobrenomeUser("Autenticado");
		usuarioSecurity.setTelefoneUser("11999999999");
		usuarioSecurity.setDataNascimentoUser(LocalDate.of(1995, 5, 5));
		usuarioSecurity.setPlanoUser("Free");
		usuarioSecurity.setEmailUser("auth.user@example.com");
		usuarioSecurity.setSenha(passwordEncoder.encode("123456"));
		usuarioSecurity.setRole(List.of(role));

		usuarioSecurityRepository.save(usuarioSecurity);

		System.out.println("✅ UsuarioSecurity criado com sucesso: auth.user@example.com | senha: 123456");

	}
}