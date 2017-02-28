-- Oracle
delete from t_standardday where calendar in (select id from t_calendar where calendartype <> 'Eight_TimeZone');
DELETE from t_calendar  where calendartype <> 'Eight_TimeZone';

declare 
  crow t_calendar%rowtype;
  cursor crows is select id, domainid from t_calendar where calendarname='Standard_Calendar';
  begin
  for crow in crows loop
    update t_user  set calendar = crow.id where domainid = crow.domainid;
    dbms_output.put_line(crow.Id||','||crow.domainid||','||crows%rowcount);
  end loop;
  commit;
end;

UPDATE t_superuser set loginpwd = '123';
UPDATE t_superuser set loginpwd = 'teemlink' where loginno = 'admin';

update t_resource set type ='100', mobileico='001' WHERE description ='Mobile';
update t_resource set type ='100', mobileico='111' WHERE superior in (select id FROM t_resource where description ='Mobile');


update t_user set levels = 0;
update t_view set readonly = 0;
update t_view set viewtype=1;

-- 2010年1月6日

-- MY SQL
alter table t_user_role_set drop primary key; 
alter table t_user_role_set change ROLEID ROLEID varchar(255);
alter table t_user_role_set change USERID USERID varchar(255);

alter table t_user_department_set drop primary key; 
alter table t_user_department_set change DEPARTMENTID DEPARTMENTID varchar(255);
alter table t_user_department_set change USERID USERID varchar(255);

-- Oracle
ALTER TABLE T_USER_ROLE_SET DROP CONSTRAINT "SYS_C0012485"; --主键约束对应每个数据库有所不同
ALTER TABLE T_USER_ROLE_SET MODIFY ("ROLEID" NULL);
ALTER TABLE T_USER_ROLE_SET MODIFY ("USERID" NULL);

ALTER TABLE T_USER_DEPARTMENT_SET DROP CONSTRAINT "SYS_C0022606"; --主键约束对应每个数据库有所不同
ALTER TABLE T_USER_DEPARTMENT_SET MODIFY ("DEPARTMENTID" NULL);
ALTER TABLE T_USER_DEPARTMENT_SET MODIFY ("USERID" NULL);