CREATE TABLE cafeteria_order (
    id INTEGER PRIMARY KEY AUTO_INCREMENT
);

CREATE TABLE menu_item (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    menu_item_id INTEGER,
    id_order INTEGER,
    foreign key (id_order) references cafeteria_order(id)
);

