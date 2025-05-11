<h1 align="left"> Challenge Odontoprev: Java Advanced</h1>
<p align="left">O projeto N.I.B. visa desenvolver um aplicativo que permite aos segurados da Odontoprev monitorar suas rotinas diárias, coletando informações sobre hábitos de saúde e bem-estar. Utilizando modelos de inteligência artificial, o aplicativo prevê a probabilidade de acionamento de sinistros para procedimentos odontológicos mais custosos.</p>

<h2 align="left">Benefícios do Aplicativo</h2>
<ul>
  <li>🩺 <strong>Monitoramento de Saúde:</strong> Acompanhe o cotidiano do seu usuário, bem como as condições de sua saúde bucal.</li>
  <li>⚠️ <strong>Prevenção e Intervenção:</strong> Identifique padrões e anomalias para intervenções precoces.</li>
  <li>📱 <strong>Acesso Facilitado:</strong> Acesso prático e personalizado a dados de saúde.</li>
  <li>🤖 <strong>Dados para Inteligência Artificial:</strong> Geração de base de dados para modelos de IA que atendam necessidades da empresa.</li>
</ul>

<h2 align="left">Objetivos do Projeto</h2>
<ul>
  <li>📊 <strong>Monitoramento de Rotinas:</strong> Registro das atividades diárias dos usuários.</li>
  <li>🔍 <strong>Identificação de Tendências:</strong> Análise de dados para reconhecer perfis propensos a intervenções de alto custo.</li>
  <li>🔒 <strong>Autenticidade e Confiabilidade:</strong> Verificação cruzada realizada por profissionais de odontologia.</li>
</ul>

<h2 align="left">Tecnologias Utilizadas</h2>
<ul>
  <li>⚙️ <strong>Backend:</strong> Java com Spring.</li>
  <li>🐄️ <strong>Banco de Dados:</strong> Oracle.</li>
  <li>🧠 <strong>Inteligência Artificial:</strong> Modelos de machine learning com Python para análise de dados.</li>
  <li>📱 <strong>Aplicativo:</strong> Construção de aplicativo de tela com a linguagem Kotlin.</li>
</ul>

<h2 align="left"> 📹 Link do vídeo</h2>

```bash
https://www.youtube.com/watch?v=4L5RpHhTqTg
```

<h2 align="left"> 🏠 Relacionamentos: Visão banco</h2>

<h3 align="left"> 🏠 Modelo Lógico </h3>
  <img src="Imagens/2TDSPR_NIB_Logico.png">

<h3 align="left"> 🏠 Modelo Físico </h3>
  <img src="Imagens/2TDSPR_NIB_Fisico.png">

<h2 align="left">	🧮 Testes 🧮</h2>

<h3 align="left"> ℹ️ Instruções</h3>

<p align="left">Acesse a pasta N.I.B via terminal e, use o seguinte comando:

```bash
mvn clean package
```

<p align="left">Após a mensagem de sucesso do build, digite os seguintes comandos via terminal:

```bash
docker-compose up --build
```

<p align="left">Assim que finalizar a construção do container, acesse a seguinte url para visualizar aas informações da aplicação via Prometheus:

```bash
http://localhost:9090/targets
```

<h3 align="left"> 💂 Security</h3>

<p align="left">Na página do login, use a seguinte credencial</p>
<ul>
  <li><strong>Username: </strong>admin</li>
  <li><strong>Password: </strong>admin123</li>
</ul>

<h3 align="left"> 🧙‍♀️ Usuário</h3>
<ul>
  <li><strong>Nome:</strong> Usuário</li>
  <li><strong>Sobrenome:</strong> Testte</li>
  <li><strong>CPF:</strong> 98684948009</li>
  <li><strong>Telefone:</strong> 1234567890</li>
  <li><strong>E-mail:</strong> maria.fogolin@example.com</li>
  <li><strong>Data de Nascimento:</strong> 01/01/1990</li>
  <li><strong>Plano:</strong> Premium</li>
</ul>

<h3 align="left"> 👷‍♀️ Profissional</h3>
<ul>
  <li><strong>Nome:</strong> João</li>
  <li><strong>Sobrenome:</strong> Silva</li>
  <li><strong>Telefone:</strong> 11987654321</li>
  <li><strong>E-mail:</strong> joao.silva@exemplo.com</li>
  <li><strong>Data de cadastro:</strong> 27/09/2024</li>
  <li><strong>Registro do Profissional:</strong> 0123456789</li>
  <li><strong>Tipo do profissional:</strong> Dentista</li>
</ul>

<h3 align="left"> 📅 Diário</h3>
<ul>
  <li><strong>Data do Registro:</strong> 03/10/2024</li>
  <li><strong>Escovação:</strong> 1 vez ao dia</li>
  <li><strong>Uso de fio dental:</strong> 1 vez ao dia</li>
  <li><strong>Uso de enxaguante:</strong> 1 vez ao dia</li>
  <li><strong>Sintoma relatado:</strong> Registro com mês diferente</li>
</ul>

<h3 align="left"> 🏥 Consulta</h3>
<ul>
  <li><strong>Data da Consulta:</strong> 02/10/2024</li>
  <li><strong>Descrição:</strong> Consulta de teste</li>
  <li><strong>Registro do Profissional:</strong> 1234567890</li>
  <li><strong>CPF do Usuário:</strong> 92712116003</li>
</ul>

<h2 align="left"> 🤝 Integrantes</h2>
<ul>
  <li>Igor Gabriel Pereira Marcondes <strong>RM 553544</strong></li>
  <li>Maria Beatriz Reis Fogolin de Godoy <strong>RM 552669</strong></li>
  <li>Nicholas Pereira Paulo Lima Barbosa <strong>RM 552744</strong></li>
</ul>
