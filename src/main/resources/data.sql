
INSERT INTO user (userId, active, email, firstName, lastName, password,roles,failed_attempt,account_locked)
VALUES ('4028808e7ed2f2ab017ed2f41f930000',true,'abiswa15@asu.edu','Avirup','Biswas','$2a$10$WJU5Xe5BGmy2upG.53RVp.x1nNkv1yAWkQ6s2PhHNgvztbgYiTchu','ADMIN',0,false) 
ON DUPLICATE KEY UPDATE FirstName='Avirup';
