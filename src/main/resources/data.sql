


insert into place(id, name) values('2e4baf1c-5acb-4efb-a1af-eddada31b00f', 'Tashkent');
insert into place(id, name) values('2e4baf1c-5acb-4efb-a1af-eddada31baaa', 'Seoul');
insert into place(id, name) values('2e4baf1c-5acb-4efb-a1af-eddada31baab', 'Moscow');
insert into place(id, name) values('2e4baf1c-5acb-4efb-a1af-eddada31baac', 'NewYork');


insert into user (user_id, user_name) values ('3ede0ef2-92b7-4817-a5f3-0c575361f745', 'Rustam');
insert into user (user_id, user_name) values ('3ede0ef2-92b7-4817-a5f3-0c575361f746', 'Dorothy');
insert into user (user_id, user_name) values ('3ede0ef2-92b7-4817-a5f3-0c575361f747', 'Anna');
insert into user (user_id, user_name) values ('3ede0ef2-92b7-4817-a5f3-0c575361f748', 'Kim');

insert into place(id, name) values('pl_id_1', 'pl_name_1');
insert into user(user_id, user_name) values('user_id_1', 'user_name_1');

insert into review (id, place_id, user_id, content) values ('rw_id_1', 'pl_id_1', 'user_id_1', 'content1');


insert into point_history (id, description, point, review_place_id, review_user_id) values ('pt_id_1', 'descr1', 1, 'pl_id_1', 'user_id_1');
insert into point_history (id, description, point, review_place_id, review_user_id) values ('pt_id_2', 'descr1', 1, 'pl_id_1', 'user_id_1');