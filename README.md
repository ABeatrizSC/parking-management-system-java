# Sistema de Gestão de Estacionamento
Projeto dedicado ao desenvolvimento de um sistema de gestão de estacionamento em função do desafio 1 do programa de bolsas da Compass Uol AWS_SPRINGBOOT_AGO/24.

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
### Criação do banco de dados no MySQL Workbench
```sql
CREATE DATABASE `parking-management-system-java-db`;
 ```

### Clonagem e execução
Siga os passos abaixo para configurar o projeto no seu ambiente
1. **Clone o repositório**
```bash
 git clone https://github.com/ABeatrizSC/parking-management-system-java
.git 
 ```
```bash
  cd parking-management-system-java
 
 ```

2. **Instale as dependências**

 ```bash
 mvn clean install
 ```

 3. **Execute o projeto**

### Criação das 500 vagas
Como o desafio propôs o gerenciamento de 500 vagas, uma forma que pensei em criá-las rapidamente foi copiando o código feito abaixo e executá-lo somente uma vez na classe Main:

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

## Script do banco de dados
O FlyWay no momento da execução é responsável por criar as tabelas abaixo automaticamente:

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

CREATE TABLE IF NOT EXISTS `monthlyPayers` (   
    `id` INT NOT NULL PRIMARY KEY AUTO_INCREMENT,   
    `licensePlate` VARCHAR(100) NULL,   
    `valuePerMonth` DOUBLE NULL,   
    `vehicle_id` INT NOT NULL
);

ALTER TABLE `monthlyPayers` 
ADD FOREIGN KEY (`vehicle_id`) REFERENCES `vehicles`(`id`);

CREATE TABLE IF NOT EXISTS `deliveryTrucks` (   
    `id` INT NOT NULL PRIMARY KEY AUTO_INCREMENT,   
    `licensePlate` VARCHAR(100) NULL,   
    `vehicle_id` INT NOT NULL
);

ALTER TABLE `deliveryTrucks` 
ADD FOREIGN KEY (`vehicle_id`) REFERENCES `vehicles`(`id`);

CREATE TABLE IF NOT EXISTS `parkingSpaces` (
    `id` INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `isOccupied` BIT(2) NULL,
    `slotType` VARCHAR(100) NULL,
    `vehicle_id` INT 
);

ALTER TABLE `parkingSpaces` 
ADD FOREIGN KEY (`vehicle_id`) REFERENCES `vehicles`(`id`);

CREATE TABLE IF NOT EXISTS `tickets` (
    `id` INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `startHour` TIME NULL,
    `finishHour` TIME NULL,
    `totalValue` DOUBLE NULL,
    `parkingSpaces` VARCHAR(100) NULL,
    `vehicle_id` INT NOT NULL
);

ALTER TABLE `tickets` 
ADD FOREIGN KEY (`vehicle_id`) REFERENCES `vehicles`(`id`);
```

## Como navegar/Utilizar o sistema
O menu é composto por opções que poderão ser selecionadas a partir de um número referenciado antes da opção ou então por um campo personalizado de acordo com a opção desejada pelo usuário.

### Validações de entrada
1. **Veículos**
  - Só serão aceitas categorias de veículos existentes no sistema (CAR, MOTOCYCLE, DELIVERY_TRUCKS e PUBLIC_SERVICE);
  - Se a categoria escolhida for CAR e MOTOCYCLE, o usuário é encaminhado para escolher os tipos de acessos disponíveis a estes (MONTHLY PAYER ou TICKET (CASUAL/avulso));
  - Se não, os marcados como PUBLIC SERVICE serão encaminhados diretamente à escolha das vagas e os de categoria DELIVERY TRUCKS para se registrar ou fazer "login".

2. **Placas**
    - Apenas as categorias de acesso MONTHLY PAYER e DELIVERY TRUCK farão o registro de placas;
    -  Não é possível criar veículos com a mesma placa;
    -  As placas deverão ter de 7 à 8 caracteres.

3. **Cancelas**
    - As cancelas possuem validação para permitirem a entrada somente de veículos autorizados a passarem por elas (detalhes na descrição do desafio);
    - Se a primeira opção marcada for 'Entering', na hora de escolher as cancelas só aparecerão as responsáveis pelas entradas de veículos. O mesmo acontece quando selecionado a opção "Exiting".
    - 
4. **Vagas**
    - Cada categoria de veículo ocupa um determinado numero de vagas (descrito no desafio), ou seja, deverá informar o número necessário de vagas na hora de escolhê-las;
    - As vagas escolhidas deverão ser números **sequenciais**;
    - Sendo assim, não é possível adentrar com o veículo se não houver vagas da quantidade necessária OU se não forem sequenciais;
    - Não é possível veículos que são MONTHLY PAYERS estacionarem nas vagas de veículos CASUAL e vice-versa.

## Contato
* GitHub: [ABeatrizSC](https://github.com/ABeatrizSC)
* Linkedin: [Ana Beatriz Santucci Carmoni](www.linkedin.com/in/ana-carmoni)
* Email: [anabeatrizscarmoni@gmail.com](mailto:anabeatrizscarmoni@gmail.com)

