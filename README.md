## Getting Started

Bem-vindo ao meu Projeto da disciplina de Sistemas Distribuidos
Proprietario: Gabriel Leonardo Martins de Oliveira
RA: a2317940

## Folder Structure

`Bibliotecas`:
    gson-2.10.1 - biblioteca para trabalhar com Json
    jBCrypt-0.4.1 - biblioteca para utilizar hash e guardar a senha no banco de dados
    mongo-java-driver - conector com o banco de dados MongoDB

`Classes`:
    Server: classe que roda o servidor
    ServerTreatment: classe para tratar as funcoes do Server
    Client: classe que roda o cliente
    User: classe entidade que representa o usuario e que executa todas as funcoes e chama conexao com banco
    UserDB: classe que faz a comunicacao com o banco de dados
    Database: conexao com o banco de dados
    CaesarCrypt: classe para encriptografia da senha para que transite de forma segura do cliente ao servidor
    
## Banco de dados
    O banco de dados utilizado foi o MongoDB, o conector dele esta definido na pasta libs
