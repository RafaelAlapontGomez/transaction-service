create sequence IF NOT EXISTS reference_seq;

insert into transaction (id, ACCOUNT_IBAN, AMOUNT, DATE, DESCRIPTION, FEE, REFERENCE)
values(hibernate_sequence.NEXTVAL,'ES9820385778983000760236', 193.38, sysdate,
       'Restaurant payment', 6.18,  lpad(REFERENCE_SEQ.nextval, 5, '0') || 'A');
insert into transaction (id, ACCOUNT_IBAN, AMOUNT, DATE, DESCRIPTION, FEE, REFERENCE)
values(hibernate_sequence.NEXTVAL,'ES9820385778983000760240', 1093.38, sysdate - 2,
       'Payroll', 13.18,  lpad(REFERENCE_SEQ.nextval, 5, '0') || 'A');
insert into transaction (id, ACCOUNT_IBAN, AMOUNT, DATE, DESCRIPTION, FEE, REFERENCE)
values(hibernate_sequence.NEXTVAL,'ES9820385778983000760250', -193.38, sysdate - 5,
       'School', 0,  lpad(REFERENCE_SEQ.nextval, 5, '0') || 'A');
insert into transaction (id, ACCOUNT_IBAN, AMOUNT, DATE, DESCRIPTION, FEE, REFERENCE)
values(hibernate_sequence.NEXTVAL,'ES9820385778983000760236', 1093.38, sysdate + 2,
       'Restaurant payment', 5.18,  lpad(REFERENCE_SEQ.nextval, 5, '0') || 'A');
insert into transaction (id, ACCOUNT_IBAN, AMOUNT, DATE, DESCRIPTION, FEE, REFERENCE)
values(hibernate_sequence.NEXTVAL,'ES9820385778983000760250', 193.38, sysdate + 5,
      'Gasoline', 3.18,  lpad(REFERENCE_SEQ.nextval, 5, '0') || 'A');
insert into transaction (id, ACCOUNT_IBAN, AMOUNT, DATE, DESCRIPTION, FEE, REFERENCE)
values(hibernate_sequence.NEXTVAL,'ES9820385778983000760236',  -19.38, sysdate,
      'Pay taxes', 1.18,  lpad(REFERENCE_SEQ.nextval, 5, '0') || 'A');

insert into account (id, ACCOUNT_IBAN, TOTAL_ACCOUNT_BALANCE)
VALUES (hibernate_sequence.NEXTVAL, 'ES9820385778983000760260', 100000.00);
insert into account (id, ACCOUNT_IBAN, TOTAL_ACCOUNT_BALANCE)
VALUES (hibernate_sequence.NEXTVAL, 'ES9820385778983000760236', 1000.00);
insert into account (id, ACCOUNT_IBAN, TOTAL_ACCOUNT_BALANCE)
VALUES (hibernate_sequence.NEXTVAL, 'ES9820385778983000760240', 100.00);
insert into account (id, ACCOUNT_IBAN, TOTAL_ACCOUNT_BALANCE)
VALUES (hibernate_sequence.NEXTVAL, 'ES9820385778983000760250', 10.00);
