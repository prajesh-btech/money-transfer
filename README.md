# money-transfer
A Java RESTful API for money transfer between accounts

### Technologies :
- JAX-RS API 2.27
- Java 8
- Junit 4.12

### How to run:

1. start the HttpJdkServer 

Application starts a JdkHttp server on localhost port 9998, The sample data stored in memory

- http://localhost:9998/accounts
- http://localhost:9998/accounts/1
- http://localhost:9998/transactions
- http://localhost:9998/transactions/101

### Available Services
| HTTP METHOD	|  PATH	                       |  USAGE                  |
|-------------|------------------------------|-------------------------|
|GET	        |/accounts	                   | get all the accounts    |
|GET	        |/accounts/{accountNumber}     | get single account      |
|PUT	        |/accounts	                   | update account          |
|POST	        |/accounts	                   | create new account      |
|DELETE	      |/accounts                     | delete account          |
|GET	        |/transactions	               | get all the transactions|
|GET	        |/transactions/{transactionId} | get single transaction  |
|POST	        |/transactions                 | make new transaction    |

### Sample JSON outputs :
#### Accounts
[{"accountNumber":1,"name":"Rajesh","balance":100},{"accountNumber":2,"name":"Kris","balance":200}]

#### Account:
{"accountNumber":1,"name":"Rajesh","balance":100}

#### Transactions :

[{"transactionId":101,"sourceAcctNumber":1,"destAcctNumber":2,"amount":50}]





