# Sistema de Gestão de Estacionamento
Projeto dedicado ao desenvolvimento de um sistema de gestão de estacionamento em função do desafio 1 do programa de bolsas da Compass Uol Spring Boot - AWS - Ago/2024.

## Desafio proposto

### Descrição
Desenvolva um sistema de gestão de estacionamento para um shopping, utilizando apenas Java e MySQL. O sistema deve gerenciar o número de vagas disponíveis, registrar a entrada e saída de veículos por diferentes cancelas,
calcular o valor a ser pago pelo tempo de permanência e garantir que as regras específicas de entrada e saída sejam respeitadas. 
O sistema também deve permitir o cadastro de veículos mensalistas e caminhões de entrega, enquanto veículos avulsos e de serviço público não precisam ser previamente
cadastrados.

### Requisitos do desafio

1. **Cadastro de Veículos e Categorias:**

   O sistema deve suportar o cadastro de veículos nas seguintes categorias:
   
   - **Mensalista:** Veículo com um plano de estacionamento recorrente. As placas dos veículos mensalistas devem ser registradas previamente para abrir automaticamente as cancelas.
   
   - **Caminhões de Entrega:** Veículos de carga, como caminhões de entrega e vans. As placas desses veículos devem ser registradas previamente para abrir automaticamente as cancelas.
   
   - **Serviço Público:** Veículos de serviço público, como ambulâncias e viaturas, que têm acesso livre, não ocupam vagas e são isentos de cobrança. Esses veículos não precisam ser cadastrados.
   
   - **Avulso:** Veículos que não precisam de cadastro prévio. Recebem um ticket na entrada, que será utilizado para registrar a saída e calcular o valor a ser pago.

2. **Tipos de Veículos:**

   - Carros de Passeio
   - Motos
   - Caminhões de Entrega
   - Veículos de Serviço Público

3. **Gestão das Cancelas:**

   - Cancelas de entrada são numeradas de 1 a 5.
   - Cancelas de saída são numeradas de 6 a 10.
   - Mensalistas e Avulsos podem entrar por qualquer cancela de entrada (1 a 5) e sair por qualquer cancela de saída (6 a 10).
   - Caminhões de Entrega só podem entrar pela cancela 1 e sair por qualquer cancela de saída (6 a 10).
   - Motos só podem entrar pela cancela 5 e sair pela cancela 10.
   - Veículos de Serviço Público têm acesso livre em todas as cancelas sem cobrança.

4. **Gestão de Vagas:**

   - O sistema deve controlar o número total de vagas disponíveis, numerando-as para facilitar o gerenciamento.
   - O limite máximo de vagas disponíveis no estacionamento é 500 vagas.
   - Reserve 200 vagas para os mensalistas.
   - Moto ocupa 1 vaga, Carro de Passeio 2 vagas, Caminhão de Entrega 4 vagas.

5. **Registro de Entrada e Saída através de Tickets:**

   - **Mensalistas e Caminhões de Entrega:** A entrada e saída são registradas automaticamente com base na placa do veículo. Não é necessário ticket.
   
   - **Avulsos:** Recebem um ticket ao entrar no estacionamento, que registra:
     - A hora de entrada.
     - A hora de saída (registrada no momento da saída).
     - A cancela de entrada e saída.
     - A vaga ocupada (número da vaga).
     - O valor a ser pago (calculado no momento da saída).
   
   - Veículos de Serviço Público não recebem ticket.

6. **Cobrança:**

   - Implementar um sistema de cálculo de valor baseado no tempo que o veículo permaneceu no estacionamento, por vaga ocupada.
   - Valor por minuto estacionado: R$ 0,10.
   - Cobrança mínima de R$ 5,00 para veículos avulsos.
   - Mensalistas pagam uma taxa fixa mensal de R$ 250,00.
   - Veículos de Serviço Público são isentos de cobrança.


## Requisitos
Certifique-se de ter as seguintes ferramentas instaladas e configuradas:
- Java Development Kit (JDK) 11 ou superior
- Apache Maven
- MySQL 8.0.34 (LTS)
- MySQL Workbench 8.0

**IDE utilizada no desenvolvimento: IntelliJ IDEA Community Edition 2024.2**

## Execução do Projeto
Siga os passos abaixo para configurar o projeto no seu ambiente
1. **Clone o repositório**
```bash
 git clone https://github.com/ABeatrizSC/sistema-gestao-estacionamento.git 
 ```
```bash
  cd sistema-gestao-estacionamento 
 ```

2. **Compile o projeto**

 ```bash
 mvn clean install
 ```

 3. **Execute o projeto**
 Para executar o projeto, use o comando:

 ```bash
 mvn exec:java -Dexec.mainClass="com.exemplo.Main"
 ```

### Criação do banco de dados 
No MySQL Workbench, execute os seguintes códigos SQL para criar o banco de dados completo do sistema:

