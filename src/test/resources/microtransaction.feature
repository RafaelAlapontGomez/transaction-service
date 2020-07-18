Feature: create transaction and bussines rules
  Scenario: Create transaction using post method
		Given A new transaction
		|	{ "reference":"12345A",	"accountIban":"ES9820385778983000760260", "date":"2019-07-16T16:55:42.000Z",	"amount":193.38, "fee":3.18, "description":"Restaurant payment"	}	    |
		When I invoke a method post
		Then The system store the transaction in our system
		
  Scenario: transaction is not stored in our system
		Given A transaction that is not stored in our system
		When I check the status from any channel
		Then The system returns the status INVALID
		
  Scenario: CASE B A transaction that is stored in our system
		Given CASEB A transaction that is stored in our system with this payload
		| reference | channel |
		| 00002A    | CLIENT  |
		When CASEB I check the status from CLIENT or ATM channel
		 And CASEB the transaction date is before today
		Then CASEB The system returns the above expected
		| reference | status   | 
		| 00002A    | SETTLED  | 
		 And CASEB the amount substracting the fee
		 | amount   |  fee     |
		 | 1093.38  |  13.18   |     
  
  Scenario: CASEC A transaction that is stored in our system
		Given CASEC A transaction that is stored in our system with this payload
		| reference | channel  |
		| 00003A    | INTERNAL |
		When CASEC I check the status from INTERNAL channel
		 And CASEC the transaction date is before today
		Then CASEC The system returns the above expected
		| reference | status   | 
		| 00003A    | SETTLED  | 
		 And CASEC the amoount
		 | amount   |
		 | -193.38  |     
		 And CASEC the fee
		 | fee  |
		 | 0.0  |     

  Scenario: CASE D A transaction that is stored in our system
		Given CASED A transaction that is stored in our system with this payload
		| reference | channel |
		| 00001A    | ATM     |
		When CASED I check the status from CLIENT or ATM channel
		 And CASED the transaction date is today
		Then CASED The system returns the above expected
		| reference | status   | 
		| 00001A    | PENDING  | 
		 And CASED the amount substracting the fee
		 | amount  |  fee     |
		 | 193.38  |  6.18   |     
  
  Scenario: CASEE A transaction that is stored in our system
		Given CASEE A transaction that is stored in our system with this payload
		| reference | channel  |
		| 00006A    | INTERNAL |
		When CASEE I check the status from INTERNAL channel
		 And CASEE the transaction date istoday
		Then CASEE The system returns the above expected
		| reference | status   | 
		| 00006A    | PENDING  | 
		 And CASEE the amoount
		 | amount   |
		 | -19.38   |     
		 And CASEE the fee
		 | fee     |
		 | 1.18    |     

  Scenario: CASE F A transaction that is stored in our system
		Given CASEF A transaction that is stored in our system with this payload
		| reference | channel |
		| 00005A    | CLIENT  |
		When CASEF I check the status from CLIENT channel
		 And CASEF the transaction date is after today
		Then CASEF The system returns the above expected
		| reference | status   | 
		| 00005A    | FUTURE   | 
		 And CASEF the amount substracting the fee
		 | amount  |  fee     |
		 | 193.38  | 3.18     |     
  
  Scenario: CASE G A transaction that is stored in our system
		Given CASEG A transaction that is stored in our system with this payload
		| reference | channel |
		| 00005A    | ATM     |
		When CASEG I check the status from ATM channel
		 And CASEG the transaction date is after today
		Then CASEG The system returns the above expected
		| reference | status   | 
		| 00005A    | PENDING  | 
		 And CASEG the amount substracting the fee
		 | amount  |  fee     |
		 | 193.38  | 3.18     |     
  
  Scenario: CASEH A transaction that is stored in our system
		Given CASEH A transaction that is stored in our system with this payload
		| reference | channel  |
		| 00004A    | INTERNAL |
		When CASEH I check the status from INTERNAL channel
		 And CASEH the transaction date is after today
		Then CASEH The system returns the above expected
		| reference | status   | 
		| 00004A    | FUTURE   | 
		 And CASEH the amoount
		 | amount   |
		 | 1093.38  |     
		 And CASEH the fee
		 | fee  |
		 | 5.18 |     
		 		    	