```sql
CREATE TABLE IF NOT EXISTS `vehicles` (   
    `id` INT NOT NULL PRIMARY KEY AUTO_INCREMENT,   
    `category` VARCHAR(100) NULL,   
    `slotSize` INT NULL,   
    `accessType` VARCHAR(50) NULL,   
    `entranceGate` INT NULL,   
    `entranceGatesAvailable` VARCHAR(100) NULL,   
    `exitGate` INT NULL,   
    `exitGatesAvailable` VARCHAR(100) NULL 
);

CREATE TABLE IF NOT EXISTS `monthlyPayer` (   
    `id` INT NOT NULL PRIMARY KEY AUTO_INCREMENT,   
    `licensePlate` VARCHAR(100) NULL,   
    `valuePerMonth` DOUBLE NULL,   
    `vehicle_id` INT NOT NULL
);

ALTER TABLE `monthlyPayer` 
ADD FOREIGN KEY (`vehicle_id`) REFERENCES `vehicles`(`id`);

CREATE TABLE IF NOT EXISTS `deliveryTruck` (   
    `id` INT NOT NULL PRIMARY KEY AUTO_INCREMENT,   
    `licensePlate` VARCHAR(100) NULL,   
    `vehicle_id` INT NOT NULL
);

ALTER TABLE `deliveryTruck` 
ADD FOREIGN KEY (`vehicle_id`) REFERENCES `vehicles`(`id`);

CREATE TABLE IF NOT EXISTS `parkingSpace` (
    `id` INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `isOccupied` BIT(2) NULL,
    `slotType` VARCHAR(100) NULL,
    `vehicle_id` INT 
);

ALTER TABLE `parkingSpace` 
ADD FOREIGN KEY (`vehicle_id`) REFERENCES `vehicles`(`id`);

CREATE TABLE IF NOT EXISTS `ticket` (
    `id` INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `startHour` TIME NULL,
    `finishHour` TIME NULL,
    `totalValue` DOUBLE NULL,
    `parkingSpaces` VARCHAR(100) NULL,
    `vehicle_id` INT NOT NULL
);

ALTER TABLE `ticket` 
ADD FOREIGN KEY (`vehicle_id`) REFERENCES `vehicles`(`id`);
```

### Criação das 500 vagas
Como o desafio propôs o gerenciamento de 500 vagas, o código abaixo irá criar automaticamente as 500 vagas no banco de dados.
Para isso, comente todo o código que está dentro do método main, cole o código abaixo e rode o projeto uma vez.
Depois, basta removê-lo e retirar o comentário dos outros códigos.
 ```java
ParkingSpace monthlyParkingSpace = new ParkingSpace(null, SlotType.MONTHLY, false, null);
ParkingSpace casualParkingSpace = new ParkingSpace(null, SlotType.CASUAL, false, null);
ParkingSpaceDao parkingSpaceDao = createParkingSpaceDao();
for (int i = 1; i <= 200; i++) {
    parkingSpaceDao.insert(monthlyParkingSpace);
}
for (int i = 1; i <= 300; i++) {
    parkingSpaceDao.insert(casualParkingSpace);
}
 ```

### Utilizando o Sistema
Para utilizar o sistema, o todo o menu é composto por opções que poderão ser selecionadas a partir de uma letra referenciada antes da opção ou então personalizadas de acordo com a opção desejada pelo usuário.
Todas os campos possuem validação, onde:
1. **Veículos**
  - Só serão categorias de veículos existentes no sistema (CAR, MOTOCYCLE, DELIVERY_TRUCKS e PUBLIC_SERVICE)
  - Se a categoria escolhida for CAR e MOTOCYCLE, o usuário é encaminhado para escolher os tipos de acessos disponíveis (MONTHLY PAYER ou TICKET (avulso))
  - Se não, os marcados como PUBLIC SERVICE serão encaminhados diretamente à escolha das vagas e os de DELIVERY TRUCKS para se registrar ou fazer login

2. **Placas**
    - Apenas as categorias de acesso MONTHLY PAYER e DELIVERY TRUCK poderão fazer o registro de placas
    -  Não é possível criar veículos com a mesma placa
    -  As placas deverao ser de tamanho 7 a 8, e todas ao convertidas para caixa alta ao serem salvas no banco

2. **Cancelas**
    - As cancelas possuem validação para permitirem a entrada somente de veículos autorizados a passarem por elas (detalhes na descrição do desafio)
    - Se a primeira opção marcada for 'Entering', na hora de escolher as cancelas só aparecerão as responsáveis pela entrada de veículo. O mesmo acontece quando selecionado a opção "Exiting"


### Contribuindo
Se você quiser contribuir para este projeto, siga as etapas a
1. Fork o repositório.
2. Crie uma branch para suas alterações (`git checkout -b min
3. Faça as alterações e commit (`git commit -am 'Adiciona nova
4. Push para a branch (`git push origin minha-nova-feature`).
5. Abra um Pull Request.

## 📲 Contato
Para dúvidas ou problemas, entre em contato em:
* Email: anabeatrizscarmoni@gmail.com
* GitHub: github.com/ABeatrizSC
